package com.jmesh.controler.ui.devicescan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.controler.R;
import com.jmesh.controler.data.BDBleDevice;
import com.jmesh.controler.data.DataResolver;
import com.jmesh.controler.ui.devicecontrol.ActivityControlMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/1.
 */

public class AdapterDeviceScan extends RecyclerView.Adapter {
    List<DataResolver.RawData> rawDataList;
    Context context;

    public AdapterDeviceScan(Context context) {
        this.context = context;
        rawDataList = new ArrayList<>();
    }

    public void clearData() {
        rawDataList.clear();
        notifyDataSetChanged();
    }

    public void addDevice(DataResolver.RawData rawData) {
        if (rawData == null) {
            return;
        }
        rawDataList.add(rawData);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scan_device, parent, false);
        HolderItemDeviceScan holderItemDeviceScan = new HolderItemDeviceScan(view);
        return holderItemDeviceScan;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HolderItemDeviceScan holderItemDeviceScan = (HolderItemDeviceScan) holder;
        holderItemDeviceScan.setDate(getItem(position));
    }

    private DataResolver.RawData getItem(int position) {
        if (rawDataList == null && position >= rawDataList.size()) {
            return null;
        }
        return rawDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return rawDataList == null ? 0 : rawDataList.size();
    }

    class HolderItemDeviceScan extends RecyclerView.ViewHolder implements View.OnClickListener {

        public HolderItemDeviceScan(View itemView) {
            super(itemView);
            assignViews();
            itemView.setOnClickListener(this);
        }

        private JmeshDraweeView itemScanDeviceIcon;
        private TextView itemScanDeviceName;

        private void assignViews() {
            itemScanDeviceIcon = itemView.findViewById(R.id.item_scan_device_icon);
            itemScanDeviceName = itemView.findViewById(R.id.item_scan_device_name);
        }

        DataResolver.RawData rawData;

        private void setDate(DataResolver.RawData rawData) {
            this.rawData = rawData;
            int deviceType = rawData.getBleDeviceType();
            String name = BDBleDevice.getName(deviceType);
            itemScanDeviceName.setText(name + rawData.getMeterCode());
            itemScanDeviceIcon.setNativeDrawable(BDBleDevice.getNativeImg(deviceType));
        }

        @Override
        public void onClick(View v) {
            if (rawData == null) {
                return;
            }
            int type = rawData.getBleDeviceType();
            String mac = rawData.getBleDevice().getMac();
            String meterCode = rawData.getMeterCode();
            ActivityControlMain.openActivity(context, type, meterCode, mac);
        }
    }

}
