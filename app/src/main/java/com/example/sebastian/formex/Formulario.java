package com.example.sebastian.formex;

import java.util.List;

public class Formulario {
    private List<Pregunta> preguntas;

    public Formulario(List<Pregunta> preguntas){
        this.preguntas = preguntas;
    }
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
}
