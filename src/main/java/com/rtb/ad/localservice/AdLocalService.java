package com.rtb.ad.localservice;

import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.wolf.framework.local.Local;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface AdLocalService extends Local{

    public AdEntity insertAndInquireAd(Map<String, String> parameterMap);
    
    public AdEntity inquireAdByAdId(String adId);
    
    public List<AdEntity> inquirePageByUserId(String userId, int pageIndex, int pageSize);
    
    public List<String> inquireAdIdPageByUserId(String userId, int pageIndex, int pageSize);
    
    public List<AdEntity> inquireAdByAdIdList(List<String> adIdList);
    
    //
    public AdPointEntity insertAndInquireAdPoint(Map<String, String> parameterMap);
    
    public AdPointEntity updateAndInquireAdPoint(Map<String, String> parameterMap);
    
    public AdPointEntity inquireAdPointByAdId(String adId);
    
    public List<AdPointEntity> inquireAdPointByAdIdList(List<String> adIdList);
}
