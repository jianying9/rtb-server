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
import com.wolf.framework.service.SessionHandleTypeEnum;
import com.wolf.framework.session.Session;
import com.wolf.framework.worker.context.MessageContext;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.LOGOUT,
        parameterTypeEnum = ParameterTypeEnum.NO_PARAMETER,
        returnParameter = {"nickName", "userId"},
        parametersConfigs = {UserEntity.class},
        sessionHandleTypeEnum = SessionHandleTypeEnum.REMOVE,
        response = true,
        group = ActionGroupNames.USER,
        description = "用户退出")
public class LogoutServiceImpl implements Service {

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
            messageContext.setNewSession(null);
            messageContext.setEntityData(userEntity);
            messageContext.success();
        }
    }
}
