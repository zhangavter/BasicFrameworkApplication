package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import zhang.acfun.com.basicframeworklib.model.PhotoModel;

public interface OnMediaListener {
    void onCheck(Boolean check, PhotoModel model);

    void onClickSure();
}
