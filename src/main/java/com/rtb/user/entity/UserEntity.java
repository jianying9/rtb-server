package com.rtb.user.entity;

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
        tableName = "RtbUser")
@ParametersConfig()
public class UserEntity extends Entity implements Parameter {

    @ParameterConfig(basicTypeEnum = BasicTypeEnum.UUID, desc = "用户id")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "用户ID")
    private String userId;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_32, desc = "昵称")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.INDEX, desc = "昵称")
    private String nickName;
    //
    @RColumnConfig(desc = "密码md5")
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_32, desc = "密码md5")
    private String password;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_60, desc = "邮箱")
    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.INDEX, desc = "邮箱")
    private String userEmail;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.INT, desc = "点数", defaultValue = "0")
    @RColumnConfig(desc = "点数")
    private int point;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.DATE_TIME, desc = "注册时间")
    @RColumnConfig(desc = "注册时间")
    private long createTime;

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getPoint() {
        return point;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public String getKeyValue() {
        return this.userId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(8, 1);
        map.put("userId", this.userId);
        map.put("nickName", this.nickName);
        map.put("password", this.password);
        map.put("userEmail", this.userEmail);
        map.put("point", Integer.toString(this.point));
        map.put("createTime", Long.toString(this.createTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.userId = entityMap.get("userId");
        this.nickName = entityMap.get("nickName");
        this.password = entityMap.get("password");
        this.userEmail = entityMap.get("userEmail");
        this.point = Integer.parseInt(entityMap.get("point"));
        this.createTime = Long.parseLong(entityMap.get("createTime"));
    }
}
