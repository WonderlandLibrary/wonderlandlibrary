// 
// Decompiled by Procyon v0.6.0
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
        this.map.put(field, (value != null) ? value.unwrap() : null);
    }
    
    @Override
    public void set(final String field, final Object[] values) {
        this.map.put(field, (values != null) ? Arrays.asList(values) : null);
    }
    
    @Override
    public void set(final String field, final NoSqlObject<Map<String, Object>>[] values) {
        if (values == null) {
            this.map.put(field, null);
        }
        else {
            final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(values.length);
            for (final NoSqlObject<Map<String, Object>> value : values) {
                list.add(value.unwrap());
            }
            this.map.put(field, list);
        }
    }
    
    @Override
    public Map<String, Object> unwrap() {
        return this.map;
    }
}
