package com.example.bobi.processofthings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Bobi on 18.1.2018 г..
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private String[] contentList;
    private String[] imageURL;
    private String[] contentPrice;
    private String ipfsHttps = "https://ipfs.io/ipfs/";
    private ViewHolder viewHolder;

    public CustomGrid(Context c, String[] contentList, String[] imageURL, String[] contentPrice) {
        mContext = c;
        this.contentList = contentList;
        this.imageURL = imageURL;
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
            grid = inflater.inflate(R.layout.gridview_cell, null, true);
            viewHolder.nameProduct = (TextView) grid.findViewById(R.id.cellTextView);
            viewHolder.nameProduct.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
            viewHolder.nameProduct.setTypeface(null, Typeface.BOLD_ITALIC);
            viewHolder.nameProduct.setTextColor(Color.parseColor("#BE9B7B"));
            viewHolder.imageProduct = (ImageView) grid.findViewById(R.id.cellImage);
            viewHolder.productPrice = (TextView) grid.findViewById(R.id.cellPrice);
            viewHolder.productPrice.setTypeface(null, Typeface.BOLD);
            viewHolder.productPrice.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
            viewHolder.productPrice.setTextColor(Color.parseColor("#198119"));

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
        viewHolder.productPrice.setText("Price: " + contentPrice[position]);
        viewHolder.productPrice.setTypeface(null, Typeface.BOLD);
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
