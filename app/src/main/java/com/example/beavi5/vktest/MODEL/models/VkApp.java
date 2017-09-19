package com.example.beavi5.vktest.MODEL.models;

/**
 * Created by beavi5 on 18.09.2017.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VkApp {
    //constants for OAUTH AUTHORIZE in Vkontakte
    public static final String CALLBACK_URL = "http://oauth.vk.com/blank.html";
    private static final String OAUTH_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6188395&scope=4096&redirect_uri=http://oauth.vk.com/blank.html&display=touch&response_type=token";

    private Context _context;
    private VkDialogListener _listener;
    private VkSession _vkSess;

    private String VK_API_URL = "https://api.vkontakte.ru/method/";


    public VkApp(Context context){
        _context = context;
        _vkSess = new VkSession(_context);
    }

    public void setListener(VkDialogListener listener) { _listener = listener; }

    public void showLoginDialog(){
        new VkDialog(_context,OAUTH_AUTHORIZE_URL,_listener).show();
    }

    //parse vkontakte JSON response
    private boolean parseResponse(String jsonStr){
        boolean errorFlag = true;

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
            JSONObject errorObj = null;

            if( jsonObj.has("error") ) {
                errorObj = jsonObj.getJSONObject("error");
                int errCode = errorObj.getInt("error_code");
                if( errCode == 14){
                    errorFlag = false;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.DEBUG_TAG,"exception when creating json object");
        }

        return errorFlag;
    }


    public String[] getAccessToken(String url) {
        String[] query = url.split("#");
        String[] params = query[1].split("&");
        //params[0] - access token=value, params[1] - expires_in=value,
        //params[2] - user_id=value
        return params;
    }

    public boolean hasAccessToken() {
        String[] params = _vkSess.getAccessToken();
        if( params != null ) {
            long accessTime = Long.parseLong(params[3]);
            long currentTime = System.currentTimeMillis();
            long expireTime = (currentTime - accessTime) / 1000;

            //Log.d(Constants.DEBUG_TAG,"expires time="+expireTime);

            if( params[0].equals("") & params[1].equals("") & params[2].equals("") & Long.parseLong(params[3]) ==0 ){
                //Log.d(Constants.DEBUG_TAG,"access token empty");
                return false;
            }
            else if( expireTime >= Long.parseLong(params[1]) ) {
                //Log.d(Constants.DEBUG_TAG,"access token time expires out");
                return false;
            }
            else {
                //Log.d(Constants.DEBUG_TAG,"access token ok");
                return true;
            }
        }
        return false;
    }

    public void saveAccessToken(String accessToken, String expires, String userId) {
        _vkSess.saveAccessToken(accessToken, expires, userId);
    }

    public void resetAccessToken() { _vkSess.resetAccessToken(); }

    public interface VkDialogListener {
        void onComplete(String url);
        void onGetFriends(List<FriendModel> friendsList, String param);
        void onError(String description);
    }
}