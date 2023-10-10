/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheValue;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ICUUncheckedIOException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceTypeMismatchException;
import com.ibm.icu.util.VersionInfo;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public final class ICUResourceBundleReader {
    private static final int DATA_FORMAT = 1382380354;
    private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
    private static final int URES_INDEX_LENGTH = 0;
    private static final int URES_INDEX_KEYS_TOP = 1;
    private static final int URES_INDEX_BUNDLE_TOP = 3;
    private static final int URES_INDEX_MAX_TABLE_LENGTH = 4;
    private static final int URES_INDEX_ATTRIBUTES = 5;
    private static final int URES_INDEX_16BIT_TOP = 6;
    private static final int URES_INDEX_POOL_CHECKSUM = 7;
    private static final int URES_ATT_NO_FALLBACK = 1;
    private static final int URES_ATT_IS_POOL_BUNDLE = 2;
    private static final int URES_ATT_USES_POOL_BUNDLE = 4;
    private static final CharBuffer EMPTY_16_BIT_UNITS = CharBuffer.wrap("\u0000");
    static final int LARGE_SIZE = 24;
    private static final boolean DEBUG = false;
    private int dataVersion;
    private ByteBuffer bytes;
    private byte[] keyBytes;
    private CharBuffer b16BitUnits;
    private ICUResourceBundleReader poolBundleReader;
    private int rootRes;
    private int localKeyLimit;
    private int poolStringIndexLimit;
    private int poolStringIndex16Limit;
    private boolean noFallback;
    private boolean isPoolBundle;
    private boolean usesPoolBundle;
    private int poolCheckSum;
    private ResourceCache resourceCache;
    private static ReaderCache CACHE = new ReaderCache();
    private static final ICUResourceBundleReader NULL_READER = new ICUResourceBundleReader();
    private static final byte[] emptyBytes = new byte[0];
    private static final ByteBuffer emptyByteBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
    private static final char[] emptyChars = new char[0];
    private static final int[] emptyInts = new int[0];
    private static final String emptyString = "";
    private static final Array EMPTY_ARRAY = new Array();
    private static final Table EMPTY_TABLE = new Table();
    private static int[] PUBLIC_TYPES = new int[]{0, 1, 2, 3, 2, 2, 0, 7, 8, 8, -1, -1, -1, -1, 14, -1};
    private static final String ICU_RESOURCE_SUFFIX = ".res";

    private ICUResourceBundleReader() {
    }

    private ICUResourceBundleReader(ByteBuffer inBytes, String baseName, String localeID, ClassLoader loader) throws IOException {
        this.init(inBytes);
        if (this.usesPoolBundle) {
            this.poolBundleReader = ICUResourceBundleReader.getReader(baseName, "pool", loader);
            if (this.poolBundleReader == null || !this.poolBundleReader.isPoolBundle) {
                throw new IllegalStateException("pool.res is not a pool bundle");
            }
            if (this.poolBundleReader.poolCheckSum != this.poolCheckSum) {
                throw new IllegalStateException("pool.res has a different checksum than this bundle");
            }
        }
    }

    static ICUResourceBundleReader getReader(String baseName, String localeID, ClassLoader root) {
        ReaderCacheKey info = new ReaderCacheKey(baseName, localeID);
        ICUResourceBundleReader reader = (ICUResourceBundleReader)CACHE.getInstance(info, root);
        if (reader == NULL_READER) {
            return null;
        }
        return reader;
    }

    private void init(ByteBuffer inBytes) throws IOException {
        int bundleTop;
        this.dataVersion = ICUBinary.readHeader(inBytes, 1382380354, IS_ACCEPTABLE);
        byte majorFormatVersion = inBytes.get(16);
        this.bytes = ICUBinary.sliceWithOrder(inBytes);
        int dataLength = this.bytes.remaining();
        this.rootRes = this.bytes.getInt(0);
        int indexes0 = this.getIndexesInt(0);
        int indexLength = indexes0 & 0xFF;
        if (indexLength <= 4) {
            throw new ICUException("not enough indexes");
        }
        if (dataLength < 1 + indexLength << 2 || dataLength < (bundleTop = this.getIndexesInt(3)) << 2) {
            throw new ICUException("not enough bytes");
        }
        int maxOffset = bundleTop - 1;
        if (majorFormatVersion >= 3) {
            this.poolStringIndexLimit = indexes0 >>> 8;
        }
        if (indexLength > 5) {
            int att = this.getIndexesInt(5);
            this.noFallback = (att & 1) != 0;
            this.isPoolBundle = (att & 2) != 0;
            this.usesPoolBundle = (att & 4) != 0;
            this.poolStringIndexLimit |= (att & 0xF000) << 12;
            this.poolStringIndex16Limit = att >>> 16;
        }
        int keysBottom = 1 + indexLength;
        int keysTop = this.getIndexesInt(1);
        if (keysTop > keysBottom) {
            if (this.isPoolBundle) {
                this.keyBytes = new byte[keysTop - keysBottom << 2];
                this.bytes.position(keysBottom << 2);
            } else {
                this.localKeyLimit = keysTop << 2;
                this.keyBytes = new byte[this.localKeyLimit];
            }
            this.bytes.get(this.keyBytes);
        }
        if (indexLength > 6) {
            int _16BitTop = this.getIndexesInt(6);
            if (_16BitTop > keysTop) {
                int num16BitUnits = (_16BitTop - keysTop) * 2;
                this.bytes.position(keysTop << 2);
                this.b16BitUnits = this.bytes.asCharBuffer();
                this.b16BitUnits.limit(num16BitUnits);
                maxOffset |= num16BitUnits - 1;
            } else {
                this.b16BitUnits = EMPTY_16_BIT_UNITS;
            }
        } else {
            this.b16BitUnits = EMPTY_16_BIT_UNITS;
        }
        if (indexLength > 7) {
            this.poolCheckSum = this.getIndexesInt(7);
        }
        if (!this.isPoolBundle || this.b16BitUnits.length() > 1) {
            this.resourceCache = new ResourceCache(maxOffset);
        }
        this.bytes.position(0);
    }

    private int getIndexesInt(int i2) {
        return this.bytes.getInt(1 + i2 << 2);
    }

    VersionInfo getVersion() {
        return ICUBinary.getVersionInfoFromCompactInt(this.dataVersion);
    }

    int getRootResource() {
        return this.rootRes;
    }

    boolean getNoFallback() {
        return this.noFallback;
    }

    boolean getUsesPoolBundle() {
        return this.usesPoolBundle;
    }

    static int RES_GET_TYPE(int res) {
        return res >>> 28;
    }

    private static int RES_GET_OFFSET(int res) {
        return res & 0xFFFFFFF;
    }

    private int getResourceByteOffset(int offset) {
        return offset << 2;
    }

    static int RES_GET_INT(int res) {
        return res << 4 >> 4;
    }

    static int RES_GET_UINT(int res) {
        return res & 0xFFFFFFF;
    }

    static boolean URES_IS_ARRAY(int type) {
        return type == 8 || type == 9;
    }

    static boolean URES_IS_TABLE(int type) {
        return type == 2 || type == 5 || type == 4;
    }

    private char[] getChars(int offset, int count) {
        char[] chars = new char[count];
        if (count <= 16) {
            for (int i2 = 0; i2 < count; ++i2) {
                chars[i2] = this.bytes.getChar(offset);
                offset += 2;
            }
        } else {
            CharBuffer temp = this.bytes.asCharBuffer();
            temp.position(offset / 2);
            temp.get(chars);
        }
        return chars;
    }

    private int getInt(int offset) {
        return this.bytes.getInt(offset);
    }

    private int[] getInts(int offset, int count) {
        int[] ints = new int[count];
        if (count <= 16) {
            for (int i2 = 0; i2 < count; ++i2) {
                ints[i2] = this.bytes.getInt(offset);
                offset += 4;
            }
        } else {
            IntBuffer temp = this.bytes.asIntBuffer();
            temp.position(offset / 4);
            temp.get(ints);
        }
        return ints;
    }

    private char[] getTable16KeyOffsets(int offset) {
        int length;
        if ((length = this.b16BitUnits.charAt(offset++)) > 0) {
            char[] result = new char[length];
            if (length <= 16) {
                for (int i2 = 0; i2 < length; ++i2) {
                    result[i2] = this.b16BitUnits.charAt(offset++);
                }
            } else {
                CharBuffer temp = this.b16BitUnits.duplicate();
                temp.position(offset);
                temp.get(result);
            }
            return result;
        }
        return emptyChars;
    }

    private char[] getTableKeyOffsets(int offset) {
        char length = this.bytes.getChar(offset);
        if (length > '\u0000') {
            return this.getChars(offset + 2, length);
        }
        return emptyChars;
    }

    private int[] getTable32KeyOffsets(int offset) {
        int length = this.getInt(offset);
        if (length > 0) {
            return this.getInts(offset + 4, length);
        }
        return emptyInts;
    }

    private static String makeKeyStringFromBytes(byte[] keyBytes, int keyOffset) {
        byte b2;
        StringBuilder sb = new StringBuilder();
        while ((b2 = keyBytes[keyOffset]) != 0) {
            ++keyOffset;
            sb.append((char)b2);
        }
        return sb.toString();
    }

    private String getKey16String(int keyOffset) {
        if (keyOffset < this.localKeyLimit) {
            return ICUResourceBundleReader.makeKeyStringFromBytes(this.keyBytes, keyOffset);
        }
        return ICUResourceBundleReader.makeKeyStringFromBytes(this.poolBundleReader.keyBytes, keyOffset - this.localKeyLimit);
    }

    private String getKey32String(int keyOffset) {
        if (keyOffset >= 0) {
            return ICUResourceBundleReader.makeKeyStringFromBytes(this.keyBytes, keyOffset);
        }
        return ICUResourceBundleReader.makeKeyStringFromBytes(this.poolBundleReader.keyBytes, keyOffset & Integer.MAX_VALUE);
    }

    private void setKeyFromKey16(int keyOffset, UResource.Key key) {
        if (keyOffset < this.localKeyLimit) {
            key.setBytes(this.keyBytes, keyOffset);
        } else {
            key.setBytes(this.poolBundleReader.keyBytes, keyOffset - this.localKeyLimit);
        }
    }

    private void setKeyFromKey32(int keyOffset, UResource.Key key) {
        if (keyOffset >= 0) {
            key.setBytes(this.keyBytes, keyOffset);
        } else {
            key.setBytes(this.poolBundleReader.keyBytes, keyOffset & Integer.MAX_VALUE);
        }
    }

    private int compareKeys(CharSequence key, char keyOffset) {
        if (keyOffset < this.localKeyLimit) {
            return ICUBinary.compareKeys(key, this.keyBytes, (int)keyOffset);
        }
        return ICUBinary.compareKeys(key, this.poolBundleReader.keyBytes, keyOffset - this.localKeyLimit);
    }

    private int compareKeys32(CharSequence key, int keyOffset) {
        if (keyOffset >= 0) {
            return ICUBinary.compareKeys(key, this.keyBytes, keyOffset);
        }
        return ICUBinary.compareKeys(key, this.poolBundleReader.keyBytes, keyOffset & Integer.MAX_VALUE);
    }

    String getStringV2(int res) {
        String s2;
        assert (ICUResourceBundleReader.RES_GET_TYPE(res) == 6);
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        assert (offset != 0);
        Object value = this.resourceCache.get(res);
        if (value != null) {
            return (String)value;
        }
        char first = this.b16BitUnits.charAt(offset);
        if ((first & 0xFFFFFC00) != 56320) {
            char c2;
            if (first == '\u0000') {
                return emptyString;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            while ((c2 = this.b16BitUnits.charAt(++offset)) != '\u0000') {
                sb.append(c2);
            }
            s2 = sb.toString();
        } else {
            int length;
            if (first < '\udfef') {
                length = first & 0x3FF;
                ++offset;
            } else if (first < '\udfff') {
                length = first - 57327 << 16 | this.b16BitUnits.charAt(offset + 1);
                offset += 2;
            } else {
                length = this.b16BitUnits.charAt(offset + 1) << 16 | this.b16BitUnits.charAt(offset + 2);
                offset += 3;
            }
            s2 = this.b16BitUnits.subSequence(offset, offset + length).toString();
        }
        return (String)this.resourceCache.putIfAbsent(res, s2, s2.length() * 2);
    }

    private String makeStringFromBytes(int offset, int length) {
        if (length <= 16) {
            StringBuilder sb = new StringBuilder(length);
            for (int i2 = 0; i2 < length; ++i2) {
                sb.append(this.bytes.getChar(offset));
                offset += 2;
            }
            return sb.toString();
        }
        CharBuffer cs = this.bytes.asCharBuffer();
        return cs.subSequence(offset /= 2, offset + length).toString();
    }

    String getString(int res) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (res != offset && ICUResourceBundleReader.RES_GET_TYPE(res) != 6) {
            return null;
        }
        if (offset == 0) {
            return emptyString;
        }
        if (res != offset) {
            if (offset < this.poolStringIndexLimit) {
                return this.poolBundleReader.getStringV2(res);
            }
            return this.getStringV2(res - this.poolStringIndexLimit);
        }
        Object value = this.resourceCache.get(res);
        if (value != null) {
            return (String)value;
        }
        offset = this.getResourceByteOffset(offset);
        int length = this.getInt(offset);
        String s2 = this.makeStringFromBytes(offset + 4, length);
        return (String)this.resourceCache.putIfAbsent(res, s2, s2.length() * 2);
    }

    private boolean isNoInheritanceMarker(int res) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (offset != 0) {
            if (res == offset) {
                return this.getInt(offset = this.getResourceByteOffset(offset)) == 3 && this.bytes.getChar(offset + 4) == '\u2205' && this.bytes.getChar(offset + 6) == '\u2205' && this.bytes.getChar(offset + 8) == '\u2205';
            }
            if (ICUResourceBundleReader.RES_GET_TYPE(res) == 6) {
                if (offset < this.poolStringIndexLimit) {
                    return this.poolBundleReader.isStringV2NoInheritanceMarker(offset);
                }
                return this.isStringV2NoInheritanceMarker(offset - this.poolStringIndexLimit);
            }
        }
        return false;
    }

    private boolean isStringV2NoInheritanceMarker(int offset) {
        char first = this.b16BitUnits.charAt(offset);
        if (first == '\u2205') {
            return this.b16BitUnits.charAt(offset + 1) == '\u2205' && this.b16BitUnits.charAt(offset + 2) == '\u2205' && this.b16BitUnits.charAt(offset + 3) == '\u0000';
        }
        if (first == '\udc03') {
            return this.b16BitUnits.charAt(offset + 1) == '\u2205' && this.b16BitUnits.charAt(offset + 2) == '\u2205' && this.b16BitUnits.charAt(offset + 3) == '\u2205';
        }
        return false;
    }

    String getAlias(int res) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (ICUResourceBundleReader.RES_GET_TYPE(res) == 3) {
            if (offset == 0) {
                return emptyString;
            }
            Object value = this.resourceCache.get(res);
            if (value != null) {
                return (String)value;
            }
            offset = this.getResourceByteOffset(offset);
            int length = this.getInt(offset);
            String s2 = this.makeStringFromBytes(offset + 4, length);
            return (String)this.resourceCache.putIfAbsent(res, s2, length * 2);
        }
        return null;
    }

    byte[] getBinary(int res, byte[] ba) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (ICUResourceBundleReader.RES_GET_TYPE(res) == 1) {
            if (offset == 0) {
                return emptyBytes;
            }
            int length = this.getInt(offset = this.getResourceByteOffset(offset));
            if (length == 0) {
                return emptyBytes;
            }
            if (ba == null || ba.length != length) {
                ba = new byte[length];
            }
            offset += 4;
            if (length <= 16) {
                for (int i2 = 0; i2 < length; ++i2) {
                    ba[i2] = this.bytes.get(offset++);
                }
            } else {
                ByteBuffer temp = this.bytes.duplicate();
                temp.position(offset);
                temp.get(ba);
            }
            return ba;
        }
        return null;
    }

    ByteBuffer getBinary(int res) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (ICUResourceBundleReader.RES_GET_TYPE(res) == 1) {
            if (offset == 0) {
                return emptyByteBuffer.duplicate();
            }
            int length = this.getInt(offset = this.getResourceByteOffset(offset));
            if (length == 0) {
                return emptyByteBuffer.duplicate();
            }
            ByteBuffer result = this.bytes.duplicate();
            result.position(offset += 4).limit(offset + length);
            result = ICUBinary.sliceWithOrder(result);
            if (!result.isReadOnly()) {
                result = result.asReadOnlyBuffer();
            }
            return result;
        }
        return null;
    }

    int[] getIntVector(int res) {
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (ICUResourceBundleReader.RES_GET_TYPE(res) == 14) {
            if (offset == 0) {
                return emptyInts;
            }
            offset = this.getResourceByteOffset(offset);
            int length = this.getInt(offset);
            return this.getInts(offset + 4, length);
        }
        return null;
    }

    Array getArray(int res) {
        int type = ICUResourceBundleReader.RES_GET_TYPE(res);
        if (!ICUResourceBundleReader.URES_IS_ARRAY(type)) {
            return null;
        }
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (offset == 0) {
            return EMPTY_ARRAY;
        }
        Object value = this.resourceCache.get(res);
        if (value != null) {
            return (Array)value;
        }
        Array array = type == 8 ? new Array32(this, offset) : new Array16(this, offset);
        return (Array)this.resourceCache.putIfAbsent(res, array, 0);
    }

    Table getTable(int res) {
        int size;
        Table table;
        int type = ICUResourceBundleReader.RES_GET_TYPE(res);
        if (!ICUResourceBundleReader.URES_IS_TABLE(type)) {
            return null;
        }
        int offset = ICUResourceBundleReader.RES_GET_OFFSET(res);
        if (offset == 0) {
            return EMPTY_TABLE;
        }
        Object value = this.resourceCache.get(res);
        if (value != null) {
            return (Table)value;
        }
        if (type == 2) {
            table = new Table1632(this, offset);
            size = table.getSize() * 2;
        } else if (type == 5) {
            table = new Table16(this, offset);
            size = table.getSize() * 2;
        } else {
            table = new Table32(this, offset);
            size = table.getSize() * 4;
        }
        return (Table)this.resourceCache.putIfAbsent(res, table, size);
    }

    public static String getFullName(String baseName, String localeName) {
        if (baseName == null || baseName.length() == 0) {
            if (localeName.length() == 0) {
                localeName = ULocale.getDefault().toString();
                return localeName;
            }
            return localeName + ICU_RESOURCE_SUFFIX;
        }
        if (baseName.indexOf(46) == -1) {
            if (baseName.charAt(baseName.length() - 1) != '/') {
                return baseName + "/" + localeName + ICU_RESOURCE_SUFFIX;
            }
            return baseName + localeName + ICU_RESOURCE_SUFFIX;
        }
        baseName = baseName.replace('.', '/');
        if (localeName.length() == 0) {
            return baseName + ICU_RESOURCE_SUFFIX;
        }
        return baseName + "_" + localeName + ICU_RESOURCE_SUFFIX;
    }

    private static final class ResourceCache {
        private static final int SIMPLE_LENGTH = 32;
        private static final int ROOT_BITS = 7;
        private static final int NEXT_BITS = 6;
        private int[] keys = new int[32];
        private Object[] values = new Object[32];
        private int length;
        private int maxOffsetBits;
        private int levelBitsList;
        private Level rootLevel;

        private static boolean storeDirectly(int size) {
            return size < 24 || CacheValue.futureInstancesWillBeStrong();
        }

        private static final Object putIfCleared(Object[] values, int index, Object item, int size) {
            Object value = values[index];
            if (!(value instanceof SoftReference)) {
                return value;
            }
            assert (size >= 24);
            if ((value = ((SoftReference)value).get()) != null) {
                return value;
            }
            values[index] = CacheValue.futureInstancesWillBeStrong() ? item : new SoftReference<Object>(item);
            return item;
        }

        ResourceCache(int maxOffset) {
            assert (maxOffset != 0);
            this.maxOffsetBits = 28;
            while (maxOffset <= 0x7FFFFFF) {
                maxOffset <<= 1;
                --this.maxOffsetBits;
            }
            int keyBits = this.maxOffsetBits + 2;
            if (keyBits <= 7) {
                this.levelBitsList = keyBits;
            } else if (keyBits < 10) {
                this.levelBitsList = 0x30 | keyBits - 3;
            } else {
                this.levelBitsList = 7;
                keyBits -= 7;
                int shift = 4;
                while (true) {
                    if (keyBits <= 6) {
                        this.levelBitsList |= keyBits << shift;
                        break;
                    }
                    if (keyBits < 9) {
                        this.levelBitsList |= (0x30 | keyBits - 3) << shift;
                        break;
                    }
                    this.levelBitsList |= 6 << shift;
                    keyBits -= 6;
                    shift += 4;
                }
            }
        }

        private int makeKey(int res) {
            int type = ICUResourceBundleReader.RES_GET_TYPE(res);
            int miniType = type == 6 ? 1 : (type == 5 ? 3 : (type == 9 ? 2 : 0));
            return ICUResourceBundleReader.RES_GET_OFFSET(res) | miniType << this.maxOffsetBits;
        }

        private int findSimple(int key) {
            int start = 0;
            int limit = this.length;
            while (limit - start > 8) {
                int mid = (start + limit) / 2;
                if (key < this.keys[mid]) {
                    limit = mid;
                    continue;
                }
                start = mid;
            }
            while (start < limit) {
                int k2 = this.keys[start];
                if (key < k2) {
                    return ~start;
                }
                if (key == k2) {
                    return start;
                }
                ++start;
            }
            return ~start;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        synchronized Object get(int res) {
            Object value;
            assert (ICUResourceBundleReader.RES_GET_OFFSET(res) != 0);
            if (this.length >= 0) {
                int index = this.findSimple(res);
                if (index < 0) return null;
                value = this.values[index];
            } else {
                value = this.rootLevel.get(this.makeKey(res));
                if (value == null) {
                    return null;
                }
            }
            if (!(value instanceof SoftReference)) return value;
            return ((SoftReference)value).get();
        }

        synchronized Object putIfAbsent(int res, Object item, int size) {
            if (this.length >= 0) {
                int index = this.findSimple(res);
                if (index >= 0) {
                    return ResourceCache.putIfCleared(this.values, index, item, size);
                }
                if (this.length < 32) {
                    if ((index ^= 0xFFFFFFFF) < this.length) {
                        System.arraycopy(this.keys, index, this.keys, index + 1, this.length - index);
                        System.arraycopy(this.values, index, this.values, index + 1, this.length - index);
                    }
                    ++this.length;
                    this.keys[index] = res;
                    this.values[index] = ResourceCache.storeDirectly(size) ? item : new SoftReference<Object>(item);
                    return item;
                }
                this.rootLevel = new Level(this.levelBitsList, 0);
                for (int i2 = 0; i2 < 32; ++i2) {
                    this.rootLevel.putIfAbsent(this.makeKey(this.keys[i2]), this.values[i2], 0);
                }
                this.keys = null;
                this.values = null;
                this.length = -1;
            }
            return this.rootLevel.putIfAbsent(this.makeKey(res), item, size);
        }

        private static final class Level {
            int levelBitsList;
            int shift;
            int mask;
            int[] keys;
            Object[] values;

            Level(int levelBitsList, int shift) {
                this.levelBitsList = levelBitsList;
                this.shift = shift;
                int bits = levelBitsList & 0xF;
                assert (bits != 0);
                int length = 1 << bits;
                this.mask = length - 1;
                this.keys = new int[length];
                this.values = new Object[length];
            }

            Object get(int key) {
                Level level;
                int index = key >> this.shift & this.mask;
                int k2 = this.keys[index];
                if (k2 == key) {
                    return this.values[index];
                }
                if (k2 == 0 && (level = (Level)this.values[index]) != null) {
                    return level.get(key);
                }
                return null;
            }

            Object putIfAbsent(int key, Object item, int size) {
                int index = key >> this.shift & this.mask;
                int k2 = this.keys[index];
                if (k2 == key) {
                    return ResourceCache.putIfCleared(this.values, index, item, size);
                }
                if (k2 == 0) {
                    Level level = (Level)this.values[index];
                    if (level != null) {
                        return level.putIfAbsent(key, item, size);
                    }
                    this.keys[index] = key;
                    this.values[index] = ResourceCache.storeDirectly(size) ? item : new SoftReference<Object>(item);
                    return item;
                }
                Level level = new Level(this.levelBitsList >> 4, this.shift + (this.levelBitsList & 0xF));
                int i2 = k2 >> level.shift & level.mask;
                level.keys[i2] = k2;
                level.values[i2] = this.values[index];
                this.keys[index] = 0;
                this.values[index] = level;
                return level.putIfAbsent(key, item, size);
            }
        }
    }

    private static final class Table32
    extends Table {
        @Override
        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return this.getContainer32Resource(reader, index);
        }

        Table32(ICUResourceBundleReader reader, int offset) {
            offset = reader.getResourceByteOffset(offset);
            this.key32Offsets = reader.getTable32KeyOffsets(offset);
            this.size = this.key32Offsets.length;
            this.itemsOffset = offset + 4 * (1 + this.size);
        }
    }

    private static final class Table16
    extends Table {
        @Override
        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return this.getContainer16Resource(reader, index);
        }

        Table16(ICUResourceBundleReader reader, int offset) {
            this.keyOffsets = reader.getTable16KeyOffsets(offset);
            this.size = this.keyOffsets.length;
            this.itemsOffset = offset + 1 + this.size;
        }
    }

    private static final class Table1632
    extends Table {
        @Override
        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return this.getContainer32Resource(reader, index);
        }

        Table1632(ICUResourceBundleReader reader, int offset) {
            offset = reader.getResourceByteOffset(offset);
            this.keyOffsets = reader.getTableKeyOffsets(offset);
            this.size = this.keyOffsets.length;
            this.itemsOffset = offset + 2 * (this.size + 2 & 0xFFFFFFFE);
        }
    }

    static class Table
    extends Container
    implements UResource.Table {
        protected char[] keyOffsets;
        protected int[] key32Offsets;
        private static final int URESDATA_ITEM_NOT_FOUND = -1;

        Table() {
        }

        String getKey(ICUResourceBundleReader reader, int index) {
            if (index < 0 || this.size <= index) {
                return null;
            }
            return this.keyOffsets != null ? reader.getKey16String(this.keyOffsets[index]) : reader.getKey32String(this.key32Offsets[index]);
        }

        int findTableItem(ICUResourceBundleReader reader, CharSequence key) {
            int start = 0;
            int limit = this.size;
            while (start < limit) {
                int mid = start + limit >>> 1;
                int result = this.keyOffsets != null ? reader.compareKeys(key, this.keyOffsets[mid]) : reader.compareKeys32(key, this.key32Offsets[mid]);
                if (result < 0) {
                    limit = mid;
                    continue;
                }
                if (result > 0) {
                    start = mid + 1;
                    continue;
                }
                return mid;
            }
            return -1;
        }

        @Override
        int getResource(ICUResourceBundleReader reader, String resKey) {
            return this.getContainerResource(reader, this.findTableItem(reader, resKey));
        }

        @Override
        public boolean getKeyAndValue(int i2, UResource.Key key, UResource.Value value) {
            if (0 <= i2 && i2 < this.size) {
                ReaderValue readerValue = (ReaderValue)value;
                if (this.keyOffsets != null) {
                    readerValue.reader.setKeyFromKey16(this.keyOffsets[i2], key);
                } else {
                    readerValue.reader.setKeyFromKey32(this.key32Offsets[i2], key);
                }
                readerValue.res = this.getContainerResource(readerValue.reader, i2);
                return true;
            }
            return false;
        }
    }

    private static final class Array16
    extends Array {
        @Override
        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return this.getContainer16Resource(reader, index);
        }

        Array16(ICUResourceBundleReader reader, int offset) {
            this.size = reader.b16BitUnits.charAt(offset);
            this.itemsOffset = offset + 1;
        }
    }

    private static final class Array32
    extends Array {
        @Override
        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return this.getContainer32Resource(reader, index);
        }

        Array32(ICUResourceBundleReader reader, int offset) {
            offset = reader.getResourceByteOffset(offset);
            this.size = reader.getInt(offset);
            this.itemsOffset = offset + 4;
        }
    }

    static class Array
    extends Container
    implements UResource.Array {
        Array() {
        }

        @Override
        public boolean getValue(int i2, UResource.Value value) {
            if (0 <= i2 && i2 < this.size) {
                ReaderValue readerValue = (ReaderValue)value;
                readerValue.res = this.getContainerResource(readerValue.reader, i2);
                return true;
            }
            return false;
        }
    }

    static class Container {
        protected int size;
        protected int itemsOffset;

        public final int getSize() {
            return this.size;
        }

        int getContainerResource(ICUResourceBundleReader reader, int index) {
            return -1;
        }

        protected int getContainer16Resource(ICUResourceBundleReader reader, int index) {
            if (index < 0 || this.size <= index) {
                return -1;
            }
            int res16 = reader.b16BitUnits.charAt(this.itemsOffset + index);
            if (res16 >= reader.poolStringIndex16Limit) {
                res16 = res16 - reader.poolStringIndex16Limit + reader.poolStringIndexLimit;
            }
            return 0x60000000 | res16;
        }

        protected int getContainer32Resource(ICUResourceBundleReader reader, int index) {
            if (index < 0 || this.size <= index) {
                return -1;
            }
            return reader.getInt(this.itemsOffset + 4 * index);
        }

        int getResource(ICUResourceBundleReader reader, String resKey) {
            return this.getContainerResource(reader, Integer.parseInt(resKey));
        }

        Container() {
        }
    }

    static class ReaderValue
    extends UResource.Value {
        ICUResourceBundleReader reader;
        int res;

        ReaderValue() {
        }

        @Override
        public int getType() {
            return PUBLIC_TYPES[ICUResourceBundleReader.RES_GET_TYPE(this.res)];
        }

        @Override
        public String getString() {
            String s2 = this.reader.getString(this.res);
            if (s2 == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return s2;
        }

        @Override
        public String getAliasString() {
            String s2 = this.reader.getAlias(this.res);
            if (s2 == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return s2;
        }

        @Override
        public int getInt() {
            if (ICUResourceBundleReader.RES_GET_TYPE(this.res) != 7) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return ICUResourceBundleReader.RES_GET_INT(this.res);
        }

        @Override
        public int getUInt() {
            if (ICUResourceBundleReader.RES_GET_TYPE(this.res) != 7) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return ICUResourceBundleReader.RES_GET_UINT(this.res);
        }

        @Override
        public int[] getIntVector() {
            int[] iv = this.reader.getIntVector(this.res);
            if (iv == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return iv;
        }

        @Override
        public ByteBuffer getBinary() {
            ByteBuffer bb = this.reader.getBinary(this.res);
            if (bb == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return bb;
        }

        @Override
        public UResource.Array getArray() {
            Array array = this.reader.getArray(this.res);
            if (array == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return array;
        }

        @Override
        public UResource.Table getTable() {
            Table table = this.reader.getTable(this.res);
            if (table == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return table;
        }

        @Override
        public boolean isNoInheritanceMarker() {
            return this.reader.isNoInheritanceMarker(this.res);
        }

        @Override
        public String[] getStringArray() {
            Array array = this.reader.getArray(this.res);
            if (array == null) {
                throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
            }
            return this.getStringArray(array);
        }

        @Override
        public String[] getStringArrayOrStringAsArray() {
            Array array = this.reader.getArray(this.res);
            if (array != null) {
                return this.getStringArray(array);
            }
            String s2 = this.reader.getString(this.res);
            if (s2 != null) {
                return new String[]{s2};
            }
            throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
        }

        @Override
        public String getStringOrFirstOfArray() {
            int r2;
            String s2 = this.reader.getString(this.res);
            if (s2 != null) {
                return s2;
            }
            Array array = this.reader.getArray(this.res);
            if (array != null && array.size > 0 && (s2 = this.reader.getString(r2 = array.getContainerResource(this.reader, 0))) != null) {
                return s2;
            }
            throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
        }

        private String[] getStringArray(Array array) {
            String[] result = new String[array.size];
            for (int i2 = 0; i2 < array.size; ++i2) {
                int r2 = array.getContainerResource(this.reader, i2);
                String s2 = this.reader.getString(r2);
                if (s2 == null) {
                    throw new UResourceTypeMismatchException(ICUResourceBundleReader.emptyString);
                }
                result[i2] = s2;
            }
            return result;
        }
    }

    private static class ReaderCache
    extends SoftCache<ReaderCacheKey, ICUResourceBundleReader, ClassLoader> {
        private ReaderCache() {
        }

        @Override
        protected ICUResourceBundleReader createInstance(ReaderCacheKey key, ClassLoader loader) {
            String fullName = ICUResourceBundleReader.getFullName(key.baseName, key.localeID);
            try {
                ByteBuffer inBytes;
                if (key.baseName != null && key.baseName.startsWith("com/ibm/icu/impl/data/icudt62b")) {
                    String itemPath = fullName.substring("com/ibm/icu/impl/data/icudt62b".length() + 1);
                    inBytes = ICUBinary.getData(loader, fullName, itemPath);
                    if (inBytes == null) {
                        return NULL_READER;
                    }
                } else {
                    InputStream stream = ICUData.getStream(loader, fullName);
                    if (stream == null) {
                        return NULL_READER;
                    }
                    inBytes = ICUBinary.getByteBufferFromInputStreamAndCloseStream(stream);
                }
                return new ICUResourceBundleReader(inBytes, key.baseName, key.localeID, loader);
            }
            catch (IOException ex) {
                throw new ICUUncheckedIOException("Data file " + fullName + " is corrupt - " + ex.getMessage(), ex);
            }
        }
    }

    private static class ReaderCacheKey {
        final String baseName;
        final String localeID;

        ReaderCacheKey(String baseName, String localeID) {
            this.baseName = baseName == null ? ICUResourceBundleReader.emptyString : baseName;
            this.localeID = localeID == null ? ICUResourceBundleReader.emptyString : localeID;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReaderCacheKey)) {
                return false;
            }
            ReaderCacheKey info = (ReaderCacheKey)obj;
            return this.baseName.equals(info.baseName) && this.localeID.equals(info.localeID);
        }

        public int hashCode() {
            return this.baseName.hashCode() ^ this.localeID.hashCode();
        }
    }

    private static final class IsAcceptable
    implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        @Override
        public boolean isDataVersionAcceptable(byte[] formatVersion) {
            return formatVersion[0] == 1 && (formatVersion[1] & 0xFF) >= 1 || 2 <= formatVersion[0] && formatVersion[0] <= 3;
        }
    }
}

