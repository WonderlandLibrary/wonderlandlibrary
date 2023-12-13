package cn.hanabi.utils.jprocess.jpowershell;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerShell implements AutoCloseable {
   private static final Logger logger = Logger.getLogger(PowerShell.class.getName());
   private Process p;
   private long pid = -1L;
   private PrintWriter commandWriter;
   private boolean closed = false;
   private ExecutorService threadpool;
   private static final String DEFAULT_WIN_EXECUTABLE = "powershell.exe";
   private static final String DEFAULT_LINUX_EXECUTABLE = "powershell";
   private int waitPause = 5;
   private long maxWait = 10000L;
   private File tempFolder = null;
   private boolean scriptMode = false;
   public static final String END_SCRIPT_STRING = "--END-JPOWERSHELL-SCRIPT--";

   private PowerShell() {
   }

   public PowerShell configuration(Map config) {
      try {
         this.waitPause = Integer.valueOf(config != null && config.get("waitPause") != null ? (String)config.get("waitPause") : PowerShellConfig.getConfig().getProperty("waitPause"));
         this.maxWait = Long.valueOf(config != null && config.get("maxWait") != null ? (String)config.get("maxWait") : PowerShellConfig.getConfig().getProperty("maxWait"));
         this.tempFolder = config != null && config.get("tempFolder") != null ? this.getTempFolder((String)config.get("tempFolder")) : this.getTempFolder(PowerShellConfig.getConfig().getProperty("tempFolder"));
      } catch (NumberFormatException var3) {
         logger.log(Level.SEVERE, "Could not read configuration. Using default values.", var3);
      }

      return this;
   }

   public static PowerShell openSession() throws PowerShellNotAvailableException {
      return openSession((String)null);
   }

   public static PowerShell openSession(String customPowerShellExecutablePath) throws PowerShellNotAvailableException {
      PowerShell powerShell = new PowerShell();
      powerShell.configuration((Map)null);
      String powerShellExecutablePath = customPowerShellExecutablePath == null ? (OSDetector.isWindows() ? "powershell.exe" : "powershell") : customPowerShellExecutablePath;
      PowerShell cp = powerShell.initalize(powerShellExecutablePath);
      powerShell.close();
      return cp;
   }

   private PowerShell initalize(String powerShellExecutablePath) throws PowerShellNotAvailableException {
      String codePage = PowerShellCodepage.getIdentifierByCodePageName(Charset.defaultCharset().name());
      ProcessBuilder pb;
      if (OSDetector.isWindows()) {
         pb = new ProcessBuilder(new String[]{"cmd.exe", "/c", "chcp", codePage, ">", "NUL", "&", powerShellExecutablePath, "-ExecutionPolicy", "Bypass", "-NoExit", "-NoProfile", "-Command", "-"});
      } else {
         pb = new ProcessBuilder(new String[]{powerShellExecutablePath, "-nologo", "-noexit", "-Command", "-"});
      }

      pb.redirectErrorStream(true);

      try {
         this.p = pb.start();
         if (this.p.waitFor(5L, TimeUnit.SECONDS) && !this.p.isAlive()) {
            throw new PowerShellNotAvailableException("Cannot execute PowerShell. Please make sure that it is installed in your system. Errorcode:" + this.p.exitValue());
         }
      } catch (InterruptedException | IOException var5) {
         throw new PowerShellNotAvailableException("Cannot execute PowerShell. Please make sure that it is installed in your system", var5);
      }

      this.commandWriter = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(this.p.getOutputStream())), true);
      this.threadpool = Executors.newFixedThreadPool(2);
      this.pid = this.getPID();
      return this;
   }

   public PowerShellResponse executeCommand(String command) {
      String commandOutput = "";
      boolean isError = false;
      boolean timeout = false;
      this.checkState();
      PowerShellCommandProcessor commandProcessor = new PowerShellCommandProcessor("standard", this.p.getInputStream(), this.waitPause, this.scriptMode);
      Future result = this.threadpool.submit(commandProcessor);
      this.commandWriter.println(command);

      try {
         if (!result.isDone()) {
            try {
               commandOutput = (String)result.get(this.maxWait, TimeUnit.MILLISECONDS);
            } catch (TimeoutException var12) {
               timeout = true;
               isError = true;
               result.cancel(true);
            }
         }
      } catch (ExecutionException | InterruptedException var13) {
         logger.log(Level.SEVERE, "Unexpected error when processing PowerShell command", var13);
         isError = true;
      } finally {
         commandProcessor.close();
      }

      return new PowerShellResponse(isError, commandOutput, timeout);
   }

   public static PowerShellResponse executeSingleCommand(String command) {
      PowerShellResponse response = null;

      try {
         PowerShell session = openSession();
         Throwable var3 = null;

         try {
            response = session.executeCommand(command);
         } catch (Throwable var13) {
            var3 = var13;
            throw var13;
         } finally {
            if (session != null) {
               if (var3 != null) {
                  try {
                     session.close();
                  } catch (Throwable var12) {
                     var3.addSuppressed(var12);
                  }
               } else {
                  session.close();
               }
            }

         }
      } catch (PowerShellNotAvailableException var15) {
         logger.log(Level.SEVERE, "PowerShell not available", var15);
      }

      return response;
   }

   public PowerShell executeCommandAndChain(String command, PowerShellResponseHandler... response) {
      PowerShellResponse powerShellResponse = this.executeCommand(command);
      if (response.length > 0) {
         this.handleResponse(response[0], powerShellResponse);
      }

      return this;
   }

   private void handleResponse(PowerShellResponseHandler response, PowerShellResponse powerShellResponse) {
      try {
         response.handle(powerShellResponse);
      } catch (Exception var4) {
         logger.log(Level.SEVERE, "PowerShell not available", var4);
      }

   }

   public boolean isLastCommandInError() {
      return !Boolean.valueOf(this.executeCommand("$?").getCommandOutput());
   }

   public PowerShellResponse executeScript(String scriptPath) {
      return this.executeScript(scriptPath, "");
   }

   public PowerShellResponse executeScript(String scriptPath, String params) {
      try {
         BufferedReader srcReader = new BufferedReader(new FileReader(new File(scriptPath)));
         Throwable var4 = null;

         PowerShellResponse var5;
         try {
            var5 = this.executeScript(srcReader, params);
         } catch (Throwable var16) {
            var4 = var16;
            throw var16;
         } finally {
            if (srcReader != null) {
               if (var4 != null) {
                  try {
                     srcReader.close();
                  } catch (Throwable var15) {
                     var4.addSuppressed(var15);
                  }
               } else {
                  srcReader.close();
               }
            }

         }

         return var5;
      } catch (FileNotFoundException var18) {
         logger.log(Level.SEVERE, "Unexpected error when processing PowerShell script: file not found", var18);
         return new PowerShellResponse(true, "Wrong script path: " + scriptPath, false);
      } catch (IOException var19) {
         logger.log(Level.SEVERE, "Unexpected error when processing PowerShell script", var19);
         return new PowerShellResponse(true, "IO error reading: " + scriptPath, false);
      }
   }

   public PowerShellResponse executeScript(BufferedReader srcReader) {
      return this.executeScript(srcReader, "");
   }

   public PowerShellResponse executeScript(BufferedReader srcReader, String params) {
      PowerShellResponse response;
      if (srcReader != null) {
         File tmpFile = this.createWriteTempFile(srcReader);
         if (tmpFile != null) {
            this.scriptMode = true;
            response = this.executeCommand(tmpFile.getAbsolutePath() + " " + params);
            this.scriptMode = false;
            tmpFile.delete();
         } else {
            response = new PowerShellResponse(true, "Cannot create temp script file!", false);
         }
      } else {
         logger.log(Level.SEVERE, "Script buffered reader is null!");
         response = new PowerShellResponse(true, "Script buffered reader is null!", false);
      }

      return response;
   }

   private File createWriteTempFile(BufferedReader srcReader) {
      BufferedWriter tmpWriter = null;
      File tmpFile = null;

      try {
         tmpFile = File.createTempFile("psscript_" + (new Date()).getTime(), ".ps1", this.tempFolder);
         String line;
         if (!tmpFile.exists()) {
            line = null;
            return line;
         }

         tmpWriter = new BufferedWriter(new FileWriter(tmpFile));

         while(srcReader != null && (line = srcReader.readLine()) != null) {
            tmpWriter.write(line);
            tmpWriter.newLine();
         }

         tmpWriter.write("Write-Output \"--END-JPOWERSHELL-SCRIPT--\"");
      } catch (IOException var15) {
         logger.log(Level.SEVERE, "Unexpected error while writing temporary PowerShell script", var15);
      } finally {
         try {
            if (tmpWriter != null) {
               tmpWriter.close();
            }
         } catch (IOException var14) {
            logger.log(Level.SEVERE, "Unexpected error when processing temporary PowerShell script", var14);
         }

      }

      return tmpFile;
   }

   public void close() {
      if (!this.closed) {
         try {
            Future closeTask = this.threadpool.submit(() -> {
               this.commandWriter.println("exit");
               this.p.waitFor();
               return "OK";
            });
            if (!this.closeAndWait(closeTask) && this.pid > 0L) {
               Logger.getLogger(PowerShell.class.getName()).log(Level.INFO, "Forcing PowerShell to close. PID: " + this.pid);

               try {
                  Runtime.getRuntime().exec("taskkill.exe /PID " + this.pid + " /F /T");
                  this.closed = true;
               } catch (IOException var16) {
                  Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Unexpected error while killing powershell process", var16);
               }
            }
         } catch (ExecutionException | InterruptedException var17) {
            logger.log(Level.SEVERE, "Unexpected error when when closing PowerShell", var17);
         } finally {
            this.commandWriter.close();

            try {
               if (this.p.isAlive()) {
                  this.p.getInputStream().close();
               }
            } catch (IOException var15) {
               logger.log(Level.SEVERE, "Unexpected error when when closing streams", var15);
            }

            if (this.threadpool != null) {
               try {
                  this.threadpool.shutdownNow();
                  this.threadpool.awaitTermination(5L, TimeUnit.SECONDS);
               } catch (InterruptedException var14) {
                  logger.log(Level.SEVERE, "Unexpected error when when shutting down thread pool", var14);
               }
            }

            this.closed = true;
         }
      }

   }

   private boolean closeAndWait(Future task) throws InterruptedException, ExecutionException {
      boolean closed = true;
      if (!task.isDone()) {
         try {
            task.get(this.maxWait, TimeUnit.MILLISECONDS);
         } catch (TimeoutException var4) {
            logger.log(Level.WARNING, "Powershell process cannot be closed. Session seems to be blocked");
            task.cancel(true);
            closed = false;
         }
      }

      return closed;
   }

   private void checkState() {
      if (this.closed) {
         throw new IllegalStateException("PowerShell is already closed. Please open a new session.");
      }
   }

   private long getPID() {
      String commandOutput = this.executeCommand("$pid").getCommandOutput();
      commandOutput = commandOutput.replaceAll("\\D", "");
      return !commandOutput.isEmpty() ? Long.valueOf(commandOutput) : -1L;
   }

   private File getTempFolder(String tempPath) {
      if (tempPath != null) {
         File folder = new File(tempPath);
         if (folder.exists()) {
            return folder;
         }
      }

      return null;
   }
}
