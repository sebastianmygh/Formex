package com.example.sebastian.formex;

import java.util.List;

public class Pregunta {
    private String pregunta;
    private List<Opcion> opciones;

    public Pregunta() { }

    public Pregunta(String pregunta){
        this.pregunta = pregunta;
    }

    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }

    public String getPregunta() {
        return pregunta;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
