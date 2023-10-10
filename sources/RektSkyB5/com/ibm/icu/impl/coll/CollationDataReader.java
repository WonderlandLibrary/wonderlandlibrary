/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.Trie2_32;
import com.ibm.icu.impl.USerializedSet;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationFastLatin;
import com.ibm.icu.impl.coll.CollationSettings;
import com.ibm.icu.impl.coll.CollationTailoring;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ICUException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

final class CollationDataReader {
    static final int IX_INDEXES_LENGTH = 0;
    static final int IX_OPTIONS = 1;
    static final int IX_RESERVED2 = 2;
    static final int IX_RESERVED3 = 3;
    static final int IX_JAMO_CE32S_START = 4;
    static final int IX_REORDER_CODES_OFFSET = 5;
    static final int IX_REORDER_TABLE_OFFSET = 6;
    static final int IX_TRIE_OFFSET = 7;
    static final int IX_RESERVED8_OFFSET = 8;
    static final int IX_CES_OFFSET = 9;
    static final int IX_RESERVED10_OFFSET = 10;
    static final int IX_CE32S_OFFSET = 11;
    static final int IX_ROOT_ELEMENTS_OFFSET = 12;
    static final int IX_CONTEXTS_OFFSET = 13;
    static final int IX_UNSAFE_BWD_OFFSET = 14;
    static final int IX_FAST_LATIN_TABLE_OFFSET = 15;
    static final int IX_SCRIPTS_OFFSET = 16;
    static final int IX_COMPRESSIBLE_BYTES_OFFSET = 17;
    static final int IX_RESERVED18_OFFSET = 18;
    static final int IX_TOTAL_SIZE = 19;
    private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
    private static final int DATA_FORMAT = 1430482796;

    static void read(CollationTailoring base, ByteBuffer inBytes, CollationTailoring tailoring) throws IOException {
        int[] reorderCodes;
        int reorderCodesLength;
        int length;
        int i2;
        tailoring.version = ICUBinary.readHeader(inBytes, 1430482796, IS_ACCEPTABLE);
        if (base != null && base.getUCAVersion() != tailoring.getUCAVersion()) {
            throw new ICUException("Tailoring UCA version differs from base data UCA version");
        }
        int inLength = inBytes.remaining();
        if (inLength < 8) {
            throw new ICUException("not enough bytes");
        }
        int indexesLength = inBytes.getInt();
        if (indexesLength < 2 || inLength < indexesLength * 4) {
            throw new ICUException("not enough indexes");
        }
        int[] inIndexes = new int[20];
        inIndexes[0] = indexesLength;
        for (i2 = 1; i2 < indexesLength && i2 < inIndexes.length; ++i2) {
            inIndexes[i2] = inBytes.getInt();
        }
        for (i2 = indexesLength; i2 < inIndexes.length; ++i2) {
            inIndexes[i2] = -1;
        }
        if (indexesLength > inIndexes.length) {
            ICUBinary.skipBytes(inBytes, (indexesLength - inIndexes.length) * 4);
        }
        if (inLength < (length = indexesLength > 19 ? inIndexes[19] : (indexesLength > 5 ? inIndexes[indexesLength - 1] : 0))) {
            throw new ICUException("not enough bytes");
        }
        CollationData baseData = base == null ? null : base.data;
        int index = 5;
        int offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 4) {
            int reorderRangesLength;
            if (baseData == null) {
                throw new ICUException("Collation base data must not reorder scripts");
            }
            reorderCodesLength = length / 4;
            reorderCodes = ICUBinary.getInts(inBytes, reorderCodesLength, length & 3);
            for (reorderRangesLength = 0; reorderRangesLength < reorderCodesLength && (reorderCodes[reorderCodesLength - reorderRangesLength - 1] & 0xFFFF0000) != 0; ++reorderRangesLength) {
            }
            assert (reorderRangesLength < reorderCodesLength);
            reorderCodesLength -= reorderRangesLength;
        } else {
            reorderCodes = new int[]{};
            reorderCodesLength = 0;
            ICUBinary.skipBytes(inBytes, length);
        }
        byte[] reorderTable = null;
        index = 6;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 256) {
            if (reorderCodesLength == 0) {
                throw new ICUException("Reordering table without reordering codes");
            }
            reorderTable = new byte[256];
            inBytes.get(reorderTable);
            length -= 256;
        }
        ICUBinary.skipBytes(inBytes, length);
        if (baseData != null && baseData.numericPrimary != ((long)inIndexes[1] & 0xFF000000L)) {
            throw new ICUException("Tailoring numeric primary weight differs from base data");
        }
        CollationData data = null;
        index = 7;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 8) {
            tailoring.ensureOwnedData();
            data = tailoring.ownedData;
            data.base = baseData;
            data.numericPrimary = (long)inIndexes[1] & 0xFF000000L;
            data.trie = tailoring.trie = Trie2_32.createFromSerialized(inBytes);
            int trieLength = data.trie.getSerializedLength();
            if (trieLength > length) {
                throw new ICUException("Not enough bytes for the mappings trie");
            }
            length -= trieLength;
        } else if (baseData != null) {
            tailoring.data = baseData;
        } else {
            throw new ICUException("Missing collation data mappings");
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 8;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        ICUBinary.skipBytes(inBytes, length);
        index = 9;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 8) {
            if (data == null) {
                throw new ICUException("Tailored ces without tailored trie");
            }
            data.ces = ICUBinary.getLongs(inBytes, length / 8, length & 7);
        } else {
            ICUBinary.skipBytes(inBytes, length);
        }
        index = 10;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        ICUBinary.skipBytes(inBytes, length);
        index = 11;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 4) {
            if (data == null) {
                throw new ICUException("Tailored ce32s without tailored trie");
            }
            data.ce32s = ICUBinary.getInts(inBytes, length / 4, length & 3);
        } else {
            ICUBinary.skipBytes(inBytes, length);
        }
        int jamoCE32sStart = inIndexes[4];
        if (jamoCE32sStart >= 0) {
            if (data == null || data.ce32s == null) {
                throw new ICUException("JamoCE32sStart index into non-existent ce32s[]");
            }
            data.jamoCE32s = new int[67];
            System.arraycopy(data.ce32s, jamoCE32sStart, data.jamoCE32s, 0, 67);
        } else if (data != null) {
            if (baseData != null) {
                data.jamoCE32s = baseData.jamoCE32s;
            } else {
                throw new ICUException("Missing Jamo CE32s for Hangul processing");
            }
        }
        index = 12;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 4) {
            int rootElementsLength = length / 4;
            if (data == null) {
                throw new ICUException("Root elements but no mappings");
            }
            if (rootElementsLength <= 4) {
                throw new ICUException("Root elements array too short");
            }
            data.rootElements = new long[rootElementsLength];
            for (int i3 = 0; i3 < rootElementsLength; ++i3) {
                data.rootElements[i3] = (long)inBytes.getInt() & 0xFFFFFFFFL;
            }
            long commonSecTer = data.rootElements[3];
            if (commonSecTer != 0x5000500L) {
                throw new ICUException("Common sec/ter weights in base data differ from the hardcoded value");
            }
            long secTerBoundaries = data.rootElements[4];
            if (secTerBoundaries >>> 24 < 69L) {
                throw new ICUException("[fixed last secondary common byte] is too low");
            }
            length &= 3;
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 13;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 2) {
            if (data == null) {
                throw new ICUException("Tailored contexts without tailored trie");
            }
            data.contexts = ICUBinary.getString(inBytes, length / 2, length & 1);
        } else {
            ICUBinary.skipBytes(inBytes, length);
        }
        index = 14;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 2) {
            if (data == null) {
                throw new ICUException("Unsafe-backward-set but no mappings");
            }
            if (baseData == null) {
                tailoring.unsafeBackwardSet = new UnicodeSet(56320, 57343);
                data.nfcImpl.addLcccChars(tailoring.unsafeBackwardSet);
            } else {
                tailoring.unsafeBackwardSet = baseData.unsafeBackwardSet.cloneAsThawed();
            }
            USerializedSet sset = new USerializedSet();
            char[] unsafeData = ICUBinary.getChars(inBytes, length / 2, length & 1);
            length = 0;
            sset.getSet(unsafeData, 0);
            int count = sset.countRanges();
            int[] range = new int[2];
            for (int i4 = 0; i4 < count; ++i4) {
                sset.getRange(i4, range);
                tailoring.unsafeBackwardSet.add(range[0], range[1]);
            }
            int c2 = 65536;
            int lead = 55296;
            while (lead < 56320) {
                if (!tailoring.unsafeBackwardSet.containsNone(c2, c2 + 1023)) {
                    tailoring.unsafeBackwardSet.add(lead);
                }
                ++lead;
                c2 += 1024;
            }
            tailoring.unsafeBackwardSet.freeze();
            data.unsafeBackwardSet = tailoring.unsafeBackwardSet;
        } else if (data != null) {
            if (baseData != null) {
                data.unsafeBackwardSet = baseData.unsafeBackwardSet;
            } else {
                throw new ICUException("Missing unsafe-backward-set");
            }
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 15;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (data != null) {
            data.fastLatinTable = null;
            data.fastLatinTableHeader = null;
            if ((inIndexes[1] >> 16 & 0xFF) == 2) {
                if (length >= 2) {
                    char header0 = inBytes.getChar();
                    int headerLength = header0 & 0xFF;
                    data.fastLatinTableHeader = new char[headerLength];
                    data.fastLatinTableHeader[0] = header0;
                    for (int i5 = 1; i5 < headerLength; ++i5) {
                        data.fastLatinTableHeader[i5] = inBytes.getChar();
                    }
                    int tableLength = length / 2 - headerLength;
                    data.fastLatinTable = ICUBinary.getChars(inBytes, tableLength, length & 1);
                    length = 0;
                    if (header0 >> 8 != 2) {
                        throw new ICUException("Fast-Latin table version differs from version in data header");
                    }
                } else if (baseData != null) {
                    data.fastLatinTable = baseData.fastLatinTable;
                    data.fastLatinTableHeader = baseData.fastLatinTableHeader;
                }
            }
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 16;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 2) {
            if (data == null) {
                throw new ICUException("Script order data but no mappings");
            }
            int scriptsLength = length / 2;
            CharBuffer inChars = inBytes.asCharBuffer();
            data.numScripts = inChars.get();
            int scriptStartsLength = scriptsLength - (1 + data.numScripts + 16);
            if (scriptStartsLength <= 2) {
                throw new ICUException("Script order data too short");
            }
            data.scriptsIndex = new char[data.numScripts + 16];
            inChars.get(data.scriptsIndex);
            data.scriptStarts = new char[scriptStartsLength];
            inChars.get(data.scriptStarts);
            if (data.scriptStarts[0] != '\u0000' || data.scriptStarts[1] != '\u0300' || data.scriptStarts[scriptStartsLength - 1] != '\uff00') {
                throw new ICUException("Script order data not valid");
            }
        } else if (data != null && baseData != null) {
            data.numScripts = baseData.numScripts;
            data.scriptsIndex = baseData.scriptsIndex;
            data.scriptStarts = baseData.scriptStarts;
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 17;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        if (length >= 256) {
            if (data == null) {
                throw new ICUException("Data for compressible primary lead bytes but no mappings");
            }
            data.compressibleBytes = new boolean[256];
            for (int i6 = 0; i6 < 256; ++i6) {
                data.compressibleBytes[i6] = inBytes.get() != 0;
            }
            length -= 256;
        } else if (data != null) {
            if (baseData != null) {
                data.compressibleBytes = baseData.compressibleBytes;
            } else {
                throw new ICUException("Missing data for compressible primary lead bytes");
            }
        }
        ICUBinary.skipBytes(inBytes, length);
        index = 18;
        offset = inIndexes[index];
        length = inIndexes[index + 1] - offset;
        ICUBinary.skipBytes(inBytes, length);
        CollationSettings ts = tailoring.settings.readOnly();
        int options = inIndexes[1] & 0xFFFF;
        char[] fastLatinPrimaries = new char[384];
        int fastLatinOptions = CollationFastLatin.getOptions(tailoring.data, ts, fastLatinPrimaries);
        if (options == ts.options && ts.variableTop != 0L && Arrays.equals(reorderCodes, ts.reorderCodes) && fastLatinOptions == ts.fastLatinOptions && (fastLatinOptions < 0 || Arrays.equals(fastLatinPrimaries, ts.fastLatinPrimaries))) {
            return;
        }
        CollationSettings settings = tailoring.settings.copyOnWrite();
        settings.options = options;
        settings.variableTop = tailoring.data.getLastPrimaryForGroup(4096 + settings.getMaxVariable());
        if (settings.variableTop == 0L) {
            throw new ICUException("The maxVariable could not be mapped to a variableTop");
        }
        if (reorderCodesLength != 0) {
            settings.aliasReordering(baseData, reorderCodes, reorderCodesLength, reorderTable);
        }
        settings.fastLatinOptions = CollationFastLatin.getOptions(tailoring.data, settings, settings.fastLatinPrimaries);
    }

    private CollationDataReader() {
    }

    private static final class IsAcceptable
    implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        @Override
        public boolean isDataVersionAcceptable(byte[] version) {
            return version[0] == 5;
        }
    }
}

