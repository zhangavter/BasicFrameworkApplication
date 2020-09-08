package zhang.acfun.com.basicframeworklib.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

/**
 * Android大小单位转换工具类
 *
 * @author wader
 */
public class DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性density；Context.getResources(). getDisplayMetrics().density）
     * @return
     */
    public static int px2dip(float pxValue, Context context) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param （DisplayMetrics类中属性density；Context.getResources(). getDisplayMetrics().density）
     * @return
     */
    public static int dip2px(float dipValue, Context ctx) {
        return (int) (dipValue * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }


    /**
     * dip转成pixels
     */
    public static int dipToPixels(int dip, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return (int) px;
    }


    /**
     * 得到屏幕尺寸系数
     */
    public static DisplayMetrics getDisplayMetrics(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(int spVal, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public static float sp2pxf(int spVal, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 动态设置视图的布局，图片尺寸是：600*290
     *
     * @param view
     */
    public static void AutoSetBrandPicSize(final View view) {
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int hight = view.getHeight();// 实际宽高单位px
                int width = view.getWidth();
                ViewGroup.LayoutParams linearParams = (ViewGroup.LayoutParams) view.getLayoutParams();
                linearParams.height = width * 290 / 600; //宽高比
                linearParams.width = width;
                view.setLayoutParams(linearParams);

            }
        });
    }

    /**
     * 测量组件的高度
     *
     * @param paramView 需要测量的组件
     * @return 组件的高度单位px
     */
    public static int getWidgetHight(View paramView) {

        return goMeasure(paramView).getMeasuredHeight();
    }

    /**
     * 测量组件的宽度
     *
     * @param paramView 需要测量的组件
     * @return 组件的宽度单位px
     */
    public static int getWidgetwidth(View paramView) {

        return goMeasure(paramView).getMeasuredWidth();
    }

    /**
     * 测量组件
     *
     * @param paramView 需要测量的组件
     * @return 返回测量后的组件
     */
    private static View goMeasure(View paramView) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        paramView.measure(w, h);
        return paramView;
    }

    /**
     * 获取状态栏的高度
     *
     * @param ctx
     * @return
     */
    public static int getStatusBarHeight(Context ctx) {
        try {
            int result = 0;
            int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = ctx.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        } catch (Exception e) {
            return DisplayUtil.dip2px(25, ctx);
        }
    }


    /**
     * 获取底部 navigation bar 高度(华为手机和魅族手机存在)
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 设置背景
     **/
    public static void setBackgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }

    /**
     * 渐变色
     **/
    public static int getColorWithAlpha(double alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeightPixels(Context context) {
        // 获取当前屏幕
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();

//        return dm.heightPixels
        boolean hasNav = checkDeviceHasNavigationBar(context);
        if (hasNav)
            return displayMetrics.heightPixels - getNavigationBarHeight(context);
        return displayMetrics.heightPixels;
    }

    /**
     * 检测是否有虚拟按键
     *
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier > 0)
            hasNavigationBar = resources.getBoolean(identifier);

        return hasNavigationBar;

    }

}
