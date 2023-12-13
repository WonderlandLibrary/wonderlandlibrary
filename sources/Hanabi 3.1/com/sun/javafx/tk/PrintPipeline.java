package com.sun.javafx.tk;

import com.sun.javafx.print.PrinterJobImpl;
import java.lang.reflect.Method;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;

public abstract class PrintPipeline {
   private static PrintPipeline ppl = null;

   public static PrintPipeline getPrintPipeline() {
      if (ppl != null) {
         return ppl;
      } else {
         try {
            String var0 = "com.sun.prism.j2d.PrismPrintPipeline";
            Class var1 = Class.forName(var0);
            Method var2 = var1.getMethod("getInstance", (Class[])null);
            ppl = (PrintPipeline)var2.invoke((Object)null, (Object[])null);
            return ppl;
         } catch (Throwable var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public abstract Printer getDefaultPrinter();

   public abstract ObservableSet getAllPrinters();

   public abstract PrinterJobImpl createPrinterJob(PrinterJob var1);
}
