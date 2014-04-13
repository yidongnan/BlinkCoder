package com.blinkcoder.controller;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.render.VelocityToolboxRender;
import com.jfinal.core.Controller;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Michael Date: 13-10-17 Time: 上午7:34
 */
public class MyController extends Controller {

	public static MyController me;

	@Override
	protected void init(HttpServletRequest request,
			HttpServletResponse response, String urlPara) {
		super.init(request, response, urlPara);
		me = this;
	}
	
	
	//渲染为Velocity视图，并返回给客户端
	@Override
	public void renderVelocity(String view) {
		//为客户端返回一个指定的页面视图
		render(new VelocityToolboxRender(myConstants.VELOCITY_TEMPLETE_PATH
				+ view));
	}

	public String ip() {
		//获取客户端的请求头，获取ip地址
		String ip = getRequest().getHeader("X-Forwarded-For");
		//判断ip地址是否为空
		if (StringUtils.isNotBlank(ip)) {
			//把获取的ip字符串分割为一个字符串数组
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip))
						continue;
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !tmpip.startsWith("10.")
							&& !tmpip.startsWith("192.168" + ".")
							&& !"127.0.0.1".equals(tmpip)) {
						return tmpip.trim();
					}
				}
			}
		}
		//获取客户端真实的ip地址
		ip = getRequest().getHeader("x-real-ip");
		if (isIPAddr(ip))
			return ip;
		
		//获取客户端的ip地址
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
		//判断地址是否为空
		if (StringUtils.isEmpty(addr))
			return false;
		String[] ips = StringUtils.split(addr, '.');	//把给定的ip字符串分割
		//判断分割后的ip组成的字符数组长度是否为空。
		if (ips.length != 4)
			return false;
		try {
			int ipa = Integer.parseInt(ips[0]);	//把ip数组的第一个元素转化为int类型
			int ipb = Integer.parseInt(ips[1]);//。。。。
			int ipc = Integer.parseInt(ips[2]);//。。。。
			int ipd = Integer.parseInt(ips[3]);//。。。。。
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
					&& ipc <= 255 && ipd >= 0 && ipd <= 255;
		} catch (Exception e) {
		}
		return false;
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
}