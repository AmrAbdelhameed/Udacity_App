package com.example.amr.udacity_app.Models;

public class ListTrailer {

    private String name;
    private String type;

    public ListTrailer() {}

    public ListTrailer(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
