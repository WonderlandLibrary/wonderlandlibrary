// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.nosql;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultNoSqlObject implements NoSqlObject<Map<String, Object>>
{
    private final Map<String, Object> map;
    
    public DefaultNoSqlObject() {
        this.map = new HashMap<String, Object>();
    }
    
    @Override
    public void set(final String field, final Object value) {
        this.map.put(field, value);
    }
    
    @Override
    public void set(final String field, final NoSqlObject<Map<String, Object>> value) {
        this.map.put(field, value.unwrap());
    }
    
    @Override
    public void set(final String field, final Object[] values) {
        this.map.put(field, Arrays.asList(values));
    }
    
    @Override
    public void set(final String field, final NoSqlObject<Map<String, Object>>[] values) {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(values.length);
        for (final NoSqlObject<Map<String, Object>> value : values) {
            list.add(value.unwrap());
        }
        this.map.put(field, list);
    }
    
    @Override
    public Map<String, Object> unwrap() {
        return this.map;
    }
}
