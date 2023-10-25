package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public interface ExceptionContext {
  ExceptionContext addContextValue(String paramString, Object paramObject);
  
  ExceptionContext setContextValue(String paramString, Object paramObject);
  
  List<Object> getContextValues(String paramString);
  
  Object getFirstContextValue(String paramString);
  
  Set<String> getContextLabels();
  
  List<Pair<String, Object>> getContextEntries();
  
  String getFormattedExceptionMessage(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\exception\ExceptionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */