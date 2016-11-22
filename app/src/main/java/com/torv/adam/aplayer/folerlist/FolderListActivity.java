package com.torv.adam.aplayer.folerlist;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.torv.adam.aplayer.R;
import com.torv.adam.aplayer.utils.PermissionUtil;

import java.util.List;

public class FolderListActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private TextView mDenyHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        mDenyHint = (TextView) findViewById(R.id.id_deny_hint);
        checkPermission();
    }

    private void checkPermission() {
        if(PermissionUtil.checkNeedRequestPermission(PERMISSIONS)) {
            PermissionUtil.checkNeedShowRationable(FolderListActivity.this, PERMISSIONS, new PermissionUtil.IRationableResultCallback() {
                @Override
                public void onNeedShowRationablePermissions(List<String> rationablePermisssions) {
                    if(rationablePermisssions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FolderListActivity.this);
                        builder.setTitle(R.string.permission_rational_title);
                        builder.setMessage(R.string.permission_rational_msg);
                        builder.setCancelable(false);
                        builder.setNegativeButton(R.string.permission_rational_cancel, null);
                        builder.setPositiveButton(R.string.permission_rational_grant, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtil.requestPermission(FolderListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                            }
                        });
                        builder.show();
                    }
                }

                @Override
                public void onNeedRequestPermissions(List<String> needRequestPermisssions) {
                    if(needRequestPermisssions.size() > 0) {
                        PermissionUtil.requestPermission(FolderListActivity.this, needRequestPermisssions.toArray(new String[needRequestPermisssions.size()]));
                    }
                }
            });
        } else {
            mDenyHint.setVisibility(View.GONE);
            replaceFragment();
        }
    }

    private void replaceFragment(){
        Fragment newFragment = new FolderListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_frg_holder, newFragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.handleGrantResult(requestCode, permissions, grantResults, new PermissionUtil.IGrantResultCallback() {
            @Override
            public void onGrantedPermissions(List<String> grantedPermissions) {
                if(null != grantedPermissions && grantedPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mDenyHint.setVisibility(View.GONE);
                    replaceFragment();
                }
            }

            @Override
            public void onDeniedPermissions(List<String> deniedPermissions) {
                if(null != deniedPermissions && deniedPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(FolderListActivity.this, R.string.permission_deny_hint, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
