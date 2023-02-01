package com.heady.betterweather.ui.tenday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TenDayViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TenDayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}