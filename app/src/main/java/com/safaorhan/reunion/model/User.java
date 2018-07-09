package com.safaorhan.reunion.model;

import android.support.v4.content.ContextCompat;

import com.safaorhan.reunion.R;

import java.util.ArrayList;

public class User {
    String id;
    String name;
    String surname;
    String email;
    int colorId;

    public int getColorId() {
        return colorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        //this.colorId = randomColor();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.colorId = randomColor();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int randomColor (){
        int[] colorsIds = {R.color.tomato,R.color.turquoise_blue,R.color.dark_cyan,R.color.navy_blue,R.color.dark_orchide,R.color.fruit_salad,R.color.dodger_blue};
        int rand = (int)(Math.random()*7);
        return colorsIds[rand];

    }


}
