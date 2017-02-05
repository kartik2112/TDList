package com.sk.tdlist;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by karti on 04-02-2017.
 */

public class RealCalendarModule {
    private final static int CALENDAR_ID=1;
    public final static String description="This event has been added via TDList App";

    public static void addEvent(Context context,String title,long startTimeMillis){
        /**
         * Reference: http://stackoverflow.com/questions/16473537/add-calendar-event-without-opening-calendar
         */
        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, CALENDAR_ID);

        event.put(CalendarContract.Events.TITLE, title);
        event.put(CalendarContract.Events.DESCRIPTION, description);
        //event.put(CalendarContract.Events.EVENT_LOCATION, location);


        /**
         * Event was being added one day before expected.
         * To solve it 1 day is being added to @startTimeMillis
         */
        event.put(CalendarContract.Events.DTSTART, startTimeMillis);
        event.put(CalendarContract.Events.DTEND, startTimeMillis + 36000000);
        event.put(CalendarContract.Events.ALL_DAY, 1);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true

        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri;
        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");
        } else {
            baseUri = Uri.parse("content://calendar/events");
        }

        context.getContentResolver().insert(baseUri, event);

        Log.d("TDCustomRealCalendar","Calendar Event added");
    }

    public static int deleteEvent(ContentResolver resolver,String eventTitle){
        /**
         * Reference:
         */
        Uri eventsUri;

        Cursor cursor;

        int noOfEventsDeleted=0;

        /**
         * Following are the columns that can be used in the selection part
         * String[] COLS={"calendar_id", "title", "description", "dtstart", "dtend","eventTimezone", "eventLocation"};
         */

        int osVersion = android.os.Build.VERSION.SDK_INT;
        if (osVersion <= 7) { //up-to Android 2.1
            eventsUri = Uri.parse("content://calendar/events");
            cursor = resolver.query(eventsUri, new String[]{ "_id" }, "Calendars._id=" + CALENDAR_ID + " and Calendars.title='"+eventTitle+"' and description='"+description+"'" , null, null);
        } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
            eventsUri = Uri.parse("content://com.android.calendar/events");
            cursor = resolver.query(eventsUri, new String[]{ "_id" }, "calendar_id=" + CALENDAR_ID + " and title='"+eventTitle+"' and description='"+description+"'" , null, null);
        }


        while(cursor.moveToNext()) {
            long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
            noOfEventsDeleted+=resolver.delete(ContentUris.withAppendedId(eventsUri, eventId), null, null);
            Log.d("RealCalendarModule","title="+eventTitle+", eventId"+eventId);
        }
        cursor.close();
        return noOfEventsDeleted;

    }
}
