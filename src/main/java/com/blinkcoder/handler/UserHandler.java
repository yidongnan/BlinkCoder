package com.blinkcoder.handler;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.kit.DesKit;
import com.blinkcoder.model.User;
import com.jfinal.handler.Handler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 2014/4/6
 * Time: 9:01
 */
public class UserHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (target.contains(".")) {
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if ("blinkcoder".equals(cookie.getName())) {
                    String loginKey = cookie.getValue();
                    String key = null;
                    try {
                        key = new String(DesKit.decrypt(Base64.decodeBase64(URLDecoder.decode(loginKey, "UTF-8")),
                                myConstants.COOKIE_ENCRYPT_KEY));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (StringUtils.isNotEmpty(key) && key.contains("|")) {
                        String[] fieldArray = key.split("\\|");
                        int id = Integer.parseInt(fieldArray[0]);
                        String password = fieldArray[1];
                        User user = User.dao.Get(id);
                        if (user.get("password").equals(password)) {
                            request.setAttribute("g_user", user);
                        }
                    }
                    break;
                }
            }
        }
        nextHandler.handle(target, request, response, isHandled);
    }
}
