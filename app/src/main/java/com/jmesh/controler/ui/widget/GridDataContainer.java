package com.jmesh.controler.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmesh.appbase.utils.Density;
import com.jmesh.controler.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public class GridDataContainer extends LinearLayout {
    public GridDataContainer(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
    }

    public GridDataContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
    }

    List<Data> data;

    public void refresh(List<Data> data) {
        this.data = data;
        removeAllViews();
        if (data == null) {
            return;
        }
        width = Density.windowDisplaySize(getContext())[0];
        addDatas();
    }


    private boolean hasRefreshData;

    public boolean isHasRefreshData() {
        return hasRefreshData;
    }

    public void setHasRefreshData(boolean hasRefreshData) {
        this.hasRefreshData = hasRefreshData;
    }

    private void addDatas() {
        if (data == null) {
            return;
        }
        LinearLayout row = null;
        for (int index = 0; index < data.size(); index++) {
            if (index % 3 == 0) {
                if (index != 0) {
                    View division = new View(getContext());
                    division.setBackgroundColor(Color.parseColor("#ffeeeeee"));
                    ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    addView(division);
                    division.setLayoutParams(layoutParams);
                }
                row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                addView(row);

            }
            if (row != null) {
                View view = getSingleData(data.get(index));
                if (view != null) {
                    row.addView(view);
                }
            }
        }
    }

    private View getSingleData(Data data) {
        if (width == 0) {
            return null;
        }
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_grid_data, null, false);
        TextView itemGridDataValueNumber = itemView.findViewById(R.id.item_grid_data_value_number);
        TextView itemGridDataValueUnit = itemView.findViewById(R.id.item_grid_data_value_unit);
        TextView itemGridDataName = itemView.findViewById(R.id.item_grid_data_name);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(layoutParams);
        itemGridDataValueNumber.setText(data.getValue());
        itemGridDataName.setText(data.getName());
        itemGridDataValueUnit.setText(data.getUnit());
        return itemView;
    }

    int width;


    public static class Data {
        private String name;
        private String value;
        private String unit;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
