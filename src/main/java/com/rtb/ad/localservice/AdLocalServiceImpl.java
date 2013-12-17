package com.rtb.ad.localservice;

import com.nd.rtb.dao.redis.ConfigException;
import com.nd.rtb.dao.redis.DBConfig;
import com.nd.rtb.dao.redis.DBConnection;
import com.nd.rtb.dao.redis.DBConnectionPool;
import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.tag.entity.TagEntity;
import com.rtb.config.RedisTableNames;
import com.rtb.key.localservice.RedisKeyLocalService;
import com.rtb.utils.KinitUtil;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquireRedisIndexContext;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.local.LocalServiceConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = AdLocalService.class,
        description = "广告操作内部接口")
public class AdLocalServiceImpl implements AdLocalService {

    @InjectRDao(clazz = AdEntity.class)
    private REntityDao<AdEntity> adEntityDao;
    //
    @InjectRDao(clazz = AdPointEntity.class)
    private REntityDao<AdPointEntity> adPointEntityDao;
    //
    @InjectRDao(clazz = AdBiddingEntity.class)
    private REntityDao<AdBiddingEntity> adBiddingEntityDao;
    //
    @InjectRDao(clazz = TagEntity.class)
    private REntityDao<TagEntity> adTagEntityDao;
    //
    @InjectLocalService()
    private RedisKeyLocalService redisKeyLocalService;
    //kakfa消息生产者
    private Producer<Integer, String> producer;
    //接收广告点击消息的topic
    private final String launchTopic = "AD_LAUNCH";
    //接收竞价消息的topic
    private final String biddingTopic = "AD_BIDDING";
    //hbase 广告表
    private HTable adHTable;
    //hbase 广告余额
    private final byte[] accountColumnFamily = Bytes.toBytes("ACCOUNT");
    private final byte[] balanceByte = Bytes.toBytes("balance");
    //rtb redis 集群线程池
    private DBConnectionPool redisPool;
    //imei tag表
    private final String RtbUserTag = "DMP_UserTags";
    //广告投放表
    private final String RtbAdWinner = "SSP_AdWinner";

    @Override
    public void init() {
        //初始化kafka
        this.initKafka();
        //初始化kinit
        KinitUtil.kinit();
        //初始化hbase
        Configuration config = HBaseConfiguration.create();
        try {
            this.adHTable = new HTable(config, "AD_DSP");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //初始化rtb redis 集群线程池
        try {
            DBConfig dbConfig = new DBConfig();
            this.redisPool = new DBConnectionPool(dbConfig);
        } catch (ConfigException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public AdEntity insertAndInquireAd(Map<String, String> parameterMap) {
        long adId = this.redisKeyLocalService.getNextKeyValue(RedisTableNames.RTB_AD);
        parameterMap.put("adId", Long.toString(adId));
        return this.adEntityDao.insertAndInquire(parameterMap);
    }

    @Override
    public AdEntity inquireAdByAdId(String adId) {
        return this.adEntityDao.inquireByKey(adId);
    }

    @Override
    public void updateAd(Map<String, String> parameterMap) {
        this.adEntityDao.update(parameterMap);
    }

    @Override
    public List<AdEntity> inquirePageByUserId(String userId, int pageIndex, int pageSize) {
        InquireRedisIndexContext context = new InquireRedisIndexContext("userId", userId);
        context.setPageIndex(pageIndex);
        context.setPageSize(pageSize);
        return this.adEntityDao.inquireByIndex(context);
    }

    @Override
    public List<String> inquireAdIdPageByUserId(String userId, int pageIndex, int pageSize) {
        InquireRedisIndexContext context = new InquireRedisIndexContext("userId", userId);
        context.setPageIndex(pageIndex);
        context.setPageSize(pageSize);
        return this.adEntityDao.inquireKeysByIndex(context);
    }

    @Override
    public List<AdEntity> inquireAdByAdIdList(List<String> adIdList) {
        return this.adEntityDao.inquireByKeys(adIdList);
    }

    @Override
    public long increaseClickNum(String adId, long clickNumber) {
        return this.adEntityDao.increase(adId, "clickNumber", clickNumber);
    }

    @Override
    public long increaseClickPoint(String adId, long clickPoint) {
        return this.adEntityDao.increase(adId, "clickPoint", clickPoint);
    }

    //
    @Override
    public AdPointEntity insertAndInquireAdPoint(Map<String, String> parameterMap) {
        return this.adPointEntityDao.insertAndInquire(parameterMap);
    }

    @Override
    public long increaseAdPoint(String userId, String adId, long adPoint) {
        long newAdPoint;
        //构造广告剩余点数id
        String hexUserId = Long.toHexString(Long.parseLong(userId));
        String hexAdId = Long.toHexString(Long.parseLong(adId));
        StringBuilder adPointIdBuilder = new StringBuilder(hexUserId.length() + hexAdId.length() + 1);
        adPointIdBuilder.append(userId).append('_').append(adId);
        String adPointId = adPointIdBuilder.toString();
        byte[] adPointIdByte = Bytes.toBytes(adPointId);
        Get get = new Get(adPointIdByte);
        get.addColumn(this.accountColumnFamily, this.balanceByte);
        long currentAdPoint = 0;
        synchronized (this) {
            try {
                Result result = this.adHTable.get(get);
                if (result.isEmpty() == false) {
                    byte[] value = result.getValue(this.accountColumnFamily, this.balanceByte);
                    currentAdPoint = Long.parseLong(Bytes.toString(value));
                }
                newAdPoint = currentAdPoint + adPoint;
                Put put = new Put(adPointIdByte);
                put.add(this.accountColumnFamily, this.balanceByte, Bytes.toBytes(Long.toString(newAdPoint)));
                this.adHTable.put(put);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
//        newAdPoint = this.adPointEntityDao.increase(adId, "adPoint", adPoint);
        return newAdPoint;
    }

    @Override
    public AdPointEntity updateAndInquireAdPoint(Map<String, String> parameterMap) {
        return this.adPointEntityDao.updateAndInquire(parameterMap);
    }

    @Override
    public void updateAdPoint(Map<String, String> parameterMap) {
        this.adPointEntityDao.update(parameterMap);
    }

    @Override
    public long inquireAdPointByAdId(String userId, String adId) {
        long adPoint = 0;
        //构造广告剩余点数id
        String hexUserId = Long.toHexString(Long.parseLong(userId));
        String hexAdId = Long.toHexString(Long.parseLong(adId));
        StringBuilder dspAdIdBuilder = new StringBuilder(hexUserId.length() + hexAdId.length() + 1);
        dspAdIdBuilder.append(userId).append('_').append(adId);
        String dspAdId = dspAdIdBuilder.toString();
        byte[] dspAdIdByte = Bytes.toBytes(dspAdId);
        Get get = new Get(dspAdIdByte);
        get.addColumn(this.accountColumnFamily, this.balanceByte);
        try {
            Result result = this.adHTable.get(get);
            if (result.isEmpty() == false) {
                byte[] value = result.getValue(this.accountColumnFamily, this.balanceByte);
                adPoint = Long.parseLong(Bytes.toString(value));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return adPoint;
    }

    @Override
    public List<AdPointEntity> inquireAdPointByAdIdList(List<String> adIdList) {
        return this.adPointEntityDao.inquireByKeys(adIdList);
    }

    @Override
    public AdBiddingEntity inquireAdBiddingByBidId(String bidId) {
        return this.adBiddingEntityDao.inquireByKey(bidId);
    }

    @Override
    public void insertAdBidding(Map<String, String> parameterMap) {
        this.adBiddingEntityDao.insert(parameterMap);
    }

    @Override
    public void updateAdBidding(Map<String, String> parameterMap) {
        this.adBiddingEntityDao.update(parameterMap);
    }

    @Override
    public void deleteAdBidding(String positionId) {
        this.adBiddingEntityDao.delete(positionId);
    }

    private synchronized void initKafka() {
        String zkConnect = ApplicationContext.CONTEXT.getParameter("kafka.zk.connect");
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("zk.connect", zkConnect);
        props.put("producer.type", "async");
        this.producer = new Producer<Integer, String>(new ProducerConfig(props));
    }

    @Override
    public void sendLaunchMessage(String userId, String adId, String positionId, String tagId, String bid) {
        Map<String, String> messageMap = new HashMap<String, String>(4, 1);
        String hexUserId = Long.toHexString(Long.parseLong(userId));
        String hexAdId = Long.toHexString(Long.parseLong(adId));
        messageMap.put("DSPID", hexUserId);
        messageMap.put("AdID", hexAdId);
        messageMap.put("PosID", positionId);
        messageMap.put("TagID", tagId);
        messageMap.put("price", bid);
        ObjectMapper mapper = new ObjectMapper();
        String messageJsonString = "";
        try {
            messageJsonString = mapper.writeValueAsString(messageMap);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (messageJsonString.isEmpty() == false) {
            ProducerData<Integer, String> messageData = new ProducerData<Integer, String>(this.launchTopic, messageJsonString);
            this.producer.send(messageData);
        }
    }

    @Override
    public void sendBiddingMessage(String userId, String adId, String positionId, String tagId, String bid) {
        Map<String, String> messageMap = new HashMap<String, String>(4, 1);
        String hexUserId = Long.toHexString(Long.parseLong(userId));
        String hexAdId = Long.toHexString(Long.parseLong(adId));
        messageMap.put("DSPID", hexUserId);
        messageMap.put("AdID", hexAdId);
        messageMap.put("PosID", positionId);
        messageMap.put("TagID", tagId);
        messageMap.put("price", bid);
        ObjectMapper mapper = new ObjectMapper();
        String messageJsonString = "";
        try {
            messageJsonString = mapper.writeValueAsString(messageMap);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (messageJsonString.isEmpty() == false) {
            ProducerData<Integer, String> messageData = new ProducerData<Integer, String>(this.biddingTopic, messageJsonString);
            this.producer.send(messageData);
        }
    }

    @Override
    public Map<String, String> inquireAdBiddingByPositionId(String positionId, String imei) {
        Map<String, String> resultMap = null;
        DBConnection dbConnection = this.redisPool.getConnection();
        try {
            String tagIds = dbConnection.query(this.RtbUserTag, imei);
            if (tagIds != null) {
                //imei的标签id集合使用,分隔
                String[] tagIdArray = tagIds.split(",");
                if (tagIdArray.length > 0) {
                    //广告投放id等于标签tagId + '-' + 广告位置posId
                    String sspId;
                    StringBuilder sspIdBuilder = new StringBuilder(32);
                    String adWinnerValue;
                    //广告记录id等于广告组dspId + '_' + 广告adId,两个id为16进制字符串
                    String dspAdId;
                    String[] dspAdArray;
                    String hexAdId;
                    String adId;
                    //最大得分
                    double maxPoint = 0;
                    long price;
                    double ctr;
                    double point;
                    long adPoint = 0;
                    byte[] adPointByte;
                    byte[] dspAdIdByte;
                    Get get;
                    Result result;
                    String[] adWinnerValueArray;
                    for (String tagId : tagIdArray) {
                        //根据标签tagId和广告位置posId查询广告投放信息
                        sspIdBuilder.append(tagId).append('-').append(positionId);
                        sspId = sspIdBuilder.toString();
                        sspIdBuilder.setLength(0);
                        adWinnerValue = dbConnection.query(this.RtbAdWinner, sspId);
                        if (adWinnerValue != null) {
                            adWinnerValue = adWinnerValue.substring(1, adWinnerValue.length() - 1);
                            adWinnerValueArray = adWinnerValue.split(",");
                            if (adWinnerValueArray.length == 3) {
                                price = Long.parseLong(adWinnerValueArray[1]);
                                ctr = Double.parseDouble(adWinnerValueArray[2]);
                                point = price * ctr;
                                if (point > maxPoint) {
                                    dspAdId = adWinnerValueArray[0];
                                    //验证广告是否还有余额
                                    dspAdIdByte = Bytes.toBytes(dspAdId);
                                    get = new Get(dspAdIdByte);
                                    get.addColumn(this.accountColumnFamily, this.balanceByte);
                                    result = this.adHTable.get(get);
                                    if (result.isEmpty() == false) {
                                        adPointByte = result.getValue(this.accountColumnFamily, this.balanceByte);
                                        adPoint = Long.parseLong(Bytes.toString(adPointByte));
                                    }
                                    if (adPoint > 100 && adPoint >= price) {
                                        //广告余额大于100并且大于投放价格
                                        dspAdArray = dspAdId.split("_");
                                        if (dspAdArray.length == 2) {
                                            maxPoint = point;
                                            hexAdId = dspAdArray[1];
                                            adId = Long.toString(Long.parseLong(hexAdId, 16));
                                            resultMap = new HashMap<String, String>(4, 1);
                                            resultMap.put("adId", adId);
                                            resultMap.put("tagId", tagId);
                                            resultMap.put("bid", Long.toString(price));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            this.redisPool.releaseConn(dbConnection);
        }
        return resultMap;
    }

    @Override
    public String inquireImeiTag(String imei) {
        String result = "";
        DBConnection dbConnection = this.redisPool.getConnection();
        try {
            String tagIds = dbConnection.query(this.RtbUserTag, imei);
            if (tagIds != null) {
                result = tagIds;
            }
        } finally {
            this.redisPool.releaseConn(dbConnection);
        }
        return result;
    }
}
