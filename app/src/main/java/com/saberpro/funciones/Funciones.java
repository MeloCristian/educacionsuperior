package com.saberpro.funciones;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class Funciones {
    public static String url = "http://192.168.0.5:3000/";
    public static String generateUrl(String s) {
        String[] p = s.split("/");
        String imageLink = "https://drive.google.com/uc?export=download&id=" + p[5];
        return imageLink;
    }
}
