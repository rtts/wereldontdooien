package nl.returntothesource.wereldontdooien.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import nl.returntothesource.wereldontdooien.R;

/**
 * Created by jolandaverhoef on 28/08/14.
 */
public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView t2 = (TextView) findViewById(R.id.textView2);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t3 = (TextView) findViewById(R.id.textView3);
        t3.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
