package com.rtb.image.localservice;

import com.rtb.config.RedisTableNames;
import com.rtb.image.entity.ImageEntity;
import com.rtb.key.localservice.RedisKeyLocalService;
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
        interfaceInfo = ImageLocalService.class,
        description = "图片操作内部接口")
public class ImageLocalServiceImpl implements ImageLocalService {
    
    @InjectRDao(clazz = ImageEntity.class)
    private REntityDao<ImageEntity> imageEntityDao;
    //
    @InjectLocalService()
    private RedisKeyLocalService redisKeyLocalService;
    
    @Override
    public void init() {
    }
    
    @Override
    public ImageEntity insertImage(Map<String, String> parameterMap) {
        long imageId = this.redisKeyLocalService.getNextKeyValue(RedisTableNames.RTB_IMAGE);
        parameterMap.put("imageId", Long.toString(imageId));
        return this.imageEntityDao.insertAndInquire(parameterMap);
    }
    
    @Override
    public ImageEntity inquireByImageId(String imageId) {
        return this.imageEntityDao.inquireByKey(imageId);
    }
    
    @Override
    public List<ImageEntity> inquireByImageIdList(List<String> imageIdList) {
        return this.imageEntityDao.inquireByKeys(imageIdList);
    }
    
    @Override
    public void delete(String imageId) {
        this.imageEntityDao.delete(imageId);
    }
}
