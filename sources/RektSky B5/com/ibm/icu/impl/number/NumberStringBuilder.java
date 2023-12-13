/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.text.NumberFormat;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NumberStringBuilder
implements CharSequence {
    public static final NumberStringBuilder EMPTY = new NumberStringBuilder();
    private char[] chars;
    private NumberFormat.Field[] fields;
    private int zero;
    private int length;
    private static final Map<NumberFormat.Field, Character> fieldToDebugChar = new HashMap<NumberFormat.Field, Character>();

    public NumberStringBuilder() {
        this(40);
    }

    public NumberStringBuilder(int capacity) {
        this.chars = new char[capacity];
        this.fields = new NumberFormat.Field[capacity];
        this.zero = capacity / 2;
        this.length = 0;
    }

    public NumberStringBuilder(NumberStringBuilder source) {
        this.copyFrom(source);
    }

    public void copyFrom(NumberStringBuilder source) {
        this.chars = Arrays.copyOf(source.chars, source.chars.length);
        this.fields = Arrays.copyOf(source.fields, source.fields.length);
        this.zero = source.zero;
        this.length = source.length;
    }

    @Override
    public int length() {
        return this.length;
    }

    public int codePointCount() {
        return Character.codePointCount(this, 0, this.length());
    }

    @Override
    public char charAt(int index) {
        assert (index >= 0);
        assert (index < this.length);
        return this.chars[this.zero + index];
    }

    public NumberFormat.Field fieldAt(int index) {
        assert (index >= 0);
        assert (index < this.length);
        return this.fields[this.zero + index];
    }

    public int getFirstCodePoint() {
        if (this.length == 0) {
            return -1;
        }
        return Character.codePointAt(this.chars, this.zero, this.zero + this.length);
    }

    public int getLastCodePoint() {
        if (this.length == 0) {
            return -1;
        }
        return Character.codePointBefore(this.chars, this.zero + this.length, this.zero);
    }

    public int codePointAt(int index) {
        return Character.codePointAt(this.chars, this.zero + index, this.zero + this.length);
    }

    public int codePointBefore(int index) {
        return Character.codePointBefore(this.chars, this.zero + index, this.zero);
    }

    public NumberStringBuilder clear() {
        this.zero = this.getCapacity() / 2;
        this.length = 0;
        return this;
    }

    public int appendCodePoint(int codePoint, NumberFormat.Field field) {
        return this.insertCodePoint(this.length, codePoint, field);
    }

    public int insertCodePoint(int index, int codePoint, NumberFormat.Field field) {
        int count = Character.charCount(codePoint);
        int position = this.prepareForInsert(index, count);
        Character.toChars(codePoint, this.chars, position);
        this.fields[position] = field;
        if (count == 2) {
            this.fields[position + 1] = field;
        }
        return count;
    }

    public int append(CharSequence sequence, NumberFormat.Field field) {
        return this.insert(this.length, sequence, field);
    }

    public int insert(int index, CharSequence sequence, NumberFormat.Field field) {
        if (sequence.length() == 0) {
            return 0;
        }
        if (sequence.length() == 1) {
            return this.insertCodePoint(index, sequence.charAt(0), field);
        }
        return this.insert(index, sequence, 0, sequence.length(), field);
    }

    public int insert(int index, CharSequence sequence, int start, int end, NumberFormat.Field field) {
        int count = end - start;
        int position = this.prepareForInsert(index, count);
        for (int i2 = 0; i2 < count; ++i2) {
            this.chars[position + i2] = sequence.charAt(start + i2);
            this.fields[position + i2] = field;
        }
        return count;
    }

    public int splice(int startThis, int endThis, CharSequence sequence, int startOther, int endOther, NumberFormat.Field field) {
        int otherLength = endOther - startOther;
        int thisLength = endThis - startThis;
        int count = otherLength - thisLength;
        int position = count > 0 ? this.prepareForInsert(startThis, count) : this.remove(startThis, -count);
        for (int i2 = 0; i2 < otherLength; ++i2) {
            this.chars[position + i2] = sequence.charAt(startOther + i2);
            this.fields[position + i2] = field;
        }
        return count;
    }

    public int append(char[] chars, NumberFormat.Field[] fields) {
        return this.insert(this.length, chars, fields);
    }

    public int insert(int index, char[] chars, NumberFormat.Field[] fields) {
        assert (fields == null || chars.length == fields.length);
        int count = chars.length;
        if (count == 0) {
            return 0;
        }
        int position = this.prepareForInsert(index, count);
        for (int i2 = 0; i2 < count; ++i2) {
            this.chars[position + i2] = chars[i2];
            this.fields[position + i2] = fields == null ? null : fields[i2];
        }
        return count;
    }

    public int append(NumberStringBuilder other) {
        return this.insert(this.length, other);
    }

    public int insert(int index, NumberStringBuilder other) {
        if (this == other) {
            throw new IllegalArgumentException("Cannot call insert/append on myself");
        }
        int count = other.length;
        if (count == 0) {
            return 0;
        }
        int position = this.prepareForInsert(index, count);
        for (int i2 = 0; i2 < count; ++i2) {
            this.chars[position + i2] = other.charAt(i2);
            this.fields[position + i2] = other.fieldAt(i2);
        }
        return count;
    }

    private int prepareForInsert(int index, int count) {
        if (index == 0 && this.zero - count >= 0) {
            this.zero -= count;
            this.length += count;
            return this.zero;
        }
        if (index == this.length && this.zero + this.length + count < this.getCapacity()) {
            this.length += count;
            return this.zero + this.length - count;
        }
        return this.prepareForInsertHelper(index, count);
    }

    private int prepareForInsertHelper(int index, int count) {
        int oldCapacity = this.getCapacity();
        int oldZero = this.zero;
        char[] oldChars = this.chars;
        NumberFormat.Field[] oldFields = this.fields;
        if (this.length + count > oldCapacity) {
            int newCapacity = (this.length + count) * 2;
            int newZero = newCapacity / 2 - (this.length + count) / 2;
            char[] newChars = new char[newCapacity];
            NumberFormat.Field[] newFields = new NumberFormat.Field[newCapacity];
            System.arraycopy(oldChars, oldZero, newChars, newZero, index);
            System.arraycopy(oldChars, oldZero + index, newChars, newZero + index + count, this.length - index);
            System.arraycopy(oldFields, oldZero, newFields, newZero, index);
            System.arraycopy(oldFields, oldZero + index, newFields, newZero + index + count, this.length - index);
            this.chars = newChars;
            this.fields = newFields;
            this.zero = newZero;
            this.length += count;
        } else {
            int newZero = oldCapacity / 2 - (this.length + count) / 2;
            System.arraycopy(oldChars, oldZero, oldChars, newZero, this.length);
            System.arraycopy(oldChars, newZero + index, oldChars, newZero + index + count, this.length - index);
            System.arraycopy(oldFields, oldZero, oldFields, newZero, this.length);
            System.arraycopy(oldFields, newZero + index, oldFields, newZero + index + count, this.length - index);
            this.zero = newZero;
            this.length += count;
        }
        return this.zero + index;
    }

    private int remove(int index, int count) {
        int position = index + this.zero;
        System.arraycopy(this.chars, position + count, this.chars, position, this.length - index - count);
        System.arraycopy(this.fields, position + count, this.fields, position, this.length - index - count);
        this.length -= count;
        return position;
    }

    private int getCapacity() {
        return this.chars.length;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end > this.length || end < start) {
            throw new IndexOutOfBoundsException();
        }
        NumberStringBuilder other = new NumberStringBuilder(this);
        other.zero = this.zero + start;
        other.length = end - start;
        return other;
    }

    @Override
    public String toString() {
        return new String(this.chars, this.zero, this.length);
    }

    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<NumberStringBuilder [");
        sb.append(this.toString());
        sb.append("] [");
        for (int i2 = this.zero; i2 < this.zero + this.length; ++i2) {
            if (this.fields[i2] == null) {
                sb.append('n');
                continue;
            }
            sb.append(fieldToDebugChar.get(this.fields[i2]));
        }
        sb.append("]>");
        return sb.toString();
    }

    public char[] toCharArray() {
        return Arrays.copyOfRange(this.chars, this.zero, this.zero + this.length);
    }

    public NumberFormat.Field[] toFieldArray() {
        return Arrays.copyOfRange(this.fields, this.zero, this.zero + this.length);
    }

    public boolean contentEquals(char[] chars, NumberFormat.Field[] fields) {
        if (chars.length != this.length) {
            return false;
        }
        if (fields.length != this.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.length; ++i2) {
            if (this.chars[this.zero + i2] != chars[i2]) {
                return false;
            }
            if (this.fields[this.zero + i2] == fields[i2]) continue;
            return false;
        }
        return true;
    }

    public boolean contentEquals(NumberStringBuilder other) {
        if (this.length != other.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.length; ++i2) {
            if (this.charAt(i2) == other.charAt(i2) && this.fieldAt(i2) == other.fieldAt(i2)) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Don't call #hashCode() or #equals() on a mutable.");
    }

    public boolean equals(Object other) {
        throw new UnsupportedOperationException("Don't call #hashCode() or #equals() on a mutable.");
    }

    public boolean nextFieldPosition(FieldPosition fp) {
        Format.Field rawField = fp.getFieldAttribute();
        if (rawField == null) {
            if (fp.getField() == 0) {
                rawField = NumberFormat.Field.INTEGER;
            } else if (fp.getField() == 1) {
                rawField = NumberFormat.Field.FRACTION;
            } else {
                return false;
            }
        }
        if (!(rawField instanceof NumberFormat.Field)) {
            throw new IllegalArgumentException("You must pass an instance of com.ibm.icu.text.NumberFormat.Field as your FieldPosition attribute.  You passed: " + rawField.getClass().toString());
        }
        NumberFormat.Field field = (NumberFormat.Field)rawField;
        boolean seenStart = false;
        int fractionStart = -1;
        int startIndex = fp.getEndIndex();
        for (int i2 = this.zero + startIndex; i2 <= this.zero + this.length; ++i2) {
            NumberFormat.Field _field;
            NumberFormat.Field field2 = _field = i2 < this.zero + this.length ? this.fields[i2] : null;
            if (seenStart && field != _field) {
                if (field == NumberFormat.Field.INTEGER && _field == NumberFormat.Field.GROUPING_SEPARATOR) continue;
                fp.setEndIndex(i2 - this.zero);
                break;
            }
            if (!seenStart && field == _field) {
                fp.setBeginIndex(i2 - this.zero);
                seenStart = true;
            }
            if (_field != NumberFormat.Field.INTEGER && _field != NumberFormat.Field.DECIMAL_SEPARATOR) continue;
            fractionStart = i2 - this.zero + 1;
        }
        if (field == NumberFormat.Field.FRACTION && !seenStart && fractionStart != -1) {
            fp.setBeginIndex(fractionStart);
            fp.setEndIndex(fractionStart);
        }
        return seenStart;
    }

    public AttributedCharacterIterator toCharacterIterator() {
        AttributedString as = new AttributedString(this.toString());
        NumberFormat.Field current = null;
        int currentStart = -1;
        for (int i2 = 0; i2 < this.length; ++i2) {
            NumberFormat.Field field = this.fields[i2 + this.zero];
            if (current == NumberFormat.Field.INTEGER && field == NumberFormat.Field.GROUPING_SEPARATOR) {
                as.addAttribute(NumberFormat.Field.GROUPING_SEPARATOR, NumberFormat.Field.GROUPING_SEPARATOR, i2, i2 + 1);
                continue;
            }
            if (current == field) continue;
            if (current != null) {
                as.addAttribute(current, current, currentStart, i2);
            }
            current = field;
            currentStart = i2;
        }
        if (current != null) {
            as.addAttribute(current, current, currentStart, this.length);
        }
        return as.getIterator();
    }

    static {
        fieldToDebugChar.put(NumberFormat.Field.SIGN, Character.valueOf('-'));
        fieldToDebugChar.put(NumberFormat.Field.INTEGER, Character.valueOf('i'));
        fieldToDebugChar.put(NumberFormat.Field.FRACTION, Character.valueOf('f'));
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT, Character.valueOf('e'));
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT_SIGN, Character.valueOf('+'));
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT_SYMBOL, Character.valueOf('E'));
        fieldToDebugChar.put(NumberFormat.Field.DECIMAL_SEPARATOR, Character.valueOf('.'));
        fieldToDebugChar.put(NumberFormat.Field.GROUPING_SEPARATOR, Character.valueOf(','));
        fieldToDebugChar.put(NumberFormat.Field.PERCENT, Character.valueOf('%'));
        fieldToDebugChar.put(NumberFormat.Field.PERMILLE, Character.valueOf('\u2030'));
        fieldToDebugChar.put(NumberFormat.Field.CURRENCY, Character.valueOf('$'));
    }
}

