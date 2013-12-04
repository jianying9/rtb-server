package com.rtb.tag.service;

import com.rtb.config.ActionGroupNames;
import com.rtb.config.ActionNames;
import com.rtb.tag.entity.TagEntity;
import com.rtb.tag.localservice.TagLocalService;
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
        actionName = ActionNames.INSERT_TAG,
        importantParameter = {"tagId", "tagName"},
        parametersConfigs = {TagEntity.class},
        response = true,
        description = "新增标签",
        group = ActionGroupNames.TAG)
public class InsertTagServiceImpl implements Service {
    
    @InjectLocalService()
    private TagLocalService tagLocalService;
    
    @Override
    public void execute(MessageContext messageContext) {
        Map<String, String> parameterMap = messageContext.getParameterMap();
        String time = Long.toString(System.currentTimeMillis());
        parameterMap.put("createTime", time);
        TagEntity tagEntity = this.tagLocalService.insertTag(parameterMap);
        messageContext.setEntityData(tagEntity);
        messageContext.success();
    }
}
