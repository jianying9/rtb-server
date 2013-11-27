package com.rtb.config;

/**
 *
 * @author aladdin
 */
public class ActionNames {
    //-----------------------文件------------------------//
    //保存图片

    public final static String INSERT_IMAGE = "INSERT_IMAGE";
    //根据imageId查询图片数据
    public final static String INQUIRE_IMAGE_BY_KEY = "INQUIRE_IMAGE_BY_KEY";
    //-----------------------用户------------------------//
    //注册
    public final static String REGISTER = "REGISTER";
    //登录
    public final static String LOGIN = "LOGIN";
    //退出
    public final static String LOGOUT = "LOGOUT";
    //获取点数
    public final static String INQUIRE_POINT = "INQUIRE_POINT";
    //充值获取点数
    public final static String PAY_FOR_POINT = "PAY_FOR_POINT";
    //-----------------------用户------------------------//
    //新增广告
    public final static String INSERT_AD = "INSERT_AD";
    //查询当前登录用户广告列表
    public final static String INQUIRE_AD = "INQUIRE_AD";
    //增加广告剩余点数
    public final static String ADD_AD_POINT = "ADD_AD_POINT";
    //竞价
    public final static String AD_BIDDING = "AD_BIDDING";
    //查询广告位当前投放广告
    public final static String INQUIRE_POSITION_AD = "INQUIRE_POSITION_AD";
    //广告广告点击记录
    public final static String CLICK_AD = "CLICK_AD";
}
