package com.sajorahasan.tiffincounter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sajorahasan.tiffincounter.R;
import com.sajorahasan.tiffincounter.room.Tiffin;

import java.util.List;

/**
 * Created by admin on 14-11-2017.
 */

public class TiffinFiltersAdapter extends RecyclerView.Adapter<TiffinFiltersAdapter.MyViewHolder> {

    private List<Tiffin> tiffinList;

    public TiffinFiltersAdapter(List<Tiffin> tiffinList) {
        this.tiffinList = tiffinList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiffin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tiffin tiffin = tiffinList.get(position);
        holder.num.setText(String.valueOf(position + 1));
        holder.date.setText(tiffin.getTiffinDate());

        if (tiffin.getType().equalsIgnoreCase("Lunch")) {
            holder.type.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        }
        if (tiffin.getType().equalsIgnoreCase("Dinner")) {
            holder.type.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
        holder.type.setText(tiffin.getType());
        holder.amount.setText(String.valueOf(tiffin.getAmount()));
    }

    public Tiffin getItem(int position) {
        return tiffinList.get(position);
    }

    @Override
    public int getItemCount() {
        return tiffinList == null ? 0 : tiffinList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView num;
        TextView date;
        TextView type;
        TextView amount;

        MyViewHolder(View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.txvNo);
            date = itemView.findViewById(R.id.txvDate);
            type = itemView.findViewById(R.id.txvType);
            amount = itemView.findViewById(R.id.txvAmount);
        }
    }
}
