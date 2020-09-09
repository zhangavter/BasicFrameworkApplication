package zhang.acfun.com.basicframeworklib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import zhang.acfun.com.basicframeworklib.Constants;


/**
 * 网络相关工具类
 */
public class NetworkUtils {
    /**
     * 检查网络是否可用
     */
    public static boolean isNetworkAvailable() {
        try {
            NetworkInfo ifo = ((ConnectivityManager) Constants.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (ifo != null) {
                if (ifo.isAvailable()) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}