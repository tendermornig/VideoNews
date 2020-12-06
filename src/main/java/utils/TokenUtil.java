package utils;

import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * @author 光
 */
public class TokenUtil {

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

    public static String updateToken(int userId, String strParam) {
        String token = encoderByMd5(strParam);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
}
