package com.torv.adam.aplayer.folerlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.torv.adam.aplayer.R;
import com.torv.adam.aplayer.bean.FolderItem;
import com.torv.adam.aplayer.utils.L;
import com.torv.adam.aplayer.vp.IContract;

import java.util.List;

/**
 * Created by AdamLi on 2016/11/17.
 */

public class FolderListFragment extends Fragment implements IContract.IView<List<FolderItem>>{

    private IContract.IPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("Lifecycle");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d("Lifecycle");
        mPresenter = new FolderListPresenter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d("Lifecycle");
        View view = inflater.inflate(R.layout.fragment_folerlist, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("Lifecycle");
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d("Lifecycle");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.d("Lifecycle");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.d("Lifecycle");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d("Lifecycle");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("Lifecycle");
    }

    @Override
    public void onData(List<FolderItem> data) {

    }
}
