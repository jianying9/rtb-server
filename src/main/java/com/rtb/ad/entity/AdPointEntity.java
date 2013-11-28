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
        tableName = "RtbAdPoint")
@ParametersConfig()
public final class AdPointEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "广告id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "广告id")
    private String adId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.INT, desc = "广告剩余点数")
    @RColumnConfig(desc = "广告剩余点数")
    private long adPoint;

    public String getAdId() {
        return adId;
    }

    public long getAdPoint() {
        return this.adPoint;
    }

    @Override
    public String getKeyValue() {
        return this.adId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("adId", this.adId);
        map.put("adPoint", Long.toString(this.adPoint));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.adId = entityMap.get("adId");
        this.adPoint = Long.parseLong(entityMap.get("adPoint"));
    }
}
