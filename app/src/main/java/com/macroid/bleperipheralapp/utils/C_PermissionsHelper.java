package com.macroid.bleperipheralapp.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class C_PermissionsHelper
{


    private static C_PermissionsHelper mInstance;
    private static AppCompatActivity mContext;
    public static ArrayList<I_PermissionHelperListener> permissionHelperListeners = new ArrayList<>();

    public C_PermissionsHelper(AppCompatActivity appCompatActivity)
    {
        mContext = appCompatActivity;
    }

    public static C_PermissionsHelper F_getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new C_PermissionsHelper(mContext);
        }
        return mInstance;
    }

    public void F_RequestPermissions(String perm, I_PermissionHelperListener permissionHelperListener)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (mContext.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
            {
                int index = permissionHelperListeners.size();
                permissionHelperListeners.add(permissionHelperListener);
                mContext.requestPermissions(new String[]{perm}, index);
            }
        }
    }

    public void F_RequestPermissions(String[] perms, I_PermissionHelperListener permissionHelperListener)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int index = permissionHelperListeners.size();
            permissionHelperListeners.add(permissionHelperListener);
            mContext.requestPermissions(perms, index);
        }
    }

    public void F_OnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            permissionHelperListeners.get(requestCode).F_Accepted();
        } else
        {
            permissionHelperListeners.get(requestCode).F_Rejected();
        }
        permissionHelperListeners.remove(requestCode);
    }

    //Check if Permission granted
    public boolean F_IsPermissionGranted(String perm)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return mContext.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

}
