/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.RoundingUtils;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.UFieldPosition;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.FieldPosition;

public abstract class DecimalQuantity_AbstractBCD
implements DecimalQuantity {
    protected int scale;
    protected int precision;
    protected byte flags;
    protected static final int NEGATIVE_FLAG = 1;
    protected static final int INFINITY_FLAG = 2;
    protected static final int NAN_FLAG = 4;
    protected double origDouble;
    protected int origDelta;
    protected boolean isApproximate;
    protected int lOptPos = Integer.MAX_VALUE;
    protected int lReqPos = 0;
    protected int rReqPos = 0;
    protected int rOptPos = Integer.MIN_VALUE;
    private static final double[] DOUBLE_MULTIPLIERS = new double[]{1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0E7, 1.0E8, 1.0E9, 1.0E10, 1.0E11, 1.0E12, 1.0E13, 1.0E14, 1.0E15, 1.0E16, 1.0E17, 1.0E18, 1.0E19, 1.0E20, 1.0E21};
    @Deprecated
    public boolean explicitExactDouble = false;
    static final byte[] INT64_BCD = new byte[]{9, 2, 2, 3, 3, 7, 2, 0, 3, 6, 8, 5, 4, 7, 7, 5, 8, 0, 8};
    private static final int SECTION_LOWER_EDGE = -1;
    private static final int SECTION_UPPER_EDGE = -2;

    @Override
    public void copyFrom(DecimalQuantity _other) {
        this.copyBcdFrom(_other);
        DecimalQuantity_AbstractBCD other = (DecimalQuantity_AbstractBCD)_other;
        this.lOptPos = other.lOptPos;
        this.lReqPos = other.lReqPos;
        this.rReqPos = other.rReqPos;
        this.rOptPos = other.rOptPos;
        this.scale = other.scale;
        this.precision = other.precision;
        this.flags = other.flags;
        this.origDouble = other.origDouble;
        this.origDelta = other.origDelta;
        this.isApproximate = other.isApproximate;
    }

    public DecimalQuantity_AbstractBCD clear() {
        this.lOptPos = Integer.MAX_VALUE;
        this.lReqPos = 0;
        this.rReqPos = 0;
        this.rOptPos = Integer.MIN_VALUE;
        this.flags = 0;
        this.setBcdToZero();
        return this;
    }

    @Override
    public void setIntegerLength(int minInt, int maxInt) {
        assert (minInt >= 0);
        assert (maxInt >= minInt);
        if (minInt < this.lReqPos) {
            minInt = this.lReqPos;
        }
        this.lOptPos = maxInt;
        this.lReqPos = minInt;
    }

    @Override
    public void setFractionLength(int minFrac, int maxFrac) {
        assert (minFrac >= 0);
        assert (maxFrac >= minFrac);
        this.rReqPos = -minFrac;
        this.rOptPos = -maxFrac;
    }

    @Override
    public long getPositionFingerprint() {
        long fingerprint = 0L;
        fingerprint ^= (long)this.lOptPos;
        fingerprint ^= (long)(this.lReqPos << 16);
        fingerprint ^= (long)this.rReqPos << 32;
        return fingerprint ^= (long)this.rOptPos << 48;
    }

    @Override
    public void roundToIncrement(BigDecimal roundingIncrement, MathContext mathContext) {
        BigDecimal temp = this.toBigDecimal();
        if ((temp = temp.divide(roundingIncrement, 0, mathContext.getRoundingMode()).multiply(roundingIncrement).round(mathContext)).signum() == 0) {
            this.setBcdToZero();
        } else {
            this.setToBigDecimal(temp);
        }
    }

    @Override
    public void multiplyBy(BigDecimal multiplicand) {
        if (this.isInfinite() || this.isZero() || this.isNaN()) {
            return;
        }
        BigDecimal temp = this.toBigDecimal();
        temp = temp.multiply(multiplicand);
        this.setToBigDecimal(temp);
    }

    @Override
    public void negate() {
        this.flags = (byte)(this.flags ^ 1);
    }

    @Override
    public int getMagnitude() throws ArithmeticException {
        if (this.precision == 0) {
            throw new ArithmeticException("Magnitude is not well-defined for zero");
        }
        return this.scale + this.precision - 1;
    }

    @Override
    public void adjustMagnitude(int delta) {
        if (this.precision != 0) {
            this.scale = Utility.addExact(this.scale, delta);
            this.origDelta = Utility.addExact(this.origDelta, delta);
        }
    }

    @Override
    public StandardPlural getStandardPlural(PluralRules rules) {
        if (rules == null) {
            return StandardPlural.OTHER;
        }
        String ruleString = rules.select(this);
        return StandardPlural.orOtherFromString(ruleString);
    }

    @Override
    public double getPluralOperand(PluralRules.Operand operand) {
        assert (!this.isApproximate);
        switch (operand) {
            case i: {
                return this.isNegative() ? (double)(-this.toLong(true)) : (double)this.toLong(true);
            }
            case f: {
                return this.toFractionLong(true);
            }
            case t: {
                return this.toFractionLong(false);
            }
            case v: {
                return this.fractionCount();
            }
            case w: {
                return this.fractionCountWithoutTrailingZeros();
            }
        }
        return Math.abs(this.toDouble());
    }

    @Override
    public void populateUFieldPosition(FieldPosition fp) {
        if (fp instanceof UFieldPosition) {
            ((UFieldPosition)fp).setFractionDigits((int)this.getPluralOperand(PluralRules.Operand.v), (long)this.getPluralOperand(PluralRules.Operand.f));
        }
    }

    @Override
    public int getUpperDisplayMagnitude() {
        assert (!this.isApproximate);
        int magnitude = this.scale + this.precision;
        int result = this.lReqPos > magnitude ? this.lReqPos : (this.lOptPos < magnitude ? this.lOptPos : magnitude);
        return result - 1;
    }

    @Override
    public int getLowerDisplayMagnitude() {
        assert (!this.isApproximate);
        int magnitude = this.scale;
        int result = this.rReqPos < magnitude ? this.rReqPos : (this.rOptPos > magnitude ? this.rOptPos : magnitude);
        return result;
    }

    @Override
    public byte getDigit(int magnitude) {
        assert (!this.isApproximate);
        return this.getDigitPos(magnitude - this.scale);
    }

    private int fractionCount() {
        return -this.getLowerDisplayMagnitude();
    }

    private int fractionCountWithoutTrailingZeros() {
        return Math.max(-this.scale, 0);
    }

    @Override
    public boolean isNegative() {
        return (this.flags & 1) != 0;
    }

    @Override
    public int signum() {
        return this.isNegative() ? -1 : (this.isZero() ? 0 : 1);
    }

    @Override
    public boolean isInfinite() {
        return (this.flags & 2) != 0;
    }

    @Override
    public boolean isNaN() {
        return (this.flags & 4) != 0;
    }

    @Override
    public boolean isZero() {
        return this.precision == 0;
    }

    public void setToInt(int n2) {
        this.setBcdToZero();
        this.flags = 0;
        if (n2 < 0) {
            this.flags = (byte)(this.flags | 1);
            n2 = -n2;
        }
        if (n2 != 0) {
            this._setToInt(n2);
            this.compact();
        }
    }

    private void _setToInt(int n2) {
        if (n2 == Integer.MIN_VALUE) {
            this.readLongToBcd(-((long)n2));
        } else {
            this.readIntToBcd(n2);
        }
    }

    public void setToLong(long n2) {
        this.setBcdToZero();
        this.flags = 0;
        if (n2 < 0L) {
            this.flags = (byte)(this.flags | 1);
            n2 = -n2;
        }
        if (n2 != 0L) {
            this._setToLong(n2);
            this.compact();
        }
    }

    private void _setToLong(long n2) {
        if (n2 == Long.MIN_VALUE) {
            this.readBigIntegerToBcd(BigInteger.valueOf(n2).negate());
        } else if (n2 <= Integer.MAX_VALUE) {
            this.readIntToBcd((int)n2);
        } else {
            this.readLongToBcd(n2);
        }
    }

    public void setToBigInteger(BigInteger n2) {
        this.setBcdToZero();
        this.flags = 0;
        if (n2.signum() == -1) {
            this.flags = (byte)(this.flags | 1);
            n2 = n2.negate();
        }
        if (n2.signum() != 0) {
            this._setToBigInteger(n2);
            this.compact();
        }
    }

    private void _setToBigInteger(BigInteger n2) {
        if (n2.bitLength() < 32) {
            this.readIntToBcd(n2.intValue());
        } else if (n2.bitLength() < 64) {
            this.readLongToBcd(n2.longValue());
        } else {
            this.readBigIntegerToBcd(n2);
        }
    }

    public void setToDouble(double n2) {
        this.setBcdToZero();
        this.flags = 0;
        if (Double.compare(n2, 0.0) < 0) {
            this.flags = (byte)(this.flags | 1);
            n2 = -n2;
        }
        if (Double.isNaN(n2)) {
            this.flags = (byte)(this.flags | 4);
        } else if (Double.isInfinite(n2)) {
            this.flags = (byte)(this.flags | 2);
        } else if (n2 != 0.0) {
            this._setToDoubleFast(n2);
            this.compact();
        }
    }

    private void _setToDoubleFast(double n2) {
        int i2;
        this.isApproximate = true;
        this.origDouble = n2;
        this.origDelta = 0;
        long ieeeBits = Double.doubleToLongBits(n2);
        int exponent = (int)((ieeeBits & 0x7FF0000000000000L) >> 52) - 1023;
        if (exponent <= 52 && (double)((long)n2) == n2) {
            this._setToLong((long)n2);
            return;
        }
        int fracLength = (int)((double)(52 - exponent) / 3.32192809489);
        if (fracLength >= 0) {
            for (i2 = fracLength; i2 >= 22; i2 -= 22) {
                n2 *= 1.0E22;
            }
            n2 *= DOUBLE_MULTIPLIERS[i2];
        } else {
            for (i2 = fracLength; i2 <= -22; i2 += 22) {
                n2 /= 1.0E22;
            }
            n2 /= DOUBLE_MULTIPLIERS[-i2];
        }
        long result = Math.round(n2);
        if (result != 0L) {
            this._setToLong(result);
            this.scale -= fracLength;
        }
    }

    private void convertToAccurateDouble() {
        double n2 = this.origDouble;
        assert (n2 != 0.0);
        int delta = this.origDelta;
        this.setBcdToZero();
        String dstr = Double.toString(n2);
        if (dstr.indexOf(69) != -1) {
            assert (dstr.indexOf(46) == 1);
            int expPos = dstr.indexOf(69);
            this._setToLong(Long.parseLong(dstr.charAt(0) + dstr.substring(2, expPos)));
            this.scale += Integer.parseInt(dstr.substring(expPos + 1)) - (expPos - 1) + 1;
        } else if (dstr.charAt(0) == '0') {
            assert (dstr.indexOf(46) == 1);
            this._setToLong(Long.parseLong(dstr.substring(2)));
            this.scale += 2 - dstr.length();
        } else if (dstr.charAt(dstr.length() - 1) == '0') {
            assert (dstr.indexOf(46) == dstr.length() - 2);
            assert (dstr.length() - 2 <= 18);
            this._setToLong(Long.parseLong(dstr.substring(0, dstr.length() - 2)));
        } else {
            int decimalPos = dstr.indexOf(46);
            this._setToLong(Long.parseLong(dstr.substring(0, decimalPos) + dstr.substring(decimalPos + 1)));
            this.scale += decimalPos - dstr.length() + 1;
        }
        this.scale += delta;
        this.compact();
        this.explicitExactDouble = true;
    }

    @Override
    public void setToBigDecimal(BigDecimal n2) {
        this.setBcdToZero();
        this.flags = 0;
        if (n2.signum() == -1) {
            this.flags = (byte)(this.flags | 1);
            n2 = n2.negate();
        }
        if (n2.signum() != 0) {
            this._setToBigDecimal(n2);
            this.compact();
        }
    }

    private void _setToBigDecimal(BigDecimal n2) {
        int fracLength = n2.scale();
        n2 = n2.scaleByPowerOfTen(fracLength);
        BigInteger bi = n2.toBigInteger();
        this._setToBigInteger(bi);
        this.scale -= fracLength;
    }

    public long toLong(boolean truncateIfOverflow) {
        assert (truncateIfOverflow || this.fitsInLong());
        long result = 0L;
        int upperMagnitude = Math.min(this.scale + this.precision, this.lOptPos) - 1;
        if (truncateIfOverflow) {
            upperMagnitude = Math.min(upperMagnitude, 17);
        }
        for (int magnitude = upperMagnitude; magnitude >= 0; --magnitude) {
            result = result * 10L + (long)this.getDigitPos(magnitude - this.scale);
        }
        if (this.isNegative()) {
            result = -result;
        }
        return result;
    }

    public long toFractionLong(boolean includeTrailingZeros) {
        long result = 0L;
        int magnitude = -1;
        int lowerMagnitude = Math.max(this.scale, this.rOptPos);
        if (includeTrailingZeros) {
            lowerMagnitude = Math.min(lowerMagnitude, this.rReqPos);
        }
        while (magnitude >= lowerMagnitude && (double)result <= 1.0E17) {
            result = result * 10L + (long)this.getDigitPos(magnitude - this.scale);
            --magnitude;
        }
        if (!includeTrailingZeros) {
            while (result > 0L && result % 10L == 0L) {
                result /= 10L;
            }
        }
        return result;
    }

    public boolean fitsInLong() {
        if (this.isZero()) {
            return true;
        }
        if (this.scale < 0) {
            return false;
        }
        int magnitude = this.getMagnitude();
        if (magnitude < 18) {
            return true;
        }
        if (magnitude > 18) {
            return false;
        }
        for (int p2 = 0; p2 < this.precision; ++p2) {
            byte digit = this.getDigit(18 - p2);
            if (digit < INT64_BCD[p2]) {
                return true;
            }
            if (digit <= INT64_BCD[p2]) continue;
            return false;
        }
        return this.isNegative();
    }

    @Override
    public double toDouble() {
        assert (!this.isApproximate);
        if (this.isNaN()) {
            return Double.NaN;
        }
        if (this.isInfinite()) {
            return this.isNegative() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        long tempLong = 0L;
        int lostDigits = this.precision - Math.min(this.precision, 17);
        for (int shift = this.precision - 1; shift >= lostDigits; --shift) {
            tempLong = tempLong * 10L + (long)this.getDigitPos(shift);
        }
        double result = tempLong;
        int _scale = this.scale + lostDigits;
        if (_scale >= 0) {
            int i2;
            for (i2 = _scale; i2 >= 22; i2 -= 22) {
                if (!Double.isInfinite(result *= 1.0E22)) continue;
                i2 = 0;
                break;
            }
            result *= DOUBLE_MULTIPLIERS[i2];
        } else {
            int i3;
            for (i3 = _scale; i3 <= -22; i3 += 22) {
                if ((result /= 1.0E22) != 0.0) continue;
                i3 = 0;
                break;
            }
            result /= DOUBLE_MULTIPLIERS[-i3];
        }
        if (this.isNegative()) {
            result = -result;
        }
        return result;
    }

    @Override
    public BigDecimal toBigDecimal() {
        if (this.isApproximate) {
            this.convertToAccurateDouble();
        }
        return this.bcdToBigDecimal();
    }

    private static int safeSubtract(int a2, int b2) {
        int diff = a2 - b2;
        if (b2 < 0 && diff < a2) {
            return Integer.MAX_VALUE;
        }
        if (b2 > 0 && diff > a2) {
            return Integer.MIN_VALUE;
        }
        return diff;
    }

    public void truncate() {
        if (this.scale < 0) {
            this.shiftRight(-this.scale);
            this.scale = 0;
            this.compact();
        }
    }

    @Override
    public void roundToMagnitude(int magnitude, MathContext mathContext) {
        int position = DecimalQuantity_AbstractBCD.safeSubtract(magnitude, this.scale);
        int _mcPrecision = mathContext.getPrecision();
        if (magnitude == Integer.MAX_VALUE || _mcPrecision > 0 && this.precision - position > _mcPrecision) {
            position = this.precision - _mcPrecision;
        }
        if ((position > 0 || this.isApproximate) && this.precision != 0) {
            int p2;
            byte leadingDigit = this.getDigitPos(DecimalQuantity_AbstractBCD.safeSubtract(position, 1));
            byte trailingDigit = this.getDigitPos(position);
            int section = 2;
            if (!this.isApproximate) {
                if (leadingDigit < 5) {
                    section = 1;
                } else if (leadingDigit > 5) {
                    section = 3;
                } else {
                    for (p2 = DecimalQuantity_AbstractBCD.safeSubtract(position, 2); p2 >= 0; --p2) {
                        if (this.getDigitPos(p2) == 0) continue;
                        section = 3;
                        break;
                    }
                }
            } else {
                int minP = Math.max(0, this.precision - 14);
                if (leadingDigit == 0) {
                    section = -1;
                    for (p2 = DecimalQuantity_AbstractBCD.safeSubtract(position, 2); p2 >= minP; --p2) {
                        if (this.getDigitPos(p2) == 0) continue;
                        section = 1;
                        break;
                    }
                } else if (leadingDigit == 4) {
                    while (p2 >= minP) {
                        if (this.getDigitPos(p2) != 9) {
                            section = 1;
                            break;
                        }
                        --p2;
                    }
                } else if (leadingDigit == 5) {
                    while (p2 >= minP) {
                        if (this.getDigitPos(p2) != 0) {
                            section = 3;
                            break;
                        }
                        --p2;
                    }
                } else if (leadingDigit == 9) {
                    section = -2;
                    while (p2 >= minP) {
                        if (this.getDigitPos(p2) != 9) {
                            section = 3;
                            break;
                        }
                        --p2;
                    }
                } else {
                    section = leadingDigit < 5 ? 1 : 3;
                }
                boolean roundsAtMidpoint = RoundingUtils.roundsAtMidpoint(mathContext.getRoundingMode().ordinal());
                if (DecimalQuantity_AbstractBCD.safeSubtract(position, 1) < this.precision - 14 || roundsAtMidpoint && section == 2 || !roundsAtMidpoint && section < 0) {
                    this.convertToAccurateDouble();
                    this.roundToMagnitude(magnitude, mathContext);
                    return;
                }
                this.isApproximate = false;
                this.origDouble = 0.0;
                this.origDelta = 0;
                if (position <= 0) {
                    return;
                }
                if (section == -1) {
                    section = 1;
                }
                if (section == -2) {
                    section = 3;
                }
            }
            boolean roundDown = RoundingUtils.getRoundingDirection(trailingDigit % 2 == 0, this.isNegative(), section, mathContext.getRoundingMode().ordinal(), this);
            if (position >= this.precision) {
                this.setBcdToZero();
                this.scale = magnitude;
            } else {
                this.shiftRight(position);
            }
            if (!roundDown) {
                if (trailingDigit == 9) {
                    int bubblePos = 0;
                    while (this.getDigitPos(bubblePos) == 9) {
                        ++bubblePos;
                    }
                    this.shiftRight(bubblePos);
                }
                byte digit0 = this.getDigitPos(0);
                assert (digit0 != 9);
                this.setDigitPos(0, (byte)(digit0 + 1));
                ++this.precision;
            }
            this.compact();
        }
    }

    @Override
    public void roundToInfinity() {
        if (this.isApproximate) {
            this.convertToAccurateDouble();
        }
    }

    @Deprecated
    public void appendDigit(byte value, int leadingZeros, boolean appendAsInteger) {
        assert (leadingZeros >= 0);
        if (value == 0) {
            if (appendAsInteger && this.precision != 0) {
                this.scale += leadingZeros + 1;
            }
            return;
        }
        if (this.scale > 0) {
            leadingZeros += this.scale;
            if (appendAsInteger) {
                this.scale = 0;
            }
        }
        this.shiftLeft(leadingZeros + 1);
        this.setDigitPos(0, value);
        if (appendAsInteger) {
            this.scale += leadingZeros + 1;
        }
    }

    @Override
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        if (this.isNegative()) {
            sb.append('-');
        }
        if (this.precision == 0 || this.getMagnitude() < 0) {
            sb.append('0');
        }
        for (int m2 = this.getUpperDisplayMagnitude(); m2 >= this.getLowerDisplayMagnitude(); --m2) {
            sb.append((char)(48 + this.getDigit(m2)));
            if (m2 != 0) continue;
            sb.append('.');
        }
        return sb.toString();
    }

    public String toScientificString() {
        StringBuilder sb = new StringBuilder();
        this.toScientificString(sb);
        return sb.toString();
    }

    public void toScientificString(StringBuilder result) {
        assert (!this.isApproximate);
        if (this.isNegative()) {
            result.append('-');
        }
        if (this.precision == 0) {
            result.append("0E+0");
            return;
        }
        int upperPos = Math.min(this.precision + this.scale, this.lOptPos) - this.scale - 1;
        int lowerPos = Math.max(this.scale, this.rOptPos) - this.scale;
        int p2 = upperPos;
        result.append((char)(48 + this.getDigitPos(p2)));
        if (--p2 >= lowerPos) {
            result.append('.');
            while (p2 >= lowerPos) {
                result.append((char)(48 + this.getDigitPos(p2)));
                --p2;
            }
        }
        result.append('E');
        int _scale = upperPos + this.scale;
        if (_scale < 0) {
            _scale *= -1;
            result.append('-');
        } else {
            result.append('+');
        }
        if (_scale == 0) {
            result.append('0');
        }
        int insertIndex = result.length();
        while (_scale > 0) {
            int quot = _scale / 10;
            int rem = _scale % 10;
            result.insert(insertIndex, (char)(48 + rem));
            _scale = quot;
        }
    }

    protected abstract byte getDigitPos(int var1);

    protected abstract void setDigitPos(int var1, byte var2);

    protected abstract void shiftLeft(int var1);

    protected abstract void shiftRight(int var1);

    protected abstract void setBcdToZero();

    protected abstract void readIntToBcd(int var1);

    protected abstract void readLongToBcd(long var1);

    protected abstract void readBigIntegerToBcd(BigInteger var1);

    protected abstract BigDecimal bcdToBigDecimal();

    protected abstract void copyBcdFrom(DecimalQuantity var1);

    protected abstract void compact();
}

