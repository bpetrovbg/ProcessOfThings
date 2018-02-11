package com.example.bobi.processofthings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bobi on 7.12.2017 г..
 */

public class ShoppingCartFragment extends Fragment {
    public static String selectedItem = "";
    public static ListView gridView;
    private List<String> contentList = new ArrayList<>();
    private List<String> imagesList = new ArrayList<>();
    private List<String> priceList = new ArrayList<>();
    public static CustomGridShoppingCart adapter = null;

    private String pageTitle = "";

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] contentListStringArray = MainActivity.shoppingCartItemName.toArray(new String[0]);
        String[] itemsPriceStringArray = MainActivity.shoppingCartItemPrice.toArray(new String[0]);
        String[] itemsURLsListStringArray = MainActivity.shoppingCartImageURL.toArray(new String[0]);
        String[] itemsQuantityStringArray = ItemViewActivity.qtyAndSizeList.toArray(new String[0]);

        View view = inflater.inflate(R.layout.fragment_one, null);

        gridView = view.findViewById(R.id.gridView);
        adapter = new CustomGridShoppingCart(getActivity(), contentListStringArray, itemsURLsListStringArray, itemsQuantityStringArray, itemsPriceStringArray);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getContext(), selectedItem, duration).show();
                Intent intent = new Intent(getActivity(), ItemViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        return view;
    }
}