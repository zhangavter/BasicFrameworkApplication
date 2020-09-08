package zhang.acfun.com.basicframeworklib.model;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

import zhang.acfun.com.basicframeworklib.Constants;
import zhang.acfun.com.basicframeworklib.util.FileUtil;

public class PhotoModel implements Serializable {

    private String originalPath;
    private Uri fileUri;
    private String secondPath; //视频的时候，视频封面 不是视频的时候可以作为低密度图片显示
    private int checkIndex;//当选中时需要的下标，从1开始
    private boolean isCheck = false;//是否选中
    private int type;//0 图片 1视频
    private int videoTime;//视频时长，不是视频就不管

    public PhotoModel(String originalPath, Uri uri, int type) {
        super();
        this.originalPath = originalPath;
        this.type = type;
        this.fileUri = uri;
    }

    /**
     * @param originalPath
     * @param secondPath
     */
    public PhotoModel(String originalPath, String secondPath) {
        this.originalPath = originalPath;
        this.secondPath = secondPath;
    }

    public PhotoModel() {
    }

    public String getSecondPath() {
        return secondPath;
    }

    public void setSecondPath(String secondPath) {
        this.secondPath = secondPath;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    /**
     *
     * @return
     */
    public String getRealPath() {
        if (FileUtil.INSTANCE.isAndroidQorAbove() && fileUri != null &&
                originalPath != null && !originalPath.startsWith("http")) {
            File file = FileUtil.INSTANCE.prepareTempCopyFilePath(Constants.getContext(), "acfun_", FileUtil.INSTANCE.getFileExtension(originalPath));
            FileUtil.INSTANCE.copyFileFromPublicToPrivateAtTargetQ(Constants.getContext(), fileUri, file);
            return file.getAbsolutePath();
        }
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public int getCheckIndex() {
        return checkIndex;
    }

    public void setCheckIndex(int checkIndex) {
        this.checkIndex = checkIndex;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }
}
