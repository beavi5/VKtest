package com.example.beavi5.vktest.MODEL;

import android.content.Context;

import com.example.beavi5.vktest.PRESENTER.IPresenter;
import com.example.beavi5.vktest.PRESENTER.Presenter;
import com.example.beavi5.vktest.MODEL.models.VkApp;

/**
 * Created by beavi5 on 18.09.2017.
 */

public class Model implements  IModel{
    Presenter presenter;
    VkApp vkApp;
Context context;

    public Model(IPresenter presenter, Context context) {
        this.presenter = (Presenter) presenter;
        this.context = context;
    }

    @Override
    public void joinVK() {
        vkApp= new VkApp(context);
        vkApp.setListener(presenter);
        vkApp.showLoginDialog();
    }
}
