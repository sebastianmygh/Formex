package com.example.sebastian.formex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.ar_rv_respuestas);
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
