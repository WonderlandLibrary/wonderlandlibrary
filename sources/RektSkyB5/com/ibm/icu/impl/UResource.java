/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.nio.ByteBuffer;

public final class UResource {

    public static abstract class Sink {
        public abstract void put(Key var1, Value var2, boolean var3);
    }

    public static abstract class Value {
        protected Value() {
        }

        public abstract int getType();

        public abstract String getString();

        public abstract String getAliasString();

        public abstract int getInt();

        public abstract int getUInt();

        public abstract int[] getIntVector();

        public abstract ByteBuffer getBinary();

        public abstract Array getArray();

        public abstract Table getTable();

        public abstract boolean isNoInheritanceMarker();

        public abstract String[] getStringArray();

        public abstract String[] getStringArrayOrStringAsArray();

        public abstract String getStringOrFirstOfArray();

        public String toString() {
            switch (this.getType()) {
                case 0: {
                    return this.getString();
                }
                case 7: {
                    return Integer.toString(this.getInt());
                }
                case 14: {
                    int[] iv = this.getIntVector();
                    StringBuilder sb = new StringBuilder("[");
                    sb.append(iv.length).append("]{");
                    if (iv.length != 0) {
                        sb.append(iv[0]);
                        for (int i2 = 1; i2 < iv.length; ++i2) {
                            sb.append(", ").append(iv[i2]);
                        }
                    }
                    return sb.append('}').toString();
                }
                case 1: {
                    return "(binary blob)";
                }
                case 8: {
                    return "(array)";
                }
                case 2: {
                    return "(table)";
                }
            }
            return "???";
        }
    }

    public static interface Table {
        public int getSize();

        public boolean getKeyAndValue(int var1, Key var2, Value var3);
    }

    public static interface Array {
        public int getSize();

        public boolean getValue(int var1, Value var2);
    }

    public static final class Key
    implements CharSequence,
    Cloneable,
    Comparable<Key> {
        private byte[] bytes;
        private int offset;
        private int length;
        private String s;

        public Key() {
            this.s = "";
        }

        public Key(String s2) {
            this.setString(s2);
        }

        private Key(byte[] keyBytes, int keyOffset, int keyLength) {
            this.bytes = keyBytes;
            this.offset = keyOffset;
            this.length = keyLength;
        }

        public Key setBytes(byte[] keyBytes, int keyOffset) {
            this.bytes = keyBytes;
            this.offset = keyOffset;
            this.length = 0;
            while (keyBytes[keyOffset + this.length] != 0) {
                ++this.length;
            }
            this.s = null;
            return this;
        }

        public Key setToEmpty() {
            this.bytes = null;
            this.length = 0;
            this.offset = 0;
            this.s = "";
            return this;
        }

        public Key setString(String s2) {
            if (s2.isEmpty()) {
                this.setToEmpty();
            } else {
                this.bytes = new byte[s2.length()];
                this.offset = 0;
                this.length = s2.length();
                for (int i2 = 0; i2 < this.length; ++i2) {
                    char c2 = s2.charAt(i2);
                    if (c2 > '\u007f') {
                        throw new IllegalArgumentException('\"' + s2 + "\" is not an ASCII string");
                    }
                    this.bytes[i2] = (byte)c2;
                }
                this.s = s2;
            }
            return this;
        }

        public Key clone() {
            try {
                return (Key)super.clone();
            }
            catch (CloneNotSupportedException cannotOccur) {
                return null;
            }
        }

        @Override
        public char charAt(int i2) {
            assert (0 <= i2 && i2 < this.length);
            return (char)this.bytes[this.offset + i2];
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public Key subSequence(int start, int end) {
            assert (0 <= start && start < this.length);
            assert (start <= end && end <= this.length);
            return new Key(this.bytes, this.offset + start, end - start);
        }

        @Override
        public String toString() {
            if (this.s == null) {
                this.s = this.internalSubString(0, this.length);
            }
            return this.s;
        }

        private String internalSubString(int start, int end) {
            StringBuilder sb = new StringBuilder(end - start);
            for (int i2 = start; i2 < end; ++i2) {
                sb.append((char)this.bytes[this.offset + i2]);
            }
            return sb.toString();
        }

        public String substring(int start) {
            assert (0 <= start && start < this.length);
            return this.internalSubString(start, this.length);
        }

        public String substring(int start, int end) {
            assert (0 <= start && start < this.length);
            assert (start <= end && end <= this.length);
            return this.internalSubString(start, end);
        }

        private boolean regionMatches(byte[] otherBytes, int otherOffset, int n2) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.bytes[this.offset + i2] == otherBytes[otherOffset + i2]) continue;
                return false;
            }
            return true;
        }

        private boolean regionMatches(int start, CharSequence cs, int n2) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.bytes[this.offset + start + i2] == cs.charAt(i2)) continue;
                return false;
            }
            return true;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (this == other) {
                return true;
            }
            if (other instanceof Key) {
                Key otherKey = (Key)other;
                return this.length == otherKey.length && this.regionMatches(otherKey.bytes, otherKey.offset, this.length);
            }
            return false;
        }

        public boolean contentEquals(CharSequence cs) {
            if (cs == null) {
                return false;
            }
            return this == cs || cs.length() == this.length && this.regionMatches(0, cs, this.length);
        }

        public boolean startsWith(CharSequence cs) {
            int csLength = cs.length();
            return csLength <= this.length && this.regionMatches(0, cs, csLength);
        }

        public boolean endsWith(CharSequence cs) {
            int csLength = cs.length();
            return csLength <= this.length && this.regionMatches(this.length - csLength, cs, csLength);
        }

        public boolean regionMatches(int start, CharSequence cs) {
            int csLength = cs.length();
            return csLength == this.length - start && this.regionMatches(start, cs, csLength);
        }

        public int hashCode() {
            if (this.length == 0) {
                return 0;
            }
            int h2 = this.bytes[this.offset];
            for (int i2 = 1; i2 < this.length; ++i2) {
                h2 = 37 * h2 + this.bytes[this.offset];
            }
            return h2;
        }

        @Override
        public int compareTo(Key other) {
            return this.compareTo((CharSequence)other);
        }

        @Override
        public int compareTo(CharSequence cs) {
            int csLength = cs.length();
            int minLength = this.length <= csLength ? this.length : csLength;
            for (int i2 = 0; i2 < minLength; ++i2) {
                int diff = this.charAt(i2) - cs.charAt(i2);
                if (diff == 0) continue;
                return diff;
            }
            return this.length - csLength;
        }
    }
}

