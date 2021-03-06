package org.hehe7xiao.yijitong;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class YJT {
    private String phone;
    private String password;
    private String iccid;
    private String userid;
    private String custName;
    private String contentId;
    private String custUniqueId;
    private String custVCode;
    private boolean isLogin = false;
    private Geo geo = null;

    public String getUserid() {
        return userid;
    }

    private final String url = "https://www.yijitongoa.com";


    private HashMap<String, String> headers = new HashMap<String, String>() {
        {
            put("Content-Type", "application/json; charset=utf-8");
            put("apiVer", "1");
            put("clientVersion", "android_10641");
            put("Host", "www.yijitongoa.com");
            put("User-Agent", "yjt-oa");
            put("Connection", "Keep-Alive");
            put("Accept-Encoding", "gzip, deflate");
        }
    };

    public YJT(String phone, String password, String iccid) {
        this.phone = phone;
        this.password = password;
        this.iccid = iccid;
    }

    public YJT(String phone, String password, String iccid, Geo geo) {
        this.phone = phone;
        this.password = password;
        this.iccid = iccid;
        this.geo = geo;
    }

    public boolean clientLogin() {

        String url = this.url + "/yjtoa/s/clientlogin";
        HashMap<String, Object> fields = new HashMap<String, Object>() {
            {
                put("iccid", iccid);
                put("password", password);
                put("loginName", phone);

            }
        };
        String body = (new JSONObject(fields)).toString();

        try {
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .headers(this.headers)
                    .body(body)
                    .asJson();
            if (response.getCode() == 200) {
                JSONObject j = response.getBody().getObject();
                JSONObject payload = (JSONObject) j.getJSONArray("payload").get(0);
                this.custName = payload.getString("custName");
                this.custUniqueId = payload.getString("custUniqueId");
                this.contentId = payload.getString("contentId");
                this.custVCode = payload.getString("custVCode");
                this.userid = payload.getString("userId");
                return true;
            } else {
                return false;
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
        }

    }


    public boolean login() {

        String url = this.url + "/yjtoa/s/reallogin";
        HashMap<String, Object> fields = new HashMap<String, Object>() {
            {
                put("contentId", contentId);
                put("custUniqueId", custUniqueId);
                put("custVCode", custVCode);
                put("iccid", iccid);
                put("password", password);
                put("phone", phone);
                put("userId", userid);

            }
        };
        String body = (new JSONObject(fields)).toString();

        try {
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .headers(this.headers)
                    .body(body)
                    .asJson();
            if (response.getCode() == 200) {
                String cookies = response.getHeaders().get("Set-Cookie");
                this.headers.put("Cookie", cookies);
                this.isLogin = true;
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
        boolean login2 = false;
        if (!this.isLogin) {
            login2 = this.clientLogin() && this.login();
        }
        final String positionData = this.geo != null ? geo.getCoordinate() : "39.962536,116.229567";
        final String positionDescription = this.geo != null ? geo.getAddress() : "中国北京市海淀区杏石口路99号";
        if (login2) {
            String url = this.url + "/yjtoa/s/signins/attendances";
            HashMap<String, Object> fields = new HashMap<String, Object>() {
                {
                    put("descColor", 0);
                    put("iccId", iccid);
                    put("id", 0);
                    put("positionData", positionData);
                    put("positionDescription", positionDescription);
                    put("resultColor", 0);
                    put("signResult", 0);
                    put("type", "VISIT");
                    put("userId", userid);
                }
            };

            String body = (new JSONObject(fields)).toString();

            try {
                HttpResponse<JsonNode> response = Unirest.post(url)
                        .headers(this.headers)
                        .body(body)
                        .asJson();
                if (response.getCode() == 200) {
                    JSONObject j = response.getBody().getObject();
                    return j.getJSONObject("payload").getString("signResultDesc");
                }

            } catch (Exception e) {
                return e.getMessage();
            } finally {
            }
        }

        return "error";
    }

}
