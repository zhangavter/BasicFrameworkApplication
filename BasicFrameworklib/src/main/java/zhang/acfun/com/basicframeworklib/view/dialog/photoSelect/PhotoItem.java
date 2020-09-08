package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.model.PhotoModel;
import zhang.acfun.com.basicframeworklib.util.FileUtil;
import zhang.acfun.com.basicframeworklib.util.ImageLoader;
import zhang.acfun.com.basicframeworklib.util.ToastUtil;
import zhang.acfun.com.basicframeworklib.view.ChooseButton;
import zhang.acfun.com.basicframeworklib.view.PercentImageView;
import zhang.acfun.com.basicframeworklib.view.dialog.PhotoSelectorDialog;


public class PhotoItem extends LinearLayout implements ChooseButton.OnCheckListener {

    private PercentImageView ivPhoto;
    private ChooseButton cbPhoto;
    private TextView tv_GIF;
    private onPhotoItemCheckedListener listener;
    private boolean isCheckAll;
    private PhotoModel photo;
    private boolean isCheckMore = false;//是否多选
    private RelativeLayout layout_mode_camera;
    private FrameLayout layotu_mode_photo;

    public PhotoItem(Context context) {
        super(context);
        init(context);
    }

    public PhotoItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this, true);
        layotu_mode_photo = findViewById(R.id.layotu_mode_photo);
        layout_mode_camera = findViewById(R.id.layout_mode_camera);
        ivPhoto = findViewById(R.id.iv_photo_lpsi);
        cbPhoto = findViewById(R.id.cb_photo_lpsi);
        tv_GIF = findViewById(R.id.tv_GIF);

        layotu_mode_photo.setVisibility(VISIBLE);
        layout_mode_camera.setVisibility(GONE);

        cbPhoto.setOnCheckListener(this);

        isCheckMore = PhotoSelectorDialog.isCheckMore();
        if (isCheckMore) {
            //多选
            cbPhoto.showCheckText(true);
            cbPhoto.setSrc(R.drawable.ic_choice_xuankuang_icon, R.drawable.ic_shoot_choose_pic);
        } else {
            //单选
            cbPhoto.setSrc(R.drawable.ic_choice_xuankuang_icon, R.drawable.ic_choice_gougou_check);
        }
    }


    public void setOnPhotoItemCheckedListener(onPhotoItemCheckedListener listener) {
        this.listener = listener;
    }


    /**
     * 设置是否选中
     */
    public void setCheck(boolean check) {
        cbPhoto.setCheck(check, false);
        itemDark(check);
    }

    /**
     * 设置是否显示选中框
     */
    public void setShowCheckBox(boolean isShowCheckbox) {
        cbPhoto.setVisibility(isShowCheckbox ? VISIBLE : GONE);
    }

    @Override
    public boolean OnCheck(boolean isChecked) {

        if (isChecked && !PhotoSelectorDialog.canCheckMore() && isCheckMore) {
            ToastUtil.showToast("最多只能选择" + PhotoSelectorDialog.MAX_NUM + "张图片");
            return true;
        }
        photo.setCheck(isChecked);

        itemDark(isChecked);

        if (!isCheckAll && listener != null) {
            listener.onCheckedChanged(photo, null, isChecked); // 调用主界面回调函数
        }
        cbPhoto.setCheck(isChecked, false);
        return true;//返回true，表示所有事件，当前执行完
    }


    /**
     * 让图片变暗或者变亮
     */
    private void itemDark(boolean isDark) {
        // 让图片变暗或者变亮
        if (isDark) {
            setDrawingable();
            ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            ivPhoto.clearColorFilter();
        }
    }

    public void setData(PhotoModel photo, int width, boolean isGifAutoPlay) {
        this.photo = photo;
        layotu_mode_photo.setVisibility(VISIBLE);
        layout_mode_camera.setVisibility(GONE);

       // ivPhoto.setPlaceholderImage(R.drawable.select_back);
        if (FileUtil.INSTANCE.isAndroidQorAbove())
//            ivPhoto.setImageURI(photo.getFileUri(),getContext());
            ImageLoader.loadImageByUri(ivPhoto, photo.getFileUri(), width, width, isGifAutoPlay);
        else
            ImageLoader.loadImageByPath(ivPhoto, photo.getOriginalPath(), width, width, isGifAutoPlay);

        cbPhoto.setCheckedText("" + photo.getCheckIndex());


        if (0 == photo.getType()) {
            //图片
            boolean isGIF = FileUtil.INSTANCE.isGIF(photo.getOriginalPath());
            tv_GIF.setVisibility(isGIF ? View.VISIBLE : View.GONE);
        } else if (1 == photo.getType()) {
            //视频
            tv_GIF.setVisibility(View.VISIBLE);
            tv_GIF.setText(DateUtils.formatElapsedTime(photo.getVideoTime() / 1000));
        } else {
            tv_GIF.setVisibility(View.GONE);
        }


    }

    /**
     * 设置成相机拍照的风格
     */
    public void setCameraStyle() {
        layotu_mode_photo.setVisibility(INVISIBLE);
        layout_mode_camera.setVisibility(VISIBLE);
    }


    /**
     * 刷新选中时的数字
     */
    public void notifyCheckNumChanged(PhotoModel photo) {
        this.photo = photo;
        cbPhoto.setCheckedText("" + photo.getCheckIndex());
    }

    /**
     * 获取当前使用的PhotoModel
     */
    public PhotoModel getPhotoModel() {
        return photo;
    }

    private void setDrawingable() {
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
    }

//    @Override
//    public void setSelected(boolean selected) {
//        if (photo == null) {
//            return;
//        }
//        isCheckAll = true;
//        cbPhoto.setCheck(selected);
//        isCheckAll = false;
//    }


    /**
     * 图片Item选中事件监听器
     */
    public interface onPhotoItemCheckedListener {
        void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked);
    }


    /**
     * 图片Item点击事件监听器
     */
    public interface onPhotoItemClickListener {
        void onClick(PhotoModel photoModel, int real_position);
    }

}
