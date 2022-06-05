package com.example.finalproject;

public class compType {
    private String name;
    private String description;
    private int image ;

    public compType(String name, String description,int image){
        this.name = name ;
        this.description = description;
        this.image = image;
    }

    // getter & setters
    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }


}
