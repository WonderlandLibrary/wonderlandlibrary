/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.RawCollationKey;

public final class CollationKey
implements Comparable<CollationKey> {
    private byte[] m_key_;
    private String m_source_;
    private int m_hashCode_;
    private int m_length_;
    private static final int MERGE_SEPERATOR_ = 2;

    public CollationKey(String source, byte[] key) {
        this(source, key, -1);
    }

    private CollationKey(String source, byte[] key, int length) {
        this.m_source_ = source;
        this.m_key_ = key;
        this.m_hashCode_ = 0;
        this.m_length_ = length;
    }

    public CollationKey(String source, RawCollationKey key) {
        this.m_source_ = source;
        this.m_length_ = key.size - 1;
        this.m_key_ = key.releaseBytes();
        assert (this.m_key_[this.m_length_] == 0);
        this.m_hashCode_ = 0;
    }

    public String getSourceString() {
        return this.m_source_;
    }

    public byte[] toByteArray() {
        int length = this.getLength() + 1;
        byte[] result = new byte[length];
        System.arraycopy(this.m_key_, 0, result, 0, length);
        return result;
    }

    @Override
    public int compareTo(CollationKey target) {
        int i2 = 0;
        int r2;
        int l2;
        while ((l2 = this.m_key_[i2] & 0xFF) >= (r2 = target.m_key_[i2] & 0xFF)) {
            if (l2 > r2) {
                return 1;
            }
            if (l2 == 0) {
                return 0;
            }
            ++i2;
        }
        return -1;
    }

    public boolean equals(Object target) {
        if (!(target instanceof CollationKey)) {
            return false;
        }
        return this.equals((CollationKey)target);
    }

    public boolean equals(CollationKey target) {
        if (this == target) {
            return true;
        }
        if (target == null) {
            return false;
        }
        CollationKey other = target;
        int i2 = 0;
        while (true) {
            if (this.m_key_[i2] != other.m_key_[i2]) {
                return false;
            }
            if (this.m_key_[i2] == 0) break;
            ++i2;
        }
        return true;
    }

    public int hashCode() {
        if (this.m_hashCode_ == 0) {
            if (this.m_key_ == null) {
                this.m_hashCode_ = 1;
            } else {
                int size = this.m_key_.length >> 1;
                StringBuilder key = new StringBuilder(size);
                int i2 = 0;
                while (this.m_key_[i2] != 0 && this.m_key_[i2 + 1] != 0) {
                    key.append((char)(this.m_key_[i2] << 8 | 0xFF & this.m_key_[i2 + 1]));
                    i2 += 2;
                }
                if (this.m_key_[i2] != 0) {
                    key.append((char)(this.m_key_[i2] << 8));
                }
                this.m_hashCode_ = key.toString().hashCode();
            }
        }
        return this.m_hashCode_;
    }

    public CollationKey getBound(int boundType, int noOfLevels) {
        int offset = 0;
        int keystrength = 0;
        if (noOfLevels > 0) {
            while (offset < this.m_key_.length && this.m_key_[offset] != 0) {
                if (this.m_key_[offset++] != 1) continue;
                ++keystrength;
                if (--noOfLevels != 0 && offset != this.m_key_.length && this.m_key_[offset] != 0) continue;
                --offset;
                break;
            }
        }
        if (noOfLevels > 0) {
            throw new IllegalArgumentException("Source collation key has only " + keystrength + " strength level. Call getBound() again  with noOfLevels < " + keystrength);
        }
        byte[] resultkey = new byte[offset + boundType + 1];
        System.arraycopy(this.m_key_, 0, resultkey, 0, offset);
        switch (boundType) {
            case 0: {
                break;
            }
            case 1: {
                resultkey[offset++] = 2;
                break;
            }
            case 2: {
                resultkey[offset++] = -1;
                resultkey[offset++] = -1;
                break;
            }
            default: {
                throw new IllegalArgumentException("Illegal boundType argument");
            }
        }
        resultkey[offset] = 0;
        return new CollationKey(null, resultkey, offset);
    }

    public CollationKey merge(CollationKey source) {
        if (source == null || source.getLength() == 0) {
            throw new IllegalArgumentException("CollationKey argument can not be null or of 0 length");
        }
        byte[] result = new byte[this.getLength() + source.getLength() + 2];
        int rindex = 0;
        int index = 0;
        int sourceindex = 0;
        while (true) {
            if (this.m_key_[index] < 0 || this.m_key_[index] >= 2) {
                result[rindex++] = this.m_key_[index++];
                continue;
            }
            result[rindex++] = 2;
            while (source.m_key_[sourceindex] < 0 || source.m_key_[sourceindex] >= 2) {
                result[rindex++] = source.m_key_[sourceindex++];
            }
            if (this.m_key_[index] != 1 || source.m_key_[sourceindex] != 1) break;
            ++index;
            ++sourceindex;
            result[rindex++] = 1;
        }
        int remainingLength = this.m_length_ - index;
        if (remainingLength > 0) {
            System.arraycopy(this.m_key_, index, result, rindex, remainingLength);
            rindex += remainingLength;
        } else {
            remainingLength = source.m_length_ - sourceindex;
            if (remainingLength > 0) {
                System.arraycopy(source.m_key_, sourceindex, result, rindex, remainingLength);
                rindex += remainingLength;
            }
        }
        result[rindex] = 0;
        assert (rindex == result.length - 1);
        return new CollationKey(null, result, rindex);
    }

    private int getLength() {
        if (this.m_length_ >= 0) {
            return this.m_length_;
        }
        int length = this.m_key_.length;
        for (int index = 0; index < length; ++index) {
            if (this.m_key_[index] != 0) continue;
            length = index;
            break;
        }
        this.m_length_ = length;
        return this.m_length_;
    }

    public static final class BoundMode {
        public static final int LOWER = 0;
        public static final int UPPER = 1;
        public static final int UPPER_LONG = 2;
        @Deprecated
        public static final int COUNT = 3;

        private BoundMode() {
        }
    }
}

