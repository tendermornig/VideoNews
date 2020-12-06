package service;

import modles.BaseResponse;
import modles.UserModel;
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
@RequestMapping("/user")
public class UserService {

    /**
     * 注册方法
     *
     * @param jsonParam 客户提交的用户账号与用户密码json串
     * @return 注册结果
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONObject registerUser(@RequestBody JSONObject jsonParam) {
        UserModel user = (UserModel) JSONObject.toBean(jsonParam, UserModel.class);
        JSONObject result = new JSONObject();
        result.element("msg", ApiConfig.FAIL);
        result.element("code", ApiConfig.FAIL_CODE);
        if (user.getUserAccount() == null || "".equals(user.getUserAccount())) {
            result.element("error", "缺少参数userAccount");
            return result;
        }
        if (user.getUserPassword() == null || "".equals(user.getUserPassword())) {
            result.element("error", "缺少参数userPassword");
            return result;
        }
        int executeResult = 0;
        Connection c = null;
        Statement s = null;
        try {
            String sql = "insert into user(username, password) values('" + user.getUserAccount() + "', '" + user.getUserPassword() + "')";
            c = DbUtil.Instance.getMySqlConn();
            s = c.createStatement();
            executeResult = s.executeUpdate(sql);
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            if (executeResult > 0) {
                result.element("msg", ApiConfig.SUCCESS);
                result.element("code", ApiConfig.SUCCESS_CODE);
            }
            DbUtil.Instance.close(s, c, null);
        }
        return result;
    }

    /**
     * 登录方法
     *
     * @param jsonParam 客户端提交的用户账号与用户密码json串
     * @return 登录结果 如登录成功则返回登录成功msg与code与token 登录失败则返回登录失败msg与code
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponse<String> userLogin(@RequestBody JSONObject jsonParam) {
        UserModel user = (UserModel) JSONObject.toBean(jsonParam, UserModel.class);
        BaseResponse<String> result = new BaseResponse<>();
        result.setMsg(ApiConfig.FAIL);
        result.setCode(ApiConfig.FAIL_CODE);
        if (user.getUserAccount() == null || "".equals(user.getUserAccount())) {
            result.setData("缺少参数userAccount");
            return result;
        }
        if (user.getUserPassword() == null || "".equals(user.getUserPassword())) {
            result.setData("缺少参数userPassword");
            return result;
        }
        boolean loginResult = false;
        Connection c = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            String sql = "select * from user where username = '" + user.getUserAccount() + "' and password = '" + user.getUserPassword() + "'";
            c = DbUtil.Instance.getMySqlConn();
            s = c.createStatement();
            rs = s.executeQuery(sql);
            loginResult = rs.next();
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            if (loginResult) {
                String token = encoderByMd5(user.getUserAccount() + System.currentTimeMillis() + user.getUserPassword());
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE);
                result.setData(token);
            }
            DbUtil.Instance.close(s, c, rs);
        }
        return result;
    }

    /**
     * 使用MD5加密生成简单的token
     *
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
