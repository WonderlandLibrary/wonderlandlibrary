package cn.hanabi.utils.jprocess.main.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessesUtils {
   private static final String CRLF = "\r\n";
   private static String customDateFormat;
   private static Locale customLocale;

   private ProcessesUtils() {
   }

   public static String executeCommand(String... command) {
      String commandOutput = null;

      try {
         ProcessBuilder processBuilder = new ProcessBuilder(command);
         processBuilder.redirectErrorStream(true);
         commandOutput = readData(processBuilder.start());
      } catch (IOException var3) {
         commandOutput = "";
         Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error executing command", var3);
         var3.printStackTrace();
      }

      return commandOutput;
   }

   private static String readData(Process process) {
      StringBuilder commandOutput = new StringBuilder();
      BufferedReader processOutput = null;

      try {
         processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

         String line;
         while((line = processOutput.readLine()) != null) {
            if (!line.isEmpty()) {
               commandOutput.append(line).append("\r\n");
            }
         }
      } catch (IOException var12) {
         Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error reading data", var12);
      } finally {
         try {
            if (processOutput != null) {
               processOutput.close();
            }
         } catch (IOException var11) {
            Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error closing reader", var11);
         }

      }

      return commandOutput.toString();
   }

   public static int executeCommandAndGetCode(String... command) {
      Process process;
      try {
         process = Runtime.getRuntime().exec(command);
         process.waitFor();
      } catch (IOException var3) {
         Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, (String)null, var3);
         return -1;
      } catch (InterruptedException var4) {
         Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, (String)null, var4);
         return -1;
      }

      return process.exitValue();
   }

   public static String parseWindowsDateTimeToSimpleTime(String dateTime) {
      String returnedDate = dateTime;
      if (dateTime != null && !dateTime.isEmpty()) {
         String hour = dateTime.substring(8, 10);
         String minutes = dateTime.substring(10, 12);
         String seconds = dateTime.substring(12, 14);
         returnedDate = hour + ":" + minutes + ":" + seconds;
      }

      return returnedDate;
   }

   public static String parseWindowsDateTimeToFullDate(String dateTime) {
      String returnedDate = dateTime;
      if (dateTime != null && !dateTime.isEmpty()) {
         String year = dateTime.substring(0, 4);
         String month = dateTime.substring(4, 6);
         String day = dateTime.substring(6, 8);
         String hour = dateTime.substring(8, 10);
         String minutes = dateTime.substring(10, 12);
         String seconds = dateTime.substring(12, 14);
         returnedDate = month + "/" + day + "/" + year + " " + hour + ":" + minutes + ":" + seconds;
      }

      return returnedDate;
   }

   public static String parseUnixLongTimeToFullDate(String longFormatDate) throws ParseException {
      DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      List formatsToTry = new ArrayList();
      formatsToTry.addAll(Arrays.asList("MMM dd HH:mm:ss yyyy", "dd MMM HH:mm:ss yyyy"));
      List localesToTry = new ArrayList();
      localesToTry.addAll(Arrays.asList(Locale.getDefault(), Locale.getDefault(Category.FORMAT), Locale.ENGLISH));
      if (getCustomDateFormat() != null) {
         formatsToTry.add(0, getCustomDateFormat());
      }

      if (getCustomLocale() != null) {
         localesToTry.add(0, getCustomLocale());
      }

      ParseException lastException = null;
      Iterator var5 = localesToTry.iterator();

      while(var5.hasNext()) {
         Locale locale = (Locale)var5.next();
         Iterator var7 = formatsToTry.iterator();

         while(var7.hasNext()) {
            String format = (String)var7.next();
            DateFormat originalFormat = new SimpleDateFormat(format, locale);

            try {
               return targetFormat.format(originalFormat.parse(longFormatDate));
            } catch (ParseException var11) {
               lastException = var11;
            }
         }
      }

      throw lastException;
   }

   public static String getCustomDateFormat() {
      return customDateFormat;
   }

   public static void setCustomDateFormat(String dateFormat) {
      customDateFormat = dateFormat;
   }

   public static Locale getCustomLocale() {
      return customLocale;
   }

   public static void setCustomLocale(Locale customLocale) {
      ProcessesUtils.customLocale = customLocale;
   }
}
