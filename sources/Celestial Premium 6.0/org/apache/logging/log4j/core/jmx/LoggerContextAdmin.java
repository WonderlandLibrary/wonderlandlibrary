/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.jmx.LoggerContextAdminMBean;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContextAdmin
extends NotificationBroadcasterSupport
implements LoggerContextAdminMBean,
PropertyChangeListener {
    private static final int PAGE = 4096;
    private static final int TEXT_BUFFER = 65536;
    private static final int BUFFER_SIZE = 2048;
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private final AtomicLong sequenceNo = new AtomicLong();
    private final ObjectName objectName;
    private final LoggerContext loggerContext;
    private String customConfigText;

    public LoggerContextAdmin(LoggerContext loggerContext, Executor executor) {
        super(executor, LoggerContextAdmin.createNotificationInfo());
        this.loggerContext = Assert.isNotNull(loggerContext, "loggerContext");
        try {
            String ctxName = Server.escape(loggerContext.getName());
            String name = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s", ctxName);
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        loggerContext.addPropertyChangeListener(this);
    }

    private static MBeanNotificationInfo createNotificationInfo() {
        String[] notifTypes = new String[]{"com.apache.logging.log4j.core.jmx.config.reconfigured"};
        String name = Notification.class.getName();
        String description = "Configuration reconfigured";
        return new MBeanNotificationInfo(notifTypes, name, "Configuration reconfigured");
    }

    @Override
    public String getStatus() {
        return this.loggerContext.getStatus().toString();
    }

    @Override
    public String getName() {
        return this.loggerContext.getName();
    }

    private Configuration getConfig() {
        return this.loggerContext.getConfiguration();
    }

    @Override
    public String getConfigLocationURI() {
        if (this.loggerContext.getConfigLocation() != null) {
            return String.valueOf(this.loggerContext.getConfigLocation());
        }
        if (this.getConfigName() != null) {
            return String.valueOf(new File(this.getConfigName()).toURI());
        }
        return "";
    }

    @Override
    public void setConfigLocationURI(String configLocation) throws URISyntaxException, IOException {
        LOGGER.debug("---------");
        LOGGER.debug("Remote request to reconfigure using location " + configLocation);
        URI uri = new URI(configLocation);
        uri.toURL().openStream().close();
        this.loggerContext.setConfigLocation(uri);
        LOGGER.debug("Completed remote request to reconfigure.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"config".equals(evt.getPropertyName())) {
            return;
        }
        if (this.loggerContext.getConfiguration().getName() != null) {
            this.customConfigText = null;
        }
        Notification notif = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", this.getObjectName(), this.nextSeqNo(), this.now(), null);
        this.sendNotification(notif);
    }

    @Override
    public String getConfigText() throws IOException {
        return this.getConfigText(Charsets.UTF_8.name());
    }

    @Override
    public String getConfigText(String charsetName) throws IOException {
        if (this.customConfigText != null) {
            return this.customConfigText;
        }
        try {
            Charset charset = Charset.forName(charsetName);
            return this.readContents(new URI(this.getConfigLocationURI()), charset);
        }
        catch (Exception ex) {
            StringWriter sw = new StringWriter(2048);
            ex.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }

    @Override
    public void setConfigText(String configText, String charsetName) {
        String old = this.customConfigText;
        this.customConfigText = Assert.isNotNull(configText, "configText");
        LOGGER.debug("---------");
        LOGGER.debug("Remote request to reconfigure from config text.");
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(configText.getBytes(charsetName));
            ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(in);
            Configuration updated = ConfigurationFactory.getInstance().getConfiguration(source);
            this.loggerContext.start(updated);
            LOGGER.debug("Completed remote request to reconfigure from config text.");
        }
        catch (Exception ex) {
            this.customConfigText = old;
            String msg = "Could not reconfigure from config text";
            LOGGER.error("Could not reconfigure from config text", (Throwable)ex);
            throw new IllegalArgumentException("Could not reconfigure from config text", ex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String readContents(URI uri, Charset charset) throws IOException {
        String string;
        InputStream in = null;
        InputStreamReader reader = null;
        try {
            in = uri.toURL().openStream();
            reader = new InputStreamReader(in, charset);
            StringBuilder result = new StringBuilder(65536);
            char[] buff = new char[4096];
            int count = -1;
            while ((count = reader.read(buff)) >= 0) {
                result.append(buff, 0, count);
            }
            string = result.toString();
        }
        catch (Throwable throwable) {
            Closer.closeSilent(in);
            Closer.closeSilent(reader);
            throw throwable;
        }
        Closer.closeSilent(in);
        Closer.closeSilent(reader);
        return string;
    }

    @Override
    public String getConfigName() {
        return this.getConfig().getName();
    }

    @Override
    public String getConfigClassName() {
        return this.getConfig().getClass().getName();
    }

    @Override
    public String getConfigFilter() {
        return String.valueOf(this.getConfig().getFilter());
    }

    @Override
    public String getConfigMonitorClassName() {
        return this.getConfig().getConfigurationMonitor().getClass().getName();
    }

    @Override
    public Map<String, String> getConfigProperties() {
        return this.getConfig().getProperties();
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }

    private long now() {
        return System.currentTimeMillis();
    }
}

