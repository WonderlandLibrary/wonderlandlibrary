// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;

@Plugin(name = "RollingRandomAccessFile", category = "Core", elementType = "appender", printObject = true)
public final class RollingRandomAccessFileAppender extends AbstractOutputStreamAppender<RollingRandomAccessFileManager>
{
    private final String fileName;
    private final String filePattern;
    private final Object advertisement;
    private final Advertiser advertiser;
    
    private RollingRandomAccessFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final RollingRandomAccessFileManager manager, final String fileName, final String filePattern, final boolean ignoreExceptions, final boolean immediateFlush, final int bufferSize, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        else {
            this.advertisement = null;
        }
        this.fileName = fileName;
        this.filePattern = filePattern;
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
    
    @Override
    public void append(final LogEvent event) {
        final RollingRandomAccessFileManager manager = this.getManager();
        manager.checkRollover(event);
        super.append(event);
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getFilePattern() {
        return this.filePattern;
    }
    
    public int getBufferSize() {
        return this.getManager().getBufferSize();
    }
    
    @Deprecated
    public static <B extends Builder<B>> RollingRandomAccessFileAppender createAppender(final String fileName, final String filePattern, final String append, final String name, final String immediateFlush, final String bufferSizeStr, final TriggeringPolicy policy, final RolloverStrategy strategy, final Layout<? extends Serializable> layout, final Filter filter, final String ignoreExceptions, final String advertise, final String advertiseURI, final Configuration configuration) {
        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean isIgnoreExceptions = Booleans.parseBoolean(ignoreExceptions, true);
        final boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
        return newBuilder().withAdvertise(isAdvertise).withAdvertiseURI(advertiseURI).withAppend(isAppend).withBufferSize(bufferSize).setConfiguration(configuration).withFileName(fileName).withFilePattern(filePattern).setFilter(filter).setIgnoreExceptions(isIgnoreExceptions).withImmediateFlush(isImmediateFlush).setLayout(layout).setName(name).withPolicy(policy).withStrategy(strategy).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RollingRandomAccessFileAppender>
    {
        @PluginBuilderAttribute("fileName")
        private String fileName;
        @PluginBuilderAttribute("filePattern")
        private String filePattern;
        @PluginBuilderAttribute("append")
        private boolean append;
        @PluginElement("Policy")
        private TriggeringPolicy policy;
        @PluginElement("Strategy")
        private RolloverStrategy strategy;
        @PluginBuilderAttribute("advertise")
        private boolean advertise;
        @PluginBuilderAttribute("advertiseURI")
        private String advertiseURI;
        @PluginBuilderAttribute
        private String filePermissions;
        @PluginBuilderAttribute
        private String fileOwner;
        @PluginBuilderAttribute
        private String fileGroup;
        
        public Builder() {
            this.append = true;
            this.withBufferSize(262144);
            this.setIgnoreExceptions(true);
            this.withImmediateFlush(true);
        }
        
        @Override
        public RollingRandomAccessFileAppender build() {
            final String name = this.getName();
            if (name == null) {
                RollingRandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
                return null;
            }
            if (this.strategy == null) {
                if (this.fileName != null) {
                    this.strategy = DefaultRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(this.getConfiguration()).build();
                }
                else {
                    this.strategy = DirectWriteRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(this.getConfiguration()).build();
                }
            }
            else if (this.fileName == null && !(this.strategy instanceof DirectFileRolloverStrategy)) {
                RollingRandomAccessFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a DirectFileRolloverStrategy must be configured");
                return null;
            }
            if (this.filePattern == null) {
                RollingRandomAccessFileAppender.LOGGER.error("No filename pattern provided for FileAppender with name " + name);
                return null;
            }
            if (this.policy == null) {
                RollingRandomAccessFileAppender.LOGGER.error("A TriggeringPolicy must be provided");
                return null;
            }
            final Layout<? extends Serializable> layout = this.getOrCreateLayout();
            final boolean immediateFlush = this.isImmediateFlush();
            final int bufferSize = this.getBufferSize();
            final RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(this.fileName, this.filePattern, this.append, immediateFlush, bufferSize, this.policy, this.strategy, this.advertiseURI, layout, this.filePermissions, this.fileOwner, this.fileGroup, this.getConfiguration());
            if (manager == null) {
                return null;
            }
            manager.initialize();
            return new RollingRandomAccessFileAppender(name, layout, this.getFilter(), manager, this.fileName, this.filePattern, this.isIgnoreExceptions(), immediateFlush, bufferSize, this.advertise ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray(), null);
        }
        
        public B withFileName(final String fileName) {
            this.fileName = fileName;
            return this.asBuilder();
        }
        
        public B withFilePattern(final String filePattern) {
            this.filePattern = filePattern;
            return this.asBuilder();
        }
        
        public B withAppend(final boolean append) {
            this.append = append;
            return this.asBuilder();
        }
        
        public B withPolicy(final TriggeringPolicy policy) {
            this.policy = policy;
            return this.asBuilder();
        }
        
        public B withStrategy(final RolloverStrategy strategy) {
            this.strategy = strategy;
            return this.asBuilder();
        }
        
        public B withAdvertise(final boolean advertise) {
            this.advertise = advertise;
            return this.asBuilder();
        }
        
        public B withAdvertiseURI(final String advertiseURI) {
            this.advertiseURI = advertiseURI;
            return this.asBuilder();
        }
        
        public B withFilePermissions(final String filePermissions) {
            this.filePermissions = filePermissions;
            return this.asBuilder();
        }
        
        public B withFileOwner(final String fileOwner) {
            this.fileOwner = fileOwner;
            return this.asBuilder();
        }
        
        public B withFileGroup(final String fileGroup) {
            this.fileGroup = fileGroup;
            return this.asBuilder();
        }
    }
}
