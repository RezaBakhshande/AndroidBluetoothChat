package com.macroid.bleperipheralapp.di.models;

import androidx.appcompat.app.AppCompatActivity;

import com.macroid.bleperipheralapp.di.scope.MainScope;
import com.macroid.bleperipheralapp.utils.C_PermissionsHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class C_PermissionHelperModel
{
    private final AppCompatActivity appCompatActivity;

    public C_PermissionHelperModel(AppCompatActivity appCompatActivity)
    {
        this.appCompatActivity = appCompatActivity;
    }

    @MainScope
    @Provides
    public C_PermissionsHelper F_GetPermissionsHelper(AppCompatActivity appCompatActivity)
    {
        return new C_PermissionsHelper(appCompatActivity);
    }

    @MainScope
    @Provides
    public AppCompatActivity appCompatActivity()
    {
        return appCompatActivity;
    }
}
