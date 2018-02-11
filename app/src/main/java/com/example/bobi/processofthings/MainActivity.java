package com.example.bobi.processofthings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static JSONObject menuitems = null;
    public static String test;
    public static String tabName;
    public static NavigationView navigationView;
    public static List<String> shoppingCartItemName = new ArrayList<>();
    public static List<String> shoppingCartItemPrice = new ArrayList<>();
    public static List<String> shoppingCartImageURL = new ArrayList<>();
    public static List<String> shoppingCartQuantity = new ArrayList<>();
    public static int sCorner = 30;
    public static int sMargin = 2;
    public static double shoppingCartTotalPrice = 0.0;
    //public static List<String> shoppingCartItems = new ArrayList<>();
    //public static JSONArray shoppingCartList = new JSONArray();
    //Title, Soups, Alcohol, Salads Lists
    public final List<String> mFragmentTitleList = new ArrayList<>();
    public final List<String> soupsList = new ArrayList<>();
    public final List<String> alcDrinksList = new ArrayList<>();
    public final List<String> saladsList = new ArrayList<>();
    //Fragment List
    private final List<Fragment> mFragmentList = new ArrayList<>();
    String myData = "";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressBar spinner;
    private ViewPagerAdapter adapter;

    //Loading the JSON data, which will be used to create dynamically content such as tabs and info for the Fragments
    private void loaddata() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =
                "https://ipfs.io/ipfs/QmQ69ScwoqSsFAdGV4oUKNaDMzSDuY9zcdRoFYkHPUZM7E";
        String line = "";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myData = response;
                JSONArray nameArray = null;
                JSONArray imageURLArray = null;
                JSONArray priceArray = null;


                /*JSONArray soupsArray = null;
                JSONArray alcDrinksArray = null;
                JSONArray saladsArray = null;
                //Log.d("response", myData);
                */
                JSONObject object = null;
                try {
                    object = new JSONObject(myData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONObject menuitems = null;
                try {
                    menuitems = object.getJSONObject("menuitems");
                    Log.d("menuitems", menuitems.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                try {
                    alcDrinksArray = menuitems.getJSONArray("Alcohol Drinks");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < alcDrinksArray.length(); i++) {
                    try {
                        alcDrinksList.add(String.valueOf(alcDrinksArray.getJSONObject(i).get("name")));
                        Log.d("alcDrinksList", alcDrinksList.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    soupsArray = menuitems.getJSONArray("Soups");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < soupsArray.length(); i++) {
                    try {
                        soupsList.add(String.valueOf(soupsArray.getJSONObject(i).get("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                        saladsArray = menuitems.getJSONArray("Salads");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < saladsArray.length(); i++) {
                    try {
                        saladsList.add(String.valueOf(saladsArray.getJSONObject(i).get("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                */

                for (int i = 0; i < menuitems.length(); i++) {
                    try {
                        mFragmentTitleList.add(menuitems.names().getString(i));

                        final List<String> randomList = new ArrayList<>();
                        nameArray = (JSONArray) menuitems.get(menuitems.names().getString(i));
                        for (int j = 0; j < nameArray.length(); j++) {
                            randomList.add(String.valueOf(nameArray.getJSONObject(j).get("name")));
                        }

                        final List<String> imagesURL = new ArrayList<>();
                        imageURLArray = (JSONArray) menuitems.get(menuitems.names().getString(i));
                        for (int k = 0; k < imageURLArray.length(); k++) {
                            imagesURL.add(String.valueOf(imageURLArray.getJSONObject(k).get("image")));
                        }

                        final List<String> priceList = new ArrayList<>();
                        priceArray = (JSONArray) menuitems.get(menuitems.names().getString(i));
                        for (int m = 0; m < priceArray.length(); m++) {
                            priceList.add(String.valueOf(priceArray.getJSONObject(m).get("price")));
                        }



                        //sends List with contents to the Fragment
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("data", (ArrayList<String>) randomList);

                        //send List with images URLS to the Fragment
                        bundle.putStringArrayList("images", (ArrayList<String>) imagesURL);

                        //send List with prices to the Fragment
                        bundle.putStringArrayList("prices", (ArrayList<String>) priceList);

                        mFragmentList.add(new OneFragment());
                        mFragmentList.get(i).setArguments(bundle);
                        //mFragmentTitleList gives output [Alcohol Drinks, Soups, Salads]
                        Log.d("test", String.valueOf(mFragmentTitleList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "Key = " + menuitems.names().getString(i) + " value = " + menuitems.get(menuitems.names().getString(i)));
                }

                spinner.setVisibility(View.GONE);

                setupViewPager(viewPager);
                //setupGridView(gridView);
                tabLayout.setupWithViewPager(viewPager);
                // Tab ViewPager setting
                viewPager.setOffscreenPageLimit(mFragmentList.size());
                tabLayout.setupWithViewPager(viewPager);
                try {
                    tabName = menuitems.names().getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tabName = tab.getText().toString();
                        Log.d("ontab", tabName);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, "Service not available!", duration).show();
            }
        });
        queue.add(stringRequest);
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mFragmentTitleList);
        viewPager.setAdapter(adapter);
    }

    //function for the toolbar button to open the navdrawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            navigationView.bringToFront();
            drawerLayout.requestLayout();
        }
        return true;
    }

    //function for the app drawer category highlighting when tapped
    @Override
    public boolean onNavigationItemSelected(MenuItem myitem) {
        MenuItem mi = myitem;
        Log.d("sklonnakashon", mi.toString());
        switch (mi.toString()) {
            case "Home":
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "Shopping cart":
                Intent intent1 = new Intent(this, ShoppingCartActivity.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loaddata();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.progressbar);
        spinner.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navview);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
    }

    //ViewPagerAdapter settings
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleLists) {
            super(fm);
            this.mFragmentList = fragments;
            this.mFragmentTitleList = titleLists;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}