/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageConsumer
 *  javax.jms.MessageListener
 *  javax.jms.ObjectMessage
 */
package org.apache.logging.log4j.core.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.appender.mom.JmsManager;
import org.apache.logging.log4j.core.net.JndiManager;

public class JmsServer
extends LogEventListener
implements MessageListener,
LifeCycle2 {
    private final AtomicReference<LifeCycle.State> state = new AtomicReference<LifeCycle.State>(LifeCycle.State.INITIALIZED);
    private final JmsManager jmsManager;
    private MessageConsumer messageConsumer;

    public JmsServer(String connectionFactoryBindingName, String destinationBindingName, String username, String password) {
        String managerName = JmsServer.class.getName() + '@' + JmsServer.class.hashCode();
        JndiManager jndiManager = JndiManager.getDefaultManager(managerName);
        this.jmsManager = JmsManager.getJmsManager((String)managerName, (JndiManager)jndiManager, (String)connectionFactoryBindingName, (String)destinationBindingName, (String)username, (String)password);
    }

    public LifeCycle.State getState() {
        return this.state.get();
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                Serializable body = ((ObjectMessage)message).getObject();
                if (body instanceof LogEvent) {
                    this.log((LogEvent)body);
                } else {
                    LOGGER.warn("Expected ObjectMessage to contain LogEvent. Got type {} instead.", body.getClass());
                }
            } else {
                LOGGER.warn("Received message of type {} and JMSType {} which cannot be handled.", message.getClass(), message.getJMSType());
            }
        }
        catch (JMSException e) {
            LOGGER.catching(e);
        }
    }

    public void initialize() {
    }

    @Override
    public void start() {
        if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
            try {
                this.messageConsumer = this.jmsManager.createMessageConsumer();
                this.messageConsumer.setMessageListener((MessageListener)this);
            }
            catch (JMSException e) {
                throw new LoggingException(e);
            }
        }
    }

    @Override
    public void stop() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {
        boolean stopped = true;
        try {
            this.messageConsumer.close();
        }
        catch (JMSException e) {
            LOGGER.debug("Exception closing {}", (Object)this.messageConsumer, (Object)e);
            stopped = false;
        }
        return stopped && this.jmsManager.stop(timeout, timeUnit);
    }

    @Override
    public boolean isStarted() {
        return this.state.get() == LifeCycle.State.STARTED;
    }

    public boolean isStopped() {
        return this.state.get() == LifeCycle.State.STOPPED;
    }

    public void run() throws IOException {
        String line;
        this.start();
        System.out.println("Type \"exit\" to quit.");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
        while ((line = stdin.readLine()) != null && !line.equalsIgnoreCase("exit")) {
        }
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
        this.stop();
    }
}

