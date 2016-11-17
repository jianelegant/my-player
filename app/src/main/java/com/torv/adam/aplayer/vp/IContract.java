package com.torv.adam.aplayer.vp;

/**
 * Created by AdamLi on 2016/11/17.
 */

public interface IContract {

    public interface IPresenter{

        public void start();
    }

    public interface IView<T>{

        public void onData(T data);
    }
}
