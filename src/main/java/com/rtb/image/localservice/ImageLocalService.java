package com.rtb.image.localservice;

import com.rtb.image.entity.ImageEntity;
import com.wolf.framework.local.Local;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface ImageLocalService extends Local{

    public ImageEntity insertImage(Map<String, String> parameterMap);
    
    public ImageEntity inquireByImageId(String imageId);
    
    public List<ImageEntity> inquireByImageIdList(List<String> imageIdList);
    
    public void delete(String imageId);
}
