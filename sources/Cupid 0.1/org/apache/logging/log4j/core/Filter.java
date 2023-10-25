package org.apache.logging.log4j.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.EnglishEnums;

public interface Filter extends LifeCycle {
  public static final Filter[] EMPTY_ARRAY = new Filter[0];
  
  public static final String ELEMENT_TYPE = "filter";
  
  Result getOnMismatch();
  
  Result getOnMatch();
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  Result filter(LogEvent paramLogEvent);
  
  public enum Result {
    ACCEPT, NEUTRAL, DENY;
    
    public static Result toResult(String name) {
      return toResult(name, null);
    }
    
    public static Result toResult(String name, Result defaultResult) {
      return (Result)EnglishEnums.valueOf(Result.class, name, defaultResult);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\Filter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */