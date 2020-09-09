package zhang.acfun.com.basicframeworklib;

import android.content.Context;

/**
 * 静态常量
 **/
public class Constants {
    private static Context ctx;
    public static boolean EnDebug = true;

    public static String SERVER="";
    public static String ServiceErrMsg = "程序哥哥正在维护";


    public static void init(Context context) {
        ctx = context;
    }

    public static Context getContext() {
        return ctx;
    }

    public static void envInit(Boolean isDebug, String currentChannel) {
        EnDebug = isDebug;
        //开发环境
        if (currentChannel.contains("EnvPre")) {
            //dev环境
          /*  SERVER = "https://www.com/";
            WAP_SERVER = "https://www.com/";
            WAP = "https://www.com/";*/
        } else if (currentChannel.contains("EnvDev")) {
            //ci环境
        } else {
            //线上环境
        }
    }
}
