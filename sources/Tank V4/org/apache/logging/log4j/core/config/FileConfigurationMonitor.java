package org.apache.logging.log4j.core.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FileConfigurationMonitor implements ConfigurationMonitor {
   private static final int MASK = 15;
   private static final int MIN_INTERVAL = 5;
   private static final int MILLIS_PER_SECOND = 1000;
   private final File file;
   private long lastModified;
   private final List listeners;
   private final int interval;
   private long nextCheck;
   private volatile int counter = 0;
   private final Reconfigurable reconfigurable;

   public FileConfigurationMonitor(Reconfigurable var1, File var2, List var3, int var4) {
      this.reconfigurable = var1;
      this.file = var2;
      this.lastModified = var2.lastModified();
      this.listeners = var3;
      this.interval = (var4 < 5 ? 5 : var4) * 1000;
      this.nextCheck = System.currentTimeMillis() + (long)var4;
   }

   public void checkConfiguration() {
      if ((++this.counter & 15) == 0) {
         synchronized(this){}
         long var2 = System.currentTimeMillis();
         if (var2 >= this.nextCheck) {
            this.nextCheck = var2 + (long)this.interval;
            if (this.file.lastModified() > this.lastModified) {
               this.lastModified = this.file.lastModified();
               Iterator var4 = this.listeners.iterator();

               while(var4.hasNext()) {
                  ConfigurationListener var5 = (ConfigurationListener)var4.next();
                  var5.onChange(this.reconfigurable);
               }
            }
         }
      }

   }
}
