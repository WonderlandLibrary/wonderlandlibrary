package org.apache.logging.log4j.spi;

import java.util.EnumSet;

public enum StandardLevel {
  OFF(0),
  FATAL(100),
  ERROR(200),
  WARN(300),
  INFO(400),
  DEBUG(500),
  TRACE(600),
  ALL(2147483647);
  
  private static final EnumSet<StandardLevel> LEVELSET;
  
  private final int intLevel;
  
  static {
    LEVELSET = EnumSet.allOf(StandardLevel.class);
  }
  
  StandardLevel(int val) {
    this.intLevel = val;
  }
  
  public int intLevel() {
    return this.intLevel;
  }
  
  public static StandardLevel getStandardLevel(int intLevel) {
    StandardLevel level = OFF;
    for (StandardLevel lvl : LEVELSET) {
      if (lvl.intLevel() > intLevel)
        break; 
      level = lvl;
    } 
    return level;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\StandardLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */