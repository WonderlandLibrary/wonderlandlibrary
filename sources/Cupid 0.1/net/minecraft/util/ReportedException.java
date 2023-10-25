package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
  private final CrashReport theReportedExceptionCrashReport;
  
  public ReportedException(CrashReport report) {
    this.theReportedExceptionCrashReport = report;
  }
  
  public CrashReport getCrashReport() {
    return this.theReportedExceptionCrashReport;
  }
  
  public Throwable getCause() {
    return this.theReportedExceptionCrashReport.getCrashCause();
  }
  
  public String getMessage() {
    return this.theReportedExceptionCrashReport.getDescription();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\ReportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */