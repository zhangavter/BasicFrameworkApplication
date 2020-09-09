
package zhang.acfun.com.basicframeworklib.network;

public enum ResultCode {

    /**
     * 成功 true
     */
    RESULT_OK(0x00),

    /**
     * 失败 false
     */
    RESULT_FAILED(0x1),

    /**
     * 网络连接有问题
     **/
    NETWORK_TROBLE(0x2),

    /**
     * 访问平率过高，错误码
     **/
    ACCESS_TO_MANY(0x3),

    /**
     * 手机号已经注册返回码
     **/
    THIRD_REGISTER_ALERT(0x4),

    /**
     * 用户sign 错误或者过期
     **/
    USER_NOT_LOGIN_SIGN_ERROR(0x5),
    /**
     * 服务器维护
     **/
    ServiceErr(0x6),

    /**
     * 定位失败
     **/
    LOCATION_ERROR(0x7),

    /**
     * 设备在其他地方登录
     **/
    OTHER_LOGIN(0x8);

    static ResultCode mapIntToValue(final int stateInt) {
        for (ResultCode value : ResultCode.values()) {
            if (stateInt == value.getIntValue()) {
                return value;
            }
        }

        // If not, return default
        return RESULT_FAILED;
    }

    private int mIntValue;

    ResultCode(int intValue) {
        mIntValue = intValue;
    }

    int getIntValue() {
        return mIntValue;
    }


}
