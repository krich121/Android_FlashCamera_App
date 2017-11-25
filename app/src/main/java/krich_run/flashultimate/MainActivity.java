package krich_run.flashultimate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraDevice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void FlashClick(View view) {
        getCamera();

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature((PackageManager.FEATURE_CAMERA_FLASH));

        if (!hasFlash) {
//            DialogInterface dialogInterface = new DialogInterface.OnClickListener()
//            {
//                public void onClick(DialogInterface dialogInterface, int i)
//                {
//                    finish();
//                }
//            };

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("ผิดพลาด !!");
            alert.setMessage("แุปกรณ์ของคุณ ไม่สนับสนุนการเปิดแฟลช !");
//            alert.setButton("OK", dialogInterface);

            alert.show();
        }
        else
        {
            doFlash();
        }

    }

    private void getCamera() {
        if (camera == null)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
            }

            try{
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                params = camera.getParameters();
            }
            catch (RuntimeException e){
                Log.e("Camera Error.", e.getMessage());
            }
        }
    }

    private void doFlash() {
        if(!isFlashOn){
            openFlash();
        }
        else{
            offFlash();
        }
    }

    private void openFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void offFlash(){
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            camera.release();
            camera = null;
            isFlashOn = false;
        }
    }
}
