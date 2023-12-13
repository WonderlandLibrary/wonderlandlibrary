package com.sun.javafx.print;

import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.stage.Window;

public interface PrinterJobImpl {
   PrinterImpl getPrinterImpl();

   void setPrinterImpl(PrinterImpl var1);

   boolean showPrintDialog(Window var1);

   boolean showPageDialog(Window var1);

   PageLayout validatePageLayout(PageLayout var1);

   boolean print(PageLayout var1, Node var2);

   boolean endJob();

   void cancelJob();
}
