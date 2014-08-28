package nl.returntothesource.wereldontdooien.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
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

import nl.returntothesource.wereldontdooien.R;
import nl.returntothesource.wereldontdooien.io.Fonkel;
import nl.returntothesource.wereldontdooien.io.FonkelIO;
import nl.returntothesource.wereldontdooien.receiver.AlarmReceiver;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: remove! only for testing.
//        FonkelIO.deleteFonkelsFromDisk(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(12);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        // Set the alarm for the daily notification, if this is not yet done
        AlarmReceiver.setAlarm(MainActivity.this);
        new DownloadFonkelsTask().execute();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.rootview));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            } ((ViewGroup) view).removeAllViews();
        }
    }

    public class DownloadFonkelsTask extends AsyncTask<Void, Void, List<Fonkel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                findViewById(R.id.pager).setVisibility(View.INVISIBLE);
                findViewById(R.id.error_bar).setVisibility(View.INVISIBLE);
                findViewById(R.id.error_message).setVisibility(View.GONE);
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
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();

                adapter.setImages(result);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(result.size() - 1, false);
                viewPager.setVisibility(View.VISIBLE);
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            } else {
                this.cancel(false);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(showFromDisk()) {
                findViewById(R.id.error_message).setVisibility(View.GONE);
            } else {
                findViewById(R.id.error_message).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            findViewById(R.id.error_bar).setVisibility(View.VISIBLE);
        }
    }

    private boolean showFromDisk() {
        List<Fonkel> fonkels = FonkelIO.readFonkelsFromDisk(MainActivity.this);
        if (fonkels != null && fonkels.size() > 0) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();
            adapter.setImages(fonkels);
            adapter.notifyDataSetChanged();
            viewPager.setCurrentItem(fonkels.size() - 1, false);
            viewPager.setVisibility(View.VISIBLE);
            return true;
        } else {
            return false;
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
                ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();
                if (adapter != null && adapter.getImages() != null && adapter.getImages().size() > 0) {
                    Fonkel currentFonkel = adapter.getImages().get(viewPager.getCurrentItem());
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                    int type = Integer.valueOf(pref.getString("surprise_category", "0"));
                    int newFonkel = adapter.getRandom(type, currentFonkel);
                    if (newFonkel != -1) {
                        viewPager.setCurrentItem(newFonkel, true);
                    }
                    return true;
                } else {
                    showNoFonkelsDialog();
                    return false;
                }
            case R.id.action_refresh:
                new DownloadFonkelsTask().execute();
                return true;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
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
                if (fonkels == null) {
                    fonkels = new ArrayList<Fonkel>();
                    fonkelsByCategory.put(f.type, fonkels);
                }
                fonkels.add(f);
            }
        }

        public Integer getRandom(int type, Fonkel currentFonkel) {
            List<Fonkel> fonkelsToChoose;
            if (type == 0) {
                fonkelsToChoose = images;
            } else {
                fonkelsToChoose = fonkelsByCategory.get(type);
            }
            if (fonkelsToChoose != null && fonkelsToChoose.size() > 0) {
                int count = fonkelsToChoose.size();
                Fonkel randomFonkel = fonkelsToChoose.get(0);
                if (count > 1) {
                    do {
                        randomFonkel = fonkelsToChoose.get(new Random().nextInt(count));
                    } while (randomFonkel == currentFonkel);
                }
                return images.indexOf(randomFonkel);
            } else {
                showNoFonkelsInCategoryDialog();
            }
            return images.indexOf(currentFonkel);
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
//            imageLoader.DisplayImage("http://www.returntothesource.nl/" + images.get(position).afbeelding, imageView);

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((SquareView) object);
        }
    }

    private void showNoFonkelsDialog() {
        DialogFragment newFragment = new NoFonkelsDialogFragment();
        newFragment.show(getSupportFragmentManager(), "nofonkels");
    }
    private void showNoFonkelsInCategoryDialog() {
        DialogFragment newFragment = new NoFonkelsInCategoryDialogFragment();
        newFragment.show(getSupportFragmentManager(), "nofonkelsincategory");
    }

    @SuppressLint("ValidFragment")
    public class NoFonkelsDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_no_fonkels)
                    .setPositiveButton(R.string.nu_ophalen, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new DownloadFonkelsTask().execute();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @SuppressLint("ValidFragment")
    public class NoFonkelsInCategoryDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_no_fonkels_in_category)
                    .setPositiveButton(R.string.naar_instellingen, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                            NoFonkelsInCategoryDialogFragment.this.startActivity(i);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
