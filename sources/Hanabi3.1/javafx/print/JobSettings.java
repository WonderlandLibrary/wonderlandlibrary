package javafx.print;

import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public final class JobSettings {
   private PrinterJob job;
   private Printer printer;
   private PrinterAttributes printerCaps;
   private boolean defaultCopies = true;
   private boolean hasOldCopies = false;
   private int oldCopies;
   private boolean defaultSides = true;
   private boolean hasOldSides = false;
   private PrintSides oldSides;
   private boolean defaultCollation = true;
   private boolean hasOldCollation = false;
   private Collation oldCollation;
   private boolean defaultPrintColor = true;
   private boolean hasOldPrintColor = false;
   private PrintColor oldPrintColor;
   private boolean defaultPrintQuality = true;
   private boolean hasOldPrintQuality = false;
   private PrintQuality oldPrintQuality;
   private boolean defaultPrintResolution = true;
   private boolean hasOldPrintResolution = false;
   private PrintResolution oldPrintResolution;
   private boolean defaultPaperSource = true;
   private boolean hasOldPaperSource = false;
   private PaperSource oldPaperSource;
   private boolean defaultPageLayout = true;
   private boolean hasOldPageLayout = false;
   private PageLayout oldPageLayout;
   private static final String DEFAULT_JOBNAME = "JavaFX Print Job";
   private SimpleStringProperty jobName;
   private IntegerProperty copies;
   private ObjectProperty pageRanges = null;
   private ObjectProperty sides = null;
   private ObjectProperty collation = null;
   private ObjectProperty color = null;
   private ObjectProperty quality = null;
   private ObjectProperty resolution = null;
   private ObjectProperty paperSource = null;
   private ObjectProperty layout = null;

   JobSettings(Printer var1) {
      this.printer = var1;
      this.printerCaps = var1.getPrinterAttributes();
   }

   void setPrinterJob(PrinterJob var1) {
      this.job = var1;
   }

   private boolean isJobNew() {
      return this.job == null || this.job.isJobNew();
   }

   void updateForPrinter(Printer var1) {
      this.printer = var1;
      this.printerCaps = var1.getPrinterAttributes();
      if (this.defaultCopies) {
         if (this.getCopies() != this.printerCaps.getDefaultCopies()) {
            this.setCopies(this.printerCaps.getDefaultCopies());
            this.defaultCopies = true;
         }
      } else {
         int var2 = this.getCopies();
         if (this.hasOldCopies && this.oldCopies > var2) {
            var2 = this.oldCopies;
         }

         int var3 = this.printerCaps.getMaxCopies();
         if (!this.hasOldCopies && this.getCopies() > var3) {
            this.hasOldCopies = true;
            this.oldCopies = this.getCopies();
         }

         if (var2 > var3) {
            var2 = var3;
         }

         this.setCopies(var2);
      }

      PrintSides var24 = this.getPrintSides();
      PrintSides var23 = this.printerCaps.getDefaultPrintSides();
      Set var4 = this.printerCaps.getSupportedPrintSides();
      if (this.defaultSides) {
         if (var24 != var23) {
            this.setPrintSides(var23);
            this.defaultSides = true;
         }
      } else if (this.hasOldSides) {
         if (var4.contains(this.oldSides)) {
            this.setPrintSides(this.oldSides);
            this.hasOldSides = false;
         } else {
            this.setPrintSides(var23);
         }
      } else if (!var4.contains(var24)) {
         this.hasOldSides = true;
         this.oldSides = var24;
         this.setPrintSides(var23);
      }

      Collation var5 = this.getCollation();
      Collation var6 = this.printerCaps.getDefaultCollation();
      Set var7 = this.printerCaps.getSupportedCollations();
      if (this.defaultCollation) {
         if (var5 != var6) {
            this.setCollation(var6);
            this.defaultCollation = true;
         }
      } else if (this.hasOldCollation) {
         if (var7.contains(this.oldCollation)) {
            this.setCollation(this.oldCollation);
            this.hasOldCollation = false;
         } else {
            this.setCollation(var6);
         }
      } else if (!var7.contains(var5)) {
         this.hasOldCollation = true;
         this.oldCollation = var5;
         this.setCollation(var6);
      }

      PrintColor var8 = this.getPrintColor();
      PrintColor var9 = this.printerCaps.getDefaultPrintColor();
      Set var10 = this.printerCaps.getSupportedPrintColors();
      if (this.defaultPrintColor) {
         if (var8 != var9) {
            this.setPrintColor(var9);
            this.defaultPrintColor = true;
         }
      } else if (this.hasOldPrintColor) {
         if (var10.contains(this.oldPrintColor)) {
            this.setPrintColor(this.oldPrintColor);
            this.hasOldPrintColor = false;
         } else {
            this.setPrintColor(var9);
         }
      } else if (!var10.contains(var8)) {
         this.hasOldPrintColor = true;
         this.oldPrintColor = var8;
         this.setPrintColor(var9);
      }

      PrintQuality var11 = this.getPrintQuality();
      PrintQuality var12 = this.printerCaps.getDefaultPrintQuality();
      Set var13 = this.printerCaps.getSupportedPrintQuality();
      if (this.defaultPrintQuality) {
         if (var11 != var12) {
            this.setPrintQuality(var12);
            this.defaultPrintQuality = true;
         }
      } else if (this.hasOldPrintQuality) {
         if (var13.contains(this.oldPrintQuality)) {
            this.setPrintQuality(this.oldPrintQuality);
            this.hasOldPrintQuality = false;
         } else {
            this.setPrintQuality(var12);
         }
      } else if (!var13.contains(var11)) {
         this.hasOldPrintQuality = true;
         this.oldPrintQuality = var11;
         this.setPrintQuality(var12);
      }

      PrintResolution var14 = this.getPrintResolution();
      PrintResolution var15 = this.printerCaps.getDefaultPrintResolution();
      Set var16 = this.printerCaps.getSupportedPrintResolutions();
      if (this.defaultPrintResolution) {
         if (var14 != var15) {
            this.setPrintResolution(var15);
            this.defaultPrintResolution = true;
         }
      } else if (this.hasOldPrintResolution) {
         if (var16.contains(this.oldPrintResolution)) {
            this.setPrintResolution(this.oldPrintResolution);
            this.hasOldPrintResolution = false;
         } else {
            this.setPrintResolution(var15);
         }
      } else if (!var16.contains(var14)) {
         this.hasOldPrintResolution = true;
         this.oldPrintResolution = var14;
         this.setPrintResolution(var15);
      }

      PaperSource var17 = this.getPaperSource();
      PaperSource var18 = this.printerCaps.getDefaultPaperSource();
      Set var19 = this.printerCaps.getSupportedPaperSources();
      if (this.defaultPaperSource) {
         if (var17 != var18) {
            this.setPaperSource(var18);
            this.defaultPaperSource = true;
         }
      } else if (this.hasOldPaperSource) {
         if (var19.contains(this.oldPaperSource)) {
            this.setPaperSource(this.oldPaperSource);
            this.hasOldPaperSource = false;
         } else {
            this.setPaperSource(var18);
         }
      } else if (!var19.contains(var17)) {
         this.hasOldPaperSource = true;
         this.oldPaperSource = var17;
         this.setPaperSource(var18);
      }

      PageLayout var20 = this.getPageLayout();
      PageLayout var21 = var1.getDefaultPageLayout();
      if (this.defaultPageLayout) {
         if (!var20.equals(var21)) {
            this.setPageLayout(var21);
            this.defaultPageLayout = true;
         }
      } else {
         PageLayout var22;
         if (this.hasOldPageLayout) {
            var22 = this.job.validatePageLayout(this.oldPageLayout);
            if (var22.equals(this.oldPageLayout)) {
               this.setPageLayout(this.oldPageLayout);
               this.hasOldPageLayout = false;
            } else {
               this.setPageLayout(var21);
            }
         } else {
            var22 = this.job.validatePageLayout(var20);
            if (!var22.equals(var20)) {
               this.hasOldPageLayout = true;
               this.oldPageLayout = var20;
               this.setPageLayout(var21);
            }
         }
      }

   }

   public final StringProperty jobNameProperty() {
      if (this.jobName == null) {
         this.jobName = new SimpleStringProperty(this, "jobName", "JavaFX Print Job") {
            public void set(String var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     var1 = "JavaFX Print Job";
                  }

                  super.set(var1);
               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("Jobname property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("Jobname property cannot be bound");
            }
         };
      }

      return this.jobName;
   }

   public String getJobName() {
      return (String)this.jobNameProperty().get();
   }

   public void setJobName(String var1) {
      this.jobNameProperty().set(var1);
   }

   public final IntegerProperty copiesProperty() {
      if (this.copies == null) {
         this.copies = new SimpleIntegerProperty(this, "copies", this.printerCaps.getDefaultCopies()) {
            public void set(int var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 <= 0) {
                     if (!JobSettings.this.defaultCopies) {
                        super.set(JobSettings.this.printerCaps.getDefaultCopies());
                        JobSettings.this.defaultCopies = true;
                     }
                  } else {
                     super.set(var1);
                     JobSettings.this.defaultCopies = false;
                  }
               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("Copies property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("Copies property cannot be bound");
            }
         };
      }

      return this.copies;
   }

   public int getCopies() {
      return this.copiesProperty().get();
   }

   public final void setCopies(int var1) {
      this.copiesProperty().set(var1);
   }

   public final ObjectProperty pageRangesProperty() {
      if (this.pageRanges == null) {
         this.pageRanges = new SimpleObjectProperty(this, "pageRanges", (Object)null) {
            public void set(Object var1) {
               try {
                  this.set((PageRange[])((PageRange[])var1));
               } catch (ClassCastException var3) {
               }
            }

            public void set(PageRange[] var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 != null && var1.length != 0 && var1[0] != null) {
                     int var2 = var1.length;
                     PageRange[] var3 = new PageRange[var2];
                     int var4 = 0;
                     int var5 = 0;

                     while(true) {
                        if (var5 >= var2) {
                           var1 = var3;
                           break;
                        }

                        PageRange var6 = var1[var5];
                        if (var6 == null || var4 >= var6.getStartPage()) {
                           return;
                        }

                        var4 = var6.getEndPage();
                        var3[var5] = var6;
                        ++var5;
                     }
                  } else {
                     var1 = null;
                  }

                  super.set(var1);
               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PageRanges property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PageRanges property cannot be bound");
            }
         };
      }

      return this.pageRanges;
   }

   public PageRange[] getPageRanges() {
      return (PageRange[])((PageRange[])this.pageRangesProperty().get());
   }

   public void setPageRanges(PageRange... var1) {
      this.pageRangesProperty().set((PageRange[])var1);
   }

   public final ObjectProperty printSidesProperty() {
      if (this.sides == null) {
         this.sides = new SimpleObjectProperty(this, "printSides", this.printerCaps.getDefaultPrintSides()) {
            public void set(PrintSides var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (JobSettings.this.defaultSides) {
                        return;
                     }

                     super.set(JobSettings.this.printerCaps.getDefaultPrintSides());
                     JobSettings.this.defaultSides = true;
                  }

                  if (JobSettings.this.printerCaps.getSupportedPrintSides().contains(var1)) {
                     super.set(var1);
                     JobSettings.this.defaultSides = false;
                  }

               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PrintSides property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PrintSides property cannot be bound");
            }
         };
      }

      return this.sides;
   }

   public PrintSides getPrintSides() {
      return (PrintSides)this.printSidesProperty().get();
   }

   public void setPrintSides(PrintSides var1) {
      if (var1 != this.getPrintSides()) {
         this.printSidesProperty().set(var1);
      }
   }

   public final ObjectProperty collationProperty() {
      if (this.collation == null) {
         Collation var1 = this.printerCaps.getDefaultCollation();
         this.collation = new SimpleObjectProperty(this, "collation", var1) {
            public void set(Collation var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (!JobSettings.this.defaultCollation) {
                        super.set(JobSettings.this.printerCaps.getDefaultCollation());
                        JobSettings.this.defaultCollation = true;
                     }
                  } else {
                     if (JobSettings.this.printerCaps.getSupportedCollations().contains(var1)) {
                        super.set(var1);
                        JobSettings.this.defaultCollation = false;
                     }

                  }
               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("Collation property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("Collation property cannot be bound");
            }
         };
      }

      return this.collation;
   }

   public Collation getCollation() {
      return (Collation)this.collationProperty().get();
   }

   public void setCollation(Collation var1) {
      if (var1 != this.getCollation()) {
         this.collationProperty().set(var1);
      }
   }

   public final ObjectProperty printColorProperty() {
      if (this.color == null) {
         this.color = new SimpleObjectProperty(this, "printColor", this.printerCaps.getDefaultPrintColor()) {
            public void set(PrintColor var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (JobSettings.this.defaultPrintColor) {
                        return;
                     }

                     super.set(JobSettings.this.printerCaps.getDefaultPrintColor());
                     JobSettings.this.defaultPrintColor = true;
                  }

                  if (JobSettings.this.printerCaps.getSupportedPrintColors().contains(var1)) {
                     super.set(var1);
                     JobSettings.this.defaultPrintColor = false;
                  }

               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PrintColor property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PrintColor property cannot be bound");
            }
         };
      }

      return this.color;
   }

   public PrintColor getPrintColor() {
      return (PrintColor)this.printColorProperty().get();
   }

   public void setPrintColor(PrintColor var1) {
      if (var1 != this.getPrintColor()) {
         this.printColorProperty().set(var1);
      }
   }

   public final ObjectProperty printQualityProperty() {
      if (this.quality == null) {
         this.quality = new SimpleObjectProperty(this, "printQuality", this.printerCaps.getDefaultPrintQuality()) {
            public void set(PrintQuality var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (JobSettings.this.defaultPrintQuality) {
                        return;
                     }

                     super.set(JobSettings.this.printerCaps.getDefaultPrintQuality());
                     JobSettings.this.defaultPrintQuality = true;
                  }

                  if (JobSettings.this.printerCaps.getSupportedPrintQuality().contains(var1)) {
                     super.set(var1);
                     JobSettings.this.defaultPrintQuality = false;
                  }

               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PrintQuality property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PrintQuality property cannot be bound");
            }
         };
      }

      return this.quality;
   }

   public PrintQuality getPrintQuality() {
      return (PrintQuality)this.printQualityProperty().get();
   }

   public void setPrintQuality(PrintQuality var1) {
      if (var1 != this.getPrintQuality()) {
         this.printQualityProperty().set(var1);
      }
   }

   public final ObjectProperty printResolutionProperty() {
      if (this.resolution == null) {
         this.resolution = new SimpleObjectProperty(this, "printResolution", this.printerCaps.getDefaultPrintResolution()) {
            public void set(PrintResolution var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (JobSettings.this.defaultPrintResolution) {
                        return;
                     }

                     super.set(JobSettings.this.printerCaps.getDefaultPrintResolution());
                     JobSettings.this.defaultPrintResolution = true;
                  }

                  if (JobSettings.this.printerCaps.getSupportedPrintResolutions().contains(var1)) {
                     super.set(var1);
                     JobSettings.this.defaultPrintResolution = false;
                  }

               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PrintResolution property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PrintResolution property cannot be bound");
            }
         };
      }

      return this.resolution;
   }

   public PrintResolution getPrintResolution() {
      return (PrintResolution)this.printResolutionProperty().get();
   }

   public void setPrintResolution(PrintResolution var1) {
      if (var1 != null && var1 != this.getPrintResolution()) {
         this.printResolutionProperty().set(var1);
      }
   }

   public final ObjectProperty paperSourceProperty() {
      if (this.paperSource == null) {
         this.paperSource = new SimpleObjectProperty(this, "paperSource", this.printerCaps.getDefaultPaperSource()) {
            public void set(PaperSource var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 == null) {
                     if (JobSettings.this.defaultPaperSource) {
                        return;
                     }

                     super.set(JobSettings.this.printerCaps.getDefaultPaperSource());
                     JobSettings.this.defaultPaperSource = true;
                  }

                  if (JobSettings.this.printerCaps.getSupportedPaperSources().contains(var1)) {
                     super.set(var1);
                     JobSettings.this.defaultPaperSource = false;
                  }

               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PaperSource property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PaperSource property cannot be bound");
            }
         };
      }

      return this.paperSource;
   }

   public PaperSource getPaperSource() {
      return (PaperSource)this.paperSourceProperty().get();
   }

   public void setPaperSource(PaperSource var1) {
      this.paperSourceProperty().set(var1);
   }

   public final ObjectProperty pageLayoutProperty() {
      if (this.layout == null) {
         this.layout = new SimpleObjectProperty(this, "pageLayout", this.printer.getDefaultPageLayout()) {
            public void set(PageLayout var1) {
               if (JobSettings.this.isJobNew()) {
                  if (var1 != null) {
                     JobSettings.this.defaultPageLayout = false;
                     super.set(var1);
                  }
               }
            }

            public void bind(ObservableValue var1) {
               throw new RuntimeException("PageLayout property cannot be bound");
            }

            public void bindBidirectional(Property var1) {
               throw new RuntimeException("PageLayout property cannot be bound");
            }
         };
      }

      return this.layout;
   }

   public PageLayout getPageLayout() {
      return (PageLayout)this.pageLayoutProperty().get();
   }

   public void setPageLayout(PageLayout var1) {
      this.pageLayoutProperty().set(var1);
   }

   public String toString() {
      String var1 = System.lineSeparator();
      return " Collation = " + this.getCollation() + var1 + " Copies = " + this.getCopies() + var1 + " Sides = " + this.getPrintSides() + var1 + " JobName = " + this.getJobName() + var1 + " Page ranges = " + this.getPageRanges() + var1 + " Print color = " + this.getPrintColor() + var1 + " Print quality = " + this.getPrintQuality() + var1 + " Print resolution = " + this.getPrintResolution() + var1 + " Paper source = " + this.getPaperSource() + var1 + " Page layout = " + this.getPageLayout();
   }
}
