package utils;

/**
 * @author 光
 * 配置常量类
 */
public class ApiConfig {
    /**
     * 访问成功时的msg
     */
    public static final String SUCCESS = "success";
    /**
     * 访问失败时的msg
     */
    public static final String FAIL = "fail";
    /**
     * 访问成功时的code
     */
    public static final int SUCCESS_CODE_200 = 200;
    /**
     * 访问失败时的code
     */
    public static final int FAIL_CODE_400 = 400;
    /**
     * 注册失败但访问成功时的code
     */
    public static final int FAIL_CODE_401 = 401;
    /**
     * token过期code
     */
    public static final int FAIL_CODE_402 = 402;
    /**
     * 获取视频资讯列表数量参数错误
     */
    public static final int FAIL_CODE_403 = 403;
    /**
     * token过期间隔 单位毫秒
     */
    public static final long OVERDUE_INTERVAL = 1000 * 60 * 60;
}
