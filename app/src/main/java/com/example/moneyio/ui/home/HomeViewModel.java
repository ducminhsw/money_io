package com.example.moneyio.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List> eList;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        eList = new MutableLiveData<List>();
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List> getListExpense() {
        return eList;
    }
}