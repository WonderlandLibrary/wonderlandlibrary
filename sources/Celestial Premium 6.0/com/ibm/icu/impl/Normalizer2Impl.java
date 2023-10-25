/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.Trie2Writable;
import com.ibm.icu.impl.Trie2_16;
import com.ibm.icu.impl.Trie2_32;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class Normalizer2Impl {
    private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
    private static final byte[] DATA_FORMAT = new byte[]{78, 114, 109, 50};
    private static final Trie2.ValueMapper segmentStarterMapper = new Trie2.ValueMapper(){

        public int map(int in) {
            return in & Integer.MIN_VALUE;
        }
    };
    public static final int MIN_CCC_LCCC_CP = 768;
    public static final int MIN_YES_YES_WITH_CC = 65281;
    public static final int JAMO_VT = 65280;
    public static final int MIN_NORMAL_MAYBE_YES = 65024;
    public static final int JAMO_L = 1;
    public static final int MAX_DELTA = 64;
    public static final int IX_NORM_TRIE_OFFSET = 0;
    public static final int IX_EXTRA_DATA_OFFSET = 1;
    public static final int IX_SMALL_FCD_OFFSET = 2;
    public static final int IX_RESERVED3_OFFSET = 3;
    public static final int IX_TOTAL_SIZE = 7;
    public static final int IX_MIN_DECOMP_NO_CP = 8;
    public static final int IX_MIN_COMP_NO_MAYBE_CP = 9;
    public static final int IX_MIN_YES_NO = 10;
    public static final int IX_MIN_NO_NO = 11;
    public static final int IX_LIMIT_NO_NO = 12;
    public static final int IX_MIN_MAYBE_YES = 13;
    public static final int IX_MIN_YES_NO_MAPPINGS_ONLY = 14;
    public static final int IX_COUNT = 16;
    public static final int MAPPING_HAS_CCC_LCCC_WORD = 128;
    public static final int MAPPING_HAS_RAW_MAPPING = 64;
    public static final int MAPPING_NO_COMP_BOUNDARY_AFTER = 32;
    public static final int MAPPING_LENGTH_MASK = 31;
    public static final int COMP_1_LAST_TUPLE = 32768;
    public static final int COMP_1_TRIPLE = 1;
    public static final int COMP_1_TRAIL_LIMIT = 13312;
    public static final int COMP_1_TRAIL_MASK = 32766;
    public static final int COMP_1_TRAIL_SHIFT = 9;
    public static final int COMP_2_TRAIL_SHIFT = 6;
    public static final int COMP_2_TRAIL_MASK = 65472;
    private VersionInfo dataVersion;
    private int minDecompNoCP;
    private int minCompNoMaybeCP;
    private int minYesNo;
    private int minYesNoMappingsOnly;
    private int minNoNo;
    private int limitNoNo;
    private int minMaybeYes;
    private Trie2_16 normTrie;
    private String maybeYesCompositions;
    private String extraData;
    private byte[] smallFCD;
    private int[] tccc180;
    private Trie2_32 canonIterData;
    private ArrayList<UnicodeSet> canonStartSets;
    private static final int CANON_NOT_SEGMENT_STARTER = Integer.MIN_VALUE;
    private static final int CANON_HAS_COMPOSITIONS = 0x40000000;
    private static final int CANON_HAS_SET = 0x200000;
    private static final int CANON_VALUE_MASK = 0x1FFFFF;

    public Normalizer2Impl load(InputStream data) {
        try {
            int i;
            BufferedInputStream bis = new BufferedInputStream(data);
            this.dataVersion = ICUBinary.readHeaderAndDataVersion(bis, DATA_FORMAT, IS_ACCEPTABLE);
            DataInputStream ds = new DataInputStream(bis);
            int indexesLength = ds.readInt() / 4;
            if (indexesLength <= 13) {
                throw new IOException("Normalizer2 data: not enough indexes");
            }
            int[] inIndexes = new int[indexesLength];
            inIndexes[0] = indexesLength * 4;
            for (int i2 = 1; i2 < indexesLength; ++i2) {
                inIndexes[i2] = ds.readInt();
            }
            this.minDecompNoCP = inIndexes[8];
            this.minCompNoMaybeCP = inIndexes[9];
            this.minYesNo = inIndexes[10];
            this.minYesNoMappingsOnly = inIndexes[14];
            this.minNoNo = inIndexes[11];
            this.limitNoNo = inIndexes[12];
            this.minMaybeYes = inIndexes[13];
            int offset = inIndexes[0];
            int nextOffset = inIndexes[1];
            this.normTrie = Trie2_16.createFromSerialized(ds);
            int trieLength = this.normTrie.getSerializedLength();
            if (trieLength > nextOffset - offset) {
                throw new IOException("Normalizer2 data: not enough bytes for normTrie");
            }
            ds.skipBytes(nextOffset - offset - trieLength);
            offset = nextOffset;
            nextOffset = inIndexes[2];
            int numChars = (nextOffset - offset) / 2;
            if (numChars != 0) {
                char[] chars = new char[numChars];
                for (i = 0; i < numChars; ++i) {
                    chars[i] = ds.readChar();
                }
                this.maybeYesCompositions = new String(chars);
                this.extraData = this.maybeYesCompositions.substring(65024 - this.minMaybeYes);
            }
            offset = nextOffset;
            this.smallFCD = new byte[256];
            for (i = 0; i < 256; ++i) {
                this.smallFCD[i] = ds.readByte();
            }
            this.tccc180 = new int[384];
            int bits = 0;
            int c = 0;
            while (c < 384) {
                if ((c & 0xFF) == 0) {
                    bits = this.smallFCD[c >> 8];
                }
                if (bits & true) {
                    int i3 = 0;
                    while (i3 < 32) {
                        this.tccc180[c] = this.getFCD16FromNormData(c) & 0xFF;
                        ++i3;
                        ++c;
                    }
                } else {
                    c += 32;
                }
                bits >>= 1;
            }
            data.close();
            return this;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Normalizer2Impl load(String name) {
        return this.load(ICUData.getRequiredStream(name));
    }

    public void addPropertyStarts(UnicodeSet set) {
        for (Trie2.Range range : this.normTrie) {
            if (range.leadSurrogate) break;
            set.add(range.startCodePoint);
        }
        for (int c = 44032; c < 55204; c += 28) {
            set.add(c);
            set.add(c + 1);
        }
        set.add(55204);
    }

    public void addCanonIterPropertyStarts(UnicodeSet set) {
        this.ensureCanonIterData();
        Iterator<Trie2.Range> trieIterator = this.canonIterData.iterator(segmentStarterMapper);
        while (trieIterator.hasNext()) {
            Trie2.Range range = trieIterator.next();
            if (range.leadSurrogate) break;
            set.add(range.startCodePoint);
        }
    }

    public Trie2_16 getNormTrie() {
        return this.normTrie;
    }

    public synchronized Normalizer2Impl ensureCanonIterData() {
        if (this.canonIterData == null) {
            Trie2Writable newData = new Trie2Writable(0, 0);
            this.canonStartSets = new ArrayList();
            for (Trie2.Range range : this.normTrie) {
                if (range.leadSurrogate) break;
                int norm16 = range.value;
                if (norm16 == 0 || this.minYesNo <= norm16 && norm16 < this.minNoNo) continue;
                for (int c = range.startCodePoint; c <= range.endCodePoint; ++c) {
                    int oldValue;
                    int newValue = oldValue = newData.get(c);
                    if (norm16 >= this.minMaybeYes) {
                        newValue |= Integer.MIN_VALUE;
                        if (norm16 < 65024) {
                            newValue |= 0x40000000;
                        }
                    } else if (norm16 < this.minYesNo) {
                        newValue |= 0x40000000;
                    } else {
                        int c2 = c;
                        int norm16_2 = norm16;
                        while (this.limitNoNo <= norm16_2 && norm16_2 < this.minMaybeYes) {
                            c2 = this.mapAlgorithmic(c2, norm16_2);
                            norm16_2 = this.getNorm16(c2);
                        }
                        if (this.minYesNo <= norm16_2 && norm16_2 < this.limitNoNo) {
                            char firstUnit = this.extraData.charAt(norm16_2);
                            int length = firstUnit & 0x1F;
                            if ((firstUnit & 0x80) != 0 && c == c2 && (this.extraData.charAt(norm16_2 - 1) & 0xFF) != 0) {
                                newValue |= Integer.MIN_VALUE;
                            }
                            if (length != 0) {
                                int limit = ++norm16_2 + length;
                                c2 = this.extraData.codePointAt(norm16_2);
                                this.addToStartSet(newData, c, c2);
                                if (norm16_2 >= this.minNoNo) {
                                    while ((norm16_2 += Character.charCount(c2)) < limit) {
                                        c2 = this.extraData.codePointAt(norm16_2);
                                        int c2Value = newData.get(c2);
                                        if ((c2Value & Integer.MIN_VALUE) != 0) continue;
                                        newData.set(c2, c2Value | Integer.MIN_VALUE);
                                    }
                                }
                            }
                        } else {
                            this.addToStartSet(newData, c, c2);
                        }
                    }
                    if (newValue == oldValue) continue;
                    newData.set(c, newValue);
                }
            }
            this.canonIterData = newData.toTrie2_32();
        }
        return this;
    }

    public int getNorm16(int c) {
        return this.normTrie.get(c);
    }

    public int getCompQuickCheck(int norm16) {
        if (norm16 < this.minNoNo || 65281 <= norm16) {
            return 1;
        }
        if (this.minMaybeYes <= norm16) {
            return 2;
        }
        return 0;
    }

    public boolean isCompNo(int norm16) {
        return this.minNoNo <= norm16 && norm16 < this.minMaybeYes;
    }

    public boolean isDecompYes(int norm16) {
        return norm16 < this.minYesNo || this.minMaybeYes <= norm16;
    }

    public int getCC(int norm16) {
        if (norm16 >= 65024) {
            return norm16 & 0xFF;
        }
        if (norm16 < this.minNoNo || this.limitNoNo <= norm16) {
            return 0;
        }
        return this.getCCFromNoNo(norm16);
    }

    public static int getCCFromYesOrMaybe(int norm16) {
        return norm16 >= 65024 ? norm16 & 0xFF : 0;
    }

    public int getFCD16(int c) {
        if (c < 0) {
            return 0;
        }
        if (c < 384) {
            return this.tccc180[c];
        }
        if (c <= 65535 && !this.singleLeadMightHaveNonZeroFCD16(c)) {
            return 0;
        }
        return this.getFCD16FromNormData(c);
    }

    public int getFCD16FromBelow180(int c) {
        return this.tccc180[c];
    }

    public boolean singleLeadMightHaveNonZeroFCD16(int lead) {
        byte bits = this.smallFCD[lead >> 8];
        if (bits == 0) {
            return false;
        }
        return (bits >> (lead >> 5 & 7) & 1) != 0;
    }

    public int getFCD16FromNormData(int c) {
        int norm16;
        while (true) {
            if ((norm16 = this.getNorm16(c)) <= this.minYesNo) {
                return 0;
            }
            if (norm16 >= 65024) {
                return (norm16 &= 0xFF) | norm16 << 8;
            }
            if (norm16 >= this.minMaybeYes) {
                return 0;
            }
            if (!this.isDecompNoAlgorithmic(norm16)) break;
            c = this.mapAlgorithmic(c, norm16);
        }
        char firstUnit = this.extraData.charAt(norm16);
        if ((firstUnit & 0x1F) == 0) {
            return 511;
        }
        int fcd16 = firstUnit >> 8;
        if ((firstUnit & 0x80) != 0) {
            fcd16 |= this.extraData.charAt(norm16 - 1) & 0xFF00;
        }
        return fcd16;
    }

    public String getDecomposition(int c) {
        int norm16;
        int decomp = -1;
        while (c >= this.minDecompNoCP && !this.isDecompYes(norm16 = this.getNorm16(c))) {
            if (this.isHangul(norm16)) {
                StringBuilder buffer = new StringBuilder();
                Hangul.decompose(c, buffer);
                return buffer.toString();
            }
            if (this.isDecompNoAlgorithmic(norm16)) {
                decomp = c = this.mapAlgorithmic(c, norm16);
                continue;
            }
            int length = this.extraData.charAt(norm16++) & 0x1F;
            return this.extraData.substring(norm16, norm16 + length);
        }
        if (decomp < 0) {
            return null;
        }
        return UTF16.valueOf(decomp);
    }

    public String getRawDecomposition(int c) {
        int norm16;
        if (c < this.minDecompNoCP || this.isDecompYes(norm16 = this.getNorm16(c))) {
            return null;
        }
        if (this.isHangul(norm16)) {
            StringBuilder buffer = new StringBuilder();
            Hangul.getRawDecomposition(c, buffer);
            return buffer.toString();
        }
        if (this.isDecompNoAlgorithmic(norm16)) {
            return UTF16.valueOf(this.mapAlgorithmic(c, norm16));
        }
        char firstUnit = this.extraData.charAt(norm16);
        int mLength = firstUnit & 0x1F;
        if ((firstUnit & 0x40) != 0) {
            int rawMapping = norm16 - (firstUnit >> 7 & 1) - 1;
            char rm0 = this.extraData.charAt(rawMapping);
            if (rm0 <= '\u001f') {
                return this.extraData.substring(rawMapping - rm0, rawMapping);
            }
            StringBuilder buffer = new StringBuilder(mLength - 1).append(rm0);
            return buffer.append(this.extraData, norm16 += 3, norm16 + mLength - 2).toString();
        }
        return this.extraData.substring(++norm16, norm16 + mLength);
    }

    public boolean isCanonSegmentStarter(int c) {
        return this.canonIterData.get(c) >= 0;
    }

    public boolean getCanonStartSet(int c, UnicodeSet set) {
        int canonValue = this.canonIterData.get(c) & Integer.MAX_VALUE;
        if (canonValue == 0) {
            return false;
        }
        set.clear();
        int value = canonValue & 0x1FFFFF;
        if ((canonValue & 0x200000) != 0) {
            set.addAll(this.canonStartSets.get(value));
        } else if (value != 0) {
            set.add(value);
        }
        if ((canonValue & 0x40000000) != 0) {
            int norm16 = this.getNorm16(c);
            if (norm16 == 1) {
                int syllable = 44032 + (c - 4352) * 588;
                set.add(syllable, syllable + 588 - 1);
            } else {
                this.addComposites(this.getCompositionsList(norm16), set);
            }
        }
        return true;
    }

    public int decompose(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
        block11: {
            int minNoCP = this.minDecompNoCP;
            int c = 0;
            int norm16 = 0;
            int prevBoundary = src;
            int prevCC = 0;
            while (true) {
                int cc;
                int prevSrc = src;
                while (src != limit) {
                    char c2;
                    c = s.charAt(src);
                    if (c < minNoCP || this.isMostDecompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
                        ++src;
                        continue;
                    }
                    if (!UTF16.isSurrogate((char)c)) break;
                    if (UTF16Plus.isSurrogateLead(c)) {
                        if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1))) {
                            c = Character.toCodePoint((char)c, c2);
                        }
                    } else if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
                        --src;
                        c = Character.toCodePoint(c2, (char)c);
                    }
                    if (!this.isMostDecompYesAndZeroCC(norm16 = this.getNorm16(c))) break;
                    src += Character.charCount(c);
                }
                if (src != prevSrc) {
                    if (buffer != null) {
                        buffer.flushAndAppendZeroCC(s, prevSrc, src);
                    } else {
                        prevCC = 0;
                        prevBoundary = src;
                    }
                }
                if (src == limit) break block11;
                src += Character.charCount(c);
                if (buffer != null) {
                    this.decompose(c, norm16, buffer);
                    continue;
                }
                if (!this.isDecompYes(norm16) || prevCC > (cc = Normalizer2Impl.getCCFromYesOrMaybe(norm16)) && cc != 0) break;
                prevCC = cc;
                if (cc > 1) continue;
                prevBoundary = src;
            }
            return prevBoundary;
        }
        return src;
    }

    public void decomposeAndAppend(CharSequence s, boolean doDecompose, ReorderingBuffer buffer) {
        int cc;
        int limit = s.length();
        if (limit == 0) {
            return;
        }
        if (doDecompose) {
            this.decompose(s, 0, limit, buffer);
            return;
        }
        int c = Character.codePointAt(s, 0);
        int src = 0;
        int prevCC = cc = this.getCC(this.getNorm16(c));
        int firstCC = cc;
        while (cc != 0) {
            prevCC = cc;
            if ((src += Character.charCount(c)) >= limit) break;
            c = Character.codePointAt(s, src);
            cc = this.getCC(this.getNorm16(c));
        }
        buffer.append(s, 0, src, firstCC, prevCC);
        buffer.append(s, src, limit);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean compose(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doCompose, ReorderingBuffer buffer) {
        minNoMaybeCP = this.minCompNoMaybeCP;
        prevBoundary = src;
        c = 0;
        norm16 = 0;
        prevCC = 0;
        while (true) {
            prevSrc = src;
            while (src != limit) {
                c = s.charAt(src);
                if (c < minNoMaybeCP || this.isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
                    ++src;
                    continue;
                }
                if (!UTF16.isSurrogate((char)c)) break;
                if (UTF16Plus.isSurrogateLead(c)) {
                    if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1))) {
                        c = Character.toCodePoint((char)c, c2);
                    }
                } else if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
                    --src;
                    c = Character.toCodePoint(c2, (char)c);
                }
                if (!this.isCompYesAndZeroCC(norm16 = this.getNorm16(c))) break;
                src += Character.charCount(c);
            }
            if (src != prevSrc) {
                if (src == limit) {
                    if (doCompose == false) return true;
                    buffer.flushAndAppendZeroCC(s, prevSrc, src);
                    return true;
                }
                prevBoundary = src - 1;
                if (Character.isLowSurrogate(s.charAt(prevBoundary)) && prevSrc < prevBoundary && Character.isHighSurrogate(s.charAt(prevBoundary - 1))) {
                    --prevBoundary;
                }
                if (doCompose) {
                    buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
                    buffer.append(s, prevBoundary, src);
                } else {
                    prevCC = 0;
                }
                prevSrc = src;
            } else if (src == limit) {
                return true;
            }
            src += Character.charCount(c);
            if (Normalizer2Impl.isJamoVT(norm16) && prevBoundary != prevSrc) {
                prev = s.charAt(prevSrc - 1);
                needToDecompose = false;
                if (c < 4519) {
                    if ((prev = (char)(prev - 4352)) < '\u0013') {
                        if (!doCompose) {
                            return false;
                        }
                        syllable = (char)(44032 + (prev * 21 + (c - 4449)) * 28);
                        if (src != limit && (t = (char)(s.charAt(src) - 4519)) < '\u001c') {
                            syllable = (char)(syllable + t);
                            prevBoundary = ++src;
                            buffer.setLastChar(syllable);
                            continue;
                        }
                        needToDecompose = true;
                    }
                } else if (Hangul.isHangulWithoutJamoT(prev)) {
                    if (!doCompose) {
                        return false;
                    }
                    buffer.setLastChar((char)(prev + c - 4519));
                    prevBoundary = src;
                    continue;
                }
                if (!needToDecompose) {
                    if (doCompose) {
                        buffer.append((char)c);
                        continue;
                    }
                    prevCC = 0;
                    continue;
                }
            }
            if (norm16 < 65281) ** GOTO lbl85
            cc = norm16 & 255;
            if (onlyContiguous && (doCompose != false ? buffer.getLastCC() : prevCC) == 0 && prevBoundary < prevSrc && this.getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc) {
                if (!doCompose) {
                    return false;
                }
            } else {
                if (doCompose) {
                    buffer.append(c, cc);
                    continue;
                }
                if (prevCC > cc) return false;
                prevCC = cc;
                continue;
lbl85:
                // 1 sources

                if (!doCompose && !this.isMaybeOrNonZeroCC(norm16)) {
                    return false;
                }
            }
            if (this.hasCompBoundaryBefore(c, norm16)) {
                prevBoundary = prevSrc;
            } else if (doCompose) {
                buffer.removeSuffix(prevSrc - prevBoundary);
            }
            src = this.findNextCompBoundary(s, src, limit);
            recomposeStartIndex = buffer.length();
            this.decomposeShort(s, prevBoundary, src, buffer);
            this.recompose(buffer, recomposeStartIndex, onlyContiguous);
            if (!doCompose) {
                if (!buffer.equals(s, prevBoundary, src)) {
                    return false;
                }
                buffer.remove();
                prevCC = 0;
            }
            prevBoundary = src;
        }
    }

    public int composeQuickCheck(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doSpan) {
        int prevBoundary;
        block10: {
            int qcResult = 0;
            int minNoMaybeCP = this.minCompNoMaybeCP;
            prevBoundary = src;
            int c = 0;
            int norm16 = 0;
            int prevCC = 0;
            while (true) {
                int prevSrc = src;
                while (true) {
                    char c2;
                    if (src == limit) {
                        return src << 1 | qcResult;
                    }
                    c = s.charAt(src);
                    if (c < minNoMaybeCP || this.isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
                        ++src;
                        continue;
                    }
                    if (!UTF16.isSurrogate((char)c)) break;
                    if (UTF16Plus.isSurrogateLead(c)) {
                        if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1))) {
                            c = Character.toCodePoint((char)c, c2);
                        }
                    } else if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
                        --src;
                        c = Character.toCodePoint(c2, (char)c);
                    }
                    if (!this.isCompYesAndZeroCC(norm16 = this.getNorm16(c))) break;
                    src += Character.charCount(c);
                }
                if (src != prevSrc) {
                    prevBoundary = src - 1;
                    if (Character.isLowSurrogate(s.charAt(prevBoundary)) && prevSrc < prevBoundary && Character.isHighSurrogate(s.charAt(prevBoundary - 1))) {
                        --prevBoundary;
                    }
                    prevCC = 0;
                    prevSrc = src;
                }
                src += Character.charCount(c);
                if (!this.isMaybeOrNonZeroCC(norm16)) break block10;
                int cc = Normalizer2Impl.getCCFromYesOrMaybe(norm16);
                if (onlyContiguous && cc != 0 && prevCC == 0 && prevBoundary < prevSrc && this.getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc || prevCC > cc && cc != 0) break block10;
                prevCC = cc;
                if (norm16 >= 65281) continue;
                if (doSpan) break;
                qcResult = 1;
            }
            return prevBoundary << 1;
        }
        return prevBoundary << 1;
    }

    public void composeAndAppend(CharSequence s, boolean doCompose, boolean onlyContiguous, ReorderingBuffer buffer) {
        int firstStarterInSrc;
        int src = 0;
        int limit = s.length();
        if (!buffer.isEmpty() && 0 != (firstStarterInSrc = this.findNextCompBoundary(s, 0, limit))) {
            int lastStarterInDest = this.findPreviousCompBoundary(buffer.getStringBuilder(), buffer.length());
            StringBuilder middle = new StringBuilder(buffer.length() - lastStarterInDest + firstStarterInSrc + 16);
            middle.append(buffer.getStringBuilder(), lastStarterInDest, buffer.length());
            buffer.removeSuffix(buffer.length() - lastStarterInDest);
            middle.append(s, 0, firstStarterInSrc);
            this.compose(middle, 0, middle.length(), onlyContiguous, true, buffer);
            src = firstStarterInSrc;
        }
        if (doCompose) {
            this.compose(s, src, limit, onlyContiguous, true, buffer);
        } else {
            buffer.append(s, src, limit);
        }
    }

    public int makeFCD(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
        int prevBoundary = src;
        int c = 0;
        int prevFCD16 = 0;
        int fcd16 = 0;
        while (true) {
            int prevSrc = src;
            while (src != limit) {
                c = s.charAt(src);
                if (c < 768) {
                    prevFCD16 = ~c;
                    ++src;
                    continue;
                }
                if (!this.singleLeadMightHaveNonZeroFCD16(c)) {
                    prevFCD16 = 0;
                    ++src;
                    continue;
                }
                if (UTF16.isSurrogate((char)c)) {
                    char c2;
                    if (UTF16Plus.isSurrogateLead(c)) {
                        if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1))) {
                            c = Character.toCodePoint((char)c, c2);
                        }
                    } else if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
                        --src;
                        c = Character.toCodePoint(c2, (char)c);
                    }
                }
                if ((fcd16 = this.getFCD16FromNormData(c)) > 255) break;
                prevFCD16 = fcd16;
                src += Character.charCount(c);
            }
            if (src != prevSrc) {
                if (src == limit) {
                    if (buffer == null) break;
                    buffer.flushAndAppendZeroCC(s, prevSrc, src);
                    break;
                }
                prevBoundary = src;
                if (prevFCD16 < 0) {
                    int prev = ~prevFCD16;
                    int n = prevFCD16 = prev < 384 ? this.tccc180[prev] : this.getFCD16FromNormData(prev);
                    if (prevFCD16 > 1) {
                        --prevBoundary;
                    }
                } else {
                    int p = src - 1;
                    if (Character.isLowSurrogate(s.charAt(p)) && prevSrc < p && Character.isHighSurrogate(s.charAt(p - 1))) {
                        prevFCD16 = this.getFCD16FromNormData(Character.toCodePoint(s.charAt(--p), s.charAt(p + 1)));
                    }
                    if (prevFCD16 > 1) {
                        prevBoundary = p;
                    }
                }
                if (buffer != null) {
                    buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
                    buffer.append(s, prevBoundary, src);
                }
                prevSrc = src;
            } else if (src == limit) break;
            src += Character.charCount(c);
            if ((prevFCD16 & 0xFF) <= fcd16 >> 8) {
                if ((fcd16 & 0xFF) <= 1) {
                    prevBoundary = src;
                }
                if (buffer != null) {
                    buffer.appendZeroCC(c);
                }
                prevFCD16 = fcd16;
                continue;
            }
            if (buffer == null) {
                return prevBoundary;
            }
            buffer.removeSuffix(prevSrc - prevBoundary);
            src = this.findNextFCDBoundary(s, src, limit);
            this.decomposeShort(s, prevBoundary, src, buffer);
            prevBoundary = src;
            prevFCD16 = 0;
        }
        return src;
    }

    public void makeFCDAndAppend(CharSequence s, boolean doMakeFCD, ReorderingBuffer buffer) {
        int firstBoundaryInSrc;
        int src = 0;
        int limit = s.length();
        if (!buffer.isEmpty() && 0 != (firstBoundaryInSrc = this.findNextFCDBoundary(s, 0, limit))) {
            int lastBoundaryInDest = this.findPreviousFCDBoundary(buffer.getStringBuilder(), buffer.length());
            StringBuilder middle = new StringBuilder(buffer.length() - lastBoundaryInDest + firstBoundaryInSrc + 16);
            middle.append(buffer.getStringBuilder(), lastBoundaryInDest, buffer.length());
            buffer.removeSuffix(buffer.length() - lastBoundaryInDest);
            middle.append(s, 0, firstBoundaryInSrc);
            this.makeFCD(middle, 0, middle.length(), buffer);
            src = firstBoundaryInSrc;
        }
        if (doMakeFCD) {
            this.makeFCD(s, src, limit, buffer);
        } else {
            buffer.append(s, src, limit);
        }
    }

    public boolean hasDecompBoundary(int c, boolean before) {
        int norm16;
        while (true) {
            if (c < this.minDecompNoCP) {
                return true;
            }
            norm16 = this.getNorm16(c);
            if (this.isHangul(norm16) || this.isDecompYesAndZeroCC(norm16)) {
                return true;
            }
            if (norm16 > 65024) {
                return false;
            }
            if (!this.isDecompNoAlgorithmic(norm16)) break;
            c = this.mapAlgorithmic(c, norm16);
        }
        char firstUnit = this.extraData.charAt(norm16);
        if ((firstUnit & 0x1F) == 0) {
            return false;
        }
        if (!before) {
            if (firstUnit > '\u01ff') {
                return false;
            }
            if (firstUnit <= '\u00ff') {
                return true;
            }
        }
        return (firstUnit & 0x80) == 0 || (this.extraData.charAt(norm16 - 1) & 0xFF00) == 0;
    }

    public boolean isDecompInert(int c) {
        return this.isDecompYesAndZeroCC(this.getNorm16(c));
    }

    public boolean hasCompBoundaryBefore(int c) {
        return c < this.minCompNoMaybeCP || this.hasCompBoundaryBefore(c, this.getNorm16(c));
    }

    public boolean hasCompBoundaryAfter(int c, boolean onlyContiguous, boolean testInert) {
        int norm16;
        while (true) {
            if (Normalizer2Impl.isInert(norm16 = this.getNorm16(c))) {
                return true;
            }
            if (norm16 <= this.minYesNo) {
                return this.isHangul(norm16) && !Hangul.isHangulWithoutJamoT((char)c);
            }
            if (norm16 >= (testInert ? this.minNoNo : this.minMaybeYes)) {
                return false;
            }
            if (!this.isDecompNoAlgorithmic(norm16)) break;
            c = this.mapAlgorithmic(c, norm16);
        }
        char firstUnit = this.extraData.charAt(norm16);
        return (firstUnit & 0x20) == 0 && (!onlyContiguous || firstUnit <= '\u01ff');
    }

    public boolean hasFCDBoundaryBefore(int c) {
        return c < 768 || this.getFCD16(c) <= 255;
    }

    public boolean hasFCDBoundaryAfter(int c) {
        int fcd16 = this.getFCD16(c);
        return fcd16 <= 1 || (fcd16 & 0xFF) == 0;
    }

    public boolean isFCDInert(int c) {
        return this.getFCD16(c) <= 1;
    }

    private boolean isMaybe(int norm16) {
        return this.minMaybeYes <= norm16 && norm16 <= 65280;
    }

    private boolean isMaybeOrNonZeroCC(int norm16) {
        return norm16 >= this.minMaybeYes;
    }

    private static boolean isInert(int norm16) {
        return norm16 == 0;
    }

    private static boolean isJamoL(int norm16) {
        return norm16 == 1;
    }

    private static boolean isJamoVT(int norm16) {
        return norm16 == 65280;
    }

    private boolean isHangul(int norm16) {
        return norm16 == this.minYesNo;
    }

    private boolean isCompYesAndZeroCC(int norm16) {
        return norm16 < this.minNoNo;
    }

    private boolean isDecompYesAndZeroCC(int norm16) {
        return norm16 < this.minYesNo || norm16 == 65280 || this.minMaybeYes <= norm16 && norm16 <= 65024;
    }

    private boolean isMostDecompYesAndZeroCC(int norm16) {
        return norm16 < this.minYesNo || norm16 == 65024 || norm16 == 65280;
    }

    private boolean isDecompNoAlgorithmic(int norm16) {
        return norm16 >= this.limitNoNo;
    }

    private int getCCFromNoNo(int norm16) {
        if ((this.extraData.charAt(norm16) & 0x80) != 0) {
            return this.extraData.charAt(norm16 - 1) & 0xFF;
        }
        return 0;
    }

    int getTrailCCFromCompYesAndZeroCC(CharSequence s, int cpStart, int cpLimit) {
        int c = cpStart == cpLimit - 1 ? s.charAt(cpStart) : Character.codePointAt(s, cpStart);
        int prevNorm16 = this.getNorm16(c);
        if (prevNorm16 <= this.minYesNo) {
            return 0;
        }
        return this.extraData.charAt(prevNorm16) >> 8;
    }

    private int mapAlgorithmic(int c, int norm16) {
        return c + norm16 - (this.minMaybeYes - 64 - 1);
    }

    private int getCompositionsListForDecompYes(int norm16) {
        if (norm16 == 0 || 65024 <= norm16) {
            return -1;
        }
        if ((norm16 -= this.minMaybeYes) < 0) {
            norm16 += 65024;
        }
        return norm16;
    }

    private int getCompositionsListForComposite(int norm16) {
        char firstUnit = this.extraData.charAt(norm16);
        return 65024 - this.minMaybeYes + norm16 + 1 + (firstUnit & 0x1F);
    }

    private int getCompositionsList(int norm16) {
        return this.isDecompYes(norm16) ? this.getCompositionsListForDecompYes(norm16) : this.getCompositionsListForComposite(norm16);
    }

    public void decomposeShort(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
        while (src < limit) {
            int c = Character.codePointAt(s, src);
            src += Character.charCount(c);
            this.decompose(c, this.getNorm16(c), buffer);
        }
    }

    private void decompose(int c, int norm16, ReorderingBuffer buffer) {
        block3: {
            while (true) {
                if (this.isDecompYes(norm16)) {
                    buffer.append(c, Normalizer2Impl.getCCFromYesOrMaybe(norm16));
                    break block3;
                }
                if (this.isHangul(norm16)) {
                    Hangul.decompose(c, buffer);
                    break block3;
                }
                if (!this.isDecompNoAlgorithmic(norm16)) break;
                c = this.mapAlgorithmic(c, norm16);
                norm16 = this.getNorm16(c);
            }
            char firstUnit = this.extraData.charAt(norm16);
            int length = firstUnit & 0x1F;
            int trailCC = firstUnit >> 8;
            int leadCC = (firstUnit & 0x80) != 0 ? this.extraData.charAt(norm16 - 1) >> 8 : 0;
            buffer.append(this.extraData, ++norm16, norm16 + length, leadCC, trailCC);
        }
    }

    private static int combine(String compositions, int list, int trail) {
        block9: {
            if (trail < 13312) {
                char firstUnit;
                int key1 = trail << 1;
                while (key1 > (firstUnit = compositions.charAt(list))) {
                    list += 2 + (firstUnit & '\u0001');
                }
                if (key1 == (firstUnit & 0x7FFE)) {
                    if ((firstUnit & '\u0001') != 0) {
                        return compositions.charAt(list + 1) << 16 | compositions.charAt(list + 2);
                    }
                    return compositions.charAt(list + 1);
                }
            } else {
                char secondUnit;
                int key1 = 13312 + (trail >> 9 & 0xFFFFFFFE);
                int key2 = trail << 6 & 0xFFFF;
                while (true) {
                    char firstUnit;
                    if (key1 > (firstUnit = compositions.charAt(list))) {
                        list += 2 + (firstUnit & '\u0001');
                        continue;
                    }
                    if (key1 != (firstUnit & 0x7FFE)) break block9;
                    secondUnit = compositions.charAt(list + 1);
                    if (key2 <= secondUnit) break;
                    if ((firstUnit & 0x8000) == 0) {
                        list += 3;
                        continue;
                    }
                    break block9;
                    break;
                }
                if (key2 == (secondUnit & 0xFFC0)) {
                    return (secondUnit & 0xFFFF003F) << 16 | compositions.charAt(list + 2);
                }
            }
        }
        return -1;
    }

    private void addComposites(int list, UnicodeSet set) {
        char firstUnit;
        do {
            int compositeAndFwd;
            if (((firstUnit = this.maybeYesCompositions.charAt(list)) & '\u0001') == 0) {
                compositeAndFwd = this.maybeYesCompositions.charAt(list + 1);
                list += 2;
            } else {
                compositeAndFwd = (this.maybeYesCompositions.charAt(list + 1) & 0xFFFF003F) << 16 | this.maybeYesCompositions.charAt(list + 2);
                list += 3;
            }
            int composite = compositeAndFwd >> 1;
            if ((compositeAndFwd & '\u0001') != 0) {
                this.addComposites(this.getCompositionsListForComposite(this.getNorm16(composite)), set);
            }
            set.add(composite);
        } while ((firstUnit & 0x8000) == 0);
    }

    private void recompose(ReorderingBuffer buffer, int recomposeStartIndex, boolean onlyContiguous) {
        int p = recomposeStartIndex;
        StringBuilder sb = buffer.getStringBuilder();
        if (p == sb.length()) {
            return;
        }
        int compositionsList = -1;
        int starter = -1;
        boolean starterIsSupplementary = false;
        int prevCC = 0;
        while (true) {
            int c = sb.codePointAt(p);
            p += Character.charCount(c);
            int norm16 = this.getNorm16(c);
            int cc = Normalizer2Impl.getCCFromYesOrMaybe(norm16);
            if (this.isMaybe(norm16) && compositionsList >= 0 && (prevCC < cc || prevCC == 0)) {
                int pRemove;
                if (Normalizer2Impl.isJamoVT(norm16)) {
                    char prev;
                    if (c < 4519 && (prev = (char)(sb.charAt(starter) - 4352)) < '\u0013') {
                        char t;
                        pRemove = p - 1;
                        char syllable = (char)(44032 + (prev * 21 + (c - 4449)) * 28);
                        if (p != sb.length() && (t = (char)(sb.charAt(p) - 4519)) < '\u001c') {
                            ++p;
                            syllable = (char)(syllable + t);
                        }
                        sb.setCharAt(starter, syllable);
                        sb.delete(pRemove, p);
                        p = pRemove;
                    }
                    if (p == sb.length()) break;
                    compositionsList = -1;
                    continue;
                }
                int compositeAndFwd = Normalizer2Impl.combine(this.maybeYesCompositions, compositionsList, c);
                if (compositeAndFwd >= 0) {
                    int composite = compositeAndFwd >> 1;
                    pRemove = p - Character.charCount(c);
                    sb.delete(pRemove, p);
                    p = pRemove;
                    if (starterIsSupplementary) {
                        if (composite > 65535) {
                            sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
                            sb.setCharAt(starter + 1, UTF16.getTrailSurrogate(composite));
                        } else {
                            sb.setCharAt(starter, (char)c);
                            sb.deleteCharAt(starter + 1);
                            starterIsSupplementary = false;
                            --p;
                        }
                    } else if (composite > 65535) {
                        starterIsSupplementary = true;
                        sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
                        sb.insert(starter + 1, UTF16.getTrailSurrogate(composite));
                        ++p;
                    } else {
                        sb.setCharAt(starter, (char)composite);
                    }
                    if (p == sb.length()) break;
                    if ((compositeAndFwd & 1) != 0) {
                        compositionsList = this.getCompositionsListForComposite(this.getNorm16(composite));
                        continue;
                    }
                    compositionsList = -1;
                    continue;
                }
            }
            prevCC = cc;
            if (p == sb.length()) break;
            if (cc == 0) {
                compositionsList = this.getCompositionsListForDecompYes(norm16);
                if (compositionsList < 0) continue;
                if (c <= 65535) {
                    starterIsSupplementary = false;
                    starter = p - 1;
                    continue;
                }
                starterIsSupplementary = true;
                starter = p - 2;
                continue;
            }
            if (!onlyContiguous) continue;
            compositionsList = -1;
        }
        buffer.flush();
    }

    public int composePair(int a, int b) {
        int list;
        int norm16 = this.getNorm16(a);
        if (Normalizer2Impl.isInert(norm16)) {
            return -1;
        }
        if (norm16 < this.minYesNoMappingsOnly) {
            if (Normalizer2Impl.isJamoL(norm16)) {
                if (0 <= (b -= 4449) && b < 21) {
                    return 44032 + ((a - 4352) * 21 + b) * 28;
                }
                return -1;
            }
            if (this.isHangul(norm16)) {
                if (Hangul.isHangulWithoutJamoT((char)a) && 0 < (b -= 4519) && b < 28) {
                    return a + b;
                }
                return -1;
            }
            list = norm16;
            if (norm16 > this.minYesNo) {
                list += 1 + (this.extraData.charAt(list) & 0x1F);
            }
            list += 65024 - this.minMaybeYes;
        } else {
            if (norm16 < this.minMaybeYes || 65024 <= norm16) {
                return -1;
            }
            list = norm16 - this.minMaybeYes;
        }
        if (b < 0 || 0x10FFFF < b) {
            return -1;
        }
        return Normalizer2Impl.combine(this.maybeYesCompositions, list, b) >> 1;
    }

    private boolean hasCompBoundaryBefore(int c, int norm16) {
        while (true) {
            if (this.isCompYesAndZeroCC(norm16)) {
                return true;
            }
            if (this.isMaybeOrNonZeroCC(norm16)) {
                return false;
            }
            if (!this.isDecompNoAlgorithmic(norm16)) break;
            c = this.mapAlgorithmic(c, norm16);
            norm16 = this.getNorm16(c);
        }
        char firstUnit = this.extraData.charAt(norm16);
        if ((firstUnit & 0x1F) == 0) {
            return false;
        }
        if ((firstUnit & 0x80) != 0 && (this.extraData.charAt(norm16 - 1) & 0xFF00) != 0) {
            return false;
        }
        return this.isCompYesAndZeroCC(this.getNorm16(Character.codePointAt(this.extraData, norm16 + 1)));
    }

    private int findPreviousCompBoundary(CharSequence s, int p) {
        while (p > 0) {
            int c = Character.codePointBefore(s, p);
            p -= Character.charCount(c);
            if (!this.hasCompBoundaryBefore(c)) continue;
            break;
        }
        return p;
    }

    private int findNextCompBoundary(CharSequence s, int p, int limit) {
        int norm16;
        int c;
        while (p < limit && !this.hasCompBoundaryBefore(c = Character.codePointAt(s, p), norm16 = this.normTrie.get(c))) {
            p += Character.charCount(c);
        }
        return p;
    }

    private int findPreviousFCDBoundary(CharSequence s, int p) {
        while (p > 0) {
            int c = Character.codePointBefore(s, p);
            p -= Character.charCount(c);
            if (c >= 768 && this.getFCD16(c) > 255) continue;
            break;
        }
        return p;
    }

    private int findNextFCDBoundary(CharSequence s, int p, int limit) {
        int c;
        while (p < limit && (c = Character.codePointAt(s, p)) >= 768 && this.getFCD16(c) > 255) {
            p += Character.charCount(c);
        }
        return p;
    }

    private void addToStartSet(Trie2Writable newData, int origin, int decompLead) {
        int canonValue = newData.get(decompLead);
        if ((canonValue & 0x3FFFFF) == 0 && origin != 0) {
            newData.set(decompLead, canonValue | origin);
        } else {
            UnicodeSet set;
            if ((canonValue & 0x200000) == 0) {
                int firstOrigin = canonValue & 0x1FFFFF;
                canonValue = canonValue & 0xFFE00000 | 0x200000 | this.canonStartSets.size();
                newData.set(decompLead, canonValue);
                set = new UnicodeSet();
                this.canonStartSets.add(set);
                if (firstOrigin != 0) {
                    set.add(firstOrigin);
                }
            } else {
                set = this.canonStartSets.get(canonValue & 0x1FFFFF);
            }
            set.add(origin);
        }
    }

    private static final class IsAcceptable
    implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        public boolean isDataVersionAcceptable(byte[] version) {
            return version[0] == 2;
        }
    }

    public static final class UTF16Plus {
        public static boolean isSurrogateLead(int c) {
            return (c & 0x400) == 0;
        }

        public static boolean equal(CharSequence s1, CharSequence s2) {
            if (s1 == s2) {
                return true;
            }
            int length = s1.length();
            if (length != s2.length()) {
                return false;
            }
            for (int i = 0; i < length; ++i) {
                if (s1.charAt(i) == s2.charAt(i)) continue;
                return false;
            }
            return true;
        }

        public static boolean equal(CharSequence s1, int start1, int limit1, CharSequence s2, int start2, int limit2) {
            if (limit1 - start1 != limit2 - start2) {
                return false;
            }
            if (s1 == s2 && start1 == start2) {
                return true;
            }
            while (start1 < limit1) {
                if (s1.charAt(start1++) == s2.charAt(start2++)) continue;
                return false;
            }
            return true;
        }
    }

    public static final class ReorderingBuffer
    implements Appendable {
        private final Normalizer2Impl impl;
        private final Appendable app;
        private final StringBuilder str;
        private final boolean appIsStringBuilder;
        private int reorderStart;
        private int lastCC;
        private int codePointStart;
        private int codePointLimit;

        public ReorderingBuffer(Normalizer2Impl ni, Appendable dest, int destCapacity) {
            this.impl = ni;
            this.app = dest;
            if (this.app instanceof StringBuilder) {
                this.appIsStringBuilder = true;
                this.str = (StringBuilder)dest;
                this.str.ensureCapacity(destCapacity);
                this.reorderStart = 0;
                if (this.str.length() == 0) {
                    this.lastCC = 0;
                } else {
                    this.setIterator();
                    this.lastCC = this.previousCC();
                    if (this.lastCC > 1) {
                        while (this.previousCC() > 1) {
                        }
                    }
                    this.reorderStart = this.codePointLimit;
                }
            } else {
                this.appIsStringBuilder = false;
                this.str = new StringBuilder();
                this.reorderStart = 0;
                this.lastCC = 0;
            }
        }

        public boolean isEmpty() {
            return this.str.length() == 0;
        }

        public int length() {
            return this.str.length();
        }

        public int getLastCC() {
            return this.lastCC;
        }

        public StringBuilder getStringBuilder() {
            return this.str;
        }

        public boolean equals(CharSequence s, int start, int limit) {
            return UTF16Plus.equal(this.str, 0, this.str.length(), s, start, limit);
        }

        public void setLastChar(char c) {
            this.str.setCharAt(this.str.length() - 1, c);
        }

        public void append(int c, int cc) {
            if (this.lastCC <= cc || cc == 0) {
                this.str.appendCodePoint(c);
                this.lastCC = cc;
                if (cc <= 1) {
                    this.reorderStart = this.str.length();
                }
            } else {
                this.insert(c, cc);
            }
        }

        public void append(CharSequence s, int start, int limit, int leadCC, int trailCC) {
            if (start == limit) {
                return;
            }
            if (this.lastCC <= leadCC || leadCC == 0) {
                if (trailCC <= 1) {
                    this.reorderStart = this.str.length() + (limit - start);
                } else if (leadCC <= 1) {
                    this.reorderStart = this.str.length() + 1;
                }
                this.str.append(s, start, limit);
                this.lastCC = trailCC;
            } else {
                int c = Character.codePointAt(s, start);
                start += Character.charCount(c);
                this.insert(c, leadCC);
                while (start < limit) {
                    leadCC = (start += Character.charCount(c = Character.codePointAt(s, start))) < limit ? Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c)) : trailCC;
                    this.append(c, leadCC);
                }
            }
        }

        public ReorderingBuffer append(char c) {
            this.str.append(c);
            this.lastCC = 0;
            this.reorderStart = this.str.length();
            return this;
        }

        public void appendZeroCC(int c) {
            this.str.appendCodePoint(c);
            this.lastCC = 0;
            this.reorderStart = this.str.length();
        }

        public ReorderingBuffer append(CharSequence s) {
            if (s.length() != 0) {
                this.str.append(s);
                this.lastCC = 0;
                this.reorderStart = this.str.length();
            }
            return this;
        }

        public ReorderingBuffer append(CharSequence s, int start, int limit) {
            if (start != limit) {
                this.str.append(s, start, limit);
                this.lastCC = 0;
                this.reorderStart = this.str.length();
            }
            return this;
        }

        public void flush() {
            if (this.appIsStringBuilder) {
                this.reorderStart = this.str.length();
            } else {
                try {
                    this.app.append(this.str);
                    this.str.setLength(0);
                    this.reorderStart = 0;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.lastCC = 0;
        }

        public ReorderingBuffer flushAndAppendZeroCC(CharSequence s, int start, int limit) {
            if (this.appIsStringBuilder) {
                this.str.append(s, start, limit);
                this.reorderStart = this.str.length();
            } else {
                try {
                    this.app.append(this.str).append(s, start, limit);
                    this.str.setLength(0);
                    this.reorderStart = 0;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.lastCC = 0;
            return this;
        }

        public void remove() {
            this.str.setLength(0);
            this.lastCC = 0;
            this.reorderStart = 0;
        }

        public void removeSuffix(int suffixLength) {
            int oldLength = this.str.length();
            this.str.delete(oldLength - suffixLength, oldLength);
            this.lastCC = 0;
            this.reorderStart = this.str.length();
        }

        private void insert(int c, int cc) {
            this.setIterator();
            this.skipPrevious();
            while (this.previousCC() > cc) {
            }
            if (c <= 65535) {
                this.str.insert(this.codePointLimit, (char)c);
                if (cc <= 1) {
                    this.reorderStart = this.codePointLimit + 1;
                }
            } else {
                this.str.insert(this.codePointLimit, Character.toChars(c));
                if (cc <= 1) {
                    this.reorderStart = this.codePointLimit + 2;
                }
            }
        }

        private void setIterator() {
            this.codePointStart = this.str.length();
        }

        private void skipPrevious() {
            this.codePointLimit = this.codePointStart;
            this.codePointStart = this.str.offsetByCodePoints(this.codePointStart, -1);
        }

        private int previousCC() {
            this.codePointLimit = this.codePointStart;
            if (this.reorderStart >= this.codePointStart) {
                return 0;
            }
            int c = this.str.codePointBefore(this.codePointStart);
            this.codePointStart -= Character.charCount(c);
            if (c < 768) {
                return 0;
            }
            return Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c));
        }
    }

    public static final class Hangul {
        public static final int JAMO_L_BASE = 4352;
        public static final int JAMO_V_BASE = 4449;
        public static final int JAMO_T_BASE = 4519;
        public static final int HANGUL_BASE = 44032;
        public static final int JAMO_L_COUNT = 19;
        public static final int JAMO_V_COUNT = 21;
        public static final int JAMO_T_COUNT = 28;
        public static final int JAMO_L_LIMIT = 4371;
        public static final int JAMO_V_LIMIT = 4470;
        public static final int JAMO_VT_COUNT = 588;
        public static final int HANGUL_COUNT = 11172;
        public static final int HANGUL_LIMIT = 55204;

        public static boolean isHangul(int c) {
            return 44032 <= c && c < 55204;
        }

        public static boolean isHangulWithoutJamoT(char c) {
            return (c = (char)(c - 44032)) < '\u2ba4' && c % 28 == 0;
        }

        public static boolean isJamoL(int c) {
            return 4352 <= c && c < 4371;
        }

        public static boolean isJamoV(int c) {
            return 4449 <= c && c < 4470;
        }

        public static int decompose(int c, Appendable buffer) {
            try {
                int c2 = (c -= 44032) % 28;
                buffer.append((char)(4352 + (c /= 28) / 21));
                buffer.append((char)(4449 + c % 21));
                if (c2 == 0) {
                    return 2;
                }
                buffer.append((char)(4519 + c2));
                return 3;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void getRawDecomposition(int c, Appendable buffer) {
            try {
                int orig = c;
                int c2 = (c -= 44032) % 28;
                if (c2 == 0) {
                    buffer.append((char)(4352 + (c /= 28) / 21));
                    buffer.append((char)(4449 + c % 21));
                } else {
                    buffer.append((char)(orig - c2));
                    buffer.append((char)(4519 + c2));
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

