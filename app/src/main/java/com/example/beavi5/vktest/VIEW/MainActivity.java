package com.example.beavi5.vktest.VIEW;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.beavi5.vktest.PRESENTER.IPresenter;
import com.example.beavi5.vktest.PRESENTER.Presenter;
import com.example.beavi5.vktest.R;
import com.example.beavi5.vktest.VIEW.IView;
import com.example.beavi5.vktest.MODEL.models.FriendModel;
import com.example.beavi5.vktest.MODEL.models.RVAdapter;
import com.example.beavi5.vktest.MODEL.models.VkApp;
import com.example.beavi5.vktest.MODEL.models.VkDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {
VkDialog dialog;
    LinearLayoutManager linearLayoutManager;
    List<FriendModel> friendsList;
    IPresenter presenter;


    RVAdapter rvAdapter;
    RecyclerView recyclerView;

    VkApp vkApp;
    Button joinVKBtn;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);

        setContentView(R.layout.activity_main);
       // CookieManager cookieManager = CookieManager.getInstance();
        //cookieManager.removeAllCookie();
        vkApp= new VkApp(this);
        vkApp.setListener(this);
       // vkApp.resetAccessToken();


recyclerView= (RecyclerView) findViewById(R.id.friendsRV);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
       // friendsList=new ArrayList<>();
        //rvAdapter = new RVAdapter(friendsList);
       // recyclerView.setAdapter(rvAdapter);

        joinVKBtn = (Button) findViewById(R.id.joinVKBtn);


        joinVKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.joinVK(); //

               // vkApp.showLoginDialog();
            }
        });







    }



    @Override
    public void onComplete(String url) {

    }

    @Override
    public void onGetFriends(List<FriendModel> friendsList, String param) {
        rvAdapter = new RVAdapter(friendsList,param);
         recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onError(String description) {

    }
}
