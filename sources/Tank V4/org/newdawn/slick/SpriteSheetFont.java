package org.newdawn.slick;

import java.io.UnsupportedEncodingException;
import org.newdawn.slick.util.Log;

public class SpriteSheetFont implements Font {
   private SpriteSheet font;
   private char startingCharacter;
   private int charWidth;
   private int charHeight;
   private int horizontalCount;
   private int numChars;

   public SpriteSheetFont(SpriteSheet var1, char var2) {
      this.font = var1;
      this.startingCharacter = var2;
      this.horizontalCount = var1.getHorizontalCount();
      int var3 = var1.getVerticalCount();
      this.charWidth = var1.getWidth() / this.horizontalCount;
      this.charHeight = var1.getHeight() / var3;
      this.numChars = this.horizontalCount * var3;
   }

   public void drawString(float var1, float var2, String var3) {
      this.drawString(var1, var2, var3, Color.white);
   }

   public void drawString(float var1, float var2, String var3, Color var4) {
      this.drawString(var1, var2, var3, var4, 0, var3.length() - 1);
   }

   public void drawString(float var1, float var2, String var3, Color var4, int var5, int var6) {
      try {
         byte[] var7 = var3.getBytes("US-ASCII");

         for(int var8 = 0; var8 < var7.length; ++var8) {
            int var9 = var7[var8] - this.startingCharacter;
            if (var9 < this.numChars) {
               int var10 = var9 % this.horizontalCount;
               int var11 = var9 / this.horizontalCount;
               if (var8 >= var5 || var8 <= var6) {
                  this.font.getSprite(var10, var11).draw(var1 + (float)(var8 * this.charWidth), var2, var4);
               }
            }
         }
      } catch (UnsupportedEncodingException var12) {
         Log.error((Throwable)var12);
      }

   }

   public int getHeight(String var1) {
      return this.charHeight;
   }

   public int getWidth(String var1) {
      return this.charWidth * var1.length();
   }

   public int getLineHeight() {
      return this.charHeight;
   }
}
