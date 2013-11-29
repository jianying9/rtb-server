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
            String bidStr = messageContext.getParameter("bid");
            long bid = Long.parseLong(bidStr);
            AdPointEntity adPointEntity = this.adLocalService.inquireAdPointByAdId(adId);
            if (adPointEntity != null) {
                //扣点
                long newAdPoint = this.adLocalService.increaseAdPoint(adId, -bid);
                //如果adPoint <= 0,则空出广告位
                String positionId = messageContext.getParameter("positionId");
                if (newAdPoint <= 0) {
                    AdBiddingEntity adBiddingEntity = this.adLocalService.inquireAdBiddingByPositionId(positionId);
                    if (adBiddingEntity != null) {
                        if (adBiddingEntity.getAdId().equals(adId)) {
                            this.adLocalService.deleteAdBidding(positionId);
                        }
                    }
                }
                //广告累计统计
                this.adLocalService.increaseClickNum(adId, 1);
                this.adLocalService.increaseClickPoint(adId, bid);
                //发送广告点击消息到kafka
                this.adLocalService.sendLaunchMessage(adId, positionId, "1", bidStr);
            }
        }
    }
}
