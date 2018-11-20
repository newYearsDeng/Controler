package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jmesh.appbase.BaseApplication;
import com.jmesh.appbase.utils.ComFuncs;

/**
 * Created by Administrator on 2018/6/30.
 */

public class JmeshDraweeView extends SimpleDraweeView {

    public JmeshDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public JmeshDraweeView(Context context) {
        super(context);
    }

    public JmeshDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JmeshDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNativeDrawable(int resId) {
        setImageURI(Uri.parse("res://" + BaseApplication.context.getPackageName() + "/" + Integer.toString(resId)));
    }

    public void setNativeDrawable(String nativeDrawable) {
        int resId = ComFuncs.getMipmapResource(nativeDrawable);
        setNativeDrawable(resId);
    }

}
