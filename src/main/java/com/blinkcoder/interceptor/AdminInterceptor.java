package com.blinkcoder.interceptor;

import com.blinkcoder.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

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
        User user = controller.getSessionAttr("user");
        if (user != null) {
            ai.invoke();
        } else {
            controller.setAttr("msg", "需要管理员权限");
            controller.renderError(500);
        }
    }
}