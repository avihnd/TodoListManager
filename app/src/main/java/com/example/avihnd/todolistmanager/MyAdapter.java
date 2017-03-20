package com.example.avihnd.todolistmanager;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Avi on 17/03/2017.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<String> mDataSet;
    private boolean color = true;


    //nested class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        public TextView mTextView;
        public ViewHolder(TextView v)
        {
            super(v);
            mTextView = v;
            v.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, 1001, getAdapterPosition(), "delete item");
        }

    }



    public MyAdapter(ArrayList<String> dataSet) {
        mDataSet = dataSet;

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        if (color)
        {
            v.setTextColor(Color.RED);
        }
        else
        {
            v.setTextColor(Color.BLUE);
        }
        color = !color;
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataSet.get(position));

        /*holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    curPosClickedOn = adapterPos;
                }
                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
