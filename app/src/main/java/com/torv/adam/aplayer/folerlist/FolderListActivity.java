package com.torv.adam.aplayer.folerlist;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.torv.adam.aplayer.R;
import com.torv.adam.aplayer.utils.PermissionUtil;

import java.util.List;

public class FolderListActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        checkPermission();
    }

    private void checkPermission() {
        if(PermissionUtil.checkNeedRequestPermission(PERMISSIONS)) {
            PermissionUtil.checkNeedShowRationable(FolderListActivity.this, PERMISSIONS, new PermissionUtil.IRationableResultCallback() {
                @Override
                public void onNeedShowRationablePermissions(List<String> rationablePermisssions) {
                    // TODO explain dialog
                }

                @Override
                public void onNeedRequestPermissions(List<String> needRequestPermisssions) {
                    PermissionUtil.requestPermission(FolderListActivity.this, needRequestPermisssions.toArray(new String[needRequestPermisssions.size()]));
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.handleGrantResult(requestCode, permissions, grantResults, new PermissionUtil.IGrantResultCallback() {
            @Override
            public void onGrantedPermissions(List<String> grantedPermissions) {
                // TODO
            }

            @Override
            public void onDeniedPermissions(List<String> deniedPermissions) {
                // TODO
            }
        });
    }
}
