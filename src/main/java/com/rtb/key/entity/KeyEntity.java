package com.rtb.key.entity;

import com.rtb.config.RedisTableNames;
import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.annotation.ColumnTypeEnum;
import com.wolf.framework.dao.annotation.RColumnConfig;
import com.wolf.framework.dao.annotation.RDaoConfig;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author aladdin
 */
@RDaoConfig(
        tableName = RedisTableNames.RTB_KEY)
public final class KeyEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "表名")
    private String tableName;
    //
    @RColumnConfig(desc = "主键值")
    private long indexValue;

    public String getTableName() {
        return tableName;
    }

    public long getIndexValue() {
        return indexValue;
    }

    @Override
    public String getKeyValue() {
        return this.tableName;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("tableName", this.tableName);
        map.put("indexValue", Long.toString(this.indexValue));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.tableName = entityMap.get("tableName");
        this.indexValue = Long.parseLong(entityMap.get("indexValue"));
    }
}
