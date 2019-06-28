package com.example.sebastian.formex;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    private DatabaseConnector(){

    }
    public static DatabaseConnector getInstance(){
        if(instance == null)
            instance = new DatabaseConnector();
        return instance;
    }
    public void GrabFormulario(final DatabaseListener<Formulario> listener){
        final List<Pregunta> preguntas = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference preguntasRef = database.getReference().child("Preguntas");
        preguntasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    try{
                        Pregunta pregunta = snapshot.getValue(Pregunta.class);
                        preguntas.add(pregunta);
                    } catch(Exception e) {

                    }
                }
                listener.finish(new Formulario(preguntas));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
