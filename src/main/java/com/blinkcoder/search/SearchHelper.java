package com.blinkcoder.search;

import com.jfinal.log.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-2-28
 * Time: 下午4:34
 */
public class SearchHelper {

    private final static Logger log = Logger.getLogger(SearchHelper.class);
    private final static IKAnalyzer analyzer = new IKAnalyzer();
    private final static int MAX_COUNT = 1000;
    private String indexPath;

    public final static String FN_ID = "___id";
    public final static String FN_CLASSNAME = "___class";

    public static SearchHelper init(String indexPath) throws
            FileNotFoundException {
        SearchHelper searchHelper = new SearchHelper();
        indexPath = FilenameUtils.normalize(indexPath);
        File file = new File(indexPath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (!indexPath.endsWith(File.separator)) {
            indexPath += File.separator;
        }
        searchHelper.indexPath = indexPath;
        return searchHelper;
    }

    private IndexWriter getWriter(Class<? extends Searchable> objClass)
            throws IOException {
        Directory dir = FSDirectory.open(new File(indexPath + objClass
                .getSimpleName()));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
                analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(dir, config);
    }


    private IndexSearcher getSearcher(Class<? extends Searchable> objClass)
            throws IOException {
        Directory dir = FSDirectory.open(new File(indexPath + objClass
                .getSimpleName()));
        return new IndexSearcher(DirectoryReader.open(dir));
    }


    private IndexSearcher getSearchers(List<Class<? extends Searchable>>
                                               objClasses) throws IOException {
        IndexReader[] readers = new IndexReader[objClasses.size()];
        int idx = 0;
        for (Class<? extends Searchable> objClass : objClasses) {
            FSDirectory dir = FSDirectory.open(new File(indexPath + objClass
                    .getSimpleName()));
            readers[idx++] = DirectoryReader.open(dir);
        }
        return new IndexSearcher(new MultiReader(readers, true));
    }

    /**
     * 优化索引库
     *
     * @param objClass
     * @throws IOException
     */
    public void optimize(Class<? extends Searchable> objClass) throws
            IOException {
        IndexWriter writer = getWriter(objClass);
        try {
            writer.forceMerge(1);
            writer.commit();
        } finally {
            writer.close();
            writer = null;
        }
    }

    /**
     * 多库搜索
     *
     * @param objClasses
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public List<Searchable> find(List<Class<? extends Searchable>>
                                         objClasses, Query query,
                                 Filter filter, Sort sort, int page,
                                 int count) throws IOException {
        IndexSearcher searcher = getSearchers(objClasses);
        return find(searcher, query, filter, sort, page, count);
    }

    /**
     * 单库搜索
     *
     * @param objClass
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public List<Integer> find(Class<? extends Searchable> objClass,
                              Query query, Filter filter, Sort sort,
                              int page, int count) throws IOException {
        IndexSearcher searcher = getSearcher(objClass);
        List<Searchable> results = find(searcher, query, filter, sort, page,
                count);
        List<Integer> ids = new ArrayList<>();
        for (Searchable obj : results) {
            if (obj != null)
                ids.add(obj.getId());
        }
        return ids;
    }

    /**
     * 多库搜索
     *
     * @param objClasses
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    public int count(List<Class<? extends Searchable>> objClasses,
                     Query query, Filter filter) throws IOException {
        IndexSearcher searcher = getSearchers(objClasses);
        return count(searcher, query, filter);
    }

    /**
     * 搜索
     *
     * @param beanClass
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    public int count(Class<? extends Searchable> objClass, Query query,
                     Filter filter) throws IOException {
        IndexSearcher searcher = getSearcher(objClass);
        return count(searcher, query, filter);
    }

    /**
     * 搜索
     *
     * @param searcher
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    private List<Searchable> find(IndexSearcher searcher, Query query,
                                  Filter filter, Sort sort, int page,
                                  int count) throws IOException {
        try {
            TopDocs hits;
            if (filter != null && sort != null)
                hits = searcher.search(query, filter, MAX_COUNT, sort);
            else if (filter != null)
                hits = searcher.search(query, filter, MAX_COUNT);
            else if (sort != null)
                hits = searcher.search(query, MAX_COUNT, sort);
            else
                hits = searcher.search(query, MAX_COUNT);
            if (hits == null) return null;
            List<Searchable> results = new ArrayList<>();
            int nBegin = (page - 1) * count;
            int nEnd = Math.min(nBegin + count, hits.scoreDocs.length);
            for (int i = nBegin; i < nEnd; i++) {
                ScoreDoc s_doc = hits.scoreDocs[i];
                Document doc = searcher.doc(s_doc.doc);

                Searchable obj = doc2obj(doc);

                if (obj != null && !results.contains(obj)) {
                    results.add(obj);
                }
            }
            return results;

        } catch (IOException e) {
            log.error("Unabled to find via query: " + query, e);
        }
        return null;
    }

    /**
     * 根据查询条件统计搜索结果数
     *
     * @param searcher
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    private int count(IndexSearcher searcher, Query query,
                      Filter filter) throws IOException {
        try {
            TotalHitCountCollector thcc = new TotalHitCountCollector();
            if (filter != null)
                searcher.search(query, filter, thcc);
            else
                searcher.search(query, thcc);
            return Math.min(MAX_COUNT, thcc.getTotalHits());
        } catch (IOException e) {
            log.error("Unabled to find via query: " + query, e);
            return -1;
        }
    }

    /**
     * 批量添加索引
     *
     * @param docs
     * @throws IOException
     */
    public int add(List<? extends Searchable> objs) throws IOException {
        if (objs == null || objs.size() == 0)
            return 0;
        int doc_count = 0;
        IndexWriter writer = getWriter(objs.get(0).getClass());
        try {
            for (Searchable obj : objs) {
                Document doc = obj2doc(obj);
                writer.addDocument(doc);
                doc_count++;
            }
            writer.commit();
        } finally {
            writer.close();
            writer = null;
        }
        return doc_count;
    }

    /**
     * 批量删除索引
     *
     * @param docs
     * @throws IOException
     */
    public int delete(List<? extends Searchable> objs) throws IOException {
        if (objs == null || objs.size() == 0)
            return 0;
        int doc_count = 0;
        IndexWriter writer = getWriter(objs.get(0).getClass());
        try {
            for (Searchable obj : objs) {
                writer.deleteDocuments(new Term("id",
                        String.valueOf(obj.getId())));
                doc_count++;
            }
            writer.commit();
        } finally {
            writer.close();
            writer = null;
        }
        return doc_count;
    }

    /**
     * 批量更新索引
     *
     * @param docs
     * @throws IOException
     */
    public void update(List<? extends Searchable> objs) throws IOException {
        delete(objs);
        add(objs);
    }

    public static Searchable doc2obj(Document doc) {
        try {
            int id = NumberUtils.toInt(doc.get(FN_ID), 0);
            if (id <= 0)
                return null;
            Searchable obj = (Searchable) Class.forName(doc.get(FN_CLASSNAME)
            ).newInstance();
            obj.setId(id);
            return obj;
        } catch (Exception e) {
            log.error("Unabled generate object from document#id=" + doc
                    .toString(), e);
            return null;
        }
    }

    private static Field obj2field(String field, Object fieldValue,
                                   boolean store) {
        if (fieldValue == null)
            return null;
        if (fieldValue instanceof Date) //日期
            return new LongField(field, ((Date) fieldValue).getTime(),
                    store ? Field.Store.YES : Field.Store.NO);
        if (fieldValue instanceof Number) //其他数值
            return new StringField(field, String.valueOf(((Number)
                    fieldValue).longValue()), store ? Field.Store.YES : Field
                    .Store.NO);
        //其他默认当字符串处理
        return new StringField(field, (String) fieldValue,
                store ? Field.Store.YES : Field.Store.NO);
    }

    private static Document obj2doc(Searchable obj) {
        if (obj == null)
            return null;
        Document doc = new Document();
        doc.add(new IntField(FN_ID, obj.getId(), Field.Store.YES));
        doc.add(new StoredField(FN_CLASSNAME, obj.getClass().getName()));

        //存储字段
        List<String> fields = new ArrayList<>();
        Map<String, Object> eDatas = obj.storeDatas();
        if (eDatas != null)
            for (String fn : eDatas.keySet()) {
                if (fields.contains(fn))
                    continue;
                Object fv = eDatas.get(fn);
                if (fv != null)
                    doc.add(obj2field(fn, fv, true));
            }

        //索引字段
        eDatas = obj.indexDatas();
        if (eDatas != null)
            for (String fn : eDatas.keySet()) {
                if (fields.contains(fn))
                    continue;
                String fv = eDatas.get(fn).toString();
                if (fv != null) {
                    TextField tf = new TextField(fn, fv, Field.Store.NO);
                    tf.setBoost(obj.boost());
                    doc.add(tf);
                }
            }

        return doc;
    }

    public static Query makeQuery(String field, String q, float boost) {
        if (StringUtils.isBlank(q) || StringUtils.isBlank(field))
            return new BooleanQuery();
        QueryParser parser = new QueryParser(Version.LUCENE_41, field,
                analyzer);
        parser.setDefaultOperator(QueryParser.AND_OPERATOR);
        try {
            Query querySinger = parser.parse(q);
            querySinger.setBoost(boost);
            return querySinger;
        } catch (Exception e) {
            TermQuery queryTerm = new TermQuery(new Term(field, q));
            queryTerm.setBoost(boost);
            return queryTerm;
        }
    }
}
