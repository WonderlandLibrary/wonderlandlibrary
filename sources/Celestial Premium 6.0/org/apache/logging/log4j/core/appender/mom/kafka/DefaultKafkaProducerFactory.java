/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.kafka.clients.producer.KafkaProducer
 *  org.apache.kafka.clients.producer.Producer
 */
package org.apache.logging.log4j.core.appender.mom.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;

public class DefaultKafkaProducerFactory
implements KafkaProducerFactory {
    @Override
    public Producer<byte[], byte[]> newKafkaProducer(Properties config) {
        return new KafkaProducer(config);
    }
}

