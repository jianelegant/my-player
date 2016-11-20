package com.torv.adam.aplayer.folerlist;

import com.torv.adam.aplayer.bean.FolderItem;

import java.util.List;

/**
 * Created by AdamLi on 2016/11/17.
 */

public interface IFolderListContract {

    public interface IPresenter{

        public void start();
    }

    public interface IView{

        public void onData(List<FolderItem> folderList);
    }
}
