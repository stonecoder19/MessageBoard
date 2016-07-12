package com.swengroup6.messageboard.helper;

import android.content.Context;
import android.content.res.TypedArray;

import com.swengroup6.messageboard.R;


/**
 * Created by Matthew on 8/31/2015.
 */

public class Utils {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});

        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);

        styledAttributes.recycle();

        return toolbarHeight;
    }
}
