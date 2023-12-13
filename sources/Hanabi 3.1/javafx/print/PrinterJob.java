package javafx.print;

import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.tk.PrintPipeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.stage.Window;

public final class PrinterJob {
   private PrinterJobImpl jobImpl;
   private ObjectProperty printer;
   private JobSettings settings;
   private ReadOnlyObjectWrapper jobStatus;

   public static final PrinterJob createPrinterJob() {
      SecurityManager var0 = System.getSecurityManager();
      if (var0 != null) {
         var0.checkPrintJobAccess();
      }

      Printer var1 = Printer.getDefaultPrinter();
      return var1 == null ? null : new PrinterJob(var1);
   }

   public static final PrinterJob createPrinterJob(Printer var0) {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPrintJobAccess();
      }

      return new PrinterJob(var0);
   }

   private PrinterJob(Printer var1) {
      this.jobStatus = new ReadOnlyObjectWrapper(PrinterJob.JobStatus.NOT_STARTED);
      this.printer = this.createPrinterProperty(var1);
      this.settings = var1.getDefaultJobSettings();
      this.settings.setPrinterJob(this);
      this.createImplJob(var1, this.settings);
   }

   private synchronized PrinterJobImpl createImplJob(Printer var1, JobSettings var2) {
      if (this.jobImpl == null) {
         this.jobImpl = PrintPipeline.getPrintPipeline().createPrinterJob(this);
      }

      return this.jobImpl;
   }

   boolean isJobNew() {
      return this.getJobStatus() == PrinterJob.JobStatus.NOT_STARTED;
   }

   private ObjectProperty createPrinterProperty(Printer var1) {
      return new SimpleObjectProperty(var1) {
         public void set(Printer var1) {
            if (var1 != this.get() && PrinterJob.this.isJobNew()) {
               if (var1 == null) {
                  var1 = Printer.getDefaultPrinter();
               }

               super.set(var1);
               PrinterJob.this.jobImpl.setPrinterImpl(var1.getPrinterImpl());
               PrinterJob.this.settings.updateForPrinter(var1);
            }
         }

         public void bind(ObservableValue var1) {
            throw new RuntimeException("Printer property cannot be bound");
         }

         public void bindBidirectional(Property var1) {
            throw new RuntimeException("Printer property cannot be bound");
         }

         public Object getBean() {
            return PrinterJob.this;
         }

         public String getName() {
            return "printer";
         }
      };
   }

   public final ObjectProperty printerProperty() {
      return this.printer;
   }

   public synchronized Printer getPrinter() {
      return (Printer)this.printerProperty().get();
   }

   public synchronized void setPrinter(Printer var1) {
      this.printerProperty().set(var1);
   }

   public synchronized JobSettings getJobSettings() {
      return this.settings;
   }

   public synchronized boolean showPrintDialog(Window var1) {
      return !this.isJobNew() ? false : this.jobImpl.showPrintDialog(var1);
   }

   public synchronized boolean showPageSetupDialog(Window var1) {
      return !this.isJobNew() ? false : this.jobImpl.showPageDialog(var1);
   }

   synchronized PageLayout validatePageLayout(PageLayout var1) {
      if (var1 == null) {
         throw new NullPointerException("pageLayout cannot be null");
      } else {
         return this.jobImpl.validatePageLayout(var1);
      }
   }

   public synchronized boolean printPage(PageLayout var1, Node var2) {
      if (((JobStatus)this.jobStatus.get()).ordinal() > PrinterJob.JobStatus.PRINTING.ordinal()) {
         return false;
      } else {
         if (this.jobStatus.get() == PrinterJob.JobStatus.NOT_STARTED) {
            this.jobStatus.set(PrinterJob.JobStatus.PRINTING);
         }

         if (var1 != null && var2 != null) {
            boolean var3 = this.jobImpl.print(var1, var2);
            if (!var3) {
               this.jobStatus.set(PrinterJob.JobStatus.ERROR);
            }

            return var3;
         } else {
            this.jobStatus.set(PrinterJob.JobStatus.ERROR);
            throw new NullPointerException("Parameters cannot be null");
         }
      }
   }

   public synchronized boolean printPage(Node var1) {
      return this.printPage(this.settings.getPageLayout(), var1);
   }

   public ReadOnlyObjectProperty jobStatusProperty() {
      return this.jobStatus.getReadOnlyProperty();
   }

   public JobStatus getJobStatus() {
      return (JobStatus)this.jobStatus.get();
   }

   public void cancelJob() {
      if (((JobStatus)this.jobStatus.get()).ordinal() <= PrinterJob.JobStatus.PRINTING.ordinal()) {
         this.jobStatus.set(PrinterJob.JobStatus.CANCELED);
         this.jobImpl.cancelJob();
      }

   }

   public synchronized boolean endJob() {
      if (this.jobStatus.get() == PrinterJob.JobStatus.NOT_STARTED) {
         this.cancelJob();
         return false;
      } else if (this.jobStatus.get() == PrinterJob.JobStatus.PRINTING) {
         boolean var1 = this.jobImpl.endJob();
         this.jobStatus.set(var1 ? PrinterJob.JobStatus.DONE : PrinterJob.JobStatus.ERROR);
         return var1;
      } else {
         return false;
      }
   }

   public String toString() {
      return "JavaFX PrinterJob " + this.getPrinter() + "\n" + this.getJobSettings() + "\nJob Status = " + this.getJobStatus();
   }

   public static enum JobStatus {
      NOT_STARTED,
      PRINTING,
      CANCELED,
      ERROR,
      DONE;
   }
}
