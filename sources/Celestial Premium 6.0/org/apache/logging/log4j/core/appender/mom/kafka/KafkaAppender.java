/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.mom.kafka;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.layout.SerializedLayout;

@Plugin(name="Kafka", category="Core", elementType="appender", printObject=true)
public final class KafkaAppender
extends AbstractAppender {
    private final Integer retryCount;
    private final KafkaManager manager;

    @Deprecated
    public static KafkaAppender createAppender(Layout<? extends Serializable> layout, Filter filter, String name, boolean ignoreExceptions, String topic, Property[] properties, Configuration configuration, String key) {
        if (layout == null) {
            AbstractLifeCycle.LOGGER.error("No layout provided for KafkaAppender");
            return null;
        }
        KafkaManager kafkaManager = KafkaManager.getManager(configuration.getLoggerContext(), name, topic, true, properties, key);
        return new KafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager, null, null);
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private KafkaAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, KafkaManager manager, Property[] properties, Integer retryCount) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = Objects.requireNonNull(manager, "manager");
        this.retryCount = retryCount;
    }

    @Override
    public void append(LogEvent event) {
        if (event.getLoggerName() != null && event.getLoggerName().startsWith("org.apache.kafka")) {
            LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), this.getName());
        } else {
            try {
                this.tryAppend(event);
            }
            catch (Exception e) {
                if (this.retryCount != null) {
                    for (int currentRetryAttempt = 0; currentRetryAttempt < this.retryCount; ++currentRetryAttempt) {
                        try {
                            this.tryAppend(event);
                            break;
                        }
                        catch (Exception exception) {
                            continue;
                        }
                    }
                }
                this.error("Unable to write to Kafka in appender [" + this.getName() + "]", event, e);
            }
        }
    }

    private void tryAppend(LogEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        byte[] data;
        Layout<? extends Serializable> layout = this.getLayout();
        if (layout instanceof SerializedLayout) {
            byte[] header = layout.getHeader();
            byte[] body = layout.toByteArray(event);
            data = new byte[header.length + body.length];
            System.arraycopy(header, 0, data, 0, header.length);
            System.arraycopy(body, 0, data, header.length, body.length);
        } else {
            data = layout.toByteArray(event);
        }
        this.manager.send(data);
    }

    @Override
    public void start() {
        super.start();
        this.manager.startup();
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        this.setStopped();
        return stopped &= this.manager.stop(timeout, timeUnit);
    }

    @Override
    public String toString() {
        return "KafkaAppender{name=" + this.getName() + ", state=" + (Object)((Object)this.getState()) + ", topic=" + this.manager.getTopic() + '}';
    }

    public static class Builder<B extends Builder<B>>
    extends AbstractAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<KafkaAppender> {
        @PluginAttribute(value="retryCount")
        private String retryCount;
        @PluginAttribute(value="topic")
        private String topic;
        @PluginAttribute(value="key")
        private String key;
        @PluginAttribute(value="syncSend", defaultBoolean=true)
        private boolean syncSend;

        @Override
        public KafkaAppender build() {
            Layout<Serializable> layout = this.getLayout();
            if (layout == null) {
                LOGGER.error("No layout provided for KafkaAppender");
                return null;
            }
            KafkaManager kafkaManager = KafkaManager.getManager(this.getConfiguration().getLoggerContext(), this.getName(), this.topic, this.syncSend, this.getPropertyArray(), this.key);
            return new KafkaAppender(this.getName(), layout, this.getFilter(), this.isIgnoreExceptions(), kafkaManager, this.getPropertyArray(), this.getRetryCount());
        }

        public String getTopic() {
            return this.topic;
        }

        public boolean isSyncSend() {
            return this.syncSend;
        }

        public B setTopic(String topic) {
            this.topic = topic;
            return (B)((Builder)this.asBuilder());
        }

        public B setSyncSend(boolean syncSend) {
            this.syncSend = syncSend;
            return (B)((Builder)this.asBuilder());
        }

        public B setKey(String key) {
            this.key = key;
            return (B)((Builder)this.asBuilder());
        }

        public Integer getRetryCount() {
            Integer intRetryCount = null;
            try {
                intRetryCount = Integer.valueOf(this.retryCount);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            return intRetryCount;
        }
    }
}

