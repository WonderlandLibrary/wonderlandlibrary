/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.MessageProducer
 *  javax.jms.Session
 *  javax.jms.Topic
 *  javax.jms.TopicConnection
 *  javax.jms.TopicConnectionFactory
 *  javax.jms.TopicPublisher
 *  javax.jms.TopicSession
 */
package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.net.AbstractJMSManager;

public class JMSTopicManager
extends AbstractJMSManager {
    private static final JMSTopicManagerFactory FACTORY = new JMSTopicManagerFactory();
    private TopicInfo info;
    private final String factoryBindingName;
    private final String topicBindingName;
    private final String userName;
    private final String password;
    private final Context context;

    protected JMSTopicManager(String name, Context context, String factoryBindingName, String topicBindingName, String userName, String password, TopicInfo info) {
        super(name);
        this.context = context;
        this.factoryBindingName = factoryBindingName;
        this.topicBindingName = topicBindingName;
        this.userName = userName;
        this.password = password;
        this.info = info;
    }

    public static JMSTopicManager getJMSTopicManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password) {
        if (factoryBindingName == null) {
            LOGGER.error("No factory name provided for JMSTopicManager");
            return null;
        }
        if (topicBindingName == null) {
            LOGGER.error("No topic name provided for JMSTopicManager");
            return null;
        }
        String name = "JMSTopic:" + factoryBindingName + '.' + topicBindingName;
        return JMSTopicManager.getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password));
    }

    @Override
    public void send(Serializable object) throws Exception {
        if (this.info == null) {
            this.info = JMSTopicManager.connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
        }
        try {
            super.send(object, (Session)this.info.session, (MessageProducer)this.info.publisher);
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

    private static TopicInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress) throws Exception {
        block4: {
            try {
                TopicConnectionFactory factory = (TopicConnectionFactory)JMSTopicManager.lookup(context, factoryBindingName);
                TopicConnection conn = userName != null ? factory.createTopicConnection(userName, password) : factory.createTopicConnection();
                TopicSession sess = conn.createTopicSession(false, 1);
                Topic topic = (Topic)JMSTopicManager.lookup(context, queueBindingName);
                TopicPublisher publisher = sess.createPublisher(topic);
                conn.start();
                return new TopicInfo(conn, sess, publisher);
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

    private static class JMSTopicManagerFactory
    implements ManagerFactory<JMSTopicManager, FactoryData> {
        private JMSTopicManagerFactory() {
        }

        @Override
        public JMSTopicManager createManager(String name, FactoryData data) {
            try {
                Context ctx = AbstractJMSManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
                TopicInfo info = JMSTopicManager.connect(ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, true);
                return new JMSTopicManager(name, ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, info);
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

    private static class TopicInfo {
        private final TopicConnection conn;
        private final TopicSession session;
        private final TopicPublisher publisher;

        public TopicInfo(TopicConnection conn, TopicSession session, TopicPublisher publisher) {
            this.conn = conn;
            this.session = session;
            this.publisher = publisher;
        }
    }

    private static class FactoryData {
        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String topicBindingName;
        private final String userName;
        private final String password;

        public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password) {
            this.factoryName = factoryName;
            this.providerURL = providerURL;
            this.urlPkgPrefixes = urlPkgPrefixes;
            this.securityPrincipalName = securityPrincipalName;
            this.securityCredentials = securityCredentials;
            this.factoryBindingName = factoryBindingName;
            this.topicBindingName = topicBindingName;
            this.userName = userName;
            this.password = password;
        }
    }
}

