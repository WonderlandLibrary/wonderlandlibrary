// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.status.StatusData;
import javax.management.Notification;
import java.util.Iterator;
import org.apache.logging.log4j.status.StatusLogger;
import javax.management.MBeanNotificationInfo;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.Level;
import javax.management.ObjectName;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.status.StatusListener;
import javax.management.NotificationBroadcasterSupport;

public class StatusLoggerAdmin extends NotificationBroadcasterSupport implements StatusListener, StatusLoggerAdminMBean
{
    private final AtomicLong sequenceNo;
    private final ObjectName objectName;
    private final String contextName;
    private Level level;
    
    public StatusLoggerAdmin(final String contextName, final Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
        this.sequenceNo = new AtomicLong();
        this.level = Level.WARN;
        this.contextName = contextName;
        try {
            final String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", Server.escape(contextName));
            this.objectName = new ObjectName(mbeanName);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        this.removeListeners(contextName);
        StatusLogger.getLogger().registerListener(this);
    }
    
    private void removeListeners(final String ctxName) {
        final StatusLogger logger = StatusLogger.getLogger();
        final Iterable<StatusListener> listeners = logger.getListeners();
        for (final StatusListener statusListener : listeners) {
            if (statusListener instanceof StatusLoggerAdmin) {
                final StatusLoggerAdmin adminListener = (StatusLoggerAdmin)statusListener;
                if (ctxName == null || !ctxName.equals(adminListener.contextName)) {
                    continue;
                }
                logger.removeListener(adminListener);
            }
        }
    }
    
    private static MBeanNotificationInfo createNotificationInfo() {
        final String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
        final String name = Notification.class.getName();
        final String description = "StatusLogger has logged an event";
        return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
    }
    
    @Override
    public String[] getStatusDataHistory() {
        final List<StatusData> data = this.getStatusData();
        final String[] result = new String[data.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = data.get(i).getFormattedStatus();
        }
        return result;
    }
    
    @Override
    public List<StatusData> getStatusData() {
        return StatusLogger.getLogger().getStatusData();
    }
    
    @Override
    public String getLevel() {
        return this.level.name();
    }
    
    @Override
    public Level getStatusLevel() {
        return this.level;
    }
    
    @Override
    public void setLevel(final String level) {
        this.level = Level.toLevel(level, Level.ERROR);
    }
    
    @Override
    public String getContextName() {
        return this.contextName;
    }
    
    @Override
    public void log(final StatusData data) {
        final Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", this.getObjectName(), this.nextSeqNo(), this.nowMillis(), data.getFormattedStatus());
        this.sendNotification(notifMsg);
        final Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", this.getObjectName(), this.nextSeqNo(), this.nowMillis());
        notifData.setUserData(data);
        this.sendNotification(notifData);
    }
    
    @Override
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }
    
    private long nowMillis() {
        return System.currentTimeMillis();
    }
    
    @Override
    public void close() throws IOException {
    }
}
