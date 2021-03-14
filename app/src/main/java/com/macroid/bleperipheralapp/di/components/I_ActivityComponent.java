package com.macroid.bleperipheralapp.di.components;


import com.macroid.bleperipheralapp.view.C_MainActivity;
import com.macroid.bleperipheralapp.view.C_PeripheralActivity;
import com.macroid.bleperipheralapp.di.models.C_BluetoothModelDagger;
import com.macroid.bleperipheralapp.di.models.C_ContextModel;
import com.macroid.bleperipheralapp.di.models.C_PermissionHelperModel;
import com.macroid.bleperipheralapp.di.scope.MainScope;

@MainScope
@dagger.Component(modules = {C_BluetoothModelDagger.class, C_PermissionHelperModel.class, C_ContextModel.class})
public interface I_ActivityComponent
{

    void Inject(C_MainActivity chatActivity);

    void Inject(C_PeripheralActivity peripheralActivity);


}
