/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.RandomAccessFileManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;

@Plugin(name="RandomAccessFile", category="Core", elementType="appender", printObject=true)
public final class RandomAccessFileAppender
extends AbstractOutputStreamAppender {
    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;

    private RandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RandomAccessFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            HashMap<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.fileName = filename;
        this.advertiser = advertiser;
    }

    @Override
    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }

    @Override
    public void append(LogEvent event) {
        ((RandomAccessFileManager)this.getManager()).setEndOfBatch(event.isEndOfBatch());
        super.append(event);
    }

    public String getFileName() {
        return this.fileName;
    }

    @PluginFactory
    public static RandomAccessFileAppender createAppender(@PluginAttribute(value="fileName") String fileName, @PluginAttribute(value="append") String append, @PluginAttribute(value="name") String name, @PluginAttribute(value="immediateFlush") String immediateFlush, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="advertise") String advertise, @PluginAttribute(value="advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
        RandomAccessFileManager manager;
        boolean isAppend = Booleans.parseBoolean(append, true);
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        if (name == null) {
            LOGGER.error("No name provided for FileAppender");
            return null;
        }
        if (fileName == null) {
            LOGGER.error("No filename provided for FileAppender with name " + name);
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        if ((manager = RandomAccessFileManager.getFileManager(fileName, isAppend, isFlush, advertiseURI, layout)) == null) {
            return null;
        }
        return new RandomAccessFileAppender(name, layout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}

