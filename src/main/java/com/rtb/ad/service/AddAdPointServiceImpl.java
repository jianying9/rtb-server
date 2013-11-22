package com.rtb.ad.service;

import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.localservice.UserLocalService;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ParameterTypeEnum;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.session.Session;
import com.wolf.framework.worker.context.MessageContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.ADD_AD_POINT,
        parameterTypeEnum = ParameterTypeEnum.PARAMETER,
        importantParameter = {"adPoint", "adId"},
        returnParameter = {"adId", "adPoint", "point", "lastUpdateTime"},
        parametersConfigs = {AdPointEntity.class, UserEntity.class},
        response = true,
        description = "增加广告点数",
        group = ActionGroupNames.AD)
public class AddAdPointServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;
    //
    @InjectLocalService()
    private UserLocalService userLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        String adId = parameterMap.get("adId");
        String pointStr = parameterMap.get("adPoint");
        int adPoint = Integer.parseInt(pointStr);
        AdEntity adEntity = this.adLocalService.inquireAdByAdId(adId);
        //判断广告存在
        if(adEntity != null) {
            Session session = messageContext.getSession();
            String userId = session.getUserId();
            UserEntity userEntity = this.userLocalService.inquireUserByUserId(userId);
            //判断用户点数是否足够
            if(userEntity.getPoint() >= adPoint) {
                //增加广告对应点数
                AdPointEntity adPointEntity = this.adLocalService.inquireAdPointByAdId(adId);
                if(adPointEntity == null) {
                    //新增广告点数记录
                    adPointEntity = this.adLocalService.insertAndInquireAdPoint(parameterMap);
                } else {
                    //更新已有广告点数
                    int newPoint = adPointEntity.getAdPoint()+ adPoint;
                    parameterMap.put("adPoint", Integer.toString(newPoint));
                    adPointEntity = this.adLocalService.updateAndInquireAdPoint(parameterMap);
                }
                //扣除用户广告点数
                Map<String, String> updateUserMap = new HashMap<String, String>(2, 1);
                int newUserPoint = userEntity.getPoint() - adPoint;
                updateUserMap.put("userId", userId);
                updateUserMap.put("point", Integer.toString(newUserPoint));
                userEntity = this.userLocalService.updateUserAndInquire(updateUserMap);
                newUserPoint = userEntity.getPoint();
                //返回
                Map<String, String> resultMap = new HashMap<String, String>(4, 1);
                resultMap.putAll(adPointEntity.toMap());
                resultMap.put("point", Integer.toString(newUserPoint));
                messageContext.setMapData(resultMap);
                messageContext.success();
            }
        }
    }
}
