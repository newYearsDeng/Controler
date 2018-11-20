package com.jmesh.appbase.base;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/3.
 */

public interface JSONCachable {
    JSONObject toJson();
    void parseJson(JSONObject jsonObject);
}
