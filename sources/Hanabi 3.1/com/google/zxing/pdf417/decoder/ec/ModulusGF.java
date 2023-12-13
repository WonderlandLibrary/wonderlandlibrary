package com.google.zxing.pdf417.decoder.ec;

public final class ModulusGF {
   public static final ModulusGF PDF417_GF = new ModulusGF(929, 3);
   private final int[] expTable;
   private final int[] logTable;
   private final ModulusPoly zero;
   private final ModulusPoly one;
   private final int modulus;

   public ModulusGF(int modulus, int generator) {
      this.modulus = modulus;
      this.expTable = new int[modulus];
      this.logTable = new int[modulus];
      int x = 1;

      int i;
      for(i = 0; i < modulus; ++i) {
         this.expTable[i] = x;
         x = x * generator % modulus;
      }

      for(i = 0; i < modulus - 1; this.logTable[this.expTable[i]] = i++) {
      }

      this.zero = new ModulusPoly(this, new int[]{0});
      this.one = new ModulusPoly(this, new int[]{1});
   }

   ModulusPoly getZero() {
      return this.zero;
   }

   ModulusPoly getOne() {
      return this.one;
   }

   ModulusPoly buildMonomial(int degree, int coefficient) {
      if (degree < 0) {
         throw new IllegalArgumentException();
      } else if (coefficient == 0) {
         return this.zero;
      } else {
         int[] coefficients = new int[degree + 1];
         coefficients[0] = coefficient;
         return new ModulusPoly(this, coefficients);
      }
   }

   int add(int a, int b) {
      return (a + b) % this.modulus;
   }

   int subtract(int a, int b) {
      return (this.modulus + a - b) % this.modulus;
   }

   int exp(int a) {
      return this.expTable[a];
   }

   int log(int a) {
      if (a == 0) {
         throw new IllegalArgumentException();
      } else {
         return this.logTable[a];
      }
   }

   int inverse(int a) {
      if (a == 0) {
         throw new ArithmeticException();
      } else {
         return this.expTable[this.modulus - this.logTable[a] - 1];
      }
   }

   int multiply(int a, int b) {
      return a != 0 && b != 0 ? this.expTable[(this.logTable[a] + this.logTable[b]) % (this.modulus - 1)] : 0;
   }

   int getSize() {
      return this.modulus;
   }
}
