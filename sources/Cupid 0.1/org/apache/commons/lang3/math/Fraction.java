package org.apache.commons.lang3.math;

import java.math.BigInteger;

public final class Fraction extends Number implements Comparable<Fraction> {
  private static final long serialVersionUID = 65382027393090L;
  
  public static final Fraction ZERO = new Fraction(0, 1);
  
  public static final Fraction ONE = new Fraction(1, 1);
  
  public static final Fraction ONE_HALF = new Fraction(1, 2);
  
  public static final Fraction ONE_THIRD = new Fraction(1, 3);
  
  public static final Fraction TWO_THIRDS = new Fraction(2, 3);
  
  public static final Fraction ONE_QUARTER = new Fraction(1, 4);
  
  public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
  
  public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
  
  public static final Fraction ONE_FIFTH = new Fraction(1, 5);
  
  public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
  
  public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
  
  public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
  
  private final int numerator;
  
  private final int denominator;
  
  private transient int hashCode = 0;
  
  private transient String toString = null;
  
  private transient String toProperString = null;
  
  private Fraction(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public static Fraction getFraction(int numerator, int denominator) {
    if (denominator == 0)
      throw new ArithmeticException("The denominator must not be zero"); 
    if (denominator < 0) {
      if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE)
        throw new ArithmeticException("overflow: can't negate"); 
      numerator = -numerator;
      denominator = -denominator;
    } 
    return new Fraction(numerator, denominator);
  }
  
  public static Fraction getFraction(int whole, int numerator, int denominator) {
    long numeratorValue;
    if (denominator == 0)
      throw new ArithmeticException("The denominator must not be zero"); 
    if (denominator < 0)
      throw new ArithmeticException("The denominator must not be negative"); 
    if (numerator < 0)
      throw new ArithmeticException("The numerator must not be negative"); 
    if (whole < 0) {
      numeratorValue = whole * denominator - numerator;
    } else {
      numeratorValue = whole * denominator + numerator;
    } 
    if (numeratorValue < -2147483648L || numeratorValue > 2147483647L)
      throw new ArithmeticException("Numerator too large to represent as an Integer."); 
    return new Fraction((int)numeratorValue, denominator);
  }
  
  public static Fraction getReducedFraction(int numerator, int denominator) {
    if (denominator == 0)
      throw new ArithmeticException("The denominator must not be zero"); 
    if (numerator == 0)
      return ZERO; 
    if (denominator == Integer.MIN_VALUE && (numerator & 0x1) == 0) {
      numerator /= 2;
      denominator /= 2;
    } 
    if (denominator < 0) {
      if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE)
        throw new ArithmeticException("overflow: can't negate"); 
      numerator = -numerator;
      denominator = -denominator;
    } 
    int gcd = greatestCommonDivisor(numerator, denominator);
    numerator /= gcd;
    denominator /= gcd;
    return new Fraction(numerator, denominator);
  }
  
  public static Fraction getFraction(double value) {
    double delta1;
    int sign = (value < 0.0D) ? -1 : 1;
    value = Math.abs(value);
    if (value > 2.147483647E9D || Double.isNaN(value))
      throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN"); 
    int wholeNumber = (int)value;
    value -= wholeNumber;
    int numer0 = 0;
    int denom0 = 1;
    int numer1 = 1;
    int denom1 = 0;
    int numer2 = 0;
    int denom2 = 0;
    int a1 = (int)value;
    int a2 = 0;
    double x1 = 1.0D;
    double x2 = 0.0D;
    double y1 = value - a1;
    double y2 = 0.0D;
    double delta2 = Double.MAX_VALUE;
    int i = 1;
    do {
      delta1 = delta2;
      a2 = (int)(x1 / y1);
      x2 = y1;
      y2 = x1 - a2 * y1;
      numer2 = a1 * numer1 + numer0;
      denom2 = a1 * denom1 + denom0;
      double fraction = numer2 / denom2;
      delta2 = Math.abs(value - fraction);
      a1 = a2;
      x1 = x2;
      y1 = y2;
      numer0 = numer1;
      denom0 = denom1;
      numer1 = numer2;
      denom1 = denom2;
      i++;
    } while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
    if (i == 25)
      throw new ArithmeticException("Unable to convert double to fraction"); 
    return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
  }
  
  public static Fraction getFraction(String str) {
    if (str == null)
      throw new IllegalArgumentException("The string must not be null"); 
    int pos = str.indexOf('.');
    if (pos >= 0)
      return getFraction(Double.parseDouble(str)); 
    pos = str.indexOf(' ');
    if (pos > 0) {
      int whole = Integer.parseInt(str.substring(0, pos));
      str = str.substring(pos + 1);
      pos = str.indexOf('/');
      if (pos < 0)
        throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z"); 
      int i = Integer.parseInt(str.substring(0, pos));
      int j = Integer.parseInt(str.substring(pos + 1));
      return getFraction(whole, i, j);
    } 
    pos = str.indexOf('/');
    if (pos < 0)
      return getFraction(Integer.parseInt(str), 1); 
    int numer = Integer.parseInt(str.substring(0, pos));
    int denom = Integer.parseInt(str.substring(pos + 1));
    return getFraction(numer, denom);
  }
  
  public int getNumerator() {
    return this.numerator;
  }
  
  public int getDenominator() {
    return this.denominator;
  }
  
  public int getProperNumerator() {
    return Math.abs(this.numerator % this.denominator);
  }
  
  public int getProperWhole() {
    return this.numerator / this.denominator;
  }
  
  public int intValue() {
    return this.numerator / this.denominator;
  }
  
  public long longValue() {
    return this.numerator / this.denominator;
  }
  
  public float floatValue() {
    return this.numerator / this.denominator;
  }
  
  public double doubleValue() {
    return this.numerator / this.denominator;
  }
  
  public Fraction reduce() {
    if (this.numerator == 0)
      return equals(ZERO) ? this : ZERO; 
    int gcd = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
    if (gcd == 1)
      return this; 
    return getFraction(this.numerator / gcd, this.denominator / gcd);
  }
  
  public Fraction invert() {
    if (this.numerator == 0)
      throw new ArithmeticException("Unable to invert zero."); 
    if (this.numerator == Integer.MIN_VALUE)
      throw new ArithmeticException("overflow: can't negate numerator"); 
    if (this.numerator < 0)
      return new Fraction(-this.denominator, -this.numerator); 
    return new Fraction(this.denominator, this.numerator);
  }
  
  public Fraction negate() {
    if (this.numerator == Integer.MIN_VALUE)
      throw new ArithmeticException("overflow: too large to negate"); 
    return new Fraction(-this.numerator, this.denominator);
  }
  
  public Fraction abs() {
    if (this.numerator >= 0)
      return this; 
    return negate();
  }
  
  public Fraction pow(int power) {
    if (power == 1)
      return this; 
    if (power == 0)
      return ONE; 
    if (power < 0) {
      if (power == Integer.MIN_VALUE)
        return invert().pow(2).pow(-(power / 2)); 
      return invert().pow(-power);
    } 
    Fraction f = multiplyBy(this);
    if (power % 2 == 0)
      return f.pow(power / 2); 
    return f.pow(power / 2).multiplyBy(this);
  }
  
  private static int greatestCommonDivisor(int u, int v) {
    if (u == 0 || v == 0) {
      if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE)
        throw new ArithmeticException("overflow: gcd is 2^31"); 
      return Math.abs(u) + Math.abs(v);
    } 
    if (Math.abs(u) == 1 || Math.abs(v) == 1)
      return 1; 
    if (u > 0)
      u = -u; 
    if (v > 0)
      v = -v; 
    int k = 0;
    while ((u & 0x1) == 0 && (v & 0x1) == 0 && k < 31) {
      u /= 2;
      v /= 2;
      k++;
    } 
    if (k == 31)
      throw new ArithmeticException("overflow: gcd is 2^31"); 
    int t = ((u & 0x1) == 1) ? v : -(u / 2);
    while (true) {
      while ((t & 0x1) == 0)
        t /= 2; 
      if (t > 0) {
        u = -t;
      } else {
        v = t;
      } 
      t = (v - u) / 2;
      if (t == 0)
        return -u * (1 << k); 
    } 
  }
  
  private static int mulAndCheck(int x, int y) {
    long m = x * y;
    if (m < -2147483648L || m > 2147483647L)
      throw new ArithmeticException("overflow: mul"); 
    return (int)m;
  }
  
  private static int mulPosAndCheck(int x, int y) {
    long m = x * y;
    if (m > 2147483647L)
      throw new ArithmeticException("overflow: mulPos"); 
    return (int)m;
  }
  
  private static int addAndCheck(int x, int y) {
    long s = x + y;
    if (s < -2147483648L || s > 2147483647L)
      throw new ArithmeticException("overflow: add"); 
    return (int)s;
  }
  
  private static int subAndCheck(int x, int y) {
    long s = x - y;
    if (s < -2147483648L || s > 2147483647L)
      throw new ArithmeticException("overflow: add"); 
    return (int)s;
  }
  
  public Fraction add(Fraction fraction) {
    return addSub(fraction, true);
  }
  
  public Fraction subtract(Fraction fraction) {
    return addSub(fraction, false);
  }
  
  private Fraction addSub(Fraction fraction, boolean isAdd) {
    if (fraction == null)
      throw new IllegalArgumentException("The fraction must not be null"); 
    if (this.numerator == 0)
      return isAdd ? fraction : fraction.negate(); 
    if (fraction.numerator == 0)
      return this; 
    int d1 = greatestCommonDivisor(this.denominator, fraction.denominator);
    if (d1 == 1) {
      int i = mulAndCheck(this.numerator, fraction.denominator);
      int j = mulAndCheck(fraction.numerator, this.denominator);
      return new Fraction(isAdd ? addAndCheck(i, j) : subAndCheck(i, j), mulPosAndCheck(this.denominator, fraction.denominator));
    } 
    BigInteger uvp = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf((fraction.denominator / d1)));
    BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf((this.denominator / d1)));
    BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
    int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
    int d2 = (tmodd1 == 0) ? d1 : greatestCommonDivisor(tmodd1, d1);
    BigInteger w = t.divide(BigInteger.valueOf(d2));
    if (w.bitLength() > 31)
      throw new ArithmeticException("overflow: numerator too large after multiply"); 
    return new Fraction(w.intValue(), mulPosAndCheck(this.denominator / d1, fraction.denominator / d2));
  }
  
  public Fraction multiplyBy(Fraction fraction) {
    if (fraction == null)
      throw new IllegalArgumentException("The fraction must not be null"); 
    if (this.numerator == 0 || fraction.numerator == 0)
      return ZERO; 
    int d1 = greatestCommonDivisor(this.numerator, fraction.denominator);
    int d2 = greatestCommonDivisor(fraction.numerator, this.denominator);
    return getReducedFraction(mulAndCheck(this.numerator / d1, fraction.numerator / d2), mulPosAndCheck(this.denominator / d2, fraction.denominator / d1));
  }
  
  public Fraction divideBy(Fraction fraction) {
    if (fraction == null)
      throw new IllegalArgumentException("The fraction must not be null"); 
    if (fraction.numerator == 0)
      throw new ArithmeticException("The fraction to divide by must not be zero"); 
    return multiplyBy(fraction.invert());
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (!(obj instanceof Fraction))
      return false; 
    Fraction other = (Fraction)obj;
    return (getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator());
  }
  
  public int hashCode() {
    if (this.hashCode == 0)
      this.hashCode = 37 * (629 + getNumerator()) + getDenominator(); 
    return this.hashCode;
  }
  
  public int compareTo(Fraction other) {
    if (this == other)
      return 0; 
    if (this.numerator == other.numerator && this.denominator == other.denominator)
      return 0; 
    long first = this.numerator * other.denominator;
    long second = other.numerator * this.denominator;
    if (first == second)
      return 0; 
    if (first < second)
      return -1; 
    return 1;
  }
  
  public String toString() {
    if (this.toString == null)
      this.toString = (new StringBuilder(32)).append(getNumerator()).append('/').append(getDenominator()).toString(); 
    return this.toString;
  }
  
  public String toProperString() {
    if (this.toProperString == null)
      if (this.numerator == 0) {
        this.toProperString = "0";
      } else if (this.numerator == this.denominator) {
        this.toProperString = "1";
      } else if (this.numerator == -1 * this.denominator) {
        this.toProperString = "-1";
      } else if (((this.numerator > 0) ? -this.numerator : this.numerator) < -this.denominator) {
        int properNumerator = getProperNumerator();
        if (properNumerator == 0) {
          this.toProperString = Integer.toString(getProperWhole());
        } else {
          this.toProperString = (new StringBuilder(32)).append(getProperWhole()).append(' ').append(properNumerator).append('/').append(getDenominator()).toString();
        } 
      } else {
        this.toProperString = (new StringBuilder(32)).append(getNumerator()).append('/').append(getDenominator()).toString();
      }  
    return this.toProperString;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\math\Fraction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */