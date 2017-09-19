package com.example.beavi5.vktest.PRESENTER;

import android.content.Context;

import com.example.beavi5.vktest.MODEL.IModel;
import com.example.beavi5.vktest.MODEL.Model;
import com.example.beavi5.vktest.VIEW.IView;
import com.example.beavi5.vktest.MODEL.models.VkApp;
import com.example.beavi5.vktest.MODEL.models.FriendModel;

import java.util.List;

/**
 * Created by beavi5 on 18.09.2017.
 */

public class Presenter implements IPresenter, VkApp.VkDialogListener{
    IView view;
    IModel model;
    Context context;

    public Presenter(IView view) {
        this.view = view;
        this.context = (Context) view;
    }

    @Override
    public void joinVK() {
        model = new Model(this,context);
        model.joinVK();
    }

    @Override
    public void onJoinVK(List<FriendModel> friendsList) {

    }

    @Override
    public void onComplete(String url) {

    }

    @Override
    public void onGetFriends(List<FriendModel> friendsList, String param) {
view.onGetFriends(friendsList,param);
    }

    @Override
    public void onError(String description) {

    }
}
