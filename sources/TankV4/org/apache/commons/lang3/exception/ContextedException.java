package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;

public class ContextedException extends Exception implements ExceptionContext {
   private static final long serialVersionUID = 20110706L;
   private final ExceptionContext exceptionContext;

   public ContextedException() {
      this.exceptionContext = new DefaultExceptionContext();
   }

   public ContextedException(String var1) {
      super(var1);
      this.exceptionContext = new DefaultExceptionContext();
   }

   public ContextedException(Throwable var1) {
      super(var1);
      this.exceptionContext = new DefaultExceptionContext();
   }

   public ContextedException(String var1, Throwable var2) {
      super(var1, var2);
      this.exceptionContext = new DefaultExceptionContext();
   }

   public ContextedException(String var1, Throwable var2, ExceptionContext var3) {
      super(var1, var2);
      if (var3 == null) {
         var3 = new DefaultExceptionContext();
      }

      this.exceptionContext = (ExceptionContext)var3;
   }

   public ContextedException addContextValue(String var1, Object var2) {
      this.exceptionContext.addContextValue(var1, var2);
      return this;
   }

   public ContextedException setContextValue(String var1, Object var2) {
      this.exceptionContext.setContextValue(var1, var2);
      return this;
   }

   public List getContextValues(String var1) {
      return this.exceptionContext.getContextValues(var1);
   }

   public Object getFirstContextValue(String var1) {
      return this.exceptionContext.getFirstContextValue(var1);
   }

   public List getContextEntries() {
      return this.exceptionContext.getContextEntries();
   }

   public Set getContextLabels() {
      return this.exceptionContext.getContextLabels();
   }

   public String getMessage() {
      return this.getFormattedExceptionMessage(super.getMessage());
   }

   public String getRawMessage() {
      return super.getMessage();
   }

   public String getFormattedExceptionMessage(String var1) {
      return this.exceptionContext.getFormattedExceptionMessage(var1);
   }

   public ExceptionContext setContextValue(String var1, Object var2) {
      return this.setContextValue(var1, var2);
   }

   public ExceptionContext addContextValue(String var1, Object var2) {
      return this.addContextValue(var1, var2);
   }
}
