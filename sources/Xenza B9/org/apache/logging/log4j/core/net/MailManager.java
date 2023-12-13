// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class MailManager extends AbstractManager
{
    static String createManagerName(final String to, final String cc, final String bcc, final String from, final String replyTo, final String subject, final String smtpProtocol, final String smtpHost, final int smtpPort, final String smtpUsername, final boolean smtpDebug, final String filterName) {
        final StringBuilder sb = new StringBuilder();
        if (to != null) {
            sb.append(to);
        }
        sb.append(':');
        if (cc != null) {
            sb.append(cc);
        }
        sb.append(':');
        if (bcc != null) {
            sb.append(bcc);
        }
        sb.append(':');
        if (from != null) {
            sb.append(from);
        }
        sb.append(':');
        if (replyTo != null) {
            sb.append(replyTo);
        }
        sb.append(':');
        if (subject != null) {
            sb.append(subject);
        }
        sb.append(':');
        sb.append(smtpProtocol).append(':').append(smtpHost).append(':').append(smtpPort).append(':');
        if (smtpUsername != null) {
            sb.append(smtpUsername);
        }
        sb.append(smtpDebug ? ":debug:" : "::");
        sb.append(filterName);
        return "SMTP:" + sb.toString();
    }
    
    public MailManager(final LoggerContext loggerContext, final String name) {
        super(loggerContext, name);
    }
    
    public abstract void add(final LogEvent event);
    
    public abstract void sendEvents(final Layout<?> layout, final LogEvent appendEvent);
    
    public static class FactoryData
    {
        private final String to;
        private final String cc;
        private final String bcc;
        private final String from;
        private final String replyTo;
        private final String subject;
        private final AbstractStringLayout.Serializer subjectSerializer;
        private final String smtpProtocol;
        private final String smtpHost;
        private final int smtpPort;
        private final String smtpUsername;
        private final String smtpPassword;
        private final boolean smtpDebug;
        private final int bufferSize;
        private final SslConfiguration sslConfiguration;
        private final String filterName;
        private final String managerName;
        
        public FactoryData(final String to, final String cc, final String bcc, final String from, final String replyTo, final String subject, final AbstractStringLayout.Serializer subjectSerializer, final String smtpProtocol, final String smtpHost, final int smtpPort, final String smtpUsername, final String smtpPassword, final boolean smtpDebug, final int bufferSize, final SslConfiguration sslConfiguration, final String filterName) {
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
            this.from = from;
            this.replyTo = replyTo;
            this.subject = subject;
            this.subjectSerializer = subjectSerializer;
            this.smtpProtocol = smtpProtocol;
            this.smtpHost = smtpHost;
            this.smtpPort = smtpPort;
            this.smtpUsername = smtpUsername;
            this.smtpPassword = smtpPassword;
            this.smtpDebug = smtpDebug;
            this.bufferSize = bufferSize;
            this.sslConfiguration = sslConfiguration;
            this.filterName = filterName;
            this.managerName = MailManager.createManagerName(to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpDebug, filterName);
        }
        
        public String getTo() {
            return this.to;
        }
        
        public String getCc() {
            return this.cc;
        }
        
        public String getBcc() {
            return this.bcc;
        }
        
        public String getFrom() {
            return this.from;
        }
        
        public String getReplyTo() {
            return this.replyTo;
        }
        
        public String getSubject() {
            return this.subject;
        }
        
        public AbstractStringLayout.Serializer getSubjectSerializer() {
            return this.subjectSerializer;
        }
        
        public String getSmtpProtocol() {
            return this.smtpProtocol;
        }
        
        public String getSmtpHost() {
            return this.smtpHost;
        }
        
        public int getSmtpPort() {
            return this.smtpPort;
        }
        
        public String getSmtpUsername() {
            return this.smtpUsername;
        }
        
        public String getSmtpPassword() {
            return this.smtpPassword;
        }
        
        public boolean isSmtpDebug() {
            return this.smtpDebug;
        }
        
        public int getBufferSize() {
            return this.bufferSize;
        }
        
        public SslConfiguration getSslConfiguration() {
            return this.sslConfiguration;
        }
        
        public String getFilterName() {
            return this.filterName;
        }
        
        public String getManagerName() {
            return this.managerName;
        }
    }
}
