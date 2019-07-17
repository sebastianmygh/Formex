package com.example.sebastian.formex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private LinearLayout layout;
    private Integer preguntasTotales;
    private ProgressBar loadingBar;
    private Formulario formulario;
    private DatabaseConnector connector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingBar = findViewById(R.id.am_pb_loading);
        setLoading(true);
        startApplication();
    }

    private void declareViews() {
        if(preguntasTotales == 0){
            TextView error = findViewById(R.id.am_tv_empty);
            error.setVisibility(View.VISIBLE);
        }
        else{
            TextView enviar = findViewById(R.id.am_et_enviar);
            enviar.setVisibility(View.VISIBLE);
            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getResults();
                }
            });
        }
    }

    private int convertPxToDp(int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    private void setLoading(Boolean status){
        if(status){
            loadingBar.setVisibility(View.VISIBLE);
            loadingBar.setIndeterminate(true);
        } else{
            loadingBar.setVisibility(View.GONE);
            loadingBar.setIndeterminate(false);
        }
    }

    private void initialize() {
        layout = findViewById(R.id.am_ll_form);
        Point screen = new Point();
        getWindowManager().getDefaultDisplay().getSize(screen);
        LinearLayout.LayoutParams layoutTextview = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTextview.setMargins(0, convertPxToDp(15), 0, convertPxToDp(15));
        for(Pregunta pregunta : formulario.getPreguntas()){
            TextView textView = new TextView(this);
            textView.setLayoutParams(layoutTextview);
            textView.setText(pregunta.getPregunta());
            textView.setTextSize(.02f * screen.x);
            layout.addView(textView);
            RadioGroup radioGroup = new RadioGroup(this);

            for(Opcion opcion : pregunta.getOpciones()){
                RadioButton button = new RadioButton(this);
                LinearLayout.LayoutParams layoutRadioButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                button.setLayoutParams(layoutRadioButton);
                button.setText(opcion.getOpcion());
                radioGroup.addView(button);
            }
            layout.addView(radioGroup);
        }
    }

    private void getResults(){
        Integer amountChecked = 0;
        List<String> opcionesSeleccionadas = new ArrayList<>();
        for(int i = 0; i < layout.getChildCount(); i++){
            View v = layout.getChildAt(i);
            if(v instanceof RadioGroup){
                RadioGroup rg = (RadioGroup)v;
                RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
                if(rb != null){
                    amountChecked ++;
                    opcionesSeleccionadas.add(rb.getText().toString());
                }
            }
        }
        if(amountChecked.equals(preguntasTotales)){
            saveResults(opcionesSeleccionadas);
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Faltan responder un par de preguntas", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveResults(List<String> listado){
        List<Respuesta> respuestas = new ArrayList<>();
        for (int i = 0; i < listado.size(); i++){
            Respuesta respuesta = new Respuesta(formulario.getPreguntas().get(i).getPregunta(), listado.get(i));
            respuestas.add(respuesta);
        }

        connector = DatabaseConnector.getInstance();
        connector.saveRespuestas(respuestas);

    }

    private void startApplication(){
        connector = DatabaseConnector.getInstance();

        connector.grabFormulario(new DatabaseListener<Formulario>() {
            @Override
            public void finish(Formulario item) {
                preguntasTotales = item.getPreguntas().size();
                setLoading(false);
                formulario = item;
                initialize();
                declareViews();
            }
        });
    }
}
