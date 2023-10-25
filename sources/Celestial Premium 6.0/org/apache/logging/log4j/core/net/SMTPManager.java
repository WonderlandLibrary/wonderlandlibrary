/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.activation.DataSource
 *  javax.mail.Authenticator
 *  javax.mail.BodyPart
 *  javax.mail.Message
 *  javax.mail.Message$RecipientType
 *  javax.mail.MessagingException
 *  javax.mail.Multipart
 *  javax.mail.PasswordAuthentication
 *  javax.mail.Session
 *  javax.mail.Transport
 *  javax.mail.internet.InternetHeaders
 *  javax.mail.internet.MimeBodyPart
 *  javax.mail.internet.MimeMessage
 *  javax.mail.internet.MimeMultipart
 *  javax.mail.internet.MimeUtility
 *  javax.mail.util.ByteArrayDataSource
 */
package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.CyclicBuffer;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.MimeMessageBuilder;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SMTPManager
extends AbstractManager {
    private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory();
    private final Session session;
    private final CyclicBuffer<LogEvent> buffer;
    private volatile MimeMessage message;
    private final FactoryData data;

    protected SMTPManager(String name, Session session, MimeMessage message, FactoryData data) {
        super(name);
        this.session = session;
        this.message = message;
        this.data = data;
        this.buffer = new CyclicBuffer<LogEvent>(LogEvent.class, data.numElements);
    }

    public void add(LogEvent event) {
        this.buffer.add(event);
    }

    public static SMTPManager getSMTPManager(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, String filterName, int numElements) {
        if (Strings.isEmpty(protocol)) {
            protocol = "smtp";
        }
        StringBuilder sb = new StringBuilder();
        if (to != null) {
            sb.append(to);
        }
        sb.append(":");
        if (cc != null) {
            sb.append(cc);
        }
        sb.append(":");
        if (bcc != null) {
            sb.append(bcc);
        }
        sb.append(":");
        if (from != null) {
            sb.append(from);
        }
        sb.append(":");
        if (replyTo != null) {
            sb.append(replyTo);
        }
        sb.append(":");
        if (subject != null) {
            sb.append(subject);
        }
        sb.append(":");
        sb.append(protocol).append(":").append(host).append(":").append("port").append(":");
        if (username != null) {
            sb.append(username);
        }
        sb.append(":");
        if (password != null) {
            sb.append(password);
        }
        sb.append(isDebug ? ":debug:" : "::");
        sb.append(filterName);
        String name = "SMTP:" + NameUtil.md5(sb.toString());
        return SMTPManager.getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subject, protocol, host, port, username, password, isDebug, numElements));
    }

    public void sendEvents(Layout<?> layout, LogEvent appendEvent) {
        if (this.message == null) {
            this.connect();
        }
        try {
            LogEvent[] priorEvents = this.buffer.removeAll();
            byte[] rawBytes = this.formatContentToBytes(priorEvents, appendEvent, layout);
            String contentType = layout.getContentType();
            String encoding = this.getEncoding(rawBytes, contentType);
            byte[] encodedBytes = this.encodeContentToBytes(rawBytes, encoding);
            InternetHeaders headers = this.getHeaders(contentType, encoding);
            MimeMultipart mp = this.getMimeMultipart(encodedBytes, headers);
            this.sendMultipartMessage(this.message, mp);
        }
        catch (MessagingException e) {
            LOGGER.error("Error occurred while sending e-mail notification.", (Throwable)e);
            throw new LoggingException("Error occurred while sending email", e);
        }
        catch (IOException e) {
            LOGGER.error("Error occurred while sending e-mail notification.", (Throwable)e);
            throw new LoggingException("Error occurred while sending email", e);
        }
        catch (RuntimeException e) {
            LOGGER.error("Error occurred while sending e-mail notification.", (Throwable)e);
            throw new LoggingException("Error occurred while sending email", e);
        }
    }

    protected byte[] formatContentToBytes(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout) throws IOException {
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        this.writeContent(priorEvents, appendEvent, layout, raw);
        return raw.toByteArray();
    }

    private void writeContent(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, ByteArrayOutputStream out) throws IOException {
        this.writeHeader(layout, out);
        this.writeBuffer(priorEvents, appendEvent, layout, out);
        this.writeFooter(layout, out);
    }

    protected void writeHeader(Layout<?> layout, OutputStream out) throws IOException {
        byte[] header = layout.getHeader();
        if (header != null) {
            out.write(header);
        }
    }

    protected void writeBuffer(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, OutputStream out) throws IOException {
        for (LogEvent priorEvent : priorEvents) {
            byte[] bytes = layout.toByteArray(priorEvent);
            out.write(bytes);
        }
        byte[] bytes = layout.toByteArray(appendEvent);
        out.write(bytes);
    }

    protected void writeFooter(Layout<?> layout, OutputStream out) throws IOException {
        byte[] footer = layout.getFooter();
        if (footer != null) {
            out.write(footer);
        }
    }

    protected String getEncoding(byte[] rawBytes, String contentType) {
        ByteArrayDataSource dataSource = new ByteArrayDataSource(rawBytes, contentType);
        return MimeUtility.getEncoding((DataSource)dataSource);
    }

    protected byte[] encodeContentToBytes(byte[] rawBytes, String encoding) throws MessagingException, IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        this.encodeContent(rawBytes, encoding, encoded);
        return encoded.toByteArray();
    }

    protected void encodeContent(byte[] bytes, String encoding, ByteArrayOutputStream out) throws MessagingException, IOException {
        OutputStream encoder = MimeUtility.encode((OutputStream)out, (String)encoding);
        encoder.write(bytes);
        encoder.close();
    }

    protected InternetHeaders getHeaders(String contentType, String encoding) {
        InternetHeaders headers = new InternetHeaders();
        headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
        headers.setHeader("Content-Transfer-Encoding", encoding);
        return headers;
    }

    protected MimeMultipart getMimeMultipart(byte[] encodedBytes, InternetHeaders headers) throws MessagingException {
        MimeMultipart mp = new MimeMultipart();
        MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
        mp.addBodyPart((BodyPart)part);
        return mp;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void sendMultipartMessage(MimeMessage message, MimeMultipart mp) throws MessagingException {
        MimeMessage mimeMessage = message;
        synchronized (mimeMessage) {
            message.setContent((Multipart)mp);
            message.setSentDate(new Date());
            Transport.send((Message)message);
        }
    }

    private synchronized void connect() {
        if (this.message != null) {
            return;
        }
        try {
            this.message = new MimeMessageBuilder(this.session).setFrom(this.data.from).setReplyTo(this.data.replyto).setRecipients(Message.RecipientType.TO, this.data.to).setRecipients(Message.RecipientType.CC, this.data.cc).setRecipients(Message.RecipientType.BCC, this.data.bcc).setSubject(this.data.subject).getMimeMessage();
        }
        catch (MessagingException e) {
            LOGGER.error("Could not set SMTPAppender message options.", (Throwable)e);
            this.message = null;
        }
    }

    private static class SMTPManagerFactory
    implements ManagerFactory<SMTPManager, FactoryData> {
        private SMTPManagerFactory() {
        }

        @Override
        public SMTPManager createManager(String name, FactoryData data) {
            MimeMessage message;
            Authenticator authenticator;
            String prefix = "mail." + data.protocol;
            Properties properties = PropertiesUtil.getSystemProperties();
            properties.put("mail.transport.protocol", data.protocol);
            if (properties.getProperty("mail.host") == null) {
                properties.put("mail.host", NetUtils.getLocalHostname());
            }
            if (null != data.host) {
                properties.put(prefix + ".host", data.host);
            }
            if (data.port > 0) {
                properties.put(prefix + ".port", String.valueOf(data.port));
            }
            if (null != (authenticator = this.buildAuthenticator(data.username, data.password))) {
                properties.put(prefix + ".auth", "true");
            }
            Session session = Session.getInstance((Properties)properties, (Authenticator)authenticator);
            session.setProtocolForAddress("rfc822", data.protocol);
            session.setDebug(data.isDebug);
            try {
                message = new MimeMessageBuilder(session).setFrom(data.from).setReplyTo(data.replyto).setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc).setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject).getMimeMessage();
            }
            catch (MessagingException e) {
                LOGGER.error("Could not set SMTPAppender message options.", (Throwable)e);
                message = null;
            }
            return new SMTPManager(name, session, message, data);
        }

        private Authenticator buildAuthenticator(final String username, final String password) {
            if (null != password && null != username) {
                return new Authenticator(){
                    private final PasswordAuthentication passwordAuthentication;
                    {
                        this.passwordAuthentication = new PasswordAuthentication(username, password);
                    }

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return this.passwordAuthentication;
                    }
                };
            }
            return null;
        }
    }

    private static class FactoryData {
        private final String to;
        private final String cc;
        private final String bcc;
        private final String from;
        private final String replyto;
        private final String subject;
        private final String protocol;
        private final String host;
        private final int port;
        private final String username;
        private final String password;
        private final boolean isDebug;
        private final int numElements;

        public FactoryData(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, int numElements) {
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
            this.from = from;
            this.replyto = replyTo;
            this.subject = subject;
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.isDebug = isDebug;
            this.numElements = numElements;
        }
    }
}

