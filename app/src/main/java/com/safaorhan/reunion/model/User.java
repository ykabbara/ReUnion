package com.safaorhan.reunion.model;

import com.safaorhan.reunion.R;

public class User {
    String id;
    String name;
    String surname;
    String email;

    public int getColorId() {
        return colorId;
    }

    int colorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        int[] colorsIds = {R.color.tomato, R.color.turquoise_blue,R.color.dark_cyan,R.color.navy_blue,R.color.dark_orchide,R.color.fruit_salad,R.color.dodger_blue};
        int rand = (int)(Math.random()*7);
        return colorsIds[rand];

    }
}
