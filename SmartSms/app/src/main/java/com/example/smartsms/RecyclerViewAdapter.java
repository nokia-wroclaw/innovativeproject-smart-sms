package com.example.smartsms;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<String> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View v) {
            super(v);
            description = v.findViewById(R.id.description);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
        }
    }

    public RecyclerViewAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {

        holder.description.setText(mDataset.get(position));
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}