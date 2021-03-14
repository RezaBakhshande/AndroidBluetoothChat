package com.macroid.bleperipheralapp.models;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.macroid.bleperipheralapp.R;
import com.macroid.bleperipheralapp.utils.C_Operation;
import com.macroid.bleperipheralapp.view.C_ShowMessage;

import java.util.HashSet;

import static com.macroid.bleperipheralapp.utils.C_Constants.BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID;
import static com.macroid.bleperipheralapp.utils.C_Constants.HEART_RATE_SERVICE_UUID;

public class C_BluetoothModel extends C_ShowMessage
{
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothGattCharacteristic mSampleCharacteristic;

    private BluetoothGattServer mGattServer;
    private HashSet<BluetoothDevice> mBluetoothDevices;

    Context context;

    public C_Operation operation = new C_Operation();



    public boolean F_IsDeviceSupportBluetooth(BluetoothAdapter bluetoothAdapter)
    {
        return bluetoothAdapter.getBondedDevices() != null;
    }


    public void F_EnableBluetooth(Activity activity, BluetoothAdapter bluetoothAdapter)
    {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else
        {
            Toast.makeText(activity, "Bluetooth Already Enabled", Toast.LENGTH_SHORT).show();
        }
    }


    public void F_SetGattServer(Activity context)
    {

        mBluetoothDevices = new HashSet<>();
        BluetoothManager mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (mBluetoothManager != null)
        {
            mGattServer = mBluetoothManager.openGattServer(this, mGattServerCallback);
        } else
        {
            F_ShowMessage(R.string.unknownError, getApplicationContext());
        }
    }


    public void F_SetBluetoothService()
    {

        // create the Service
        BluetoothGattService mSampleService = new BluetoothGattService(HEART_RATE_SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        /*
        create the Characteristic.
        we need to grant to the Client permission to read (for when the user clicks the "Request Characteristic" button).
        no need for notify permission as this is an action the Server initiate.
         */
        mSampleCharacteristic = new BluetoothGattCharacteristic(BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID, BluetoothGattCharacteristic.PROPERTY_NOTIFY | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ);
        setCharacteristic(); // set initial state

        // add the Characteristic to the Service
        mSampleService.addCharacteristic(mSampleCharacteristic);

        // add the Service to the Server/Peripheral
        if (mGattServer != null)
        {
            mGattServer.addService(mSampleService);
        }
    }


    public void setCharacteristic()
    {
        setCharacteristic(1);
    }

    /*
    update the value of Characteristic.
    the client will receive the Characteristic value when:
        1. the Client user clicks the "Request Characteristic" button
        2. teh Server user clicks the "Notify Client" button

    value - can be between 0-255 according to:
    https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.body_sensor_location.xml
     */
    public void setCharacteristic(int checkedId)
    {
        /*
        done each time the user changes a value of a Characteristic
         */
        int value = checkedId;
        mSampleCharacteristic.setValue(getValue(value));
    }

    private byte[] getValue(int value)
    {
        return new byte[]{(byte) value};
    }

    /*
    send to the client the value of the Characteristic,
    as the user requested to notify.
     */
    public void F_NotifyCharacteristicChanged()
    {
        /*
        done when the user clicks the notify button in the app.
        indicate - true for indication (acknowledge) and false for notification (un-acknowledge).
         */
        boolean indicate = (mSampleCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) == BluetoothGattCharacteristic.PROPERTY_INDICATE;

        for (BluetoothDevice device : mBluetoothDevices)
        {
            if (mGattServer != null)
            {
                mGattServer.notifyCharacteristicChanged(device, mSampleCharacteristic, indicate);
            }
        }
    }

    /**
     * Returns Intent addressed to the {@code PeripheralAdvertiseService} class.
     */


    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback()
    {

        @Override
        public void onConnectionStateChange(BluetoothDevice device, final int status, int newState)
        {

            super.onConnectionStateChange(device, status, newState);

            String msg;

            if (status == BluetoothGatt.GATT_SUCCESS)
            {

                if (newState == BluetoothGatt.STATE_CONNECTED)
                {

                    mBluetoothDevices.add(device);

                    msg = "Connected to device: " + device.getAddress();
                    F_ShowMessage(msg,getBaseContext());

                } else if (newState == BluetoothGatt.STATE_DISCONNECTED)
                {

                    mBluetoothDevices.remove(device);

                    msg = "Disconnected from device";
                    F_ShowMessage(msg, getBaseContext());
                }

            } else
            {
                mBluetoothDevices.remove(device);

                msg = getString(R.string.statusErrorWhenConnecting) + ": " + status;
                F_ShowMessage(msg, getBaseContext());

            }
        }


        @Override
        public void onNotificationSent(BluetoothDevice device, int status)
        {
            super.onNotificationSent(device, status);
            //Log.v(MainActivity.TAG, "Notification sent. Status: " + status);
        }


        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic)
        {

            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);

            if (mGattServer == null)
            {
                return;
            }

            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        }


        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value)
        {

            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);

            String s = new String(value);
            mSampleCharacteristic.setValue(operation.F_Answer(s));

            if (responseNeeded)
            {
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, value);
            }

        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor)
        {

            super.onDescriptorReadRequest(device, requestId, offset, descriptor);

            if (mGattServer == null)
            {
                return;
            }

            //Log.d(MainActivity.TAG, "Device tried to read descriptor: " + descriptor.getUuid());
            //Log.d(MainActivity.TAG, "Value: " + Arrays.toString(descriptor.getValue()));

            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, descriptor.getValue());
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                             BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded,
                                             int offset,
                                             byte[] value)
        {

            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);


        }
    };


    /*//This method for implimentation dagger2
    public void F_DaggerImplimentation()
    {
        DaggerI_BluetoothModelComponent.builder()
                .c_OperationModel(new C_OperationModel())
                .build()
                .Inject(this);
    }*/


}
