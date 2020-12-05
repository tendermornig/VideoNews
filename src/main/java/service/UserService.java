package service;

import modles.BaseResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import utils.ApiConfig;
import utils.DbUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author 光
 */
@RestController
@RequestMapping("/User")
public class UserService {

    /**
     * 注册方法
     * @param jsonParam 客户提交的用户账号与用户密码json串
     * @return 注册结果
     */
    @ResponseBody
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONObject registerUser(@RequestBody JSONObject jsonParam) {
        String userAccount = jsonParam.get("userAccount").toString();
        String userPassword = jsonParam.get("userPassword").toString();
        JSONObject result = new JSONObject();
        int executeResult = 0;
        Connection c = null;
        Statement s = null;
        try {
            String sql = "insert into user(username, password) values('" + userAccount + "', '" + userPassword + "')";
            c = DbUtil.Instance.getMySqlConn();
            s = c.createStatement();
            executeResult = s.executeUpdate(sql);
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            if (executeResult > 0) {
                result.element("msg", ApiConfig.SUCCESS);
                result.element("code", ApiConfig.SUCCESS_CODE);
            } else {
                result.element("msg", ApiConfig.FAIL);
                result.element("code", ApiConfig.FAIL_CODE);
            }
            DbUtil.Instance.close(s, c, null);
        }
        return result;
    }

    /**
     * 登录方法
     * @param jsonParam 客户端提交的用户账号与用户密码json串
     * @return 登录结果 如登录成功则返回登录成功msg与code与token 登录失败则返回登录失败msg与code
     */
    @ResponseBody
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponse<String> userLogin(@RequestBody JSONObject jsonParam) {
        String userAccount = jsonParam.get("userAccount").toString();
        String userPassword = jsonParam.get("userPassword").toString();
        boolean loginResult = false;
        Connection c = null;
        Statement s = null;
        ResultSet rs = null;
        BaseResponse<String> result = new BaseResponse<>();
        try {
            String sql = "select * from user where username = '" + userAccount + "' and password = '" + userPassword + "'";
            c = DbUtil.Instance.getMySqlConn();
            s = c.createStatement();
            rs = s.executeQuery(sql);
            loginResult = rs.next();
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            if (loginResult) {
                String token = encoderByMd5(userAccount + userPassword + System.currentTimeMillis());
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE);
                result.setData(token);
            } else {
                result.setMsg(ApiConfig.FAIL);
                result.setCode(ApiConfig.FAIL_CODE);
            }
            DbUtil.Instance.close(s, c, rs);
        }
        return result;
    }

    /**
     * 使用MD5加密生成简单的token
     * @param strParam 用户账号+用户密码+登录时间拼接成的字符串
     * @return 生成好的token
     */
    private String encoderByMd5(String strParam) {
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
}
