package com.blinkcoder.controller;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.render.VelocityToolboxRender;
import com.jfinal.core.Controller;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public String ip() {
        String ip = getRequest().getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = StringUtils.split(ip, ',');
            if (ips != null) {
                for (String tmpip : ips) {
                    if (StringUtils.isBlank(tmpip))
                        continue;
                    tmpip = tmpip.trim();
                    if (isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip
                            .startsWith("192.168" +
                                    ".") && !"127.0.0.1".equals(tmpip)) {
                        return tmpip.trim();
                    }
                }
            }
        }
        ip = getRequest().getHeader("x-real-ip");
        if (isIPAddr(ip))
            return ip;
        ip = getRequest().getRemoteAddr();
        if (ip.indexOf('.') == -1)
            ip = "127.0.0.1";
        return ip;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr
     * @return
     */
    public boolean isIPAddr(String addr) {
        if (StringUtils.isEmpty(addr))
            return false;
        String[] ips = StringUtils.split(addr, '.');
        if (ips.length != 4)
            return false;
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
        }
        return false;
    }
}