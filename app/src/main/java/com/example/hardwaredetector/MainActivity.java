package com.example.hardwaredetector;
import java.io.File;
import java.io.IOException;
import java.security.Policy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import android.content.Intent;
import android.net.Uri;


import android.os.Bundle;
import android.util.Log;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;

    static int TAKE_PICTURE = 1;
    Uri outputFileUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) this.findViewById(R.id.surface1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    public void surfaceChanged(SurfaceHolder arg0, int arg1,int arg2,int arg3){

    }
    public void surfaceCreated(SurfaceHolder arg0){

    }

    public void surfaceDestroyed(SurfaceHolder arg0){

    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            camera = Camera.open();

        }
        catch (Exception e){
            //---exception handling here---
            Log.d("Flashlight",e.toString());
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        TurnOff(null);
        camera.release();
    }
    public void TurnOn(View view){
        if(FlashAvailable() && camera != null){
            Parameters p = camera.getParameters();

            //---works with Andrid 2.x as well---
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
            try {
                camera.setPreviewDisplay(surfaceHolder);

            }
            catch (IOException e){

                e.printStackTrace();
            }
        }
    }
    public void TurnOff(View view){
        if(FlashAvailable() && camera != null){
            Parameters p = camera.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
        }
    }
    private Boolean FlashAvailable(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
            Toast.makeText(this,"The Center key was pressed",Toast.LENGTH_SHORT).show();
            return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast.makeText(this,"The Right key was pressed",Toast.LENGTH_SHORT).show();
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast.makeText(this,"The Left key was pressed",Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this,"The Back key was pressed",Toast.LENGTH_SHORT).show();
               //---move the entire task to the backgroud---
                moveTaskToBack(true);

                //---this event has been handled---
                return true;


        }
        //---this event has been handled---
        return false;
    }

    public void btnTakePhoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),"MyPhoto.jpg");
        outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent,TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Toast.makeText(this, outputFileUri.toString(),Toast.LENGTH_LONG).show();
        }

    }
}
