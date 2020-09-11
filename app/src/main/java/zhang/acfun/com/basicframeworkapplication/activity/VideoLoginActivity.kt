package zhang.acfun.com.basicframeworkapplication.activity

import kotlinx.android.synthetic.main.video_bg_login.*
import zhang.acfun.com.basicframeworkapplication.R
import zhang.acfun.com.basicframeworklib.base.BaseActivity

class VideoLoginActivity : BaseActivity() {
    private val vodeoPathID = R.raw.bgs

    override fun setLayoutView(): Int = R.layout.video_bg_login

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
        videoView.setVideoRes(vodeoPathID)
        videoView.isLooping = true
        videoView.setVolume(0f, 0f)
        startVideo()
    }

    private fun startVideo() {
        videoView?.prepare { videoView?.start() }
    }


    private fun stopVideo() {
        videoView?.pause()
    }


    override fun onDestroy() {
        videoView?.stop()
        super.onDestroy()
    }
}