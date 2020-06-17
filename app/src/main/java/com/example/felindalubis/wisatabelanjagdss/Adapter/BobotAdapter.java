package com.example.felindalubis.wisatabelanjagdss.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.felindalubis.wisatabelanjagdss.Model.BobotModel;
import com.example.felindalubis.wisatabelanjagdss.R;

import java.util.ArrayList;

public class BobotAdapter extends RecyclerView.Adapter<BobotAdapter.CustomViewHolder>{
    Context context;
    ArrayList<BobotModel> data;

    public BobotAdapter(Context context, ArrayList<BobotModel> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public BobotAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_bobot, parent, false);

        return new BobotAdapter.CustomViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BobotAdapter.CustomViewHolder holder, final int position) {
        holder.item_id.setText("User "+String.valueOf(((BobotModel)data.get(position)).id));
        holder.item_rating.setText(String.valueOf(((BobotModel)data.get(position)).rating));
        holder.item_facility.setText(String.valueOf(((BobotModel)data.get(position)).fasilitas));
        holder.item_jarak.setText(String.valueOf(((BobotModel)data.get(position)).jarak));
        holder.item_jamop.setText(String.valueOf(((BobotModel)data.get(position)).operasional));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView item_id, item_rating, item_jamop, item_jarak, item_facility;

        public CustomViewHolder(View itemView) {
            super(itemView);
            item_id = itemView.findViewById(R.id.tv_id);
            item_rating = itemView.findViewById(R.id.tv_rating);
            item_facility = itemView.findViewById(R.id.tv_facility);
            item_jarak = itemView.findViewById(R.id.tv_distance);
            item_jamop = itemView.findViewById(R.id.tv_operasional);
        }
    }
}
