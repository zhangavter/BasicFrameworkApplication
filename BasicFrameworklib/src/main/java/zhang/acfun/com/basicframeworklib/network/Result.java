package zhang.acfun.com.basicframeworklib.network;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * @author star_Yang
 * @version V1.0
 * @Title: Result.java
 * @Package com.palmlink.weedu.model
 * @date 2014-1-5 下午9:49:36
 */
public class Result {

    /**
     * 成功失败
     */
    private ResultCode code;

    private Object obj;

    private String state = "";

    public Result(ResultCode success, Object obj) {
        this.code = success;
        this.obj = obj;
    }

    public ResultCode getCode() {
        return code;
    }

    public boolean isSuccess() {
        return code == ResultCode.RESULT_OK;
    }

    public Object getObj() {
        if (obj == null) {
            return "";
        }
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    /**
     * @return 返回结果必须有result 字段
     */
    public String getResult() {
        try {
            JSONObject json = JSON.parseObject(getObj().toString());
            if (json != null && json.containsKey("result")) {
                String result = json.getString("result");
                if (TextUtils.isEmpty(result) || "[]".equals(result)) {
                    return null;
                }
               /* if (json.containsKey("nd") && json.getBoolean("nd")) {
                    //需要解密
                    result = Security.resultString(result);
                }*/
                return result;
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 有些result中还嵌套了一个result
     *
     * @return
     */
    public String getResultFromResult() {

        JSONObject json = JSON.parseObject(getResult()
        );
        String listStr = null;
        if (json != null && json.containsKey("result")) {
            listStr = json.getString("result");
        }


        return listStr;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
