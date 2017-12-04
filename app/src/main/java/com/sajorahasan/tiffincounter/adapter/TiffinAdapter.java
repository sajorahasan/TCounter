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

public class TiffinAdapter extends RecyclerView.Adapter<TiffinAdapter.MyViewHolder> {

    private List<Tiffin> tiffinList;
    private static OnItemClickListener mListener;
    private static OnItemLongClickListener longListener;

    // Define the mListener interface
    public interface OnItemClickListener {
        void onDateClick(View view, int position);
        void onAmountClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        longListener = listener;
    }

    public TiffinAdapter(List<Tiffin> tiffinList) {
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

    public void removeItem(int position) {
        tiffinList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Tiffin item, int position) {
        tiffinList.add(position, item);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView num;
        public TextView date;
        public TextView type;
        public TextView amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.txvNo);
            date = itemView.findViewById(R.id.txvDate);
            type = itemView.findViewById(R.id.txvType);
            amount = itemView.findViewById(R.id.txvAmount);

            date.setOnClickListener(view -> {
                if (mListener != null) mListener.onDateClick(view, getAdapterPosition());
            });

            amount.setOnClickListener(view -> {
                if (mListener != null) mListener.onAmountClick(view, getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                if (mListener != null) longListener.onItemLongClick(v, getAdapterPosition());
                return true;
            });
        }
    }
}
