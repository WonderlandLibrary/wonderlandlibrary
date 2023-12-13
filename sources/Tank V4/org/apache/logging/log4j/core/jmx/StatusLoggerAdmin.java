package org.apache.logging.log4j.core.jmx;

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
   private Level level;

   public StatusLoggerAdmin(Executor var1) {
      super(var1, new MBeanNotificationInfo[]{createNotificationInfo()});
      this.level = Level.WARN;

      try {
         this.objectName = new ObjectName("org.apache.logging.log4j2:type=StatusLogger");
      } catch (Exception var3) {
         throw new IllegalStateException(var3);
      }

      StatusLogger.getLogger().registerListener(this);
   }

   private static MBeanNotificationInfo createNotificationInfo() {
      String[] var0 = new String[]{"com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message"};
      String var1 = Notification.class.getName();
      String var2 = "StatusLogger has logged an event";
      return new MBeanNotificationInfo(var0, var1, "StatusLogger has logged an event");
   }

   public String[] getStatusDataHistory() {
      List var1 = this.getStatusData();
      String[] var2 = new String[var1.size()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = ((StatusData)var1.get(var3)).getFormattedStatus();
      }

      return var2;
   }

   public List getStatusData() {
      return StatusLogger.getLogger().getStatusData();
   }

   public String getLevel() {
      return this.level.name();
   }

   public Level getStatusLevel() {
      return this.level;
   }

   public void setLevel(String var1) {
      this.level = Level.toLevel(var1, Level.ERROR);
   }

   public void log(StatusData var1) {
      Notification var2 = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", this.getObjectName(), this.nextSeqNo(), this.now(), var1.getFormattedStatus());
      this.sendNotification(var2);
      Notification var3 = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", this.getObjectName(), this.nextSeqNo(), this.now());
      var3.setUserData(var1);
      this.sendNotification(var3);
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
