package com.example.bobi.processofthings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static com.example.bobi.processofthings.ShoppingCartFragment.adapter;
import static com.example.bobi.processofthings.ShoppingCartFragment.gridView;

/**
 * Created by Bobi on 18.1.2018 г..
 */

public class CustomGridShoppingCart extends BaseAdapter {
    private Context mContext;
    private String[] contentList;
    private String[] imageURL;
    private String[] quantityList;
    private String[] contentPrice;
    private String ipfsHttps = "https://ipfs.io/ipfs/";
    private ViewHolder viewHolder;

    public CustomGridShoppingCart(Context c, String[] contentList, String[] imageURL, String[] quantityList, String[] contentPrice) {
        mContext = c;
        this.contentList = contentList;
        this.imageURL = imageURL;
        this.quantityList = quantityList;
        this.contentPrice = contentPrice;
    }

    @Override
    public int getCount() {
        return contentList.length;
    }

    @Override
    public Object getItem(int position) {
        return contentList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView nameProduct;
        ImageView imageProduct;
        TextView productPrice;
        Button removeButton;
        TextView quantityProduct;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid = convertView;
        viewHolder = new ViewHolder();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.shopping_cart_cell, null, true);
            viewHolder.nameProduct = (TextView) grid.findViewById(R.id.cartName);
            viewHolder.nameProduct.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
            viewHolder.nameProduct.setTypeface(null, Typeface.BOLD_ITALIC);
            viewHolder.nameProduct.setTextColor(Color.parseColor("#BE9B7B"));
            viewHolder.removeButton = (Button) grid.findViewById(R.id.cartRemoveButton);
            viewHolder.removeButton.setTag(position);
            viewHolder.imageProduct = (ImageView) grid.findViewById(R.id.cartImage);
            viewHolder.quantityProduct = (TextView) grid.findViewById(R.id.cartQuantity);
            viewHolder.productPrice = (TextView) grid.findViewById(R.id.cartPrice);

            //viewHolder.productPrice = (TextView) grid.findViewById(R.id.cartPrice);
            //viewHolder.productPrice.setTypeface(null, Typeface.BOLD);
            //viewHolder.productPrice.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
            //viewHolder.productPrice.setTextColor(Color.parseColor("#198119"));

            viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    Log.d("butonchetoid", String.valueOf(position));
                    MainActivity.shoppingCartItemName.remove(position);
                    MainActivity.shoppingCartImageURL.remove(position);
                    ItemViewActivity.qtyAndSizeList.remove(position);
                    MainActivity.shoppingCartTotalPrice -= Double.parseDouble(MainActivity.shoppingCartItemPrice.get(position));
                    ShoppingCartActivity.toolbarShoppingCartTotal.setText("Total price: " + String.format("%.2f", MainActivity.shoppingCartTotalPrice) + "lv");
                    MainActivity.shoppingCartItemPrice.remove(position);

                    String[] contentListStringArray = MainActivity.shoppingCartItemName.toArray(new String[0]);
                    String[] itemsURLsListStringArray = MainActivity.shoppingCartImageURL.toArray(new String[0]);
                    String[] itemsQuantityStringArray = ItemViewActivity.qtyAndSizeList.toArray(new String[0]);
                    String[] itemsPriceStringArray = MainActivity.shoppingCartItemPrice.toArray(new String[0]);
                    adapter = new CustomGridShoppingCart(mContext, contentListStringArray, itemsURLsListStringArray, itemsQuantityStringArray, itemsPriceStringArray);
                    gridView.setAdapter(adapter);
                    Log.d("listche", MainActivity.shoppingCartItemName.toString());
                }
            });

            try {
                String url = ipfsHttps + imageURL[position];
                Glide.with(mContext).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).into(viewHolder.imageProduct);
            } catch (Exception e) {
                String defaultImage = "http://www.simplydeliciousbynancy.com/wp-content/uploads/2015/03/SimplyDeliciousLogoDesyrel.png";
                Glide.with(mContext).load(defaultImage).into(viewHolder.imageProduct);
            }
            grid.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) grid.getTag();
        }

        viewHolder.nameProduct.setText(contentList[position]);
        try {
            viewHolder.quantityProduct.setText("Qty: " + quantityList[position]);
        }catch (Exception e) {}
        try {
            String formatedTotalPrice = String.format("%.2f", Double.parseDouble(contentPrice[position]));
            viewHolder.productPrice.setText("Total: " + formatedTotalPrice + "lv");
            //viewHolder.productPrice.setText("Price: " + contentPrice[position]);
            viewHolder.productPrice.setTypeface(null, Typeface.BOLD);
        }catch (Exception e) {}
        try {
            String url = ipfsHttps + imageURL[position];
            Glide.with(mContext).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(viewHolder.imageProduct);
        } catch (Exception e) {
            String defaultImage = "http://www.simplydeliciousbynancy.com/wp-content/uploads/2015/03/SimplyDeliciousLogoDesyrel.png";
            Glide.with(mContext).load(defaultImage).into(viewHolder.imageProduct);
        }
        return grid;
    }
}
