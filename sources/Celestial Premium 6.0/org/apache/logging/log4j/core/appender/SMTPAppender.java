/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.HTMLLayout;
import org.apache.logging.log4j.core.net.SMTPManager;

@Plugin(name="SMTP", category="Core", elementType="appender", printObject=true)
public final class SMTPAppender
extends AbstractAppender {
    private static final int DEFAULT_BUFFER_SIZE = 512;
    protected final SMTPManager manager;

    private SMTPAppender(String name, Filter filter, Layout<? extends Serializable> layout, SMTPManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }

    @PluginFactory
    public static SMTPAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="to") String to, @PluginAttribute(value="cc") String cc, @PluginAttribute(value="bcc") String bcc, @PluginAttribute(value="from") String from, @PluginAttribute(value="replyTo") String replyTo, @PluginAttribute(value="subject") String subject, @PluginAttribute(value="smtpProtocol") String smtpProtocol, @PluginAttribute(value="smtpHost") String smtpHost, @PluginAttribute(value="smtpPort") String smtpPortStr, @PluginAttribute(value="smtpUsername") String smtpUsername, @PluginAttribute(value="smtpPassword") String smtpPassword, @PluginAttribute(value="smtpDebug") String smtpDebug, @PluginAttribute(value="bufferSize") String bufferSizeStr, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filter") Filter filter, @PluginAttribute(value="ignoreExceptions") String ignore) {
        SMTPManager manager;
        int bufferSize;
        if (name == null) {
            LOGGER.error("No name provided for SMTPAppender");
            return null;
        }
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
        boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
        int n = bufferSize = bufferSizeStr == null ? 512 : Integer.parseInt(bufferSizeStr);
        if (layout == null) {
            layout = HTMLLayout.createLayout(null, null, null, null, null, null);
        }
        if (filter == null) {
            filter = ThresholdFilter.createFilter(null, null, null);
        }
        if ((manager = SMTPManager.getSMTPManager(to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, filter.toString(), bufferSize)) == null) {
            return null;
        }
        return new SMTPAppender(name, filter, layout, manager, ignoreExceptions);
    }

    @Override
    public boolean isFiltered(LogEvent event) {
        boolean filtered = super.isFiltered(event);
        if (filtered) {
            this.manager.add(event);
        }
        return filtered;
    }

    @Override
    public void append(LogEvent event) {
        this.manager.sendEvents(this.getLayout(), event);
    }
}

