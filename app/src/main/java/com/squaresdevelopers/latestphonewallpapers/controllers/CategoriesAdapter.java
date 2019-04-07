package com.squaresdevelopers.latestphonewallpapers.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;
import com.squaresdevelopers.latestphonewallpapers.fragments.WallpaperItemsFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eapple on 30/11/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<CategoryDetailModel> categoryModelList;
    private List<CategoryDetailModel> listFiltered;

    public CategoriesAdapter(Activity context, ArrayList<CategoryDetailModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
        this.listFiltered = categoryModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_categories_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CategoryDetailModel model = listFiltered.get(position);

        holder.tvName.setText(model.getCategoryName());
        holder.ivLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralUtils.putStringValueInEditor(context, "id", String.valueOf(model.getId()));
                GeneralUtils.putStringValueInEditor(context, "name", model.getCategoryName());
                GeneralUtils.connectFragmentWithDrawer(context, new WallpaperItemsFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = categoryModelList;
                } else {
                    List<CategoryDetailModel> filteredList = new ArrayList<>();
                    for (CategoryDetailModel row : categoryModelList) {

                        if (row.getCategoryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }


                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<CategoryDetailModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RelativeLayout ivLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            ivLayout = itemView.findViewById(R.id.category_layout);

        }
    }
}
