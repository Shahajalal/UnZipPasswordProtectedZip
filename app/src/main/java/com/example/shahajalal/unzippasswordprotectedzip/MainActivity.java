package com.example.shahajalal.unzippasswordprotectedzip;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class MainActivity extends AppCompatActivity {
    Button button;
    private static final int EXTERNAL_READ_REQ_CODE = 26;
    String of= Environment.getExternalStorageDirectory().getPath() + "/FileInAsset/"+"xmlLayout.zip";
    String path=Environment.getExternalStorageDirectory().getPath() + "/FileInAsset/unzip";

    //This has been done using "https://cryptii.com/text-decimal" this website
    String UnZipPasswordProtectedZip=new String(new byte[]{85,110,90,105,112,80,97,115,115,119,111,114,100,80,114,111,116,101,99,116,101,100,90,105,112});

    String packageName=new String(new byte[]{99,111,109,46,101,120,97,109,112,108,101,46,115,104,97,104,97,106,97,108,97,108,46,117,110,122,105,112,112,97,115,115,119,111,114,100,112,114,111,116,101,99,116,101,100,122,105,112});


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(getTitle().equals(UnZipPasswordProtectedZip)) {
            if(this.getApplicationContext().getPackageName().equals(packageName)) {
                setContentView(R.layout.activity_main);
                button=findViewById(R.id.buttonid);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyassetfile();
                        unpackZip();
                    }
                });
            }
        }else {
            finish();
        }


    }
    public boolean copyassetfile(){

        try{
            permission();
            InputStream inputStream=this.getAssets().open("xmlLayout.zip");
            String outputFile= of;
            File file=new File(outputFile);
            if(file.exists()){

                Toast.makeText(this,"File already exists",Toast.LENGTH_SHORT).show();
                return true;
            }else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            OutputStream OS = new FileOutputStream(outputFile,true);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff))>0)
            {
                OS.write(buff,0,length);
            }
            OS.flush();
            OS.close();
            Toast.makeText(this,"File Copied successful",Toast.LENGTH_SHORT).show();

        }catch (Exception e){

        }
        return true;
    }

    public void permission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Test","Test");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_READ_REQ_CODE);

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_READ_REQ_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private boolean unpackZip()
    {

        try {
            ZipFile zipFile = new ZipFile(of);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword("shahajalal");
            }
            zipFile.extractAll(path+"new");
            Toast.makeText(this,"Unzip Successful",Toast.LENGTH_SHORT).show();
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return true;
    }
}
