package zhang.acfun.com.basicframeworklib.model;

import java.io.Serializable;

public class ResultModel implements Serializable {

    private String invalidParams;
    private String isHasResult;
    private String msg;
    private String result;
    private String state;

    public String getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(String invalidParams) {
        this.invalidParams = invalidParams;
    }

    public String getIsHasResult() {
        return isHasResult;
    }

    public void setIsHasResult(String isHasResult) {
        this.isHasResult = isHasResult;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
