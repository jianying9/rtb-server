package com.rtb.ad.entity;

import com.wolf.framework.data.BasicTypeEnum;
import com.wolf.framework.service.parameter.Parameter;
import com.wolf.framework.service.parameter.ParameterConfig;
import com.wolf.framework.service.parameter.ParametersConfig;

/**
 *
 *
 * @author aladdin
 */
@ParametersConfig()
public final class AdParameter implements Parameter {

    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_60, desc = "用户imei")
    private String imei;
    //
    @ParameterConfig(basicTypeEnum = BasicTypeEnum.CHAR_4000, desc = "imei的标签")
    private String tagIds;
}
