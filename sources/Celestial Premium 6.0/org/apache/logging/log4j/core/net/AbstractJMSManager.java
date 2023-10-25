/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageProducer
 *  javax.jms.ObjectMessage
 *  javax.jms.Session
 *  javax.jms.TextMessage
 */
package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractJMSManager
extends AbstractManager {
    public AbstractJMSManager(String name) {
        super(name);
    }

    protected static Context createContext(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials) throws NamingException {
        Properties props = AbstractJMSManager.getEnvironment(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials);
        return new InitialContext(props);
    }

    protected static Object lookup(Context ctx, String name) throws NamingException {
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            LOGGER.warn("Could not find name [" + name + "].");
            throw e;
        }
    }

    protected static Properties getEnvironment(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials) {
        Properties props = new Properties();
        if (factoryName != null) {
            props.put("java.naming.factory.initial", factoryName);
            if (providerURL != null) {
                props.put("java.naming.provider.url", providerURL);
            } else {
                LOGGER.warn("The InitialContext factory name has been provided without a ProviderURL. This is likely to cause problems");
            }
            if (urlPkgPrefixes != null) {
                props.put("java.naming.factory.url.pkgs", urlPkgPrefixes);
            }
            if (securityPrincipalName != null) {
                props.put("java.naming.security.principal", securityPrincipalName);
                if (securityCredentials != null) {
                    props.put("java.naming.security.credentials", securityCredentials);
                } else {
                    LOGGER.warn("SecurityPrincipalName has been set without SecurityCredentials. This is likely to cause problems.");
                }
            }
            return props;
        }
        return null;
    }

    public abstract void send(Serializable var1) throws Exception;

    public synchronized void send(Serializable object, Session session, MessageProducer producer) throws Exception {
        try {
            TextMessage msg;
            if (object instanceof String) {
                msg = session.createTextMessage();
                msg.setText((String)((Object)object));
            } else {
                msg = session.createObjectMessage();
                ((ObjectMessage)msg).setObject(object);
            }
            producer.send((Message)msg);
        }
        catch (JMSException ex) {
            LOGGER.error("Could not publish message via JMS " + this.getName());
            throw ex;
        }
    }
}

