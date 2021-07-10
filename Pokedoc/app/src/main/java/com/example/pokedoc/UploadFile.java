package com.example.pokedoc;


class UploadFiless {
    public String name;
    public String url;

    public UploadFiless(){

    }

    public UploadFiless(String name, String url){
        this.name=name;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}