package org.hehe7xiao.yijitong;


import android.text.TextUtils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class YJT {
    private String phone;
    private String password;
    private String iccid;
    private String userid;
    private boolean isLogin = false;
    private final String url = "http://www.yijitongoa.com:9090";


    private HashMap<String, String> headers = new HashMap<String, String>() {
        {
            put("Content-Type", "application/json; charset=utf-8");
            put("apiVer", "1");
            put("clientVersion", "android_10373");
            put("Host", "www.yijitongoa.com:9090");
            put("User-Agent", "yjt-oa");
            put("Connection", "Keep-Alive");
        }
    };


    public YJT(String phone, String password, String iccid, String userid) {
        this.phone = phone;
        this.password = password;
        this.iccid = iccid;
        this.userid = userid;
    }

    public boolean login() {

        String url = this.url + "/yjtoa/s/reallogin";
        HashMap<String, Object> fields = new HashMap<String, Object>() {
            {
                put("contentId", 0);
                put("custUniqueId", 0);
                put("custVCode", 0);
                put("iccid", iccid);
                put("password", password);
                put("phone", phone);
                put("userId", userid);

            }
        };
        JSONObject body = new JSONObject(fields);
        try {
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .headers(this.headers)
                    .body(body)
//                    .fields(fields)
                    .asJson();
            if (response.getStatus() == 200) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                this.headers.put("Cookie", TextUtils.join("; ", cookies));
                return true;
            } else {
                return false;
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        } finally {
        }

    }

    public String attendance() {
        if (!this.isLogin) {
            this.login();
        }
        String url = this.url + "/yjtoa/s/signins/attendances";
        HashMap<String, Object> fields = new HashMap<String, Object>() {
            {
                put("descColor", 0);
                put("iccId", iccid);
                put("id", 0);
                put("positionData", "39.962536,116.229567");
                put("positionDescription", "中国北京市海淀区杏石口路99号");
                put("resultColor", 0);
                put("signResult", 0);
                put("type", "VISIT");
                put("userId", userid);
            }
        };
        JSONObject body = new JSONObject(fields);
        try {
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .headers(this.headers)
                    .body(body)
//                    .fields(fields)
                    .asJson();
            if (response.getStatus() == 200) {
                JSONObject j = response.getBody().getObject();
                return j.getJSONObject("payload").getString("signResultDesc");
            }

        } catch (Exception e) {
            return e.getMessage();
        } finally {
        }

        return "error";
    }

}
