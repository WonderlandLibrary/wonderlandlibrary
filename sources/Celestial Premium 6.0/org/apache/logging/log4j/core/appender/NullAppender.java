/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="Null", category="Core", elementType="appender", printObject=true)
public class NullAppender
extends AbstractAppender {
    public static final String PLUGIN_NAME = "Null";

    @PluginFactory
    public static NullAppender createAppender(@PluginAttribute(value="name", defaultString="null") String name) {
        return new NullAppender(name);
    }

    private NullAppender(String name) {
        super(name, null, null, true, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
    }
}

