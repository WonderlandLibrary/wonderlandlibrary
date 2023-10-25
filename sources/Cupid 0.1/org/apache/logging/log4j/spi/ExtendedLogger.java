package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public interface ExtendedLogger extends Logger {
  boolean isEnabled(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Throwable paramThrowable);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object... paramVarArgs);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void logMessage(String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Supplier<?>... paramVarArgs);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\ExtendedLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */