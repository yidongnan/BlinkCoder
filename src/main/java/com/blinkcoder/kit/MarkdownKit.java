package com.blinkcoder.kit;

import org.apache.log4j.Logger;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-2-10
 * Time: 下午6:20
 */
public class MarkdownKit {
    private static final Logger logger = Logger.getLogger(MarkdownKit.class);

    private static final Markdown4jProcessor processor = new Markdown4jProcessor();

    public static String parse(String content) {
        try {
            return processor.process(content);
        } catch (IOException e) {
            logger.error("Markdown Parse Error!");
            return "";
        }
    }
}
