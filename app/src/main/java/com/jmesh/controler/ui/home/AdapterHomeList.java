package com.jmesh.controler.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.DlgJmeshBase;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.controler.R;
import com.jmesh.controler.data.BDBleDevice;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.Device;
import com.jmesh.controler.data.dao.IDevice;
import com.jmesh.controler.ui.devicecontrol.ActivityControlMain;

import java.util.List;

/**
 * Created by Administrator on 2018/11/12.
 */

public class AdapterHomeList extends RecyclerView.Adapter {
    Context context;

    public AdapterHomeList(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_list, parent, false);
        return new HomeListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof HomeListItemHolder)) {
            return;
        }
        HomeListItemHolder homeListItemHolder = (HomeListItemHolder) holder;
        homeListItemHolder.setData(devices.get(position));
    }

    List<Device> devices;

    private List<Device> getDevices() {
        if (devices == null) {
            devices = DBHelper.getInstance().getAllDevice();
        }
        return devices;
    }

    public void refreshDevice() {
        devices = DBHelper.getInstance().getAllDevice();
    }

    @Override
    public int getItemCount() {
        return getDevices().size();
    }

    class HomeListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public HomeListItemHolder(View itemView) {
            super(itemView);
            assignViews();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private JmeshDraweeView itemHomeListIcon;
        private TextView itemHomeListName;
        private TextView itemHomeListDesc;

        private void assignViews() {
            itemHomeListIcon = itemView.findViewById(R.id.item_home_list_icon);
            itemHomeListName = (TextView) itemView.findViewById(R.id.item_home_list_name);
            itemHomeListDesc = (TextView) itemView.findViewById(R.id.item_home_list_desc);
        }

        IDevice device;

        public void setData(IDevice data) {
            if (data == null) {
                return;
            }
            device = data;
            itemHomeListName.setText(data.getName());
            itemHomeListDesc.setText(data.getMeterCode());
            itemHomeListIcon.setNativeDrawable(BDBleDevice.getNativeBigImg(data.getType()));
        }


        @Override
        public void onClick(View v) {
            if (devices.get(getLayoutPosition()) != null) {
                Device device = devices.get(getLayoutPosition());
                ActivityControlMain.openActivity(context, device.type, device.meterCode, device.mac);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v != itemView || device == null) {
                return true;
            }
            DlgJmeshBase dlgJmeshBase = new DlgJmeshBase(itemView.getContext());
            dlgJmeshBase.setTitle("是否删除设备？");
            dlgJmeshBase.setCancelButtonText("取消");
            dlgJmeshBase.setConfirmButtonText("确认");
            dlgJmeshBase.setOnDlgClickListener(new DlgJmeshBase.OnDlgClick() {
                @Override
                public void onConfirm() {
                    DBHelper.getInstance().deleteDevice((Device) device);
                    refreshDevice();
                    notifyDataSetChanged();
                }

                @Override
                public void onCancel() {

                }
            });
            dlgJmeshBase.show();
            return true;
        }
    }


}
