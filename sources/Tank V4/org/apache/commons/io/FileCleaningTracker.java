package org.apache.commons.io;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FileCleaningTracker {
   ReferenceQueue q = new ReferenceQueue();
   final Collection trackers = Collections.synchronizedSet(new HashSet());
   final List deleteFailures = Collections.synchronizedList(new ArrayList());
   volatile boolean exitWhenFinished = false;
   Thread reaper;

   public void track(File var1, Object var2) {
      this.track(var1, var2, (FileDeleteStrategy)null);
   }

   public void track(File var1, Object var2, FileDeleteStrategy var3) {
      if (var1 == null) {
         throw new NullPointerException("The file must not be null");
      } else {
         this.addTracker(var1.getPath(), var2, var3);
      }
   }

   public void track(String var1, Object var2) {
      this.track(var1, var2, (FileDeleteStrategy)null);
   }

   public void track(String var1, Object var2, FileDeleteStrategy var3) {
      if (var1 == null) {
         throw new NullPointerException("The path must not be null");
      } else {
         this.addTracker(var1, var2, var3);
      }
   }

   private synchronized void addTracker(String var1, Object var2, FileDeleteStrategy var3) {
      if (this.exitWhenFinished) {
         throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
      } else {
         if (this.reaper == null) {
            this.reaper = new FileCleaningTracker.Reaper(this);
            this.reaper.start();
         }

         this.trackers.add(new FileCleaningTracker.Tracker(var1, var3, var2, this.q));
      }
   }

   public int getTrackCount() {
      return this.trackers.size();
   }

   public List getDeleteFailures() {
      return this.deleteFailures;
   }

   public synchronized void exitWhenFinished() {
      this.exitWhenFinished = true;
      if (this.reaper != null) {
         Thread var1;
         synchronized(var1 = this.reaper){}
         this.reaper.interrupt();
      }

   }

   private static final class Tracker extends PhantomReference {
      private final String path;
      private final FileDeleteStrategy deleteStrategy;

      Tracker(String var1, FileDeleteStrategy var2, Object var3, ReferenceQueue var4) {
         super(var3, var4);
         this.path = var1;
         this.deleteStrategy = var2 == null ? FileDeleteStrategy.NORMAL : var2;
      }

      public String getPath() {
         return this.path;
      }

      public boolean delete() {
         return this.deleteStrategy.deleteQuietly(new File(this.path));
      }
   }

   private final class Reaper extends Thread {
      final FileCleaningTracker this$0;

      Reaper(FileCleaningTracker var1) {
         super("File Reaper");
         this.this$0 = var1;
         this.setPriority(10);
         this.setDaemon(true);
      }

      public void run() {
         while(!this.this$0.exitWhenFinished || this.this$0.trackers.size() > 0) {
            try {
               FileCleaningTracker.Tracker var1 = (FileCleaningTracker.Tracker)this.this$0.q.remove();
               this.this$0.trackers.remove(var1);
               if (!var1.delete()) {
                  this.this$0.deleteFailures.add(var1.getPath());
               }

               var1.clear();
            } catch (InterruptedException var2) {
            }
         }

      }
   }
}
