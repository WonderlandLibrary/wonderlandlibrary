package com.ibm.icu.text;

public class BidiClassifier {
   protected Object context;

   public BidiClassifier(Object var1) {
      this.context = var1;
   }

   public void setContext(Object var1) {
      this.context = var1;
   }

   public Object getContext() {
      return this.context;
   }

   public int classify(int var1) {
      return 19;
   }
}
