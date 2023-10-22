package com.sun.javafx.jmx;

public class MXNodeAlgorithmContext {
   private int counter;

   public MXNodeAlgorithmContext() {
      this(0);
   }

   public MXNodeAlgorithmContext(int var1) {
      this.counter = var1;
   }

   public int getNextInt() {
      return ++this.counter;
   }
}
