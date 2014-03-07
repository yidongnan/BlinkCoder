package com.blinkcoder.kit;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-12-9
 * Time: 上午10:40
 */
public class FormatKit {

    /**
     * 删除html中多余的空白
     *
     * @param html
     * @return
     */
    public static String trim_html(String html) {
        return StringUtils.replace(StringUtils.replace(html, "\r\n", ""),
                "\t", "");
    }

    /**
     * 格式化HTML文本
     *
     * @param context
     * @return
     */
    public static String text(String context) {
        if (context == null)
            return "";
        String html = StringUtils.replace(context, "<", "&lt;");
        return StringUtils.replace(html, ">", "&gt;");
    }

    /**
     * 格式化HTML文本
     *
     * @param context
     * @return
     */
    public static String html(String context) {
        if (context == null)
            return "";
        String html = context;
        html = StringUtils.replace(html, "'", "&apos;");
        html = StringUtils.replace(html, "\"", "&quot;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    /**
     * 格式化HTML文本
     *
     * @param content
     * @return
     */
    public static String rhtml(String content) {
        if (StringUtils.isBlank(content))
            return content;
        String html = content;
        html = StringUtils.replace(html, "&", "&amp;");
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    public static String plain_text(String html) {
        if (StringUtils.isBlank(html))
            return "";
        return Jsoup.parse(html).text();
    }

    /**
     * 字符串智能截断
     *
     * @param str
     * @param maxWidth
     * @return
     */
    public static String abbreviate(String str, int maxWidth) {
        if (str == null) return null;
        if (str.length() <= maxWidth) return str;
        StringBuilder buf = new StringBuilder();
        int len = str.length();
        int wc = 0;
        int ncount = 2 * maxWidth - 3;
        for (int i = 0; i < len; ) {
            if (wc >= ncount) break;
            char ch = str.charAt(i++);
            buf.append(ch);
            wc += 2;
            if (wc >= ncount) break;
            if (CharUtils.isAscii(ch)) {
                wc -= 1;
                if (i >= len) break;
                char nch = str.charAt(i++);
                buf.append(nch);
                if (!CharUtils.isAscii(nch))
                    wc += 2;
                else
                    wc += 1;
            }
        }
        buf.append("...");
        return buf.toString();
    }
}
