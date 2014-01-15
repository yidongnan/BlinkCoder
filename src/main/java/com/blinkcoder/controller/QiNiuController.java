package com.blinkcoder.controller;


import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.service.QiNiu;
import com.jfinal.aop.Before;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-1-6
 * Time: 下午10:15
 */
public class QiNiuController extends MyController {

    @Before(AdminInterceptor.class)
    public void token() {
        renderJson("token", QiNiu.token());
    }

    @Before(AdminInterceptor.class)
    public void callback() {
        String upload_ret = getPara("upload_ret");
        renderJson(QiNiu.callbackUEditor(upload_ret));
    }

    @Before(AdminInterceptor.class)
    public void imageManager() {
        renderText(QiNiu.imageManager());
    }
}
