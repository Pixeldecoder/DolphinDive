package com.capstone.dolphindive.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.capstone.dolphindive.diveshoplistdetail;
import com.capstone.dolphindive.model.diveshopdata;
import com.capstone.dolphindive.R;

import java.util.ArrayList;
import java.util.List;

public class placesAdapter extends RecyclerView.Adapter<placesAdapter.RecentsViewHolder> implements Filterable{
    private ArrayList<diveshopdata> recentsDataList;
    private ArrayList<diveshopdata> fullList;
    Context context;

    public placesAdapter(Context context, int i, ArrayList<diveshopdata> recentsDataList) {
        this.context = context;
        this.recentsDataList = recentsDataList;
        fullList = new ArrayList<>(recentsDataList);
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recents_row_item, parent, false);
        RecentsViewHolder evh = new RecentsViewHolder(view);
        // here we create a recyclerview row item layout file
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {

        holder.countryName.setText(recentsDataList.get(position).getCountryName());
        holder.placeName.setText(recentsDataList.get(position).getPlaceName());
        holder.price.setText(recentsDataList.get(position).getPrice());
        holder.placeImage.setImageResource(recentsDataList.get(position).getImageUrl());
        holder.rate.setText(recentsDataList.get(position).getRate());
        holder.popular.setText(recentsDataList.get(position).getPopular());
        holder.itemView.setOnClickListener((view)-> {
                Intent i=new Intent(context, diveshoplistdetail.class);
                context.startActivity(i);
        });

    }



    @Override
    public int getItemCount() {

        return recentsDataList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<diveshopdata> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (diveshopdata item : fullList) {
                    if (item.getCountryName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
    }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recentsDataList.clear();
            recentsDataList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static final class RecentsViewHolder extends RecyclerView.ViewHolder{

        ImageView placeImage;
        TextView placeName, countryName, price,rate,popular;

        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.diveshop_name);
            countryName = itemView.findViewById(R.id.country_name);
            price = itemView.findViewById(R.id.price);
            popular = itemView.findViewById(R.id.popular);
            rate = itemView.findViewById(R.id.rate);
        }
    }


}

