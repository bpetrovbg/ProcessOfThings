package com.example.bobi.processofthings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bobi on 18.12.2017 г..
 */

public class ItemViewActivity extends AppCompatActivity {
    public static List<String> qtyAndSizeList = new ArrayList<>();
    static double sumFromSpinners = 0.0;
    static double sumFromEditTexts = 0.0;
    static int quantityFromSpinners = 0;
    static int quantityFromEdittexts = 0;
    private Toolbar toolbar;
    private TextView itemName;
    private TextView itemDescription;
    private TextView totalPrice;
    private ImageView itemImage;
    private String ipfsHttps = "https://ipfs.io/ipfs/";
    private ProgressBar imageSpinner;
    private String ipfsImage = "";
    private String tabName = "";
    private JSONObject categoriesObject = null;
    private JSONArray myArray = null;
    private int finalQuantity1;
    private int finalQuantity2;
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private String str = "";
    private String str2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);

        imageSpinner = findViewById(R.id.imagespinner);
        imageSpinner.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText("0.00lv");
        itemImage = findViewById(R.id.itemImage);

        final Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String itemNameString = (String) itemName.getText();
                String itemPriceString = (String) totalPrice.getText();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                switch (itemPriceString) {
                    case "0.00lv":
                    case "0,00lv":
                        Toast.makeText(context, "Cannot add 0 items", duration).show();
                        break;
                    default:
                        int finalQuantity3 = finalQuantity1 + finalQuantity2;
                        MainActivity.shoppingCartItemName.add(itemNameString);
                        MainActivity.shoppingCartImageURL.add(ipfsImage);
                        MainActivity.shoppingCartQuantity.add(String.valueOf(finalQuantity3));
                        MainActivity.shoppingCartItemPrice.add(String.valueOf(sumFromEditTexts + sumFromSpinners));
                        MainActivity.shoppingCartTotalPrice += Double.parseDouble(String.valueOf(sumFromEditTexts + sumFromSpinners));

                        if (str.isEmpty()) {
                            qtyAndSizeList.add(str2);
                        }
                        if (str2.isEmpty()) {
                            qtyAndSizeList.add(str);
                        }
                        if (str.isEmpty() == false && str2.isEmpty() == false) {
                            qtyAndSizeList.add(str + ", " + str2);
                        }

                        Log.d("currentSizewe", qtyAndSizeList.toString());

                        Toast.makeText(context, itemName.getText() + " added", duration).show();
                        addToCartButton.setText("Added! ✓ ✓ ✓ ");
                        addToCartButton.setBackgroundColor(getResources().getColor(R.color.DKGreen));
                        addToCartButton.setEnabled(false);
                        addToCartButton.setOnClickListener(null);
                }
            }
        });

        tabName = MainActivity.tabName;
        categoriesObject = MainActivity.menuitems;
        //Log.d("categoriesObject", categoriesObject.toString());
        for (int i = 0; i < categoriesObject.length(); i++) {
            try {
                if (tabName == categoriesObject.names().getString(i)) {
                    myArray = categoriesObject.getJSONArray(tabName);

                    for (int j = 0; j < myArray.length(); j++) {
                        if (OneFragment.selectedItem == myArray.getJSONObject(j).get("name")) {
                            try {
                                itemName.setText(OneFragment.selectedItem);
                                itemName.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
                                itemName.setTextColor(getResources().getColor(R.color.White));
                            } catch (Exception e) {
                                itemName.setText("No name available");
                            }
                            try {
                                itemDescription.setText((CharSequence) myArray.getJSONObject(j).get("description"));
                                itemDescription.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                                itemDescription.setTextColor(getResources().getColor(R.color.White));
                            } catch (Exception e) {
                                itemDescription.setText("No description available");
                            }
                            try {
                                String sizes = (String) myArray.getJSONObject(j).get("size");
                                final String[] differentSizes = sizes.split(",");
                                String prices = (String) myArray.getJSONObject(j).get("price");
                                String[] differentPrices = prices.split(",");

                                final LinearLayout sizeAndPriceGroup = findViewById(R.id.sizeAndPriceGroup);
                                final LinearLayout quantityGroup = findViewById(R.id.quantityGroup);
                                final LinearLayout moreQuantityGroup = findViewById(R.id.moreQuantityGroup);

                                final ArrayList<Spinner> listSpinners = new ArrayList<>();
                                final ArrayList<TextView> textViews = new ArrayList<>();
                                final ArrayList<EditText> editTexts = new ArrayList<>();
                                totalPrice.setText("0.00lv");

                                for (int m = 0; m < differentSizes.length; m++) {
                                    TextView textView = new TextView(this);
                                    final Spinner spinner = new Spinner(this);

                                    spinner.setMinimumHeight(85);
                                    spinner.setMinimumWidth(230);
                                    spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    spinner.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                    spinner.setId(m);
                                    spinner.setBackgroundResource(R.drawable.spinner_border);
                                    textViews.add(textView);

                                    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                            this, R.array.item_number, R.layout.spinner_item);
                                    adapter.setDropDownViewResource(R.layout.my_spinner_layout);
                                    spinner.setAdapter(adapter);

                                    final EditText editText = new EditText(this);
                                    editText.setEms(3);
                                    editText.setMinimumHeight(5);
                                    editText.setId(m);
                                    editText.setPadding(0, 0, 0, 10);
                                    Log.d("etid", String.valueOf(editText.getId()));
                                    editText.setVisibility(View.INVISIBLE);
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Object itemSel = parent.getItemAtPosition(position);
                                            int itemSelId = parent.getId();

                                            sumFromSpinners = 0.0;
                                            quantityFromSpinners = 0;
                                            list.clear();

                                            if (itemSel.toString().equals("More")) {
                                                spinner.findViewById(itemSelId);
                                                spinner.setVisibility(View.INVISIBLE);
                                                spinner.setSelection(0);
                                                editText.findViewById(itemSelId);
                                                editText.setVisibility(View.VISIBLE);
                                                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                inputManager.showSoftInput(editText.findViewById(itemSelId), InputMethodManager.SHOW_IMPLICIT);

                                            } else {
                                                for (int i = 0; i < listSpinners.size(); i++) {
                                                    //listSpinners.get(a).getSelectedItem().toString()); //spinners value
                                                    if (!(listSpinners.get(i).getSelectedItem().toString().equals("More"))) {
                                                        //Log.d("ceni4kata", textViews.get(i).getText().toString());
                                                        String[] splitCurrentPrice = (textViews.get(i).getText().toString().substring(0, textViews.get(i).getText().toString().length() - 2)).split(", ");
                                                        String currentPrice = splitCurrentPrice[1];
                                                        double priceFromSpinner = (Double.parseDouble(currentPrice)) * (Double.parseDouble(listSpinners.get(i).getSelectedItem().toString()));
                                                        int qtyFromSpinner = Integer.parseInt(listSpinners.get(i).getSelectedItem().toString());
                                                        if (priceFromSpinner != 0) {
                                                            String currentSize = splitCurrentPrice[0];
                                                            list.add(currentSize + " " + "(" + String.valueOf(qtyFromSpinner) + ")" + ", ");
                                                            String[] str1 = list.toArray(new String[0]);
                                                            StringBuilder builder = new StringBuilder();
                                                            for (String s : str1) {
                                                                builder.append(s);
                                                            }
                                                            str = builder.substring(0, builder.toString().length() - 2).toString();
                                                        }
                                                        sumFromSpinners += priceFromSpinner;
                                                        quantityFromSpinners += qtyFromSpinner;
                                                    }
                                                }
                                                double finalPrice = sumFromSpinners + sumFromEditTexts;
                                                // int finalQuantity = quantityFromSpinners + quantityFromEdittexts;
                                                finalQuantity1 = quantityFromSpinners + quantityFromEdittexts;
                                                String formatedFinalPrice = String.format("%.2f", finalPrice);
                                                totalPrice.setText(formatedFinalPrice + "lv");
                                            }
                                        }

                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });

                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            sumFromEditTexts = 0.0;
                                            str2 = "";
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            if (editText.getText().toString().trim().isEmpty()) {
                                                int editTextID = editText.getId();
                                                spinner.findViewById(editTextID);
                                                spinner.setVisibility(View.VISIBLE);
                                                editText.findViewById(editTextID);
                                                editText.setVisibility(View.INVISIBLE);
                                                spinner.setSelection(0);
                                                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable editable) {
                                            list2.clear();
                                            if (editText.getText().toString().trim().isEmpty()) {
                                                int editTextID = editText.getId();
                                                spinner.findViewById(editTextID);
                                                spinner.setVisibility(View.VISIBLE);
                                                editText.findViewById(editTextID);
                                                editText.setVisibility(View.INVISIBLE);
                                                spinner.setSelection(0);
                                                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                            }
                                            for (int i = 0; i < editTexts.size(); i++) {
                                                //editTexts.get(i).getText().toString()); edittext value
                                                if (!(editTexts.get(i).getText().toString().isEmpty())) {
                                                    String[] splitCurrentPrice = (textViews.get(i).getText().toString().substring(0, textViews.get(i).getText().toString().length() - 2)).split(", ");
                                                    String currentPrice = splitCurrentPrice[1];
                                                    double priceFromEditText = Double.parseDouble(currentPrice) * Double.parseDouble(editTexts.get(i).getText().toString());
                                                    int qtyFromEditText = Integer.parseInt(editTexts.get(i).getText().toString());
                                                    if (priceFromEditText != 0.00) {
                                                        String currentSize = splitCurrentPrice[0];
                                                        list2.add(currentSize + " " + "(" + String.valueOf(qtyFromEditText) + ")" + ", ");
                                                        String[] str1 = list2.toArray(new String[0]);
                                                        StringBuilder builder = new StringBuilder();
                                                        for (String s : str1) {
                                                            builder.append(s);
                                                        }
                                                        str2 = builder.substring(0, builder.toString().length() - 2).toString();
                                                        Log.d("str2", str2);
                                                    }
                                                    sumFromEditTexts += priceFromEditText;
                                                    quantityFromEdittexts += qtyFromEditText;
                                                }
                                            }
                                            //Log.d("sumaspineri", String.valueOf(sumFromSpinners));
                                            editText.setGravity(Gravity.CENTER_HORIZONTAL);
                                            double finalPrice = sumFromEditTexts + sumFromSpinners;
                                            //int finalQuantity = quantityFromEdittexts + quantityFromSpinners;
                                            //Log.d("quantityspineri", String.valueOf(finalQuantity));
                                            finalQuantity2 = quantityFromEdittexts + quantityFromSpinners;
                                            String formatedFinalPrice = String.format("%.2f", finalPrice);
                                            totalPrice.setText(formatedFinalPrice + "lv");
                                            editText.clearFocus();
                                            editText.setFocusable(true);
                                        }
                                    });

                                    textView.setId(m);
                                    textView.setText(differentSizes[m] + ", " + differentPrices[m]);
                                    textView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                                    textView.setTextColor(getResources().getColor(R.color.White));
                                    textView.setTypeface(null, Typeface.ITALIC);
                                    textView.setMinHeight(75);
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                    sizeAndPriceGroup.addView(textView);
                                    quantityGroup.addView(spinner);
                                    moreQuantityGroup.addView(editText);
                                    listSpinners.add(spinner);
                                    editTexts.add(editText);
                                }

                            } catch (Exception e) {
                                LinearLayout nosizeorprice = findViewById(R.id.nosizeorprice);
                                TextView tv = new TextView(this);
                                tv.setText("No size or price available");
                                tv.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                                nosizeorprice.addView(tv);
                                nosizeorprice.setVisibility(View.VISIBLE);
                            }

                            try {
                                ipfsImage = (String) myArray.getJSONObject(j).get("image");
                                String url = ipfsHttps + ipfsImage;
                                //Glide.with(this).load(url).into(itemImage);
                                Glide.with(this).load(url).bitmapTransform(new GlideRoundedCornersTransformation(this, MainActivity.sCorner, MainActivity.sMargin)).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        imageSpinner.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        imageSpinner.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(itemImage);
                            } catch (Exception e) {
                                String defaultImage = "http://www.simplydeliciousbynancy.com/wp-content/uploads/2015/03/SimplyDeliciousLogoDesyrel.png";
                                Glide.with(this).load(defaultImage).into(itemImage);
                                imageSpinner.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sumFromSpinners = 0.0;
        sumFromEditTexts = 0.0;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sumFromSpinners = 0.0;
                sumFromEditTexts = 0.0;
                Intent intent = new Intent(ItemViewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}