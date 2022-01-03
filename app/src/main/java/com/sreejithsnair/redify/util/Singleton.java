package com.sreejithsnair.redify.util;

import android.graphics.Bitmap;
import android.net.Uri;

public class Singleton {

    private boolean IS_FULL_SCREEN = true;
    private boolean IS_PLACEHOLDER_OFF = false;
    private boolean IS_REDIFY_ENABLED = false;
    private Uri imageURI;
    private Bitmap imageBitmap;

    // Getters
    public Uri getImageURI() { return imageURI; }
    public boolean isIS_FULL_SCREEN() {
        return IS_FULL_SCREEN;
    }
    public boolean isIS_PLACEHOLDER_OFF() {
        return IS_PLACEHOLDER_OFF;
    }
    public boolean isIS_REDIFY_ENABLED() { return IS_REDIFY_ENABLED; }
    public Bitmap getImageBitmap() { return imageBitmap; }

    // Setters
    public void setImageURI(Uri imageURI) {
        this.imageURI = imageURI;
    }
    public void setIS_FULL_SCREEN(boolean IS_FULL_SCREEN) {
        this.IS_FULL_SCREEN = IS_FULL_SCREEN;
    }
    public void setIS_PLACEHOLDER_OFF(boolean IS_PLACEHOLDER_OFF) { this.IS_PLACEHOLDER_OFF = IS_PLACEHOLDER_OFF; }
    public void setIS_REDIFY_ENABLED(boolean IS_REDIFY_ENABLED) { this.IS_REDIFY_ENABLED = IS_REDIFY_ENABLED; }
    public void setImageBitmap(Bitmap imageBitmap) { this.imageBitmap = imageBitmap; }

    private Singleton(){}

    private static Singleton singleton;

    public static Singleton getInstance(){
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }

}
