package com.jmesh.appbase.dao;

import android.content.ContentValues;

/**
 * Created by Administrator on 2018/7/6.
 */

public abstract class CVParse {
    public abstract ContentValues parseToCV();

    public abstract void resolveCV(ContentValues contentValues);
}
