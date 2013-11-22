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
        tableName = "RtbAd")
@ParametersConfig()
public class AdEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "广告id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "广告id")
    private String adId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_32, desc = "广告标题")
    @RColumnConfig(desc = "广告标题")
    private String adName;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "邮箱")
    @RColumnConfig(desc = "图片id")
    private String imageId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_120, desc = "广告链接")
    @RColumnConfig(desc = "广告链接")
    private String url;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.INT, desc = "累计点击次数")
    @RColumnConfig(desc = "累计点击次数", defaultValue = "0")
    private int clickNumber;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.INT, desc = "累计点击消耗点数")
    @RColumnConfig(desc = "累计点击消耗点数", defaultValue = "0")
    private int clickPoint;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "用户id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.INDEX, desc = "用户id")
    private String userId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.DATE_TIME, desc = "创建时间")
    @RColumnConfig(desc = "创建时间")
    private long createTime;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.DATE_TIME, desc = "最后修改时间")
    @RColumnConfig(desc = "最后修改时间", defaultValue = "1384957981862")
    private long lastUpdateTime;

    public String getAdId() {
        return adId;
    }

    public String getAdName() {
        return adName;
    }

    public String getImageId() {
        return imageId;
    }

    public String getUrl() {
        return url;
    }

    public int getClickNumber() {
        return clickNumber;
    }

    public int getClickPoint() {
        return clickPoint;
    }

    public String getUserId() {
        return userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String getKeyValue() {
        return this.adId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(8, 1);
        map.put("adId", this.adId);
        map.put("adName", this.adName);
        map.put("imageId", this.imageId);
        map.put("url", this.url);
        map.put("clickNumber", Integer.toString(this.clickNumber));
        map.put("clickPoint", Integer.toString(this.clickPoint));
        map.put("createTime", Long.toString(this.createTime));
        map.put("lastUpdateTime", Long.toString(this.lastUpdateTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.adId = entityMap.get("adId");
        this.adName = entityMap.get("adName");
        this.imageId = entityMap.get("imageId");
        this.url = entityMap.get("url");
        this.clickNumber = Integer.parseInt(entityMap.get("clickNumber"));
        this.clickPoint = Integer.parseInt(entityMap.get("clickPoint"));
        this.createTime = Long.parseLong(entityMap.get("createTime"));
        this.lastUpdateTime = Long.parseLong(entityMap.get("lastUpdateTime"));
    }
}
