package com.example.beavi5.vktest.MODEL.models;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beavi5.vktest.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by beavi5 on 18.09.2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.FriendHolder> {
    List<FriendModel> friendsList;
    String token;

    public RVAdapter(List<FriendModel> friendsList, String token) {
        this.friendsList = friendsList;
        this.token = token;
    }

    public RVAdapter(List<FriendModel> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row,parent,false));
    }

    @Override
    public void onBindViewHolder(final FriendHolder holder, final int position) {
        holder.firstNameFriendTV.setText(friendsList.get(position).getFirstName());
        holder.lastNameFriendTV.setText(friendsList.get(position).getLastName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                    //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                    LayoutInflater li = LayoutInflater.from(holder.itemView.getContext());
                    View addNewCommentView = li.inflate(R.layout.dialog_new_msg, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(holder.itemView.getContext());

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(addNewCommentView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText etNewComment = (EditText) addNewCommentView.findViewById(R.id.etNewComment);
                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Отправить",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:


                // формируют url запроса
               // String url = "https://api.vk.com/method/messages.send?user_id="+friendsList.get(position).getUserId()+"&message="+etNewComment.getText().toString()+"&"+token;
                final String feedUrl = "https://api.vk.com/method/messages.send?user_id="+friendsList.get(position).getUserId()+"&v=5.28&"+token+"&message="+etNewComment.getText().toString();





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


                                        }}
                                            ).setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog =  mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();


            }
        });

        Picasso.with(holder.photoFriendIV.getContext()).load(friendsList.get(position).getPhotoUrl()).into(holder.photoFriendIV);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class  FriendHolder extends RecyclerView.ViewHolder {

        TextView lastNameFriendTV;
        TextView firstNameFriendTV;
        ImageView photoFriendIV;

        public FriendHolder(View itemView) {
            super(itemView);

            firstNameFriendTV = itemView.findViewById(R.id.firstNameTV);
            lastNameFriendTV = itemView.findViewById(R.id.lastNameTV);
            photoFriendIV = itemView.findViewById(R.id.photoIV);



        }
    }
}
