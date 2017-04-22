package com.example.avihnd.todolistmanager;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.R.attr.data;
import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener {
    private static final String DIALOG_DATE = "MainActivity.DateDialog";
    private static final String TAB_NAME = "missions";
    private DatePickerFragment dialog;
    private EditText mUserInput;
    private RecyclerView mTdl;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    ArrayList<TDLItem> tdlItems = new ArrayList<TDLItem>();
    private DatabaseReference myRef;


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
        mAdapter = (MyAdapter)new MyAdapter(this, tdlItems);
        mTdl.setAdapter(mAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_DATE);
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(TAB_NAME);
        addDBListenter(myRef);


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
        switch (item.getItemId()) {
            case 1001:
                //remove item
                int pos = item.getOrder();
                final TDLItem itemToBeRemoved = tdlItems.get(pos);
                removeItemFromFirebase(itemToBeRemoved);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onFinishDialog(Date date) {
        String bodyInput = mUserInput.getText().toString() + "\n";
        String dataStringReminder = dialog.getDateStringReminder();
        TDLItem newItem = new  TDLItem(bodyInput, date, dataStringReminder);
        //add to cloud the new item
        myRef.push().setValue(newItem);

    }


    public void addTdlToScreen(TDLItem newItem)
    {
        Date date = newItem.getDate();
        tdlItems.add(newItem);

        int position = tdlItems.size() - 1;
        mAdapter.notifyItemInserted(position);
        //scroll to the bottom of the list
        mTdl.scrollToPosition(position);
        if (date != null)
        {
            Toast.makeText(this, "Selected Date :"+ date.toString(), Toast.LENGTH_LONG).show();
        }

    }


    private int _findPosOfItem(TDLItem item)
    {
        for (int pos = 0; pos < tdlItems.size(); pos++)
        {
            TDLItem it = tdlItems.get(pos);
            if (it.equals(item))
            {
                return pos;
            }
        }
        return -1;


    }


    public void removeTdlFromScreen(TDLItem item)
    {
        int pos  = _findPosOfItem(item);
        tdlItems.remove(pos);
        mAdapter.notifyItemRemoved(pos);
        mAdapter.notifyItemRangeChanged(pos, tdlItems.size());

    }

    private void addDBListenter(DatabaseReference newRef)
    {
        newRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                TDLItem newItem = dataSnapshot.getValue(TDLItem.class);
                if (newItem != null)
                    Log.d("tagChildAdded", "Value is: " + newItem.getBody());
                    addTdlToScreen(newItem);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TDLItem item = dataSnapshot.getValue(TDLItem.class);
                if (item != null)
                    Log.d("tagChildRemoved", "Value is: " + item.getBody());
                    removeTdlFromScreen(item);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tagDataCancelled", "Failed to read value.", error.toException());
            }
        });

    }

    //remove the tdlItem given as an argument from the database of firebase
    public void removeItemFromFirebase(final TDLItem itemToBeRemoved)
    {
        final Query query = myRef.orderByValue();
        Log.d("logTaskFound", itemToBeRemoved.getBody());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot task : dataSnapshot.getChildren())
                {
                    TDLItem item = task.getValue(TDLItem.class);
                    Log.d("logTaskFound", item.getBody());
                    if (item.equals(itemToBeRemoved))
                    {
                        task.getRef().removeValue();
                        query.removeEventListener(this);
                        break;
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


