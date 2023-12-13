package com.sun.javafx.scene.text;

public class HitInfo {
   private int charIndex;
   private boolean leading;

   public int getCharIndex() {
      return this.charIndex;
   }

   public void setCharIndex(int var1) {
      this.charIndex = var1;
   }

   public boolean isLeading() {
      return this.leading;
   }

   public void setLeading(boolean var1) {
      this.leading = var1;
   }

   public int getInsertionIndex() {
      return this.leading ? this.charIndex : this.charIndex + 1;
   }

   public String toString() {
      return "charIndex: " + this.charIndex + ", isLeading: " + this.leading;
   }
}
