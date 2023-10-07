// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.util.Hashtable;
import javax.mail.PasswordAuthentication;
import javax.net.ssl.SSLSocketFactory;
import javax.mail.Authenticator;
import java.util.Properties;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import javax.mail.Transport;
import java.util.Date;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.activation.DataSource;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetHeaders;
import java.io.IOException;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.LoggerContext;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.CyclicBuffer;
import javax.mail.Session;

public class SmtpManager extends MailManager
{
    public static final SMTPManagerFactory FACTORY;
    private final Session session;
    private final CyclicBuffer<LogEvent> buffer;
    private volatile MimeMessage message;
    private final FactoryData data;
    
    private static MimeMessage createMimeMessage(final FactoryData data, final Session session, final LogEvent appendEvent) throws MessagingException {
        return new MimeMessageBuilder(session).setFrom(data.getFrom()).setReplyTo(data.getReplyTo()).setRecipients(Message.RecipientType.TO, data.getTo()).setRecipients(Message.RecipientType.CC, data.getCc()).setRecipients(Message.RecipientType.BCC, data.getBcc()).setSubject(data.getSubjectSerializer().toSerializable(appendEvent)).build();
    }
    
    protected SmtpManager(final String name, final Session session, final MimeMessage message, final FactoryData data) {
        super(null, name);
        this.session = session;
        this.message = message;
        this.data = data;
        this.buffer = new CyclicBuffer<LogEvent>(LogEvent.class, data.getBufferSize());
    }
    
    @Override
    public void add(final LogEvent event) {
        this.buffer.add(event.toImmutable());
    }
    
    @Deprecated
    public static SmtpManager getSmtpManager(final Configuration config, final String to, final String cc, final String bcc, final String from, final String replyTo, final String subject, final String protocol, final String host, final int port, final String username, final String password, final boolean isDebug, final String filterName, final int numElements, final SslConfiguration sslConfiguration) {
        final AbstractStringLayout.Serializer subjectSerializer = PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(subject).build();
        final FactoryData data = new FactoryData(to, cc, bcc, from, replyTo, subject, subjectSerializer, protocol, host, port, username, password, isDebug, numElements, sslConfiguration, filterName);
        return AbstractManager.getManager(data.getManagerName(), (ManagerFactory<SmtpManager, FactoryData>)SmtpManager.FACTORY, data);
    }
    
    @Override
    public void sendEvents(final Layout<?> layout, final LogEvent appendEvent) {
        if (this.message == null) {
            this.connect(appendEvent);
        }
        try {
            final LogEvent[] priorEvents = this.removeAllBufferedEvents();
            final byte[] rawBytes = this.formatContentToBytes(priorEvents, appendEvent, layout);
            final String contentType = layout.getContentType();
            final String encoding = this.getEncoding(rawBytes, contentType);
            final byte[] encodedBytes = this.encodeContentToBytes(rawBytes, encoding);
            final InternetHeaders headers = this.getHeaders(contentType, encoding);
            final MimeMultipart mp = this.getMimeMultipart(encodedBytes, headers);
            final String subject = this.data.getSubjectSerializer().toSerializable(appendEvent);
            this.sendMultipartMessage(this.message, mp, subject);
        }
        catch (final MessagingException | IOException | RuntimeException e) {
            this.logError("Caught exception while sending e-mail notification.", e);
            throw new LoggingException("Error occurred while sending email", e);
        }
    }
    
    LogEvent[] removeAllBufferedEvents() {
        return this.buffer.removeAll();
    }
    
    protected byte[] formatContentToBytes(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout) throws IOException {
        final ByteArrayOutputStream raw = new ByteArrayOutputStream();
        this.writeContent(priorEvents, appendEvent, layout, raw);
        return raw.toByteArray();
    }
    
    private void writeContent(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout, final ByteArrayOutputStream out) throws IOException {
        this.writeHeader(layout, out);
        this.writeBuffer(priorEvents, appendEvent, layout, out);
        this.writeFooter(layout, out);
    }
    
    protected void writeHeader(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] header = layout.getHeader();
        if (header != null) {
            out.write(header);
        }
    }
    
    protected void writeBuffer(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout, final OutputStream out) throws IOException {
        for (final LogEvent priorEvent : priorEvents) {
            final byte[] bytes = layout.toByteArray(priorEvent);
            out.write(bytes);
        }
        final byte[] bytes2 = layout.toByteArray(appendEvent);
        out.write(bytes2);
    }
    
    protected void writeFooter(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] footer = layout.getFooter();
        if (footer != null) {
            out.write(footer);
        }
    }
    
    protected String getEncoding(final byte[] rawBytes, final String contentType) {
        final DataSource dataSource = (DataSource)new ByteArrayDataSource(rawBytes, contentType);
        return MimeUtility.getEncoding(dataSource);
    }
    
    protected byte[] encodeContentToBytes(final byte[] rawBytes, final String encoding) throws MessagingException, IOException {
        final ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        this.encodeContent(rawBytes, encoding, encoded);
        return encoded.toByteArray();
    }
    
    protected void encodeContent(final byte[] bytes, final String encoding, final ByteArrayOutputStream out) throws MessagingException, IOException {
        try (final OutputStream encoder = MimeUtility.encode((OutputStream)out, encoding)) {
            encoder.write(bytes);
        }
    }
    
    protected InternetHeaders getHeaders(final String contentType, final String encoding) {
        final InternetHeaders headers = new InternetHeaders();
        headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
        headers.setHeader("Content-Transfer-Encoding", encoding);
        return headers;
    }
    
    protected MimeMultipart getMimeMultipart(final byte[] encodedBytes, final InternetHeaders headers) throws MessagingException {
        final MimeMultipart mp = new MimeMultipart();
        final MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
        mp.addBodyPart((BodyPart)part);
        return mp;
    }
    
    @Deprecated
    protected void sendMultipartMessage(final MimeMessage msg, final MimeMultipart mp) throws MessagingException {
        synchronized (msg) {
            msg.setContent((Multipart)mp);
            msg.setSentDate(new Date());
            Transport.send((Message)msg);
        }
    }
    
    protected void sendMultipartMessage(final MimeMessage msg, final MimeMultipart mp, final String subject) throws MessagingException {
        synchronized (msg) {
            msg.setContent((Multipart)mp);
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            Transport.send((Message)msg);
        }
    }
    
    private synchronized void connect(final LogEvent appendEvent) {
        if (this.message != null) {
            return;
        }
        try {
            this.message = createMimeMessage(this.data, this.session, appendEvent);
        }
        catch (final MessagingException e) {
            this.logError("Could not set SmtpAppender message options", (Throwable)e);
            this.message = null;
        }
    }
    
    static {
        FACTORY = new SMTPManagerFactory();
    }
    
    public static class SMTPManagerFactory implements MailManagerFactory
    {
        @Override
        public SmtpManager createManager(final String name, final FactoryData data) {
            final String smtpProtocol = data.getSmtpProtocol();
            final String prefix = "mail." + smtpProtocol;
            final Properties properties = PropertiesUtil.getSystemProperties();
            properties.setProperty("mail.transport.protocol", smtpProtocol);
            if (properties.getProperty("mail.host") == null) {
                properties.setProperty("mail.host", NetUtils.getLocalHostname());
            }
            final String smtpHost = data.getSmtpHost();
            if (null != smtpHost) {
                properties.setProperty(prefix + ".host", smtpHost);
            }
            if (data.getSmtpPort() > 0) {
                properties.setProperty(prefix + ".port", String.valueOf(data.getSmtpPort()));
            }
            final Authenticator authenticator = this.buildAuthenticator(data.getSmtpUsername(), data.getSmtpPassword());
            if (null != authenticator) {
                properties.setProperty(prefix + ".auth", "true");
            }
            if (smtpProtocol.equals("smtps")) {
                final SslConfiguration sslConfiguration = data.getSslConfiguration();
                if (sslConfiguration != null) {
                    final SSLSocketFactory sslSocketFactory = sslConfiguration.getSslSocketFactory();
                    ((Hashtable<String, SSLSocketFactory>)properties).put(prefix + ".ssl.socketFactory", sslSocketFactory);
                    properties.setProperty(prefix + ".ssl.checkserveridentity", Boolean.toString(sslConfiguration.isVerifyHostName()));
                }
            }
            final Session session = Session.getInstance(properties, authenticator);
            session.setProtocolForAddress("rfc822", smtpProtocol);
            session.setDebug(data.isSmtpDebug());
            return new SmtpManager(name, session, null, data);
        }
        
        private Authenticator buildAuthenticator(final String username, final String password) {
            if (null != password && null != username) {
                return new Authenticator() {
                    private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
                    
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return this.passwordAuthentication;
                    }
                };
            }
            return null;
        }
    }
}
