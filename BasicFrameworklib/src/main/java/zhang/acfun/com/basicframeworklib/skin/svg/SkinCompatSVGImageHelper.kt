package zhang.acfun.com.basicframeworklib.skin.svg

import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import skin.support.content.res.SkinCompatResources
import skin.support.widget.SkinCompatHelper
import zhang.acfun.com.basicframeworklib.R

/*
 * @author zxb
 * @Time 2018/11/24 22:29
 */
class SkinCompatSVGImageHelper(view: ImageView) : SkinCompatHelper() {

    private val imageView = view

    private var svgColor = INVALID_ID

    //    系统使用了这个，做了处理( mDrawable.mutate())，所以不行
    private var androidTint = INVALID_ID
    private var tint = INVALID_ID
    //    系统使用了这个，做了处理( mDrawable.mutate())，所以不行

    private var mSrcResId = INVALID_ID
    private var mSrcCompatResId = INVALID_ID

    /**
     *
     */
    fun loadFromAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.apply {
            var a: TypedArray? = null
            try {
                a = imageView.context.obtainStyledAttributes(this, R.styleable.SvgImageView, defStyleAttr, 0)
                svgColor = a.getResourceId(R.styleable.SvgImageView_svg_color, INVALID_ID)
//                tint = a.getResourceId(R.styleable.SvgImageView_tint, INVALID_ID)
//                androidTint = a.getResourceId(R.styleable.SvgImageView_android_tint, INVALID_ID)
                mSrcResId = a.getResourceId(R.styleable.SvgImageView_android_src, INVALID_ID)
                mSrcCompatResId = a.getResourceId(R.styleable.SvgImageView_srcCompat, INVALID_ID)
            } finally {
                a?.recycle()
            }
            applySkin()
        }
    }

    /**
     *
     */
    fun setImageResource(
        resId: Int, color: Int = when {
            svgColor != INVALID_ID -> svgColor
            tint != INVALID_ID -> tint
            androidTint != INVALID_ID -> androidTint
            else -> INVALID_ID
        }
    ) {
        mSrcCompatResId = resId
        svgColor = color
        applySkin()
    }

    /**
     *
     */
    private fun setSvgColor(drawableCompat: VectorDrawableCompat) {
        when {
            svgColor != INVALID_ID -> drawableCompat.setTint(SkinCompatResources.getColor(imageView.context, svgColor))
            tint != INVALID_ID -> drawableCompat.setTint(SkinCompatResources.getColor(imageView.context, tint))
            androidTint != INVALID_ID -> drawableCompat.setTint(
                SkinCompatResources.getColor(
                    imageView.context,
                    androidTint
                )
            )
        }
    }

    override fun applySkin() {

        val resources = imageView.resources
        val theme = imageView.context.theme

        val drawable: VectorDrawableCompat?
        mSrcCompatResId = checkResourceId(mSrcCompatResId)
        if (mSrcCompatResId != INVALID_ID) {
            drawable = VectorDrawableCompat.create(resources, mSrcCompatResId, theme)
        } else {
            mSrcResId = checkResourceId(mSrcResId)
            if (mSrcResId == INVALID_ID) {
                return
            }
            drawable = VectorDrawableCompat.create(resources, mSrcResId, theme)
        }

        if (drawable != null) {
            setSvgColor(drawable)
            imageView.setImageDrawable(drawable)
        }
    }
}