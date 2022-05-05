package com.saberpro.funciones;

public class Funciones {
    public static String url = "http://192.168.0.5:3000/";
    public static String generateUrl(String s) {
        String[] p = s.split("/");
        String imageLink = "https://drive.google.com/uc?export=download&id=" + p[5];
        return imageLink;
    }
}
