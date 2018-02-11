package com.example.bobi.processofthings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bobi on 23.1.2018 г..
 */

public class ShoppingCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbarShoppingCart;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    public static TextView toolbarShoppingCartTotal;
    private TextView toolbarShoppingCartTitle;
    private Button toolbarBuyButton;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private JSONArray shoppingCartList = new JSONArray();
    JSONObject singleJSONObjectItem = new JSONObject();
    JSONObject jsonObjectTotal = new JSONObject();
    JSONObject jsonObjectPOT = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentList.add(new ShoppingCartFragment());
        setContentView(R.layout.shopping_cart);

        toolbarShoppingCart = (Toolbar) findViewById(R.id.toolbarShoppingCart);
        setSupportActionBar(toolbarShoppingCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarShoppingCartTitle = (TextView) findViewById(R.id.toolbarShoppingCartTitle);
        toolbarShoppingCartTitle.setText("My shopping cart");

        toolbarBuyButton = (Button) findViewById(R.id.cartBuyButton);
        toolbarBuyButton.setText("Buy");

        toolbarBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                if(MainActivity.shoppingCartItemName.isEmpty()) {
                    Toast.makeText(context, "Your shopping cart is empty!", duration).show();
                }
                else {
                    String q = "\"";
                    String c = ", ";

                    String orderID = randomString();

                    for (int position = 0; position < MainActivity.shoppingCartItemName.size(); position++) {
                        //String uniqueOrderID = "{\"menu_" + orderID + "\"" + ": [";
                        String name = "{\"name\":" + q + MainActivity.shoppingCartItemName.get(position) + q + c;
                        String sizeandqty = "\"sizeandqty\":" + q + ItemViewActivity.qtyAndSizeList.get(position) + q + c;
                        String totalprice = "\"sum\":" + q + String.format("%.2f", Double.parseDouble(MainActivity.shoppingCartItemPrice.get(position))) + "lv" + q + "}" + "]" + "}";
                        String itemInfo = "";
                        itemInfo = itemInfo.concat(name).concat(sizeandqty).concat(totalprice);
                        try {
                            singleJSONObjectItem = new JSONObject(itemInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        shoppingCartList.put(singleJSONObjectItem);
                    }
                    try {
                        jsonObjectTotal = new JSONObject("{\"ordertotalprice\":" + q + String.format("%.2f", MainActivity.shoppingCartTotalPrice) + "lv" + q + "}");
                        shoppingCartList.put(jsonObjectTotal);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObjectPOT = new JSONObject();
                        jsonObjectPOT.put("order_" + orderID, shoppingCartList);
                        Log.d("jsonObjectPOT", jsonObjectPOT.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //TODO: POST REQUEST TO POT
                    Log.d("shpclist", shoppingCartList.toString());
                    shoppingCartList = new JSONArray();
                    Log.d("shpclist", shoppingCartList.toString());

                    toolbarBuyButton.setText("Ordered");
                    toolbarBuyButton.setEnabled(false);
                    toolbarBuyButton.setOnClickListener(null);

                    for (int i = 0; i < 2; i++) {
                        Toast.makeText(context, "Processing your order! Thank you for eating at our restaurant! Bon Appétit! ", duration).show();

                    }

                    MainActivity.shoppingCartItemName.clear();
                    MainActivity.shoppingCartImageURL.clear();
                    ItemViewActivity.qtyAndSizeList.clear();
                    MainActivity.shoppingCartItemPrice.clear();
                    MainActivity.shoppingCartTotalPrice = 0.0;

                    ShoppingCartFragment.gridView.setAdapter(ShoppingCartFragment.adapter);
                    viewPager = findViewById(R.id.viewpagerShoppingCart);
                    setupViewPager(viewPager);
                    toolbarShoppingCartTotal.setText("Total price: " + String.format("%.2f", MainActivity.shoppingCartTotalPrice) + "lv");
                    toolbarBuyButton.setBackgroundResource(R.mipmap.check_icon);
                    toolbarBuyButton.setTextColor(getResources().getColor(R.color.DKGreen));
                }
            }
        });

        toolbarShoppingCartTotal = (TextView) findViewById(R.id.toolbarShoppingCartTotal);
        toolbarShoppingCartTotal.setText("Total price: " + String.format("%.2f", MainActivity.shoppingCartTotalPrice) + "lv");

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navview);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        viewPager = findViewById(R.id.viewpagerShoppingCart);
        setupViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.navigationView.getMenu().getItem(0).setChecked(true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            navigationView.bringToFront();
            drawerLayout.requestLayout();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem myitem) {
        MenuItem mi = myitem;
        Log.d("miitem", mi.toString());
        switch (mi.toString()) {
            case "Home":
                Intent intent = new Intent(this, MainActivity.class);
                MainActivity.navigationView.getMenu().getItem(0).setChecked(true);
                startActivity(intent);
                break;
            case "Shopping cart":
                MainActivity.navigationView.getMenu().getItem(1).setChecked(true);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    //ViewPagerAdapter settings
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
    }

    protected String randomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}