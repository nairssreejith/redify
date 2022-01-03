package com.sreejithsnair.redify.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sreejithsnair.redify.R;
import com.sreejithsnair.redify.util.Singleton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public ImageView imvImage;

    public ImageButton imgBtnToggleFullScreen;

    public Button btnGallery;
    public Button btnRedify;

    private final int REQUEST_CODE = 1;

    public static native int[] redified(int[] pixels, int depth, float red, int width, int height);

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnGallery.setOnClickListener( v -> openGallery());
        imgBtnToggleFullScreen.setOnClickListener(v -> toggleAction());
        btnRedify.setOnClickListener(v -> performRedify());
    }

    private void performRedify() {
        Singleton.getInstance().setImageBitmap(escalateRed(10, 1f, Singleton.getInstance().getImageBitmap()));
        imvImage.setImageBitmap(Singleton.getInstance().getImageBitmap());
    }

    public static Bitmap escalateRed(int depth, float red, Bitmap inputImage){
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true);

        inputImage.getPixels(pixels,0,width,0,0, width, height);
        redified(pixels, depth, red, width, height);
        inputImage.setPixels(pixels,0, width, 0,0, width, height);

        return inputImage;
    }

    private void openGallery() {
        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImage, REQUEST_CODE);
    }

    private void toggleAction(){
        fullScreenState(Singleton.getInstance().isIS_FULL_SCREEN());
        Singleton.getInstance().setIS_FULL_SCREEN(!Singleton.getInstance().isIS_FULL_SCREEN());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            try {
                Singleton.getInstance().setImageURI(data.getData());
                Singleton.getInstance().setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Singleton.getInstance().getImageURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imvImage.setImageBitmap(Singleton.getInstance().getImageBitmap());
            btnRedify.setEnabled(true);
            Singleton.getInstance().setIS_REDIFY_ENABLED(true);
            Singleton.getInstance().setIS_PLACEHOLDER_OFF(true);
        }
    }

    private void fullScreenState(Boolean state){
        if(state){
            imgBtnToggleFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_24);
            imvImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        else{
            imgBtnToggleFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24);
            imvImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(Singleton.getInstance().isIS_PLACEHOLDER_OFF()){
            outState.putString("URI", Singleton.getInstance().getImageURI().toString());
            outState.putBoolean("IS_PLACEHOLDER_OFF", Singleton.getInstance().isIS_PLACEHOLDER_OFF());
            outState.putBoolean("IS_FULL_SCREEN", !Singleton.getInstance().isIS_FULL_SCREEN());
        }
        else{
            outState.putInt("ImageResource", R.drawable.ic_placeholder);
            outState.putBoolean("IS_PLACEHOLDER_OFF", Singleton.getInstance().isIS_PLACEHOLDER_OFF());
            outState.putBoolean("IS_FULL_SCREEN", !Singleton.getInstance().isIS_FULL_SCREEN());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.getBoolean("IS_PLACEHOLDER_OFF")){
            fullScreenState(savedInstanceState.getBoolean("IS_FULL_SCREEN"));
            imvImage.setImageBitmap(Singleton.getInstance().getImageBitmap());
        }
        else{
            fullScreenState(savedInstanceState.getBoolean("IS_FULL_SCREEN"));
            imvImage.setImageResource(savedInstanceState.getInt("ImageResource"));
        }
    }

    private void init(){
        imvImage = findViewById(R.id.img_view_image);
        imvImage.setImageResource(R.drawable.ic_placeholder);

        imgBtnToggleFullScreen = findViewById(R.id.img_btn_toggle_fullscreen);

        btnGallery = findViewById(R.id.btn_gallery);

        btnRedify = findViewById(R.id.btn_redify);
        if(!Singleton.getInstance().isIS_REDIFY_ENABLED()){ btnRedify.setEnabled(false); }
    }
}