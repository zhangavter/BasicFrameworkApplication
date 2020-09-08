package zhang.acfun.com.basicframeworklib.view

import android.content.Context
import android.util.AttributeSet
import com.facebook.drawee.view.SimpleDraweeView
import zhang.acfun.com.basicframeworklib.R

/**
 * Created by Soli on 2016/7/22.
 */
class PercentImageView : SimpleDraweeView {

    private var isSquare = false
    private var heightPercent = 0.0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    /**
     *
     */
    private fun init(attrs: AttributeSet?) {
        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.PercentImageView)
            if (a != null) {
                heightPercent = a.getFloat(R.styleable.PercentImageView_heightPercent, 0.0f).toDouble()
                isSquare = a.getBoolean(R.styleable.PercentImageView_isSquare, false)
                if (isSquare) {
                    heightPercent = 1.0
                }
                a.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (heightPercent > 0.0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * heightPercent).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * @param heightPercent
     */
    fun setHeightPercent(heightPercent: Double) {
        this.heightPercent = heightPercent
        invalidate()
    }
}
