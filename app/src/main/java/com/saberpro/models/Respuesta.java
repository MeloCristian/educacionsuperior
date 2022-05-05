package com.saberpro.models;

public class Respuesta {

    private String id_respuesta;
    private String id_pregunta;
    private String descripcion;
    private boolean correcta;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId_respuesta() {
        return id_respuesta;
    }

    public void setId_respuesta(String id_respuesta) {
        this.id_respuesta = id_respuesta;
    }

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public void setCorrecta(boolean correcta) {
        this.correcta = correcta;
    }
}
