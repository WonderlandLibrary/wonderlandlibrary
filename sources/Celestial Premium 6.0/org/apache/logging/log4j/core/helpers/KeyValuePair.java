/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="KeyValuePair", category="Core", printObject=true)
public class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }

    @PluginFactory
    public static KeyValuePair createPair(@PluginAttribute(value="key") String key, @PluginAttribute(value="value") String value) {
        return new KeyValuePair(key, value);
    }
}

