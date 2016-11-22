package com.torv.adam.aplayer.folerlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torv.adam.aplayer.R;
import com.torv.adam.aplayer.bean.FolderItem;
import com.torv.adam.aplayer.videolist.VideoListActivity;
import com.torv.adam.libs.utils.Constant;
import com.torv.adam.libs.utils.Font;
import com.torv.adam.libs.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamLi on 2016/11/17.
 */

public class FolderListFragment extends Fragment implements IFolderListContract.IView{

    private IFolderListContract.IPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private FolderListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("Lifecycle");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d("Lifecycle");
        View view = inflater.inflate(R.layout.fragment_folerlist, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_foler_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d("Lifecycle");
        mPresenter = new FolderListPresenter(getContext(), this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FolderListAdapter();
        mAdapter.setContext(getContext());
        mRecyclerView.setAdapter(mAdapter);
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
        mAdapter.setData(data);
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder{

        TextView pathName;
        TextView videoCount;

        public FolderViewHolder(View itemView) {
            super(itemView);
            pathName = (TextView) itemView.findViewById(R.id.id_folder_path);
            pathName.setTypeface(Font.instance.getTypeFace(Font.ROBOTO_REGULAR));
            videoCount = (TextView) itemView.findViewById(R.id.id_folder_videocount);
            videoCount.setTypeface(Font.instance.getTypeFace(Font.ROBOTO_LIGHT));
        }
    }

    static class FolderListAdapter extends RecyclerView.Adapter<FolderViewHolder> {

        private List<FolderItem> mFolderList = new ArrayList<>();

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        public void setData(List<FolderItem> dataList) {
            if(null == dataList) {
                return;
            }
            mFolderList.clear();
            mFolderList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FolderViewHolder viewHolder = new FolderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.folder_list_item, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FolderViewHolder holder, int position) {
            final FolderItem folderItem = mFolderList.get(position);
            if(null != folderItem) {
                holder.pathName.setText(folderItem.bucketDisplayName);
                holder.videoCount.setText(String.format(mContext.getResources().getString(R.string.xx_vides), folderItem.videoCount));

                holder.pathName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoListActivity.class);
                        intent.putExtra(Constant.VIDEO_PATH_KEY, folderItem.path);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mFolderList.size();
        }
    }
}
