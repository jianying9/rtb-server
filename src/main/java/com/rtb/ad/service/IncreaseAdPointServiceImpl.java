package com.rtb.ad.service;

import com.rtb.ad.entity.AdEntity;
import com.rtb.ad.entity.AdPointEntity;
import com.rtb.ad.localservice.AdLocalService;
import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.localservice.UserLocalService;
import com.wolf.framework.local.InjectLocalService;
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
        actionName = ActionNames.INCREASE_AD_POINT,
        importantParameter = {"adPoint", "adId"},
        returnParameter = {"adId", "adPoint", "point"},
        parametersConfigs = {AdPointEntity.class, UserEntity.class},
        response = true,
        description = "增加广告点数",
        group = ActionGroupNames.AD)
public class IncreaseAdPointServiceImpl implements Service {

    @InjectLocalService()
    private AdLocalService adLocalService;
    //
    @InjectLocalService()
    private UserLocalService userLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String adId = messageContext.getParameter("adId");
        long adPoint = Long.parseLong(messageContext.getParameter("adPoint"));
        AdEntity adEntity = this.adLocalService.inquireAdByAdId(adId);
        //判断广告存在
        if (adEntity != null) {
            Session session = messageContext.getSession();
            String userId = session.getUserId();
            UserEntity userEntity = this.userLocalService.inquireUserByUserId(userId);
            //判断用户点数是否足够
            if (userEntity.getPoint() >= adPoint) {
                //增加广告对应点数
                long newAdPoint = this.adLocalService.increaseAdPoint(userId, adId, adPoint);
                //扣除用户广告点数
                long newPoint = this.userLocalService.increasePoint(userId, -adPoint);
                //返回
                Map<String, String> resultMap = new HashMap<String, String>(4, 1);
                resultMap.put("adPoint", Long.toString(newAdPoint));
                resultMap.put("point", Long.toString(newPoint));
                resultMap.put("adId", adId);
                messageContext.setMapData(resultMap);
                messageContext.success();
            }
        }
    }
}
