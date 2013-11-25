package com.rtb.ad.localservice;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquireRedisIndexContext;
import com.wolf.framework.local.LocalServiceConfig;
import java.util.List;
import java.util.Map;

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

    @Override
    public void init() {
    }

    @Override
    public AdEntity insertAndInquireAd(Map<String, String> parameterMap) {
        return this.adEntityDao.insertAndInquire(parameterMap);
    }

    @Override
    public AdEntity inquireAdByAdId(String adId) {
        return this.adEntityDao.inquireByKey(adId);
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

    //
    @Override
    public AdPointEntity insertAndInquireAdPoint(Map<String, String> parameterMap) {
        return this.adPointEntityDao.insertAndInquire(parameterMap);
    }

    @Override
    public AdPointEntity updateAndInquireAdPoint(Map<String, String> parameterMap) {
        return this.adPointEntityDao.updateAndInquire(parameterMap);
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
    public AdBiddingEntity inquireAdBiddingByPositionId(String positionId) {
        return this.adBiddingEntityDao.inquireByKey(positionId);
    }

    @Override
    public void insertAdBidding(Map<String, String> parameterMap) {
        this.adBiddingEntityDao.insert(parameterMap);
    }

    @Override
    public void updateAdBidding(Map<String, String> parameterMap) {
        this.adBiddingEntityDao.update(parameterMap);
    }
}
