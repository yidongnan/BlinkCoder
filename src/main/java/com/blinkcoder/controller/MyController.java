package com.blinkcoder.controller;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.kit.DesKit;
import com.blinkcoder.model.User;
import com.blinkcoder.render.VelocityToolboxRender;
import com.jfinal.core.Controller;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: Michael
 * Date: 13-10-17
 * Time: 上午7:34
 */
public class MyController extends Controller {

    public static MyController me;

    @Override
    protected void init(HttpServletRequest request,
                        HttpServletResponse response, String urlPara) {
        super.init(request, response, urlPara);
        me = this;
    }

    @Override
    public void renderVelocity(String view) {
        render(new VelocityToolboxRender(myConstants.VELOCITY_TEMPLETE_PATH +
                view));
    }

    public String header(String name) {
        return getRequest().getHeader(name);
    }

    public void header(String name, String value) {
        getResponse().setHeader(name, value);
    }

    public void header(String name, int value) {
        getResponse().setIntHeader(name, value);
    }

    public void header(String name, long value) {
        getResponse().setDateHeader(name, value);
    }

    public User loginUser() {
        return (User) getRequest().getAttribute("g_user");
    }

    public void saveUserInCookie(User user) {
        String loginKey = loginKey(user);
        removeCookie("blinkcoder");
        setCookie("blinkcoder", loginKey, 365 * 24 * 3600, "/");
    }

    private String loginKey(User user) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(user.get("id"));
            sb.append('|');
            sb.append(user.get("openid"));
            return URLEncoder.encode(new String(Base64.encodeBase64(DesKit.encrypt(sb.toString().getBytes(),
                    myConstants.COOKIE_ENCRYPT_KEY))), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}