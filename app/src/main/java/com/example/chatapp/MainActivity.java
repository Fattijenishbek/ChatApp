package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.models.Chat;
import com.example.chatapp.chat.ChatActivity;
import com.example.chatapp.contacts.ContactsActivity;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private List<Chat> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(this, PhoneActivity.class));
        }
            recyclerView = findViewById(R.id.recyclerView);
            initList();
            getChats();

    }

    /**
     * I don't know, but this is important method
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(MainActivity.this , ContactsActivity.class));
            finish();
        }
    }

    public void onClickContacts(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ChatsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chat",list.get(position));
                startActivity(intent);
            }
        });
    }
    private void getChats() {
        String myUserId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("userIds",myUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable  QuerySnapshot snapshots, @Nullable  FirebaseFirestoreException error) {
                        for(DocumentChange change : snapshots.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    Chat chat = change.getDocument().toObject(Chat.class);
                                    chat.setId(change.getDocument().getId());
                                    list.add(chat);
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}