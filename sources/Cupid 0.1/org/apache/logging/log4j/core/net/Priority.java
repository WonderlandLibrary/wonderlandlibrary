package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public class Priority {
  private final Facility facility;
  
  private final Severity severity;
  
  public Priority(Facility facility, Severity severity) {
    this.facility = facility;
    this.severity = severity;
  }
  
  public static int getPriority(Facility facility, Level level) {
    return toPriority(facility, Severity.getSeverity(level));
  }
  
  private static int toPriority(Facility aFacility, Severity aSeverity) {
    return (aFacility.getCode() << 3) + aSeverity.getCode();
  }
  
  public Facility getFacility() {
    return this.facility;
  }
  
  public Severity getSeverity() {
    return this.severity;
  }
  
  public int getValue() {
    return toPriority(this.facility, this.severity);
  }
  
  public String toString() {
    return Integer.toString(getValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\Priority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */