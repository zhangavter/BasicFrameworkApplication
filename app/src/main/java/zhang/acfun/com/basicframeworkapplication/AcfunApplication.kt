package zhang.acfun.com.basicframeworkapplication

import android.app.Application
import com.tencent.mmkv.MMKV
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.loader.fresco.FrescoImageLoader
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory
import zhang.acfun.com.basicframeworklib.Constants

class AcfunApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Constants.envInit(
            !(true), "EnvDev"
        )

        Constants.init(this)

        MMKV.initialize(this)

        Mojito.initialize(
            FrescoImageLoader.with(this),
            SketchImageLoadFactory()
        )
    }
}