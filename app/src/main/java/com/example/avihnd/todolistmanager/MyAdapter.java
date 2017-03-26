package com.example.avihnd.todolistmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Avi on 17/03/2017.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<TDLItem> mDataSet;
    private boolean color = true;
    Context ctx;

    //nested class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        public TextView mTextView;
        private Context context;


        public ViewHolder(Context contextIn, TextView v)
        {
            super(v);
            mTextView = v;
            context = contextIn;
            v.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);

            v.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View popupView = layoutInflater.inflate(R.layout.popup, null);
                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            RecyclerView.LayoutParams.WRAP_CONTENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT);
                    TextView reminderDate = (TextView)popupView.findViewById(R.id.tv_reminder_date);
                    TextView reminderBody = (TextView)popupView.findViewById(R.id.tv_reminder_body);

                    //get date and reminder title
                    Date d = MyAdapter.this.mDataSet.get(getAdapterPosition()).getDate();
                    if (d == null)
                    {
                        reminderDate.setText("no date chosen");
                    }
                    else
                    {
                        reminderDate.setText(d.toString());
                    }
                    reminderBody.setText(MyAdapter.this.mDataSet.get(getAdapterPosition()).getDateReminderTitle());
                    Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                    btnDismiss.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }});

                    popupWindow.showAsDropDown(v, 50, -30);


                    //add a delete button
                    Button btnDelete = (Button)popupView.findViewById(R.id.delete_reminder);
                    btnDelete.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            int pos = getAdapterPosition();
                            mDataSet.remove(pos);
                            notifyItemRemoved(pos);
                            notifyItemRangeChanged(pos, mDataSet.size());
                            popupWindow.dismiss();
                        }});

                    popupWindow.showAsDropDown(v, 50, -30);

                    //add a call button if the task starts with "call"
                    final String dataStringReminder = MyAdapter.this.mDataSet.get(getAdapterPosition()).getDateReminderTitle();
                    Button btnCall = (Button)popupView.findViewById(R.id.open_dialer);
                    if (dataStringReminder != null)
                    {
                        //call the number written in the reminder if the format is "call <number>"
                        if (dataStringReminder.startsWith("call "))
                        {
                            btnCall.setVisibility(View.VISIBLE);
                            btnCall.setOnClickListener(new Button.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData(Uri.parse("tel:"+ dataStringReminder.substring(5)));
                                    context.startActivity(intent);
                                    popupWindow.dismiss();
                                }});

                            popupWindow.showAsDropDown(v, 50, -30);


                        }
                    }


                }});


        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, 1001, getAdapterPosition(), "delete item");
        }
    }



    public MyAdapter(Context ctxIn, ArrayList<TDLItem> dataSet) {
        mDataSet = dataSet;
        ctx = ctxIn;

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
        ViewHolder vh = new ViewHolder(ctx, v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText( mDataSet.get(position).getBody());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
