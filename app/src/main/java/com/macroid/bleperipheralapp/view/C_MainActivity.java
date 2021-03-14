package com.macroid.bleperipheralapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.macroid.bleperipheralapp.R;
import com.macroid.bleperipheralapp.di.components.DaggerI_ActivityComponent;
import com.macroid.bleperipheralapp.di.models.C_BluetoothModelDagger;
import com.macroid.bleperipheralapp.di.models.C_PermissionHelperModel;
import com.macroid.bleperipheralapp.models.C_BluetoothModel;
import com.macroid.bleperipheralapp.utils.C_PermissionsHelper;
import com.macroid.bleperipheralapp.utils.I_PermissionHelperListener;

import javax.inject.Inject;

public class C_MainActivity extends AppCompatActivity
{

    @Inject
    C_BluetoothModel bluetoothModel;

    @Inject
    C_PermissionsHelper permissionsHelper;

    //init bluetooth
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        F_DaggerImplimentation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuSearchDevices:

                if (bluetoothAdapter.isEnabled())
                {

                    permissionsHelper.F_RequestPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, new I_PermissionHelperListener()
                    {
                        @Override
                        public void F_Accepted()
                        {
                            startActivity(new Intent(getApplicationContext(), C_PeripheralActivity.class));
                            finish();
                        }

                        @Override
                        public void F_Rejected()
                        {
                            Toast.makeText(getApplicationContext(), "Please Accept Location Permission", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (permissionsHelper.F_IsPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
                    {
                        Toast.makeText(getApplicationContext(), "Start Searching Devices", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), C_PeripheralActivity.class));
                        finish();
                    }

                } else
                {
                    Toast.makeText(getApplicationContext(), "Please turn on Bluetooth first", Toast.LENGTH_SHORT).show();
                }


                return true;
            case R.id.menuEnableBluetooth:
                if (bluetoothModel.F_IsDeviceSupportBluetooth(bluetoothAdapter))
                {
                    bluetoothModel.F_EnableBluetooth(C_MainActivity.this, bluetoothAdapter);
                } else
                {
                    Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        C_PermissionsHelper.F_getInstance().F_OnRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}