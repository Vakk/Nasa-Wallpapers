package com.vakk.nasaapod;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vakk.nasaapod.fragments.PageFragment;
import com.vakk.nasaapod.helpers.Image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vakk on 3/17/16.
 */
public class FullscreenActivity extends FragmentActivity {
    List<Image> images;
    int currentPosition;
    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_fullscreen);
        Intent intent = getIntent();
        // get our images
        images = (ArrayList<Image>) intent.getSerializableExtra("images");
        int position = intent.getIntExtra("position", -1);
        // Page viewer
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        if (position != -1) {
            pager.setCurrentItem(position);
        }
    }

    /**
     * PAGE VIEWER
     */
    /**
     * pages adapter... setup items and size of array
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // create new fragment
            return PageFragment.newInstance(images.get(position));
        }

        /**
         * get size of pageViewer
         *
         * @return return current size
         */
        @Override
        public int getCount() {
            return images.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.set_wallpaper) {
            // TODO: 3/21/16 need string resources
            Toast.makeText(this, "Start download and setup wallpaper...", Toast.LENGTH_LONG).show();
            setWallpaper();
            return true;
        }
        if (id == R.id.show_details) {
            Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
            intent.putExtra("image",images.get(currentPosition));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * set current image as wallpaper, uses list "images" and present image position in list
     */
    void setWallpaper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpaperManager wpm = WallpaperManager.getInstance(getApplicationContext());
                try {

                    URI uri = URI.create(images.get(currentPosition).getUrl());
                    URL url = uri.toURL();
                    wpm.setStream(url.openStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
