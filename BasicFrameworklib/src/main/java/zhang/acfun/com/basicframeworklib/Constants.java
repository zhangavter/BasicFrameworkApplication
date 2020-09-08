package zhang.acfun.com.basicframeworklib;

import android.content.Context;

/**
 * 静态常量
 **/
public class Constants {
    private static Context ctx;

    public static void init(Context context) {
        ctx = context;
    }

    public static Context getContext() {
        return ctx;
    }
}
