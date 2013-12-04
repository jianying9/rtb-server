package com.rtb.tag.localservice;

import com.rtb.tag.entity.TagEntity;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.local.LocalServiceConfig;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = TagLocalService.class,
        description = "标签操作接口")
public class TagLocalServiceImpl implements TagLocalService {

    @InjectRDao(clazz = TagEntity.class)
    private REntityDao<TagEntity> tagEntityDao;
    //

    @Override
    public void init() {
    }

    @Override
    public TagEntity insertTag(Map<String, String> parameterMap) {
        return this.tagEntityDao.insertAndInquire(parameterMap);
    }
}
