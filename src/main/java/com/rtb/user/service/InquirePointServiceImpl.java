package com.rtb.user.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.config.RtbResponseFlags;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.localservice.UserLocalService;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.session.Session;
import com.wolf.framework.worker.context.MessageContext;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_POINT,
        parameterTypeEnum = ParameterTypeEnum.NO_PARAMETER,
        returnParameter = {"point", "userId"},
        parametersConfigs = {UserEntity.class},
        response = true,
        group = ActionGroupNames.USER,
        description = "查询用户点数")
public class InquirePointServiceImpl implements Service {

    @InjectLocalService()
    private UserLocalService userLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Session session = messageContext.getSession();
        String userId = session.getUserId();
        UserEntity userEntity = this.userLocalService.inquireUserByUserId(userId);
        if (userEntity == null) {
            messageContext.setFlag(RtbResponseFlags.FAILURE_USER_ID_NOT_EXIST);
        } else {
            messageContext.setEntityData(userEntity);
            messageContext.success();
        }
    }
}
