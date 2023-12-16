package com.example.my_medicos.activities.slideshow;

import java.util.ArrayList;

public class Slideshow {
    private String title;
    private ArrayList<Image> images;
    private String file;

    public Slideshow(String title, ArrayList<Image> images, String file) {
        this.title = title;
        this.images = images;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static class Image {
        private String id;
        private String url;

        public Image(String id, String url) {
            this.id = id;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}