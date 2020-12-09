package modles;

import utils.ApiConfig;

import java.io.Serializable;

/**
 * @author 光
 */
public class BaseResponse<T> implements Serializable {

    private String msg = ApiConfig.FAIL;
    private int code = ApiConfig.FAIL_CODE_400;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
