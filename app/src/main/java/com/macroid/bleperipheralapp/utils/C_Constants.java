package com.macroid.bleperipheralapp.utils;

import java.util.UUID;

/**
 * Created by Reza Bakhshande on 12/03/2021.
 */

public class C_Constants
{


    public static final int SERVER_MSG_FIRST_STATE = 1;
    public static final int SERVER_MSG_SECOND_STATE = 2;

    /*
    TODO bluetooth
    better to use different Bluetooth Service,
    instead of Heart Rate Service:

    maybe Object Transfer Service is more suitable:
     */
    public static final UUID HEART_RATE_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    public static final UUID BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID = UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb");




    private static UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }
}
