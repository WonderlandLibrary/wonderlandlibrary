package com.google.zxing.pdf417.decoder.ec;

import com.google.zxing.ChecksumException;

public final class ErrorCorrection {
   private final ModulusGF field;

   public ErrorCorrection() {
      this.field = ModulusGF.PDF417_GF;
   }

   public void decode(int[] received, int numECCodewords, int[] erasures) throws ChecksumException {
      ModulusPoly poly = new ModulusPoly(this.field, received);
      int[] S = new int[numECCodewords];
      boolean error = false;

      for(int i = numECCodewords; i > 0; --i) {
         int eval = poly.evaluateAt(this.field.exp(i));
         S[numECCodewords - i] = eval;
         if (eval != 0) {
            error = true;
         }
      }

      if (error) {
         ModulusPoly knownErrors = this.field.getOne();
         int[] arr$ = erasures;
         int len$ = erasures.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int erasure = arr$[i$];
            int b = this.field.exp(received.length - 1 - erasure);
            ModulusPoly term = new ModulusPoly(this.field, new int[]{this.field.subtract(0, b), 1});
            knownErrors = knownErrors.multiply(term);
         }

         ModulusPoly syndrome = new ModulusPoly(this.field, S);
         ModulusPoly[] sigmaOmega = this.runEuclideanAlgorithm(this.field.buildMonomial(numECCodewords, 1), syndrome, numECCodewords);
         ModulusPoly sigma = sigmaOmega[0];
         ModulusPoly omega = sigmaOmega[1];
         int[] errorLocations = this.findErrorLocations(sigma);
         int[] errorMagnitudes = this.findErrorMagnitudes(omega, sigma, errorLocations);

         for(int i = 0; i < errorLocations.length; ++i) {
            int position = received.length - 1 - this.field.log(errorLocations[i]);
            if (position < 0) {
               throw ChecksumException.getChecksumInstance();
            }

            received[position] = this.field.subtract(received[position], errorMagnitudes[i]);
         }
      }

   }

   private ModulusPoly[] runEuclideanAlgorithm(ModulusPoly a, ModulusPoly b, int R) throws ChecksumException {
      ModulusPoly rLast;
      if (a.getDegree() < b.getDegree()) {
         rLast = a;
         a = b;
         b = rLast;
      }

      rLast = a;
      ModulusPoly r = b;
      ModulusPoly tLast = this.field.getZero();

      ModulusPoly t;
      ModulusPoly tLastLast;
      ModulusPoly q;
      for(t = this.field.getOne(); r.getDegree() >= R / 2; t = q.multiply(t).subtract(tLastLast).negative()) {
         ModulusPoly rLastLast = rLast;
         tLastLast = tLast;
         rLast = r;
         tLast = t;
         if (r.isZero()) {
            throw ChecksumException.getChecksumInstance();
         }

         r = rLastLast;
         q = this.field.getZero();
         int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());

         int degreeDiff;
         int scale;
         for(int dltInverse = this.field.inverse(denominatorLeadingTerm); r.getDegree() >= rLast.getDegree() && !r.isZero(); r = r.subtract(rLast.multiplyByMonomial(degreeDiff, scale))) {
            degreeDiff = r.getDegree() - rLast.getDegree();
            scale = this.field.multiply(r.getCoefficient(r.getDegree()), dltInverse);
            q = q.add(this.field.buildMonomial(degreeDiff, scale));
         }
      }

      int sigmaTildeAtZero = t.getCoefficient(0);
      if (sigmaTildeAtZero == 0) {
         throw ChecksumException.getChecksumInstance();
      } else {
         int inverse = this.field.inverse(sigmaTildeAtZero);
         q = t.multiply(inverse);
         ModulusPoly omega = r.multiply(inverse);
         return new ModulusPoly[]{q, omega};
      }
   }

   private int[] findErrorLocations(ModulusPoly errorLocator) throws ChecksumException {
      int numErrors = errorLocator.getDegree();
      int[] result = new int[numErrors];
      int e = 0;

      for(int i = 1; i < this.field.getSize() && e < numErrors; ++i) {
         if (errorLocator.evaluateAt(i) == 0) {
            result[e] = this.field.inverse(i);
            ++e;
         }
      }

      if (e != numErrors) {
         throw ChecksumException.getChecksumInstance();
      } else {
         return result;
      }
   }

   private int[] findErrorMagnitudes(ModulusPoly errorEvaluator, ModulusPoly errorLocator, int[] errorLocations) {
      int errorLocatorDegree = errorLocator.getDegree();
      int[] formalDerivativeCoefficients = new int[errorLocatorDegree];

      for(int i = 1; i <= errorLocatorDegree; ++i) {
         formalDerivativeCoefficients[errorLocatorDegree - i] = this.field.multiply(i, errorLocator.getCoefficient(i));
      }

      ModulusPoly formalDerivative = new ModulusPoly(this.field, formalDerivativeCoefficients);
      int s = errorLocations.length;
      int[] result = new int[s];

      for(int i = 0; i < s; ++i) {
         int xiInverse = this.field.inverse(errorLocations[i]);
         int numerator = this.field.subtract(0, errorEvaluator.evaluateAt(xiInverse));
         int denominator = this.field.inverse(formalDerivative.evaluateAt(xiInverse));
         result[i] = this.field.multiply(numerator, denominator);
      }

      return result;
   }
}
