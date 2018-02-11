package com.example.bobi.processofthings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bobi on 7.12.2017 г..
 */

public class OneFragment extends Fragment {
    public static String selectedItem = "";
    ArrayAdapter<String> adapter;
    ListView gridView;
    private List<String> contentList = new ArrayList<>();
    private List<String> imagesList = new ArrayList<>();
    private List<String> priceList = new ArrayList<>();
    private String pageTitle = "";

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentList = getArguments().getStringArrayList("data");
        Log.d("contentList", contentList.toString());

        imagesList = getArguments().getStringArrayList("images");
        Log.d("imagesList", imagesList.toString());

        View view = inflater.inflate(R.layout.fragment_one, null);

        gridView = view.findViewById(R.id.gridView);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contentList);


        gridView.setAdapter(adapter);

        //TODO: https://stackoverflow.com/questions/39294103/viewing-an-image-on-a-android-fragment?answertab=votes#tab-top
        //TODO: nov layout s text i kartinki...

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getContext(), selectedItem, duration).show();
                Intent intent = new Intent(getActivity(), ItemViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }
}
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentList = getArguments().getStringArrayList("data");
        String[] contentListStringArray = contentList.toArray(new String[0]);
        Log.d("contentList", contentList.toString());

        imagesList = getArguments().getStringArrayList("images");
        String[] imagesURLsStringArray = imagesList.toArray(new String[0]);
        Log.d("imagesList", imagesList.toString());

        priceList = getArguments().getStringArrayList("prices");
        String[] priceListStringArray = priceList.toArray(new String[0]);
        Log.d("priceList", priceList.toString());

        View view = inflater.inflate(R.layout.fragment_one, null);
        gridView = view.findViewById(R.id.gridView);

        CustomGrid adapter = new CustomGrid(getActivity(), contentListStringArray, imagesURLsStringArray, priceListStringArray);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getContext(), selectedItem, duration).show();
                Intent intent = new Intent(getActivity(), ItemViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }
}