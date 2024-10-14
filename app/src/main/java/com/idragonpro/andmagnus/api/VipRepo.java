package com.idragonpro.andmagnus.api;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VipRepo {
    com.idragonpro.andmagnus.api.WebApi apiIterface;
    Context context;
    com.idragonpro.andmagnus.beans.Movies movies;
    private static VipRepo vipRepo;

    public static VipRepo getInstance() {
        if (vipRepo == null) {
            vipRepo = new VipRepo();
        }
        return vipRepo;
    }

    public VipRepo() {
        apiIterface = com.idragonpro.andmagnus.api.API.getRetrofit().create(com.idragonpro.andmagnus.api.WebApi.class);
    }

    public MutableLiveData<com.idragonpro.andmagnus.models.vipModel.Vip> VipPackage() {
        final MutableLiveData<com.idragonpro.andmagnus.models.vipModel.Vip> result = new MutableLiveData<>();
        apiIterface.vipUserPackage().enqueue(new Callback<com.idragonpro.andmagnus.models.vipModel.Vip>() {
            @Override
            public void onResponse(@NotNull Call<com.idragonpro.andmagnus.models.vipModel.Vip> call, @NotNull Response<com.idragonpro.andmagnus.models.vipModel.Vip> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        result.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<com.idragonpro.andmagnus.models.vipModel.Vip> call, @NotNull Throwable t) {
                com.idragonpro.andmagnus.models.vipModel.Vip signupresponseData = new com.idragonpro.andmagnus.models.vipModel.Vip();
                result.setValue(signupresponseData);
            }
        });
        return result;
    }

}
