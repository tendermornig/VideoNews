package service;

import modles.BaseResponse;
import modles.UserModel;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import utils.ApiConfig;
import utils.DbUtil;
import utils.TokenUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public BaseResponse<String> registerUser(@RequestBody JSONObject jsonParam) {
        UserModel user = (UserModel) JSONObject.toBean(jsonParam, UserModel.class);
        BaseResponse<String> result = verificationUserParam(user);
        String sql = "insert ignore into user(user_account, user_password) values('"
                + user.getUserAccount() + "', '"
                + user.getUserPassword() + "')";
        if (DbUtil.Instance.updateDb(sql)) {
            result.setMsg(ApiConfig.SUCCESS);
            result.setCode(ApiConfig.SUCCESS_CODE_200);
        } else {
            result.setMsg(ApiConfig.SUCCESS);
            result.setCode(ApiConfig.FAIL_CODE_401);
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
        BaseResponse<String> result = verificationUserParam(user);
        result.setMsg(ApiConfig.SUCCESS);
        String sql = "select * from user where user_account = '"
                + user.getUserAccount()
                + "' and user_password = '"
                + user.getUserPassword() + "'";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null && rs.next()) {
                int userId = rs.getInt(1);
                String token = TokenUtil.updateToken(userId,
                        user.getUserAccount()
                                + System.currentTimeMillis()
                                + user.getUserPassword());
                result.setCode(ApiConfig.SUCCESS_CODE_200);
                result.setData(token);
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            DbUtil.Instance.close();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/isToken", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public BaseResponse<Boolean> verificationTokenCache(@RequestHeader("token") String token) {
        BaseResponse<Boolean> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setCode(ApiConfig.FAIL_CODE_402);
            result.setData(false);
            return result;
        }
        result.setMsg(ApiConfig.SUCCESS);
        result.setCode(ApiConfig.SUCCESS_CODE_200);
        result.setData(true);
        return result;
    }

    private BaseResponse<String> verificationUserParam(UserModel user) {
        BaseResponse<String> result = new BaseResponse<>();
        if (user.getUserAccount() == null || "".equals(user.getUserAccount())) {
            result.setData("缺少参数userAccount");
            return result;
        }
        if (user.getUserPassword() == null || "".equals(user.getUserPassword())) {
            result.setData("缺少参数userPassword");
            return result;
        }
        return result;
    }
}
