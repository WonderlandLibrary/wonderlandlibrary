// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MemoryMappedFile", category = "Core", elementType = "appender", printObject = true)
public final class MemoryMappedFileAppender extends AbstractOutputStreamAppender<MemoryMappedFileManager>
{
    private static final int BIT_POSITION_1GB = 30;
    private static final int MAX_REGION_LENGTH = 1073741824;
    private static final int MIN_REGION_LENGTH = 256;
    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;
    
    private MemoryMappedFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final MemoryMappedFileManager manager, final String filename, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.fileName = filename;
        this.advertiser = advertiser;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return true;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getRegionLength() {
        return this.getManager().getRegionLength();
    }
    
    @Deprecated
    public static <B extends Builder<B>> MemoryMappedFileAppender createAppender(final String fileName, final String append, final String name, final String immediateFlush, final String regionLengthStr, final String ignore, final Layout<? extends Serializable> layout, final Filter filter, final String advertise, final String advertiseURI, final Configuration config) {
        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, false);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final int regionLength = Integers.parseInt(regionLengthStr, 33554432);
        return newBuilder().setAdvertise(isAdvertise).setAdvertiseURI(advertiseURI).setAppend(isAppend).setConfiguration(config).setFileName(fileName).setFilter(filter).setIgnoreExceptions(ignoreExceptions).withImmediateFlush(isImmediateFlush).setLayout(layout).setName(name).setRegionLength(regionLength).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private static int determineValidRegionLength(final String name, final int regionLength) {
        if (regionLength > 1073741824) {
            MemoryMappedFileAppender.LOGGER.info("MemoryMappedAppender[{}] Reduced region length from {} to max length: {}", name, regionLength, 1073741824);
            return 1073741824;
        }
        if (regionLength < 256) {
            MemoryMappedFileAppender.LOGGER.info("MemoryMappedAppender[{}] Expanded region length from {} to min length: {}", name, regionLength, 256);
            return 256;
        }
        final int result = Integers.ceilingNextPowerOfTwo(regionLength);
        if (regionLength != result) {
            MemoryMappedFileAppender.LOGGER.info("MemoryMappedAppender[{}] Rounded up region length from {} to next power of two: {}", name, regionLength, result);
        }
        return result;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<MemoryMappedFileAppender>
    {
        @PluginBuilderAttribute("fileName")
        private String fileName;
        @PluginBuilderAttribute("append")
        private boolean append;
        @PluginBuilderAttribute("regionLength")
        private int regionLength;
        @PluginBuilderAttribute("advertise")
        private boolean advertise;
        @PluginBuilderAttribute("advertiseURI")
        private String advertiseURI;
        
        public Builder() {
            this.append = true;
            this.regionLength = 33554432;
        }
        
        @Override
        public MemoryMappedFileAppender build() {
            final String name = this.getName();
            final int actualRegionLength = determineValidRegionLength(name, this.regionLength);
            if (name == null) {
                MemoryMappedFileAppender.LOGGER.error("No name provided for MemoryMappedFileAppender");
                return null;
            }
            if (this.fileName == null) {
                MemoryMappedFileAppender.LOGGER.error("No filename provided for MemoryMappedFileAppender with name " + name);
                return null;
            }
            final Layout<? extends Serializable> layout = this.getOrCreateLayout();
            final MemoryMappedFileManager manager = MemoryMappedFileManager.getFileManager(this.fileName, this.append, this.isImmediateFlush(), actualRegionLength, this.advertiseURI, layout);
            if (manager == null) {
                return null;
            }
            return new MemoryMappedFileAppender(name, layout, this.getFilter(), manager, this.fileName, this.isIgnoreExceptions(), false, this.advertise ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray(), null);
        }
        
        public B setFileName(final String fileName) {
            this.fileName = fileName;
            return this.asBuilder();
        }
        
        public B setAppend(final boolean append) {
            this.append = append;
            return this.asBuilder();
        }
        
        public B setRegionLength(final int regionLength) {
            this.regionLength = regionLength;
            return this.asBuilder();
        }
        
        public B setAdvertise(final boolean advertise) {
            this.advertise = advertise;
            return this.asBuilder();
        }
        
        public B setAdvertiseURI(final String advertiseURI) {
            this.advertiseURI = advertiseURI;
            return this.asBuilder();
        }
    }
}
