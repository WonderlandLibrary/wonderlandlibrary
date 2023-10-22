package cn.hanabi.utils.jprocess.jpowershell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class PowerShellCommandProcessor implements Callable {
   private static final String CRLF = "\r\n";
   private final BufferedReader reader;
   private boolean closed = false;
   private final boolean scriptMode;
   private final int waitPause;

   public PowerShellCommandProcessor(String name, InputStream inputStream, int waitPause, boolean scriptMode) {
      this.reader = new BufferedReader(new InputStreamReader(inputStream));
      this.waitPause = waitPause;
      this.scriptMode = scriptMode;
   }

   public String call() throws InterruptedException {
      StringBuilder powerShellOutput = new StringBuilder();

      try {
         if (this.startReading()) {
            this.readData(powerShellOutput);
         }
      } catch (IOException var3) {
         Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Unexpected error reading PowerShell output", var3);
         return var3.getMessage();
      }

      return powerShellOutput.toString().replaceAll("\\s+$", "");
   }

   private void readData(StringBuilder powerShellOutput) throws IOException {
      while(true) {
         String line;
         if (null != (line = this.reader.readLine()) && (!this.scriptMode || !line.equals("--END-JPOWERSHELL-SCRIPT--"))) {
            powerShellOutput.append(line).append("\r\n");
            if (this.scriptMode) {
               continue;
            }

            try {
               if (!this.closed && this.canContinueReading()) {
                  continue;
               }
            } catch (InterruptedException var4) {
               Logger.getLogger(PowerShellCommandProcessor.class.getName()).log(Level.SEVERE, "Error executing command and reading result", var4);
               continue;
            }
         }

         return;
      }
   }

   private boolean startReading() throws IOException, InterruptedException {
      while(true) {
         if (!this.reader.ready()) {
            Thread.sleep((long)this.waitPause);
            if (!this.closed) {
               continue;
            }

            return false;
         }

         return true;
      }
   }

   private boolean canContinueReading() throws IOException, InterruptedException {
      if (!this.reader.ready()) {
         Thread.sleep((long)this.waitPause);
      }

      if (!this.reader.ready()) {
         Thread.sleep(50L);
      }

      return this.reader.ready();
   }

   public void close() {
      this.closed = true;
   }
}
