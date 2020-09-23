package com.internshala.galleryapplication;

public class ImageModel {
    String id;
    String path;
    String filename;

    public ImageModel() {

    }

    public ImageModel(String id, String path,String fileName) {
        this.id=id;
        this.path=path;
        this.filename=fileName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
