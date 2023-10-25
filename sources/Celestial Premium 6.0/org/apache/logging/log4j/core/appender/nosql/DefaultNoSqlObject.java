/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.nosql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.nosql.NoSqlObject;

public class DefaultNoSqlObject
implements NoSqlObject<Map<String, Object>> {
    private final Map<String, Object> map = new HashMap<String, Object>();

    @Override
    public void set(String field, Object value) {
        this.map.put(field, value);
    }

    @Override
    public void set(String field, NoSqlObject<Map<String, Object>> value) {
        this.map.put(field, value.unwrap());
    }

    @Override
    public void set(String field, Object[] values) {
        this.map.put(field, Arrays.asList(values));
    }

    @Override
    public void set(String field, NoSqlObject<Map<String, Object>>[] values) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(values.length);
        for (NoSqlObject<Map<String, Object>> value : values) {
            list.add(value.unwrap());
        }
        this.map.put(field, list);
    }

    @Override
    public Map<String, Object> unwrap() {
        return this.map;
    }
}

