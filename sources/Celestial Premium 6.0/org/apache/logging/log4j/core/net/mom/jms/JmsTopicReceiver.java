/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.mom.jms;

import org.apache.logging.log4j.core.net.mom.jms.AbstractJmsReceiver;

public class JmsTopicReceiver
extends AbstractJmsReceiver {
    private JmsTopicReceiver() {
    }

    public static void main(String[] args) throws Exception {
        JmsTopicReceiver receiver = new JmsTopicReceiver();
        receiver.doMain(args);
    }

    @Override
    protected void usage() {
        System.err.println("Wrong number of arguments.");
        System.err.println("Usage: java " + JmsTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
    }
}

