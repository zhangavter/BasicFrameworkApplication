package zhang.acfun.com.basicframeworklib.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import zhang.acfun.com.basicframeworklib.Constants;
import zhang.acfun.com.basicframeworklib.R;


/**
 * 利用Fresco 加载图片
 *
 * @author zxb
 * @Time 18-5-31 下午4:53
 */
public class ImageLoader {

    /**
     * @param image
     * @param path
     * @param width
     * @param height
     */
    public static void loadImageByPath(SimpleDraweeView image, String path, int width, int height) {
        loadImageByPath(image, path, width, height, false);
    }

    /**
     * 加载本地图片
     */
    public static void loadImageByPath(SimpleDraweeView image, String path, int width, int height, boolean isGifAutoPlay) {
        loadImageByPath(image, path, width, height, isGifAutoPlay, true);
    }

    /**
     * @param image
     * @param path
     * @param width
     * @param height
     * @param isGifAutoPlay
     * @param isDealNight
     */
    public static void loadImageByPath(SimpleDraweeView image, String path, int width, int height, boolean isGifAutoPlay, boolean isDealNight) {
        loadImage(image, "file://" + path, false, false, isGifAutoPlay, width, height, isDealNight, null, null);
    }

    /**
     * @param image
     * @param url
     */
    public static void loadImage(SimpleDraweeView image, String url) {

        if (image == null) return;

        int width = image.getWidth();
        int height = image.getHeight();

        loadImage(image, url, width, height);
    }

    /**
     * @param image
     * @param url
     * @param width
     * @param height
     */
    public static void loadImage(SimpleDraweeView image, String url, int width, int height) {
        loadImage(image, url, false, false, false, width, height, true, null, null);
    }

    /**
     * @param image
     * @param url
     * @param isAutoPlay
     * @param width
     * @param height
     */
    public static void loadImage(SimpleDraweeView image, String url, boolean isAutoPlay, int width, int height) {
        loadImage(image, url, false, false, isAutoPlay, width, height, true, null, null);
    }

    public static void loadImage(SimpleDraweeView image, String url, int width, int height, ControllerListener controllerListener) {
        loadImage(image, url, false, false, false, width, height, true, null, controllerListener);
    }

    /**
     * @param image
     * @param url
     * @param isGif
     * @param gifNeedCut
     * @param isAutoPlay
     * @param width
     * @param height
     * @param dealNight
     * @param processor
     * @param controllerListener
     */
    public static void loadImage(SimpleDraweeView image,
                                 String url,
                                 boolean isGif,
                                 boolean gifNeedCut,
                                 boolean isAutoPlay,
                                 int width,
                                 int height,
                                 boolean dealNight,
                                 BasePostprocessor processor,
                                 ControllerListener controllerListener) {

        if (image == null) return;

        if (TextUtils.isEmpty(url)) {
           // int id = dealNight && ThemeUtil.INSTANCE.isThemeNight() ? R.drawable.default_loading_back_10dp_ng_night : R.drawable.default_loading_back_10dp_ng;
            int id =  R.drawable.default_loading_back_10dp_ng;
            loadResPic(image, id);
            return;
        }

        int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0f, Constants.getContext().getResources().getDisplayMetrics());

        if (width <= 0) {
            width = defaultSize;
        }
        if (height <= 0) {
            height = defaultSize;
        }


        String tempPath = url;
      /*  if (url.startsWith("http")) {
            tempPath = JokerImageUtil.INSTANCE.getImageInfo(url).getUrl();
            url = JokerImageUtil.INSTANCE.getRequestUrl(url, width, height, gifNeedCut);
        }*/

        try {
            RoundingParams params = image.getHierarchy().getRoundingParams();
            if (params == null)
                params = new RoundingParams();

            if (isGif || FileUtil.INSTANCE.isGIF(tempPath)) {
                params.setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR);
               // params.setOverlayColor(SkinCompatResources.getColor(Constant.getContext(), R.color.B1));
                params.setOverlayColor(ContextCompat.getColor(Constants.getContext(),R.color.C6));
            } else {
                params.setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
            }

            image.getHierarchy().setRoundingParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log(url);


        ImageRequestBuilder imageRequest = getImageRequestBuilder(url, width, height, dealNight);

        if (processor != null) {
            imageRequest.setPostprocessor(processor);
        }

        PipelineDraweeControllerBuilder draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest.build())
                .setOldController(image.getController())
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(isAutoPlay)
                .setControllerListener(controllerListener);

        image.setController(draweeController.build());
    }

    /**
     * @param url
     * @return
     */
    private static ImageRequestBuilder getImageRequestBuilder(String url, int width, int height, boolean dealNight) {
        ImageRequestBuilder imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))
                .setRotationOptions(RotationOptions.autoRotate());

        //不需要夜间模式的图片亮度
//        if (dealNight && ThemeUtil.INSTANCE.isThemeNight()) {
//            imageRequest.setPostprocessor(new NightPostprocessor(Utils.INSTANCE.MD5(url)));
//        }

        return imageRequest;
    }

    /**
     * 加载本地Res图片（drawable,mipmap图片）
     *
     * @param image
     * @param id
     */
    public static void loadResPic(SimpleDraweeView image, int id) {
        loadResPic(image, id, true);
    }

    /**
     * @param image
     * @param id
     * @param dealNight
     */
    public static void loadResPic(SimpleDraweeView image, int id, boolean dealNight) {
        loadSimpleImage(image, Uri.parse("res://" + Constants.getContext().getPackageName() + "/" + id), dealNight);
    }

    /**
     * @param image
     * @param uri
     */
    public static void loadSimpleImage(SimpleDraweeView image, Uri uri) {
        loadSimpleImage(image, uri, true);
    }

    /**
     * @param image
     * @param uri
     * @param width
     * @param height
     */
    public static void loadImageByUri(SimpleDraweeView image, Uri uri, int width, int height, boolean isGifAutoPlay) {
        if (image == null || uri == null)
            return;

        ImageRequestBuilder
                imageRequest = getImageRequestBuilder(uri.toString(), width, height, false);

        PipelineDraweeControllerBuilder draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest.build())
                .setOldController(image.getController())
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(isGifAutoPlay);

        image.setController(draweeController.build());

    }

    /**
     * @param image
     * @param uri
     * @param dealNight
     */
    public static void loadSimpleImage(SimpleDraweeView image, Uri uri, boolean dealNight) {

        if (image == null || uri == null)
            return;

        log(uri.toString());

        ImageRequestBuilder imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setRotationOptions(RotationOptions.autoRotate());

       /* if (dealNight && ThemeUtil.INSTANCE.isThemeNight()) {
            imageRequest.setPostprocessor(new NightPostprocessor(Utils.INSTANCE.MD5(uri.toString())));
        }*/

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest.build())
                .setAutoPlayAnimations(true)
                .setOldController(image.getController())
                .build();
        image.setController(draweeController);
    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     */
    private static void log(String url) {
        //MLog.d("多媒体加载", url);
    }
}
