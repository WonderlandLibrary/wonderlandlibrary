/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.MessageProducer
 *  javax.jms.Queue
 *  javax.jms.QueueConnection
 *  javax.jms.QueueConnectionFactory
 *  javax.jms.QueueSender
 *  javax.jms.QueueSession
 *  javax.jms.Session
 */
package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.net.AbstractJMSManager;

public class JMSQueueManager
extends AbstractJMSManager {
    private static final JMSQueueManagerFactory FACTORY = new JMSQueueManagerFactory();
    private QueueInfo info;
    private final String factoryBindingName;
    private final String queueBindingName;
    private final String userName;
    private final String password;
    private final Context context;

    protected JMSQueueManager(String name, Context context, String factoryBindingName, String queueBindingName, String userName, String password, QueueInfo info) {
        super(name);
        this.context = context;
        this.factoryBindingName = factoryBindingName;
        this.queueBindingName = queueBindingName;
        this.userName = userName;
        this.password = password;
        this.info = info;
    }

    public static JMSQueueManager getJMSQueueManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password) {
        if (factoryBindingName == null) {
            LOGGER.error("No factory name provided for JMSQueueManager");
            return null;
        }
        if (queueBindingName == null) {
            LOGGER.error("No topic name provided for JMSQueueManager");
            return null;
        }
        String name = "JMSQueue:" + factoryBindingName + '.' + queueBindingName;
        return JMSQueueManager.getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password));
    }

    @Override
    public synchronized void send(Serializable object) throws Exception {
        if (this.info == null) {
            this.info = JMSQueueManager.connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
        }
        try {
            super.send(object, (Session)this.info.session, (MessageProducer)this.info.sender);
        }
        catch (Exception ex) {
            this.cleanup(true);
            throw ex;
        }
    }

    @Override
    public void releaseSub() {
        if (this.info != null) {
            this.cleanup(false);
        }
    }

    private void cleanup(boolean quiet) {
        block5: {
            block4: {
                try {
                    this.info.session.close();
                }
                catch (Exception e) {
                    if (quiet) break block4;
                    LOGGER.error("Error closing session for " + this.getName(), (Throwable)e);
                }
            }
            try {
                this.info.conn.close();
            }
            catch (Exception e) {
                if (quiet) break block5;
                LOGGER.error("Error closing connection for " + this.getName(), (Throwable)e);
            }
        }
        this.info = null;
    }

    private static QueueInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress) throws Exception {
        block4: {
            try {
                QueueConnectionFactory factory = (QueueConnectionFactory)JMSQueueManager.lookup(context, factoryBindingName);
                QueueConnection conn = userName != null ? factory.createQueueConnection(userName, password) : factory.createQueueConnection();
                QueueSession sess = conn.createQueueSession(false, 1);
                Queue queue = (Queue)JMSQueueManager.lookup(context, queueBindingName);
                QueueSender sender = sess.createSender(queue);
                conn.start();
                return new QueueInfo(conn, sess, sender);
            }
            catch (NamingException ex) {
                LOGGER.warn("Unable to locate connection factory " + factoryBindingName, (Throwable)ex);
                if (!suppress) {
                    throw ex;
                }
            }
            catch (JMSException ex) {
                LOGGER.warn("Unable to create connection to queue " + queueBindingName, (Throwable)ex);
                if (suppress) break block4;
                throw ex;
            }
        }
        return null;
    }

    private static class JMSQueueManagerFactory
    implements ManagerFactory<JMSQueueManager, FactoryData> {
        private JMSQueueManagerFactory() {
        }

        @Override
        public JMSQueueManager createManager(String name, FactoryData data) {
            try {
                Context ctx = AbstractJMSManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
                QueueInfo info = JMSQueueManager.connect(ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, true);
                return new JMSQueueManager(name, ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, info);
            }
            catch (NamingException ex) {
                LOGGER.error("Unable to locate resource", (Throwable)ex);
            }
            catch (Exception ex) {
                LOGGER.error("Unable to connect", (Throwable)ex);
            }
            return null;
        }
    }

    private static class QueueInfo {
        private final QueueConnection conn;
        private final QueueSession session;
        private final QueueSender sender;

        public QueueInfo(QueueConnection conn, QueueSession session, QueueSender sender) {
            this.conn = conn;
            this.session = session;
            this.sender = sender;
        }
    }

    private static class FactoryData {
        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String queueBindingName;
        private final String userName;
        private final String password;

        public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password) {
            this.factoryName = factoryName;
            this.providerURL = providerURL;
            this.urlPkgPrefixes = urlPkgPrefixes;
            this.securityPrincipalName = securityPrincipalName;
            this.securityCredentials = securityCredentials;
            this.factoryBindingName = factoryBindingName;
            this.queueBindingName = queueBindingName;
            this.userName = userName;
            this.password = password;
        }
    }
}

