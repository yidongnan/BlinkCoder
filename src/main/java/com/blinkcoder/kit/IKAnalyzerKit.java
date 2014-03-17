package com.blinkcoder.kit;

import com.jfinal.log.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-3-3
 * Time: 下午2:09
 */
public class IKAnalyzerKit {

    private final static Logger log = Logger.getLogger(IKAnalyzerKit.class);

    @SuppressWarnings("unchecked")
    private final static List<String> nowords = new ArrayList<String>() {{
        try {
            addAll(IOUtils.readLines(IKAnalyzerKit.class.getResourceAsStream
                    ("/stopword.dic")));
        } catch (IOException e) {
            log.error("Unabled to read stopword file", e);
        }
    }};

    @SuppressWarnings("unchecked")
    private final static List<String> ReserveKeys = new ArrayList<String>() {{
        try {
            addAll(IOUtils.readLines(IKAnalyzerKit.class.getResourceAsStream
                    ("/keywords.dic")));
        } catch (IOException e) {
            log.error("Unabled to read keywords file", e);
        }
    }};

    public static String cleanupKey(String key) {
        if (ReserveKeys.contains(key.trim().toLowerCase()))
            return key;

        StringBuilder sb = new StringBuilder();
        List<String> keys = splitKeywords(key);
        for (String word : keys) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(word);
        }

        return sb.toString();
    }

    public static List<String> splitKeywords(String sentence) {

        List<String> keys = new ArrayList<>();

        if (StringUtils.isNotBlank(sentence)) {
            StringReader reader = new StringReader(sentence);
            IKSegmenter ikseg = new IKSegmenter(reader, true);
            try {
                do {
                    Lexeme me = ikseg.next();
                    if (me == null)
                        break;
                    String term = me.getLexemeText();
                    if (nowords.contains(term.toLowerCase()))
                        continue;
                    keys.add(term);
                } while (true);
            } catch (IOException e) {
                log.error("Unable to split keywords", e);
            }
        }

        return keys;
    }

}
