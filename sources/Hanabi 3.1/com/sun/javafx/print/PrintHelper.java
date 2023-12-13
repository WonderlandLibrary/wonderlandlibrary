package com.sun.javafx.print;

import javafx.print.JobSettings;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintResolution;
import javafx.print.Printer;

public class PrintHelper {
   private static PrintAccessor printAccessor;

   private PrintHelper() {
   }

   public static PrintResolution createPrintResolution(int var0, int var1) {
      return printAccessor.createPrintResolution(var0, var1);
   }

   public static Paper createPaper(String var0, double var1, double var3, Units var5) {
      return printAccessor.createPaper(var0, var1, var3, var5);
   }

   public static PaperSource createPaperSource(String var0) {
      return printAccessor.createPaperSource(var0);
   }

   public static JobSettings createJobSettings(Printer var0) {
      return printAccessor.createJobSettings(var0);
   }

   public static Printer createPrinter(PrinterImpl var0) {
      return printAccessor.createPrinter(var0);
   }

   public static PrinterImpl getPrinterImpl(Printer var0) {
      return printAccessor.getPrinterImpl(var0);
   }

   public static void setPrintAccessor(PrintAccessor var0) {
      if (printAccessor != null) {
         throw new IllegalStateException();
      } else {
         printAccessor = var0;
      }
   }

   private static void forceInit(Class var0) {
      try {
         Class.forName(var0.getName(), true, var0.getClassLoader());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      forceInit(Printer.class);
   }

   public interface PrintAccessor {
      PrintResolution createPrintResolution(int var1, int var2);

      Paper createPaper(String var1, double var2, double var4, Units var6);

      PaperSource createPaperSource(String var1);

      JobSettings createJobSettings(Printer var1);

      Printer createPrinter(PrinterImpl var1);

      PrinterImpl getPrinterImpl(Printer var1);
   }
}
