package com.sk.tdlist;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by karti on 03-02-2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new TaskViewsFactory(this.getApplicationContext(),
                intent));
    }
}
