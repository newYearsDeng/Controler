package com.jmesh.controler.data;

import android.text.TextUtils;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.blebase.utils.HexUtils;
import com.jmesh.blebase.utils.JMeshLog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataResolver {
    public static final byte[] bytes = HexUtils.hexStringToBytes(("02010611094244303A303030303838383830303431"));

    public static List<byte[]> spiltData(byte[] data) {
        if (data == null) {
            return null;
        }
        List<byte[]> dataList = new ArrayList<>();
        int lengthIndex = 0;
        while (true) {
            int dataSizes = ((int) data[lengthIndex]) & 0x000000ff;
            byte[] singleData = new byte[dataSizes + 1];
            for (int i = 0; i < dataSizes + 1; i++) {
                singleData[i] = data[lengthIndex + i];
            }
            dataList.add(singleData);
            lengthIndex += dataSizes + 1;
            if (lengthIndex >= data.length) {
                break;
            }
        }
        return dataList;
    }

    public static class RawData {
        private byte[] srcData;
        private List<SplitRawData> splitRawDataList;
        BleDevice bleDevice;

        public RawData(BleDevice bleDevice) {
            this.bleDevice = bleDevice;
            this.srcData = bleDevice.getScanRecord();
            resolve();
        }

        public BleDevice getBleDevice() {
            return bleDevice;
        }

        public RawData(byte[] rawData) {
            this.srcData = rawData;
            resolve();
        }

        private void resolve() {
            if (srcData == null) {
                return;
            }
            try {
                List<byte[]> splitDataList = spiltData(srcData);
                for (byte[] singleData : splitDataList) {
                    SplitRawData splitRawData = new SplitRawData();
                    splitRawData.setLength(singleData[0]);
                    splitRawData.setType(singleData[1]);
                    splitRawData.setData(HexUtils.intercept(singleData, 2, singleData.length - 2));
                    if (splitRawDataList == null) {
                        splitRawDataList = new ArrayList<>();
                    }
                    splitRawDataList.add(splitRawData);
                }
            } catch (Throwable throwable) {

            }

        }


        public byte[] getSrcData() {
            return srcData;
        }

        public void setSrcData(byte[] srcData) {
            this.srcData = srcData;
        }

        public List<SplitRawData> getSplitRawDataList() {
            return splitRawDataList;
        }

        public void setSplitRawDataList(List<SplitRawData> splitRawDataList) {
            this.splitRawDataList = splitRawDataList;
        }

        public boolean isBDDevice() {
            if (splitRawDataList == null) {
                return false;
            }
            for (SplitRawData splitRawData : splitRawDataList) {
                String dataStr = new String(splitRawData.data);
                if (isBD(dataStr)) {
                    return true;
                }
            }
            return false;

        }

        private boolean isBD(String s) {
            if (TextUtils.isEmpty(s)) {
                return false;
            }
            s = s.toLowerCase();
            String regex = "bd[A-Za-z0-9]+\\:";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                setMeterCode(s.split(":")[1]);
                String match = matcher.group(0);
                int dIndex = match.indexOf('d');
                int colonIndex = match.indexOf(':');
                String type = (String) match.subSequence(dIndex + 1, colonIndex);
                try {
                    setBleDeviceType(Integer.parseInt(type));
                } catch (Throwable t) {
                }
                return true;
            }
            return false;
        }

        String meterCode;

        public String getMeterCode() {
            return meterCode;
        }

        public void setMeterCode(String meterCode) {
            this.meterCode = meterCode;
        }

        int bleDeviceType = -1;

        public int getBleDeviceType() {
            return bleDeviceType;
        }

        public void setBleDeviceType(int type) {
            this.bleDeviceType = type;
        }
    }


    public static class SplitRawData {
        int length;
        int type;
        byte[] data;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

    }

}
