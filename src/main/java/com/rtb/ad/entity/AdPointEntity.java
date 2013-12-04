package com.rtb.ad.entity;

import com.rtb.config.RedisTableNames;
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
        tableName = RedisTableNames.RTB_AD_POINT)
@ParametersConfig()
public final class AdPointEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.LONG, desc = "广告Id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "广告Id")
    private long adId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.LONG, desc = "广告剩余点数")
    @RColumnConfig(desc = "广告剩余点数")
    private long adPoint;

    public long getAdId() {
        return adId;
    }

    public long getAdPoint() {
        return this.adPoint;
    }

    @Override
    public String getKeyValue() {
        return Long.toString(this.adId);
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("adId", Long.toString(this.adId));
        map.put("adPoint", Long.toString(this.adPoint));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.adId = Long.parseLong(entityMap.get("adId"));
        this.adPoint = Long.parseLong(entityMap.get("adPoint"));
    }
}
