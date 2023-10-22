package com.sun.prism.j2d.print;

import com.sun.glass.ui.Application;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.PrismPrintGraphics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.ObservableSet;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

public class J2DPrinterJob implements PrinterJobImpl {
   PrinterJob fxPrinterJob;
   java.awt.print.PrinterJob pJob2D;
   Printer fxPrinter;
   J2DPrinter j2dPrinter;
   private JobSettings settings;
   private PrintRequestAttributeSet printReqAttrSet;
   private volatile Object elo = null;
   private static Class onTopClass = null;
   private boolean jobRunning = false;
   private boolean jobError = false;
   private boolean jobDone = false;
   private J2DPageable j2dPageable = null;
   private Object monitor = new Object();

   PrintRequestAttribute getAlwaysOnTop(long var1) {
      return (PrintRequestAttribute)AccessController.doPrivileged(() -> {
         PrintRequestAttribute var2 = null;

         try {
            if (onTopClass == null) {
               onTopClass = Class.forName("sun.print.DialogOnTop");
            }

            Constructor var3 = onTopClass.getConstructor(Long.TYPE);
            var2 = (PrintRequestAttribute)var3.newInstance(var1);
         } catch (Throwable var4) {
         }

         return var2;
      });
   }

   public J2DPrinterJob(PrinterJob var1) {
      this.fxPrinterJob = var1;
      this.fxPrinter = this.fxPrinterJob.getPrinter();
      this.j2dPrinter = this.getJ2DPrinter(this.fxPrinter);
      this.settings = this.fxPrinterJob.getJobSettings();
      this.pJob2D = java.awt.print.PrinterJob.getPrinterJob();

      try {
         this.pJob2D.setPrintService(this.j2dPrinter.getService());
      } catch (PrinterException var3) {
      }

      this.printReqAttrSet = new HashPrintRequestAttributeSet();
      this.printReqAttrSet.add(DialogTypeSelection.NATIVE);
      this.j2dPageable = new J2DPageable();
      this.pJob2D.setPageable(this.j2dPageable);
   }

   private void setEnabledState(Window var1, boolean var2) {
      if (var1 != null) {
         TKStage var3 = var1.impl_getPeer();
         if (var3 != null) {
            Application.invokeAndWait(() -> {
               var3.setEnabled(var2);
            });
         }
      }
   }

   public boolean showPrintDialog(Window var1) {
      if (!this.jobRunning && !this.jobDone) {
         if (GraphicsEnvironment.isHeadless()) {
            return true;
         } else {
            if (onTopClass != null) {
               this.printReqAttrSet.remove(onTopClass);
            }

            if (var1 != null) {
               long var2 = var1.impl_getPeer().getRawHandle();
               PrintRequestAttribute var4 = this.getAlwaysOnTop(var2);
               if (var4 != null) {
                  this.printReqAttrSet.add(var4);
               }
            }

            boolean var8 = false;
            this.syncSettingsToAttributes();

            try {
               this.setEnabledState(var1, false);
               if (!Toolkit.getToolkit().isFxUserThread()) {
                  var8 = this.pJob2D.printDialog(this.printReqAttrSet);
               } else {
                  if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                     throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                  }

                  var8 = this.showPrintDialogWithNestedLoop(var1);
               }

               if (var8) {
                  this.updateSettingsFromDialog();
               }
            } finally {
               this.setEnabledState(var1, true);
            }

            return var8;
         }
      } else {
         return false;
      }
   }

   private boolean showPrintDialogWithNestedLoop(Window var1) {
      PrintDialogRunnable var2 = new PrintDialogRunnable();
      Thread var3 = new Thread(var2, "FX Print Dialog Thread");
      var3.start();
      Object var4 = Toolkit.getToolkit().enterNestedEventLoop(var2);
      boolean var5 = false;

      try {
         var5 = (Boolean)var4;
      } catch (Exception var7) {
      }

      return var5;
   }

   public boolean showPageDialog(Window var1) {
      if (!this.jobRunning && !this.jobDone) {
         if (GraphicsEnvironment.isHeadless()) {
            return true;
         } else {
            if (onTopClass != null) {
               this.printReqAttrSet.remove(onTopClass);
            }

            if (var1 != null) {
               long var2 = var1.impl_getPeer().getRawHandle();
               PrintRequestAttribute var4 = this.getAlwaysOnTop(var2);
               if (var4 != null) {
                  this.printReqAttrSet.add(var4);
               }
            }

            boolean var8 = false;
            this.syncSettingsToAttributes();

            try {
               this.setEnabledState(var1, false);
               if (!Toolkit.getToolkit().isFxUserThread()) {
                  PageFormat var3 = this.pJob2D.pageDialog(this.printReqAttrSet);
                  var8 = var3 != null;
               } else {
                  if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                     throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                  }

                  var8 = this.showPageDialogFromNestedLoop(var1);
               }
            } finally {
               this.setEnabledState(var1, true);
            }

            if (var8) {
               this.updateSettingsFromDialog();
            }

            return var8;
         }
      } else {
         return false;
      }
   }

   private boolean showPageDialogFromNestedLoop(Window var1) {
      PageDialogRunnable var2 = new PageDialogRunnable();
      Thread var3 = new Thread(var2, "FX Page Setup Dialog Thread");
      var3.start();
      Object var4 = Toolkit.getToolkit().enterNestedEventLoop(var2);
      boolean var5 = false;

      try {
         var5 = (Boolean)var4;
      } catch (Exception var7) {
      }

      return var5;
   }

   private void updateJobName() {
      String var1 = this.pJob2D.getJobName();
      if (!var1.equals(this.settings.getJobName())) {
         this.settings.setJobName(var1);
      }

   }

   private void updateCopies() {
      int var1 = this.pJob2D.getCopies();
      if (this.settings.getCopies() != var1) {
         this.settings.setCopies(var1);
      }

   }

   private void updatePageRanges() {
      PageRanges var1 = (PageRanges)this.printReqAttrSet.get(PageRanges.class);
      if (var1 != null) {
         int[][] var2 = var1.getMembers();
         if (var2.length == 1) {
            PageRange var9 = new PageRange(var2[0][0], var2[0][1]);
            this.settings.setPageRanges(var9);
         } else if (var2.length > 0) {
            try {
               ArrayList var3 = new ArrayList();
               int var4 = 0;
               int var5 = 0;

               while(true) {
                  if (var5 >= var2.length) {
                     this.settings.setPageRanges((PageRange[])var3.toArray(new PageRange[0]));
                     break;
                  }

                  int var6 = var2[var5][0];
                  int var7 = var2[var5][1];
                  if (var6 <= var4 || var7 < var6) {
                     return;
                  }

                  var4 = var7;
                  var3.add(new PageRange(var6, var7));
                  ++var5;
               }
            } catch (Exception var8) {
            }
         }
      }

   }

   private void updateSides() {
      Sides var1 = (Sides)this.printReqAttrSet.get(Sides.class);
      if (var1 == null) {
         var1 = (Sides)this.j2dPrinter.getService().getDefaultAttributeValue(Sides.class);
      }

      if (var1 == Sides.ONE_SIDED) {
         this.settings.setPrintSides(PrintSides.ONE_SIDED);
      } else if (var1 == Sides.DUPLEX) {
         this.settings.setPrintSides(PrintSides.DUPLEX);
      } else if (var1 == Sides.TUMBLE) {
         this.settings.setPrintSides(PrintSides.TUMBLE);
      }

   }

   private void updateCollation() {
      SheetCollate var1 = (SheetCollate)this.printReqAttrSet.get(SheetCollate.class);
      if (var1 == null) {
         var1 = this.j2dPrinter.getDefaultSheetCollate();
      }

      if (var1 == SheetCollate.UNCOLLATED) {
         this.settings.setCollation(Collation.UNCOLLATED);
      } else {
         this.settings.setCollation(Collation.COLLATED);
      }

   }

   private void updateColor() {
      Chromaticity var1 = (Chromaticity)this.printReqAttrSet.get(Chromaticity.class);
      if (var1 == null) {
         var1 = this.j2dPrinter.getDefaultChromaticity();
      }

      if (var1 == Chromaticity.COLOR) {
         this.settings.setPrintColor(PrintColor.COLOR);
      } else {
         this.settings.setPrintColor(PrintColor.MONOCHROME);
      }

   }

   private void updatePrintQuality() {
      PrintQuality var1 = (PrintQuality)this.printReqAttrSet.get(PrintQuality.class);
      if (var1 == null) {
         var1 = this.j2dPrinter.getDefaultPrintQuality();
      }

      if (var1 == PrintQuality.DRAFT) {
         this.settings.setPrintQuality(javafx.print.PrintQuality.DRAFT);
      } else if (var1 == PrintQuality.HIGH) {
         this.settings.setPrintQuality(javafx.print.PrintQuality.HIGH);
      } else {
         this.settings.setPrintQuality(javafx.print.PrintQuality.NORMAL);
      }

   }

   private void updatePrintResolution() {
      PrinterResolution var1 = (PrinterResolution)this.printReqAttrSet.get(PrinterResolution.class);
      if (var1 == null) {
         var1 = this.j2dPrinter.getDefaultPrinterResolution();
      }

      int var2 = var1.getCrossFeedResolution(100);
      int var3 = var1.getFeedResolution(100);
      this.settings.setPrintResolution(PrintHelper.createPrintResolution(var2, var3));
   }

   private void updatePageLayout() {
      Media var1 = (Media)this.printReqAttrSet.get(Media.class);
      Paper var2 = this.j2dPrinter.getPaperForMedia(var1);
      OrientationRequested var3 = (OrientationRequested)this.printReqAttrSet.get(OrientationRequested.class);
      PageOrientation var4 = J2DPrinter.reverseMapOrientation(var3);
      MediaPrintableArea var5 = (MediaPrintableArea)this.printReqAttrSet.get(MediaPrintableArea.class);
      PageLayout var6;
      if (var5 == null) {
         var6 = this.fxPrinter.createPageLayout(var2, var4, Printer.MarginType.DEFAULT);
      } else {
         double var7 = var2.getWidth();
         double var9 = var2.getHeight();
         short var11 = 25400;
         double var12 = (double)(var5.getX(var11) * 72.0F);
         double var14 = (double)(var5.getY(var11) * 72.0F);
         double var16 = (double)(var5.getWidth(var11) * 72.0F);
         double var18 = (double)(var5.getHeight(var11) * 72.0F);
         double var20 = 0.0;
         double var22 = 0.0;
         double var24 = 0.0;
         double var26 = 0.0;
         switch (var4) {
            case PORTRAIT:
               var20 = var12;
               var22 = var7 - var12 - var16;
               var24 = var14;
               var26 = var9 - var14 - var18;
               break;
            case REVERSE_PORTRAIT:
               var20 = var7 - var12 - var16;
               var22 = var12;
               var24 = var9 - var14 - var18;
               var26 = var14;
               break;
            case LANDSCAPE:
               var20 = var14;
               var22 = var9 - var14 - var18;
               var24 = var7 - var12 - var16;
               var26 = var12;
               break;
            case REVERSE_LANDSCAPE:
               var20 = var9 - var14 - var18;
               var24 = var12;
               var22 = var14;
               var26 = var7 - var12 - var16;
         }

         if (Math.abs(var20) < 0.01) {
            var20 = 0.0;
         }

         if (Math.abs(var22) < 0.01) {
            var22 = 0.0;
         }

         if (Math.abs(var24) < 0.01) {
            var24 = 0.0;
         }

         if (Math.abs(var26) < 0.01) {
            var26 = 0.0;
         }

         var6 = this.fxPrinter.createPageLayout(var2, var4, var20, var22, var24, var26);
      }

      this.settings.setPageLayout(var6);
   }

   private void updatePaperSource() {
      Media var1 = (Media)this.printReqAttrSet.get(Media.class);
      if (var1 instanceof MediaTray) {
         PaperSource var2 = this.j2dPrinter.getPaperSource((MediaTray)var1);
         if (var2 != null) {
            this.settings.setPaperSource(var2);
         }
      }

   }

   private Printer getFXPrinterForService(PrintService var1) {
      ObservableSet var2 = Printer.getAllPrinters();
      Iterator var3 = var2.iterator();

      Printer var4;
      PrintService var6;
      do {
         if (!var3.hasNext()) {
            return this.fxPrinter;
         }

         var4 = (Printer)var3.next();
         J2DPrinter var5 = (J2DPrinter)PrintHelper.getPrinterImpl(var4);
         var6 = var5.getService();
      } while(!var6.equals(var1));

      return var4;
   }

   public void setPrinterImpl(PrinterImpl var1) {
      this.j2dPrinter = (J2DPrinter)var1;
      this.fxPrinter = this.j2dPrinter.getPrinter();

      try {
         this.pJob2D.setPrintService(this.j2dPrinter.getService());
      } catch (PrinterException var3) {
      }

   }

   public PrinterImpl getPrinterImpl() {
      return this.j2dPrinter;
   }

   private J2DPrinter getJ2DPrinter(Printer var1) {
      return (J2DPrinter)PrintHelper.getPrinterImpl(var1);
   }

   public Printer getPrinter() {
      return this.fxPrinter;
   }

   public void setPrinter(Printer var1) {
      this.fxPrinter = var1;
      this.j2dPrinter = this.getJ2DPrinter(var1);

      try {
         this.pJob2D.setPrintService(this.j2dPrinter.getService());
      } catch (PrinterException var3) {
      }

   }

   private void updatePrinter() {
      PrintService var1 = this.j2dPrinter.getService();
      PrintService var2 = this.pJob2D.getPrintService();
      if (!var1.equals(var2)) {
         Printer var3 = this.getFXPrinterForService(var2);
         this.fxPrinterJob.setPrinter(var3);
      }
   }

   private void updateSettingsFromDialog() {
      this.updatePrinter();
      this.updateJobName();
      this.updateCopies();
      this.updatePageRanges();
      this.updateSides();
      this.updateCollation();
      this.updatePageLayout();
      this.updatePaperSource();
      this.updateColor();
      this.updatePrintQuality();
      this.updatePrintResolution();
   }

   private void syncSettingsToAttributes() {
      this.syncJobName();
      this.syncCopies();
      this.syncPageRanges();
      this.syncSides();
      this.syncCollation();
      this.syncPageLayout();
      this.syncPaperSource();
      this.syncColor();
      this.syncPrintQuality();
      this.syncPrintResolution();
   }

   private void syncJobName() {
      this.pJob2D.setJobName(this.settings.getJobName());
   }

   private void syncCopies() {
      this.pJob2D.setCopies(this.settings.getCopies());
      this.printReqAttrSet.add(new Copies(this.settings.getCopies()));
   }

   private void syncPageRanges() {
      this.printReqAttrSet.remove(PageRanges.class);
      PageRange[] var1 = this.settings.getPageRanges();
      if (var1 != null && var1.length > 0) {
         int var2 = var1.length;
         int[][] var3 = new int[var2][2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4][0] = var1[var4].getStartPage();
            var3[var4][1] = var1[var4].getEndPage();
         }

         this.printReqAttrSet.add(new PageRanges(var3));
      }

   }

   private void syncSides() {
      Sides var1 = Sides.ONE_SIDED;
      PrintSides var2 = this.settings.getPrintSides();
      if (var2 == PrintSides.DUPLEX) {
         var1 = Sides.DUPLEX;
      } else if (var2 == PrintSides.TUMBLE) {
         var1 = Sides.TUMBLE;
      }

      this.printReqAttrSet.add(var1);
   }

   private void syncCollation() {
      if (this.settings.getCollation() == Collation.UNCOLLATED) {
         this.printReqAttrSet.add(SheetCollate.UNCOLLATED);
      } else {
         this.printReqAttrSet.add(SheetCollate.COLLATED);
      }

   }

   private void syncPageLayout() {
      PageLayout var1 = this.settings.getPageLayout();
      PageOrientation var2 = var1.getPageOrientation();
      this.printReqAttrSet.add(J2DPrinter.mapOrientation(var2));
      double var3 = var1.getPaper().getWidth();
      double var5 = var1.getPaper().getHeight();
      float var7 = (float)(var3 / 72.0);
      float var8 = (float)(var5 / 72.0);
      MediaSizeName var9 = MediaSize.findMedia(var7, var8, 25400);
      if (var9 == null) {
         var9 = MediaSizeName.NA_LETTER;
      }

      this.printReqAttrSet.add(var9);
      double var10 = 0.0;
      double var12 = 0.0;
      double var14 = var3;
      double var16 = var5;
      switch (var2) {
         case PORTRAIT:
            var10 = var1.getLeftMargin();
            var12 = var1.getTopMargin();
            var14 = var3 - var10 - var1.getRightMargin();
            var16 = var5 - var12 - var1.getBottomMargin();
            break;
         case REVERSE_PORTRAIT:
            var10 = var1.getRightMargin();
            var12 = var1.getBottomMargin();
            var14 = var3 - var10 - var1.getLeftMargin();
            var16 = var5 - var12 - var1.getTopMargin();
            break;
         case LANDSCAPE:
            var10 = var1.getBottomMargin();
            var12 = var1.getLeftMargin();
            var14 = var3 - var10 - var1.getTopMargin();
            var16 = var5 - var12 - var1.getRightMargin();
            break;
         case REVERSE_LANDSCAPE:
            var10 = var1.getTopMargin();
            var12 = var1.getRightMargin();
            var14 = var3 - var10 - var1.getBottomMargin();
            var16 = var5 - var12 - var1.getLeftMargin();
      }

      var10 /= 72.0;
      var12 /= 72.0;
      var16 /= 72.0;
      var14 /= 72.0;
      MediaPrintableArea var18 = new MediaPrintableArea((float)var10, (float)var12, (float)var14, (float)var16, 25400);
      this.printReqAttrSet.add(var18);
   }

   private void syncPaperSource() {
      Media var1 = (Media)this.printReqAttrSet.get(Media.class);
      if (var1 != null && var1 instanceof MediaTray) {
         this.printReqAttrSet.remove(Media.class);
      }

      PaperSource var2 = this.settings.getPaperSource();
      if (!var2.equals(this.j2dPrinter.defaultPaperSource())) {
         MediaTray var3 = this.j2dPrinter.getTrayForPaperSource(var2);
         if (var3 != null) {
            this.printReqAttrSet.add(var3);
         }
      }

   }

   private void syncColor() {
      if (this.settings.getPrintColor() == PrintColor.MONOCHROME) {
         this.printReqAttrSet.add(Chromaticity.MONOCHROME);
      } else {
         this.printReqAttrSet.add(Chromaticity.COLOR);
      }

   }

   private void syncPrintQuality() {
      javafx.print.PrintQuality var1 = this.settings.getPrintQuality();
      PrintQuality var2;
      if (var1 == javafx.print.PrintQuality.DRAFT) {
         var2 = PrintQuality.DRAFT;
      } else if (var1 == javafx.print.PrintQuality.HIGH) {
         var2 = PrintQuality.HIGH;
      } else {
         var2 = PrintQuality.NORMAL;
      }

      this.printReqAttrSet.add(var2);
   }

   private void syncPrintResolution() {
      PrintService var1 = this.pJob2D.getPrintService();
      if (!var1.isAttributeCategorySupported(PrinterResolution.class)) {
         this.printReqAttrSet.remove(PrinterResolution.class);
      } else {
         PrinterResolution var2 = (PrinterResolution)this.printReqAttrSet.get(PrinterResolution.class);
         if (var2 != null && !var1.isAttributeValueSupported(var2, (DocFlavor)null, (AttributeSet)null)) {
            this.printReqAttrSet.remove(PrinterResolution.class);
         }

         PrintResolution var3 = this.settings.getPrintResolution();
         if (var3 != null) {
            int var4 = var3.getCrossFeedResolution();
            int var5 = var3.getFeedResolution();
            var2 = new PrinterResolution(var4, var5, 100);
            if (var1.isAttributeValueSupported(var2, (DocFlavor)null, (AttributeSet)null)) {
               this.printReqAttrSet.add(var2);
            }
         }
      }
   }

   public PageLayout validatePageLayout(PageLayout var1) {
      boolean var2 = false;
      PrinterAttributes var3 = this.fxPrinter.getPrinterAttributes();
      Paper var4 = var1.getPaper();
      if (!var3.getSupportedPapers().contains(var4)) {
         var2 = true;
         var4 = var3.getDefaultPaper();
      }

      PageOrientation var5 = var1.getPageOrientation();
      if (!var3.getSupportedPageOrientations().contains(var5)) {
         var2 = true;
         var5 = var3.getDefaultPageOrientation();
      }

      if (var2) {
         var1 = this.fxPrinter.createPageLayout(var4, var5, Printer.MarginType.DEFAULT);
      }

      return var1;
   }

   private void checkPermissions() {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPrintJobAccess();
      }

   }

   public boolean print(PageLayout var1, Node var2) {
      if (Toolkit.getToolkit().isFxUserThread() && !Toolkit.getToolkit().canStartNestedEventLoop()) {
         throw new IllegalStateException("Printing is not allowed during animation or layout processing");
      } else if (!this.jobError && !this.jobDone) {
         if (!this.jobRunning) {
            this.checkPermissions();
            this.syncSettingsToAttributes();
            PrintJobRunnable var3 = new PrintJobRunnable();
            Thread var4 = new Thread(var3, "Print Job Thread");
            var4.start();
            this.jobRunning = true;
         }

         try {
            this.j2dPageable.implPrintPage(var1, var2);
         } catch (Throwable var5) {
            if (PrismSettings.debug) {
               System.err.println("printPage caught exception.");
               var5.printStackTrace();
            }

            this.jobError = true;
            this.jobDone = true;
         }

         return !this.jobError;
      } else {
         return false;
      }
   }

   public boolean endJob() {
      if (this.jobRunning && !this.jobDone && !this.jobError) {
         this.jobDone = true;

         try {
            synchronized(this.monitor) {
               this.monitor.notify();
               return this.jobDone;
            }
         } catch (IllegalStateException var4) {
            if (PrismSettings.debug) {
               System.err.println("Internal Error " + var4);
            }

            return this.jobDone;
         }
      } else {
         return false;
      }
   }

   public void cancelJob() {
      if (!this.pJob2D.isCancelled()) {
         this.pJob2D.cancel();
      }

      this.jobDone = true;
      if (this.jobRunning) {
         this.jobRunning = false;

         try {
            synchronized(this.monitor) {
               this.monitor.notify();
            }
         } catch (IllegalStateException var4) {
            if (PrismSettings.debug) {
               System.err.println("Internal Error " + var4);
            }
         }
      }

   }

   private class J2DPageable implements Pageable, Printable {
      private volatile boolean pageDone;
      private int currPageIndex;
      private volatile PageInfo newPageInfo;
      private PageInfo currPageInfo;
      private PageFormat currPageFormat;

      private J2DPageable() {
         this.currPageIndex = -1;
         this.newPageInfo = null;
      }

      private boolean waitForNextPage(int var1) {
         if (J2DPrinterJob.this.elo != null && this.currPageInfo != null) {
            Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, (Object)null));
         }

         if (this.currPageInfo != null) {
            if (Toolkit.getToolkit().isFxUserThread()) {
               this.currPageInfo.clearScene();
            } else {
               Application.invokeAndWait(new ClearSceneRunnable(this.currPageInfo));
            }
         }

         this.currPageInfo = null;
         this.pageDone = true;
         synchronized(J2DPrinterJob.this.monitor) {
            if (this.newPageInfo == null) {
               J2DPrinterJob.this.monitor.notify();
            }

            while(this.newPageInfo == null && !J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
               try {
                  J2DPrinterJob.this.monitor.wait(1000L);
               } catch (InterruptedException var5) {
               }
            }
         }

         if (!J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
            this.currPageInfo = this.newPageInfo;
            this.newPageInfo = null;
            this.currPageIndex = var1;
            this.currPageFormat = this.getPageFormatFromLayout(this.currPageInfo.getPageLayout());
            return true;
         } else {
            return false;
         }
      }

      private PageFormat getPageFormatFromLayout(PageLayout var1) {
         java.awt.print.Paper var2 = new java.awt.print.Paper();
         double var3 = var1.getPaper().getWidth();
         double var5 = var1.getPaper().getHeight();
         double var7 = 0.0;
         double var9 = 0.0;
         double var11 = var3;
         double var13 = var5;
         PageOrientation var15 = var1.getPageOrientation();
         switch (var15) {
            case PORTRAIT:
               var7 = var1.getLeftMargin();
               var9 = var1.getTopMargin();
               var11 = var3 - var7 - var1.getRightMargin();
               var13 = var5 - var9 - var1.getBottomMargin();
               break;
            case REVERSE_PORTRAIT:
               var7 = var1.getRightMargin();
               var9 = var1.getBottomMargin();
               var11 = var3 - var7 - var1.getLeftMargin();
               var13 = var5 - var9 - var1.getTopMargin();
               break;
            case LANDSCAPE:
               var7 = var1.getBottomMargin();
               var9 = var1.getLeftMargin();
               var11 = var3 - var7 - var1.getTopMargin();
               var13 = var5 - var9 - var1.getRightMargin();
               break;
            case REVERSE_LANDSCAPE:
               var7 = var1.getTopMargin();
               var9 = var1.getRightMargin();
               var11 = var3 - var7 - var1.getBottomMargin();
               var13 = var5 - var9 - var1.getLeftMargin();
         }

         var2.setSize(var3, var5);
         var2.setImageableArea(var7, var9, var11, var13);
         PageFormat var16 = new PageFormat();
         var16.setOrientation(J2DPrinter.getOrientID(var15));
         var16.setPaper(var2);
         return var16;
      }

      private boolean getPage(int var1) {
         if (var1 == this.currPageIndex) {
            return true;
         } else {
            boolean var2 = false;
            if (var1 > this.currPageIndex) {
               var2 = this.waitForNextPage(var1);
            }

            return var2;
         }
      }

      public int print(Graphics var1, PageFormat var2, int var3) {
         if (!J2DPrinterJob.this.jobError && !J2DPrinterJob.this.jobDone && this.getPage(var3)) {
            int var4 = (int)var2.getImageableX();
            int var5 = (int)var2.getImageableY();
            int var6 = (int)var2.getImageableWidth();
            int var7 = (int)var2.getImageableHeight();
            Node var8 = this.currPageInfo.getNode();
            var1.translate(var4, var5);
            this.printNode(var8, var1, var6, var7);
            return 0;
         } else {
            return 1;
         }
      }

      private void printNode(Node var1, Graphics var2, int var3, int var4) {
         PrismPrintGraphics var5 = new PrismPrintGraphics((Graphics2D)var2, var3, var4);
         NGNode var6 = var1.impl_getPeer();
         boolean var7 = false;

         try {
            var6.render(var5);
         } catch (Throwable var9) {
            if (PrismSettings.debug) {
               System.err.println("printNode caught exception.");
               var9.printStackTrace();
            }

            var7 = true;
         }

         var5.getResourceFactory().getTextureResourcePool().freeDisposalRequestedAndCheckResources(var7);
      }

      public Printable getPrintable(int var1) {
         this.getPage(var1);
         return this;
      }

      public PageFormat getPageFormat(int var1) {
         this.getPage(var1);
         return this.currPageFormat;
      }

      public int getNumberOfPages() {
         return -1;
      }

      private void implPrintPage(PageLayout var1, Node var2) {
         this.pageDone = false;
         synchronized(J2DPrinterJob.this.monitor) {
            this.newPageInfo = new PageInfo(var1, var2);
            J2DPrinterJob.this.monitor.notify();
         }

         if (Toolkit.getToolkit().isFxUserThread()) {
            J2DPrinterJob.this.elo = new Object();
            Toolkit.getToolkit().enterNestedEventLoop(J2DPrinterJob.this.elo);
            J2DPrinterJob.this.elo = null;
         } else {
            while(!this.pageDone && !J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
               synchronized(J2DPrinterJob.this.monitor) {
                  try {
                     if (!this.pageDone) {
                        J2DPrinterJob.this.monitor.wait(1000L);
                     }
                  } catch (InterruptedException var6) {
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      J2DPageable(Object var2) {
         this();
      }
   }

   static class ExitLoopRunnable implements Runnable {
      Object elo;
      Object rv;

      ExitLoopRunnable(Object var1, Object var2) {
         this.elo = var1;
         this.rv = var2;
      }

      public void run() {
         Toolkit.getToolkit().exitNestedEventLoop(this.elo, this.rv);
      }
   }

   private static class PageInfo {
      private PageLayout pageLayout;
      private Node node;
      private Parent root;
      private Node topNode;
      private Group group;
      private boolean tempGroup;
      private boolean tempScene;
      private boolean sceneInited;

      PageInfo(PageLayout var1, Node var2) {
         this.pageLayout = var1;
         this.node = var2;
      }

      Node getNode() {
         this.initScene();
         return this.node;
      }

      PageLayout getPageLayout() {
         return this.pageLayout;
      }

      void initScene() {
         if (!this.sceneInited) {
            if (this.node.getScene() == null) {
               this.tempScene = true;

               Object var1;
               for(var1 = this.node; ((Node)var1).getParent() != null; var1 = ((Node)var1).getParent()) {
               }

               if (var1 instanceof Group) {
                  this.group = (Group)var1;
               } else {
                  this.tempGroup = true;
                  this.group = new Group();
                  this.group.getChildren().add(var1);
               }

               this.root = this.group;
            } else {
               this.root = this.node.getScene().getRoot();
            }

            if (Toolkit.getToolkit().isFxUserThread()) {
               if (this.tempScene && this.root.getScene() == null) {
                  new Scene(this.root);
               }

               NodeHelper.layoutNodeForPrinting(this.root);
            } else {
               Application.invokeAndWait(new LayoutRunnable(this));
            }

            this.sceneInited = true;
         }
      }

      private void clearScene() {
         if (this.tempGroup) {
            this.group.getChildren().removeAll(this.root);
         }

         this.tempGroup = false;
         this.tempScene = false;
         this.root = null;
         this.group = null;
         this.topNode = null;
         this.sceneInited = false;
      }
   }

   static class ClearSceneRunnable implements Runnable {
      PageInfo pageInfo;

      ClearSceneRunnable(PageInfo var1) {
         this.pageInfo = var1;
      }

      public void run() {
         this.pageInfo.clearScene();
      }
   }

   static class LayoutRunnable implements Runnable {
      PageInfo pageInfo;

      LayoutRunnable(PageInfo var1) {
         this.pageInfo = var1;
      }

      public void run() {
         if (this.pageInfo.tempScene && this.pageInfo.root.getScene() == null) {
            new Scene(this.pageInfo.root);
         }

         NodeHelper.layoutNodeForPrinting(this.pageInfo.root);
      }
   }

   private class PrintJobRunnable implements Runnable {
      private PrintJobRunnable() {
      }

      public void run() {
         try {
            J2DPrinterJob.this.pJob2D.print(J2DPrinterJob.this.printReqAttrSet);
            J2DPrinterJob.this.jobDone = true;
         } catch (Throwable var2) {
            if (PrismSettings.debug) {
               System.err.println("print caught exception.");
               var2.printStackTrace();
            }

            J2DPrinterJob.this.jobError = true;
            J2DPrinterJob.this.jobDone = true;
         }

         if (J2DPrinterJob.this.elo != null) {
            Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, (Object)null));
         }

      }

      // $FF: synthetic method
      PrintJobRunnable(Object var2) {
         this();
      }
   }

   private class PageDialogRunnable implements Runnable {
      private PageDialogRunnable() {
      }

      public void run() {
         PageFormat var1 = null;
         boolean var7 = false;

         Boolean var2;
         label93: {
            try {
               var7 = true;
               var1 = J2DPrinterJob.this.pJob2D.pageDialog(J2DPrinterJob.this.printReqAttrSet);
               var7 = false;
               break label93;
            } catch (Exception var8) {
               var7 = false;
            } finally {
               if (var7) {
                  Boolean var4 = var1 != null;
                  Application.invokeLater(new ExitLoopRunnable(this, var4));
               }
            }

            var2 = var1 != null;
            Application.invokeLater(new ExitLoopRunnable(this, var2));
            return;
         }

         var2 = var1 != null;
         Application.invokeLater(new ExitLoopRunnable(this, var2));
      }

      // $FF: synthetic method
      PageDialogRunnable(Object var2) {
         this();
      }
   }

   private class PrintDialogRunnable implements Runnable {
      private PrintDialogRunnable() {
      }

      public void run() {
         boolean var1 = false;

         try {
            var1 = J2DPrinterJob.this.pJob2D.printDialog(J2DPrinterJob.this.printReqAttrSet);
         } catch (Exception var6) {
         } finally {
            Application.invokeLater(new ExitLoopRunnable(this, var1));
         }

      }

      // $FF: synthetic method
      PrintDialogRunnable(Object var2) {
         this();
      }
   }
}
