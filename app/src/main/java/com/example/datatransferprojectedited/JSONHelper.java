package com.example.datatransferprojectedited;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.example.datatransferprojectedited.model.Datum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONHelper {

    public static void saveJSON(Context context, ArrayList<Datum> userList, String fileName) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(userList);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadJSONString(Context context, String fileName) {

        String jsonString = "";
        try (FileInputStream fis = context.openFileInput(fileName)) {
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            jsonString = new String(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Error loading JSON file: ", e);
        }
        return jsonString;
    }

    public static ArrayList<Datum> loadJSON(Context context, String filename) {
        Gson gson = new Gson();
        String jsonString = loadJSONString(context, filename);
        Type type = new TypeToken<ArrayList<Datum>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }
//        StringBuilder jsonString = new StringBuilder();
//        try (FileInputStream fis = context.openFileInput(fileName);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonString.append(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonString.toString();
//    }
}
