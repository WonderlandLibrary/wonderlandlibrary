package com.sun.javafx.fxml;

public class PropertyNotFoundException extends RuntimeException {
   private static final long serialVersionUID = 0L;

   public PropertyNotFoundException() {
   }

   public PropertyNotFoundException(String var1) {
      super(var1);
   }

   public PropertyNotFoundException(Throwable var1) {
      super(var1);
   }

   public PropertyNotFoundException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
