package com.rtb.ad.service;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.worker.context.MessageContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.CLICK_AD,
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"positionId", "adId", "bid"},
        parametersConfigs = {AdBiddingEntity.class},
        validateSession = false,
        response = false,
        description = "广告点击",
        group = ActionGroupNames.AD)
public class ClickAdServiceImpl implements Service {
    
    @InjectLocalService()
    private AdLocalService adLocalService;
    
    @Override
    public void execute(MessageContext messageContext) {
        //查询广告信息
        String adId = messageContext.getParameter("adId");
        AdEntity adEntity = this.adLocalService.inquireAdByAdId(adId);
        if (adEntity != null) {
            int bid = Integer.parseInt(messageContext.getParameter("bid"));
            AdPointEntity adPointEntity = this.adLocalService.inquireAdPointByAdId(adId);
            if (adPointEntity != null) {
                String lastUpdateTime = Long.toString(System.currentTimeMillis());
                //扣点
                int newAdPoint = adPointEntity.getAdPoint();
                if (newAdPoint > 0) {
                    if (newAdPoint > bid) {
                        newAdPoint = newAdPoint - bid;
                    } else {
                        bid = newAdPoint;
                        newAdPoint = 0;
                    }
                    //更新adPoint
                    Map<String, String> adPointMap = new HashMap<String, String>(4, 1);
                    adPointMap.put("adId", adId);
                    adPointMap.put("adPoint", Integer.toString(newAdPoint));
                    adPointMap.put("lastUpdateTime", lastUpdateTime);
                    this.adLocalService.updateAdPoint(adPointMap);
                    //如果剩余点数为0,则空出广告位
                    if (newAdPoint == 0) {
                        String positionId = messageContext.getParameter("positionId");
                        AdBiddingEntity adBiddingEntity = this.adLocalService.inquireAdBiddingByPositionId(positionId);
                        if (adBiddingEntity != null) {
                            if (adBiddingEntity.getAdId().equals(adId)) {
                                this.adLocalService.deleteAdBidding(positionId);
                            }
                        }
                    }
                }
                //广告累计统计
                Map<String, String> adMap = new HashMap<String, String>(4, 1);
                adMap.put("adId", adId);
                adMap.put("lastUpdateTime", lastUpdateTime);
                int newClickPoint = adEntity.getClickPoint() + bid;
                int newClickNumber = adEntity.getClickNumber() + 1;
                adMap.put("clickPoint", Integer.toString(newClickPoint));
                adMap.put("clickNumber", Integer.toString(newClickNumber));
                this.adLocalService.updateAd(adMap);
            }
        }
    }
}
