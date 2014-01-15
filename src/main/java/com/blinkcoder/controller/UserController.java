package com.blinkcoder.controller;

import com.blinkcoder.model.User;

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
            setSessionAttr("user", user);
            redirect("/admin");
        }
    }

    public void logout() {
        removeSessionAttr("user");
        redirect("/admin/login");
    }
}
