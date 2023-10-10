package org.alphacentauri.management.sexyness.color;

import org.alphacentauri.management.sexyness.color.ColorProvider;

public class SmoothColorProvider implements ColorProvider {
   private String[] hex;
   private int r;
   private int g;
   private int b;
   private int seq;
   private int speed;
   private int current;

   public SmoothColorProvider() {
      this(1);
   }

   public SmoothColorProvider(int speed) {
      this.hex = new String[]{"00", "14", "28", "3C", "50", "64", "78", "8C", "A0", "B4", "C8", "DC", "F0"};
      this.r = 1;
      this.g = 1;
      this.b = 1;
      this.seq = 1;
      this.current = 0;
      this.speed = speed;
   }

   public void update() {
      if(this.current < this.speed) {
         ++this.current;
      } else {
         this.current = 0;
         if(this.seq == 6) {
            --this.b;
            if(this.b == 0) {
               this.seq = 1;
            }
         }

         if(this.seq == 5) {
            ++this.r;
            if(this.r == 12) {
               this.seq = 6;
            }
         }

         if(this.seq == 4) {
            --this.g;
            if(this.g == 0) {
               this.seq = 5;
            }
         }

         if(this.seq == 3) {
            ++this.b;
            if(this.b == 12) {
               this.seq = 4;
            }
         }

         if(this.seq == 2) {
            --this.r;
            if(this.r == 0) {
               this.seq = 3;
            }
         }

         if(this.seq == 1) {
            ++this.g;
            if(this.g == 12) {
               this.seq = 2;
            }
         }

      }
   }

   public int getColor() {
      return Integer.parseInt(this.hex[this.r] + this.hex[this.g] + this.hex[this.b], 16);
   }

   public int getG() {
      return Integer.parseInt(this.hex[this.g], 16);
   }

   public int getR() {
      return Integer.parseInt(this.hex[this.r], 16);
   }

   public int getB() {
      return Integer.parseInt(this.hex[this.b], 16);
   }

   public void setSpeed(int speed) {
      this.speed = speed;
   }

   public int getSpeed() {
      return this.speed;
   }
}
