package com.rtb.image.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.image.entity.ImageEntity;
import com.rtb.image.localservice.ImageLocalService;
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
        actionName = ActionNames.INSERT_IMAGE,
        importantParameter = {"dataUrl"},
        returnParameter = {"imageId"},
        parametersConfigs = {ImageEntity.class},
        response = true,
        description = "保存图片",
        group = ActionGroupNames.FILE)
public class InsertImageServiceImpl implements Service {

    @InjectLocalService()
    private ImageLocalService imageLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        parameterMap.put("createTime", Long.toString(System.currentTimeMillis()));
        ImageEntity imageEntity = this.imageLocalService.insertImage(parameterMap);
        messageContext.setEntityData(imageEntity);
        messageContext.success();
    }
}
