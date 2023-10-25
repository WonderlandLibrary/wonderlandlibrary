package org.apache.logging.log4j.internal;

public class LogManagerStatus {
  private static boolean initialized = false;
  
  public static void setInitialized(boolean managerStatus) {
    initialized = managerStatus;
  }
  
  public static boolean isInitialized() {
    return initialized;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\internal\LogManagerStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */