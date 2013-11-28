package com.rtb.ad.service;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
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
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"positionId", "bid", "adId"},
        returnParameter = {"positionId", "adId", "bid"},
        parametersConfigs = {AdBiddingEntity.class},
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
        long bid = Long.parseLong(parameterMap.get("bid"));
        AdPointEntity adPointEntity = this.adLocalService.inquireAdPointByAdId(adId);
        if (adPointEntity != null && adPointEntity.getAdPoint() >= bid) {
            String positionId = parameterMap.get("positionId");
            AdBiddingEntity adBiddingEntity = this.adLocalService.inquireAdBiddingByPositionId(positionId);
            if (adBiddingEntity == null) {
                //该广告位暂时无人竞价
                String time = Long.toString(System.currentTimeMillis());
                parameterMap.put("createTime", time);
                parameterMap.put("lastUpdateTime", time);
                this.adLocalService.insertAdBidding(parameterMap);
                messageContext.setMapData(parameterMap);
                messageContext.success();
            } else {
                //已有竞价，判断价格高低
                if (bid > adBiddingEntity.getBid()) {
                    //竞价更高,成功获取广告位
                    String time = Long.toString(System.currentTimeMillis());
                    parameterMap.put("lastUpdateTime", time);
                    this.adLocalService.updateAdBidding(parameterMap);
                    messageContext.setMapData(parameterMap);
                    messageContext.success();
                }
            }
        }
    }
}
