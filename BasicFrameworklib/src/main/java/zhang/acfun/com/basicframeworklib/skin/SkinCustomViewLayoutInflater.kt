package zhang.acfun.com.basicframeworklib.skin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import skin.support.app.SkinLayoutInflater
import zhang.acfun.com.basicframeworklib.skin.svg.SkinSvgImageView

/*
 * @author zxb
 * @Time 2018/11/25 10:08
 */
class SkinCustomViewLayoutInflater : SkinLayoutInflater {

    override fun createView(context: Context, name: String?, attrs: AttributeSet): View? {
        return when (name) {
            "zhang.acfun.com.basicframeworklib.skin.svg.SvgImageView" -> SkinSvgImageView(context, attrs)
            else -> {
                null
            }
        }
    }
}