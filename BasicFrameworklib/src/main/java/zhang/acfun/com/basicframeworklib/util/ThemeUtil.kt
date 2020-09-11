package zhang.acfun.com.basicframeworklib.util

import skin.support.SkinCompatManager
import skin.support.utils.SkinPreference

/*
 * 换肤切换开关
 * @author zxb
 * @Time 2018/11/10 09:59
 */
object ThemeUtil {

    /**
     * 是否使能换肤
     */
    var enableChaneTheme = true

    /**
     *  toggle
     */
    fun changeTheme() {
        if (enableChaneTheme) {
            if (isThemeNight()) {
                SkinCompatManager.getInstance().restoreDefaultTheme()
            } else {
                SkinCompatManager.getInstance()
                    .loadSkin("night", null, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)// 后缀加载
            }
        }
    }

    /**
     * 是否是夜间模式
     */
    fun isThemeNight() =
        if (enableChaneTheme) "night" == SkinPreference.getInstance().skinName else false
}