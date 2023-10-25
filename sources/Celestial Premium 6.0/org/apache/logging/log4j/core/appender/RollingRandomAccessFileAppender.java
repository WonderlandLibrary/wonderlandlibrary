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
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;

@Plugin(name="RollingRandomAccessFile", category="Core", elementType="appender", printObject=true)
public final class RollingRandomAccessFileAppender
extends AbstractOutputStreamAppender {
    private final String fileName;
    private final String filePattern;
    private Object advertisement;
    private final Advertiser advertiser;

    private RollingRandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            HashMap<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.fileName = fileName;
        this.filePattern = filePattern;
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
        RollingRandomAccessFileManager manager = (RollingRandomAccessFileManager)this.getManager();
        manager.checkRollover(event);
        manager.setEndOfBatch(event.isEndOfBatch());
        super.append(event);
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePattern() {
        return this.filePattern;
    }

    @PluginFactory
    public static RollingRandomAccessFileAppender createAppender(@PluginAttribute(value="fileName") String fileName, @PluginAttribute(value="filePattern") String filePattern, @PluginAttribute(value="append") String append, @PluginAttribute(value="name") String name, @PluginAttribute(value="immediateFlush") String immediateFlush, @PluginElement(value="Policy") TriggeringPolicy policy, @PluginElement(value="Strategy") RolloverStrategy strategy, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filter") Filter filter, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginAttribute(value="advertise") String advertise, @PluginAttribute(value="advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
        RollingRandomAccessFileManager manager;
        boolean isAppend = Booleans.parseBoolean(append, true);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        if (name == null) {
            LOGGER.error("No name provided for FileAppender");
            return null;
        }
        if (fileName == null) {
            LOGGER.error("No filename was provided for FileAppender with name " + name);
            return null;
        }
        if (filePattern == null) {
            LOGGER.error("No filename pattern provided for FileAppender with name " + name);
            return null;
        }
        if (policy == null) {
            LOGGER.error("A TriggeringPolicy must be provided");
            return null;
        }
        if (strategy == null) {
            strategy = DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), config);
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        if ((manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(fileName, filePattern, isAppend, isFlush, policy, strategy, advertiseURI, layout)) == null) {
            return null;
        }
        return new RollingRandomAccessFileAppender(name, layout, filter, manager, fileName, filePattern, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}

