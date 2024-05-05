/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.math.BigInteger;

public final class NumberToString {
    private final boolean isNaN;
    private boolean isNegative;
    private int decimalExponent;
    private char[] digits;
    private int nDigits;
    private static final int expMask = 2047;
    private static final long fractMask = 0xFFFFFFFFFFFFFL;
    private static final int expShift = 52;
    private static final int expBias = 1023;
    private static final long fractHOB = 0x10000000000000L;
    private static final long expOne = 0x3FF0000000000000L;
    private static final int maxSmallBinExp = 62;
    private static final int minSmallBinExp = -21;
    private static final long[] powersOf5 = new long[]{1L, 5L, 25L, 125L, 625L, 3125L, 15625L, 78125L, 390625L, 1953125L, 9765625L, 48828125L, 244140625L, 1220703125L, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L};
    private static final int[] nBitsPowerOf5 = new int[]{0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61};
    private static final char[] infinityDigits = new char[]{'I', 'n', 'f', 'i', 'n', 'i', 't', 'y'};
    private static final char[] nanDigits = new char[]{'N', 'a', 'N'};
    private static final char[] zeroes = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    private static BigInteger[] powerOf5Cache;

    public static String stringFor(double value) {
        return new NumberToString(value).toString();
    }

    private NumberToString(double value) {
        long lowDigitDifference;
        boolean high;
        boolean low;
        int ndigit;
        int nSignificantBits;
        long bits = Double.doubleToLongBits(value);
        int upper = (int)(bits >> 32);
        this.isNegative = upper < 0;
        int exponent = upper >> 20 & 0x7FF;
        bits &= 0xFFFFFFFFFFFFFL;
        if (exponent == 2047) {
            this.isNaN = true;
            if (bits == 0L) {
                this.digits = infinityDigits;
            } else {
                this.digits = nanDigits;
                this.isNegative = false;
            }
            this.nDigits = this.digits.length;
            return;
        }
        this.isNaN = false;
        if (exponent == 0) {
            if (bits == 0L) {
                this.decimalExponent = 0;
                this.digits = zeroes;
                this.nDigits = 1;
                return;
            }
            while ((bits & 0x10000000000000L) == 0L) {
                bits <<= 1;
                --exponent;
            }
            nSignificantBits = 52 + exponent + 1;
            ++exponent;
        } else {
            bits |= 0x10000000000000L;
            nSignificantBits = 53;
        }
        int nFractBits = NumberToString.countSignificantBits(bits);
        int nTinyBits = Math.max(0, nFractBits - (exponent -= 1023) - 1);
        if (exponent <= 62 && exponent >= -21 && nTinyBits < powersOf5.length && nFractBits + nBitsPowerOf5[nTinyBits] < 64 && nTinyBits == 0) {
            long halfULP = exponent > nSignificantBits ? 1L << exponent - nSignificantBits - 1 : 0L;
            bits = exponent >= 52 ? (bits <<= exponent - 52) : (bits >>>= 52 - exponent);
            int i = 0;
            while (halfULP >= 10L) {
                halfULP /= 10L;
                ++i;
            }
            int decExp = 0;
            if (i != 0) {
                long powerOf10 = powersOf5[i] << i;
                long residue = bits % powerOf10;
                bits /= powerOf10;
                decExp += i;
                if (residue >= powerOf10 >> 1) {
                    ++bits;
                }
            }
            int ndigits = 20;
            char[] digits0 = new char[26];
            int digitno = ndigits - 1;
            int c = (int)(bits % 10L);
            bits /= 10L;
            while (c == 0) {
                ++decExp;
                c = (int)(bits % 10L);
                bits /= 10L;
            }
            while (bits != 0L) {
                digits0[digitno--] = (char)(c + 48);
                ++decExp;
                c = (int)(bits % 10L);
                bits /= 10L;
            }
            digits0[digitno] = (char)(c + 48);
            char[] result = new char[ndigits -= digitno];
            System.arraycopy(digits0, digitno, result, 0, ndigits);
            this.digits = result;
            this.decimalExponent = decExp + 1;
            this.nDigits = ndigits;
            return;
        }
        double d2 = Double.longBitsToDouble(0x3FF0000000000000L | bits & 0xFFEFFFFFFFFFFFFFL);
        int decExponent = (int)Math.floor((d2 - 1.5) * 0.289529654 + 0.176091259 + (double)exponent * 0.301029995663981);
        int B5 = Math.max(0, -decExponent);
        int B2 = B5 + nTinyBits + exponent;
        int S5 = Math.max(0, decExponent);
        int S2 = S5 + nTinyBits;
        int M5 = B5;
        int M2 = B2 - nSignificantBits;
        bits >>>= 53 - nFractBits;
        int common2factor = Math.min(B2 -= nFractBits - 1, S2);
        B2 -= common2factor;
        S2 -= common2factor;
        M2 -= common2factor;
        if (nFractBits == 1) {
            --M2;
        }
        if (M2 < 0) {
            B2 -= M2;
            S2 -= M2;
            M2 = 0;
        }
        this.digits = new char[32];
        char[] digits0 = this.digits;
        int Bbits = nFractBits + B2 + (B5 < nBitsPowerOf5.length ? nBitsPowerOf5[B5] : B5 * 3);
        int tenSbits = S2 + 1 + (S5 + 1 < nBitsPowerOf5.length ? nBitsPowerOf5[S5 + 1] : (S5 + 1) * 3);
        if (Bbits < 64 && tenSbits < 64) {
            long b = bits * powersOf5[B5] << B2;
            long s = powersOf5[S5] << S2;
            long m = powersOf5[M5] << M2;
            long tens = s * 10L;
            ndigit = 0;
            int q = (int)(b / s);
            low = (b = 10L * (b % s)) < (m *= 10L);
            boolean bl = high = b + m > tens;
            if (q == 0 && !high) {
                --decExponent;
            } else {
                digits0[ndigit++] = (char)(48 + q);
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = false;
                high = false;
            }
            while (!low && !high) {
                q = (int)(b / s);
                b = 10L * (b % s);
                if ((m *= 10L) > 0L) {
                    low = b < m;
                    high = b + m > tens;
                } else {
                    low = true;
                    high = true;
                }
                if (low && q == 0) break;
                digits0[ndigit++] = (char)(48 + q);
            }
            lowDigitDifference = (b << 1) - tens;
        } else {
            BigInteger Bval = NumberToString.multiplyPowerOf5And2(BigInteger.valueOf(bits), B5, B2);
            BigInteger Sval = NumberToString.constructPowerOf5And2(S5, S2);
            BigInteger Mval = NumberToString.constructPowerOf5And2(M5, M2);
            int shiftBias = Long.numberOfLeadingZeros(bits) - 4;
            Bval = Bval.shiftLeft(shiftBias);
            Mval = Mval.shiftLeft(shiftBias);
            Sval = Sval.shiftLeft(shiftBias);
            BigInteger tenSval = Sval.multiply(BigInteger.TEN);
            ndigit = 0;
            BigInteger[] quoRem = Bval.divideAndRemainder(Sval);
            int q = quoRem[0].intValue();
            Bval = quoRem[1].multiply(BigInteger.TEN);
            low = Bval.compareTo(Mval = Mval.multiply(BigInteger.TEN)) < 0;
            boolean bl = high = Bval.add(Mval).compareTo(tenSval) > 0;
            if (q == 0 && !high) {
                --decExponent;
            } else {
                digits0[ndigit++] = (char)(48 + q);
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = false;
                high = false;
            }
            while (!low && !high) {
                quoRem = Bval.divideAndRemainder(Sval);
                q = quoRem[0].intValue();
                Bval = quoRem[1].multiply(BigInteger.TEN);
                low = Bval.compareTo(Mval = Mval.multiply(BigInteger.TEN)) < 0;
                boolean bl2 = high = Bval.add(Mval).compareTo(tenSval) > 0;
                if (low && q == 0) break;
                digits0[ndigit++] = (char)(48 + q);
            }
            if (high && low) {
                Bval = Bval.shiftLeft(1);
                lowDigitDifference = Bval.compareTo(tenSval);
            } else {
                lowDigitDifference = 0L;
            }
        }
        this.decimalExponent = decExponent + 1;
        this.digits = digits0;
        this.nDigits = ndigit;
        if (high) {
            if (low) {
                if (lowDigitDifference == 0L) {
                    if ((digits0[this.nDigits - 1] & '\u0001') != 0) {
                        this.roundup();
                    }
                } else if (lowDigitDifference > 0L) {
                    this.roundup();
                }
            } else {
                this.roundup();
            }
        }
    }

    private static int countSignificantBits(long bits) {
        if (bits != 0L) {
            return 64 - Long.numberOfLeadingZeros(bits) - Long.numberOfTrailingZeros(bits);
        }
        return 0;
    }

    private static BigInteger bigPowerOf5(int power) {
        if (powerOf5Cache == null) {
            powerOf5Cache = new BigInteger[power + 1];
        } else if (powerOf5Cache.length <= power) {
            BigInteger[] t = new BigInteger[power + 1];
            System.arraycopy(powerOf5Cache, 0, t, 0, powerOf5Cache.length);
            powerOf5Cache = t;
        }
        if (powerOf5Cache[power] != null) {
            return powerOf5Cache[power];
        }
        if (power < powersOf5.length) {
            NumberToString.powerOf5Cache[power] = BigInteger.valueOf(powersOf5[power]);
            return NumberToString.powerOf5Cache[power];
        }
        int q = power >> 1;
        int r = power - q;
        BigInteger bigQ = powerOf5Cache[q];
        if (bigQ == null) {
            bigQ = NumberToString.bigPowerOf5(q);
        }
        if (r < powersOf5.length) {
            NumberToString.powerOf5Cache[power] = bigQ.multiply(BigInteger.valueOf(powersOf5[r]));
            return NumberToString.powerOf5Cache[power];
        }
        BigInteger bigR = powerOf5Cache[r];
        if (bigR == null) {
            bigR = NumberToString.bigPowerOf5(r);
        }
        NumberToString.powerOf5Cache[power] = bigQ.multiply(bigR);
        return NumberToString.powerOf5Cache[power];
    }

    private static BigInteger multiplyPowerOf5And2(BigInteger value, int p5, int p2) {
        BigInteger returnValue = value;
        if (p5 != 0) {
            returnValue = returnValue.multiply(NumberToString.bigPowerOf5(p5));
        }
        if (p2 != 0) {
            returnValue = returnValue.shiftLeft(p2);
        }
        return returnValue;
    }

    private static BigInteger constructPowerOf5And2(int p5, int p2) {
        BigInteger v = NumberToString.bigPowerOf5(p5);
        if (p2 != 0) {
            v = v.shiftLeft(p2);
        }
        return v;
    }

    private void roundup() {
        int i = this.nDigits - 1;
        char q = this.digits[i];
        while (q == '9' && i > 0) {
            if (this.decimalExponent < 0) {
                --this.nDigits;
            } else {
                this.digits[i] = 48;
            }
            q = this.digits[--i];
        }
        if (q == '9') {
            ++this.decimalExponent;
            this.digits[0] = 49;
            return;
        }
        this.digits[i] = (char)(q + '\u0001');
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        if (this.isNegative) {
            sb.append('-');
        }
        if (this.isNaN) {
            sb.append(this.digits, 0, this.nDigits);
        } else if (this.decimalExponent > 0 && this.decimalExponent <= 21) {
            int charLength = Math.min(this.nDigits, this.decimalExponent);
            sb.append(this.digits, 0, charLength);
            if (charLength < this.decimalExponent) {
                sb.append(zeroes, 0, this.decimalExponent - charLength);
            } else if (charLength < this.nDigits) {
                sb.append('.');
                sb.append(this.digits, charLength, this.nDigits - charLength);
            }
        } else if (this.decimalExponent <= 0 && this.decimalExponent > -6) {
            sb.append('0');
            sb.append('.');
            if (this.decimalExponent != 0) {
                sb.append(zeroes, 0, -this.decimalExponent);
            }
            sb.append(this.digits, 0, this.nDigits);
        } else {
            int exponent;
            int e;
            sb.append(this.digits[0]);
            if (this.nDigits > 1) {
                sb.append('.');
                sb.append(this.digits, 1, this.nDigits - 1);
            }
            sb.append('e');
            if (this.decimalExponent <= 0) {
                sb.append('-');
                exponent = e = -this.decimalExponent + 1;
            } else {
                sb.append('+');
                exponent = e = this.decimalExponent - 1;
            }
            if (exponent > 99) {
                sb.append((char)(e / 100 + 48));
                e %= 100;
            }
            if (exponent > 9) {
                sb.append((char)(e / 10 + 48));
                e %= 10;
            }
            sb.append((char)(e + 48));
        }
        return sb.toString();
    }
}

