package org.apache.commons.io;

import java.io.File;

/** @deprecated */
@Deprecated
public class FileCleaner {
   static final FileCleaningTracker theInstance = new FileCleaningTracker();

   /** @deprecated */
   @Deprecated
   public static void track(File var0, Object var1) {
      theInstance.track(var0, var1);
   }

   /** @deprecated */
   @Deprecated
   public static void track(File var0, Object var1, FileDeleteStrategy var2) {
      theInstance.track(var0, var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public static void track(String var0, Object var1) {
      theInstance.track(var0, var1);
   }

   /** @deprecated */
   @Deprecated
   public static void track(String var0, Object var1, FileDeleteStrategy var2) {
      theInstance.track(var0, var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public static int getTrackCount() {
      return theInstance.getTrackCount();
   }

   /** @deprecated */
   @Deprecated
   public static synchronized void exitWhenFinished() {
      theInstance.exitWhenFinished();
   }

   public static FileCleaningTracker getInstance() {
      return theInstance;
   }
}
