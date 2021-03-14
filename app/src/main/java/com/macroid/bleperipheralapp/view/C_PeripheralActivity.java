package com.macroid.bleperipheralapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.macroid.bleperipheralapp.R;
import com.macroid.bleperipheralapp.di.components.DaggerI_ActivityComponent;
import com.macroid.bleperipheralapp.di.models.C_BluetoothModelDagger;
import com.macroid.bleperipheralapp.di.models.C_PermissionHelperModel;
import com.macroid.bleperipheralapp.models.C_BluetoothModel;
import com.macroid.bleperipheralapp.services.C_PeripheralAdvertiseService;

import javax.inject.Inject;

public class C_PeripheralActivity extends AppCompatActivity
{

    @Inject
    C_BluetoothModel bluetoothModel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__device_scan);
        F_DaggerImplimentation();
        bluetoothModel.F_SetGattServer(this);
        bluetoothModel.F_SetBluetoothService();
        F_StartAdvertising(F_GetServiceIntent());
        bluetoothModel.F_NotifyCharacteristicChanged();


    }


    //This method for implimentation dagger2
    private void F_DaggerImplimentation()
    {
        DaggerI_ActivityComponent.builder()
                .c_BluetoothModelDagger(new C_BluetoothModelDagger())
                .c_PermissionHelperModel(new C_PermissionHelperModel(this))
                .build()
                .Inject(this);
    }


    public void F_StartAdvertising(Intent intent)
    {
        startService(intent);
    }


    public void F_StopAdvertising(Intent intent)
    {
        stopService(intent);
    }


    private Intent F_GetServiceIntent()
    {
        return new Intent(this, C_PeripheralAdvertiseService.class);
    }


    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, C_MainActivity.class));
        finish();
        super.onBackPressed();
    }

}