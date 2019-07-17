package com.example.sebastian.formex;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    private final String PREGUNTAS = "Preguntas";
    private final String RESPUESTAS = "Respuestas";
    private FirebaseDatabase database;

    private DatabaseConnector(){

    }
    public static DatabaseConnector getInstance(){
        if(instance == null)
            instance = new DatabaseConnector();
        return instance;
    }
    public void grabRespuestas(final DatabaseListener<List<Respuesta>> listener){
        final List<Respuesta> respuestas = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference respuestasRef = database.getReference().child(RESPUESTAS);
        respuestasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    try{
                        GenericTypeIndicator<ArrayList<Respuesta>> t = new GenericTypeIndicator<ArrayList<Respuesta>>() {};
                        for (Respuesta respuesta : snapshot.getValue(t)){
                            respuestas.add(respuesta);
                        }
                    } catch(Exception e)
                    {
                        System.out.println("");
                    }
                }
                listener.finish(respuestas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.finish(new ArrayList<Respuesta>());
            }
        });
    }

    public void grabFormulario(final DatabaseListener<Formulario> listener){
        final List<Pregunta> preguntas = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference preguntasRef = database.getReference().child(PREGUNTAS);
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
                listener.finish(new Formulario(new ArrayList<Pregunta>()));
            }
        });
    }


    public void saveRespuestas(List<Respuesta> respuestas){
        DatabaseReference reference = database.getReference().child(RESPUESTAS);
        reference.push().setValue(respuestas);
    }

    private Pregunta crearPregunta(String pregunta, String... opciones){
        List<Opcion> options = new ArrayList<>();
        for(String opt : opciones){
            options.add(new Opcion(opt));
        }
        Pregunta preguntaFinal = new Pregunta(pregunta);
        preguntaFinal.setOpciones(options);
        return preguntaFinal;
    }
}
