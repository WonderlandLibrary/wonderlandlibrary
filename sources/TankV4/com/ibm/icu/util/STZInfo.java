package com.ibm.icu.util;

import java.io.Serializable;

final class STZInfo implements Serializable {
   private static final long serialVersionUID = -7849612037842370168L;
   int sy = -1;
   int sm = -1;
   int sdwm;
   int sdw;
   int st;
   int sdm;
   boolean sa;
   int em = -1;
   int edwm;
   int edw;
   int et;
   int edm;
   boolean ea;

   void setStart(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.sm = var1;
      this.sdwm = var2;
      this.sdw = var3;
      this.st = var4;
      this.sdm = var5;
      this.sa = var6;
   }

   void setEnd(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.em = var1;
      this.edwm = var2;
      this.edw = var3;
      this.et = var4;
      this.edm = var5;
      this.ea = var6;
   }

   void applyTo(SimpleTimeZone var1) {
      if (this.sy != -1) {
         var1.setStartYear(this.sy);
      }

      if (this.sm != -1) {
         if (this.sdm == -1) {
            var1.setStartRule(this.sm, this.sdwm, this.sdw, this.st);
         } else if (this.sdw == -1) {
            var1.setStartRule(this.sm, this.sdm, this.st);
         } else {
            var1.setStartRule(this.sm, this.sdm, this.sdw, this.st, this.sa);
         }
      }

      if (this.em != -1) {
         if (this.edm == -1) {
            var1.setEndRule(this.em, this.edwm, this.edw, this.et);
         } else if (this.edw == -1) {
            var1.setEndRule(this.em, this.edm, this.et);
         } else {
            var1.setEndRule(this.em, this.edm, this.edw, this.et, this.ea);
         }
      }

   }
}
