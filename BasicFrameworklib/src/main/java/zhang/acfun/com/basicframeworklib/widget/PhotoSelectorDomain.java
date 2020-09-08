package zhang.acfun.com.basicframeworklib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;

import zhang.acfun.com.basicframeworklib.model.AlbumModel;
import zhang.acfun.com.basicframeworklib.model.PhotoModel;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumController;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnLocalAlbumListener;
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnLocalReccentListener;

@SuppressLint("HandlerLeak")
public class PhotoSelectorDomain {

    private AlbumController albumController;
    private int queryCount_VIDEO = -1;//查询视频的数量

    /**
     * @param context
     */
    public PhotoSelectorDomain(Context context) {
        albumController = new AlbumController(context);
    }

    /**
     * 查询的视频条数
     * */
    public void setQueryCount_VIDEO(int queryCount_VIDEO) {
        this.queryCount_VIDEO = queryCount_VIDEO;
    }

    /**
     * 获取最新的图片
     *
     * @param listener
     */
    public void getReccent(AlbumController.Type type, final OnLocalReccentListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
            }
        };
        new Thread(() -> {
            List<PhotoModel> photos = albumController.getCurrent(type,queryCount_VIDEO);
            Message msg = handler.obtainMessage();
            msg.obj = photos;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 获取相册列表
     */
    public void updateAlbum(final OnLocalAlbumListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
            }
        };
        new Thread(() -> {
            List<AlbumModel> albums = albumController.getAlbums();
            Message msg = handler.obtainMessage();
            msg.obj = albums;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 获取单个相册下的所有照片信息
     */
    public void getAlbum(final String name, final OnLocalReccentListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
            }
        };
        new Thread(() -> {
            List<PhotoModel> photos = albumController.getAlbum(name);
            Message msg = handler.obtainMessage();
            msg.obj = photos;
            handler.sendMessage(msg);
        }).start();
    }

}
