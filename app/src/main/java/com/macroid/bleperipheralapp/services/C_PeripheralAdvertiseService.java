package com.macroid.bleperipheralapp.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;

import com.macroid.bleperipheralapp.utils.C_Constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Reza Bakhshande on 12/03/2021.
 */

public class C_PeripheralAdvertiseService extends Service
{

    
    public static boolean running = false;

    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private AdvertiseCallback mAdvertiseCallback;
    private Handler mHandler;
    private Runnable timeoutRunnable;

    
    //Length of time to allow advertising before automatically shutting off. (10 minutes)
     
    private final long TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);

    @Override
    public void onCreate() {
        running = true;
        F_Initialize();
        F_StartAdvertising();
        F_SetTimeout();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        running = false;
        F_StopAdvertising();
        mHandler.removeCallbacks(timeoutRunnable);
        stopForeground(true);
        super.onDestroy();
    }

    
    //Required for extending service, but this will be a Started Service only, so no need for binding.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    
    //Get references to system Bluetooth objects if we don't have them already.
    private void F_Initialize() {
        if (mBluetoothLeAdvertiser == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                if (bluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
                }
            }
        }
    }

    
    //Starts a delayed Runnable that will cause the BLE Advertising to timeout and stop after a set amount of time.
    private void F_SetTimeout(){
        mHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        };
        mHandler.postDelayed(timeoutRunnable, TIMEOUT);
    }

    
    //Starts BLE Advertising.
    private void F_StartAdvertising() {
        
        if (mAdvertiseCallback == null) {
            AdvertiseSettings settings = F_BuildAdvertiseSettings();
            AdvertiseData data = F_BuildAdvertiseData();
            mAdvertiseCallback = new C_SampleAdvertiseCallback();

            if (mBluetoothLeAdvertiser != null) {
                mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
            }
        }
    }

    
    //Stops BLE Advertising.
    private void F_StopAdvertising() {
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mAdvertiseCallback = null;
        }
    }

    
    //Returns an AdvertiseData object which includes the Service UUID and Device Name.
    private AdvertiseData F_BuildAdvertiseData() {

        /**
         * Note: There is a strict limit of 31 Bytes on packets sent over BLE Advertisements.
         *  This includes everything put into AdvertiseData including UUIDs, device info, &
         *  arbitrary service or manufacturer data.
         *  Attempting to send packets over this limit will result in a failure with error code
         *  AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE. Catch this error in the
         *  onStartFailure() method of an AdvertiseCallback implementation.
         */

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        //dataBuilder.addServiceUuid(Constants.SERVICE_UUID);
        dataBuilder.addServiceUuid(ParcelUuid.fromString(C_Constants.HEART_RATE_SERVICE_UUID.toString()));
        dataBuilder.setIncludeDeviceName(true);

        //For example - this will cause advertising to fail (exceeds size limit) */
        //String failureData = "asdghkajsghalkxcjhfa;sghtalksjcfhalskfjhasldkjfhdskf";
        //dataBuilder.addServiceData(Constants.SERVICE_UUID, failureData.getBytes());

        return dataBuilder.build();
    }


    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private AdvertiseSettings F_BuildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
        settingsBuilder.setTimeout(0);
        return settingsBuilder.build();
    }



    /**
     * Custom callback after Advertising succeeds or fails to start. Broadcasts the error code
     * in an Intent to be picked up by AdvertiserFragment and stops this Service.
     */
    private class C_SampleAdvertiseCallback extends AdvertiseCallback
    {

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            stopSelf();
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
        }
    }


}

