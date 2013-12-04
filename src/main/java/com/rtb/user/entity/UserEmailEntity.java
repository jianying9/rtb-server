package com.rtb.user.entity;

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
        tableName = RedisTableNames.RTB_USER_EMAIL)
public final class UserEmailEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "邮箱")
    private String userEmail;
    //
    @RColumnConfig(desc = "用户id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String getKeyValue() {
        return this.userEmail;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("userId", Long.toString(this.userId));
        map.put("userEmail", this.userEmail);
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.userId = Long.parseLong(entityMap.get("userId"));
        this.userEmail = entityMap.get("userEmail");
    }
}
