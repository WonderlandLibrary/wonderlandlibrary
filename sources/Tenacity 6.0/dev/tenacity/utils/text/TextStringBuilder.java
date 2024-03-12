// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import dev.tenacity.utils.text.matcher.StringMatcher;
import java.io.Reader;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.nio.CharBuffer;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;
import java.io.Serializable;

public class TextStringBuilder implements CharSequence, Appendable, Serializable, Builder<String>
{
    private static final char SPACE = ' ';
    static final int CAPACITY = 32;
    private static final int EOS = -1;
    private static final int FALSE_STRING_SIZE;
    private static final long serialVersionUID = 1L;
    private static final int TRUE_STRING_SIZE;
    private char[] buffer;
    private String newLine;
    private String nullText;
    private int reallocations;
    private int size;
    
    public static TextStringBuilder wrap(final char[] initialBuffer) {
        Objects.requireNonNull(initialBuffer, "initialBuffer");
        return new TextStringBuilder(initialBuffer, initialBuffer.length);
    }
    
    public static TextStringBuilder wrap(final char[] initialBuffer, final int length) {
        return new TextStringBuilder(initialBuffer, length);
    }
    
    public TextStringBuilder() {
        this(32);
    }
    
    private TextStringBuilder(final char[] initialBuffer, final int length) {
        this.buffer = Objects.requireNonNull(initialBuffer, "initialBuffer");
        if (length < 0 || length > initialBuffer.length) {
            throw new IllegalArgumentException("initialBuffer.length=" + initialBuffer.length + ", length=" + length);
        }
        this.size = length;
    }
    
    public TextStringBuilder(final CharSequence seq) {
        this(StringUtils.length(seq) + 32);
        if (seq != null) {
            this.append(seq);
        }
    }
    
    public TextStringBuilder(final int initialCapacity) {
        this.buffer = new char[(initialCapacity <= 0) ? 32 : initialCapacity];
    }
    
    public TextStringBuilder(final String str) {
        this(StringUtils.length((CharSequence)str) + 32);
        if (str != null) {
            this.append(str);
        }
    }
    
    public TextStringBuilder append(final boolean value) {
        if (value) {
            this.ensureCapacity(this.size + TextStringBuilder.TRUE_STRING_SIZE);
            this.appendTrue(this.size);
        }
        else {
            this.ensureCapacity(this.size + TextStringBuilder.FALSE_STRING_SIZE);
            this.appendFalse(this.size);
        }
        return this;
    }
    
    @Override
    public TextStringBuilder append(final char ch) {
        final int len = this.length();
        this.ensureCapacity(len + 1);
        this.buffer[this.size++] = ch;
        return this;
    }
    
    public TextStringBuilder append(final char[] chars) {
        if (chars == null) {
            return this.appendNull();
        }
        final int strLen = chars.length;
        if (strLen > 0) {
            final int len = this.length();
            this.ensureCapacity(len + strLen);
            System.arraycopy(chars, 0, this.buffer, len, strLen);
            this.size += strLen;
        }
        return this;
    }
    
    public TextStringBuilder append(final char[] chars, final int startIndex, final int length) {
        if (chars == null) {
            return this.appendNull();
        }
        if (startIndex < 0 || startIndex > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length);
        }
        if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            final int len = this.length();
            this.ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, this.buffer, len, length);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder append(final CharBuffer str) {
        return this.append(str, 0, StringUtils.length((CharSequence)str));
    }
    
    public TextStringBuilder append(final CharBuffer buf, final int startIndex, final int length) {
        if (buf == null) {
            return this.appendNull();
        }
        if (buf.hasArray()) {
            final int totalLength = buf.remaining();
            if (startIndex < 0 || startIndex > totalLength) {
                throw new StringIndexOutOfBoundsException("startIndex must be valid");
            }
            if (length < 0 || startIndex + length > totalLength) {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
            final int len = this.length();
            this.ensureCapacity(len + length);
            System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, this.buffer, len, length);
            this.size += length;
        }
        else {
            this.append(buf.toString(), startIndex, length);
        }
        return this;
    }
    
    @Override
    public TextStringBuilder append(final CharSequence seq) {
        if (seq == null) {
            return this.appendNull();
        }
        if (seq instanceof TextStringBuilder) {
            return this.append((TextStringBuilder)seq);
        }
        if (seq instanceof StringBuilder) {
            return this.append((StringBuilder)seq);
        }
        if (seq instanceof StringBuffer) {
            return this.append((StringBuffer)seq);
        }
        if (seq instanceof CharBuffer) {
            return this.append((CharBuffer)seq);
        }
        return this.append(seq.toString());
    }
    
    @Override
    public TextStringBuilder append(final CharSequence seq, final int startIndex, final int endIndex) {
        if (seq == null) {
            return this.appendNull();
        }
        if (endIndex <= 0) {
            throw new StringIndexOutOfBoundsException("endIndex must be valid");
        }
        if (startIndex >= endIndex) {
            throw new StringIndexOutOfBoundsException("endIndex must be greater than startIndex");
        }
        return this.append(seq.toString(), startIndex, endIndex - startIndex);
    }
    
    public TextStringBuilder append(final double value) {
        return this.append(String.valueOf(value));
    }
    
    public TextStringBuilder append(final float value) {
        return this.append(String.valueOf(value));
    }
    
    public TextStringBuilder append(final int value) {
        return this.append(String.valueOf(value));
    }
    
    public TextStringBuilder append(final long value) {
        return this.append(String.valueOf(value));
    }
    
    public TextStringBuilder append(final Object obj) {
        if (obj == null) {
            return this.appendNull();
        }
        if (obj instanceof CharSequence) {
            return this.append((CharSequence)obj);
        }
        return this.append(obj.toString());
    }
    
    public TextStringBuilder append(final String str) {
        return this.append(str, 0, StringUtils.length((CharSequence)str));
    }
    
    public TextStringBuilder append(final String str, final int startIndex, final int length) {
        if (str == null) {
            return this.appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = this.length();
            this.ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder append(final String format, final Object... objs) {
        return this.append(String.format(format, objs));
    }
    
    public TextStringBuilder append(final StringBuffer str) {
        return this.append(str, 0, StringUtils.length((CharSequence)str));
    }
    
    public TextStringBuilder append(final StringBuffer str, final int startIndex, final int length) {
        if (str == null) {
            return this.appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = this.length();
            this.ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder append(final StringBuilder str) {
        return this.append(str, 0, StringUtils.length((CharSequence)str));
    }
    
    public TextStringBuilder append(final StringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return this.appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = this.length();
            this.ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder append(final TextStringBuilder str) {
        return this.append(str, 0, StringUtils.length((CharSequence)str));
    }
    
    public TextStringBuilder append(final TextStringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return this.appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = this.length();
            this.ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder appendAll(final Iterable<?> iterable) {
        if (iterable != null) {
            for (final Object o : iterable) {
                this.append(o);
            }
        }
        return this;
    }
    
    public TextStringBuilder appendAll(final Iterator<?> it) {
        if (it != null) {
            while (it.hasNext()) {
                this.append(it.next());
            }
        }
        return this;
    }
    
    public <T> TextStringBuilder appendAll(final T... array) {
        if (array != null && array.length > 0) {
            for (final Object element : array) {
                this.append(element);
            }
        }
        return this;
    }
    
    private void appendFalse(int index) {
        this.buffer[index++] = 'f';
        this.buffer[index++] = 'a';
        this.buffer[index++] = 'l';
        this.buffer[index++] = 's';
        this.buffer[index] = 'e';
        this.size += TextStringBuilder.FALSE_STRING_SIZE;
    }
    
    public TextStringBuilder appendFixedWidthPadLeft(final int value, final int width, final char padChar) {
        return this.appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }
    
    public TextStringBuilder appendFixedWidthPadLeft(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            this.ensureCapacity(this.size + width);
            String str = (obj == null) ? this.getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, this.buffer, this.size);
            }
            else {
                final int padLen = width - strLen;
                for (int i = 0; i < padLen; ++i) {
                    this.buffer[this.size + i] = padChar;
                }
                str.getChars(0, strLen, this.buffer, this.size + padLen);
            }
            this.size += width;
        }
        return this;
    }
    
    public TextStringBuilder appendFixedWidthPadRight(final int value, final int width, final char padChar) {
        return this.appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }
    
    public TextStringBuilder appendFixedWidthPadRight(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            this.ensureCapacity(this.size + width);
            String str = (obj == null) ? this.getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, width, this.buffer, this.size);
            }
            else {
                final int padLen = width - strLen;
                str.getChars(0, strLen, this.buffer, this.size);
                for (int i = 0; i < padLen; ++i) {
                    this.buffer[this.size + strLen + i] = padChar;
                }
            }
            this.size += width;
        }
        return this;
    }
    
    public TextStringBuilder appendln(final boolean value) {
        return this.append(value).appendNewLine();
    }
    
    public TextStringBuilder appendln(final char ch) {
        return this.append(ch).appendNewLine();
    }
    
    public TextStringBuilder appendln(final char[] chars) {
        return this.append(chars).appendNewLine();
    }
    
    public TextStringBuilder appendln(final char[] chars, final int startIndex, final int length) {
        return this.append(chars, startIndex, length).appendNewLine();
    }
    
    public TextStringBuilder appendln(final double value) {
        return this.append(value).appendNewLine();
    }
    
    public TextStringBuilder appendln(final float value) {
        return this.append(value).appendNewLine();
    }
    
    public TextStringBuilder appendln(final int value) {
        return this.append(value).appendNewLine();
    }
    
    public TextStringBuilder appendln(final long value) {
        return this.append(value).appendNewLine();
    }
    
    public TextStringBuilder appendln(final Object obj) {
        return this.append(obj).appendNewLine();
    }
    
    public TextStringBuilder appendln(final String str) {
        return this.append(str).appendNewLine();
    }
    
    public TextStringBuilder appendln(final String str, final int startIndex, final int length) {
        return this.append(str, startIndex, length).appendNewLine();
    }
    
    public TextStringBuilder appendln(final String format, final Object... objs) {
        return this.append(format, objs).appendNewLine();
    }
    
    public TextStringBuilder appendln(final StringBuffer str) {
        return this.append(str).appendNewLine();
    }
    
    public TextStringBuilder appendln(final StringBuffer str, final int startIndex, final int length) {
        return this.append(str, startIndex, length).appendNewLine();
    }
    
    public TextStringBuilder appendln(final StringBuilder str) {
        return this.append(str).appendNewLine();
    }
    
    public TextStringBuilder appendln(final StringBuilder str, final int startIndex, final int length) {
        return this.append(str, startIndex, length).appendNewLine();
    }
    
    public TextStringBuilder appendln(final TextStringBuilder str) {
        return this.append(str).appendNewLine();
    }
    
    public TextStringBuilder appendln(final TextStringBuilder str, final int startIndex, final int length) {
        return this.append(str, startIndex, length).appendNewLine();
    }
    
    public TextStringBuilder appendNewLine() {
        if (this.newLine == null) {
            this.append(System.lineSeparator());
            return this;
        }
        return this.append(this.newLine);
    }
    
    public TextStringBuilder appendNull() {
        if (this.nullText == null) {
            return this;
        }
        return this.append(this.nullText);
    }
    
    public TextStringBuilder appendPadding(final int length, final char padChar) {
        if (length >= 0) {
            this.ensureCapacity(this.size + length);
            for (int i = 0; i < length; ++i) {
                this.buffer[this.size++] = padChar;
            }
        }
        return this;
    }
    
    public TextStringBuilder appendSeparator(final char separator) {
        if (this.isNotEmpty()) {
            this.append(separator);
        }
        return this;
    }
    
    public TextStringBuilder appendSeparator(final char standard, final char defaultIfEmpty) {
        if (this.isEmpty()) {
            this.append(defaultIfEmpty);
        }
        else {
            this.append(standard);
        }
        return this;
    }
    
    public TextStringBuilder appendSeparator(final char separator, final int loopIndex) {
        if (loopIndex > 0) {
            this.append(separator);
        }
        return this;
    }
    
    public TextStringBuilder appendSeparator(final String separator) {
        return this.appendSeparator(separator, null);
    }
    
    public TextStringBuilder appendSeparator(final String separator, final int loopIndex) {
        if (separator != null && loopIndex > 0) {
            this.append(separator);
        }
        return this;
    }
    
    public TextStringBuilder appendSeparator(final String standard, final String defaultIfEmpty) {
        final String str = this.isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            this.append(str);
        }
        return this;
    }
    
    public void appendTo(final Appendable appendable) throws IOException {
        if (appendable instanceof Writer) {
            ((Writer)appendable).write(this.buffer, 0, this.size);
        }
        else if (appendable instanceof StringBuilder) {
            ((StringBuilder)appendable).append(this.buffer, 0, this.size);
        }
        else if (appendable instanceof StringBuffer) {
            ((StringBuffer)appendable).append(this.buffer, 0, this.size);
        }
        else if (appendable instanceof CharBuffer) {
            ((CharBuffer)appendable).put(this.buffer, 0, this.size);
        }
        else {
            appendable.append(this);
        }
    }
    
    private void appendTrue(int index) {
        this.buffer[index++] = 't';
        this.buffer[index++] = 'r';
        this.buffer[index++] = 'u';
        this.buffer[index] = 'e';
        this.size += TextStringBuilder.TRUE_STRING_SIZE;
    }
    
    public TextStringBuilder appendWithSeparators(final Iterable<?> iterable, final String separator) {
        if (iterable != null) {
            final String sep = Objects.toString(separator, "");
            final Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                this.append(it.next());
                if (it.hasNext()) {
                    this.append(sep);
                }
            }
        }
        return this;
    }
    
    public TextStringBuilder appendWithSeparators(final Iterator<?> it, final String separator) {
        if (it != null) {
            final String sep = Objects.toString(separator, "");
            while (it.hasNext()) {
                this.append(it.next());
                if (it.hasNext()) {
                    this.append(sep);
                }
            }
        }
        return this;
    }
    
    public TextStringBuilder appendWithSeparators(final Object[] array, final String separator) {
        if (array != null && array.length > 0) {
            final String sep = Objects.toString(separator, "");
            this.append(array[0]);
            for (int i = 1; i < array.length; ++i) {
                this.append(sep);
                this.append(array[i]);
            }
        }
        return this;
    }
    
    public Reader asReader() {
        return new TextStringBuilderReader();
    }
    
    public StringTokenizer asTokenizer() {
        return new TextStringBuilderTokenizer();
    }
    
    public Writer asWriter() {
        return new TextStringBuilderWriter();
    }
    
    @Override
    public String build() {
        return this.toString();
    }
    
    public int capacity() {
        return this.buffer.length;
    }
    
    @Override
    public char charAt(final int index) {
        this.validateIndex(index);
        return this.buffer[index];
    }
    
    public TextStringBuilder clear() {
        this.size = 0;
        return this;
    }
    
    public boolean contains(final char ch) {
        final char[] thisBuf = this.buffer;
        for (int i = 0; i < this.size; ++i) {
            if (thisBuf[i] == ch) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(final String str) {
        return this.indexOf(str, 0) >= 0;
    }
    
    public boolean contains(final StringMatcher matcher) {
        return this.indexOf(matcher, 0) >= 0;
    }
    
    public TextStringBuilder delete(final int startIndex, final int endIndex) {
        final int actualEndIndex = this.validateRange(startIndex, endIndex);
        final int len = actualEndIndex - startIndex;
        if (len > 0) {
            this.deleteImpl(startIndex, actualEndIndex, len);
        }
        return this;
    }
    
    public TextStringBuilder deleteAll(final char ch) {
        for (int i = 0; i < this.size; ++i) {
            if (this.buffer[i] == ch) {
                final int start = i;
                while (++i < this.size && this.buffer[i] == ch) {}
                final int len = i - start;
                this.deleteImpl(start, i, len);
                i -= len;
            }
        }
        return this;
    }
    
    public TextStringBuilder deleteAll(final String str) {
        final int len = (str == null) ? 0 : str.length();
        if (len > 0) {
            for (int index = this.indexOf(str, 0); index >= 0; index = this.indexOf(str, index)) {
                this.deleteImpl(index, index + len, len);
            }
        }
        return this;
    }
    
    public TextStringBuilder deleteAll(final StringMatcher matcher) {
        return this.replace(matcher, null, 0, this.size, -1);
    }
    
    public TextStringBuilder deleteCharAt(final int index) {
        this.validateIndex(index);
        this.deleteImpl(index, index + 1, 1);
        return this;
    }
    
    public TextStringBuilder deleteFirst(final char ch) {
        for (int i = 0; i < this.size; ++i) {
            if (this.buffer[i] == ch) {
                this.deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }
    
    public TextStringBuilder deleteFirst(final String str) {
        final int len = (str == null) ? 0 : str.length();
        if (len > 0) {
            final int index = this.indexOf(str, 0);
            if (index >= 0) {
                this.deleteImpl(index, index + len, len);
            }
        }
        return this;
    }
    
    public TextStringBuilder deleteFirst(final StringMatcher matcher) {
        return this.replace(matcher, null, 0, this.size, 1);
    }
    
    private void deleteImpl(final int startIndex, final int endIndex, final int len) {
        System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
        this.size -= len;
    }
    
    public char drainChar(final int index) {
        this.validateIndex(index);
        final char c = this.buffer[index];
        this.deleteCharAt(index);
        return c;
    }
    
    public int drainChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        final int length = endIndex - startIndex;
        if (this.isEmpty() || length == 0 || target.length == 0) {
            return 0;
        }
        final int actualLen = Math.min(Math.min(this.size, length), target.length - targetIndex);
        this.getChars(startIndex, actualLen, target, targetIndex);
        this.delete(startIndex, actualLen);
        return actualLen;
    }
    
    public boolean endsWith(final String str) {
        if (str == null) {
            return false;
        }
        final int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        for (int pos = this.size - len, i = 0; i < len; ++i, ++pos) {
            if (this.buffer[pos] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    public TextStringBuilder ensureCapacity(final int capacity) {
        if (capacity > this.buffer.length) {
            this.reallocate(capacity * 2);
        }
        return this;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TextStringBuilder && this.equals((TextStringBuilder)obj);
    }
    
    public boolean equals(final TextStringBuilder other) {
        return other != null && Arrays.equals(this.buffer, other.buffer);
    }
    
    public boolean equalsIgnoreCase(final TextStringBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        final char[] thisBuf = this.buffer;
        final char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; --i) {
            final char c1 = thisBuf[i];
            final char c2 = otherBuf[i];
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }
    
    char[] getBuffer() {
        return this.buffer;
    }
    
    public char[] getChars(char[] target) {
        final int len = this.length();
        if (target == null || target.length < len) {
            target = new char[len];
        }
        System.arraycopy(this.buffer, 0, target, 0, len);
        return target;
    }
    
    public void getChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex < 0 || endIndex > this.length()) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        System.arraycopy(this.buffer, startIndex, target, targetIndex, endIndex - startIndex);
    }
    
    public String getNewLineText() {
        return this.newLine;
    }
    
    public String getNullText() {
        return this.nullText;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.buffer);
    }
    
    public int indexOf(final char ch) {
        return this.indexOf(ch, 0);
    }
    
    public int indexOf(final char ch, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (startIndex >= this.size) {
            return -1;
        }
        final char[] thisBuf = this.buffer;
        for (int i = startIndex; i < this.size; ++i) {
            if (thisBuf[i] == ch) {
                return i;
            }
        }
        return -1;
    }
    
    public int indexOf(final String str) {
        return this.indexOf(str, 0);
    }
    
    public int indexOf(final String str, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (str == null || startIndex >= this.size) {
            return -1;
        }
        final int strLen = str.length();
        if (strLen == 1) {
            return this.indexOf(str.charAt(0), startIndex);
        }
        if (strLen == 0) {
            return startIndex;
        }
        if (strLen > this.size) {
            return -1;
        }
        final char[] thisBuf = this.buffer;
        final int len = this.size - strLen + 1;
        int i = startIndex;
    Label_0076:
        while (i < len) {
            for (int j = 0; j < strLen; ++j) {
                if (str.charAt(j) != thisBuf[i + j]) {
                    ++i;
                    continue Label_0076;
                }
            }
            return i;
        }
        return -1;
    }
    
    public int indexOf(final StringMatcher matcher) {
        return this.indexOf(matcher, 0);
    }
    
    public int indexOf(final StringMatcher matcher, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (matcher == null || startIndex >= this.size) {
            return -1;
        }
        final int len = this.size;
        final char[] buf = this.buffer;
        for (int i = startIndex; i < len; ++i) {
            if (matcher.isMatch(buf, i, startIndex, len) > 0) {
                return i;
            }
        }
        return -1;
    }
    
    public TextStringBuilder insert(final int index, final boolean value) {
        this.validateIndex(index);
        if (value) {
            this.ensureCapacity(this.size + TextStringBuilder.TRUE_STRING_SIZE);
            System.arraycopy(this.buffer, index, this.buffer, index + TextStringBuilder.TRUE_STRING_SIZE, this.size - index);
            this.appendTrue(index);
        }
        else {
            this.ensureCapacity(this.size + TextStringBuilder.FALSE_STRING_SIZE);
            System.arraycopy(this.buffer, index, this.buffer, index + TextStringBuilder.FALSE_STRING_SIZE, this.size - index);
            this.appendFalse(index);
        }
        return this;
    }
    
    public TextStringBuilder insert(final int index, final char value) {
        this.validateIndex(index);
        this.ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
        this.buffer[index] = value;
        ++this.size;
        return this;
    }
    
    public TextStringBuilder insert(final int index, final char[] chars) {
        this.validateIndex(index);
        if (chars == null) {
            return this.insert(index, this.nullText);
        }
        final int len = chars.length;
        if (len > 0) {
            this.ensureCapacity(this.size + len);
            System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
            System.arraycopy(chars, 0, this.buffer, index, len);
            this.size += len;
        }
        return this;
    }
    
    public TextStringBuilder insert(final int index, final char[] chars, final int offset, final int length) {
        this.validateIndex(index);
        if (chars == null) {
            return this.insert(index, this.nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
        }
        if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            this.ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
            System.arraycopy(chars, offset, this.buffer, index, length);
            this.size += length;
        }
        return this;
    }
    
    public TextStringBuilder insert(final int index, final double value) {
        return this.insert(index, String.valueOf(value));
    }
    
    public TextStringBuilder insert(final int index, final float value) {
        return this.insert(index, String.valueOf(value));
    }
    
    public TextStringBuilder insert(final int index, final int value) {
        return this.insert(index, String.valueOf(value));
    }
    
    public TextStringBuilder insert(final int index, final long value) {
        return this.insert(index, String.valueOf(value));
    }
    
    public TextStringBuilder insert(final int index, final Object obj) {
        if (obj == null) {
            return this.insert(index, this.nullText);
        }
        return this.insert(index, obj.toString());
    }
    
    public TextStringBuilder insert(final int index, String str) {
        this.validateIndex(index);
        if (str == null) {
            str = this.nullText;
        }
        if (str != null) {
            final int strLen = str.length();
            if (strLen > 0) {
                final int newSize = this.size + strLen;
                this.ensureCapacity(newSize);
                System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
                this.size = newSize;
                str.getChars(0, strLen, this.buffer, index);
            }
        }
        return this;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public boolean isNotEmpty() {
        return this.size != 0;
    }
    
    public boolean isReallocated() {
        return this.reallocations > 0;
    }
    
    public int lastIndexOf(final char ch) {
        return this.lastIndexOf(ch, this.size - 1);
    }
    
    public int lastIndexOf(final char ch, int startIndex) {
        startIndex = ((startIndex >= this.size) ? (this.size - 1) : startIndex);
        if (startIndex < 0) {
            return -1;
        }
        for (int i = startIndex; i >= 0; --i) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final String str) {
        return this.lastIndexOf(str, this.size - 1);
    }
    
    public int lastIndexOf(final String str, int startIndex) {
        startIndex = ((startIndex >= this.size) ? (this.size - 1) : startIndex);
        if (str == null || startIndex < 0) {
            return -1;
        }
        final int strLen = str.length();
        if (strLen > 0 && strLen <= this.size) {
            if (strLen == 1) {
                return this.lastIndexOf(str.charAt(0), startIndex);
            }
            int i = startIndex - strLen + 1;
        Label_0069:
            while (i >= 0) {
                for (int j = 0; j < strLen; ++j) {
                    if (str.charAt(j) != this.buffer[i + j]) {
                        --i;
                        continue Label_0069;
                    }
                }
                return i;
            }
        }
        else if (strLen == 0) {
            return startIndex;
        }
        return -1;
    }
    
    public int lastIndexOf(final StringMatcher matcher) {
        return this.lastIndexOf(matcher, this.size);
    }
    
    public int lastIndexOf(final StringMatcher matcher, int startIndex) {
        startIndex = ((startIndex >= this.size) ? (this.size - 1) : startIndex);
        if (matcher == null || startIndex < 0) {
            return -1;
        }
        final char[] buf = this.buffer;
        final int endIndex = startIndex + 1;
        for (int i = startIndex; i >= 0; --i) {
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
                return i;
            }
        }
        return -1;
    }
    
    public String leftString(final int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, 0, length);
    }
    
    @Override
    public int length() {
        return this.size;
    }
    
    public String midString(int index, final int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= this.size) {
            return "";
        }
        if (this.size <= index + length) {
            return new String(this.buffer, index, this.size - index);
        }
        return new String(this.buffer, index, length);
    }
    
    public TextStringBuilder minimizeCapacity() {
        if (this.buffer.length > this.size) {
            this.reallocate(this.size);
        }
        return this;
    }
    
    public int readFrom(final CharBuffer charBuffer) {
        final int oldSize = this.size;
        final int remaining = charBuffer.remaining();
        this.ensureCapacity(this.size + remaining);
        charBuffer.get(this.buffer, this.size, remaining);
        this.size += remaining;
        return this.size - oldSize;
    }
    
    public int readFrom(final Readable readable) throws IOException {
        if (readable instanceof Reader) {
            return this.readFrom((Reader)readable);
        }
        if (readable instanceof CharBuffer) {
            return this.readFrom((CharBuffer)readable);
        }
        final int oldSize = this.size;
        while (true) {
            this.ensureCapacity(this.size + 1);
            final CharBuffer buf = CharBuffer.wrap(this.buffer, this.size, this.buffer.length - this.size);
            final int read = readable.read(buf);
            if (read == -1) {
                break;
            }
            this.size += read;
        }
        return this.size - oldSize;
    }
    
    public int readFrom(final Reader reader) throws IOException {
        final int oldSize = this.size;
        this.ensureCapacity(this.size + 1);
        int readCount = reader.read(this.buffer, this.size, this.buffer.length - this.size);
        if (readCount == -1) {
            return -1;
        }
        do {
            this.size += readCount;
            this.ensureCapacity(this.size + 1);
            readCount = reader.read(this.buffer, this.size, this.buffer.length - this.size);
        } while (readCount != -1);
        return this.size - oldSize;
    }
    
    public int readFrom(final Reader reader, final int count) throws IOException {
        if (count <= 0) {
            return 0;
        }
        final int oldSize = this.size;
        this.ensureCapacity(this.size + count);
        int target = count;
        int readCount = reader.read(this.buffer, this.size, target);
        if (readCount == -1) {
            return -1;
        }
        do {
            target -= readCount;
            this.size += readCount;
            readCount = reader.read(this.buffer, this.size, target);
        } while (target > 0 && readCount != -1);
        return this.size - oldSize;
    }
    
    private void reallocate(final int newLength) {
        this.buffer = Arrays.copyOf(this.buffer, newLength);
        ++this.reallocations;
    }
    
    public TextStringBuilder replace(final int startIndex, int endIndex, final String replaceStr) {
        endIndex = this.validateRange(startIndex, endIndex);
        final int insertLen = (replaceStr == null) ? 0 : replaceStr.length();
        this.replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
        return this;
    }
    
    public TextStringBuilder replace(final StringMatcher matcher, final String replaceStr, final int startIndex, int endIndex, final int replaceCount) {
        endIndex = this.validateRange(startIndex, endIndex);
        return this.replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
    }
    
    public TextStringBuilder replaceAll(final char search, final char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; ++i) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                }
            }
        }
        return this;
    }
    
    public TextStringBuilder replaceAll(final String searchStr, final String replaceStr) {
        final int searchLen = (searchStr == null) ? 0 : searchStr.length();
        if (searchLen > 0) {
            for (int replaceLen = (replaceStr == null) ? 0 : replaceStr.length(), index = this.indexOf(searchStr, 0); index >= 0; index = this.indexOf(searchStr, index + replaceLen)) {
                this.replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
            }
        }
        return this;
    }
    
    public TextStringBuilder replaceAll(final StringMatcher matcher, final String replaceStr) {
        return this.replace(matcher, replaceStr, 0, this.size, -1);
    }
    
    public TextStringBuilder replaceFirst(final char search, final char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; ++i) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                    break;
                }
            }
        }
        return this;
    }
    
    public TextStringBuilder replaceFirst(final String searchStr, final String replaceStr) {
        final int searchLen = (searchStr == null) ? 0 : searchStr.length();
        if (searchLen > 0) {
            final int index = this.indexOf(searchStr, 0);
            if (index >= 0) {
                final int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
                this.replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
            }
        }
        return this;
    }
    
    public TextStringBuilder replaceFirst(final StringMatcher matcher, final String replaceStr) {
        return this.replace(matcher, replaceStr, 0, this.size, 1);
    }
    
    private void replaceImpl(final int startIndex, final int endIndex, final int removeLen, final String insertStr, final int insertLen) {
        final int newSize = this.size - removeLen + insertLen;
        if (insertLen != removeLen) {
            this.ensureCapacity(newSize);
            System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
            this.size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, this.buffer, startIndex);
        }
    }
    
    private TextStringBuilder replaceImpl(final StringMatcher matcher, final String replaceStr, final int from, int to, int replaceCount) {
        if (matcher == null || this.size == 0) {
            return this;
        }
        final int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
        for (int i = from; i < to && replaceCount != 0; ++i) {
            final char[] buf = this.buffer;
            final int removeLen = matcher.isMatch(buf, i, from, to);
            if (removeLen > 0) {
                this.replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                to = to - removeLen + replaceLen;
                i = i + replaceLen - 1;
                if (replaceCount > 0) {
                    --replaceCount;
                }
            }
        }
        return this;
    }
    
    public TextStringBuilder reverse() {
        if (this.size == 0) {
            return this;
        }
        final int half = this.size / 2;
        final char[] buf = this.buffer;
        for (int leftIdx = 0, rightIdx = this.size - 1; leftIdx < half; ++leftIdx, --rightIdx) {
            final char swap = buf[leftIdx];
            buf[leftIdx] = buf[rightIdx];
            buf[rightIdx] = swap;
        }
        return this;
    }
    
    public String rightString(final int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, this.size - length, length);
    }
    
    public TextStringBuilder set(final CharSequence str) {
        this.clear();
        this.append(str);
        return this;
    }
    
    public TextStringBuilder setCharAt(final int index, final char ch) {
        this.validateIndex(index);
        this.buffer[index] = ch;
        return this;
    }
    
    public TextStringBuilder setLength(final int length) {
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < this.size) {
            this.size = length;
        }
        else if (length > this.size) {
            this.ensureCapacity(length);
            final int oldEnd = this.size;
            this.size = length;
            Arrays.fill(this.buffer, oldEnd, length, '\0');
        }
        return this;
    }
    
    public TextStringBuilder setNewLineText(final String newLine) {
        this.newLine = newLine;
        return this;
    }
    
    public TextStringBuilder setNullText(String nullText) {
        if (nullText != null && nullText.isEmpty()) {
            nullText = null;
        }
        this.nullText = nullText;
        return this;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean startsWith(final String str) {
        if (str == null) {
            return false;
        }
        final int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public CharSequence subSequence(final int startIndex, final int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - startIndex);
        }
        return this.substring(startIndex, endIndex);
    }
    
    public String substring(final int start) {
        return this.substring(start, this.size);
    }
    
    public String substring(final int startIndex, int endIndex) {
        endIndex = this.validateRange(startIndex, endIndex);
        return new String(this.buffer, startIndex, endIndex - startIndex);
    }
    
    public char[] toCharArray() {
        return (this.size == 0) ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOf(this.buffer, this.size);
    }
    
    public char[] toCharArray(final int startIndex, int endIndex) {
        endIndex = this.validateRange(startIndex, endIndex);
        final int len = endIndex - startIndex;
        return (len == 0) ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOfRange(this.buffer, startIndex, endIndex);
    }
    
    @Override
    public String toString() {
        return new String(this.buffer, 0, this.size);
    }
    
    public StringBuffer toStringBuffer() {
        return new StringBuffer(this.size).append(this.buffer, 0, this.size);
    }
    
    public StringBuilder toStringBuilder() {
        return new StringBuilder(this.size).append(this.buffer, 0, this.size);
    }
    
    public TextStringBuilder trim() {
        if (this.size == 0) {
            return this;
        }
        int len;
        char[] buf;
        int pos;
        for (len = this.size, buf = this.buffer, pos = 0; pos < len && buf[pos] <= ' '; ++pos) {}
        while (pos < len && buf[len - 1] <= ' ') {
            --len;
        }
        if (len < this.size) {
            this.delete(len, this.size);
        }
        if (pos > 0) {
            this.delete(0, pos);
        }
        return this;
    }
    
    protected void validateIndex(final int index) {
        if (index < 0 || index >= this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }
    
    protected int validateRange(final int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            endIndex = this.size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }
    
    static {
        FALSE_STRING_SIZE = Boolean.FALSE.toString().length();
        TRUE_STRING_SIZE = Boolean.TRUE.toString().length();
    }
    
    class TextStringBuilderReader extends Reader
    {
        private int mark;
        private int pos;
        
        @Override
        public void close() {
        }
        
        @Override
        public void mark(final int readAheadLimit) {
            this.mark = this.pos;
        }
        
        @Override
        public boolean markSupported() {
            return true;
        }
        
        @Override
        public int read() {
            if (!this.ready()) {
                return -1;
            }
            return TextStringBuilder.this.charAt(this.pos++);
        }
        
        @Override
        public int read(final char[] b, final int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (this.pos >= TextStringBuilder.this.size()) {
                return -1;
            }
            if (this.pos + len > TextStringBuilder.this.size()) {
                len = TextStringBuilder.this.size() - this.pos;
            }
            TextStringBuilder.this.getChars(this.pos, this.pos + len, b, off);
            this.pos += len;
            return len;
        }
        
        @Override
        public boolean ready() {
            return this.pos < TextStringBuilder.this.size();
        }
        
        @Override
        public void reset() {
            this.pos = this.mark;
        }
        
        @Override
        public long skip(long n) {
            if (this.pos + n > TextStringBuilder.this.size()) {
                n = TextStringBuilder.this.size() - this.pos;
            }
            if (n < 0L) {
                return 0L;
            }
            this.pos += (int)n;
            return n;
        }
    }
    
    class TextStringBuilderTokenizer extends StringTokenizer
    {
        @Override
        public String getContent() {
            final String str = super.getContent();
            if (str == null) {
                return TextStringBuilder.this.toString();
            }
            return str;
        }
        
        @Override
        protected List<String> tokenize(final char[] chars, final int offset, final int count) {
            if (chars == null) {
                return super.tokenize(TextStringBuilder.this.getBuffer(), 0, TextStringBuilder.this.size());
            }
            return super.tokenize(chars, offset, count);
        }
    }
    
    class TextStringBuilderWriter extends Writer
    {
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void write(final char[] cbuf) {
            TextStringBuilder.this.append(cbuf);
        }
        
        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            TextStringBuilder.this.append(cbuf, off, len);
        }
        
        @Override
        public void write(final int c) {
            TextStringBuilder.this.append((char)c);
        }
        
        @Override
        public void write(final String str) {
            TextStringBuilder.this.append(str);
        }
        
        @Override
        public void write(final String str, final int off, final int len) {
            TextStringBuilder.this.append(str, off, len);
        }
    }
}
