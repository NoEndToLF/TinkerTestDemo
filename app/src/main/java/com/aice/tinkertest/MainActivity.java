package com.aice.tinkertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aice.tinkertest.addclass.ToastUtils;
import com.aice.tinkertest.databinding.ActivityMainTinkerBinding;
import com.aice.tinkertest.tinker.util.Utils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ActivityMainTinkerBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main_tinker);
        activityMainBinding.nameTv.setText("第二次修复后");
        activityMainBinding.nameTv.setTextColor(Color.BLUE);
        activityMainBinding.toastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(MainActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);
        askForRequiredPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }

    private void askForRequiredPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }else {
            String path = MainActivity.this.getExternalFilesDir(null).getAbsolutePath()+"/Tinker";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path,"patch_signed_7zip.apk");
            if (file.exists()) {
                if (file.length() > 0) {
                    Toast.makeText(MainActivity.this, "patch_signed_7zip.apk", Toast.LENGTH_SHORT).show();
                    TinkerInstaller.onReceiveUpgradePatch(MainActivity.this, file.getAbsolutePath());
                    Log.v("patchFile路径2=",file.getAbsolutePath());
                }
            }
        }
    }

    private boolean hasRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= 16) {
            final int res = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            return res == PackageManager.PERMISSION_GRANTED;
        } else {
            // When SDK_INT is below 16, READ_EXTERNAL_STORAGE will also be granted if WRITE_EXTERNAL_STORAGE is granted.
            final int res = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return res == PackageManager.PERMISSION_GRANTED;
        }
    }
}
