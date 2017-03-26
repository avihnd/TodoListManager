package com.example.avihnd.todolistmanager;

import android.util.Log;

import java.util.Date;

/**
 * Created by Avi on 23/03/2017.
 */

public class TDLItem {
    private String body;
    private Date date;
    private String dateReminderTitle;

    public TDLItem(String bodyInput, Date dateInput, String dateTitle)
    {
        body = bodyInput;
        date = dateInput;
        dateReminderTitle = dateTitle;
        if (dateReminderTitle != null)
        {
            Log.i("tdl_item", dateReminderTitle);
        }

    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    public String getDateReminderTitle() {
        return dateReminderTitle;
    }
}
