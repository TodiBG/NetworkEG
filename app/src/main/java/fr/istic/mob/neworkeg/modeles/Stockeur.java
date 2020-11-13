package fr.istic.mob.neworkeg.modeles;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Stockeur {
    private static File createOrGetFile(File destination, String fileName, String folderName){
        File folder = new File(destination, folderName);
        folder.mkdirs() ;
        if(folder.isDirectory()){System.out.println("XXX PPPPPPPPPP"); }
        else{ System.out.println("XXX 55555555555555555555");}



        return new File(folder, fileName);
    }

    // ----------------------------------
    // READ & WRITE ON STORAGE
    // ----------------------------------

    private static String readOnFile(Context context, File file){
        JSONObject result = null;
        if (file.exists()) {
            try {

                JSONParser parser = new JSONParser();
                try {
                    result = (JSONObject) parser.parse(new FileReader(file)) ;
                } catch (ParseException e) { e.printStackTrace(); }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  new Gson().toJson(result) ;
    }


    // ---

    private static void writeOnFile(Context context, String text, File file){

        try {
            if(file.getParentFile().mkdirs()){ System.out.println("xxxx xxxxx");}
            else{ System.out.println("xxxx yyyyyyyyy");}

            FileOutputStream fos = new FileOutputStream(file);
            Writer w = new BufferedWriter(new OutputStreamWriter(fos));

            try {
                w.write(text);
                w.flush();
                fos.getFD().sync();
            } finally {
                w.close();
                //Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            //Toast.makeText(context, context.getString(R.string.error_happened), Toast.LENGTH_LONG).show();
        }


    }



    public static String getDataFromStorage(File rootDestination, Context context, String fileName, String folderName){
        File file = createOrGetFile(rootDestination, fileName, folderName);
        return readOnFile(context, file);
    }

    public static void setDataInStorage(File rootDestination, Context context, String fileName, String folderName, String text){
        File file = createOrGetFile(rootDestination, fileName, folderName);
        writeOnFile(context, text, file);
    }

    // ----------------------------------
    // EXTERNAL STORAGE
    // ----------------------------------

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }














}
