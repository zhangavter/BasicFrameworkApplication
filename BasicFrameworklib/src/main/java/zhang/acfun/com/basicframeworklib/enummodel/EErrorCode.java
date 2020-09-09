package zhang.acfun.com.basicframeworklib.enummodel;

public enum EErrorCode {


    access_to_many("access too many"), //访问平率过高，错误码
    third_register_alert("third.register.alert"), //手机号已经注册返回码
    user_not_login("USER_NOT_LOGIN_DEFAULT"), //可能是用户sign错误或过期
    user_other_login("login.other.terminal"), //账号在其他设备登录
    ServiceErr("90009"); //服务器维护


    String value;

    EErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
