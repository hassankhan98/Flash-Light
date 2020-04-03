package com.example.flashlight;
 import androidx.annotation.NonNull;
 import androidx.annotation.RequiresApi;
 import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
 import androidx.core.content.ContextCompat;

 import android.Manifest;
 import android.annotation.TargetApi;
 import android.content.Context;
 import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
 import android.hardware.camera2.CameraAccessException;
 import android.hardware.camera2.CameraManager;
 import android.os.Build;
 import android.os.Bundle;
import android.view.View;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.Button;
import android.widget.ImageButton;
 import android.widget.ImageView;
 import android.widget.Toast;
 import android.widget.ToggleButton;

 import com.startapp.android.publish.adsCommon.StartAppAd;
 import com.startapp.android.publish.adsCommon.StartAppSDK;

 import java.security.Permission;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

   Button FlashLight;

   private final int CAMERRA_REQUEST_CODE = 2;

   boolean hasCameraFlash = false;
   boolean isFlashOn = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        FlashLight = findViewById(R.id.btn);

        FlashLight.setOnClickListener(new View.OnClickListener() {
         @RequiresApi(api = Build.VERSION_CODES.M)
         @Override
         public void onClick(View v) {

             askPermission(Manifest.permission.CAMERA, CAMERRA_REQUEST_CODE);
         }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLight(){

     if(hasCameraFlash){
       if(isFlashOn) {
         FlashLight.setBackgroundResource(R.drawable.off_button);
         flashLightOff();
         isFlashOn = false;
       }
       else
       {
        FlashLight.setBackgroundResource(R.drawable.on_button);
        flashLightOn();
        isFlashOn = true;

       }

     }
       else
      {
       Toast.makeText(MainActivity.this, "No Flash Available on your device",Toast.LENGTH_LONG).show();
      }




    }

 private void flashLightOn() {
     CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

  try{
   String cameraid = cameraManager.getCameraIdList()[0];
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    cameraManager.setTorchMode(cameraid, true);
   }

  } catch (CameraAccessException e) {
   e.printStackTrace();
  }
 }


 private void flashLightOff() {
     CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

     try{
       String cameraid = cameraManager.getCameraIdList()[0];
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
       cameraManager.setTorchMode(cameraid, false);
      }

     } catch (CameraAccessException e) {
      e.printStackTrace();
     }
 }

 @RequiresApi(api = Build.VERSION_CODES.M)
 private void askPermission(String permission, int requestCode) {

     if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){


      ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
  }
     else {
      flashLight();
     }

 }

 @RequiresApi(api = Build.VERSION_CODES.M)

   public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){

     switch (requestCode){
      case CAMERRA_REQUEST_CODE:
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
          hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
          Toast.makeText(this, "Camera Permission Granted",Toast.LENGTH_LONG).show();
          flashLight();
        }
        else
        {
           Toast.makeText(this, "Camera Permission Denied",Toast.LENGTH_LONG).show();

        }

        break;

     }

 }

}



