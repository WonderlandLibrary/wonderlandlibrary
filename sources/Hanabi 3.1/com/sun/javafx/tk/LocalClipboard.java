package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.util.Pair;

final class LocalClipboard implements TKClipboard {
   private final Map values = new HashMap();

   public LocalClipboard() {
   }

   public void setSecurityContext(AccessControlContext var1) {
   }

   public Set getContentTypes() {
      return Collections.unmodifiableSet(new HashSet(this.values.keySet()));
   }

   public boolean putContent(Pair... var1) {
      Pair[] var2 = var1;
      int var3 = var1.length;

      int var4;
      Pair var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var5.getKey() == null) {
            throw new NullPointerException("Clipboard.putContent: null data format");
         }

         if (var5.getValue() == null) {
            throw new NullPointerException("Clipboard.putContent: null data");
         }
      }

      this.values.clear();
      var2 = var1;
      var3 = var1.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         this.values.put(var5.getKey(), var5.getValue());
      }

      return true;
   }

   public Object getContent(DataFormat var1) {
      return this.values.get(var1);
   }

   public boolean hasContent(DataFormat var1) {
      return this.values.containsKey(var1);
   }

   public Set getTransferModes() {
      throw new IllegalStateException();
   }

   public void setDragView(Image var1) {
      throw new IllegalStateException();
   }

   public void setDragViewOffsetX(double var1) {
      throw new IllegalStateException();
   }

   public void setDragViewOffsetY(double var1) {
      throw new IllegalStateException();
   }

   public Image getDragView() {
      throw new IllegalStateException();
   }

   public double getDragViewOffsetX() {
      throw new IllegalStateException();
   }

   public double getDragViewOffsetY() {
      throw new IllegalStateException();
   }
}
