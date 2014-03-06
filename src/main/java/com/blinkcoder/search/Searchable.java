package com.blinkcoder.search;

import java.util.Map;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-2-28
 * Time: 下午4:35
 */
public interface Searchable extends Comparable<Searchable> {

    public int getId();

    public void setId(int id);

    public float boost();

    public Map<String, Object> storeDatas();

    public Map<String, Object> indexDatas();
}
