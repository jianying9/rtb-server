package com.rtb.ad.entity;

import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.annotation.ColumnTypeEnum;
import com.wolf.framework.dao.annotation.RColumnConfig;
import com.wolf.framework.dao.annotation.RDaoConfig;
import com.wolf.framework.data.BasicTypeEnum;
import com.wolf.framework.service.parameter.Parameter;
import com.wolf.framework.service.parameter.ParameterConfig;
import com.wolf.framework.service.parameter.ParametersConfig;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author aladdin
 */
@RDaoConfig(
        tableName = "RtbTag")
@ParametersConfig()
public final class TagEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.INT, desc = "标签id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "标签id")
    private String tagId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_10, desc = "标签名")
    @RColumnConfig(desc = "标签名")
    private String tagName;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.DATE_TIME, desc = "创建时间")
    @RColumnConfig(desc = "创建时间")
    private long createTime;

    public String getTagId() {
        return this.tagId;
    }

    public String getTagName() {
        return this.tagName;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getKeyValue() {
        return this.tagId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(4, 1);
        map.put("tagId", this.tagId);
        map.put("tagName", tagName);
        map.put("createTime", Long.toString(this.createTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.tagId = entityMap.get("tagId");
        this.tagName = entityMap.get("tagName");
        this.createTime = Long.parseLong(entityMap.get("createTime"));
    }
}
