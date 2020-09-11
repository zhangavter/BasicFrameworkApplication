package zhang.acfun.com.basicframeworklib.skin.svg

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView



open class SvgImageView : AppCompatImageView {

    constructor(ctx: Context) : this(ctx, null)

    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr)


    /**
     *
     */
    open fun setImageResource(@DrawableRes resId: Int, colorId: Int) {
        super.setImageResource(resId)
    }

}