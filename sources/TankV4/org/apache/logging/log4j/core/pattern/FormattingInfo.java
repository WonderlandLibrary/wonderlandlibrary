package org.apache.logging.log4j.core.pattern;

public final class FormattingInfo {
   private static final char[] SPACES = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
   private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, Integer.MAX_VALUE);
   private final int minLength;
   private final int maxLength;
   private final boolean leftAlign;

   public FormattingInfo(boolean var1, int var2, int var3) {
      this.leftAlign = var1;
      this.minLength = var2;
      this.maxLength = var3;
   }

   public static FormattingInfo getDefault() {
      return DEFAULT;
   }

   public boolean isLeftAligned() {
      return this.leftAlign;
   }

   public int getMinLength() {
      return this.minLength;
   }

   public int getMaxLength() {
      return this.maxLength;
   }

   public void format(int var1, StringBuilder var2) {
      int var3 = var2.length() - var1;
      if (var3 > this.maxLength) {
         var2.delete(var1, var2.length() - this.maxLength);
      } else if (var3 < this.minLength) {
         int var4;
         if (this.leftAlign) {
            var4 = var2.length();
            var2.setLength(var1 + this.minLength);

            for(int var5 = var4; var5 < var2.length(); ++var5) {
               var2.setCharAt(var5, ' ');
            }
         } else {
            for(var4 = this.minLength - var3; var4 > SPACES.length; var4 -= SPACES.length) {
               var2.insert(var1, SPACES);
            }

            var2.insert(var1, SPACES, 0, var4);
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("[leftAlign=");
      var1.append(this.leftAlign);
      var1.append(", maxLength=");
      var1.append(this.maxLength);
      var1.append(", minLength=");
      var1.append(this.minLength);
      var1.append("]");
      return var1.toString();
   }
}
