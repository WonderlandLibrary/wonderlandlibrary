/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.MessageListener
 *  javax.jms.Topic
 *  javax.jms.TopicConnection
 *  javax.jms.TopicConnectionFactory
 *  javax.jms.TopicSession
 *  javax.jms.TopicSubscriber
 */
package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.net.AbstractJMSReceiver;

public class JMSTopicReceiver
extends AbstractJMSReceiver {
    public JMSTopicReceiver(String tcfBindingName, String topicBindingName, String username, String password) {
        try {
            InitialContext ctx = new InitialContext();
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)this.lookup(ctx, tcfBindingName);
            TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
            topicConnection.start();
            TopicSession topicSession = topicConnection.createTopicSession(false, 1);
            Topic topic = (Topic)ctx.lookup(topicBindingName);
            TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            topicSubscriber.setMessageListener((MessageListener)this);
        }
        catch (JMSException e) {
            this.logger.error("Could not read JMS message.", (Throwable)e);
        }
        catch (NamingException e) {
            this.logger.error("Could not read JMS message.", (Throwable)e);
        }
        catch (RuntimeException e) {
            this.logger.error("Could not read JMS message.", (Throwable)e);
        }
    }

    public static void main(String[] args) throws Exception {
        String line;
        if (args.length != 4) {
            JMSTopicReceiver.usage("Wrong number of arguments.");
        }
        String tcfBindingName = args[0];
        String topicBindingName = args[1];
        String username = args[2];
        String password = args[3];
        new JMSTopicReceiver(tcfBindingName, topicBindingName, username, password);
        Charset enc = Charset.defaultCharset();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
        System.out.println("Type \"exit\" to quit JMSTopicReceiver.");
        while ((line = stdin.readLine()) != null && !line.equalsIgnoreCase("exit")) {
        }
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }

    private static void usage(String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + JMSTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
        System.exit(1);
    }
}

