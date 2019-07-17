package com.example.sebastian.formex;

public class Respuesta {
    private String pregunta;
    private String opcion;

    public Respuesta(String pregunta, String opcion){
        this.pregunta = pregunta;
        this.opcion = opcion;
    }

    public Respuesta(){

    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
}
