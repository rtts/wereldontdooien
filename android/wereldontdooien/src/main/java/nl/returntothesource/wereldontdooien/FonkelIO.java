package nl.returntothesource.wereldontdooien;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jolandaverhoef on 04-01-14.
 */
public class FonkelIO {
    public static final String LOG_TAG = "FonkelIO";

    public static final String BASE_URL = "http://wereldontdooien.nl/";
    public static final String FILENAME = "fonkels.ser";
    public static final String DIRNAME  = "Fonkels";

    public static List<String> readFonkelsFromApi() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(BASE_URL + "api/"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();
                List<String> fonkels = Arrays.asList(responseString.split(","));
                Log.d("FonkelIO", "From api: " + fonkels);
                return fonkels;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static void writeFonkelsToDisk(Context context, List<String> fonkels) {
        try {
            Log.d("FonkelIO", "Write to disk: " + fonkels);
            FileOutputStream file = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(fonkels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> readFonkelsFromDisk(Context context) {
        try {
            InputStream file = context.openFileInput(FILENAME);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            List<String> fonkels = (List<String>)input.readObject();
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
}
