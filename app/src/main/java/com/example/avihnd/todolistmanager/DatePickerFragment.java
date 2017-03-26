package com.example.avihnd.todolistmanager;

/**
 * Created by Avi on 23/03/2017.
 */
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;
    private Date dateChosen;
    private String dateStringReminder;
    private EditText etReminderBody;


    public Date getDateChosen() {
        return dateChosen;
    }

    public String getDateStringReminder() {
        return dateStringReminder;
    }

    public interface DateDialogListener {
        void onFinishDialog(Date date);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);
        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        etReminderBody = (EditText) v.findViewById(R.id.dialog_data_et_reminder);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int mon = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year,mon,day).getTime();
                                DateDialogListener activity = (DateDialogListener) getActivity();
                                dateChosen = date;
                                dateStringReminder = etReminderBody.getText().toString();
                                activity.onFinishDialog(date);
                                dismiss();
                            }
                        })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DateDialogListener activity = (DateDialogListener) getActivity();
                        dateChosen = null;

                        activity.onFinishDialog(dateChosen);
                        dismiss();
                    }
                })
                .create();
    }

}