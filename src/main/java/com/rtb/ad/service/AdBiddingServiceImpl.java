package com.rtb.ad.service;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.tag.entity.TagEntity;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.worker.context.MessageContext;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.AD_BIDDING,
        importantParameter = {"positionId", "bid", "adId", "tagId"},
        returnParameter = {"adId"},
        parametersConfigs = {AdBiddingEntity.class, TagEntity.class, AdEntity.class},
        response = true,
        description = "广告竞价",
        group = ActionGroupNames.AD)
public class AdBiddingServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        String adId = parameterMap.get("adId");
        String bidStr = parameterMap.get("bid");
        long bid = Long.parseLong(bidStr);
        String userId = messageContext.getSession().getUserId();
        long currnetAdPoint = this.adLocalService.inquireAdPointByAdId(userId, adId);
        if (currnetAdPoint >= bid) {
            String tagId = parameterMap.get("tagId");
            String positionId = parameterMap.get("positionId");
//            StringBuilder bidIdBuilder = new StringBuilder(32);
//            bidIdBuilder.append(positionId).append('_').append(tagId);
//            String bidId = bidIdBuilder.toString();
            //
//            AdBiddingEntity adBiddingEntity = this.adLocalService.inquireAdBiddingByBidId(bidId);
//            if (adBiddingEntity == null || bid > adBiddingEntity.getBid()) {
//                parameterMap.put("bidId", bidId);
//                this.adLocalService.insertAdBidding(parameterMap);
//                messageContext.setMapData(parameterMap);
//                messageContext.success();
//            }
            //发送竞价消息
            this.adLocalService.sendBiddingMessage(userId, adId, positionId, tagId, bidStr);
            messageContext.setMapData(parameterMap);
            messageContext.success();
        }
    }
}
