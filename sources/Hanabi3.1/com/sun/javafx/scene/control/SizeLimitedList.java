package com.sun.javafx.scene.control;

import java.util.LinkedList;
import java.util.List;

public class SizeLimitedList {
   private final int maxSize;
   private final List backingList;

   public SizeLimitedList(int var1) {
      this.maxSize = var1;
      this.backingList = new LinkedList();
   }

   public Object get(int var1) {
      return this.backingList.get(var1);
   }

   public void add(Object var1) {
      this.backingList.add(0, var1);
      if (this.backingList.size() > this.maxSize) {
         this.backingList.remove(this.maxSize);
      }

   }

   public int size() {
      return this.backingList.size();
   }

   public boolean contains(Object var1) {
      return this.backingList.contains(var1);
   }
}
