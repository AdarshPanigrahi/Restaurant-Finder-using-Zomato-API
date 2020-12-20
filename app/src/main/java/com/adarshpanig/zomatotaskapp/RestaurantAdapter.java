package com.adarshpanig.zomatotaskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private ArrayList<RestaurantItem> mRestaurantList;

    public RestaurantAdapter(Context context, ArrayList<RestaurantItem> restaurantList){
        mContext=context;
        mRestaurantList=restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.restaurtant_item,parent,false);
        return new RestaurantViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantItem currentItem=mRestaurantList.get(position);

        String name=currentItem.getRname();
        String address= currentItem.getRaddress();
        String cuisines= currentItem.getRcuisines();

        holder.mName.setText(name);
        holder.mAddress.setText(address);
        holder.mCuisines.setText(cuisines);

    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        public TextView mName;
        public TextView mAddress;
        public TextView mCuisines;
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.nameid1);
            mAddress=itemView.findViewById(R.id.addressid1);
            mCuisines=itemView.findViewById(R.id.cuisinesid1);

        }
    }
}
