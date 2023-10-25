/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;

@Plugin(name="File", category="Core", elementType="appender", printObject=true)
public final class FileAppender
extends AbstractOutputStreamAppender {
    private final String fileName;
    private final Advertiser advertiser;
    private Object advertisement;

    private FileAppender(String name, Layout<? extends Serializable> layout, Filter filter, FileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
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

    public String getFileName() {
        return this.fileName;
    }

    @PluginFactory
    public static FileAppender createAppender(@PluginAttribute(value="fileName") String fileName, @PluginAttribute(value="append") String append, @PluginAttribute(value="locking") String locking, @PluginAttribute(value="name") String name, @PluginAttribute(value="immediateFlush") String immediateFlush, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginAttribute(value="bufferedIO") String bufferedIO, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="advertise") String advertise, @PluginAttribute(value="advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
        FileManager manager;
        boolean isAppend = Booleans.parseBoolean(append, true);
        boolean isLocking = Boolean.parseBoolean(locking);
        boolean isBuffered = Booleans.parseBoolean(bufferedIO, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        if (isLocking && isBuffered) {
            if (bufferedIO != null) {
                LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for " + fileName);
            }
            isBuffered = false;
        }
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
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
        if ((manager = FileManager.getFileManager(fileName, isAppend, isLocking, isBuffered, advertiseURI, layout)) == null) {
            return null;
        }
        return new FileAppender(name, layout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}

