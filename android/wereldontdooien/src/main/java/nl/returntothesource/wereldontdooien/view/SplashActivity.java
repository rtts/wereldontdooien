package nl.returntothesource.wereldontdooien.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import nl.returntothesource.wereldontdooien.R;
import nl.returntothesource.wereldontdooien.receiver.AlarmReceiver;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SquareView squareView = (SquareView) findViewById(R.id.wereldontdooisters);
        Bitmap b = BitmapFactory.decodeResource(SplashActivity.this.getResources(),
                R.drawable.splash);
        squareView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(b));
        AlarmReceiver.setAlarm(SplashActivity.this);
    }

    public void next(View v) {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }
}
