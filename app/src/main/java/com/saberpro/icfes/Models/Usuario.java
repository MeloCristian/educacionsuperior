package com.saberpro.icfes.Models;

public class Usuario {

    private String id_usuario;
    private String email_us;
    private String pass_us;
    private String id_rol;

    public String getEmail_us() {
        return email_us;
    }

    public void setEmail_us(String email_us) {
        this.email_us = email_us;
    }

    public String getPass_us() {
        return pass_us;
    }

    public void setPass_us(String pass_us) {
        this.pass_us = pass_us;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_rol() {
        return id_rol;
    }

    public void setId_rol(String id_rol) {
        this.id_rol = id_rol;
    }
}
