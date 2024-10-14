package com.idragonpro.andmagnus.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.idragonpro.andmagnus.models.vipModel.Vip;

public class viewVipModel extends ViewModel {
    VipRepo signupRepository;
    MutableLiveData<Vip> signupRepositoryMutableLiveData = new MutableLiveData<>();

    public void setVipRepository() {
        signupRepository = VipRepo.getInstance();
        signupRepositoryMutableLiveData = signupRepository.VipPackage();
    }

    public LiveData<Vip> getVip() {
        return signupRepositoryMutableLiveData;
    }
}
