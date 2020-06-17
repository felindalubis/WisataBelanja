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

public class ListRekomendasi extends RecyclerView.Adapter<ListRekomendasi.CustomViewHolder>{
    Context context;
    ArrayList<WisbelModel> data;
    onItemListener onItemListener;

    public ListRekomendasi(Context context, ArrayList<WisbelModel> data, onItemListener onItemListener){
        this.context = context;
        this.data = data;
        this.onItemListener = onItemListener;
    }

    @Override
    public ListRekomendasi.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_rekomendasi, parent, false);

        return new ListRekomendasi.CustomViewHolder(rootView, onItemListener);
    }

    @Override
    public void onBindViewHolder(ListRekomendasi.CustomViewHolder holder, final int position) {
        Glide.with(context)
                .asBitmap()
                .load(data.get(position).foto)
                .into(holder.item_image);
        holder.item_title.setText(((WisbelModel)data.get(position)).nama_tempat);
        holder.item_rating.setText(String.valueOf(((WisbelModel)data.get(position)).rating));
        holder.item_jamop.setText((((WisbelModel)data.get(position)).jam_buka)+" - "+((WisbelModel)data.get(position)).jam_tutup);
        holder.item_jarak.setText((String.valueOf(((WisbelModel)data.get(position)).jarak)).substring(0,4)+" km from you");
        holder.item_rank.setText("#"+(position+1));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView item_title, item_rating, item_jamop, item_jarak, item_rank;
        ImageView item_image;
        onItemListener onItemListener;

        public CustomViewHolder(View itemView, onItemListener onItemListener) {
            super(itemView);
            item_title = itemView.findViewById(R.id.tv_id);
            item_rating = itemView.findViewById(R.id.item_rating);
            item_jamop = itemView.findViewById(R.id.item_jamop);
            item_jarak = itemView.findViewById(R.id.item_jarak);
            item_image = itemView.findViewById(R.id.item_image);
            item_rank = itemView.findViewById(R.id.tv_rank);
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
