package com.blinkcoder.kit;

import com.blinkcoder.common.myConstants;
import com.jfinal.core.JFinal;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.struts.StrutsLinkTool;
import org.apache.velocity.tools.view.context.ViewContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-23
 * Time: 下午6:25
 */
public class LinkKit extends StrutsLinkTool {

    private VelocityContext velocity;

    public void init(Object obj) {
        super.init(obj);
        if (obj instanceof ViewContext) {
            ViewContext viewContext = (ViewContext) obj;
            velocity = (VelocityContext) viewContext.getVelocityContext();
        }
    }

    public String param(String name, String... def_value) {
        String v = request.getParameter(name);
        if (v == null) {
            String target = request.getRequestURI();
            String contextPath = request.getContextPath();
            int contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length());
            if (contextPathLength != 0)
                target = target.substring(contextPathLength);
            String[] paths = target.split("/");
            Map paramMap = VelocityKit.GetUrlParam(paths, paths.length);
            if (MapUtils.isNotEmpty(paramMap)) {
                v = (String) paramMap.get(name);
            }
        }
        return (v != null) ? v : ((def_value.length > 0) ? def_value[0] : null);
    }

    public long param(String name, long def_value) {
        return NumberUtils.toLong(param(name), def_value);
    }

    public int param(String name, int def_value) {
        return NumberUtils.toInt(param(name), def_value);
    }

    public byte param(String name, byte def_value) {
        return (byte) NumberUtils.toInt(param(name), def_value);
    }

    public String[] params(String name) {
        return request.getParameterValues(name);
    }

    public long[] lparams(String name) {
        String[] values = params(name);
        if (values == null) return null;
        List<Long> lvs = new ArrayList<Long>();
        for (String v : values) {
            long lv = NumberUtils.toLong(v, Long.MIN_VALUE);
            if (lv != Long.MIN_VALUE && !lvs.contains(lvs))
                lvs.add(lv);
        }
        long[] llvs = new long[lvs.size()];
        for (int i = 0; i < lvs.size(); i++)
            llvs[i] = lvs.get(i);
        return llvs;
    }

    public String urlPara() {
        String[] urlPara = new String[1];
        JFinal.me().getAction(request.getRequestURI(), urlPara);
        return urlPara[0] == null ? "" : urlPara[0];
    }

    public String this_vm() {
        return velocity.getCurrentTemplateName();
    }

    public static String root() {
        return root("/");
    }

    public static String root(String uri) {
        StringBuilder root = new StringBuilder(JFinal.me().getContextPath());
        if (uri.length() > 0 && uri.charAt(0) != '/')
            root.append('/');
        root.append(uri);
        return root.toString();
    }

    public static String action(String uri) {
        if (uri.length() > 0 && uri.charAt(0) != '/')
            return root("/action/" + uri);
        return root("/action" + uri);
    }

    public static String catalog(String uri) {
        return root("catalog/" + uri);
    }

    public static String label(String uri) {
        return root("label/" + uri);
    }

    public boolean is_catalog_list() {
        return request.getRequestURI().startsWith("/catalog/");
    }

    public boolean is_label_list() {
        return request.getRequestURI().startsWith("/label/");
    }

    public boolean is_blog_detail() {
        return !is_catalog_list() && !is_label_list() && param("p1") != null;
    }

    public boolean startWith(String str, String prefix) {
        return str.startsWith(prefix);
    }

    public void redirect(String url) throws IOException {
        response.sendRedirect(url);
    }

    public static String cloud_res(String name) {
        if (name == null)
            return null;
        if (StringUtils.isNotEmpty(myConstants.STATIC_RESOURCE_PATH)) {
            StringBuilder sb;
            sb = new StringBuilder(myConstants.STATIC_RESOURCE_PATH);
            if (!name.startsWith("/"))
                sb.append('/');
            sb.append(name);
            return sb.toString();
        } else {
            return local_res(name);
        }
    }

    public static String local_res(String name) {
        return root(name);
    }


}