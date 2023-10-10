package org.apache.commons.lang3;

public class NotImplementedException extends UnsupportedOperationException {
   private static final long serialVersionUID = 20131021L;
   private final String code;

   public NotImplementedException(String var1) {
      this(var1, (String)null);
   }

   public NotImplementedException(Throwable var1) {
      this((Throwable)var1, (String)null);
   }

   public NotImplementedException(String var1, Throwable var2) {
      this(var1, var2, (String)null);
   }

   public NotImplementedException(String var1, String var2) {
      super(var1);
      this.code = var2;
   }

   public NotImplementedException(Throwable var1, String var2) {
      super(var1);
      this.code = var2;
   }

   public NotImplementedException(String var1, Throwable var2, String var3) {
      super(var1, var2);
      this.code = var3;
   }

   public String getCode() {
      return this.code;
   }
}
