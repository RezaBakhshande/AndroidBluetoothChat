package com.macroid.bleperipheralapp.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Reza Bakhshande on 12/03/2021.
 */

public class C_ShowMessage extends AppCompatActivity
{

    protected void F_ShowMessage(int stringId , Context context)
    {
        F_ShowMessage(getString(stringId) , context);
    }

    protected void F_ShowMessage(String string , Context context)
    {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

}

