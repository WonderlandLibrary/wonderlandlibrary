/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.MemoryMappedFileManager;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name="MemoryMappedFile", category="Core", elementType="appender", printObject=true)
public final class MemoryMappedFileAppender
extends AbstractOutputStreamAppender<MemoryMappedFileManager> {
    private static final int BIT_POSITION_1GB = 30;
    private static final int MAX_REGION_LENGTH = 0x40000000;
    private static final int MIN_REGION_LENGTH = 256;
    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;

    private MemoryMappedFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, MemoryMappedFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, (OutputStreamManager)manager);
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

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return true;
    }

    @Override
    public void append(LogEvent event) {
        ((MemoryMappedFileManager)this.getManager()).setEndOfBatch(event.isEndOfBatch());
        super.append(event);
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getRegionLength() {
        return ((MemoryMappedFileManager)this.getManager()).getRegionLength();
    }

    @Deprecated
    public static <B extends Builder<B>> MemoryMappedFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String regionLengthStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration config) {
        boolean isAppend = Booleans.parseBoolean(append, true);
        boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, false);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        int regionLength = Integers.parseInt(regionLengthStr, 0x2000000);
        return ((Builder)((Builder)((Builder)((Builder)((Builder)((Builder)((AbstractFilterable.Builder)((Builder)((AbstractAppender.Builder)((Builder)((Builder)((Builder)MemoryMappedFileAppender.newBuilder()).setAdvertise(isAdvertise)).setAdvertiseURI(advertiseURI)).setAppend(isAppend)).setConfiguration(config)).setFileName(fileName)).setFilter(filter)).setIgnoreExceptions(ignoreExceptions)).withImmediateFlush(isImmediateFlush)).setLayout(layout)).setName(name)).setRegionLength(regionLength)).build();
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private static int determineValidRegionLength(String name, int regionLength) {
        if (regionLength > 0x40000000) {
            LOGGER.info("MemoryMappedAppender[{}] Reduced region length from {} to max length: {}", name, regionLength, 0x40000000);
            return 0x40000000;
        }
        if (regionLength < 256) {
            LOGGER.info("MemoryMappedAppender[{}] Expanded region length from {} to min length: {}", name, regionLength, 256);
            return 256;
        }
        int result = Integers.ceilingNextPowerOfTwo(regionLength);
        if (regionLength != result) {
            LOGGER.info("MemoryMappedAppender[{}] Rounded up region length from {} to next power of two: {}", name, regionLength, result);
        }
        return result;
    }

    public static class Builder<B extends Builder<B>>
    extends AbstractOutputStreamAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<MemoryMappedFileAppender> {
        @PluginBuilderAttribute(value="fileName")
        private String fileName;
        @PluginBuilderAttribute(value="append")
        private boolean append = true;
        @PluginBuilderAttribute(value="regionLength")
        private int regionLength = 0x2000000;
        @PluginBuilderAttribute(value="advertise")
        private boolean advertise;
        @PluginBuilderAttribute(value="advertiseURI")
        private String advertiseURI;

        @Override
        public MemoryMappedFileAppender build() {
            String name = this.getName();
            int actualRegionLength = MemoryMappedFileAppender.determineValidRegionLength(name, this.regionLength);
            if (name == null) {
                LOGGER.error("No name provided for MemoryMappedFileAppender");
                return null;
            }
            if (this.fileName == null) {
                LOGGER.error("No filename provided for MemoryMappedFileAppender with name " + name);
                return null;
            }
            Layout<Serializable> layout = this.getOrCreateLayout();
            MemoryMappedFileManager manager = MemoryMappedFileManager.getFileManager(this.fileName, this.append, this.isImmediateFlush(), actualRegionLength, this.advertiseURI, layout);
            if (manager == null) {
                return null;
            }
            return new MemoryMappedFileAppender(name, layout, this.getFilter(), manager, this.fileName, this.isIgnoreExceptions(), false, this.advertise ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray());
        }

        public B setFileName(String fileName) {
            this.fileName = fileName;
            return (B)((Builder)this.asBuilder());
        }

        public B setAppend(boolean append) {
            this.append = append;
            return (B)((Builder)this.asBuilder());
        }

        public B setRegionLength(int regionLength) {
            this.regionLength = regionLength;
            return (B)((Builder)this.asBuilder());
        }

        public B setAdvertise(boolean advertise) {
            this.advertise = advertise;
            return (B)((Builder)this.asBuilder());
        }

        public B setAdvertiseURI(String advertiseURI) {
            this.advertiseURI = advertiseURI;
            return (B)((Builder)this.asBuilder());
        }
    }
}

