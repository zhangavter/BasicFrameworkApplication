package zhang.acfun.com.basicframeworklib.skin.svg

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import skin.support.widget.SkinCompatBackgroundHelper
import skin.support.widget.SkinCompatSupportable
import zhang.acfun.com.basicframeworklib.skin.svg.SkinCompatSVGImageHelper
import zhang.acfun.com.basicframeworklib.skin.svg.SvgImageView

/*
 * @author zxb
 * @Time 2018/11/25 10:01
 */
class SkinSvgImageView : SvgImageView, SkinCompatSupportable {

    private var mBackgroundTintHelper: SkinCompatBackgroundHelper = SkinCompatBackgroundHelper(this)
    private var mImageHelper: SkinCompatSVGImageHelper

    constructor(ctx: Context) : this(ctx, null)

    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr)

        mImageHelper = SkinCompatSVGImageHelper(this)
        mImageHelper.loadFromAttributes(attrs, defStyleAttr)
    }

    override fun setBackgroundResource(@DrawableRes resId: Int) {
        super.setBackgroundResource(resId)
        mBackgroundTintHelper.onSetBackgroundResource(resId)
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        // Intercept this call and instead retrieve the Drawable via the image helper
        mImageHelper.setImageResource(resId)
    }

    /**
     *
     */

    override fun setImageResource(@DrawableRes resId: Int, colorId: Int) {
        mImageHelper.setImageResource(resId, colorId)
    }

    override fun applySkin() {
        mBackgroundTintHelper.applySkin()
        mImageHelper.applySkin()
    }
}