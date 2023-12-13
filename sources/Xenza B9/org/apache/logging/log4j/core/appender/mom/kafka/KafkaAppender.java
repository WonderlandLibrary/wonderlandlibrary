// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.stream.Stream;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
public final class KafkaAppender extends AbstractAppender
{
    private static final String[] KAFKA_CLIENT_PACKAGES;
    private final Integer retryCount;
    private final KafkaManager manager;
    
    @Deprecated
    public static KafkaAppender createAppender(final Layout<? extends Serializable> layout, final Filter filter, final String name, final boolean ignoreExceptions, final String topic, final Property[] properties, final Configuration configuration, final String key) {
        if (layout == null) {
            AbstractLifeCycle.LOGGER.error("No layout provided for KafkaAppender");
            return null;
        }
        final KafkaManager kafkaManager = KafkaManager.getManager(configuration.getLoggerContext(), name, topic, true, properties, key);
        return new KafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager, null, null);
    }
    
    private static boolean isRecursive(final LogEvent event) {
        return Stream.of(KafkaAppender.KAFKA_CLIENT_PACKAGES).anyMatch(prefix -> event.getLoggerName().startsWith(prefix));
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private KafkaAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final KafkaManager manager, final Property[] properties, final Integer retryCount) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = Objects.requireNonNull(manager, "manager");
        this.retryCount = retryCount;
    }
    
    @Override
    public void append(final LogEvent event) {
        if (event.getLoggerName() != null && isRecursive(event)) {
            KafkaAppender.LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), this.getName());
        }
        else {
            try {
                this.tryAppend(event);
            }
            catch (final Exception e) {
                if (this.retryCount != null) {
                    int currentRetryAttempt = 0;
                    while (currentRetryAttempt < this.retryCount) {
                        ++currentRetryAttempt;
                        try {
                            this.tryAppend(event);
                        }
                        catch (final Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
                this.error("Unable to write to Kafka in appender [" + this.getName() + "]", event, e);
            }
        }
    }
    
    @Override
    public void start() {
        super.start();
        this.manager.startup();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= this.manager.stop(timeout, timeUnit);
        this.setStopped();
        return stopped;
    }
    
    @Override
    public String toString() {
        return "KafkaAppender{name=" + this.getName() + ", state=" + this.getState() + ", topic=" + this.manager.getTopic() + '}';
    }
    
    private void tryAppend(final LogEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        final Layout<? extends Serializable> layout = this.getLayout();
        byte[] data;
        if (layout instanceof SerializedLayout) {
            final byte[] header = layout.getHeader();
            final byte[] body = layout.toByteArray(event);
            data = new byte[header.length + body.length];
            System.arraycopy(header, 0, data, 0, header.length);
            System.arraycopy(body, 0, data, header.length, body.length);
        }
        else {
            data = layout.toByteArray(event);
        }
        this.manager.send(data);
    }
    
    static {
        KAFKA_CLIENT_PACKAGES = new String[] { "org.apache.kafka.common", "org.apache.kafka.clients" };
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<KafkaAppender>
    {
        @PluginAttribute("retryCount")
        private String retryCount;
        @PluginAttribute("topic")
        private String topic;
        @PluginAttribute("key")
        private String key;
        @PluginAttribute(value = "syncSend", defaultBoolean = true)
        private boolean syncSend;
        
        @Override
        public KafkaAppender build() {
            final Layout<? extends Serializable> layout = this.getLayout();
            if (layout == null) {
                KafkaAppender.LOGGER.error("No layout provided for KafkaAppender");
                return null;
            }
            final KafkaManager kafkaManager = KafkaManager.getManager(this.getConfiguration().getLoggerContext(), this.getName(), this.topic, this.syncSend, this.getPropertyArray(), this.key);
            return new KafkaAppender(this.getName(), layout, this.getFilter(), this.isIgnoreExceptions(), kafkaManager, this.getPropertyArray(), this.getRetryCount(), null);
        }
        
        public Integer getRetryCount() {
            Integer intRetryCount = null;
            try {
                intRetryCount = Integer.valueOf(this.retryCount);
            }
            catch (final NumberFormatException ex) {}
            return intRetryCount;
        }
        
        public String getTopic() {
            return this.topic;
        }
        
        public boolean isSyncSend() {
            return this.syncSend;
        }
        
        public B setKey(final String key) {
            this.key = key;
            return this.asBuilder();
        }
        
        public B setSyncSend(final boolean syncSend) {
            this.syncSend = syncSend;
            return this.asBuilder();
        }
        
        public B setTopic(final String topic) {
            this.topic = topic;
            return this.asBuilder();
        }
    }
}
