package com.example.beavi5.vktest.VIEW;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.beavi5.vktest.MODEL.models.MultipartUtility;
import com.example.beavi5.vktest.MODEL.models.ResponsePhoto;
import com.example.beavi5.vktest.MODEL.models.UploadedPhoto;
import com.example.beavi5.vktest.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MsgActivity extends AppCompatActivity {
    private final static int GALLERY_REQUEST=1;
    private Uri mImageUri;
    ImageButton imgLoadBtn;
    EditText msgET;
    Button sendMsgBtn;
    String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
msgET = (EditText) findViewById(R.id.msgET);
        imgLoadBtn = (ImageButton) findViewById(R.id.imageLoadBtn);
        imgLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });




        sendMsgBtn = (Button) findViewById(R.id.sendMsgBtn);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String feedUrl = "https://api.vk.com/method/messages.send?user_id="+getIntent().getStringExtra("uid")+"&v=5.28&"+
                        getIntent().getStringExtra("token")
                        +"&message="
                        +msgET.getText().toString()
                        +"&attachment="+photoId;





// из документации: параметры могут передаваться как методом GET, так и POST. Если вы будете передавать большие данные (больше 2 килобайт), следует использовать POST.

                Thread th= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            URL url = new URL(feedUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            conn.connect();
                            int responseCode= conn.getResponseCode();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }});
                th.start();


        MsgActivity.this.finish();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
            mImageUri = data.getData();





            Picasso.with(this)
                    .load(mImageUri)
                    .fit().centerInside()
                    .into(imgLoadBtn);




            Log.d("OOOOOOO", "onActivityResult: "+data.getDataString());

            Log.d("RES", "ImageURI: "+mImageUri.getPath());


            RequestQueue requestQueue= Volley.newRequestQueue(this);
            final String token =this.getIntent().getStringExtra("token");
            String feedUrl="https://api.vk.com/method/photos.getMessagesUploadServer?"+this.getIntent().getStringExtra("token");
            Log.d("RES", "feedUrl: "+feedUrl);

            final StringRequest request=new StringRequest(Request.Method.GET, feedUrl, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(String response) {
                    String json = response;

                    Log.d("RES", "onResponse: "+response);
                    Gson gson = new Gson();
                   ResponsePhoto responsePhoto = gson.fromJson(json, ResponsePhoto.class);
                    Log.d("RES", "onResponsePhoto: "+responsePhoto.getUploadUrl());




                    Uri selectedImage = data.getData();
                    String wholeID = DocumentsContract.getDocumentId(selectedImage);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    String[] column = { MediaStore.Images.Media.DATA };

                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";

                    Cursor cursor = getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{ id }, null);

                    String filePath = "";

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();



                    UploadPhoto(responsePhoto.getUploadUrl(), filePath,token);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MsgActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

requestQueue.add(request);

//            Picasso.with(this)
//                    .load(mImageUri)
//                    .fit().centerInside()
//                    .into(mSelectImage);
            //mSelectImage.setImageURI(mImageUri);



        }
    }

public void UploadPhoto(final String upl_url, final String path, final String token){
Thread th = new Thread(new Runnable() {
    @Override
    public void run() {
      //  RequestQueue requestQueue= Volley.newRequestQueue(this);
        // String feedUrl="https://api.vk.com/method/photos.getMessagesUploadServer?"+this.getIntent().getStringExtra("token");
        StringBuilder response_sb = new StringBuilder();
        try {
            MultipartUtility multipart = new MultipartUtility(upl_url, "UTF-8");
            Log.d("PATH", "run: " +path);
            multipart.addFilePart("photo",new File(path));

            List<String> response = multipart.finish();

            for (String line : response) {
                response_sb.append(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("RES", "feedUrlResp: "+response_sb);

        String json = response_sb.toString();


        Gson gson = new Gson();
        UploadedPhoto uploadedPhoto = gson.fromJson(json, UploadedPhoto.class);

        RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        String feedUrl="https://api.vk.com/method/photos.saveMessagesPhoto?photo="+uploadedPhoto.getPhoto()+"&server="
                +uploadedPhoto.getServer()+"&hash="+uploadedPhoto.getHash()+"&"+token;
        Log.d("RES", "feedUrl: "+feedUrl);

        final StringRequest request=new StringRequest(Request.Method.POST, feedUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d("RES", "savePhoto: "+response);

                photoId=response.split("\"")[7];
                Log.d("RES", "savePhotoID: "+photoId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MsgActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue.add(request);



//        requestQueue.add(request);

    }
});
  th.start();
}

}
