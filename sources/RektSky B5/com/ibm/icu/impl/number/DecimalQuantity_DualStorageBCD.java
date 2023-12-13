/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.DecimalQuantity_AbstractBCD;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class DecimalQuantity_DualStorageBCD
extends DecimalQuantity_AbstractBCD {
    private byte[] bcdBytes;
    private long bcdLong = 0L;
    private boolean usingBytes = false;

    @Override
    public int maxRepresentableDigits() {
        return Integer.MAX_VALUE;
    }

    public DecimalQuantity_DualStorageBCD() {
        this.setBcdToZero();
        this.flags = 0;
    }

    public DecimalQuantity_DualStorageBCD(long input) {
        this.setToLong(input);
    }

    public DecimalQuantity_DualStorageBCD(int input) {
        this.setToInt(input);
    }

    public DecimalQuantity_DualStorageBCD(double input) {
        this.setToDouble(input);
    }

    public DecimalQuantity_DualStorageBCD(BigInteger input) {
        this.setToBigInteger(input);
    }

    public DecimalQuantity_DualStorageBCD(BigDecimal input) {
        this.setToBigDecimal(input);
    }

    public DecimalQuantity_DualStorageBCD(DecimalQuantity_DualStorageBCD other) {
        this.copyFrom(other);
    }

    public DecimalQuantity_DualStorageBCD(Number number) {
        if (number instanceof Long) {
            this.setToLong(number.longValue());
        } else if (number instanceof Integer) {
            this.setToInt(number.intValue());
        } else if (number instanceof Float) {
            this.setToDouble(number.doubleValue());
        } else if (number instanceof Double) {
            this.setToDouble(number.doubleValue());
        } else if (number instanceof BigInteger) {
            this.setToBigInteger((BigInteger)number);
        } else if (number instanceof BigDecimal) {
            this.setToBigDecimal((BigDecimal)number);
        } else if (number instanceof com.ibm.icu.math.BigDecimal) {
            this.setToBigDecimal(((com.ibm.icu.math.BigDecimal)number).toBigDecimal());
        } else {
            throw new IllegalArgumentException("Number is of an unsupported type: " + number.getClass().getName());
        }
    }

    @Override
    public DecimalQuantity createCopy() {
        return new DecimalQuantity_DualStorageBCD(this);
    }

    @Override
    protected byte getDigitPos(int position) {
        if (this.usingBytes) {
            if (position < 0 || position >= this.precision) {
                return 0;
            }
            return this.bcdBytes[position];
        }
        if (position < 0 || position >= 16) {
            return 0;
        }
        return (byte)(this.bcdLong >>> position * 4 & 0xFL);
    }

    @Override
    protected void setDigitPos(int position, byte value) {
        assert (position >= 0);
        if (this.usingBytes) {
            this.ensureCapacity(position + 1);
            this.bcdBytes[position] = value;
        } else if (position >= 16) {
            this.switchStorage();
            this.ensureCapacity(position + 1);
            this.bcdBytes[position] = value;
        } else {
            int shift = position * 4;
            this.bcdLong = this.bcdLong & (15L << shift ^ 0xFFFFFFFFFFFFFFFFL) | (long)value << shift;
        }
    }

    @Override
    protected void shiftLeft(int numDigits) {
        if (!this.usingBytes && this.precision + numDigits > 16) {
            this.switchStorage();
        }
        if (this.usingBytes) {
            int i2;
            this.ensureCapacity(this.precision + numDigits);
            for (i2 = this.precision + numDigits - 1; i2 >= numDigits; --i2) {
                this.bcdBytes[i2] = this.bcdBytes[i2 - numDigits];
            }
            while (i2 >= 0) {
                this.bcdBytes[i2] = 0;
                --i2;
            }
        } else {
            this.bcdLong <<= numDigits * 4;
        }
        this.scale -= numDigits;
        this.precision += numDigits;
    }

    @Override
    protected void shiftRight(int numDigits) {
        if (this.usingBytes) {
            int i2;
            for (i2 = 0; i2 < this.precision - numDigits; ++i2) {
                this.bcdBytes[i2] = this.bcdBytes[i2 + numDigits];
            }
            while (i2 < this.precision) {
                this.bcdBytes[i2] = 0;
                ++i2;
            }
        } else {
            this.bcdLong >>>= numDigits * 4;
        }
        this.scale += numDigits;
        this.precision -= numDigits;
    }

    @Override
    protected void setBcdToZero() {
        if (this.usingBytes) {
            this.bcdBytes = null;
            this.usingBytes = false;
        }
        this.bcdLong = 0L;
        this.scale = 0;
        this.precision = 0;
        this.isApproximate = false;
        this.origDouble = 0.0;
        this.origDelta = 0;
    }

    @Override
    protected void readIntToBcd(int n2) {
        assert (n2 != 0);
        long result = 0L;
        int i2 = 16;
        while (n2 != 0) {
            result = (result >>> 4) + ((long)n2 % 10L << 60);
            n2 /= 10;
            --i2;
        }
        assert (!this.usingBytes);
        this.bcdLong = result >>> i2 * 4;
        this.scale = 0;
        this.precision = 16 - i2;
    }

    @Override
    protected void readLongToBcd(long n2) {
        assert (n2 != 0L);
        if (n2 >= 10000000000000000L) {
            this.ensureCapacity();
            int i2 = 0;
            while (n2 != 0L) {
                this.bcdBytes[i2] = (byte)(n2 % 10L);
                n2 /= 10L;
                ++i2;
            }
            assert (this.usingBytes);
            this.scale = 0;
            this.precision = i2;
        } else {
            long result = 0L;
            int i3 = 16;
            while (n2 != 0L) {
                result = (result >>> 4) + (n2 % 10L << 60);
                n2 /= 10L;
                --i3;
            }
            assert (i3 >= 0);
            assert (!this.usingBytes);
            this.bcdLong = result >>> i3 * 4;
            this.scale = 0;
            this.precision = 16 - i3;
        }
    }

    @Override
    protected void readBigIntegerToBcd(BigInteger n2) {
        assert (n2.signum() != 0);
        this.ensureCapacity();
        int i2 = 0;
        while (n2.signum() != 0) {
            BigInteger[] temp = n2.divideAndRemainder(BigInteger.TEN);
            this.ensureCapacity(i2 + 1);
            this.bcdBytes[i2] = temp[1].byteValue();
            n2 = temp[0];
            ++i2;
        }
        this.scale = 0;
        this.precision = i2;
    }

    @Override
    protected BigDecimal bcdToBigDecimal() {
        if (this.usingBytes) {
            BigDecimal result = new BigDecimal(this.toNumberString());
            if (this.isNegative()) {
                result = result.negate();
            }
            return result;
        }
        long tempLong = 0L;
        for (int shift = this.precision - 1; shift >= 0; --shift) {
            tempLong = tempLong * 10L + (long)this.getDigitPos(shift);
        }
        BigDecimal result = BigDecimal.valueOf(tempLong);
        result = result.scaleByPowerOfTen(this.scale);
        if (this.isNegative()) {
            result = result.negate();
        }
        return result;
    }

    @Override
    protected void compact() {
        if (this.usingBytes) {
            int leading;
            int delta;
            for (delta = 0; delta < this.precision && this.bcdBytes[delta] == 0; ++delta) {
            }
            if (delta == this.precision) {
                this.setBcdToZero();
                return;
            }
            this.shiftRight(delta);
            for (leading = this.precision - 1; leading >= 0 && this.bcdBytes[leading] == 0; --leading) {
            }
            this.precision = leading + 1;
            if (this.precision <= 16) {
                this.switchStorage();
            }
        } else {
            if (this.bcdLong == 0L) {
                this.setBcdToZero();
                return;
            }
            int delta = Long.numberOfTrailingZeros(this.bcdLong) / 4;
            this.bcdLong >>>= delta * 4;
            this.scale += delta;
            this.precision = 16 - Long.numberOfLeadingZeros(this.bcdLong) / 4;
        }
    }

    private void ensureCapacity() {
        this.ensureCapacity(40);
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity;
        if (capacity == 0) {
            return;
        }
        int n2 = oldCapacity = this.usingBytes ? this.bcdBytes.length : 0;
        if (!this.usingBytes) {
            this.bcdBytes = new byte[capacity];
        } else if (oldCapacity < capacity) {
            byte[] bcd1 = new byte[capacity * 2];
            System.arraycopy(this.bcdBytes, 0, bcd1, 0, oldCapacity);
            this.bcdBytes = bcd1;
        }
        this.usingBytes = true;
    }

    private void switchStorage() {
        if (this.usingBytes) {
            this.bcdLong = 0L;
            for (int i2 = this.precision - 1; i2 >= 0; --i2) {
                this.bcdLong <<= 4;
                this.bcdLong |= (long)this.bcdBytes[i2];
            }
            this.bcdBytes = null;
            this.usingBytes = false;
        } else {
            this.ensureCapacity();
            for (int i3 = 0; i3 < this.precision; ++i3) {
                this.bcdBytes[i3] = (byte)(this.bcdLong & 0xFL);
                this.bcdLong >>>= 4;
            }
            assert (this.usingBytes);
        }
    }

    @Override
    protected void copyBcdFrom(DecimalQuantity _other) {
        DecimalQuantity_DualStorageBCD other = (DecimalQuantity_DualStorageBCD)_other;
        this.setBcdToZero();
        if (other.usingBytes) {
            this.ensureCapacity(other.precision);
            System.arraycopy(other.bcdBytes, 0, this.bcdBytes, 0, other.precision);
        } else {
            this.bcdLong = other.bcdLong;
        }
    }

    @Deprecated
    public String checkHealth() {
        if (this.usingBytes) {
            int i2;
            if (this.bcdLong != 0L) {
                return "Value in bcdLong but we are in byte mode";
            }
            if (this.precision == 0) {
                return "Zero precision but we are in byte mode";
            }
            if (this.precision > this.bcdBytes.length) {
                return "Precision exceeds length of byte array";
            }
            if (this.getDigitPos(this.precision - 1) == 0) {
                return "Most significant digit is zero in byte mode";
            }
            if (this.getDigitPos(0) == 0) {
                return "Least significant digit is zero in long mode";
            }
            for (i2 = 0; i2 < this.precision; ++i2) {
                if (this.getDigitPos(i2) >= 10) {
                    return "Digit exceeding 10 in byte array";
                }
                if (this.getDigitPos(i2) >= 0) continue;
                return "Digit below 0 in byte array";
            }
            for (i2 = this.precision; i2 < this.bcdBytes.length; ++i2) {
                if (this.getDigitPos(i2) == 0) continue;
                return "Nonzero digits outside of range in byte array";
            }
        } else {
            int i3;
            if (this.bcdBytes != null) {
                for (i3 = 0; i3 < this.bcdBytes.length; ++i3) {
                    if (this.bcdBytes[i3] == 0) continue;
                    return "Nonzero digits in byte array but we are in long mode";
                }
            }
            if (this.precision == 0 && this.bcdLong != 0L) {
                return "Value in bcdLong even though precision is zero";
            }
            if (this.precision > 16) {
                return "Precision exceeds length of long";
            }
            if (this.precision != 0 && this.getDigitPos(this.precision - 1) == 0) {
                return "Most significant digit is zero in long mode";
            }
            if (this.precision != 0 && this.getDigitPos(0) == 0) {
                return "Least significant digit is zero in long mode";
            }
            for (i3 = 0; i3 < this.precision; ++i3) {
                if (this.getDigitPos(i3) >= 10) {
                    return "Digit exceeding 10 in long";
                }
                if (this.getDigitPos(i3) >= 0) continue;
                return "Digit below 0 in long (?!)";
            }
            for (i3 = this.precision; i3 < 16; ++i3) {
                if (this.getDigitPos(i3) == 0) continue;
                return "Nonzero digits outside of range in long";
            }
        }
        return null;
    }

    @Deprecated
    public boolean isUsingBytes() {
        return this.usingBytes;
    }

    public String toString() {
        return String.format("<DecimalQuantity %s:%d:%d:%s %s %s%s>", this.lOptPos > 1000 ? "999" : String.valueOf(this.lOptPos), this.lReqPos, this.rReqPos, this.rOptPos < -1000 ? "-999" : String.valueOf(this.rOptPos), this.usingBytes ? "bytes" : "long", this.isNegative() ? "-" : "", this.toNumberString());
    }

    private String toNumberString() {
        StringBuilder sb = new StringBuilder();
        if (this.usingBytes) {
            if (this.precision == 0) {
                sb.append('0');
            }
            for (int i2 = this.precision - 1; i2 >= 0; --i2) {
                sb.append(this.bcdBytes[i2]);
            }
        } else {
            sb.append(Long.toHexString(this.bcdLong));
        }
        sb.append("E");
        sb.append(this.scale);
        return sb.toString();
    }
}

