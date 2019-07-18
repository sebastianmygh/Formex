package com.example.sebastian.formex;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private FirebaseAuth auth;

    private void isLoggedin(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        isLoggedin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.ar_rv_respuestas);
        FloatingActionButton fab = findViewById(R.id.ar_fab_logout);
        auth = FirebaseAuth.getInstance();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser != null){
                    auth.signOut();
                    isLoggedin();
                }
            }
        });
        listen();
    }

    private void listen(){
        DatabaseConnector connector = DatabaseConnector.getInstance();
        connector.grabRespuestas(new DatabaseListener<List<Respuesta>>() {
            @Override
            public void finish(List<Respuesta> item) {
                RespuestaAdapter adapter = new RespuestaAdapter(item);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(manager);
            }
        });
    }
}
