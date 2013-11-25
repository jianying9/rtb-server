package com.rtb.ad.service;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.image.entity.ImageEntity;
import com.rtb.image.localservice.ImageLocalService;
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
        actionName = ActionNames.INQUIRE_POSITION_AD,
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"positionId"},
        returnParameter = {"positionId", "adId", "bid", "adName", "url", "dataUrl"},
        parametersConfigs = {AdBiddingEntity.class, AdEntity.class, ImageEntity.class},
        validateSession = false,
        response = true,
        description = "查询广告位广告",
        group = ActionGroupNames.AD)
public class InquirePositionAdServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;
    //
    @InjectLocalService()
    private ImageLocalService imageLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String positionId = messageContext.getParameter("positionId");
        AdBiddingEntity adBiddingEntity = this.adLocalService.inquireAdBiddingByPositionId(positionId);
        if (adBiddingEntity != null) {
            //查询广告信息
            String adId = adBiddingEntity.getAdId();
            AdEntity adEntity = this.adLocalService.inquireAdByAdId(adId);
            if (adEntity != null) {
                Map<String, String> resultMap = adBiddingEntity.toMap();
                resultMap.put("adName", adEntity.getAdName());
                resultMap.put("url", adEntity.getUrl());
                //查询图片信息
                String imageId = adEntity.getImageId();
                ImageEntity imageEntity = this.imageLocalService.inquireByImageId(imageId);
                String dataUrl = "";
                if(imageEntity != null) {
                    dataUrl = imageEntity.getDataUrl();
                }
                resultMap.put("dataUrl", dataUrl);
                messageContext.setMapData(resultMap);
                messageContext.success();
            } else {
                messageContext.setMapData(messageContext.getParameterMap());
            }
        } else {
            messageContext.setMapData(messageContext.getParameterMap());
        }
    }
}
