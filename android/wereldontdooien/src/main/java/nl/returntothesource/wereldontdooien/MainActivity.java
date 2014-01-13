package nl.returntothesource.wereldontdooien;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(12);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        new DownloadFonkelsTask().execute();
        // Set the alarm for the daily notification, if this is not yet done
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmReceiver.setAlarm(MainActivity.this);
            }
        }).start();
    }

    private class DownloadFonkelsTask extends AsyncTask<Void, Void, List<String>> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected List<String> doInBackground(Void... voids) {
            List<String> fonkels = FonkelIO.readFonkelsFromApi();
            FonkelIO.writeFonkelsToDisk(MainActivity.this, fonkels);
            return fonkels;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(List<String> result) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();
            adapter.setImages(result);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {
        ImageLoader imageLoader;
        private List<String> images;

        public ImagePagerAdapter() {
            imageLoader = new ImageLoader(MainActivity.this);
        }

        @Override
        public int getCount() {
            if (images == null) return 0;
            return images.size();
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImages() {
            return this.images;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = MainActivity.this;
            SquareView imageView = new SquareView(context);
            //int padding = context.getResources().getDimensionPixelSize(
            //        R.dimen.padding_medium);
            //imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.DisplayImage(FonkelIO.BASE_URL + "media/" + images.get(position), imageView);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((SquareView) object);
        }
    }
}
