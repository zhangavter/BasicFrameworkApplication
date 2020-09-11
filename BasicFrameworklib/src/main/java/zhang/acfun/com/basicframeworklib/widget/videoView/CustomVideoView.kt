package zhang.acfun.com.basicframeworklib.widget.videoView

import android.content.Context
import android.util.AttributeSet

/**
 * 视频播放,主要是因为手机的大小很多，不能保证原生的VideoView能实现全屏 * Created by lgl on 16/2/18.
 */
class CustomVideoView : ScalableVideoView {


    private var mVideoWidth = 0
    private var mVideoHeight = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle)



    fun setVideoRes(id: Int) {

        super.setRawData(id)
    }

}