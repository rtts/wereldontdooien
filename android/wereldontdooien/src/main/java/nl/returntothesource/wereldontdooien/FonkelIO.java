package nl.returntothesource.wereldontdooien;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jolandaverhoef on 04-01-14.
 */
public class FonkelIO {
    public static final String LOG_TAG = "FonkelIO";

    public static final String BASE_URL = "http://wereldontdooien.nl/";
    public static final String FILENAME = "fonkels.ser";
    public static final String DIRNAME  = "Fonkels";

    public static List<Fonkel> readFonkelsFromApi() {
        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        try {
            URL urlToRequest = new URL(BASE_URL + "api/");
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK)
                throw new IOException("Http Status " + statusCode);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Type fonkelType = new TypeToken<List<Fonkel>>() {}.getType();
            List<Fonkel> fonkels = new Gson().fromJson(reader, fonkelType);
            Log.d("FonkelIO", "Fonkels from api: " + fonkels);
            return fonkels;
        } catch (IOException e) {
            Log.w("FonkelIO", "Failed to read fonkels from api");
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            Log.w("FonkelIO", "Failed to read fonkels from api");
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }

    public static void saveFonkelFromAPI(Context context, String fonkelName) throws IOException {
        URL url = new URL(BASE_URL + "media/" + fonkelName);
        InputStream input = url.openStream();
        try {
            File storagePath = getAlbumStorageDir(context);
            OutputStream output = new FileOutputStream (new File(storagePath,fonkelName));
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }
    }

    public static void writeFonkelsToDisk(Context context, List<Fonkel> fonkels) {
        try {
            Log.d("FonkelIO", "Write to disk: " + fonkels);
            FileOutputStream file = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            file.write(new Gson().toJson(fonkels).getBytes());
            //ObjectOutput output = new ObjectOutputStream(file);
            //output.writeObject(fonkels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Fonkel> readFonkelsFromDisk(Context context) {
        try {
            InputStream in = context.openFileInput(FILENAME);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Type fonkelType = new TypeToken<List<Fonkel>>() {}.getType();
            List<Fonkel> fonkels = new Gson().fromJson(reader, fonkelType);
            //ObjectInput input = new ObjectInputStream(file);
            //deserialize the List
            //List<String> fonkels = (List<String>)input.readObject();
            Log.d("FonkelIO", "Read from disk: " + fonkels);
            return fonkels;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getAlbumStorageDir(Context context) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), DIRNAME);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
