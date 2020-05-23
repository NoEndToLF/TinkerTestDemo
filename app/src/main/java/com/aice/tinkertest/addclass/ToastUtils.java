package com.aice.tinkertest.addclass;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context){
        Toast.makeText(context, "第二次修复后", Toast.LENGTH_SHORT).show();
    }
}
