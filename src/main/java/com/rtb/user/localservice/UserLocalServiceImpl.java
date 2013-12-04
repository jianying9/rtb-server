package com.rtb.user.localservice;

import com.rtb.config.RedisTableNames;
import com.rtb.key.localservice.RedisKeyLocalService;
import com.rtb.user.entity.UserEmailEntity;
import com.rtb.user.entity.UserEntity;
import com.rtb.user.entity.UserNickNameEntity;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.local.InjectLocalService;
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
    //
    @InjectRDao(clazz = UserEmailEntity.class)
    private REntityDao<UserEmailEntity> userEmailEntityDao;
    //
    @InjectRDao(clazz = UserNickNameEntity.class)
    private REntityDao<UserNickNameEntity> userNickNameEntityDao;
    //
    @InjectLocalService()
    private RedisKeyLocalService redisKeyLocalService;

    @Override
    public void init() {
    }

    @Override
    public boolean isUserEmailExist(String userEmail) {
        boolean result = false;
        UserEmailEntity userEmailEntity = this.userEmailEntityDao.inquireByKey(userEmail);
        if (userEmailEntity != null) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean isNickNameExist(String nickName) {
        boolean result = false;
        UserNickNameEntity userNickNameEntity = this.userNickNameEntityDao.inquireByKey(nickName);
        if (userNickNameEntity != null) {
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
        //生成主键
        long userId = this.redisKeyLocalService.getNextKeyValue(RedisTableNames.RTB_USER);
        parameterMap.put("userId", Long.toString(userId));
        //保存用户信息
        UserEntity userEntity = this.userEntityDao.insertAndInquire(parameterMap);
        //保存userEmail和userId映射
        Map<String, String> insertMap = userEntity.toMap();
        this.userEmailEntityDao.insert(insertMap);
        //保存nickName和userId映射
        this.userNickNameEntityDao.insert(insertMap);
        return userEntity;
    }

    @Override
    public UserEntity inquireUserByUserEmail(String userEmail) {
        UserEntity userEntity;
        UserEmailEntity userEmailEntity = this.userEmailEntityDao.inquireByKey(userEmail);
        if (userEmailEntity == null) {
            userEntity = null;
        } else {
            long userId = userEmailEntity.getUserId();
            userEntity = this.userEntityDao.inquireByKey(Long.toString(userId));
        }
        return userEntity;
    }

    @Override
    public UserEntity updateUserAndInquire(Map<String, String> parameterMap) {
        return this.userEntityDao.updateAndInquire(parameterMap);
    }

    @Override
    public long increasePoint(String userId, long point) {
        return this.userEntityDao.increase(userId, "point", point);
    }
}
