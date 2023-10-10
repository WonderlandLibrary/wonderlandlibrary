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

@Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
public final class RandomAccessFileAppender extends AbstractOutputStreamAppender<RandomAccessFileManager>
{
    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;
    
    private RandomAccessFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final RandomAccessFileManager manager, final String filename, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser, final Property[] properties) {
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
    
    public int getBufferSize() {
        return this.getManager().getBufferSize();
    }
    
    @Deprecated
    public static <B extends Builder<B>> RandomAccessFileAppender createAppender(final String fileName, final String append, final String name, final String immediateFlush, final String bufferSizeStr, final String ignore, final Layout<? extends Serializable> layout, final Filter filter, final String advertise, final String advertiseURI, final Configuration configuration) {
        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
        return newBuilder().setAdvertise(isAdvertise).setAdvertiseURI(advertiseURI).setAppend(isAppend).withBufferSize(bufferSize).setConfiguration(configuration).setFileName(fileName).setFilter(filter).setIgnoreExceptions(ignoreExceptions).withImmediateFlush(isFlush).setLayout(layout).setName(name).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RandomAccessFileAppender>
    {
        @PluginBuilderAttribute("fileName")
        private String fileName;
        @PluginBuilderAttribute("append")
        private boolean append;
        @PluginBuilderAttribute("advertise")
        private boolean advertise;
        @PluginBuilderAttribute("advertiseURI")
        private String advertiseURI;
        
        public Builder() {
            this.append = true;
            this.withBufferSize(262144);
        }
        
        @Override
        public RandomAccessFileAppender build() {
            final String name = this.getName();
            if (name == null) {
                RandomAccessFileAppender.LOGGER.error("No name provided for RandomAccessFileAppender");
                return null;
            }
            if (this.fileName == null) {
                RandomAccessFileAppender.LOGGER.error("No filename provided for RandomAccessFileAppender with name {}", name);
                return null;
            }
            final Layout<? extends Serializable> layout = this.getOrCreateLayout();
            final boolean immediateFlush = this.isImmediateFlush();
            final RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(this.fileName, this.append, immediateFlush, this.getBufferSize(), this.advertiseURI, layout, null);
            if (manager == null) {
                return null;
            }
            return new RandomAccessFileAppender(name, layout, this.getFilter(), manager, this.fileName, this.isIgnoreExceptions(), immediateFlush, this.advertise ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray(), null);
        }
        
        public B setFileName(final String fileName) {
            this.fileName = fileName;
            return this.asBuilder();
        }
        
        public B setAppend(final boolean append) {
            this.append = append;
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
