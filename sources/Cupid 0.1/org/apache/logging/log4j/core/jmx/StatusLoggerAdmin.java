package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class StatusLoggerAdmin extends NotificationBroadcasterSupport implements StatusListener, StatusLoggerAdminMBean {
  private final AtomicLong sequenceNo = new AtomicLong();
  
  private final ObjectName objectName;
  
  private final String contextName;
  
  private Level level = Level.WARN;
  
  public StatusLoggerAdmin(String contextName, Executor executor) {
    super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
    this.contextName = contextName;
    try {
      String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { Server.escape(contextName) });
      this.objectName = new ObjectName(mbeanName);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
    removeListeners(contextName);
    StatusLogger.getLogger().registerListener(this);
  }
  
  private void removeListeners(String ctxName) {
    StatusLogger logger = StatusLogger.getLogger();
    Iterable<StatusListener> listeners = logger.getListeners();
    for (StatusListener statusListener : listeners) {
      if (statusListener instanceof StatusLoggerAdmin) {
        StatusLoggerAdmin adminListener = (StatusLoggerAdmin)statusListener;
        if (ctxName != null && ctxName.equals(adminListener.contextName))
          logger.removeListener(adminListener); 
      } 
    } 
  }
  
  private static MBeanNotificationInfo createNotificationInfo() {
    String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
    String name = Notification.class.getName();
    String description = "StatusLogger has logged an event";
    return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
  }
  
  public String[] getStatusDataHistory() {
    List<StatusData> data = getStatusData();
    String[] result = new String[data.size()];
    for (int i = 0; i < result.length; i++)
      result[i] = ((StatusData)data.get(i)).getFormattedStatus(); 
    return result;
  }
  
  public List<StatusData> getStatusData() {
    return StatusLogger.getLogger().getStatusData();
  }
  
  public String getLevel() {
    return this.level.name();
  }
  
  public Level getStatusLevel() {
    return this.level;
  }
  
  public void setLevel(String level) {
    this.level = Level.toLevel(level, Level.ERROR);
  }
  
  public String getContextName() {
    return this.contextName;
  }
  
  public void log(StatusData data) {
    Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", getObjectName(), nextSeqNo(), nowMillis(), data.getFormattedStatus());
    sendNotification(notifMsg);
    Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", getObjectName(), nextSeqNo(), nowMillis());
    notifData.setUserData(data);
    sendNotification(notifData);
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
  
  private long nextSeqNo() {
    return this.sequenceNo.getAndIncrement();
  }
  
  private long nowMillis() {
    return System.currentTimeMillis();
  }
  
  public void close() throws IOException {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\StatusLoggerAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */