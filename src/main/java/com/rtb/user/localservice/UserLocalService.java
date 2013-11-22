package com.rtb.user.localservice;

import com.rtb.user.entity.UserEntity;
import com.wolf.framework.local.Local;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface UserLocalService extends Local{

    public boolean isUserEmailExist(String userEmail);

    public UserEntity inquireUserByUserEmail(String userEmail);

    public UserEntity inquireUserByUserId(String userId);

    public List<UserEntity> inquireUserByUserIdList(List<String> userIdList);

    public UserEntity insertAndInquireUser(Map<String, String> parameterMap);
    
    public UserEntity updateUserAndInquire(Map<String, String> parameterMap);
}
