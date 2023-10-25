/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.Connection
 *  javax.jms.ConnectionFactory
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.MessageConsumer
 *  javax.jms.MessageProducer
 *  javax.jms.Session
 */
package org.apache.logging.log4j.core.appender.mom;

import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.status.StatusLogger;

public class JmsManager
extends AbstractManager {
    static final JmsManagerFactory FACTORY = new JmsManagerFactory();
    private final JmsManagerConfiguration configuration;
    private volatile Reconnector reconnector;
    private volatile JndiManager jndiManager;
    private volatile Connection connection;
    private volatile Session session;
    private volatile Destination destination;
    private volatile MessageProducer messageProducer;

    public static JmsManager getJmsManager(String name, Properties jndiProperties, String connectionFactoryName, String destinationName, String userName, char[] password, boolean immediateFail, long reconnectIntervalMillis) {
        JmsManagerConfiguration configuration = new JmsManagerConfiguration(jndiProperties, connectionFactoryName, destinationName, userName, password, immediateFail, reconnectIntervalMillis);
        return JmsManager.getManager(name, FACTORY, configuration);
    }

    private JmsManager(String name, JmsManagerConfiguration configuration) {
        super(null, name);
        this.configuration = configuration;
        this.jndiManager = configuration.getJndiManager();
        try {
            this.connection = this.createConnection(this.jndiManager);
            this.session = this.createSession(this.connection);
            this.destination = this.createDestination(this.jndiManager);
            this.messageProducer = this.createMessageProducer(this.session, this.destination);
            this.connection.start();
        }
        catch (JMSException | NamingException e) {
            this.reconnector = this.createReconnector();
            this.reconnector.start();
        }
    }

    private boolean closeConnection() {
        if (this.connection == null) {
            return true;
        }
        Connection temp = this.connection;
        this.connection = null;
        try {
            temp.close();
            return true;
        }
        catch (JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS Connection: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), (Object)temp, (Object)e);
            return false;
        }
    }

    private boolean closeJndiManager() {
        if (this.jndiManager == null) {
            return true;
        }
        JndiManager tmp = this.jndiManager;
        this.jndiManager = null;
        tmp.close();
        return true;
    }

    private boolean closeMessageProducer() {
        if (this.messageProducer == null) {
            return true;
        }
        MessageProducer temp = this.messageProducer;
        this.messageProducer = null;
        try {
            temp.close();
            return true;
        }
        catch (JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS MessageProducer: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), (Object)temp, (Object)e);
            return false;
        }
    }

    private boolean closeSession() {
        if (this.session == null) {
            return true;
        }
        Session temp = this.session;
        this.session = null;
        try {
            temp.close();
            return true;
        }
        catch (JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS Session: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), (Object)temp, (Object)e);
            return false;
        }
    }

    private Connection createConnection(JndiManager jndiManager) throws NamingException, JMSException {
        ConnectionFactory connectionFactory = (ConnectionFactory)jndiManager.lookup(this.configuration.getConnectionFactoryName());
        if (this.configuration.getUserName() != null && this.configuration.getPassword() != null) {
            return connectionFactory.createConnection(this.configuration.getUserName(), this.configuration.getPassword() == null ? null : String.valueOf(this.configuration.getPassword()));
        }
        return connectionFactory.createConnection();
    }

    private Destination createDestination(JndiManager jndiManager) throws NamingException {
        return (Destination)jndiManager.lookup(this.configuration.getDestinationName());
    }

    public Message createMessage(Serializable object) throws JMSException {
        if (object instanceof String) {
            return this.session.createTextMessage((String)((Object)object));
        }
        if (object instanceof org.apache.logging.log4j.message.MapMessage) {
            return this.map((org.apache.logging.log4j.message.MapMessage)object, this.session.createMapMessage());
        }
        return this.session.createObjectMessage(object);
    }

    private void createMessageAndSend(LogEvent event, Serializable serializable) throws JMSException {
        Message message = this.createMessage(serializable);
        message.setJMSTimestamp(event.getTimeMillis());
        this.messageProducer.send(message);
    }

    public MessageConsumer createMessageConsumer() throws JMSException {
        return this.session.createConsumer(this.destination);
    }

    public MessageProducer createMessageProducer(Session session, Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    private Reconnector createReconnector() {
        Reconnector recon = new Reconnector(this);
        recon.setDaemon(true);
        recon.setPriority(1);
        return recon;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, 1);
    }

    public JmsManagerConfiguration getJmsManagerConfiguration() {
        return this.configuration;
    }

    JndiManager getJndiManager() {
        return this.configuration.getJndiManager();
    }

    <T> T lookup(String destinationName) throws NamingException {
        return this.jndiManager.lookup(destinationName);
    }

    private MapMessage map(org.apache.logging.log4j.message.MapMessage<?, ?> log4jMapMessage, MapMessage jmsMapMessage) {
        log4jMapMessage.forEach((key, value) -> {
            try {
                jmsMapMessage.setObject(key, value);
            }
            catch (JMSException e) {
                throw new IllegalArgumentException(String.format("%s mapping key '%s' to value '%s': %s", ((Object)((Object)e)).getClass(), key, value, e.getLocalizedMessage()), e);
            }
        });
        return jmsMapMessage;
    }

    protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
        if (this.reconnector != null) {
            this.reconnector.shutdown();
            this.reconnector.interrupt();
            this.reconnector = null;
        }
        boolean closed = false;
        closed &= this.closeJndiManager();
        closed &= this.closeMessageProducer();
        closed &= this.closeSession();
        return (closed &= this.closeConnection()) && this.jndiManager.stop(timeout, timeUnit);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void send(LogEvent event, Serializable serializable) {
        if (this.messageProducer == null && this.reconnector != null && !this.configuration.isImmediateFail()) {
            this.reconnector.latch();
            if (this.messageProducer == null) {
                throw new AppenderLoggingException("Error sending to JMS Manager '" + this.getName() + "': JMS message producer not available");
            }
        }
        JmsManager jmsManager = this;
        synchronized (jmsManager) {
            block11: {
                try {
                    this.createMessageAndSend(event, serializable);
                }
                catch (JMSException causeEx) {
                    if (!this.configuration.isRetry() || this.reconnector != null) break block11;
                    this.reconnector = this.createReconnector();
                    try {
                        this.closeJndiManager();
                        this.reconnector.reconnect();
                    }
                    catch (JMSException | NamingException reconnEx) {
                        JmsManager.logger().debug("Cannot reestablish JMS connection to {}: {}; starting reconnector thread {}", this.configuration, reconnEx.getLocalizedMessage(), this.reconnector.getName(), reconnEx);
                        this.reconnector.start();
                        throw new AppenderLoggingException(String.format("JMS exception sending to %s for %s", this.getName(), this.configuration), causeEx);
                    }
                    try {
                        this.createMessageAndSend(event, serializable);
                    }
                    catch (JMSException e) {
                        throw new AppenderLoggingException(String.format("Error sending to %s after reestablishing JMS connection for %s", this.getName(), this.configuration), causeEx);
                    }
                }
            }
        }
    }

    private class Reconnector
    extends Log4jThread {
        private final CountDownLatch latch;
        private volatile boolean shutdown;
        private final Object owner;

        private Reconnector(Object owner) {
            super("JmsManager-Reconnector");
            this.latch = new CountDownLatch(1);
            this.owner = owner;
        }

        public void latch() {
            try {
                this.latch.await();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void reconnect() throws NamingException, JMSException {
            JndiManager jndiManager2 = JmsManager.this.getJndiManager();
            Connection connection2 = JmsManager.this.createConnection(jndiManager2);
            Session session2 = JmsManager.this.createSession(connection2);
            Destination destination2 = JmsManager.this.createDestination(jndiManager2);
            MessageProducer messageProducer2 = JmsManager.this.createMessageProducer(session2, destination2);
            connection2.start();
            Object object = this.owner;
            synchronized (object) {
                JmsManager.this.jndiManager = jndiManager2;
                JmsManager.this.connection = connection2;
                JmsManager.this.session = session2;
                JmsManager.this.destination = destination2;
                JmsManager.this.messageProducer = messageProducer2;
                JmsManager.this.reconnector = null;
                this.shutdown = true;
            }
            JmsManager.logger().debug("Connection reestablished to {}", JmsManager.this.configuration);
        }

        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Reconnector.sleep(JmsManager.this.configuration.getReconnectIntervalMillis());
                    this.reconnect();
                }
                catch (InterruptedException | JMSException | NamingException e) {
                    JmsManager.logger().debug("Cannot reestablish JMS connection to {}: {}", JmsManager.this.configuration, e.getLocalizedMessage(), e);
                }
                finally {
                    this.latch.countDown();
                }
            }
        }

        public void shutdown() {
            this.shutdown = true;
        }
    }

    private static class JmsManagerFactory
    implements ManagerFactory<JmsManager, JmsManagerConfiguration> {
        private JmsManagerFactory() {
        }

        @Override
        public JmsManager createManager(String name, JmsManagerConfiguration data) {
            try {
                return new JmsManager(name, data);
            }
            catch (Exception e) {
                JmsManager.logger().error("Error creating JmsManager using JmsManagerConfiguration [{}]", data, e);
                return null;
            }
        }
    }

    public static class JmsManagerConfiguration {
        private final Properties jndiProperties;
        private final String connectionFactoryName;
        private final String destinationName;
        private final String userName;
        private final char[] password;
        private final boolean immediateFail;
        private final boolean retry;
        private final long reconnectIntervalMillis;

        JmsManagerConfiguration(Properties jndiProperties, String connectionFactoryName, String destinationName, String userName, char[] password, boolean immediateFail, long reconnectIntervalMillis) {
            this.jndiProperties = jndiProperties;
            this.connectionFactoryName = connectionFactoryName;
            this.destinationName = destinationName;
            this.userName = userName;
            this.password = password;
            this.immediateFail = immediateFail;
            this.reconnectIntervalMillis = reconnectIntervalMillis;
            this.retry = reconnectIntervalMillis > 0L;
        }

        public String getConnectionFactoryName() {
            return this.connectionFactoryName;
        }

        public String getDestinationName() {
            return this.destinationName;
        }

        public JndiManager getJndiManager() {
            return JndiManager.getJndiManager(this.getJndiProperties());
        }

        public Properties getJndiProperties() {
            return this.jndiProperties;
        }

        public char[] getPassword() {
            return this.password;
        }

        public long getReconnectIntervalMillis() {
            return this.reconnectIntervalMillis;
        }

        public String getUserName() {
            return this.userName;
        }

        public boolean isImmediateFail() {
            return this.immediateFail;
        }

        public boolean isRetry() {
            return this.retry;
        }

        public String toString() {
            return "JmsManagerConfiguration [jndiProperties=" + this.jndiProperties + ", connectionFactoryName=" + this.connectionFactoryName + ", destinationName=" + this.destinationName + ", userName=" + this.userName + ", immediateFail=" + this.immediateFail + ", retry=" + this.retry + ", reconnectIntervalMillis=" + this.reconnectIntervalMillis + "]";
        }
    }
}

