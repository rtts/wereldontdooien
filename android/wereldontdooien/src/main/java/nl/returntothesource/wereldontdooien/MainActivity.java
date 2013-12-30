package nl.returntothesource.wereldontdooien;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WebView contentView = (WebView) findViewById(R.id.fullscreen_content);
        contentView.loadUrl("http://www.google.nl/");
    }

}
