/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.print.JobSettings
 *  javafx.print.Paper
 *  javafx.print.PaperSource
 *  javafx.print.PrintResolution
 *  javafx.print.Printer
 */
package com.sun.javafx.print;

import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import javafx.print.JobSettings;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintResolution;
import javafx.print.Printer;

public class PrintHelper {
    private static PrintAccessor printAccessor;

    private PrintHelper() {
    }

    public static PrintResolution createPrintResolution(int n, int n2) {
        return printAccessor.createPrintResolution(n, n2);
    }

    public static Paper createPaper(String string, double d, double d2, Units units) {
        return printAccessor.createPaper(string, d, d2, units);
    }

    public static PaperSource createPaperSource(String string) {
        return printAccessor.createPaperSource(string);
    }

    public static JobSettings createJobSettings(Printer printer) {
        return printAccessor.createJobSettings(printer);
    }

    public static Printer createPrinter(PrinterImpl printerImpl) {
        return printAccessor.createPrinter(printerImpl);
    }

    public static PrinterImpl getPrinterImpl(Printer printer) {
        return printAccessor.getPrinterImpl(printer);
    }

    public static void setPrintAccessor(PrintAccessor printAccessor) {
        if (PrintHelper.printAccessor != null) {
            throw new IllegalStateException();
        }
        PrintHelper.printAccessor = printAccessor;
    }

    private static void forceInit(Class<?> class_) {
        try {
            Class.forName(class_.getName(), true, class_.getClassLoader());
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new AssertionError((Object)classNotFoundException);
        }
    }

    static {
        PrintHelper.forceInit(Printer.class);
    }

    public static interface PrintAccessor {
        public PrintResolution createPrintResolution(int var1, int var2);

        public Paper createPaper(String var1, double var2, double var4, Units var6);

        public PaperSource createPaperSource(String var1);

        public JobSettings createJobSettings(Printer var1);

        public Printer createPrinter(PrinterImpl var1);

        public PrinterImpl getPrinterImpl(Printer var1);
    }
}

