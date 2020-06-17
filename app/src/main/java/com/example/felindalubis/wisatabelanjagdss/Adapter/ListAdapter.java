package com.example.felindalubis.wisatabelanjagdss.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.felindalubis.wisatabelanjagdss.Model.WisbelModel;
import com.example.felindalubis.wisatabelanjagdss.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder>{
    Context context;
    ArrayList<WisbelModel> data;
    onItemListener onItemListener;

    public ListAdapter(Context context, ArrayList<WisbelModel> data, onItemListener onItemListener){
        this.context = context;
        this.data = data;
        this.onItemListener = onItemListener;
    }

    @Override
    public ListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_wisbel2, parent, false);

        return new ListAdapter.CustomViewHolder(rootView, onItemListener);
    }

    @Override
    public void onBindViewHolder(ListAdapter.CustomViewHolder holder, final int position) {
        Glide.with(context)
                .asBitmap()
                .load(data.get(position).foto)
                .into(holder.item_image);
        holder.item_title.setText(((WisbelModel)data.get(position)).nama_tempat);
        holder.item_rating.setText(String.valueOf(((WisbelModel)data.get(position)).rating));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView item_title, item_rating;
        ImageView item_image;
        onItemListener onItemListener;

        public CustomViewHolder(View itemView, onItemListener onItemListener) {
            super(itemView);
            item_title = itemView.findViewById(R.id.tv_id);
            item_rating = itemView.findViewById(R.id.item_rating);
            item_image = itemView.findViewById(R.id.item_image);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(data, getAdapterPosition());
        }
    }
    public interface onItemListener{
        void onItemClick(ArrayList<WisbelModel> data, int position);
    }
}
