package com.rtb.ad.service;

import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.session.Session;
import com.wolf.framework.worker.context.MessageContext;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INSERT_AD,
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"adName", "imageId", "url"},
        returnParameter = {"adId", "adName", "imageId", "url", "clickNumber", "clickPoint", "lastUpdateTime"},
        parametersConfigs = {AdEntity.class},
        response = true,
        description = "新增广告",
        group = ActionGroupNames.AD)
public class InsertAdServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        parameterMap.put("clickNumber", "0");
        parameterMap.put("clickPoint", "0");
        parameterMap.put("lastBidding", "");
        String time = Long.toString(System.currentTimeMillis());
        parameterMap.put("createTime", time);
        parameterMap.put("lastUpdateTime", time);
        Session session = messageContext.getSession();
        parameterMap.put("userId", session.getUserId());
        AdEntity adEntity = this.adLocalService.insertAndInquireAd(parameterMap);
        messageContext.setEntityData(adEntity);
        messageContext.success();
    }
}
