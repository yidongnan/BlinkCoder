package com.blinkcoder.kit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-1-11
 * Time: 下午8:40
 */
public class PropertyKit {

    /**
     * properties字符串转成properties对象
     *
     * @param p_args
     * @param args
     * @throws java.io.IOException
     */
    public static Properties stringToProperties(String p_args) {
        if (StringUtils.isBlank(p_args))
            return null;

        BufferedReader reader = new BufferedReader(new StringReader(p_args));
        try {
            Properties args = new Properties();
            do {
                String line = reader.readLine();
                if (line == null)
                    break;
                try {
                    int idx = line.indexOf('=');
                    if (idx > 0) {
                        String key = line.substring(0, idx);
                        String value = line.substring(idx + 1);
                        args.put(key, value);
                    }
                } catch (Exception e) {
                }
            } while (true);

            return args;
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Properties To String
     *
     * @param args
     * @return
     * @throws IOException
     */
    public static String propertiesToString(Properties args) {
        if (args == null || args.size() == 0)
            return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            boolean has_write = false;
            for (Object key : args.keySet()) {
                if (has_write)
                    baos.write(new byte[]{'\r', '\n'});
                String skey = (String) key;
                String line = skey + '=' + args.getProperty(skey);
                baos.write(line.getBytes());
                has_write = true;
            }
            // args.store(baos, null);
            return baos.toString();
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
            baos = null;
        }
    }

    /**
     * 加载properties资源文件
     *
     * @param loader
     * @param resource
     * @return
     */
    public static Properties loadFromResource(Class<?> loader,
                                              String resource) {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = (loader == null) ? PropertyKit.class.getResourceAsStream
                    (resource) : loader
                    .getResourceAsStream(resource);
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            return stringToProperties(IOUtils.toString(reader));
        } catch (Exception excp) {
            throw new RuntimeException(excp);
        } finally {
            IOUtils.closeQuietly(in);
            reader = null;
        }
    }

    /**
     * 从文件中加载
     *
     * @param file
     * @return
     */
    public static Properties loadFromResource(File file) throws IOException {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            return stringToProperties(IOUtils.toString(reader));
        } finally {
            IOUtils.closeQuietly(in);
            reader = null;
        }
    }

    /**
     * 保存Props到文件中
     *
     * @param props
     * @param path
     * @throws IOException
     * @date 2008-9-5
     * @author eric.chan
     */
    public static void savePropertiesToFile(Properties props, String path)
            throws IOException {
        FileOutputStream fos = null;
        try {
            File f = new File(path);
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            fos = new FileOutputStream(path);
            props.store(fos, null);
        } finally {
            props = null;
            if (fos != null)
                fos.close();
        }
    }
}
