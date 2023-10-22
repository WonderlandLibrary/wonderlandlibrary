package com.sun.javafx.print;

import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;

public interface PrinterImpl {
   void setPrinter(Printer var1);

   String getName();

   JobSettings getDefaultJobSettings();

   Rectangle2D printableArea(Paper var1);

   int defaultCopies();

   int maxCopies();

   Collation defaultCollation();

   Set supportedCollations();

   PrintSides defaultSides();

   Set supportedSides();

   PageRange defaultPageRange();

   boolean supportsPageRanges();

   PrintResolution defaultPrintResolution();

   Set supportedPrintResolution();

   PrintColor defaultPrintColor();

   Set supportedPrintColor();

   PrintQuality defaultPrintQuality();

   Set supportedPrintQuality();

   PageOrientation defaultOrientation();

   Set supportedOrientation();

   Paper defaultPaper();

   Set supportedPapers();

   PaperSource defaultPaperSource();

   Set supportedPaperSources();
}
