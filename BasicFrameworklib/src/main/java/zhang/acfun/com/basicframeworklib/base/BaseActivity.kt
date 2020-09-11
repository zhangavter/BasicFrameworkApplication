package zhang.acfun.com.basicframeworklib.base

import android.os.Bundle

abstract class BaseActivity : BaseFunctionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(setLayoutView())
        initView()
        initListener()
        initData()
    }


    protected abstract fun setLayoutView(): Int
    protected abstract fun initView()
    protected abstract fun initListener()
    protected abstract fun initData()
}