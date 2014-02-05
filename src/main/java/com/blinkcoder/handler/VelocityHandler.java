package com.blinkcoder.handler;

import com.blinkcoder.common.myConstants;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-12-13
 * Time: 下午9:37
 */
public class VelocityHandler extends Handler {

    private final static String VM_EXT = ".vm";
    private final static String VM_INDEX = "/index" + VM_EXT;
    private final static List<String> vm_cache = new Vector<String>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static String Prefix = "Action/";

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response,
                       boolean[] isHandled) {
        if (target.indexOf(".") != -1) {
            return;
        }

        String[] urlPara = {null};
        if (!target.startsWith(Prefix)) {
            String[] paths = target.split("/");
            target = GetTemplate(request, paths, paths.length);
        }
        nextHandler.handle("/WEB-INF/www/index", request, response, isHandled);
    }

    private String _MakeQueryString(String[] paths, int idx_base) {
        StringBuilder params = new StringBuilder();
        int idx = 1;
        for (int i = idx_base; i < paths.length; i++) {
            if (params.length() == 0)
                params.append('?');
            if (i > idx_base)
                params.append('&');
            params.append("p");
            params.append(idx++);
            params.append('=');
            params.append(paths[i]);
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(params)) {
            sb.append("UrlPara     : ").append(params.substring(1)).append("\n");
        }
        sb.append
                ("--------------------------------------------------------------------------------\n");
        System.out.println(sb);
        return params.toString();
    }

    /**
     * 判断某个页面是否存在，如果存在则缓存此结果
     *
     * @param path
     * @return
     */
    private boolean _IsVmExist(String path) {
        if (vm_cache.contains(path)) {
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ")
                    .append(sdf.format(new Date())).append(" ------------------------------\n");
            sb.append("Velocity Template File : ").append(path);
            System.out.println(sb);
            return true;
        }
        File testFile = new File(JFinal.me().getServletContext().getRealPath(path));
        boolean isVM = testFile.exists() && testFile.isFile();
        if (isVM) {
            vm_cache.add(path);
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ")
                    .append(sdf.format(new Date())).append(" ------------------------------\n");
            sb.append("Velocity Template File : ").append(path);
            System.out.println(sb);
        }
        return isVM;
    }

    private String GetTemplate(HttpServletRequest request, String[] paths, int length) {
        StringBuilder vm = new StringBuilder(myConstants.VELOCITY_TEMPLETE_PATH);

        if (length == 0) {
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ")
                    .append(sdf.format(new Date())).append(" ------------------------------\n");
            sb.append("Velocity Template File : ").append(vm.toString() + VM_INDEX);
            System.out.println(sb);
            return vm.toString() + VM_INDEX + _MakeQueryString(paths, length);
        }

        for (int i = 0; i < length; i++) {
            if (!"".equals(paths[i])) {
                vm.append('/');
                vm.append(paths[i]);
            }
        }
        String vms = vm.toString();
        String the_path = vms;

        if (_IsVmExist(the_path + VM_EXT))
            return the_path + VM_EXT + _MakeQueryString(paths, length);

        the_path += VM_INDEX;

        if (_IsVmExist(the_path))
            return the_path + _MakeQueryString(paths, length);

        vms += VM_EXT;
        if (_IsVmExist(vms))
            return vms + _MakeQueryString(paths, length);
        String view = GetTemplate(request, paths, length - 1);
        return view;
    }
}
