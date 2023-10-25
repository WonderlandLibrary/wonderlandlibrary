/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableSet
 *  javafx.print.Printer
 *  javafx.print.PrinterJob
 */
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
        }
        try {
            String string = "com.sun.prism.j2d.PrismPrintPipeline";
            Class<?> class_ = Class.forName(string);
            Method method = class_.getMethod("getInstance", null);
            ppl = (PrintPipeline)method.invoke(null, null);
            return ppl;
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public abstract Printer getDefaultPrinter();

    public abstract ObservableSet<Printer> getAllPrinters();

    public abstract PrinterJobImpl createPrinterJob(PrinterJob var1);
}

