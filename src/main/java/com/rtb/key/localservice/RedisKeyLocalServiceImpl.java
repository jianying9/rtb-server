package com.rtb.key.localservice;

import com.rtb.image.entity.ImageEntity;
import com.rtb.key.entity.KeyEntity;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.local.LocalServiceConfig;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = RedisKeyLocalService.class,
        description = "redis table主键值管理")
public class RedisKeyLocalServiceImpl implements RedisKeyLocalService {

    @InjectRDao(clazz = KeyEntity.class)
    private REntityDao<KeyEntity> keyEntityDao;

    @Override
    public void init() {
    }

    @Override
    public long getNextKeyValue(String tableName) {
        return this.keyEntityDao.increase(tableName, "indexValue", 1);
    }
}
