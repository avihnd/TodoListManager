package com.example.avihnd.todolistmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText mUserInput;
    private RecyclerView mTdl;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    ArrayList<String> tdlStrings = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the widgets by id
        mUserInput = (EditText)findViewById(R.id.et_user_input);
        mTdl = (RecyclerView)findViewById(R.id.rv_tdl);

        //set layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mTdl.setLayoutManager(mLayoutManager);

        //set adapter
        mAdapter = (MyAdapter)new MyAdapter(tdlStrings);
        mTdl.setAdapter(mAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStr = mUserInput.getText().toString() + "\n";
                tdlStrings.add(inputStr);
                int position = tdlStrings.size();
                mAdapter.notifyItemInserted(position - 1);
                //scroll to the bottom of the list
                mTdl.scrollToPosition(position - 1);
                //Toast.makeText(mContext,"Added : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tdl_hold_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1001:
                //remove item
                int pos = item.getOrder();
                tdlStrings.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                mAdapter.notifyItemRangeChanged(pos, tdlStrings.size());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
