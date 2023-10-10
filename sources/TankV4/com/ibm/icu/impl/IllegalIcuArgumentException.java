package com.ibm.icu.impl;

public class IllegalIcuArgumentException extends IllegalArgumentException {
   private static final long serialVersionUID = 3789261542830211225L;

   public IllegalIcuArgumentException(String var1) {
      super(var1);
   }

   public IllegalIcuArgumentException(Throwable var1) {
      super(var1);
   }

   public IllegalIcuArgumentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public synchronized IllegalIcuArgumentException initCause(Throwable var1) {
      return (IllegalIcuArgumentException)super.initCause(var1);
   }

   public Throwable initCause(Throwable var1) {
      return this.initCause(var1);
   }
}
