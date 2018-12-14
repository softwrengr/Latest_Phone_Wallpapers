package com.squaresdevelopers.latestphonewallpapers.dataModels;

public class LikedImagesmodel {

    String name;
    byte[] image;


   /* public FolderImageModel(String name, String path, byte[] image) {
        this.name = name;
        this.path = path;
        this.image = image;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
