package com.blinkcoder.controller;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.kit.DesKit;
import com.blinkcoder.model.User;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class UserController extends MyController {

    public void login() {
        User loginUser = getModel(User.class);
        User user = User.dao.login(loginUser.getStr("username"), loginUser.getStr("password"));
        if (user == null)
            renderJson("msg", "登录失败，请确认是否输入正确的邮箱地址和密码");
        else {
            String loginKey = null;
            try {
                loginKey = loginKey(user, ip());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            removeCookie("blinkcoder");
            setCookie("blinkcoder", loginKey, 365 * 24 * 3600, "/");
            getRequest().setAttribute("g_user", user);
            redirect("/admin");
        }
    }

    private String loginKey(User user, String ip) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(user.get("id"));
        sb.append('|');
        sb.append(user.get("password"));
        byte[] data = Base64.encodeBase64(DesKit.encrypt(sb.toString().getBytes(), myConstants.COOKIE_ENCRYPT_KEY));
        return URLEncoder.encode(new String(data), "UTF-8");
    }

    public void logout() {
        removeCookie("sid");
        getRequest().removeAttribute("g_user");
        redirect("/admin/login");
    }
}
