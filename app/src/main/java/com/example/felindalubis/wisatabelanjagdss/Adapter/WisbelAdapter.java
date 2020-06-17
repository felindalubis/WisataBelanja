package com.example.felindalubis.wisatabelanjagdss.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.felindalubis.wisatabelanjagdss.Model.WisbelModel;
import com.example.felindalubis.wisatabelanjagdss.R;

import java.util.ArrayList;

public class WisbelAdapter extends RecyclerView.Adapter<WisbelAdapter.CustomViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    Context context;
    ArrayList<WisbelModel> data;
    onItemListener onItemListener;

    //declare adapter constructor
    public WisbelAdapter(Context context, ArrayList<WisbelModel> data, onItemListener onItemListener){
        this.context = context;
        this.data = data;
        this.onItemListener = onItemListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_wisbel, parent, false);

        return new CustomViewHolder(rootView, onItemListener);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.item_title.setText(data.get(position).nama_tempat);
        holder.item_rate.setText(String.valueOf(data.get(position).rating));
        holder.item_rank.setText("#"+(position+1));
        }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView item_title, item_rate, item_rank;
        onItemListener onItemListener;

        public CustomViewHolder(View itemView, onItemListener onItemListener) {
            super(itemView);

            item_title = itemView.findViewById(R.id.tv_id);
            item_rate = itemView.findViewById(R.id.item_rate);
            item_rank = itemView.findViewById(R.id.rank_tv);
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





//    private ArrayList<WisbelModel> data;
//    private Context mContext;
//
//    public WisbelAdapter(Context mContext, ArrayList<WisbelModel> data) {
//        this.data = data;
//        this.mContext = mContext;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: called.");
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wisbel, parent, false);
//
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder : called");
//        holder.item_title.setText(((WisbelModel)data.get(position)).getNama_tempat());
//        holder.item_rate.setText(String.valueOf(((WisbelModel)data.get(position)).getRating()));
//
//        holder.item_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: click di title: " + ((WisbelModel)data.get(position)));
//                //ngapain kalo diklik titelnya (sementara)
//                Toast.makeText(mContext, ((WisbelModel)data.get(position)).getNama_tempat(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class ViewHolder  extends RecyclerView.ViewHolder{
//        TextView item_title, item_rate;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            item_title = itemView.findViewById(R.id.item_title);
//            item_rate = itemView.findViewById(R.id.item_rate);
//        }
//    }
//}
