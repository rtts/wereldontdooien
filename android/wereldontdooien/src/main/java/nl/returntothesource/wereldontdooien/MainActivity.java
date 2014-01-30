package nl.returntothesource.wereldontdooien;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

        /*
        SquareView squareView = (SquareView) findViewById(R.id.dummy_fonkel);
        Bitmap b = BitmapFactory.decodeResource(MainActivity.this.getResources(),
                R.drawable.dummyfonkel);
        squareView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(b));
        */
        // Download the fonkels
        new DownloadFonkelsTask().execute();
        // Set the alarm for the daily notification, if this is not yet done
        AlarmReceiver.setAlarm(MainActivity.this);
    }

    private class DownloadFonkelsTask extends AsyncTask<Void, Void, List<Fonkel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                findViewById(R.id.pager).setVisibility(View.GONE);
                findViewById(R.id.error_bar).setVisibility(View.GONE);
                //findViewById(R.id.dummy_fonkel).setVisibility(View.VISIBLE);
                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            } else {
                this.cancel(false);
            }
        }

        protected List<Fonkel> doInBackground(Void... voids) {
            List<Fonkel> fonkels = FonkelIO.readFonkelsFromApi();
            if (fonkels != null && fonkels.size() > 0) {
                FonkelIO.writeFonkelsToDisk(MainActivity.this, fonkels);
            } else {
                this.cancel(false);
            }
            return fonkels;
        }

        protected void onPostExecute(List<Fonkel> result) {
            if (result != null && result.size() > 0) {
                //findViewById(R.id.dummy_fonkel).setVisibility(View.GONE);
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();
                adapter.setImages(result);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(result.size()-1, false);
                viewPager.setVisibility(View.VISIBLE);
            } else {
                this.cancel(false);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            List<Fonkel> fonkels = FonkelIO.readFonkelsFromDisk(MainActivity.this);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();
            adapter.setImages(fonkels);
            adapter.notifyDataSetChanged();
            if(fonkels != null && fonkels.size() > 0) {
                viewPager.setCurrentItem(fonkels.size() - 1, false);
                viewPager.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.error_bar).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.action_random:
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                int count = viewPager.getAdapter().getCount();
                int currentItem = viewPager.getCurrentItem();
                Integer randomNr = currentItem;
                if (count > 1) {
                    while (randomNr == currentItem) {
                        randomNr = new Random().nextInt(count);
                    }
                }
                viewPager.setCurrentItem(randomNr, true);
                return true;
            case R.id.action_refresh:
                new DownloadFonkelsTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {
        ImageLoader imageLoader;
        private List<Fonkel> images;
        private Map<Integer, List<Fonkel>> fonkelsByCategory;

        public ImagePagerAdapter() {
            imageLoader = new ImageLoader(MainActivity.this);
            fonkelsByCategory = new HashMap<Integer, List<Fonkel>>();
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

        public void setImages(List<Fonkel> images) {
            this.images = images;
            for (Fonkel f : images) {
                List<Fonkel> fonkels = fonkelsByCategory.get(f.type);
                if (fonkels == null) fonkels = new ArrayList<Fonkel>();
                fonkels.add(f);
            }
        }

        public Fonkel getRandom(int type, Fonkel currentFonkel) {
            List<Fonkel> fonkelsToChoose;
            if (type == 0) {
                fonkelsToChoose = images;
            } else {
                fonkelsToChoose = fonkelsByCategory.get(type);
            }
            int count = images.size();
            Fonkel randomFonkel = null;
            if (count > 1) {
                do {
                    randomFonkel = images.get(new Random().nextInt(count));
                } while (randomFonkel == currentFonkel);
            }
            return randomFonkel;
        }

        public List<Fonkel> getImages() {
            return this.images;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = MainActivity.this;
            SquareView imageView = new SquareView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.DisplayImage(FonkelIO.BASE_URL + "media/" + images.get(position).afbeelding, imageView);
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((SquareView) object);
        }
    }
}
