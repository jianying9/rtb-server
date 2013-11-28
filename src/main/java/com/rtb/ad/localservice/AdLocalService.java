package com.rtb.ad.localservice;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.wolf.framework.local.Local;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface AdLocalService extends Local {

    public AdEntity insertAndInquireAd(Map<String, String> parameterMap);

    public AdEntity inquireAdByAdId(String adId);

    public void updateAd(Map<String, String> parameterMap);

    public List<AdEntity> inquirePageByUserId(String userId, int pageIndex, int pageSize);

    public List<String> inquireAdIdPageByUserId(String userId, int pageIndex, int pageSize);

    public List<AdEntity> inquireAdByAdIdList(List<String> adIdList);
    
    public long increaseClickNum(String adId, long clickNumber);
    
    public long increaseClickPoint(String adId, long clickPoint);

    //
    public AdPointEntity insertAndInquireAdPoint(Map<String, String> parameterMap);

    public AdPointEntity updateAndInquireAdPoint(Map<String, String> parameterMap);

    public void updateAdPoint(Map<String, String> parameterMap);

    public AdPointEntity inquireAdPointByAdId(String adId);
    
    public long increaseAdPoint(String adId, long adPoint);

    public List<AdPointEntity> inquireAdPointByAdIdList(List<String> adIdList);

    //
    public AdBiddingEntity inquireAdBiddingByPositionId(String positionId);

    public void insertAdBidding(Map<String, String> parameterMap);

    public void updateAdBidding(Map<String, String> parameterMap);

    public void deleteAdBidding(String positionId);
}
