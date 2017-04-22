package com.example.avihnd.todolistmanager;

import android.util.Log;

import java.util.Date;

/**
 * Created by Avi on 23/03/2017.
 * todo_list item, it has a body, date for the tdl mission, and a date reminder title
 */

public class TDLItem {
    private String body;
    private Date date;
    private String dateReminderTitle;

    /**
     * empty constructor
     */
    public TDLItem()
    {
    }

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

    @Override
    public boolean equals(Object obj) {
        TDLItem it = (TDLItem) obj;
        boolean bodyEquals = body.equals(it.getBody());
        boolean dateEquals;
        if (date == null)
        {
            if (it.getDate() == null)
            {
                dateEquals = true;
            }
            else
            {
                dateEquals = false;
            }
        }
        else {
            dateEquals = date.equals(it.getDate());
        }
        boolean reminderEquals;
        if (dateReminderTitle == null) {
            if (it.getDateReminderTitle()== null)
            {
                reminderEquals = true;
            }
            else{
                reminderEquals = false;
            }
        }
        else{
            reminderEquals = dateReminderTitle.equals(it.getDateReminderTitle());
        }
        return (bodyEquals&& dateEquals && reminderEquals);
    }
}
