/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.MessageListener
 *  javax.jms.Queue
 *  javax.jms.QueueConnection
 *  javax.jms.QueueConnectionFactory
 *  javax.jms.QueueReceiver
 *  javax.jms.QueueSession
 */
package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.net.AbstractJMSReceiver;

public class JMSQueueReceiver
extends AbstractJMSReceiver {
    public JMSQueueReceiver(String qcfBindingName, String queueBindingName, String username, String password) {
        try {
            InitialContext ctx = new InitialContext();
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)this.lookup(ctx, qcfBindingName);
            QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(username, password);
            queueConnection.start();
            QueueSession queueSession = queueConnection.createQueueSession(false, 1);
            Queue queue = (Queue)ctx.lookup(queueBindingName);
            QueueReceiver queueReceiver = queueSession.createReceiver(queue);
            queueReceiver.setMessageListener((MessageListener)this);
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
            JMSQueueReceiver.usage("Wrong number of arguments.");
        }
        String qcfBindingName = args[0];
        String queueBindingName = args[1];
        String username = args[2];
        String password = args[3];
        new JMSQueueReceiver(qcfBindingName, queueBindingName, username, password);
        Charset enc = Charset.defaultCharset();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
        System.out.println("Type \"exit\" to quit JMSQueueReceiver.");
        while ((line = stdin.readLine()) != null && !line.equalsIgnoreCase("exit")) {
        }
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }

    private static void usage(String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + JMSQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
        System.exit(1);
    }
}

