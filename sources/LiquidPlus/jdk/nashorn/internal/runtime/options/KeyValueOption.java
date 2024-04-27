/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.options;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import jdk.nashorn.internal.runtime.options.Option;

public class KeyValueOption
extends Option<String> {
    protected Map<String, String> map;

    KeyValueOption(String value) {
        super(value);
        this.initialize();
    }

    Map<String, String> getValues() {
        return Collections.unmodifiableMap(this.map);
    }

    public boolean hasValue(String key) {
        return this.map != null && this.map.get(key) != null;
    }

    String getValue(String key) {
        if (this.map == null) {
            return null;
        }
        String val = this.map.get(key);
        return "".equals(val) ? null : val;
    }

    private void initialize() {
        if (this.getValue() == null) {
            return;
        }
        this.map = new LinkedHashMap<String, String>();
        StringTokenizer st = new StringTokenizer((String)this.getValue(), ",");
        while (st.hasMoreElements()) {
            String token = st.nextToken();
            String[] keyValue = token.split(":");
            if (keyValue.length == 1) {
                this.map.put(keyValue[0], "");
                continue;
            }
            if (keyValue.length == 2) {
                this.map.put(keyValue[0], keyValue[1]);
                continue;
            }
            throw new IllegalArgumentException(token);
        }
    }
}

