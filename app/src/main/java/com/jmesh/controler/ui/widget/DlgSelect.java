package com.jmesh.controler.ui.widget;

/**
 * Created by Administrator on 2018/12/10.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.DlgBase;
import com.jmesh.appbase.utils.Density;
import com.jmesh.controler.R;

public class DlgSelect extends DlgBase {
    public DlgSelect(@NonNull Context context) {
        super(context, null, 0);
    }

    public DlgSelect(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DlgSelect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    LinearLayout itemContainer;

    @Override
    public View getContentView() {
        itemContainer = new LinearLayout(getContext());
        itemContainer.setOrientation(LinearLayout.VERTICAL);
        return itemContainer;
    }

    public void addItem(final String itemName, OnItemClickListener listener) {
        final Item item = new Item(itemName, listener);
        if (item == null) {
            return;
        }
        TextView textView = new TextView(getContext());
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.black));
        itemContainer.addView(textView);
        textView.setText(item.itemName);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, Density.dip2px(getContext(), 10), 0, Density.dip2px(getContext(), 10));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.listener.onItemClick();
                dimiss();
            }
        });
        textView.setBackground(getResources().getDrawable(R.drawable.bg_white_grey));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layoutParams);

    }

    public class Item {
        public String itemName;
        public OnItemClickListener listener;

        public Item(String itemName, OnItemClickListener listener) {
            this.itemName = itemName;
            this.listener = listener;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }

    @Override
    protected void initContentView(View contentView) {

    }

    public interface OnItemClickListener {
        void onItemClick();
    }

}
