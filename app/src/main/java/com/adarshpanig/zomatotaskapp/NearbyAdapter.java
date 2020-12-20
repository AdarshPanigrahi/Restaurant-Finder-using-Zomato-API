package com.adarshpanig.zomatotaskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    private Context mContext;
    private ArrayList<NearbyItem> mNearbyList;

    public NearbyAdapter(Context context, ArrayList<NearbyItem> nearbyList){
        mContext=context;
        mNearbyList=nearbyList;
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.nearby_item,parent,false);
        return new NearbyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        NearbyItem currentItem=mNearbyList.get(position);

        String name=currentItem.getRname();
        String address= currentItem.getRaddress();
        String cuisines= currentItem.getRcuisines();

        holder.mName.setText(name);
        holder.mAddress.setText(address);
        holder.mCuisines.setText(cuisines);

    }

    @Override
    public int getItemCount() {
        return mNearbyList.size();
    }

    public class NearbyViewHolder extends RecyclerView.ViewHolder{

        public TextView mName;
        public TextView mAddress;
        public TextView mCuisines;
        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.nameid);
            mAddress=itemView.findViewById(R.id.addressid);
            mCuisines=itemView.findViewById(R.id.cuisinesid);

        }
    }
}
