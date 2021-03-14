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

public abstract class C_ShowMessage extends AppCompatActivity
{

    protected void F_ShowMessage(int stringId)
    {
        F_ShowMessage(getString(stringId));
    }

    protected void F_ShowMessage(String string)
    {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

}

