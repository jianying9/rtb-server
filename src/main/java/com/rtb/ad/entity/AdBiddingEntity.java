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
        tableName = RedisTableNames.RTB_AD_BIDDING)
@ParametersConfig()
public final class AdBiddingEntity extends Entity implements Parameter {

    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_60, desc = "竞价id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "竞价id")
    private String bidId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.LONG, desc = "广告位id,[0,9]")
    @RColumnConfig(desc = "广告位id")
    private long positionId;
    //
    @RColumnConfig(desc = "广告id")
    private long adId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.LONG, desc = "出价")
    @RColumnConfig(desc = "出价")
    private long bid;
    //
    @RColumnConfig(desc = "标签Id", defaultValue = "103006")
    private long tagId;

    public String getBidId() {
        return bidId;
    }

    public long getPositionId() {
        return positionId;
    }

    public long getAdId() {
        return adId;
    }

    public long getBid() {
        return bid;
    }

    public long getTagId() {
        return tagId;
    }

    @Override
    public String getKeyValue() {
        return this.bidId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(8, 1);
        map.put("bidId", this.bidId);
        map.put("positionId", Long.toString(this.positionId));
        map.put("adId", Long.toString(this.adId));
        map.put("bid", Long.toString(this.bid));
        map.put("tagId", Long.toString(this.bid));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.bidId = entityMap.get("bid");
        this.positionId = Long.parseLong(entityMap.get("positionId"));
        this.adId = Long.parseLong(entityMap.get("adId"));
        this.bid = Long.parseLong(entityMap.get("bid"));
        this.tagId = Long.parseLong(entityMap.get("tagId"));
    }
}
