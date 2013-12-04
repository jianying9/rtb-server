package com.rtb.key.localservice;

import com.wolf.framework.local.Local;

/**
 *
 * @author aladdin
 */
public interface RedisKeyLocalService extends Local{

    /**
     * 表对应的主键值自增1
     * @param tableName
     * @return 
     */
    public long getNextKeyValue(String tableName);
}
