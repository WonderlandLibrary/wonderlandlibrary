package cn.hanabi.utils.jprocess.main.info;

import cn.hanabi.utils.jprocess.main.util.OSDetector;

public class ProcessesFactory {
   private ProcessesFactory() {
   }

   public static ProcessesService getService() {
      if (OSDetector.isWindows()) {
         return new WindowsProcessesService();
      } else if (OSDetector.isUnix()) {
         return new UnixProcessesService();
      } else {
         throw new UnsupportedOperationException("Your Operating System is not supported by this library.");
      }
   }
}
