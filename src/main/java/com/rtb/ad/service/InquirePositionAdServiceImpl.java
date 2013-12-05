package com.rtb.ad.service;

import com.rtb.ad.entity.AdBiddingEntity;
import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.image.entity.ImageEntity;
import com.rtb.image.localservice.ImageLocalService;
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
        actionName = ActionNames.INQUIRE_POSITION_AD,
        importantParameter = {"positionId", "userId"},
        returnParameter = {"positionId", "adId", "bid", "adName", "url", "dataUrl", "userId", "tagId"},
        parametersConfigs = {AdBiddingEntity.class, AdEntity.class, ImageEntity.class, TagEntity.class},
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
        String imei = messageContext.getParameter("userId");
//        String imei = "353922050040300";
        Map<String, String> adBiddingMap = this.adLocalService.inquireAdBiddingByPositionId(positionId, imei);
        if (adBiddingMap == null) {
            //无投放广告竞价信息
            messageContext.setMapData(messageContext.getParameterMap());
        } else {
            //有投放广告竞价信息
            String adId = adBiddingMap.get("adId");
            AdEntity adEntity = this.adLocalService.inquireAdByAdId(adId);
            if (adEntity == null) {
                //无广告信息
                messageContext.setMapData(messageContext.getParameterMap());
            } else {
                //有广告信息
                adBiddingMap.put("positionId", positionId);
                adBiddingMap.put("adName", adEntity.getAdName());
                adBiddingMap.put("url", adEntity.getUrl());
                adBiddingMap.put("userId", adEntity.getUserId());
                //查询图片信息
                String imageId = adEntity.getImageId();
                ImageEntity imageEntity = this.imageLocalService.inquireByImageId(imageId);
                String dataUrl = "";
                if (imageEntity != null) {
                    dataUrl = imageEntity.getDataUrl();
                }
                adBiddingMap.put("dataUrl", dataUrl);
                messageContext.setMapData(adBiddingMap);
                messageContext.success();
            }
        }
    }
}
