package com.rtb.user.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.config.ResponseFlags;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.localservice.UserLocalService;
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
        actionName = ActionNames.PAY_FOR_POINT,
        parameterTypeEnum = ParameterTypeEnum.NO_PARAMETER,
        importantParameter = {"point"},
        returnParameter = {"point", "userId"},
        parametersConfigs = {UserEntity.class},
        response = true,
        group = ActionGroupNames.USER,
        description = "充值获取点数")
public class PayForPointServiceImpl implements Service {
    
    @InjectLocalService()
    private UserLocalService userLocalService;
    
    @Override
    public void execute(MessageContext messageContext) {
        Session session = messageContext.getSession();
        String userId = session.getUserId();
        UserEntity userEntity = this.userLocalService.inquireUserByUserId(userId);
        if (userEntity == null) {
            messageContext.setFlag(ResponseFlags.FAILURE_USER_ID_NOT_EXIST);
        } else {
            long point = Long.parseLong(messageContext.getParameter("point"));
            point = this.userLocalService.increasePoint(userId, point);
            Map<String, String> resultMap = userEntity.toMap();
            resultMap.put("point", Long.toString(point));
            messageContext.setMapData(resultMap);
            messageContext.success();
        }
    }
}
