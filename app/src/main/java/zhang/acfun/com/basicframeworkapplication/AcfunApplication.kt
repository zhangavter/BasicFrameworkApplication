package zhang.acfun.com.basicframeworkapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tencent.mmkv.MMKV
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.loader.fresco.FrescoImageLoader
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater
import zhang.acfun.com.basicframeworklib.Constants
import zhang.acfun.com.basicframeworklib.skin.SkinCustomViewLayoutInflater
import zhang.acfun.com.basicframeworklib.skin.flycotablayout.app.SkinFlycoTabLayoutInflater

class AcfunApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Constants.envInit(!(true), "EnvDev")

        Constants.init(this)

        MMKV.initialize(this)

        loadSkin()

        Mojito.initialize(
            FrescoImageLoader.with(this),
            SketchImageLoadFactory()
        )
    }


    fun loadSkin() {
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤
            .addInflater(SkinMaterialViewInflater()) // material design
            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout
            .addInflater(SkinCardViewInflater()) // CardView v7
            .addInflater(SkinCustomViewLayoutInflater()) // hdodenhof/CircleImageView
            .addInflater(SkinFlycoTabLayoutInflater()) // FlycoTabLayout
            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤
            .loadSkin()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}