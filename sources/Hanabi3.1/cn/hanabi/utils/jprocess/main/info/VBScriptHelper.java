package cn.hanabi.utils.jprocess.main.info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

class VBScriptHelper {
   private static final String CRLF = "\r\n";

   private VBScriptHelper() {
   }

   private static String executeScript(String scriptCode) {
      String scriptResponse = "";
      File tmpFile = null;
      FileWriter writer = null;
      BufferedReader processOutput = null;
      BufferedReader errorOutput = null;

      try {
         tmpFile = File.createTempFile("wmi4java" + (new Date()).getTime(), ".vbs");
         writer = new FileWriter(tmpFile);
         writer.write(scriptCode);
         writer.flush();
         writer.close();
         Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "cscript.exe", "/NoLogo", tmpFile.getAbsolutePath()});
         processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

         String line;
         while((line = processOutput.readLine()) != null) {
            if (!line.isEmpty()) {
               scriptResponse = scriptResponse + line + "\r\n";
            }
         }

         if (scriptResponse.isEmpty()) {
            errorOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String errorResponse = "";

            while((line = errorOutput.readLine()) != null) {
               if (!line.isEmpty()) {
                  errorResponse = errorResponse + line + "\r\n";
               }
            }

            if (!errorResponse.isEmpty()) {
               Logger.getLogger(VBScriptHelper.class.getName()).log(Level.SEVERE, "WMI operation finished in error: ");
            }

            errorOutput.close();
         }
      } catch (Exception var17) {
         Logger.getLogger(VBScriptHelper.class.getName()).log(Level.SEVERE, (String)null, var17);
      } finally {
         try {
            if (processOutput != null) {
               processOutput.close();
            }

            if (errorOutput != null) {
               errorOutput.close();
            }

            if (writer != null) {
               writer.close();
            }

            if (tmpFile != null) {
               tmpFile.delete();
            }
         } catch (IOException var16) {
            Logger.getLogger(VBScriptHelper.class.getName()).log(Level.SEVERE, (String)null, var16);
         }

      }

      return scriptResponse.trim();
   }

   public static String getProcessesOwner() {
      try {
         StringBuilder scriptCode = new StringBuilder(200);
         scriptCode.append("Set objWMIService=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\").append(".").append("/").append("root/cimv2").append("\")").append("\r\n");
         scriptCode.append("Set colProcessList = objWMIService.ExecQuery(\"Select * from Win32_Process\")").append("\r\n");
         scriptCode.append("For Each objProcess in colProcessList").append("\r\n");
         scriptCode.append("colProperties = objProcess.GetOwner(strNameOfUser,strUserDomain)").append("\r\n");
         scriptCode.append("Wscript.Echo objProcess.ProcessId & \":\" & strNameOfUser").append("\r\n");
         scriptCode.append("Next").append("\r\n");
         return executeScript(scriptCode.toString());
      } catch (Exception var1) {
         Logger.getLogger(VBScriptHelper.class.getName()).log(Level.SEVERE, (String)null, var1);
         return null;
      }
   }

   public static String changePriority(int pid, int newPriority) {
      try {
         StringBuilder scriptCode = new StringBuilder(200);
         scriptCode.append("Set objWMIService=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\").append(".").append("/").append("root/cimv2").append("\")").append("\r\n");
         scriptCode.append("Set colProcesses = objWMIService.ExecQuery(\"Select * from Win32_Process Where ProcessId = ").append(pid).append("\")").append("\r\n");
         scriptCode.append("For Each objProcess in colProcesses").append("\r\n");
         scriptCode.append("objProcess.SetPriority(").append(newPriority).append(")").append("\r\n");
         scriptCode.append("Next").append("\r\n");
         return executeScript(scriptCode.toString());
      } catch (Exception var3) {
         Logger.getLogger(VBScriptHelper.class.getName()).log(Level.SEVERE, (String)null, var3);
         return null;
      }
   }
}
