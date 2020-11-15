package fr.istic.mob.neworkeg.modeles;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Fonction {

    public static Uri takeScreenshot(Activity activity, View view, String dirPath, String fileName){
        verifyStoragePermission(activity) ;

        System.out.println("dirPath : "+ dirPath);
        File file = new File(dirPath);

        if (!(file.exists())){
            boolean mkdirs = file.mkdirs() ;
        }

        String path = dirPath+"/"+fileName+".jpeg" ;
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        File image = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(image) ;

            int quality = 100 ;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream );
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {  e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }

        return  Uri.parse(path)  ;
    }


    private  static  int REQUEST_EXTERNAL_STORAGE = 1 ;
    private static  String[] PERMISSION_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE} ;

    private static  void  verifyStoragePermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
        if ((permission != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }


}
