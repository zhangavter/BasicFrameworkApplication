package zhang.acfun.com.basicframeworklib.view.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tbruyelle.rxpermissions3.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.model.AlbumModel;
import zhang.acfun.com.basicframeworklib.model.PhotoModel;
import zhang.acfun.com.basicframeworklib.util.DisplayUtil;
import zhang.acfun.com.basicframeworklib.util.FileUtil;
import zhang.acfun.com.basicframeworklib.util.StatusBarUtil;
import zhang.acfun.com.basicframeworklib.util.ToastUtil;
import zhang.acfun.com.basicframeworklib.view.SlidedownView;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumController;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumListView;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnLocalAlbumListener;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnLocalReccentListener;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnMediaListener;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.PhotoAdapter;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.SpacingDecoration;
import zhang.acfun.com.basicframeworklib.widget.PhotoSelectorDomain;
import zhang.acfun.com.basicframeworklib.widget.bottomsheet.BaseSheetFragment;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

/**
 * 时间：2018/11/13
 * 作者：zxb
 */
public class PhotoSelectorDialog extends BaseSheetFragment {

    public static int MAX_NUM = Integer.MAX_VALUE;//最多选中多少张图片
    public static LinkedHashMap<String, PhotoModel> mCheckPhoto;//选中的照片
    private RecyclerView rv_photo_list;
    private LinearLayout layout_title_photo;
    private AlbumListView album_list;
    private ImageView iv_arrow;
    // private LoadingInsideView picLoadingView;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoAdapter adapter;
    private TextView tv_title_photo, tv_end_photo;
    private boolean isShowGif = true;//是否显示gif
    private boolean isShowCamera = false;//是否显示相机
    private boolean isShowCheckbox = true;//是否显示选中的框框
    private OnEndClickListener onEndClickListener;

    private SlidedownView folderSelect;

    private int topOffset = 0;
    private String lastMenuSelect = "";
    private boolean fromOnItemClick = false;
    private AlbumController.Type mType = AlbumController.Type.PHOTO;

    @Override
    protected int getContentView() {
        return R.layout.dialog_photo_selector;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        topOffset = StatusBarUtil.getStatusBarHeight(getActivity()) + DisplayUtil.dip2px(68, getCtx());
        setTopOffset(topOffset);

        initFolderSelect(view);

        mCheckPhoto = new LinkedHashMap<>();
        // picLoadingView = view.findViewById(R.id.picLoadingView);
//        picLoadingView.setloadingBackgroundColor(ThemeUtil.INSTANCE.isThemeNight() ? Color.parseColor("#161823") : Color.parseColor("#FFFFFF"));
        rv_photo_list = view.findViewById(R.id.rv_photo_list);
        layout_title_photo = view.findViewById(R.id.layout_title_photo);
        tv_title_photo = view.findViewById(R.id.tv_title_photo);
        iv_arrow = view.findViewById(R.id.iv_arrow);
        tv_end_photo = view.findViewById(R.id.tv_end_photo);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        rv_photo_list.setLayoutManager(manager);

        adapter = new PhotoAdapter(getContext());
        adapter.isShowCheckbox = isShowCheckbox;
        adapter.setIsGifAutoPlay(false);//不自动播放gif
//        if (isShowCamera) {
//            adapter.addHeaderView(LayoutInflater.from(getCtx()).inflate(R.layout.item_photo_selector, null));
//        }
        rv_photo_list.setAdapter(adapter);


        int spacing = DisplayUtil.dip2px(5, getContext());  // 50px
        rv_photo_list.addItemDecoration(new SpacingDecoration(spacing, spacing, false));

        photoSelectorDomain = new PhotoSelectorDomain(getContext().getApplicationContext());

        if (AlbumController.Type.VIDEO == mType) {
            layout_title_photo.setVisibility(View.GONE);
        } else {
            layout_title_photo.setVisibility(View.VISIBLE);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initListener() {

        layout_title_photo.setOnClickListener((View view) -> toggleMenu());

        album_list.setOnItemClickListener((int position, AlbumModel model) -> {
            toggleMenu();
            fromOnItemClick = true;
            tv_title_photo.setText(model.getName());
        });

        adapter.setOnPhotoItemClickListener((photoModel, real_position) -> {
            if (isShowCheckbox) {
             /*   DoPicUtils.INSTANCE.openPicFullScreen_(getContext(), new ArrayList(adapter.getList()), real_position, true,
                        true, false, new PreviewActivityListener());*/
            } else {

                mCheckPhoto.clear();
                mCheckPhoto.put(photoModel.getOriginalPath(), photoModel);
                End(false);
            }
        });

        adapter.setOnPhotoItemCheckedListener((photoModel, buttonView, isChecked) -> onCheckChange(photoModel, isChecked));

        tv_end_photo.setEnabled(false);
        tv_end_photo.setOnClickListener(view -> End(true));

    }

    @Override
    protected void initData() {

        checkPermissionAndAction();
    }

    /**
     * @return
     */
    private int getViewHeight() {
        int height = this.getResources().getDisplayMetrics().heightPixels - DisplayUtil.dip2px(55, getCtx()) - topOffset;
        return (height * 3) / 4;
    }

    /**
     *
     */
    private void initFolderSelect(@NotNull View view) {
        folderSelect = new SlidedownView(getActivity(), view.findViewById(R.id.behindheadview));
        album_list = new AlbumListView(getActivity());

        folderSelect.setContentView(album_list, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getViewHeight()));

        folderSelect.setCallback(new SlidedownView.onLifecallBack() {
            @Override
            public void isDisplay(boolean isShow) {
                iv_arrow.setRotation(isShow ? 180 : 0);
                getMenuData();
            }

            @Override
            public void start(boolean isShow) {
            }
        });
    }

    /**
     *
     */
    private void getMenuData() {
        if (fromOnItemClick) {
            fromOnItemClick = false;
            String menu = tv_title_photo.getText().toString();
            if (!menu.equals(lastMenuSelect)) {
                lastMenuSelect = menu;
                // 更新照片列表
                showProgress(true);
                if (menu.equals(getResources().getString(R.string.str_allPicture)))
                    photoSelectorDomain.getReccent(mType, reccentListener);
                else
                    photoSelectorDomain.getAlbum(menu, reccentListener); // 获取选中相册的照片
            }
        }
    }

    /**
     * 选中状态改变
     */
    private void onCheckChange(PhotoModel photoModel, boolean isChecked) {

        if (isCheckMore()) {
            //多选
            setCheckIndex(photoModel, isChecked);
        } else {
            //单选
            if (mCheckPhoto != null && isChecked) {
                mCheckPhoto.clear();
            }
        }

        if (isChecked)
            mCheckPhoto.put(photoModel.getOriginalPath(), photoModel);
        else
            mCheckPhoto.remove(photoModel.getOriginalPath());

        if (mCheckPhoto.size() > 0) {
            //有选中
//            tv_end_photo.setTextColor(SkinCompatResources.getColor(getCtx(), R.color.A1));
            tv_end_photo.setEnabled(true);
        } else {
            //没有选中
//            tv_end_photo.setTextColor(SkinCompatResources.getColor(getCtx(), R.color.C4));
            tv_end_photo.setEnabled(false);
        }

        if (isCheckMore()) {
            //多选
            int checkNum = mCheckPhoto.size();
            if (checkNum > 0)
                tv_end_photo.setText(getString(R.string.sure) + "(" + checkNum + ")");
            else
                tv_end_photo.setText(getString(R.string.sure));
        }


        adapter.notifyCheckBoxChanged();

    }

    private void End(boolean autoClose) {
        if (mCheckPhoto == null || mCheckPhoto.size() == 0) return;

        if (onEndClickListener != null) {
            List<PhotoModel> modelList = new ArrayList<>();
            for (Map.Entry<String, PhotoModel> entry : mCheckPhoto.entrySet()) {
                modelList.add(entry.getValue());
            }
            //是否关闭弹窗
            boolean close = onEndClickListener.onEndClick(modelList);


            if (autoClose && close)
                dismiss();
        } else if (autoClose)
            dismiss();
    }


    /**
     * 重新dismiss，调用dismissAllowingStateLoss更可靠
     */
    @Override
    public void dismiss() {
        try {
            if (isVisible())
                super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

      /*  if (requestCode == PhotoAdapter.REQUEST_CAMERA) {
            String path = DoPicUtils.INSTANCE.getRealCameraPicPath(getCtx());
            if (!TextUtils.isEmpty(path)) {
                //拍照的返回,去裁剪
                if (onEndClickListener != null) {
                    List<PhotoModel> modelList = new ArrayList<>();
                    PhotoModel photoModel = new PhotoModel(path, null,0);
                    modelList.add(photoModel);
                    //是否关闭弹窗
                    boolean close = onEndClickListener.onEndClick(modelList);
                    if (close)
                        dismiss();
                } else
                    dismiss();
            } else
                dismiss();
        }*/
    }

    @Override
    public void onDestroyView() {
        if (mCheckPhoto != null) {
            mCheckPhoto.clear();
            mCheckPhoto = null;
        }
        super.onDestroyView();
    }

    public void show(AppCompatActivity activity, String tag) {
        super.show(activity.getSupportFragmentManager(), tag);
    }


    private void checkPermissionAndAction() {

        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        initListData();
                    } else {
                        ToastUtil.showToast("读取相册的权限被拒绝了");
                        dismiss();
                    }
                });
    }

    private void initListData() {
        lastMenuSelect = getResources().getString(R.string.str_allPicture);

        showProgress(true);
        photoSelectorDomain.getReccent(mType, reccentListener); // 更新最近照片
        photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
    }

    /**
     *
     */
    private void showProgress(boolean show) {
//        picLoadingView.bringToFront();
        //picLoadingView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 相册目录
     */
    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(List<AlbumModel> albums) {
            album_list.setData(albums);
            album_list.smoothScrollToPosition(0); // 滚动到顶端
        }
    };

    /**
     * 获取到最新的相册列表数据
     */
    private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {

            @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (isShowCamera) {
                        adapter.addHeaderView(LayoutInflater.from(getCtx()).inflate(R.layout.item_photo_selector, null));
                    }

                    List<PhotoModel> photos = (List<PhotoModel>) msg.obj;
                    adapter.setList(photos);
                    rv_photo_list.smoothScrollToPosition(0); // 滚动到顶端

                    showProgress(false);
                }
            };


            new Thread(() -> {

                if (!isShowGif) {
                    int index = 0;
                    while (index < photos.size()) {
                        PhotoModel photo = photos.get(index);
                        boolean isGIF = FileUtil.INSTANCE.isGIF(photo.getOriginalPath());
                        if (isGIF)
                            photos.remove(index);
                        else
                            index++;
                    }

                }
                Message message = new Message();
                message.obj = photos;
                handler.sendMessage(message);
            }).start();


        }
    };

    /**
     * 开启或者关闭album
     */
    private void toggleMenu() {
        try {
            folderSelect.ToggleView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnEndClickListener(OnEndClickListener onEndClickListener) {
        this.onEndClickListener = onEndClickListener;
    }


    /**
     * 最多可以选中多少张图片
     */
    public void setMaxNum(int MAX_NUM) {
        this.MAX_NUM = MAX_NUM;
    }


    /**
     * 是否显示gif
     */
    public void setShowGif(boolean showGif) {
        this.isShowGif = showGif;
    }

    /**
     * 是否显示相机
     */
    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    /**
     * 是否显示选中的框框
     */
    public void setShowCheckbox(boolean isShowCheckbox) {
        this.isShowCheckbox = isShowCheckbox;
    }

    /**
     * 需要显示什么类型资源
     */
    public void setType(AlbumController.Type type) {
        this.mType = type;
    }

    /**
     * 是否还可以选择更多
     */
    public static boolean canCheckMore() {
        LinkedHashMap<String, PhotoModel> mCheckPhoto = PhotoSelectorDialog.mCheckPhoto;
        if (mCheckPhoto != null) {
            if (mCheckPhoto.size() >= PhotoSelectorDialog.MAX_NUM) {

                return false;
            }
        }
        return true;
    }


    /**
     * 是否是多选
     */
    public static boolean isCheckMore() {
        if (PhotoSelectorDialog.MAX_NUM > 1) {
            //多选
            return true;
        } else {
            //单选
            return false;
        }
    }

    /**
     * 计算多选时，图片上面的数字
     */
    private void setCheckIndex(PhotoModel photoModel, boolean isChecked) {

        if (photoModel == null) return;

        LinkedHashMap<String, PhotoModel> mCheckPhoto = PhotoSelectorDialog.mCheckPhoto;
        if (mCheckPhoto != null) {
            if (isChecked) {
                //数字加1
                photoModel.setCheckIndex(mCheckPhoto.size() + 1);
            } else {
                //数字减1
                boolean isFind = false;

                for (Map.Entry<String, PhotoModel> entry : mCheckPhoto.entrySet()) {
                    PhotoModel model = entry.getValue();
                    if (isFind) {
                        model.setCheckIndex(model.getCheckIndex() - 1);
                        continue;
                    }
                    if (model.getOriginalPath().equals(photoModel.getOriginalPath())) {
                        isFind = true;
                    } else {
                        continue;
                    }
                }
            }
        }

    }


    private class PreviewActivityListener implements OnMediaListener {
        @Override
        public void onCheck(Boolean check, PhotoModel model) {
            onCheckChange(model, check);
        }

        @Override
        public void onClickSure() {
            End(true);
        }
    }

    public interface OnEndClickListener {
        //返回true 就是关闭窗口 false不关闭
        boolean onEndClick(List<PhotoModel> photos);
    }
}
