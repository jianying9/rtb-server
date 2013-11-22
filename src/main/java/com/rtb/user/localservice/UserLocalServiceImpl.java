package com.rtb.user.localservice;

import com.rtb.user.entity.UserEntity;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquireRedisIndexContext;
import com.wolf.framework.local.LocalServiceConfig;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = UserLocalService.class,
        description = "用户操作内部接口")
public class UserLocalServiceImpl implements UserLocalService {

    @InjectRDao(clazz = UserEntity.class)
    private REntityDao<UserEntity> userEntityDao;

    @Override
    public void init() {
    }

    @Override
    public boolean isUserEmailExist(String userEmail) {
        boolean result = false;
        long num = this.userEntityDao.countByIndex("userEmail", userEmail);
        if (num > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public UserEntity inquireUserByUserId(String userId) {
        return this.userEntityDao.inquireByKey(userId);
    }

    @Override
    public List<UserEntity> inquireUserByUserIdList(List<String> userIdList) {
        return this.userEntityDao.inquireByKeys(userIdList);
    }

    @Override
    public UserEntity insertAndInquireUser(Map<String, String> parameterMap) {
        UserEntity userEntity = this.userEntityDao.insertAndInquire(parameterMap);
        return userEntity;
    }

    @Override
    public UserEntity inquireUserByUserEmail(String userEmail) {
        UserEntity userEntity;
        InquireRedisIndexContext inquireRedisIndexContext = new InquireRedisIndexContext("userEmail", userEmail);
        List<UserEntity> userEntityList = this.userEntityDao.inquireByIndex(inquireRedisIndexContext);
        if (userEntityList.isEmpty()) {
            userEntity = null;
        } else {
            userEntity = userEntityList.get(0);
        }
        return userEntity;
    }

    @Override
    public UserEntity updateUserAndInquire(Map<String, String> parameterMap) {
        return this.userEntityDao.updateAndInquire(parameterMap);
    }
}
