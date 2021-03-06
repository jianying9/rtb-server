package com.rtb.user.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.config.ResponseFlags;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.localservice.UserLocalService;
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
        actionName = ActionNames.REGISTER,
        importantParameter = {"nickName", "userEmail", "password"},
        returnParameter = {"userId", "userEmail"},
        parametersConfigs = {UserEntity.class},
        validateSession = false,
        response = true,
        requireTransaction = true,
        group = ActionGroupNames.USER,
        description = "用户注册:FAILURE_USER_EMAIL_USED-邮箱已经被使用")
public class RegisterServiceImpl implements Service {

    @InjectLocalService()
    private UserLocalService userLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        String userEmail = parameterMap.get("userEmail");
        synchronized (this) {
            //查询邮箱是否被使用
            boolean isExists = this.userLocalService.isUserEmailExist(userEmail);
            if (isExists) {
                //邮箱已经被使用
                messageContext.setFlag(ResponseFlags.FAILURE_USER_EMAIL_USED);
            } else {
                String nickName = parameterMap.get("nickName");
                isExists = this.userLocalService.isNickNameExist(nickName);
                if (isExists) {
                    //昵称已经被使用
                    messageContext.setFlag(ResponseFlags.FAILURE_USER_NICK_NAME_USED);
                } else {
                    //新增加用户
                    parameterMap.put("createTime", Long.toString(System.currentTimeMillis()));
                    parameterMap.put("point", "0");
                    UserEntity userEntity = this.userLocalService.insertAndInquireUser(parameterMap);
                    messageContext.setEntityData(userEntity);
                    messageContext.success();
                }
            }
        }
    }
}
