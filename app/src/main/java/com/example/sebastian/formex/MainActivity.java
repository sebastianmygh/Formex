package com.example.sebastian.formex;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private LinearLayout layout;
    private Integer preguntasTotales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startApplication();
    }

    private void declareViews() {
        TextView enviar = findViewById(R.id.am_et_enviar);
        enviar.setVisibility(View.VISIBLE);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResults();
            }
        });
    }

    private int convertPxToDp(int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    private void initialize(Formulario formulario) {
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
        if(amountChecked == preguntasTotales){
            Toast.makeText(this, "Opciones seleccionadas: " + opcionesSeleccionadas.toString(), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Flaco, te falta responder", Toast.LENGTH_SHORT).show();
        }
    }

    private void startApplication(){
        DatabaseConnector connector = DatabaseConnector.getInstance();
        connector.GrabFormulario(new DatabaseListener<Formulario>() {
            @Override
            public void finish(Formulario item) {
                preguntasTotales = item.getPreguntas().size();
                initialize(item);
                declareViews();
            }
        });
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
