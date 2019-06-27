package com.example.sebastian.formex;

import java.util.List;

public class Formulario {
    private List<Pregunta> opciones;

    public Formulario(List<Pregunta> opciones){
        this.opciones = opciones;
    }
    public List<Pregunta> getOpciones() {
        return opciones;
    }
}
