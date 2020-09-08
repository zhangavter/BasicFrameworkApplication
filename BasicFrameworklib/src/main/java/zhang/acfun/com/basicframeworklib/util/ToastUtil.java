package zhang.acfun.com.basicframeworklib.util;

import android.app.Activity;
import android.widget.Toast;

import zhang.acfun.com.basicframeworklib.Constants;


public class ToastUtil {
    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public static Toast showToast(String info) {
        Toast toast = Toast.makeText(Constants.getContext(), info, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }


    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public static Toast showToast(Activity activity, String info) {
        Toast toast = Toast.makeText(activity, info, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public static void showToastLong(String info) {
        Toast.makeText(Constants.getContext(), info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     */
    public static void showToast(int resId) {
        Toast.makeText(Constants.getContext(), Constants.getContext().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showToastLong(int resId) {
        Toast.makeText(Constants.getContext(), resId, Toast.LENGTH_LONG).show();
    }

}
