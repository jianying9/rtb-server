package com.rtb.ad.service;

import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.session.Session;
import com.wolf.framework.worker.context.MessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_AD,
        returnParameter = {"adId", "adName", "imageId", "url", "clickNumber", "clickPoint", "lastUpdateTime", "adPoint"},
        parametersConfigs = {AdEntity.class, AdPointEntity.class},
        page = true,
        response = true,
        description = "查询当前用户的广告列表",
        group = ActionGroupNames.AD)
public class InquireAdServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Session session = messageContext.getSession();
        String userId = session.getUserId();
        int pageIndex = messageContext.getPageIndex();
        int pageSize = messageContext.getPageSize();
        List<String> adIdList = this.adLocalService.inquireAdIdPageByUserId(userId, pageIndex, pageSize);
        if (adIdList.isEmpty() == false) {
            List<AdEntity> adEntityList = this.adLocalService.inquireAdByAdIdList(adIdList);
//            List<AdPointEntity> adPointEntityList = this.adLocalService.inquireAdPointByAdIdList(adIdList);
            List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>(adEntityList.size());
            Map<String, String> resultMap;
            long adPoint;
            for (AdEntity adEntity : adEntityList) {
                resultMap = adEntity.toMap();
                adPoint = this.adLocalService.inquireAdPointByAdId(userId, adEntity.getKeyValue());
                resultMap.put("adPoint", Long.toString(adPoint));
                resultMapList.add(resultMap);
            }
            messageContext.setMapListData(resultMapList);
        }
        messageContext.success();
    }
}
