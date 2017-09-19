package com.example.beavi5.vktest.VIEW;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.beavi5.vktest.R;
import com.squareup.picasso.Picasso;

public class MsgActivity extends AppCompatActivity {
    private final static int GALLERY_REQUEST=1;
    private Uri mImageUri;
    Button imgLoadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        imgLoadBtn = (Button) findViewById(R.id.imgLoadBtn);
        imgLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
            mImageUri = data.getData();
//            Picasso.with(this)
//                    .load(mImageUri)
//                    .fit().centerInside()
//                    .into(mSelectImage);
            //mSelectImage.setImageURI(mImageUri);



        }
    }
}
