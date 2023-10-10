package javassist;

import javassist.compiler.CompileError;

public class CannotCompileException extends Exception {
   private Throwable myCause;
   private String message;

   public Throwable getCause() {
      return this.myCause == this ? null : this.myCause;
   }

   public synchronized Throwable initCause(Throwable var1) {
      this.myCause = var1;
      return this;
   }

   public String getReason() {
      return this.message != null ? this.message : this.toString();
   }

   public CannotCompileException(String var1) {
      super(var1);
      this.message = var1;
      this.initCause((Throwable)null);
   }

   public CannotCompileException(Throwable var1) {
      super("by " + var1.toString());
      this.message = null;
      this.initCause(var1);
   }

   public CannotCompileException(String var1, Throwable var2) {
      this(var1);
      this.initCause(var2);
   }

   public CannotCompileException(NotFoundException var1) {
      this((String)("cannot find " + var1.getMessage()), (Throwable)var1);
   }

   public CannotCompileException(CompileError var1) {
      this((String)("[source error] " + var1.getMessage()), (Throwable)var1);
   }

   public CannotCompileException(ClassNotFoundException var1, String var2) {
      this((String)("cannot find " + var2), (Throwable)var1);
   }

   public CannotCompileException(ClassFormatError var1, String var2) {
      this((String)("invalid class format: " + var2), (Throwable)var1);
   }
}
