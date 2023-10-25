/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  javafx.collections.ObservableSet
 *  javafx.print.Collation
 *  javafx.print.JobSettings
 *  javafx.print.PageLayout
 *  javafx.print.PageOrientation
 *  javafx.print.PageRange
 *  javafx.print.Paper
 *  javafx.print.PaperSource
 *  javafx.print.PrintColor
 *  javafx.print.PrintQuality
 *  javafx.print.PrintResolution
 *  javafx.print.PrintSides
 *  javafx.print.Printer
 *  javafx.print.Printer$MarginType
 *  javafx.print.PrinterAttributes
 *  javafx.print.PrinterJob
 *  javafx.scene.Group
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.stage.Window
 */
package com.sun.prism.j2d.print;

import com.sun.glass.ui.Application;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.PrismPrintGraphics;
import com.sun.prism.j2d.print.J2DPrinter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.security.AccessController;
import java.util.ArrayList;
import javafx.collections.ObservableSet;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

public class J2DPrinterJob
implements PrinterJobImpl {
    javafx.print.PrinterJob fxPrinterJob;
    PrinterJob pJob2D;
    Printer fxPrinter;
    J2DPrinter j2dPrinter;
    private JobSettings settings;
    private PrintRequestAttributeSet printReqAttrSet;
    private volatile Object elo = null;
    private static Class onTopClass;
    private boolean jobRunning = false;
    private boolean jobError = false;
    private boolean jobDone = false;
    private J2DPageable j2dPageable = null;
    private Object monitor = new Object();

    PrintRequestAttribute getAlwaysOnTop(long l) {
        return AccessController.doPrivileged(() -> {
            PrintRequestAttribute printRequestAttribute = null;
            try {
                if (onTopClass == null) {
                    onTopClass = Class.forName("javax.print.attribute.standard.DialogOwner");
                }
                if (l == 0L) {
                    Constructor constructor = onTopClass.getConstructor(new Class[0]);
                    printRequestAttribute = (PrintRequestAttribute)constructor.newInstance(new Object[0]);
                } else {
                    printRequestAttribute = J2DPrinterJob.getAlwaysOnTop(onTopClass, l);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return printRequestAttribute;
        });
    }

    private static native PrintRequestAttribute getAlwaysOnTop(Class var0, long var1);

    public J2DPrinterJob(javafx.print.PrinterJob printerJob) {
        this.fxPrinterJob = printerJob;
        this.fxPrinter = this.fxPrinterJob.getPrinter();
        this.j2dPrinter = this.getJ2DPrinter(this.fxPrinter);
        this.settings = this.fxPrinterJob.getJobSettings();
        this.pJob2D = PrinterJob.getPrinterJob();
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        }
        catch (PrinterException printerException) {
            // empty catch block
        }
        this.printReqAttrSet = new HashPrintRequestAttributeSet();
        this.printReqAttrSet.add(DialogTypeSelection.NATIVE);
        this.j2dPageable = new J2DPageable();
        this.pJob2D.setPageable(this.j2dPageable);
    }

    private void setEnabledState(Window window, boolean bl) {
        if (window == null) {
            return;
        }
        TKStage tKStage = WindowHelper.getPeer(window);
        if (tKStage == null) {
            return;
        }
        Application.invokeAndWait(() -> tKStage.setEnabled(bl));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean showPrintDialog(Window window) {
        if (this.jobRunning || this.jobDone) {
            return false;
        }
        if (GraphicsEnvironment.isHeadless()) {
            return true;
        }
        if (onTopClass != null) {
            this.printReqAttrSet.remove(onTopClass);
        }
        if (window != null) {
            PrintRequestAttribute printRequestAttribute;
            long l = 0L;
            if (PlatformUtil.isWindows()) {
                l = WindowHelper.getPeer(window).getRawHandle();
            }
            if ((printRequestAttribute = this.getAlwaysOnTop(l)) != null) {
                this.printReqAttrSet.add(printRequestAttribute);
            }
        }
        boolean bl = false;
        this.syncSettingsToAttributes();
        try {
            this.setEnabledState(window, false);
            if (!Toolkit.getToolkit().isFxUserThread()) {
                bl = this.pJob2D.printDialog(this.printReqAttrSet);
            } else {
                if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                    throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                }
                bl = this.showPrintDialogWithNestedLoop(window);
            }
            if (bl) {
                this.updateSettingsFromDialog();
            }
        }
        finally {
            this.setEnabledState(window, true);
        }
        return bl;
    }

    private boolean showPrintDialogWithNestedLoop(Window window) {
        PrintDialogRunnable printDialogRunnable = new PrintDialogRunnable();
        Thread thread = new Thread((Runnable)printDialogRunnable, "FX Print Dialog Thread");
        thread.start();
        Object object = Toolkit.getToolkit().enterNestedEventLoop(printDialogRunnable);
        boolean bl = false;
        try {
            bl = (Boolean)object;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean showPageDialog(Window window) {
        if (this.jobRunning || this.jobDone) {
            return false;
        }
        if (GraphicsEnvironment.isHeadless()) {
            return true;
        }
        if (onTopClass != null) {
            this.printReqAttrSet.remove(onTopClass);
        }
        if (window != null) {
            PrintRequestAttribute printRequestAttribute;
            long l = 0L;
            if (PlatformUtil.isWindows()) {
                l = WindowHelper.getPeer(window).getRawHandle();
            }
            if ((printRequestAttribute = this.getAlwaysOnTop(l)) != null) {
                this.printReqAttrSet.add(printRequestAttribute);
            }
        }
        boolean bl = false;
        this.syncSettingsToAttributes();
        try {
            this.setEnabledState(window, false);
            if (!Toolkit.getToolkit().isFxUserThread()) {
                PageFormat pageFormat = this.pJob2D.pageDialog(this.printReqAttrSet);
                bl = pageFormat != null;
            } else {
                if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                    throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                }
                bl = this.showPageDialogFromNestedLoop(window);
            }
        }
        finally {
            this.setEnabledState(window, true);
        }
        if (bl) {
            this.updateSettingsFromDialog();
        }
        return bl;
    }

    private boolean showPageDialogFromNestedLoop(Window window) {
        PageDialogRunnable pageDialogRunnable = new PageDialogRunnable();
        Thread thread = new Thread((Runnable)pageDialogRunnable, "FX Page Setup Dialog Thread");
        thread.start();
        Object object = Toolkit.getToolkit().enterNestedEventLoop(pageDialogRunnable);
        boolean bl = false;
        try {
            bl = (Boolean)object;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return bl;
    }

    private void updateJobName() {
        String string = this.pJob2D.getJobName();
        if (!string.equals(this.settings.getJobName())) {
            this.settings.setJobName(string);
        }
    }

    private void updateOutputFile() {
        Destination destination = (Destination)this.printReqAttrSet.get(Destination.class);
        if (destination != null) {
            this.settings.setOutputFile(destination.getURI().getPath());
        } else {
            this.settings.setOutputFile("");
        }
    }

    private void updateCopies() {
        int n = this.pJob2D.getCopies();
        if (this.settings.getCopies() != n) {
            this.settings.setCopies(n);
        }
    }

    private void updatePageRanges() {
        PageRanges pageRanges = (PageRanges)this.printReqAttrSet.get(PageRanges.class);
        if (pageRanges != null) {
            int[][] arrn = pageRanges.getMembers();
            if (arrn.length == 1) {
                PageRange pageRange = new PageRange(arrn[0][0], arrn[0][1]);
                this.settings.setPageRanges(new PageRange[]{pageRange});
            } else if (arrn.length > 0) {
                try {
                    ArrayList<PageRange> arrayList = new ArrayList<PageRange>();
                    int n = 0;
                    for (int i = 0; i < arrn.length; ++i) {
                        int n2 = arrn[i][0];
                        int n3 = arrn[i][1];
                        if (n2 <= n || n3 < n2) {
                            return;
                        }
                        n = n3;
                        arrayList.add(new PageRange(n2, n3));
                    }
                    this.settings.setPageRanges(arrayList.toArray((T[])new PageRange[0]));
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    private void updateSides() {
        Sides sides = (Sides)this.printReqAttrSet.get(Sides.class);
        if (sides == null) {
            sides = (Sides)this.j2dPrinter.getService().getDefaultAttributeValue(Sides.class);
        }
        if (sides == Sides.ONE_SIDED) {
            this.settings.setPrintSides(PrintSides.ONE_SIDED);
        } else if (sides == Sides.DUPLEX) {
            this.settings.setPrintSides(PrintSides.DUPLEX);
        } else if (sides == Sides.TUMBLE) {
            this.settings.setPrintSides(PrintSides.TUMBLE);
        }
    }

    private void updateCollation() {
        SheetCollate sheetCollate = (SheetCollate)this.printReqAttrSet.get(SheetCollate.class);
        if (sheetCollate == null) {
            sheetCollate = this.j2dPrinter.getDefaultSheetCollate();
        }
        if (sheetCollate == SheetCollate.UNCOLLATED) {
            this.settings.setCollation(Collation.UNCOLLATED);
        } else {
            this.settings.setCollation(Collation.COLLATED);
        }
    }

    private void updateColor() {
        Chromaticity chromaticity = (Chromaticity)this.printReqAttrSet.get(Chromaticity.class);
        if (chromaticity == null) {
            chromaticity = this.j2dPrinter.getDefaultChromaticity();
        }
        if (chromaticity == Chromaticity.COLOR) {
            this.settings.setPrintColor(PrintColor.COLOR);
        } else {
            this.settings.setPrintColor(PrintColor.MONOCHROME);
        }
    }

    private void updatePrintQuality() {
        javax.print.attribute.standard.PrintQuality printQuality = (javax.print.attribute.standard.PrintQuality)this.printReqAttrSet.get(javax.print.attribute.standard.PrintQuality.class);
        if (printQuality == null) {
            printQuality = this.j2dPrinter.getDefaultPrintQuality();
        }
        if (printQuality == javax.print.attribute.standard.PrintQuality.DRAFT) {
            this.settings.setPrintQuality(PrintQuality.DRAFT);
        } else if (printQuality == javax.print.attribute.standard.PrintQuality.HIGH) {
            this.settings.setPrintQuality(PrintQuality.HIGH);
        } else {
            this.settings.setPrintQuality(PrintQuality.NORMAL);
        }
    }

    private void updatePrintResolution() {
        PrinterResolution printerResolution = (PrinterResolution)this.printReqAttrSet.get(PrinterResolution.class);
        if (printerResolution == null) {
            printerResolution = this.j2dPrinter.getDefaultPrinterResolution();
        }
        int n = printerResolution.getCrossFeedResolution(100);
        int n2 = printerResolution.getFeedResolution(100);
        this.settings.setPrintResolution(PrintHelper.createPrintResolution(n, n2));
    }

    private void updatePageLayout() {
        PageLayout pageLayout;
        Media media = (Media)this.printReqAttrSet.get(Media.class);
        javafx.print.Paper paper = this.j2dPrinter.getPaperForMedia(media);
        OrientationRequested orientationRequested = (OrientationRequested)this.printReqAttrSet.get(OrientationRequested.class);
        PageOrientation pageOrientation = J2DPrinter.reverseMapOrientation(orientationRequested);
        MediaPrintableArea mediaPrintableArea = (MediaPrintableArea)this.printReqAttrSet.get(MediaPrintableArea.class);
        if (mediaPrintableArea == null) {
            pageLayout = this.fxPrinter.createPageLayout(paper, pageOrientation, Printer.MarginType.DEFAULT);
        } else {
            double d = paper.getWidth();
            double d2 = paper.getHeight();
            int n = 25400;
            double d3 = mediaPrintableArea.getX(n) * 72.0f;
            double d4 = mediaPrintableArea.getY(n) * 72.0f;
            double d5 = mediaPrintableArea.getWidth(n) * 72.0f;
            double d6 = mediaPrintableArea.getHeight(n) * 72.0f;
            double d7 = 0.0;
            double d8 = 0.0;
            double d9 = 0.0;
            double d10 = 0.0;
            switch (pageOrientation) {
                case PORTRAIT: {
                    d7 = d3;
                    d8 = d - d3 - d5;
                    d9 = d4;
                    d10 = d2 - d4 - d6;
                    break;
                }
                case REVERSE_PORTRAIT: {
                    d7 = d - d3 - d5;
                    d8 = d3;
                    d9 = d2 - d4 - d6;
                    d10 = d4;
                    break;
                }
                case LANDSCAPE: {
                    d7 = d4;
                    d8 = d2 - d4 - d6;
                    d9 = d - d3 - d5;
                    d10 = d3;
                    break;
                }
                case REVERSE_LANDSCAPE: {
                    d7 = d2 - d4 - d6;
                    d9 = d3;
                    d8 = d4;
                    d10 = d - d3 - d5;
                }
            }
            if (Math.abs(d7) < 0.01) {
                d7 = 0.0;
            }
            if (Math.abs(d8) < 0.01) {
                d8 = 0.0;
            }
            if (Math.abs(d9) < 0.01) {
                d9 = 0.0;
            }
            if (Math.abs(d10) < 0.01) {
                d10 = 0.0;
            }
            pageLayout = this.fxPrinter.createPageLayout(paper, pageOrientation, d7, d8, d9, d10);
        }
        this.settings.setPageLayout(pageLayout);
    }

    private void updatePaperSource() {
        PaperSource paperSource;
        Media media = (Media)this.printReqAttrSet.get(Media.class);
        if (media instanceof MediaTray && (paperSource = this.j2dPrinter.getPaperSource((MediaTray)media)) != null) {
            this.settings.setPaperSource(paperSource);
        }
    }

    private Printer getFXPrinterForService(PrintService printService) {
        ObservableSet observableSet = Printer.getAllPrinters();
        for (Printer printer : observableSet) {
            J2DPrinter j2DPrinter = (J2DPrinter)PrintHelper.getPrinterImpl(printer);
            PrintService printService2 = j2DPrinter.getService();
            if (!printService2.equals(printService)) continue;
            return printer;
        }
        return this.fxPrinter;
    }

    @Override
    public void setPrinterImpl(PrinterImpl printerImpl) {
        this.j2dPrinter = (J2DPrinter)printerImpl;
        this.fxPrinter = this.j2dPrinter.getPrinter();
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        }
        catch (PrinterException printerException) {
            // empty catch block
        }
    }

    @Override
    public PrinterImpl getPrinterImpl() {
        return this.j2dPrinter;
    }

    private J2DPrinter getJ2DPrinter(Printer printer) {
        return (J2DPrinter)PrintHelper.getPrinterImpl(printer);
    }

    public Printer getPrinter() {
        return this.fxPrinter;
    }

    public void setPrinter(Printer printer) {
        this.fxPrinter = printer;
        this.j2dPrinter = this.getJ2DPrinter(printer);
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        }
        catch (PrinterException printerException) {
            // empty catch block
        }
    }

    private void updatePrinter() {
        PrintService printService;
        PrintService printService2 = this.j2dPrinter.getService();
        if (printService2.equals(printService = this.pJob2D.getPrintService())) {
            return;
        }
        Printer printer = this.getFXPrinterForService(printService);
        this.fxPrinterJob.setPrinter(printer);
    }

    private void updateSettingsFromDialog() {
        this.updatePrinter();
        this.updateJobName();
        this.updateOutputFile();
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
        this.syncOutputFile();
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

    private void syncOutputFile() {
        this.printReqAttrSet.remove(Destination.class);
        String string = this.settings.getOutputFile();
        if (string != null && !string.isEmpty()) {
            URI uRI = new File(string).toURI();
            Destination destination = new Destination(uRI);
            this.printReqAttrSet.add(destination);
        }
    }

    private void syncCopies() {
        this.pJob2D.setCopies(this.settings.getCopies());
        this.printReqAttrSet.add(new Copies(this.settings.getCopies()));
    }

    private void syncPageRanges() {
        this.printReqAttrSet.remove(PageRanges.class);
        PageRange[] arrpageRange = this.settings.getPageRanges();
        if (arrpageRange != null && arrpageRange.length > 0) {
            int n = arrpageRange.length;
            int[][] arrn = new int[n][2];
            for (int i = 0; i < n; ++i) {
                arrn[i][0] = arrpageRange[i].getStartPage();
                arrn[i][1] = arrpageRange[i].getEndPage();
            }
            this.printReqAttrSet.add(new PageRanges(arrn));
        }
    }

    private void syncSides() {
        Sides sides = Sides.ONE_SIDED;
        PrintSides printSides = this.settings.getPrintSides();
        if (printSides == PrintSides.DUPLEX) {
            sides = Sides.DUPLEX;
        } else if (printSides == PrintSides.TUMBLE) {
            sides = Sides.TUMBLE;
        }
        this.printReqAttrSet.add(sides);
    }

    private void syncCollation() {
        if (this.settings.getCollation() == Collation.UNCOLLATED) {
            this.printReqAttrSet.add(SheetCollate.UNCOLLATED);
        } else {
            this.printReqAttrSet.add(SheetCollate.COLLATED);
        }
    }

    private void syncPageLayout() {
        PageLayout pageLayout = this.settings.getPageLayout();
        PageOrientation pageOrientation = pageLayout.getPageOrientation();
        this.printReqAttrSet.add(J2DPrinter.mapOrientation(pageOrientation));
        double d = pageLayout.getPaper().getWidth();
        double d2 = pageLayout.getPaper().getHeight();
        float f = (float)(d / 72.0);
        float f2 = (float)(d2 / 72.0);
        MediaSizeName mediaSizeName = MediaSize.findMedia(f, f2, 25400);
        if (mediaSizeName == null) {
            mediaSizeName = MediaSizeName.NA_LETTER;
        }
        this.printReqAttrSet.add(mediaSizeName);
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = d;
        double d6 = d2;
        switch (pageOrientation) {
            case PORTRAIT: {
                d3 = pageLayout.getLeftMargin();
                d4 = pageLayout.getTopMargin();
                d5 = d - d3 - pageLayout.getRightMargin();
                d6 = d2 - d4 - pageLayout.getBottomMargin();
                break;
            }
            case REVERSE_PORTRAIT: {
                d3 = pageLayout.getRightMargin();
                d4 = pageLayout.getBottomMargin();
                d5 = d - d3 - pageLayout.getLeftMargin();
                d6 = d2 - d4 - pageLayout.getTopMargin();
                break;
            }
            case LANDSCAPE: {
                d3 = pageLayout.getBottomMargin();
                d4 = pageLayout.getLeftMargin();
                d5 = d - d3 - pageLayout.getTopMargin();
                d6 = d2 - d4 - pageLayout.getRightMargin();
                break;
            }
            case REVERSE_LANDSCAPE: {
                d3 = pageLayout.getTopMargin();
                d4 = pageLayout.getRightMargin();
                d5 = d - d3 - pageLayout.getBottomMargin();
                d6 = d2 - d4 - pageLayout.getLeftMargin();
            }
        }
        MediaPrintableArea mediaPrintableArea = new MediaPrintableArea((float)(d3 /= 72.0), (float)(d4 /= 72.0), (float)(d5 /= 72.0), (float)(d6 /= 72.0), 25400);
        this.printReqAttrSet.add(mediaPrintableArea);
    }

    private void syncPaperSource() {
        MediaTray mediaTray;
        PaperSource paperSource;
        Media media = (Media)this.printReqAttrSet.get(Media.class);
        if (media != null && media instanceof MediaTray) {
            this.printReqAttrSet.remove(Media.class);
        }
        if (!(paperSource = this.settings.getPaperSource()).equals((Object)this.j2dPrinter.defaultPaperSource()) && (mediaTray = this.j2dPrinter.getTrayForPaperSource(paperSource)) != null) {
            this.printReqAttrSet.add(mediaTray);
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
        PrintQuality printQuality = this.settings.getPrintQuality();
        javax.print.attribute.standard.PrintQuality printQuality2 = printQuality == PrintQuality.DRAFT ? javax.print.attribute.standard.PrintQuality.DRAFT : (printQuality == PrintQuality.HIGH ? javax.print.attribute.standard.PrintQuality.HIGH : javax.print.attribute.standard.PrintQuality.NORMAL);
        this.printReqAttrSet.add(printQuality2);
    }

    private void syncPrintResolution() {
        int n;
        PrintResolution printResolution;
        PrintService printService = this.pJob2D.getPrintService();
        if (!printService.isAttributeCategorySupported(PrinterResolution.class)) {
            this.printReqAttrSet.remove(PrinterResolution.class);
            return;
        }
        PrinterResolution printerResolution = (PrinterResolution)this.printReqAttrSet.get(PrinterResolution.class);
        if (printerResolution != null && !printService.isAttributeValueSupported(printerResolution, null, null)) {
            this.printReqAttrSet.remove(PrinterResolution.class);
        }
        if ((printResolution = this.settings.getPrintResolution()) == null) {
            return;
        }
        int n2 = printResolution.getCrossFeedResolution();
        printerResolution = new PrinterResolution(n2, n = printResolution.getFeedResolution(), 100);
        if (!printService.isAttributeValueSupported(printerResolution, null, null)) {
            return;
        }
        this.printReqAttrSet.add(printerResolution);
    }

    @Override
    public PageLayout validatePageLayout(PageLayout pageLayout) {
        boolean bl = false;
        PrinterAttributes printerAttributes = this.fxPrinter.getPrinterAttributes();
        javafx.print.Paper paper = pageLayout.getPaper();
        if (!printerAttributes.getSupportedPapers().contains((Object)paper)) {
            bl = true;
            paper = printerAttributes.getDefaultPaper();
        }
        PageOrientation pageOrientation = pageLayout.getPageOrientation();
        if (!printerAttributes.getSupportedPageOrientations().contains((Object)pageOrientation)) {
            bl = true;
            pageOrientation = printerAttributes.getDefaultPageOrientation();
        }
        if (bl) {
            pageLayout = this.fxPrinter.createPageLayout(paper, pageOrientation, Printer.MarginType.DEFAULT);
        }
        return pageLayout;
    }

    private void checkPermissions() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
            String string = this.settings.getOutputFile();
            if (string != null && !string.isEmpty()) {
                securityManager.checkWrite(string);
            }
        }
    }

    @Override
    public boolean print(PageLayout pageLayout, Node node) {
        if (Toolkit.getToolkit().isFxUserThread() && !Toolkit.getToolkit().canStartNestedEventLoop()) {
            throw new IllegalStateException("Printing is not allowed during animation or layout processing");
        }
        if (this.jobError || this.jobDone) {
            return false;
        }
        if (!this.jobRunning) {
            this.checkPermissions();
            this.syncSettingsToAttributes();
            PrintJobRunnable printJobRunnable = new PrintJobRunnable();
            Thread thread = new Thread((Runnable)printJobRunnable, "Print Job Thread");
            thread.start();
            this.jobRunning = true;
        }
        try {
            this.j2dPageable.implPrintPage(pageLayout, node);
        }
        catch (Throwable throwable) {
            if (PrismSettings.debug) {
                System.err.println("printPage caught exception.");
                throwable.printStackTrace();
            }
            this.jobError = true;
            this.jobDone = true;
        }
        return !this.jobError;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean endJob() {
        if (this.jobRunning && !this.jobDone && !this.jobError) {
            this.jobDone = true;
            try {
                Object object = this.monitor;
                synchronized (object) {
                    this.monitor.notify();
                    return this.jobDone;
                }
            }
            catch (IllegalStateException illegalStateException) {
                if (!PrismSettings.debug) return this.jobDone;
                System.err.println("Internal Error " + illegalStateException);
                return this.jobDone;
            }
        } else {
            if (!this.jobDone) return false;
            if (this.jobError) return false;
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancelJob() {
        block7: {
            if (!this.pJob2D.isCancelled()) {
                this.pJob2D.cancel();
            }
            this.jobDone = true;
            if (this.jobRunning) {
                this.jobRunning = false;
                try {
                    Object object = this.monitor;
                    synchronized (object) {
                        this.monitor.notify();
                    }
                }
                catch (IllegalStateException illegalStateException) {
                    if (!PrismSettings.debug) break block7;
                    System.err.println("Internal Error " + illegalStateException);
                }
            }
        }
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            String string = "prism_common";
            if (PrismSettings.verbose) {
                System.out.println("Loading Prism common native library ...");
            }
            NativeLibLoader.loadLibrary(string);
            if (PrismSettings.verbose) {
                System.out.println("\tsucceeded.");
            }
            return null;
        });
        onTopClass = null;
    }

    private class J2DPageable
    implements Pageable,
    Printable {
        private volatile boolean pageDone;
        private int currPageIndex = -1;
        private volatile PageInfo newPageInfo = null;
        private PageInfo currPageInfo;
        private PageFormat currPageFormat;

        private J2DPageable() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private boolean waitForNextPage(int n) {
            if (J2DPrinterJob.this.elo != null && this.currPageInfo != null) {
                Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, null));
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
            Object object = J2DPrinterJob.this.monitor;
            synchronized (object) {
                if (this.newPageInfo == null) {
                    J2DPrinterJob.this.monitor.notify();
                }
                while (this.newPageInfo == null && !J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
                    try {
                        J2DPrinterJob.this.monitor.wait(1000L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
            }
            if (J2DPrinterJob.this.jobDone || J2DPrinterJob.this.jobError) {
                return false;
            }
            this.currPageInfo = this.newPageInfo;
            this.newPageInfo = null;
            this.currPageIndex = n;
            this.currPageFormat = this.getPageFormatFromLayout(this.currPageInfo.getPageLayout());
            return true;
        }

        private PageFormat getPageFormatFromLayout(PageLayout pageLayout) {
            Paper paper = new Paper();
            double d = pageLayout.getPaper().getWidth();
            double d2 = pageLayout.getPaper().getHeight();
            double d3 = 0.0;
            double d4 = 0.0;
            double d5 = d;
            double d6 = d2;
            PageOrientation pageOrientation = pageLayout.getPageOrientation();
            switch (pageOrientation) {
                case PORTRAIT: {
                    d3 = pageLayout.getLeftMargin();
                    d4 = pageLayout.getTopMargin();
                    d5 = d - d3 - pageLayout.getRightMargin();
                    d6 = d2 - d4 - pageLayout.getBottomMargin();
                    break;
                }
                case REVERSE_PORTRAIT: {
                    d3 = pageLayout.getRightMargin();
                    d4 = pageLayout.getBottomMargin();
                    d5 = d - d3 - pageLayout.getLeftMargin();
                    d6 = d2 - d4 - pageLayout.getTopMargin();
                    break;
                }
                case LANDSCAPE: {
                    d3 = pageLayout.getBottomMargin();
                    d4 = pageLayout.getLeftMargin();
                    d5 = d - d3 - pageLayout.getTopMargin();
                    d6 = d2 - d4 - pageLayout.getRightMargin();
                    break;
                }
                case REVERSE_LANDSCAPE: {
                    d3 = pageLayout.getTopMargin();
                    d4 = pageLayout.getRightMargin();
                    d5 = d - d3 - pageLayout.getBottomMargin();
                    d6 = d2 - d4 - pageLayout.getLeftMargin();
                }
            }
            paper.setSize(d, d2);
            paper.setImageableArea(d3, d4, d5, d6);
            PageFormat pageFormat = new PageFormat();
            pageFormat.setOrientation(J2DPrinter.getOrientID(pageOrientation));
            pageFormat.setPaper(paper);
            return pageFormat;
        }

        private boolean getPage(int n) {
            if (n == this.currPageIndex) {
                return true;
            }
            boolean bl = false;
            if (n > this.currPageIndex) {
                bl = this.waitForNextPage(n);
            }
            return bl;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int n) {
            if (J2DPrinterJob.this.jobError || J2DPrinterJob.this.jobDone || !this.getPage(n)) {
                return 1;
            }
            int n2 = (int)pageFormat.getImageableX();
            int n3 = (int)pageFormat.getImageableY();
            int n4 = (int)pageFormat.getImageableWidth();
            int n5 = (int)pageFormat.getImageableHeight();
            Node node = this.currPageInfo.getNode();
            graphics.translate(n2, n3);
            this.printNode(node, graphics, n4, n5);
            return 0;
        }

        private void printNode(Node node, Graphics graphics, int n, int n2) {
            PrismPrintGraphics prismPrintGraphics = new PrismPrintGraphics((Graphics2D)graphics, n, n2);
            Object p = NodeHelper.getPeer(node);
            boolean bl = false;
            try {
                ((NGNode)p).render(prismPrintGraphics);
            }
            catch (Throwable throwable) {
                if (PrismSettings.debug) {
                    System.err.println("printNode caught exception.");
                    throwable.printStackTrace();
                }
                bl = true;
            }
            prismPrintGraphics.getResourceFactory().getTextureResourcePool().freeDisposalRequestedAndCheckResources(bl);
        }

        @Override
        public Printable getPrintable(int n) {
            this.getPage(n);
            return this;
        }

        @Override
        public PageFormat getPageFormat(int n) {
            this.getPage(n);
            return this.currPageFormat;
        }

        @Override
        public int getNumberOfPages() {
            return -1;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void implPrintPage(PageLayout pageLayout, Node node) {
            this.pageDone = false;
            Object object = J2DPrinterJob.this.monitor;
            synchronized (object) {
                this.newPageInfo = new PageInfo(pageLayout, node);
                J2DPrinterJob.this.monitor.notify();
            }
            if (Toolkit.getToolkit().isFxUserThread()) {
                J2DPrinterJob.this.elo = new Object();
                Toolkit.getToolkit().enterNestedEventLoop(J2DPrinterJob.this.elo);
                J2DPrinterJob.this.elo = null;
            } else {
                while (!(this.pageDone || J2DPrinterJob.this.jobDone || J2DPrinterJob.this.jobError)) {
                    object = J2DPrinterJob.this.monitor;
                    synchronized (object) {
                        try {
                            if (!this.pageDone) {
                                J2DPrinterJob.this.monitor.wait(1000L);
                            }
                        }
                        catch (InterruptedException interruptedException) {
                            // empty catch block
                        }
                    }
                }
            }
        }
    }

    private class PrintDialogRunnable
    implements Runnable {
        private PrintDialogRunnable() {
        }

        @Override
        public void run() {
            boolean bl = false;
            try {
                bl = J2DPrinterJob.this.pJob2D.printDialog(J2DPrinterJob.this.printReqAttrSet);
            }
            catch (Exception exception) {
            }
            finally {
                Application.invokeLater(new ExitLoopRunnable(this, bl));
            }
        }
    }

    private class PageDialogRunnable
    implements Runnable {
        private PageDialogRunnable() {
        }

        @Override
        public void run() {
            Boolean bl;
            PageFormat pageFormat = null;
            try {
                pageFormat = J2DPrinterJob.this.pJob2D.pageDialog(J2DPrinterJob.this.printReqAttrSet);
                bl = pageFormat != null;
            }
            catch (Exception exception) {
                Boolean bl2 = pageFormat != null;
                Application.invokeLater(new ExitLoopRunnable(this, bl2));
            }
            catch (Throwable throwable) {
                Boolean bl3 = pageFormat != null;
                Application.invokeLater(new ExitLoopRunnable(this, bl3));
                throw throwable;
            }
            Application.invokeLater(new ExitLoopRunnable(this, bl));
        }
    }

    private class PrintJobRunnable
    implements Runnable {
        private PrintJobRunnable() {
        }

        @Override
        public void run() {
            try {
                J2DPrinterJob.this.pJob2D.print(J2DPrinterJob.this.printReqAttrSet);
                J2DPrinterJob.this.jobDone = true;
            }
            catch (Throwable throwable) {
                if (PrismSettings.debug) {
                    System.err.println("print caught exception.");
                    throwable.printStackTrace();
                }
                J2DPrinterJob.this.jobError = true;
                J2DPrinterJob.this.jobDone = true;
            }
            if (J2DPrinterJob.this.elo != null) {
                Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, null));
            }
        }
    }

    static class ExitLoopRunnable
    implements Runnable {
        Object elo;
        Object rv;

        ExitLoopRunnable(Object object, Object object2) {
            this.elo = object;
            this.rv = object2;
        }

        @Override
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

        PageInfo(PageLayout pageLayout, Node node) {
            this.pageLayout = pageLayout;
            this.node = node;
        }

        Node getNode() {
            this.initScene();
            return this.node;
        }

        PageLayout getPageLayout() {
            return this.pageLayout;
        }

        void initScene() {
            if (this.sceneInited) {
                return;
            }
            if (this.node.getScene() == null) {
                this.tempScene = true;
                Node node = this.node;
                while (node.getParent() != null) {
                    node = node.getParent();
                }
                if (node instanceof Group) {
                    this.group = (Group)node;
                } else {
                    this.tempGroup = true;
                    this.group = new Group();
                    this.group.getChildren().add((Object)node);
                }
                this.root = this.group;
            } else {
                this.root = this.node.getScene().getRoot();
            }
            if (Toolkit.getToolkit().isFxUserThread()) {
                if (this.tempScene && this.root.getScene() == null) {
                    new Scene(this.root);
                }
                NodeHelper.layoutNodeForPrinting((Node)this.root);
            } else {
                Application.invokeAndWait(new LayoutRunnable(this));
            }
            this.sceneInited = true;
        }

        private void clearScene() {
            if (this.tempGroup) {
                this.group.getChildren().removeAll((Object[])new Node[]{this.root});
            }
            this.tempGroup = false;
            this.tempScene = false;
            this.root = null;
            this.group = null;
            this.topNode = null;
            this.sceneInited = false;
        }
    }

    static class ClearSceneRunnable
    implements Runnable {
        PageInfo pageInfo;

        ClearSceneRunnable(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        @Override
        public void run() {
            this.pageInfo.clearScene();
        }
    }

    static class LayoutRunnable
    implements Runnable {
        PageInfo pageInfo;

        LayoutRunnable(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        @Override
        public void run() {
            if (this.pageInfo.tempScene && this.pageInfo.root.getScene() == null) {
                new Scene(this.pageInfo.root);
            }
            NodeHelper.layoutNodeForPrinting((Node)this.pageInfo.root);
        }
    }
}

