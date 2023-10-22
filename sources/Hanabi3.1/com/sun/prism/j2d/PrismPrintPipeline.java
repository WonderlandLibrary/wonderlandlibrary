package com.sun.prism.j2d;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.PrintPipeline;
import com.sun.prism.j2d.print.J2DPrinter;
import com.sun.prism.j2d.print.J2DPrinterJob;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;

public final class PrismPrintPipeline extends PrintPipeline {
   private static Printer defaultPrinter = null;
   private static final NameComparator nameComparator = new NameComparator();
   private static ObservableSet printerSet = null;

   public static PrintPipeline getInstance() {
      return new PrismPrintPipeline();
   }

   public boolean printNode(NGNode var1, int var2, int var3, Graphics var4) {
      PrismPrintGraphics var5 = new PrismPrintGraphics((Graphics2D)var4, var2, var3);
      var1.render(var5);
      return true;
   }

   public PrinterJobImpl createPrinterJob(PrinterJob var1) {
      return new J2DPrinterJob(var1);
   }

   public synchronized Printer getDefaultPrinter() {
      if (defaultPrinter == null) {
         PrintService var1 = PrintServiceLookup.lookupDefaultPrintService();
         if (var1 == null) {
            defaultPrinter = null;
         } else if (printerSet == null) {
            J2DPrinter var2 = new J2DPrinter(var1);
            defaultPrinter = PrintHelper.createPrinter(var2);
         } else {
            Iterator var6 = printerSet.iterator();

            while(var6.hasNext()) {
               Printer var3 = (Printer)var6.next();
               PrinterImpl var4 = PrintHelper.getPrinterImpl(var3);
               J2DPrinter var5 = (J2DPrinter)var4;
               if (var5.getService().equals(var1)) {
                  defaultPrinter = var3;
                  break;
               }
            }
         }
      }

      return defaultPrinter;
   }

   public synchronized ObservableSet getAllPrinters() {
      if (printerSet == null) {
         TreeSet var1 = new TreeSet(nameComparator);
         Printer var2 = this.getDefaultPrinter();
         PrintService var3 = null;
         if (var2 != null) {
            J2DPrinter var4 = (J2DPrinter)PrintHelper.getPrinterImpl(var2);
            var3 = var4.getService();
         }

         PrintService[] var8 = PrintServiceLookup.lookupPrintServices((DocFlavor)null, (AttributeSet)null);

         for(int var5 = 0; var5 < var8.length; ++var5) {
            if (var3 != null && var3.equals(var8[var5])) {
               var1.add(var2);
            } else {
               J2DPrinter var6 = new J2DPrinter(var8[var5]);
               Printer var7 = PrintHelper.createPrinter(var6);
               var6.setPrinter(var7);
               var1.add(var7);
            }
         }

         printerSet = FXCollections.unmodifiableObservableSet(FXCollections.observableSet((Set)var1));
      }

      return printerSet;
   }

   static class NameComparator implements Comparator {
      public int compare(Printer var1, Printer var2) {
         return var1.getName().compareTo(var2.getName());
      }
   }
}
