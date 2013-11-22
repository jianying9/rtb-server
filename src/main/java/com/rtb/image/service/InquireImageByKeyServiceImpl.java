package com.rtb.image.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.image.entity.ImageEntity;
import com.rtb.image.localservice.ImageLocalService;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.worker.context.MessageContext;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_IMAGE_BY_KEY,
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"imageId"},
        returnParameter = {"imageId", "dataUrl"},
        parametersConfigs = {ImageEntity.class},
        validateSession = false,
        response = true,
        description = "根据图片key查询图片数据",
        group = ActionGroupNames.FILE)
public class InquireImageByKeyServiceImpl implements Service {

    @InjectLocalService()
    private ImageLocalService imageLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String imageId = messageContext.getParameter("imageId");
        ImageEntity imageEntity = this.imageLocalService.inquireByImageId(imageId);
        if (imageEntity != null) {
            messageContext.setEntityData(imageEntity);
            messageContext.success();
        }
    }
}
