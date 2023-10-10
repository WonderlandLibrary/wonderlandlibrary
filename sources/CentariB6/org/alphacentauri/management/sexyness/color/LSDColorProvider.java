package org.alphacentauri.management.sexyness.color;

import java.util.Random;
import org.alphacentauri.management.sexyness.color.ColorProvider;

public class LSDColorProvider implements ColorProvider {
   public String r = "00";
   public String g = "00";
   public String b = "00";
   Random rdm = new Random();
   char[] hex = "0123456789ABCDEF".toCharArray();

   public void update() {
      this.r = this.rdmHeyByte();
      this.g = this.rdmHeyByte();
      this.b = this.rdmHeyByte();
   }

   public int getColor() {
      return Integer.parseInt(this.r + this.g + this.b, 16);
   }

   public int getG() {
      return Integer.valueOf(this.g, 16).intValue();
   }

   public int getR() {
      return Integer.valueOf(this.r, 16).intValue();
   }

   public int getB() {
      return Integer.valueOf(this.b, 16).intValue();
   }

   private String rdmHeyByte() {
      return String.valueOf(this.hex[this.rdm.nextInt(this.hex.length)]) + this.hex[this.rdm.nextInt(this.hex.length)];
   }
}
