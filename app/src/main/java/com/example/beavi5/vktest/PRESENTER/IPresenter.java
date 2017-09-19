package com.example.beavi5.vktest.PRESENTER;

import com.example.beavi5.vktest.MODEL.models.FriendModel;

import java.util.List;

/**
 * Created by beavi5 on 18.09.2017.
 */

public interface IPresenter {
    public void joinVK();
    public void onJoinVK(List<FriendModel> friendsList);

}
