package com.sun.javafx.tk.quantum;

import java.util.Collection;
import java.util.Vector;

class GestureRecognizers implements GestureRecognizer {
   private Collection recognizers = new Vector();
   private GestureRecognizer[] workList;

   void add(GestureRecognizer var1) {
      if (!this.contains(var1)) {
         this.recognizers.add(var1);
         this.workList = null;
      }

   }

   void remove(GestureRecognizer var1) {
      if (this.contains(var1)) {
         this.recognizers.remove(var1);
         this.workList = null;
      }

   }

   boolean contains(GestureRecognizer var1) {
      return this.recognizers.contains(var1);
   }

   private GestureRecognizer[] synchWorkList() {
      if (this.workList == null) {
         this.workList = (GestureRecognizer[])this.recognizers.toArray(new GestureRecognizer[0]);
      }

      return this.workList;
   }

   public void notifyBeginTouchEvent(long var1, int var3, boolean var4, int var5) {
      GestureRecognizer[] var6 = this.synchWorkList();

      for(int var7 = 0; var7 != var6.length; ++var7) {
         var6[var7].notifyBeginTouchEvent(var1, var3, var4, var5);
      }

   }

   public void notifyNextTouchEvent(long var1, int var3, long var4, int var6, int var7, int var8, int var9) {
      GestureRecognizer[] var10 = this.synchWorkList();

      for(int var11 = 0; var11 != var10.length; ++var11) {
         var10[var11].notifyNextTouchEvent(var1, var3, var4, var6, var7, var8, var9);
      }

   }

   public void notifyEndTouchEvent(long var1) {
      GestureRecognizer[] var3 = this.synchWorkList();

      for(int var4 = 0; var4 != var3.length; ++var4) {
         var3[var4].notifyEndTouchEvent(var1);
      }

   }
}
