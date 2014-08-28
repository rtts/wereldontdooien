package nl.returntothesource.wereldontdooien.io;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by jolandaverhoef on 04-01-14.
 */
public class FonkelIO {
    public static final String LOG_TAG = "FonkelIO";

    public static final String BASE_URL = "http://wereldontdooien.nl/";
    public static final String FILENAME = "fonkels.ser";
    public static final String DIRNAME  = "Fonkels";

    public static List<Fonkel> readFonkelsFromApi() {
        //disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        try {
            URL urlToRequest = new URL(BASE_URL + "api/");
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(5000);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK)
                throw new IOException("Http Status " + statusCode);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Type fonkelType = new TypeToken<List<Fonkel>>() {}.getType();
            List<Fonkel> fonkels = new Gson().fromJson(reader, fonkelType);
            //Log.d("FonkelIO", "Fonkels from api: " + fonkels);
            return fonkels;
        } catch (IOException e) {
            //Log.w("FonkelIO", "Failed to read fonkels from api");
            //e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            //Log.w("FonkelIO", "Failed to read fonkels from api");
            //e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
//        Fonkel f = new Fonkel();
//        f.afbeelding = "logo_large.png";
//        f.type = 2;
//        List<Fonkel> result = new ArrayList<Fonkel>();
//        result.add(f);
//        return result;
    }

    public static void writeFonkelsToDisk(Context context, List<Fonkel> fonkels) {
        try {
            //Log.d("FonkelIO", "Write to disk: " + fonkels);
            FileOutputStream file = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            file.write(new Gson().toJson(fonkels).getBytes());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void deleteFonkelsFromDisk(Context context) {
        context.deleteFile(FILENAME);
    }
    public static List<Fonkel> readFonkelsFromDisk(Context context) {
        try {
            InputStream in = context.openFileInput(FILENAME);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Type fonkelType = new TypeToken<List<Fonkel>>() {}.getType();
            List<Fonkel> fonkels = new Gson().fromJson(reader, fonkelType);
            //Log.d("FonkelIO", "Read from disk: " + fonkels);
            return fonkels;
        }
        catch(Exception e){
            //e.printStackTrace();
        }
        return null;
    }
}
