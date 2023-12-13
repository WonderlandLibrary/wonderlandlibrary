// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.mom;

import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import java.util.concurrent.TimeUnit;
import javax.jms.MessageConsumer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.MapMessage;
import javax.jms.Message;
import java.io.Serializable;
import javax.jms.ConnectionFactory;
import org.apache.logging.log4j.status.StatusLogger;
import javax.jms.JMSException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.util.Properties;
import javax.jms.MessageProducer;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.Connection;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class JmsManager extends AbstractManager
{
    static final JmsManagerFactory FACTORY;
    private final JmsManagerConfiguration configuration;
    private volatile Reconnector reconnector;
    private volatile JndiManager jndiManager;
    private volatile Connection connection;
    private volatile Session session;
    private volatile Destination destination;
    private volatile MessageProducer messageProducer;
    
    public static JmsManager getJmsManager(final String name, final Properties jndiProperties, final String connectionFactoryName, final String destinationName, final String userName, final char[] password, final boolean immediateFail, final long reconnectIntervalMillis) {
        final JmsManagerConfiguration configuration = new JmsManagerConfiguration(jndiProperties, connectionFactoryName, destinationName, userName, password, immediateFail, reconnectIntervalMillis);
        return AbstractManager.getManager(name, (ManagerFactory<JmsManager, JmsManagerConfiguration>)JmsManager.FACTORY, configuration);
    }
    
    private JmsManager(final String name, final JmsManagerConfiguration configuration) {
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
        catch (final NamingException | JMSException e) {
            (this.reconnector = this.createReconnector()).start();
        }
    }
    
    private boolean closeConnection() {
        if (this.connection == null) {
            return true;
        }
        final Connection temp = this.connection;
        this.connection = null;
        try {
            temp.close();
            return true;
        }
        catch (final JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS Connection: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), temp, e);
            return false;
        }
    }
    
    private boolean closeJndiManager() {
        if (this.jndiManager == null) {
            return true;
        }
        final JndiManager tmp = this.jndiManager;
        this.jndiManager = null;
        tmp.close();
        return true;
    }
    
    private boolean closeMessageProducer() {
        if (this.messageProducer == null) {
            return true;
        }
        final MessageProducer temp = this.messageProducer;
        this.messageProducer = null;
        try {
            temp.close();
            return true;
        }
        catch (final JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS MessageProducer: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), temp, e);
            return false;
        }
    }
    
    private boolean closeSession() {
        if (this.session == null) {
            return true;
        }
        final Session temp = this.session;
        this.session = null;
        try {
            temp.close();
            return true;
        }
        catch (final JMSException e) {
            StatusLogger.getLogger().debug("Caught exception closing JMS Session: {} ({}); continuing JMS manager shutdown", e.getLocalizedMessage(), temp, e);
            return false;
        }
    }
    
    private Connection createConnection(final JndiManager jndiManager) throws NamingException, JMSException {
        final ConnectionFactory connectionFactory = jndiManager.lookup(this.configuration.getConnectionFactoryName());
        if (this.configuration.getUserName() != null && this.configuration.getPassword() != null) {
            return connectionFactory.createConnection(this.configuration.getUserName(), (this.configuration.getPassword() == null) ? null : String.valueOf(this.configuration.getPassword()));
        }
        return connectionFactory.createConnection();
    }
    
    private Destination createDestination(final JndiManager jndiManager) throws NamingException {
        return jndiManager.lookup(this.configuration.getDestinationName());
    }
    
    public Message createMessage(final Serializable object) throws JMSException {
        if (object instanceof String) {
            return (Message)this.session.createTextMessage((String)object);
        }
        if (object instanceof MapMessage) {
            return (Message)this.map((MapMessage<?, ?>)object, this.session.createMapMessage());
        }
        return (Message)this.session.createObjectMessage(object);
    }
    
    private void createMessageAndSend(final LogEvent event, final Serializable serializable) throws JMSException {
        final Message message = this.createMessage(serializable);
        message.setJMSTimestamp(event.getTimeMillis());
        this.messageProducer.send(message);
    }
    
    public MessageConsumer createMessageConsumer() throws JMSException {
        return this.session.createConsumer(this.destination);
    }
    
    public MessageProducer createMessageProducer(final Session session, final Destination destination) throws JMSException {
        return session.createProducer(destination);
    }
    
    private Reconnector createReconnector() {
        final Reconnector recon = new Reconnector((Object)this);
        recon.setDaemon(true);
        recon.setPriority(1);
        return recon;
    }
    
    private Session createSession(final Connection connection) throws JMSException {
        return connection.createSession(false, 1);
    }
    
    public JmsManagerConfiguration getJmsManagerConfiguration() {
        return this.configuration;
    }
    
    JndiManager getJndiManager() {
        return this.configuration.getJndiManager();
    }
    
     <T> T lookup(final String destinationName) throws NamingException {
        return this.jndiManager.lookup(destinationName);
    }
    
    private javax.jms.MapMessage map(final MapMessage<?, ?> log4jMapMessage, final javax.jms.MapMessage jmsMapMessage) {
        log4jMapMessage.forEach((key, value) -> {
            try {
                jmsMapMessage.setObject(key, value);
            }
            catch (final JMSException e) {
                new IllegalArgumentException(String.format("%s mapping key '%s' to value '%s': %s", e.getClass(), key, value, e.getLocalizedMessage()), (Throwable)e);
                throw;
            }
            return;
        });
        return jmsMapMessage;
    }
    
    @Override
    protected boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (this.reconnector != null) {
            this.reconnector.shutdown();
            this.reconnector.interrupt();
            this.reconnector = null;
        }
        boolean closed = false;
        closed &= this.closeJndiManager();
        closed &= this.closeMessageProducer();
        closed &= this.closeSession();
        closed &= this.closeConnection();
        return closed && this.jndiManager.stop(timeout, timeUnit);
    }
    
    void send(final LogEvent event, final Serializable serializable) {
        if (this.messageProducer == null && this.reconnector != null && !this.configuration.isImmediateFail()) {
            this.reconnector.latch();
            if (this.messageProducer == null) {
                throw new AppenderLoggingException("Error sending to JMS Manager '" + this.getName() + "': JMS message producer not available");
            }
        }
        synchronized (this) {
            try {
                this.createMessageAndSend(event, serializable);
            }
            catch (final JMSException causeEx) {
                if (this.configuration.isRetry() && this.reconnector == null) {
                    this.reconnector = this.createReconnector();
                    try {
                        this.closeJndiManager();
                        this.reconnector.reconnect();
                    }
                    catch (final NamingException | JMSException reconnEx) {
                        AbstractManager.logger().debug("Cannot reestablish JMS connection to {}: {}; starting reconnector thread {}", this.configuration, reconnEx.getLocalizedMessage(), this.reconnector.getName(), reconnEx);
                        this.reconnector.start();
                        throw new AppenderLoggingException(String.format("JMS exception sending to %s for %s", this.getName(), this.configuration), (Throwable)causeEx);
                    }
                    try {
                        this.createMessageAndSend(event, serializable);
                    }
                    catch (final JMSException e) {
                        throw new AppenderLoggingException(String.format("Error sending to %s after reestablishing JMS connection for %s", this.getName(), this.configuration), (Throwable)causeEx);
                    }
                }
            }
        }
    }
    
    static {
        FACTORY = new JmsManagerFactory();
    }
    
    public static class JmsManagerConfiguration
    {
        private final Properties jndiProperties;
        private final String connectionFactoryName;
        private final String destinationName;
        private final String userName;
        private final char[] password;
        private final boolean immediateFail;
        private final boolean retry;
        private final long reconnectIntervalMillis;
        
        JmsManagerConfiguration(final Properties jndiProperties, final String connectionFactoryName, final String destinationName, final String userName, final char[] password, final boolean immediateFail, final long reconnectIntervalMillis) {
            this.jndiProperties = jndiProperties;
            this.connectionFactoryName = connectionFactoryName;
            this.destinationName = destinationName;
            this.userName = userName;
            this.password = password;
            this.immediateFail = immediateFail;
            this.reconnectIntervalMillis = reconnectIntervalMillis;
            this.retry = (reconnectIntervalMillis > 0L);
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
        
        @Override
        public String toString() {
            return "JmsManagerConfiguration [jndiProperties=" + this.jndiProperties + ", connectionFactoryName=" + this.connectionFactoryName + ", destinationName=" + this.destinationName + ", userName=" + this.userName + ", immediateFail=" + this.immediateFail + ", retry=" + this.retry + ", reconnectIntervalMillis=" + this.reconnectIntervalMillis + "]";
        }
    }
    
    private static class JmsManagerFactory implements ManagerFactory<JmsManager, JmsManagerConfiguration>
    {
        @Override
        public JmsManager createManager(final String name, final JmsManagerConfiguration data) {
            if (JndiManager.isJndiJmsEnabled()) {
                try {
                    return new JmsManager(name, data, null);
                }
                catch (final Exception e) {
                    AbstractManager.logger().error("Error creating JmsManager using JmsManagerConfiguration [{}]", data, e);
                    return null;
                }
            }
            AbstractManager.logger().error("JNDI must be enabled by setting log4j2.enableJndiJms=true");
            return null;
        }
    }
    
    private class Reconnector extends Log4jThread
    {
        private final CountDownLatch latch;
        private volatile boolean shutdown;
        private final Object owner;
        
        private Reconnector(final Object owner) {
            super("JmsManager-Reconnector");
            this.latch = new CountDownLatch(1);
            this.owner = owner;
        }
        
        public void latch() {
            try {
                this.latch.await();
            }
            catch (final InterruptedException ex) {}
        }
        
        void reconnect() throws NamingException, JMSException {
            final JndiManager jndiManager2 = JmsManager.this.getJndiManager();
            final Connection connection2 = JmsManager.this.createConnection(jndiManager2);
            final Session session2 = JmsManager.this.createSession(connection2);
            final Destination destination2 = JmsManager.this.createDestination(jndiManager2);
            final MessageProducer messageProducer2 = JmsManager.this.createMessageProducer(session2, destination2);
            connection2.start();
            synchronized (this.owner) {
                JmsManager.this.jndiManager = jndiManager2;
                JmsManager.this.connection = connection2;
                JmsManager.this.session = session2;
                JmsManager.this.destination = destination2;
                JmsManager.this.messageProducer = messageProducer2;
                JmsManager.this.reconnector = null;
                this.shutdown = true;
            }
            AbstractManager.logger().debug("Connection reestablished to {}", JmsManager.this.configuration);
        }
        
        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Thread.sleep(JmsManager.this.configuration.getReconnectIntervalMillis());
                    this.reconnect();
                }
                catch (final InterruptedException | JMSException | NamingException e) {
                    AbstractManager.logger().debug("Cannot reestablish JMS connection to {}: {}", JmsManager.this.configuration, e.getLocalizedMessage(), e);
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
}
