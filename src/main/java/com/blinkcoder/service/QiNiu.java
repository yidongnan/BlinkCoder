package com.blinkcoder.service;

import com.alibaba.fastjson.JSONObject;
import com.blinkcoder.common.myConstants;
import com.jfinal.log.Logger;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.io.File;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-1-6
 * Time: 下午10:18
 */
public class QiNiu {

    public static final Logger log = Logger.getLogger(QiNiu.class);

    public static Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);

    public static String token() {
        PutPolicy putPolicy = new PutPolicy(myConstants.QINIU_BUICKET);
        putPolicy.returnUrl = myConstants.QINIU_RETURNURL;
        putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
        String token = null;
        try {
            token = putPolicy.token(mac);
        } catch (Exception e) {
            log.error("Qiniu uptoken get failed!");
        }
        return token;
    }

    public static JSONObject callbackUEditor(String upload_ret) {
        JSONObject callback = JSONObject.parseObject(StringUtils.newStringUtf8(Base64.decodeBase64(upload_ret)));
        JSONObject json = new JSONObject();
        if (callback.containsKey("error")) {
            json.put("state", callback.get("error"));
        } else {
            json.put("original", callback.get("name"));
            json.put("url", callback.get("key") + "-v001");
            json.put("state", "SUCCESS");
        }
        return json;
    }

    public static String imageManager() {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        RSFClient client = new RSFClient(mac);
        ListPrefixRet list = client.listPrifix("michaelchen", "", "", 10);
        StringBuffer sb = new StringBuffer();
        for (ListItem item : list.results) {
            sb.append("/");
            sb.append(item.key);
            sb.append("ue_separate_ue");
        }
        String imgStr = sb.toString();
        if (imgStr != "") {
            imgStr = imgStr.substring(0, imgStr.lastIndexOf("ue_separate_ue")).replace(File.separator, "/").trim();
        }
        return imgStr;
    }

}
