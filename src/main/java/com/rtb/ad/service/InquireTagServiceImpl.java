package com.rtb.ad.service;

import com.rtb.ad.entity.AdParameter;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.wolf.framework.local.InjectLocalService;
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
        actionName = ActionNames.INQUIRE_TAG,
        importantParameter = {"imei"},
        returnParameter = {"tagIds", "imei"},
        parametersConfigs = {AdParameter.class},
        validateSession = false,
        response = true,
        description = "查询用户标签",
        group = ActionGroupNames.AD)
public class InquireTagServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String imei = messageContext.getParameter("imei");
        String tagIds = this.adLocalService.inquireImeiTag(imei);
        Map<String, String> resultMap = new HashMap<String, String>(2, 1);
        resultMap.put("tagIds", tagIds);
        resultMap.put("imei", imei);
        messageContext.setMapData(resultMap);
        messageContext.success();
    }
}
