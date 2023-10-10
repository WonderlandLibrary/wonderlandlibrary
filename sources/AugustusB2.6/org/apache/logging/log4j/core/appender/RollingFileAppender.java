// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;
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
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;

@Plugin(name = "RollingFile", category = "Core", elementType = "appender", printObject = true)
public final class RollingFileAppender extends AbstractOutputStreamAppender<RollingFileManager>
{
    public static final String PLUGIN_NAME = "RollingFile";
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private final String fileName;
    private final String filePattern;
    private Object advertisement;
    private final Advertiser advertiser;
    
    private RollingFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final RollingFileManager manager, final String fileName, final String filePattern, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.fileName = fileName;
        this.filePattern = filePattern;
        this.advertiser = advertiser;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        final boolean stopped = super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return stopped;
    }
    
    @Override
    public void append(final LogEvent event) {
        this.getManager().checkRollover(event);
        super.append(event);
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getFilePattern() {
        return this.filePattern;
    }
    
    public <T extends TriggeringPolicy> T getTriggeringPolicy() {
        return this.getManager().getTriggeringPolicy();
    }
    
    @Deprecated
    public static <B extends Builder<B>> RollingFileAppender createAppender(final String fileName, final String filePattern, final String append, final String name, final String bufferedIO, final String bufferSizeStr, final String immediateFlush, final TriggeringPolicy policy, final RolloverStrategy strategy, final Layout<? extends Serializable> layout, final Filter filter, final String ignore, final String advertise, final String advertiseUri, final Configuration config) {
        final int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
        return newBuilder().withAdvertise(Boolean.parseBoolean(advertise)).withAdvertiseUri(advertiseUri).withAppend(Booleans.parseBoolean(append, true)).withBufferedIo(Booleans.parseBoolean(bufferedIO, true)).withBufferSize(bufferSize).setConfiguration(config).withFileName(fileName).withFilePattern(filePattern).setFilter(filter).setIgnoreExceptions(Booleans.parseBoolean(ignore, true)).withImmediateFlush(Booleans.parseBoolean(immediateFlush, true)).setLayout(layout).withCreateOnDemand(false).withLocking(false).setName(name).withPolicy(policy).withStrategy(strategy).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RollingFileAppender>
    {
        @PluginBuilderAttribute
        private String fileName;
        @PluginBuilderAttribute
        @Required
        private String filePattern;
        @PluginBuilderAttribute
        private boolean append;
        @PluginBuilderAttribute
        private boolean locking;
        @PluginElement("Policy")
        @Required
        private TriggeringPolicy policy;
        @PluginElement("Strategy")
        private RolloverStrategy strategy;
        @PluginBuilderAttribute
        private boolean advertise;
        @PluginBuilderAttribute
        private String advertiseUri;
        @PluginBuilderAttribute
        private boolean createOnDemand;
        @PluginBuilderAttribute
        private String filePermissions;
        @PluginBuilderAttribute
        private String fileOwner;
        @PluginBuilderAttribute
        private String fileGroup;
        
        public Builder() {
            this.append = true;
        }
        
        @Override
        public RollingFileAppender build() {
            final boolean isBufferedIo = this.isBufferedIo();
            final int bufferSize = this.getBufferSize();
            if (this.getName() == null) {
                RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No name provided.", this.getName());
                return null;
            }
            if (!isBufferedIo && bufferSize > 0) {
                RollingFileAppender.LOGGER.warn("RollingFileAppender '{}': The bufferSize is set to {} but bufferedIO is not true", this.getName(), bufferSize);
            }
            if (this.filePattern == null) {
                RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No file name pattern provided.", this.getName());
                return null;
            }
            if (this.policy == null) {
                RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No TriggeringPolicy provided.", this.getName());
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
                RollingFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a {} must be configured", this.getName(), DirectFileRolloverStrategy.class.getSimpleName());
                return null;
            }
            final Layout<? extends Serializable> layout = this.getOrCreateLayout();
            final RollingFileManager manager = RollingFileManager.getFileManager(this.fileName, this.filePattern, this.append, isBufferedIo, this.policy, this.strategy, this.advertiseUri, layout, bufferSize, this.isImmediateFlush(), this.createOnDemand, this.filePermissions, this.fileOwner, this.fileGroup, this.getConfiguration());
            if (manager == null) {
                return null;
            }
            manager.initialize();
            return new RollingFileAppender(this.getName(), layout, this.getFilter(), manager, this.fileName, this.filePattern, this.isIgnoreExceptions(), !isBufferedIo || this.isImmediateFlush(), this.advertise ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray(), null);
        }
        
        public String getAdvertiseUri() {
            return this.advertiseUri;
        }
        
        public String getFileName() {
            return this.fileName;
        }
        
        public boolean isAdvertise() {
            return this.advertise;
        }
        
        public boolean isAppend() {
            return this.append;
        }
        
        public boolean isCreateOnDemand() {
            return this.createOnDemand;
        }
        
        public boolean isLocking() {
            return this.locking;
        }
        
        public String getFilePermissions() {
            return this.filePermissions;
        }
        
        public String getFileOwner() {
            return this.fileOwner;
        }
        
        public String getFileGroup() {
            return this.fileGroup;
        }
        
        public B withAdvertise(final boolean advertise) {
            this.advertise = advertise;
            return this.asBuilder();
        }
        
        public B withAdvertiseUri(final String advertiseUri) {
            this.advertiseUri = advertiseUri;
            return this.asBuilder();
        }
        
        public B withAppend(final boolean append) {
            this.append = append;
            return this.asBuilder();
        }
        
        public B withFileName(final String fileName) {
            this.fileName = fileName;
            return this.asBuilder();
        }
        
        public B withCreateOnDemand(final boolean createOnDemand) {
            this.createOnDemand = createOnDemand;
            return this.asBuilder();
        }
        
        public B withLocking(final boolean locking) {
            this.locking = locking;
            return this.asBuilder();
        }
        
        public String getFilePattern() {
            return this.filePattern;
        }
        
        public TriggeringPolicy getPolicy() {
            return this.policy;
        }
        
        public RolloverStrategy getStrategy() {
            return this.strategy;
        }
        
        public B withFilePattern(final String filePattern) {
            this.filePattern = filePattern;
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
