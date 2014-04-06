package com.blinkcoder.interceptor;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.kit.DesKit;
import com.blinkcoder.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-1-12
 * Time: 下午1:49
 */
public class AdminInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Controller controller = ai.getController();
        User user = (User) controller.getRequest().getAttribute("g_user");
        if (user != null) {
            // 管理员
            ai.invoke();
        } else {
            String loginKey = controller.getCookie("blinkcoder");
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
                user = User.dao.Get(id);
                if (user.get("password").equals(password)) {
                    ai.invoke();
                } else {
                    controller.setAttr("msg", "需要管理员权限");
                    controller.renderError(500);
                }

            }
        }
    }
}