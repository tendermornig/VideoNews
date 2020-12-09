package utils;

import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 光
 */
public class TokenUtil {

    /**
     * 日期模式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 使用MD5加密+BASE64生成简单的token
     *
     * @param strParam 用户账号+用户密码+登录时间拼接成的字符串
     * @return 生成好的token
     */
    private static String encoderByMd5(String strParam) {
        String token = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            token = base64en.encode(md5.digest(strParam.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 用于添加或更新用户对应的登录token
     *
     * @param userId   需要添加或更新的用户
     * @param strParam token参数
     * @return 操作完成后需要将token返回
     */
    public static String updateToken(int userId, String strParam) {
        String token = encoderByMd5(strParam);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String updateDate = dateFormat.format(System.currentTimeMillis());
        String expireDate = dateFormat.format(System.currentTimeMillis() + ApiConfig.OVERDUE_INTERVAL);
        String sql = "insert into user_token(user_id, token, expire_time, update_time) values ("
                + userId + ", '"
                + token + "', '"
                + expireDate + "', '"
                + updateDate + "') on duplicate key update token = '"
                + token + "', expire_time = '"
                + expireDate + "', update_time = '"
                + updateDate + "'";
        DbUtil.Instance.updateDb(sql);
        return token;
    }

    public static boolean verificationToken(String token) {
        String sql = "select * from user_token where token = '"
                + token + "'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null && rs.next()) {
                Date expireTime = dateFormat.parse(rs.getString(3));
                return expireTime.getTime() - System.currentTimeMillis() < 0;
            }
        } catch (ParseException | SQLException troubles) {
            troubles.printStackTrace();
        }
        return true;
    }
}
