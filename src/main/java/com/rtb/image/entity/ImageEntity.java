package com.rtb.image.entity;

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
        tableName = "RtbImage")
@ParametersConfig()
public final class ImageEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "图片id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "图片id")
    private String imageId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.IMAGE, desc = "图片文件内容")
    @RColumnConfig(desc = "文件内容")
    private String dataUrl;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.DATE_TIME, desc = "创建时间")
    @RColumnConfig(desc = "创建时间")
    private long createTime;

    public String getImageId() {
        return imageId;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public String getKeyValue() {
        return this.imageId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(4, 1);
        map.put("imageId", this.imageId);
        map.put("dataUrl", this.dataUrl);
        map.put("createTime", Long.toString(this.createTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.imageId = entityMap.get("imageId");
        this.dataUrl = entityMap.get("dataUrl");
        this.createTime = Long.parseLong(entityMap.get("createTime"));
    }
}
