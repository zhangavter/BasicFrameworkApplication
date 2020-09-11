package zhang.acfun.com.basicframeworklib.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import zhang.acfun.com.basicframeworklib.util.KeyBoardUtils
import zhang.acfun.com.basicframeworklib.util.StatusBarUtil
import zhang.acfun.com.basicframeworklib.util.ThemeUtil

/**
 * @author zxb
 * @Time 18-5-15 下午3:07
 */
abstract class BaseFunctionActivity : BaseFixOTranslucentActivity() {

    /**
     * 上下午context
     */
    protected val ctx by lazy { this }

    //private var dialog: LoadingDialog? = null

    @JvmField
    protected var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)
    }


    /**
     *是否需要状态栏是白色字体
     */
    open fun setStatusBarMode(needWhite: Boolean = ThemeUtil.isThemeNight()) =
        StatusBarUtil.setLightMode(ctx)
//        if (needWhite) {
//             StatusBarUtil.setLightMode(ctx)
//            false
//        } else
//            StatusBarUtil.setDarkMode(this)

    open fun needSliderActivity() = true

    open fun needActioinStatusBarColor() = true

    /**
     * 当前页面是否会键盘弹出，或是当前页面是否有输入的，默认为不需要输入
     */
    open fun needDealKeyBoard() = false


    /**
     *
     */
    fun showProgressDialog(cancle: Boolean = true) {

    }

    /**
     *
     */
    fun dissProgressDialog() {

    }

    override fun onPause() {
        super.onPause()
        dissProgressDialog()
        //dialog = null
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        try {
            if (needDealKeyBoard()) {
                if (null != this.currentFocus && KeyBoardUtils.isShouldHideInput(
                        currentFocus,
                        ev!!
                    )
                ) {
                    val mInputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (mInputMethodManager != null && this.currentFocus != null)
                        mInputMethodManager.hideSoftInputFromWindow(
                            this.currentFocus!!.windowToken,
                            0
                        )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     *
     */
   // fun loadingDialg() = dialog


    //这个方法每次个Activity只能设置一次
    // LayoutInflaterCompat.setFactory2(layoutInflater, this);
    // 这里需要用我们自己的layoutInflate
    //installViewFactory里面不用系统默认的
    override fun getDelegate(): AppCompatDelegate {
        return if (ThemeUtil.enableChaneTheme)
            SkinAppCompatDelegateImpl.get(this, this)
        else
            super.getDelegate()
    }
}