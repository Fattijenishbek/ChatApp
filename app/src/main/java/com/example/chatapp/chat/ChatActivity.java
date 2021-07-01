package com.example.chatapp.chat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.chatapp.R;
import com.example.chatapp.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText editText;
    private User user;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editText = findViewById(R.id.editText);
        user = (User) getIntent().getSerializableExtra("user");
        chat = new Chat();
//        chat.setId();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(user.getId());
        userIds.add(FirebaseAuth.getInstance().getUid());
        chat.setUserIds(userIds);
    }

//    private String getChatId(){
//        String myUserId = FirebaseAuth.getInstance().getUid();
//        if(myUserId.compareTo(user.getId())>0){
//
//        }
//    }

    public void onClickSend(View view) {
        String text = editText.getText().toString().trim();
    }
}