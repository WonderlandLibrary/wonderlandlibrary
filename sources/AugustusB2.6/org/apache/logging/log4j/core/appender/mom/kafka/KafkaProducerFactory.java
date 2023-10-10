// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.kafka.clients.producer.Producer;
import java.util.Properties;

public interface KafkaProducerFactory
{
    Producer<byte[], byte[]> newKafkaProducer(final Properties config);
}
