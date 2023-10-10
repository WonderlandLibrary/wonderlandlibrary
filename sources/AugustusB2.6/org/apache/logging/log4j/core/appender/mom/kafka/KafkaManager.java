// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.util.Log4jThread;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.kafka.clients.producer.Producer;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class KafkaManager extends AbstractManager
{
    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
    static KafkaProducerFactory producerFactory;
    private final Properties config;
    private Producer<byte[], byte[]> producer;
    private final int timeoutMillis;
    private final String topic;
    private final String key;
    private final boolean syncSend;
    private static final KafkaManagerFactory factory;
    
    public KafkaManager(final LoggerContext loggerContext, final String name, final String topic, final boolean syncSend, final Property[] properties, final String key) {
        super(loggerContext, name);
        this.config = new Properties();
        this.topic = Objects.requireNonNull(topic, "topic");
        this.syncSend = syncSend;
        this.config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("batch.size", "0");
        for (final Property property : properties) {
            this.config.setProperty(property.getName(), property.getValue());
        }
        this.key = key;
        this.timeoutMillis = Integer.parseInt(this.config.getProperty("timeout.ms", "30000"));
    }
    
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0L) {
            this.closeProducer(timeout, timeUnit);
        }
        else {
            this.closeProducer(this.timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return true;
    }
    
    private void closeProducer(final long timeout, final TimeUnit timeUnit) {
        if (this.producer != null) {
            final Thread closeThread = new Log4jThread(() -> {
                if (this.producer != null) {
                    this.producer.close();
                }
                return;
            }, "KafkaManager-CloseThread");
            closeThread.setDaemon(true);
            closeThread.start();
            try {
                closeThread.join(timeUnit.toMillis(timeout));
            }
            catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void send(final byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
        if (this.producer != null) {
            byte[] newKey = null;
            if (this.key != null && this.key.contains("${")) {
                newKey = this.getLoggerContext().getConfiguration().getStrSubstitutor().replace(this.key).getBytes(StandardCharsets.UTF_8);
            }
            else if (this.key != null) {
                newKey = this.key.getBytes(StandardCharsets.UTF_8);
            }
            final ProducerRecord<byte[], byte[]> newRecord = (ProducerRecord<byte[], byte[]>)new ProducerRecord(this.topic, (Object)newKey, (Object)msg);
            if (this.syncSend) {
                final Future<RecordMetadata> response = (Future<RecordMetadata>)this.producer.send((ProducerRecord)newRecord);
                response.get(this.timeoutMillis, TimeUnit.MILLISECONDS);
            }
            else {
                this.producer.send((ProducerRecord)newRecord, (metadata, e) -> {
                    if (e != null) {
                        KafkaManager.LOGGER.error("Unable to write to Kafka in appender [" + this.getName() + "]", e);
                    }
                });
            }
        }
    }
    
    public void startup() {
        if (this.producer == null) {
            this.producer = KafkaManager.producerFactory.newKafkaProducer(this.config);
        }
    }
    
    public String getTopic() {
        return this.topic;
    }
    
    public static KafkaManager getManager(final LoggerContext loggerContext, final String name, final String topic, final boolean syncSend, final Property[] properties, final String key) {
        final StringBuilder sb = new StringBuilder(name);
        sb.append(" ").append(topic).append(" ").append(syncSend + "");
        for (final Property prop : properties) {
            sb.append(" ").append(prop.getName()).append("=").append(prop.getValue());
        }
        return AbstractManager.getManager(sb.toString(), (ManagerFactory<KafkaManager, FactoryData>)KafkaManager.factory, new FactoryData(loggerContext, topic, syncSend, properties, key));
    }
    
    static {
        KafkaManager.producerFactory = new DefaultKafkaProducerFactory();
        factory = new KafkaManagerFactory();
    }
    
    private static class FactoryData
    {
        private final LoggerContext loggerContext;
        private final String topic;
        private final boolean syncSend;
        private final Property[] properties;
        private final String key;
        
        public FactoryData(final LoggerContext loggerContext, final String topic, final boolean syncSend, final Property[] properties, final String key) {
            this.loggerContext = loggerContext;
            this.topic = topic;
            this.syncSend = syncSend;
            this.properties = properties;
            this.key = key;
        }
    }
    
    private static class KafkaManagerFactory implements ManagerFactory<KafkaManager, FactoryData>
    {
        @Override
        public KafkaManager createManager(final String name, final FactoryData data) {
            return new KafkaManager(data.loggerContext, name, data.topic, data.syncSend, data.properties, data.key);
        }
    }
}
