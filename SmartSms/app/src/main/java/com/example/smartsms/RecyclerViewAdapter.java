package com.example.smartsms;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Rule> mDataset;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public RelativeLayout viewBackground, viewForeground;
        public ImageView imagebyXML;
        public MyViewHolder(View v) {
            super(v);
            description = v.findViewById(R.id.description);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
            imagebyXML = v.findViewById(R.id.thumbnail);
        }
    }

    public RecyclerViewAdapter(Context context ,List<Rule> myDataset) {
        this.context = context;
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
        //Temporary picture set, just for demo
        if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_dialog_info.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_dialog_info);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_menu_help.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_menu_help);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_lock_silent_mode_off.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_dialog_email.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_dialog_email);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_menu_search.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_menu_search);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/sym_action_chat.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.sym_action_chat);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/ic_lock_lock.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.ic_lock_lock);
        }else if(mDataset.get(position).priority.pngPath.equals("res/drawable-xhdpi-v4/btn_star_big_off.png")){
            holder.imagebyXML.setImageResource(android.R.drawable.btn_star_big_off);
        }
        /*int id = context.getResources().getIdentifier(mDataset.get(position).priority.pngPath , null, null);
        holder.imagebyXML.setImageResource(id);*/
        holder.description.setText(mDataset.get(position).name);
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Rule rule) {
        mDataset.add(rule);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}