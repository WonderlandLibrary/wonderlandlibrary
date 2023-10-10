// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import org.apache.logging.log4j.core.util.Closer;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.net.URL;
import java.io.InputStream;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.io.FileInputStream;
import java.io.File;
import org.apache.logging.log4j.core.config.Configuration;
import javax.management.Notification;
import java.util.Objects;
import javax.management.MBeanNotificationInfo;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.core.LoggerContext;
import javax.management.ObjectName;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.status.StatusLogger;
import java.beans.PropertyChangeListener;
import javax.management.NotificationBroadcasterSupport;

public class LoggerContextAdmin extends NotificationBroadcasterSupport implements LoggerContextAdminMBean, PropertyChangeListener
{
    private static final int PAGE = 4096;
    private static final int TEXT_BUFFER = 65536;
    private static final int BUFFER_SIZE = 2048;
    private static final StatusLogger LOGGER;
    private final AtomicLong sequenceNo;
    private final ObjectName objectName;
    private final LoggerContext loggerContext;
    
    public LoggerContextAdmin(final LoggerContext loggerContext, final Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
        this.sequenceNo = new AtomicLong();
        this.loggerContext = Objects.requireNonNull(loggerContext, "loggerContext");
        try {
            final String ctxName = Server.escape(loggerContext.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s", ctxName);
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        loggerContext.addPropertyChangeListener(this);
    }
    
    private static MBeanNotificationInfo createNotificationInfo() {
        final String[] notifTypes = { "com.apache.logging.log4j.core.jmx.config.reconfigured" };
        final String name = Notification.class.getName();
        final String description = "Configuration reconfigured";
        return new MBeanNotificationInfo(notifTypes, name, "Configuration reconfigured");
    }
    
    @Override
    public String getStatus() {
        return this.loggerContext.getState().toString();
    }
    
    @Override
    public String getName() {
        return this.loggerContext.getName();
    }
    
    private Configuration getConfig() {
        return this.loggerContext.getConfiguration();
    }
    
    @Override
    public String getConfigLocationUri() {
        if (this.loggerContext.getConfigLocation() != null) {
            return String.valueOf(this.loggerContext.getConfigLocation());
        }
        if (this.getConfigName() != null) {
            return String.valueOf(new File(this.getConfigName()).toURI());
        }
        return "";
    }
    
    @Override
    public void setConfigLocationUri(final String configLocation) throws URISyntaxException, IOException {
        if (configLocation == null || configLocation.isEmpty()) {
            throw new IllegalArgumentException("Missing configuration location");
        }
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure using location " + configLocation);
        final File configFile = new File(configLocation);
        ConfigurationSource configSource = null;
        if (configFile.exists()) {
            LoggerContextAdmin.LOGGER.debug("Opening config file {}", configFile.getAbsolutePath());
            configSource = new ConfigurationSource(new FileInputStream(configFile), configFile);
        }
        else {
            final URL configURL = new URL(configLocation);
            LoggerContextAdmin.LOGGER.debug("Opening config URL {}", configURL);
            configSource = new ConfigurationSource(configURL.openStream(), configURL);
        }
        final Configuration config = ConfigurationFactory.getInstance().getConfiguration(this.loggerContext, configSource);
        this.loggerContext.start(config);
        LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure.");
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!"config".equals(evt.getPropertyName())) {
            return;
        }
        final Notification notif = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", this.getObjectName(), this.nextSeqNo(), this.now(), null);
        this.sendNotification(notif);
    }
    
    @Override
    public String getConfigText() throws IOException {
        return this.getConfigText(StandardCharsets.UTF_8.name());
    }
    
    @Override
    public String getConfigText(final String charsetName) throws IOException {
        try {
            final ConfigurationSource source = this.loggerContext.getConfiguration().getConfigurationSource();
            final ConfigurationSource copy = source.resetInputStream();
            final Charset charset = Charset.forName(charsetName);
            return this.readContents(copy.getInputStream(), charset);
        }
        catch (Exception ex) {
            final StringWriter sw = new StringWriter(2048);
            ex.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
    
    private String readContents(final InputStream in, final Charset charset) throws IOException {
        Reader reader = null;
        try {
            reader = new InputStreamReader(in, charset);
            final StringBuilder result = new StringBuilder(65536);
            final char[] buff = new char[4096];
            int count = -1;
            while ((count = reader.read(buff)) >= 0) {
                result.append(buff, 0, count);
            }
            return result.toString();
        }
        finally {
            Closer.closeSilently(in);
            Closer.closeSilently(reader);
        }
    }
    
    @Override
    public void setConfigText(final String configText, final String charsetName) {
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure from config text.");
        try {
            final InputStream in = new ByteArrayInputStream(configText.getBytes(charsetName));
            final ConfigurationSource source = new ConfigurationSource(in);
            final Configuration updated = ConfigurationFactory.getInstance().getConfiguration(this.loggerContext, source);
            this.loggerContext.start(updated);
            LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure from config text.");
        }
        catch (Exception ex) {
            final String msg = "Could not reconfigure from config text";
            LoggerContextAdmin.LOGGER.error("Could not reconfigure from config text", ex);
            throw new IllegalArgumentException("Could not reconfigure from config text", ex);
        }
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
    public Map<String, String> getConfigProperties() {
        return this.getConfig().getProperties();
    }
    
    @Override
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }
    
    private long now() {
        return System.currentTimeMillis();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
