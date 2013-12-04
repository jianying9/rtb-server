package com.rtb.ad.localservice;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.tag.entity.TagEntity;
import com.rtb.config.RedisTableNames;
import com.rtb.key.localservice.RedisKeyLocalService;
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
import org.apache.hadoop.hbase.client.HTable;
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

    @Override
    public void init() {
        //初始化kafka
        String zkConnect = ApplicationContext.CONTEXT.getParameter("kafka.zk.connect");
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("zk.connect", zkConnect);
        this.producer = new Producer<Integer, String>(new ProducerConfig(props));
        System.out.println("aaaaaaaaa------------------------------aaaaaaaaaaaa");
        //初始化hbase
        Configuration config = HBaseConfiguration.create();
        try {
            this.adHTable = new HTable(config, "AD_DSP");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("xxxxxxx------------------------------xxxxxxxx");
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
        try {
            newAdPoint = this.adHTable.incrementColumnValue(adPointIdByte, this.accountColumnFamily, this.balanceByte, adPoint, false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
    public AdPointEntity inquireAdPointByAdId(String adId) {
        return this.adPointEntityDao.inquireByKey(adId);
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

    @Override
    public void sendLaunchMessage(String userId, String adId, String positionId, String tagId, String bid) {
        Map<String, String> messageMap = new HashMap<String, String>(4, 1);
        String hexUserId = Long.toHexString(Long.parseLong(userId));
        String hexAdId = Long.toHexString(Long.parseLong(adId));
        messageMap.put("DSPID", hexUserId);
        messageMap.put("AdId", hexAdId);
        messageMap.put("PosId", positionId);
        messageMap.put("TagId", tagId);
        messageMap.put("Price", bid);
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
        messageMap.put("AdId", hexAdId);
        messageMap.put("PosId", positionId);
        messageMap.put("TagId", tagId);
        messageMap.put("Price", bid);
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
}
