/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableSet
 *  javafx.print.Printer
 *  javafx.print.PrinterJob
 */
package com.sun.prism.j2d;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.PrintPipeline;
import com.sun.prism.j2d.PrismPrintGraphics;
import com.sun.prism.j2d.print.J2DPrinter;
import com.sun.prism.j2d.print.J2DPrinterJob;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public final class PrismPrintPipeline
extends PrintPipeline {
    private static Printer defaultPrinter = null;
    private static final NameComparator nameComparator = new NameComparator();
    private static ObservableSet<Printer> printerSet = null;

    public static PrintPipeline getInstance() {
        return new PrismPrintPipeline();
    }

    public boolean printNode(NGNode nGNode, int n, int n2, Graphics graphics) {
        PrismPrintGraphics prismPrintGraphics = new PrismPrintGraphics((Graphics2D)graphics, n, n2);
        nGNode.render(prismPrintGraphics);
        return true;
    }

    @Override
    public PrinterJobImpl createPrinterJob(PrinterJob printerJob) {
        return new J2DPrinterJob(printerJob);
    }

    @Override
    public synchronized Printer getDefaultPrinter() {
        if (defaultPrinter == null) {
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                defaultPrinter = null;
            } else if (printerSet == null) {
                J2DPrinter j2DPrinter = new J2DPrinter(printService);
                defaultPrinter = PrintHelper.createPrinter(j2DPrinter);
            } else {
                for (Printer printer : printerSet) {
                    PrinterImpl printerImpl = PrintHelper.getPrinterImpl(printer);
                    J2DPrinter j2DPrinter = (J2DPrinter)printerImpl;
                    if (!j2DPrinter.getService().equals(printService)) continue;
                    defaultPrinter = printer;
                    break;
                }
            }
        }
        return defaultPrinter;
    }

    @Override
    public synchronized ObservableSet<Printer> getAllPrinters() {
        if (printerSet == null) {
            PrintService[] arrprintService;
            TreeSet<Printer> treeSet = new TreeSet<Printer>(nameComparator);
            Printer printer = this.getDefaultPrinter();
            PrintService printService = null;
            if (printer != null) {
                arrprintService = (PrintService[])PrintHelper.getPrinterImpl(printer);
                printService = arrprintService.getService();
            }
            arrprintService = PrintServiceLookup.lookupPrintServices(null, null);
            for (int i = 0; i < arrprintService.length; ++i) {
                if (printService != null && printService.equals(arrprintService[i])) {
                    treeSet.add(printer);
                    continue;
                }
                J2DPrinter j2DPrinter = new J2DPrinter(arrprintService[i]);
                Printer printer2 = PrintHelper.createPrinter(j2DPrinter);
                j2DPrinter.setPrinter(printer2);
                treeSet.add(printer2);
            }
            printerSet = FXCollections.unmodifiableObservableSet((ObservableSet)FXCollections.observableSet(treeSet));
        }
        return printerSet;
    }

    static class NameComparator
    implements Comparator<Printer> {
        NameComparator() {
        }

        @Override
        public int compare(Printer printer, Printer printer2) {
            return printer.getName().compareTo(printer2.getName());
        }
    }
}

