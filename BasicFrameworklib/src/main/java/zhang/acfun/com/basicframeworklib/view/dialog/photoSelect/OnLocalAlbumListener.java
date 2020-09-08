package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import java.util.List;

import zhang.acfun.com.basicframeworklib.model.AlbumModel;


/**
 * @author Soli
 * @Time 2018/11/20 15:55
 */
public interface OnLocalAlbumListener {
    void onAlbumLoaded(List<AlbumModel> albums);
}
