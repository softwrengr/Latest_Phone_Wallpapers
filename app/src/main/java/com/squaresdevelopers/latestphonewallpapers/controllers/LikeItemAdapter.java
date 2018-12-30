package com.squaresdevelopers.latestphonewallpapers.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
import com.squaresdevelopers.latestphonewallpapers.fragments.UnLikeFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by eapple on 01/12/2018.
 */

public class LikeItemAdapter extends BaseAdapter {
    ArrayList<ShowDetailModel> itemReponseModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;

    public LikeItemAdapter(Context context, ArrayList<ShowDetailModel> itemReponseModelArrayList) {
        this.itemReponseModelArrayList=itemReponseModelArrayList;
        this.context=context;
        if (context!=null)
        {
            this.layoutInflater=LayoutInflater.from(context);

        }
    }

    @Override
    public int getCount() {
        if (itemReponseModelArrayList!=null) return itemReponseModelArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(itemReponseModelArrayList != null && itemReponseModelArrayList.size() > position) return  itemReponseModelArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(itemReponseModelArrayList != null && itemReponseModelArrayList.size() > position) return  itemReponseModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ShowDetailModel model = itemReponseModelArrayList.get(position);

        viewHolder = new MyViewHolder();
        convertView=layoutInflater.inflate(R.layout.wallpaper_layout,parent,false);
        viewHolder.imageView = convertView.findViewById(R.id.iv_wallpaper);
        viewHolder.layout = convertView.findViewById(R.id.layout);

        Picasso.with(context).load(model.getImageUrl()).into(viewHolder.imageView);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("delete",model.getId());
                bundle.putString("image_url",model.getImageUrl());
                GeneralUtils.putStringValueInEditor(context,"image",model.getImageUrl());
                GeneralUtils.connectFragmentWithDrawer(context,new UnLikeFragment()).setArguments(bundle);
            }
        });

        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder  {
        RelativeLayout layout;
        ImageView imageView;
    }
}
