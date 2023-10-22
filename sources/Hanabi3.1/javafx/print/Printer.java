package javafx.print;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import com.sun.javafx.tk.PrintPipeline;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableSet;
import javafx.geometry.Rectangle2D;

public final class Printer {
   private static ReadOnlyObjectWrapper defaultPrinter;
   private PrinterImpl impl;
   private PrinterAttributes attributes;
   private PageLayout defPageLayout;

   public static ObservableSet getAllPrinters() {
      SecurityManager var0 = System.getSecurityManager();
      if (var0 != null) {
         var0.checkPrintJobAccess();
      }

      return PrintPipeline.getPrintPipeline().getAllPrinters();
   }

   private static ReadOnlyObjectWrapper defaultPrinterImpl() {
      SecurityManager var0 = System.getSecurityManager();
      if (var0 != null) {
         var0.checkPrintJobAccess();
      }

      if (defaultPrinter == null) {
         Printer var1 = PrintPipeline.getPrintPipeline().getDefaultPrinter();
         defaultPrinter = new ReadOnlyObjectWrapper((Object)null, "defaultPrinter", var1);
      }

      return defaultPrinter;
   }

   public static ReadOnlyObjectProperty defaultPrinterProperty() {
      return defaultPrinterImpl().getReadOnlyProperty();
   }

   public static Printer getDefaultPrinter() {
      return (Printer)defaultPrinterProperty().get();
   }

   Printer(PrinterImpl var1) {
      this.impl = var1;
      var1.setPrinter(this);
   }

   PrinterImpl getPrinterImpl() {
      return this.impl;
   }

   public String getName() {
      return this.impl.getName();
   }

   public PrinterAttributes getPrinterAttributes() {
      if (this.attributes == null) {
         this.attributes = new PrinterAttributes(this.impl);
      }

      return this.attributes;
   }

   JobSettings getDefaultJobSettings() {
      return this.impl.getDefaultJobSettings();
   }

   public PageLayout getDefaultPageLayout() {
      if (this.defPageLayout == null) {
         PrinterAttributes var1 = this.getPrinterAttributes();
         this.defPageLayout = this.createPageLayout(var1.getDefaultPaper(), var1.getDefaultPageOrientation(), Printer.MarginType.DEFAULT);
      }

      return this.defPageLayout;
   }

   public PageLayout createPageLayout(Paper var1, PageOrientation var2, MarginType var3) {
      if (var1 != null && var2 != null && var3 != null) {
         Rectangle2D var4 = this.impl.printableArea(var1);
         double var5 = var1.getWidth() / 72.0;
         double var7 = var1.getHeight() / 72.0;
         double var9 = var4.getMinX();
         double var11 = var4.getMinY();
         double var13 = var5 - var4.getMaxX();
         double var15 = var7 - var4.getMaxY();
         if (Math.abs(var9) < 0.01) {
            var9 = 0.0;
         }

         if (Math.abs(var13) < 0.01) {
            var13 = 0.0;
         }

         if (Math.abs(var11) < 0.01) {
            var11 = 0.0;
         }

         if (Math.abs(var15) < 0.01) {
            var15 = 0.0;
         }

         double var17;
         double var19;
         double var21;
         switch (var3) {
            case DEFAULT:
               var9 = var9 <= 0.75 ? 0.75 : var9;
               var13 = var13 <= 0.75 ? 0.75 : var13;
               var11 = var11 <= 0.75 ? 0.75 : var11;
               var15 = var15 <= 0.75 ? 0.75 : var15;
               break;
            case EQUAL:
               var17 = Math.max(var9, var13);
               var19 = Math.max(var11, var15);
               var21 = Math.max(var17, var19);
               var15 = var21;
               var11 = var21;
               var13 = var21;
               var9 = var21;
               break;
            case EQUAL_OPPOSITES:
               var17 = Math.max(var9, var13);
               var19 = Math.max(var11, var15);
               var13 = var17;
               var9 = var17;
               var15 = var19;
               var11 = var19;
            case HARDWARE_MINIMUM:
         }

         double var23;
         switch (var2) {
            case LANDSCAPE:
               var17 = var15;
               var19 = var11;
               var21 = var9;
               var23 = var13;
               break;
            case REVERSE_LANDSCAPE:
               var17 = var11;
               var19 = var15;
               var21 = var13;
               var23 = var9;
               break;
            case REVERSE_PORTRAIT:
               var17 = var13;
               var19 = var9;
               var21 = var15;
               var23 = var11;
               break;
            default:
               var17 = var9;
               var19 = var13;
               var21 = var11;
               var23 = var15;
         }

         var17 *= 72.0;
         var19 *= 72.0;
         var21 *= 72.0;
         var23 *= 72.0;
         return new PageLayout(var1, var2, var17, var19, var21, var23);
      } else {
         throw new NullPointerException("Parameters cannot be null");
      }
   }

   public PageLayout createPageLayout(Paper var1, PageOrientation var2, double var3, double var5, double var7, double var9) {
      if (var1 != null && var2 != null) {
         if (!(var3 < 0.0) && !(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0)) {
            Rectangle2D var11 = this.impl.printableArea(var1);
            double var12 = var1.getWidth() / 72.0;
            double var14 = var1.getHeight() / 72.0;
            double var16 = var11.getMinX();
            double var18 = var11.getMinY();
            double var20 = var12 - var11.getMaxX();
            double var22 = var14 - var11.getMaxY();
            var3 /= 72.0;
            var5 /= 72.0;
            var7 /= 72.0;
            var9 /= 72.0;
            boolean var24 = false;
            if (var2 != PageOrientation.PORTRAIT && var2 != PageOrientation.REVERSE_PORTRAIT) {
               if (var3 + var5 > var14 || var7 + var9 > var12) {
                  var24 = true;
               }
            } else if (var3 + var5 > var12 || var7 + var9 > var14) {
               var24 = true;
            }

            if (var24) {
               return this.createPageLayout(var1, var2, Printer.MarginType.DEFAULT);
            } else {
               double var25;
               double var27;
               double var29;
               double var31;
               switch (var2) {
                  case LANDSCAPE:
                     var25 = var22;
                     var27 = var18;
                     var29 = var16;
                     var31 = var20;
                     break;
                  case REVERSE_LANDSCAPE:
                     var25 = var18;
                     var27 = var22;
                     var29 = var20;
                     var31 = var16;
                     break;
                  case REVERSE_PORTRAIT:
                     var25 = var20;
                     var27 = var16;
                     var29 = var22;
                     var31 = var18;
                     break;
                  default:
                     var25 = var16;
                     var27 = var20;
                     var29 = var18;
                     var31 = var22;
               }

               var25 = var3 >= var25 ? var3 : var25;
               var27 = var5 >= var27 ? var5 : var27;
               var29 = var7 >= var29 ? var7 : var29;
               var31 = var9 >= var31 ? var9 : var31;
               var25 *= 72.0;
               var27 *= 72.0;
               var29 *= 72.0;
               var31 *= 72.0;
               return new PageLayout(var1, var2, var25, var27, var29, var31);
            }
         } else {
            throw new IllegalArgumentException("Margins must be >= 0");
         }
      } else {
         throw new NullPointerException("Parameters cannot be null");
      }
   }

   public String toString() {
      return "Printer " + this.getName();
   }

   static {
      PrintHelper.setPrintAccessor(new PrintHelper.PrintAccessor() {
         public PrintResolution createPrintResolution(int var1, int var2) {
            return new PrintResolution(var1, var2);
         }

         public Paper createPaper(String var1, double var2, double var4, Units var6) {
            return new Paper(var1, var2, var4, var6);
         }

         public PaperSource createPaperSource(String var1) {
            return new PaperSource(var1);
         }

         public JobSettings createJobSettings(Printer var1) {
            return new JobSettings(var1);
         }

         public Printer createPrinter(PrinterImpl var1) {
            return new Printer(var1);
         }

         public PrinterImpl getPrinterImpl(Printer var1) {
            return var1.getPrinterImpl();
         }
      });
   }

   public static enum MarginType {
      DEFAULT,
      HARDWARE_MINIMUM,
      EQUAL,
      EQUAL_OPPOSITES;
   }
}
