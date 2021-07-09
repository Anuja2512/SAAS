package com.example.pokedoc;


class uploadFile {
    public String name;
    public String url;

    public uploadFile(){

    }

    public uploadFile(String name, String url){
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