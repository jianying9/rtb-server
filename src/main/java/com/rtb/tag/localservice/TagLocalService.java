package com.rtb.tag.localservice;

import com.rtb.tag.entity.TagEntity;
import com.wolf.framework.local.Local;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface TagLocalService extends Local{

    public TagEntity insertTag(Map<String, String> parameterMap);
}
