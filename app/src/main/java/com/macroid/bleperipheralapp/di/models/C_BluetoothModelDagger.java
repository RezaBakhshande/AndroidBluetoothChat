package com.macroid.bleperipheralapp.di.models;


import android.content.Context;

import com.macroid.bleperipheralapp.di.scope.MainScope;
import com.macroid.bleperipheralapp.models.C_BluetoothModel;

import dagger.Module;
import dagger.Provides;

@Module
public class C_BluetoothModelDagger
{


    public C_BluetoothModelDagger()
    {
    }

    @MainScope
    @Provides
    public C_BluetoothModel F_GetBluetoothModel(Context context)
    {
        return new C_BluetoothModel();
    }



}
