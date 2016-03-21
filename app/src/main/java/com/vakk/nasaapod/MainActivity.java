package com.vakk.nasaapod;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.vakk.nasaapod.adapters.MenuAdapter;
import com.vakk.nasaapod.fragments.CurrentDayFragment;
import com.vakk.nasaapod.fragments.ShowByRangeFragment;
import com.vakk.nasaapod.helpers.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.ResponseListener;
import api.retrofit.nasa.NasaQuery;

public class MainActivity extends AppCompatActivity {
    final int START_PAGE =0;
    final int BY_DAY =1;
    final int BY_RANGE=2;
    final int EXIT=3;

    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle myDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setMenu();
        setNewFrame(new CurrentDayFragment());
    }
    /**
     * Set menu, fill list items
     */
    void setMenu(){
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayAdapter<String> adapter = new MenuAdapter(this,getResources().getStringArray(R.array.menu_items));
        myDrawerList.setAdapter(adapter);
        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                R.string.open_menu,
                R.string.close_menu
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("LOL");
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("HM");
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        setMyDrawerLayoutOnItemClickListener();
    }

    /**
     * Get checked item id
     */
    void setMyDrawerLayoutOnItemClickListener(){
        myDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case (BY_DAY): {
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO: 3/21/16 bug in date picker, month is more than chosen month by 1
                                monthOfYear = monthOfYear+1;
                                showDay(year, monthOfYear, dayOfMonth);
                            }
                        };
                        DatePickerFragment date = new DatePickerFragment();
                        date.setListener(listener);
                        date.show(getSupportFragmentManager(), "datePicker");
                        break;
                    }
                    case (BY_RANGE): {
                        setNewFrame(new ShowByRangeFragment());
                        break;
                    }
                    case (START_PAGE): {
                        setNewFrame(new CurrentDayFragment());
                        break;
                    }
                    case (EXIT): {
                        finish();
                    }

                }
                myDrawerLayout.closeDrawer(myDrawerList);
            }
        });
    }

    /**
     * show image for chosen day
     * @param year chosen year
     * @param month
     * @param day
     */
    void showDay (int year,int month, int day){
        StringBuilder date = new StringBuilder(Integer.toString(year));
                date.append("-");
        date.append(Integer.toString(month));
        date.append("-");
        date.append(Integer.toString(day));
        if (!checkDateValues(date.toString())){
            if (!checkDateFormat(date.toString())){
                Toast.makeText(getApplicationContext(),"Date must be like 2000-01-01",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong date, try one more time", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        getImagePerDate(date.toString());
    }

    /**
     * @param date current day
     * @return true if date like 2000-01-01
     */
    public static boolean checkDateFormat(String date){
        Pattern p = Pattern.compile("^[\\d]{4}-[\\d]{2}-[\\d]{2}");
        Matcher m = p.matcher(date);
        return m.matches();
    }
    /**
     *
     * @param date date
     * @return true if 1970<date<2016
     */
    public static boolean checkDateValues(String date){
        String [] dates = date.split("-");
        if (Integer.parseInt(dates[0])<1970||Integer.parseInt(dates[0])>2016){
            return false;
        }
        if (Integer.parseInt(dates[1])<1||Integer.parseInt(dates[1])>12){
            return false;
        }
        if (Integer.parseInt(dates[2])<1||Integer.parseInt(dates[2])>31){
            return false;
        }
        return true;
    }

    public ActionBarDrawerToggle getMyDrawerToggle() {
        return myDrawerToggle;
    }

    void getImagePerDate(String date){
        NasaQuery.getInstance().getImageByDay(new ResponseListener() {
            @Override
            public void done(Object obj) {
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                List<Image> list = new ArrayList<Image>();
                list.add((Image) obj);
                intent.putExtra("images", (Serializable) list);
                startActivity(intent);
            }

            @Override
            public void fail(Object obj) {
                Toast.makeText(getApplicationContext(), obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }, date);
    }

    /**
     *
     * @param fragment chosen fragment
     */
    void setNewFrame(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction =fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * pick date for show
     */
    public static class DatePickerFragment extends DialogFragment{
        private DatePickerDialog.OnDateSetListener listener;
        public void setListener(DatePickerDialog.OnDateSetListener listener){
            this.listener=listener;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}