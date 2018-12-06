package com.jmesh.controler.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.DlgBase;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.Density;
import com.jmesh.controler.R;
import com.jmesh.controler.data.AirConditionerBrand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/11/14.
 */

public class DlgChooseAirConditionerBrand extends DlgBase {

    public DlgChooseAirConditionerBrand(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_choose_airconditioner_brand, null);
        return view;
    }

    @Override
    protected void initContentView(View contentView) {
        assignViews(contentView);
        initEvent();
    }

    private void initEvent() {
        brandChooseClose.setOnClickListener(this);
        confirmBn.setOnClickListener(this);
    }

    private LinearLayout brandList;
    private TextView confirmBn;
    private JmeshDraweeView brandChooseClose;

    private void assignViews(View root) {
        brandList = (LinearLayout) root.findViewById(R.id.brand_list);
        confirmBn = (TextView) root.findViewById(R.id.confirm_bn);
        brandChooseClose = (JmeshDraweeView) root.findViewById(R.id.brand_choose_close);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.brand_choose_close:
                dimiss();
                break;
            case R.id.confirm_bn:
                confirm();
                break;
        }
    }

    private void confirm() {
        AirConditionerBrand checkItem = null;
        for (int i = 0; i < airConditionerBrandList.size(); i++) {
            if (airConditionerBrandList.get(i).isSelected()) {
                checkItem = airConditionerBrandList.get(i);
                break;
            }
        }
        if (checkItem != null) {
            if (callback != null) {
                callback.onCheck(checkItem);
            }
        }
        dimiss();

    }

    @Override
    public void dimiss() {
        super.dimiss();
        removeBrand();
        airConditionerBrandList = null;
    }

    private List<AirConditionerBrand> airConditionerBrandList;

    public List<AirConditionerBrand> getAirConditionerBrandList() {
        return airConditionerBrandList;
    }

    public void setAirConditionerBrandList(List<AirConditionerBrand> airConditionerBrandList) {
        this.airConditionerBrandList = airConditionerBrandList;
        removeBrand();
        addBrand();
        refreshBrandView();
    }

    Set<FrameLayout> items = new HashSet<>();

    private void addBrand() {
        for (int i = 0; i < airConditionerBrandList.size(); i++) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(layoutParams);
            brandList.addView(frameLayout, 1);
            items.add(frameLayout);
            frameLayout.setTag(airConditionerBrandList.get(i));
            frameLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object object = v.getTag();
                    if (object == null) {
                        return;
                    }
                    AirConditionerBrand airConditionerBrand = (AirConditionerBrand) object;
                    airConditionerBrand.setSelected(true);
                    for (int i = 0; i < airConditionerBrandList.size(); i++) {
                        if (airConditionerBrandList.get(i) != airConditionerBrand) {
                            airConditionerBrandList.get(i).setSelected(false);
                        }
                    }
                    refreshBrandView();
                }
            });
            addName(frameLayout, i);
            addCheckbox(frameLayout);
        }
    }

    private void addName(FrameLayout container, int index) {
        FrameLayout.LayoutParams nameLayout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameLayout.topMargin = Density.dip2px(getContext(), 16);
        nameLayout.bottomMargin = Density.dip2px(getContext(), 16);
        nameLayout.leftMargin = Density.dip2px(getContext(), 39);
        nameLayout.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        TextView name = new TextView(getContext());
        name.setText(airConditionerBrandList.get(index).getName());
        name.setTextSize(19);
        name.setId(R.id.id_name);
        container.addView(name, nameLayout);
    }

    private void addCheckbox(FrameLayout container) {
        JmeshDraweeView check = new JmeshDraweeView(getContext());
        FrameLayout.LayoutParams checkLayout = new FrameLayout.LayoutParams(Density.dip2px(getContext(), 19), Density.dip2px(getContext(), 19));
        checkLayout.rightMargin = Density.dip2px(getContext(), 39);
        checkLayout.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        check.setNativeDrawable(R.mipmap.check_frame_uncheck);
        container.addView(check, checkLayout);
        check.setId(R.id.id_check);
    }

    private void refreshBrandView() {
        for (FrameLayout item : items) {
            AirConditionerBrand airConditionerBrand = item.getTag() == null ? null : (AirConditionerBrand) item.getTag();
            if (airConditionerBrand == null) {
                continue;
            }
            TextView name = item.findViewById(R.id.id_name);
            JmeshDraweeView check = item.findViewById(R.id.id_check);
            if (airConditionerBrand.isSelected()) {
                if (name != null && check != null) {
                    name.setTextColor(Color.parseColor("#ff010BB2"));
                    check.setNativeDrawable(R.mipmap.check_frame_checked);
                }
            } else {
                if (name != null && check != null) {
                    name.setTextColor(Color.parseColor("#ff747474"));
                    check.setNativeDrawable(R.mipmap.check_frame_uncheck);
                }
            }
        }
    }

    private void removeBrand() {
        for (int i = 0; i < brandList.getChildCount(); i++) {
            View view = brandList.getChildAt(i);
            if (view.getTag() instanceof AirConditionerBrand) {
                view.setTag(null);
                brandList.removeView(view);
            }
        }
    }


    public interface OnCheckCallback {
        void onCheck(AirConditionerBrand airConditionerBrand);
    }

    OnCheckCallback callback;

    public void setCallback(OnCheckCallback callback) {
        this.callback = callback;
    }
}
