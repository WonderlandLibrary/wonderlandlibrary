package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class ContextedException extends Exception implements ExceptionContext {
  private static final long serialVersionUID = 20110706L;
  
  private final ExceptionContext exceptionContext;
  
  public ContextedException() {
    this.exceptionContext = new DefaultExceptionContext();
  }
  
  public ContextedException(String message) {
    super(message);
    this.exceptionContext = new DefaultExceptionContext();
  }
  
  public ContextedException(Throwable cause) {
    super(cause);
    this.exceptionContext = new DefaultExceptionContext();
  }
  
  public ContextedException(String message, Throwable cause) {
    super(message, cause);
    this.exceptionContext = new DefaultExceptionContext();
  }
  
  public ContextedException(String message, Throwable cause, ExceptionContext context) {
    super(message, cause);
    if (context == null)
      context = new DefaultExceptionContext(); 
    this.exceptionContext = context;
  }
  
  public ContextedException addContextValue(String label, Object value) {
    this.exceptionContext.addContextValue(label, value);
    return this;
  }
  
  public ContextedException setContextValue(String label, Object value) {
    this.exceptionContext.setContextValue(label, value);
    return this;
  }
  
  public List<Object> getContextValues(String label) {
    return this.exceptionContext.getContextValues(label);
  }
  
  public Object getFirstContextValue(String label) {
    return this.exceptionContext.getFirstContextValue(label);
  }
  
  public List<Pair<String, Object>> getContextEntries() {
    return this.exceptionContext.getContextEntries();
  }
  
  public Set<String> getContextLabels() {
    return this.exceptionContext.getContextLabels();
  }
  
  public String getMessage() {
    return getFormattedExceptionMessage(super.getMessage());
  }
  
  public String getRawMessage() {
    return super.getMessage();
  }
  
  public String getFormattedExceptionMessage(String baseMessage) {
    return this.exceptionContext.getFormattedExceptionMessage(baseMessage);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\exception\ContextedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */