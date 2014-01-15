package com.blinkcoder.kit;

import com.blinkcoder.common.myConstants;
import com.jfinal.core.JFinal;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-12-17
 * Time: 下午11:22
 */
public class VelocityKit {
    private final static String VM_EXT = ".vm";
    private final static String VM_INDEX = "/index" + VM_EXT;
    private final static List<String> vm_cache = new Vector<String>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String _MakeQueryString(String[] paths, int idx_base) {
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
        sb.append("--------------------------------------------------------------------------------\n");
        System.out.println(sb);
        return params.toString();
    }

    private static Map<String, String> _MakeQueryMap(String[] paths, int idx_base) {
        Map<String, String> paramMap = new HashMap<String, String>();
        int idx = 1;
        for (int i = idx_base; i < paths.length; i++) {
            paramMap.put("p" + idx++, paths[i]);
        }
        return paramMap;
    }

    /**
     * 判断某个页面是否存在，如果存在则缓存此结果
     *
     * @param path
     * @return
     */
    public static boolean _IsVmExist(String path) {
        if (vm_cache.contains(path)) {
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ").append(sdf.format(new Date())).append(" ------------------------------\n");
            sb.append("Velocity Template File : ").append(path);
            System.out.println(sb);
            return true;
        }
        File testFile = new File(JFinal.me().getServletContext().getRealPath(path));
        boolean isVM = testFile.exists() && testFile.isFile();
        if (isVM) {
            vm_cache.add(path);
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ").append(sdf.format(new Date())).append(" ------------------------------\n");
            sb.append("Velocity Template File : ").append(path);
            System.out.println(sb);
        }
        return isVM;
    }

    public static String GetTemplate(String[] paths, int length) {
        StringBuilder vm = new StringBuilder(myConstants.VELOCITY_TEMPLETE_PATH);

        if (length == 0) {
            StringBuilder sb = new StringBuilder("\nJFinal Velocity Template report -------- ").append(sdf.format(new Date())).append(" ------------------------------\n");
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
        String view = GetTemplate(paths, length - 1);
        return view;
    }

    public static Map GetUrlParam(String[] paths, int length) {
        StringBuilder vm = new StringBuilder(myConstants.VELOCITY_TEMPLETE_PATH);

        if (length == 0) {
            return null;
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
            return _MakeQueryMap(paths, length);

        the_path += VM_INDEX;

        if (_IsVmExist(the_path))
            return _MakeQueryMap(paths, length);

        vms += VM_EXT;
        if (_IsVmExist(vms))
            return _MakeQueryMap(paths, length);
        return GetUrlParam(paths, length - 1);
    }

}
