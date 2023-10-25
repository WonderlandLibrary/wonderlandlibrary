/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.Trie2Writable;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.IdentifierInfo;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SpoofChecker {
    public static final UnicodeSet INCLUSION = new UnicodeSet("[\\-.\\u00B7\\u05F3\\u05F4\\u0F0B\\u200C\\u200D\\u2019]");
    public static final UnicodeSet RECOMMENDED = new UnicodeSet("[[0-z\\u00C0-\\u017E\\u01A0\\u01A1\\u01AF\\u01B0\\u01CD-\\u01DC\\u01DE-\\u01E3\\u01E6-\\u01F5\\u01F8-\\u021B\\u021E\\u021F\\u0226-\\u0233\\u02BB\\u02BC\\u02EC\\u0300-\\u0304\\u0306-\\u030C\\u030F-\\u0311\\u0313\\u0314\\u031B\\u0323-\\u0328\\u032D\\u032E\\u0330\\u0331\\u0335\\u0338\\u0339\\u0342-\\u0345\\u037B-\\u03CE\\u03FC-\\u045F\\u048A-\\u0525\\u0531-\\u0586\\u05D0-\\u05F2\\u0621-\\u063F\\u0641-\\u0655\\u0660-\\u0669\\u0670-\\u068D\\u068F-\\u06D5\\u06E5\\u06E6\\u06EE-\\u06FF\\u0750-\\u07B1\\u0901-\\u0939\\u093C-\\u094D\\u0950\\u0960-\\u0972\\u0979-\\u0A4D\\u0A5C-\\u0A74\\u0A81-\\u0B43\\u0B47-\\u0B61\\u0B66-\\u0C56\\u0C60\\u0C61\\u0C66-\\u0CD6\\u0CE0-\\u0CEF\\u0D02-\\u0D28\\u0D2A-\\u0D39\\u0D3D-\\u0D43\\u0D46-\\u0D4D\\u0D57-\\u0D61\\u0D66-\\u0D8E\\u0D91-\\u0DA5\\u0DA7-\\u0DDE\\u0DF2\\u0E01-\\u0ED9\\u0F00\\u0F20-\\u0F8B\\u0F90-\\u109D\\u10D0-\\u10F0\\u10F7-\\u10FA\\u1200-\\u135A\\u135F\\u1380-\\u138F\\u1401-\\u167F\\u1780-\\u17A2\\u17A5-\\u17A7\\u17A9-\\u17B3\\u17B6-\\u17CA\\u17D2\\u17D7-\\u17DC\\u17E0-\\u17E9\\u1810-\\u18A8\\u18AA-\\u18F5\\u1E00-\\u1E99\\u1F00-\\u1FFC\\u2D30-\\u2D65\\u2D80-\\u2DDE\\u3005-\\u3007\\u3041-\\u31B7\\u3400-\\u9FCB\\uA000-\\uA48C\\uA67F\\uA717-\\uA71F\\uA788\\uAA60-\\uAA7B\\uAC00-\\uD7A3\\uFA0E-\\uFA29\\U00020000-\\U0002B734]-[[:Cn:][:nfkcqc=n:][:XIDC=n:]]]");
    public static final int SINGLE_SCRIPT_CONFUSABLE = 1;
    public static final int MIXED_SCRIPT_CONFUSABLE = 2;
    public static final int WHOLE_SCRIPT_CONFUSABLE = 4;
    public static final int ANY_CASE = 8;
    public static final int RESTRICTION_LEVEL = 16;
    public static final int SINGLE_SCRIPT = 16;
    public static final int INVISIBLE = 32;
    public static final int CHAR_LIMIT = 64;
    public static final int MIXED_NUMBERS = 128;
    public static final int ALL_CHECKS = -1;
    static final int MAGIC = 944111087;
    private IdentifierInfo fCachedIdentifierInfo = null;
    private int fMagic;
    private int fChecks;
    private SpoofData fSpoofData;
    private Set<ULocale> fAllowedLocales;
    private UnicodeSet fAllowedCharsSet;
    private RestrictionLevel fRestrictionLevel;
    private static Normalizer2 nfdNormalizer = Normalizer2.getNFDInstance();
    static final int SL_TABLE_FLAG = 0x1000000;
    static final int SA_TABLE_FLAG = 0x2000000;
    static final int ML_TABLE_FLAG = 0x4000000;
    static final int MA_TABLE_FLAG = 0x8000000;
    static final int KEY_MULTIPLE_VALUES = 0x10000000;
    static final int KEY_LENGTH_SHIFT = 29;

    private SpoofChecker() {
    }

    public RestrictionLevel getRestrictionLevel() {
        return this.fRestrictionLevel;
    }

    public int getChecks() {
        return this.fChecks;
    }

    public Set<ULocale> getAllowedLocales() {
        return this.fAllowedLocales;
    }

    public UnicodeSet getAllowedChars() {
        return this.fAllowedCharsSet;
    }

    public boolean failsChecks(String text, CheckResult checkResult) {
        int length = text.length();
        int result = 0;
        if (checkResult != null) {
            checkResult.position = 0;
            checkResult.numerics = null;
            checkResult.restrictionLevel = null;
        }
        IdentifierInfo identifierInfo = null;
        if (0 != (this.fChecks & 0x90)) {
            identifierInfo = this.getIdentifierInfo().setIdentifier(text).setIdentifierProfile(this.fAllowedCharsSet);
        }
        if (0 != (this.fChecks & 0x10)) {
            RestrictionLevel textRestrictionLevel = identifierInfo.getRestrictionLevel();
            if (textRestrictionLevel.compareTo(this.fRestrictionLevel) > 0) {
                result |= 0x10;
            }
            if (checkResult != null) {
                checkResult.restrictionLevel = textRestrictionLevel;
            }
        }
        if (0 != (this.fChecks & 0x80)) {
            UnicodeSet numerics = identifierInfo.getNumerics();
            if (numerics.size() > 1) {
                result |= 0x80;
            }
            if (checkResult != null) {
                checkResult.numerics = numerics;
            }
        }
        if (0 != (this.fChecks & 0x40)) {
            int i = 0;
            while (i < length) {
                int c = Character.codePointAt(text, i);
                i = Character.offsetByCodePoints(text, i, 1);
                if (this.fAllowedCharsSet.contains(c)) continue;
                result |= 0x40;
                break;
            }
        }
        if (0 != (this.fChecks & 0x26)) {
            String nfdText = nfdNormalizer.normalize(text);
            if (0 != (this.fChecks & 0x20)) {
                int firstNonspacingMark = 0;
                boolean haveMultipleMarks = false;
                UnicodeSet marksSeenSoFar = new UnicodeSet();
                int i = 0;
                while (i < length) {
                    int c = Character.codePointAt(nfdText, i);
                    i = Character.offsetByCodePoints(nfdText, i, 1);
                    if (Character.getType(c) != 6) {
                        firstNonspacingMark = 0;
                        if (!haveMultipleMarks) continue;
                        marksSeenSoFar.clear();
                        haveMultipleMarks = false;
                        continue;
                    }
                    if (firstNonspacingMark == 0) {
                        firstNonspacingMark = c;
                        continue;
                    }
                    if (!haveMultipleMarks) {
                        marksSeenSoFar.add(firstNonspacingMark);
                        haveMultipleMarks = true;
                    }
                    if (marksSeenSoFar.contains(c)) {
                        result |= 0x20;
                        break;
                    }
                    marksSeenSoFar.add(c);
                }
            }
            if (0 != (this.fChecks & 6)) {
                if (identifierInfo == null) {
                    identifierInfo = this.getIdentifierInfo();
                    identifierInfo.setIdentifier(text);
                }
                int scriptCount = identifierInfo.getScriptCount();
                ScriptSet scripts = new ScriptSet();
                this.wholeScriptCheck(nfdText, scripts);
                int confusableScriptCount = scripts.countMembers();
                if (0 != (this.fChecks & 4) && confusableScriptCount >= 2 && scriptCount == 1) {
                    result |= 4;
                }
                if (0 != (this.fChecks & 2) && confusableScriptCount >= 1 && scriptCount > 1) {
                    result |= 2;
                }
            }
        }
        if (checkResult != null) {
            checkResult.checks = result;
        }
        this.releaseIdentifierInfo(identifierInfo);
        return 0 != result;
    }

    public boolean failsChecks(String text) {
        return this.failsChecks(text, null);
    }

    public int areConfusable(String s1, String s2) {
        String s2Skeleton;
        String s1Skeleton;
        boolean possiblyWholeScriptConfusables;
        String s2Skeleton2;
        String s1Skeleton2;
        if ((this.fChecks & 7) == 0) {
            throw new IllegalArgumentException("No confusable checks are enabled.");
        }
        int flagsForSkeleton = this.fChecks & 8;
        int result = 0;
        IdentifierInfo identifierInfo = this.getIdentifierInfo();
        identifierInfo.setIdentifier(s1);
        int s1ScriptCount = identifierInfo.getScriptCount();
        identifierInfo.setIdentifier(s2);
        int s2ScriptCount = identifierInfo.getScriptCount();
        this.releaseIdentifierInfo(identifierInfo);
        if (0 != (this.fChecks & 1) && s1ScriptCount <= 1 && s2ScriptCount <= 1 && (s1Skeleton2 = this.getSkeleton(flagsForSkeleton |= 1, s1)).equals(s2Skeleton2 = this.getSkeleton(flagsForSkeleton, s2))) {
            result |= 1;
        }
        if (result & true) {
            return result;
        }
        boolean bl = possiblyWholeScriptConfusables = s1ScriptCount <= 1 && s2ScriptCount <= 1 && 0 != (this.fChecks & 4);
        if ((0 != (this.fChecks & 2) || possiblyWholeScriptConfusables) && (s1Skeleton = this.getSkeleton(flagsForSkeleton &= 0xFFFFFFFE, s1)).equals(s2Skeleton = this.getSkeleton(flagsForSkeleton, s2))) {
            result |= 2;
            if (possiblyWholeScriptConfusables) {
                result |= 4;
            }
        }
        return result;
    }

    public String getSkeleton(int type, String id) {
        int c;
        int tableMask = 0;
        switch (type) {
            case 0: {
                tableMask = 0x4000000;
                break;
            }
            case 1: {
                tableMask = 0x1000000;
                break;
            }
            case 8: {
                tableMask = 0x8000000;
                break;
            }
            case 9: {
                tableMask = 0x2000000;
                break;
            }
            default: {
                throw new IllegalArgumentException("SpoofChecker.getSkeleton(), bad type value.");
            }
        }
        String nfdId = nfdNormalizer.normalize(id);
        int normalizedLen = nfdId.length();
        StringBuilder skelSB = new StringBuilder();
        for (int inputIndex = 0; inputIndex < normalizedLen; inputIndex += Character.charCount(c)) {
            c = Character.codePointAt(nfdId, inputIndex);
            this.confusableLookup(c, tableMask, skelSB);
        }
        String skelStr = skelSB.toString();
        skelStr = nfdNormalizer.normalize(skelStr);
        return skelStr;
    }

    private void confusableLookup(int inChar, int tableMask, StringBuilder dest) {
        int low = 0;
        int mid = 0;
        int limit = this.fSpoofData.fRawData.fCFUKeysSize;
        boolean foundChar = false;
        do {
            int delta;
            int midc;
            if (inChar == (midc = this.fSpoofData.fCFUKeys[mid = low + (delta = (limit - low) / 2)] & 0x1FFFFF)) {
                foundChar = true;
                break;
            }
            if (inChar < midc) {
                limit = mid;
                continue;
            }
            low = mid + 1;
        } while (low < limit);
        if (!foundChar) {
            dest.appendCodePoint(inChar);
            return;
        }
        boolean foundKey = false;
        int keyFlags = this.fSpoofData.fCFUKeys[mid] & 0xFF000000;
        if ((keyFlags & tableMask) == 0) {
            if (0 != (keyFlags & 0x10000000)) {
                int altMid = mid - 1;
                while ((this.fSpoofData.fCFUKeys[altMid] & 0xFFFFFF) == inChar) {
                    keyFlags = this.fSpoofData.fCFUKeys[altMid] & 0xFF000000;
                    if (0 != (keyFlags & tableMask)) {
                        mid = altMid;
                        foundKey = true;
                        break;
                    }
                    --altMid;
                }
                if (!foundKey) {
                    altMid = mid + 1;
                    while ((this.fSpoofData.fCFUKeys[altMid] & 0xFFFFFF) == inChar) {
                        keyFlags = this.fSpoofData.fCFUKeys[altMid] & 0xFF000000;
                        if (0 != (keyFlags & tableMask)) {
                            mid = altMid;
                            foundKey = true;
                            break;
                        }
                        ++altMid;
                    }
                }
            }
            if (!foundKey) {
                dest.appendCodePoint(inChar);
                return;
            }
        }
        int stringLen = SpoofChecker.getKeyLength(keyFlags) + 1;
        int keyTableIndex = mid;
        short value = this.fSpoofData.fCFUValues[keyTableIndex];
        if (stringLen == 1) {
            dest.append((char)value);
            return;
        }
        if (stringLen == 4) {
            int ix;
            int stringLengthsLimit = this.fSpoofData.fRawData.fCFUStringLengthsSize;
            for (ix = 0; ix < stringLengthsLimit; ++ix) {
                if (this.fSpoofData.fCFUStringLengths[ix].fLastString < value) continue;
                stringLen = this.fSpoofData.fCFUStringLengths[ix].fStrLength;
                break;
            }
            assert (ix < stringLengthsLimit);
        }
        assert (value + stringLen <= this.fSpoofData.fRawData.fCFUStringTableLen);
        dest.append(this.fSpoofData.fCFUStrings, (int)value, stringLen);
    }

    void wholeScriptCheck(CharSequence text, ScriptSet result) {
        int inputIdx = 0;
        Trie2 table = 0 != (this.fChecks & 8) ? this.fSpoofData.fAnyCaseTrie : this.fSpoofData.fLowerCaseTrie;
        result.setAll();
        while (inputIdx < text.length()) {
            int c = Character.codePointAt(text, inputIdx);
            inputIdx = Character.offsetByCodePoints(text, inputIdx, 1);
            int index = table.get(c);
            if (index == 0) {
                int cpScript = UScript.getScript(c);
                assert (cpScript > 1);
                result.intersect(cpScript);
                continue;
            }
            if (index == 1) continue;
            result.intersect(this.fSpoofData.fScriptSets[index]);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private IdentifierInfo getIdentifierInfo() {
        IdentifierInfo returnIdInfo = null;
        SpoofChecker spoofChecker = this;
        synchronized (spoofChecker) {
            returnIdInfo = this.fCachedIdentifierInfo;
            this.fCachedIdentifierInfo = null;
        }
        if (returnIdInfo == null) {
            returnIdInfo = new IdentifierInfo();
        }
        return returnIdInfo;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void releaseIdentifierInfo(IdentifierInfo idInfo) {
        if (idInfo != null) {
            SpoofChecker spoofChecker = this;
            synchronized (spoofChecker) {
                if (this.fCachedIdentifierInfo == null) {
                    this.fCachedIdentifierInfo = idInfo;
                }
            }
        }
    }

    static final int getKeyLength(int x) {
        return x >> 29 & 3;
    }

    private static class ScriptSet {
        private int[] bits = new int[6];

        public ScriptSet() {
        }

        public ScriptSet(DataInputStream dis) throws IOException {
            for (int j = 0; j < this.bits.length; ++j) {
                this.bits[j] = dis.readInt();
            }
        }

        public void output(DataOutputStream os) throws IOException {
            for (int i = 0; i < this.bits.length; ++i) {
                os.writeInt(this.bits[i]);
            }
        }

        public boolean equals(ScriptSet other) {
            for (int i = 0; i < this.bits.length; ++i) {
                if (this.bits[i] == other.bits[i]) continue;
                return false;
            }
            return true;
        }

        public void Union(int script) {
            int index = script / 32;
            int bit = 1 << (script & 0x1F);
            assert (index < this.bits.length * 4 * 4);
            int n = index;
            this.bits[n] = this.bits[n] | bit;
        }

        public void Union(ScriptSet other) {
            for (int i = 0; i < this.bits.length; ++i) {
                int n = i;
                this.bits[n] = this.bits[n] | other.bits[i];
            }
        }

        public void intersect(ScriptSet other) {
            for (int i = 0; i < this.bits.length; ++i) {
                int n = i;
                this.bits[n] = this.bits[n] & other.bits[i];
            }
        }

        public void intersect(int script) {
            int i;
            int index = script / 32;
            int bit = 1 << (script & 0x1F);
            assert (index < this.bits.length * 4 * 4);
            for (i = 0; i < index; ++i) {
                this.bits[i] = 0;
            }
            int n = index;
            this.bits[n] = this.bits[n] & bit;
            for (i = index + 1; i < this.bits.length; ++i) {
                this.bits[i] = 0;
            }
        }

        public void setAll() {
            for (int i = 0; i < this.bits.length; ++i) {
                this.bits[i] = -1;
            }
        }

        public void resetAll() {
            for (int i = 0; i < this.bits.length; ++i) {
                this.bits[i] = 0;
            }
        }

        public int countMembers() {
            int count = 0;
            for (int i = 0; i < this.bits.length; ++i) {
                for (int x = this.bits[i]; x != 0; x &= x - 1) {
                    ++count;
                }
            }
            return count;
        }
    }

    private static class SpoofData {
        SpoofDataHeader fRawData;
        int[] fCFUKeys;
        short[] fCFUValues;
        SpoofStringLengthsElement[] fCFUStringLengths;
        char[] fCFUStrings;
        Trie2 fAnyCaseTrie;
        Trie2 fLowerCaseTrie;
        ScriptSet[] fScriptSets;

        public static SpoofData getDefault() throws IOException {
            InputStream is = ICUData.getRequiredStream("data/icudt51b/confusables.cfu");
            SpoofData This = new SpoofData(is);
            return This;
        }

        public SpoofData() {
            this.fRawData = new SpoofDataHeader();
            this.fRawData.fMagic = 944111087;
            this.fRawData.fFormatVersion[0] = 1;
            this.fRawData.fFormatVersion[1] = 0;
            this.fRawData.fFormatVersion[2] = 0;
            this.fRawData.fFormatVersion[3] = 0;
        }

        public SpoofData(InputStream is) throws IOException {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
            dis.skip(128L);
            assert (dis.markSupported());
            dis.mark(Integer.MAX_VALUE);
            this.fRawData = new SpoofDataHeader(dis);
            this.initPtrs(dis);
        }

        static boolean validateDataVersion(SpoofDataHeader rawData) {
            return rawData != null && rawData.fMagic == 944111087 && rawData.fFormatVersion[0] <= 1 && rawData.fFormatVersion[1] <= 0;
        }

        void initPtrs(DataInputStream dis) throws IOException {
            int i;
            this.fCFUKeys = null;
            this.fCFUValues = null;
            this.fCFUStringLengths = null;
            this.fCFUStrings = null;
            dis.reset();
            dis.skip(this.fRawData.fCFUKeys);
            if (this.fRawData.fCFUKeys != 0) {
                this.fCFUKeys = new int[this.fRawData.fCFUKeysSize];
                for (i = 0; i < this.fRawData.fCFUKeysSize; ++i) {
                    this.fCFUKeys[i] = dis.readInt();
                }
            }
            dis.reset();
            dis.skip(this.fRawData.fCFUStringIndex);
            if (this.fRawData.fCFUStringIndex != 0) {
                this.fCFUValues = new short[this.fRawData.fCFUStringIndexSize];
                for (i = 0; i < this.fRawData.fCFUStringIndexSize; ++i) {
                    this.fCFUValues[i] = dis.readShort();
                }
            }
            dis.reset();
            dis.skip(this.fRawData.fCFUStringTable);
            if (this.fRawData.fCFUStringTable != 0) {
                this.fCFUStrings = new char[this.fRawData.fCFUStringTableLen];
                for (i = 0; i < this.fRawData.fCFUStringTableLen; ++i) {
                    this.fCFUStrings[i] = dis.readChar();
                }
            }
            dis.reset();
            dis.skip(this.fRawData.fCFUStringLengths);
            if (this.fRawData.fCFUStringLengths != 0) {
                this.fCFUStringLengths = new SpoofStringLengthsElement[this.fRawData.fCFUStringLengthsSize];
                for (i = 0; i < this.fRawData.fCFUStringLengthsSize; ++i) {
                    this.fCFUStringLengths[i] = new SpoofStringLengthsElement();
                    this.fCFUStringLengths[i].fLastString = dis.readShort();
                    this.fCFUStringLengths[i].fStrLength = dis.readShort();
                }
            }
            dis.reset();
            dis.skip(this.fRawData.fAnyCaseTrie);
            if (this.fAnyCaseTrie == null && this.fRawData.fAnyCaseTrie != 0) {
                this.fAnyCaseTrie = Trie2.createFromSerialized(dis);
            }
            dis.reset();
            dis.skip(this.fRawData.fLowerCaseTrie);
            if (this.fLowerCaseTrie == null && this.fRawData.fLowerCaseTrie != 0) {
                this.fLowerCaseTrie = Trie2.createFromSerialized(dis);
            }
            dis.reset();
            dis.skip(this.fRawData.fScriptSets);
            if (this.fRawData.fScriptSets != 0) {
                this.fScriptSets = new ScriptSet[this.fRawData.fScriptSetsLength];
                for (i = 0; i < this.fRawData.fScriptSetsLength; ++i) {
                    this.fScriptSets[i] = new ScriptSet(dis);
                }
            }
        }

        private static class SpoofStringLengthsElement {
            short fLastString;
            short fStrLength;

            private SpoofStringLengthsElement() {
            }
        }
    }

    private static class SpoofDataHeader {
        int fMagic;
        byte[] fFormatVersion = new byte[4];
        int fLength;
        int fCFUKeys;
        int fCFUKeysSize;
        int fCFUStringIndex;
        int fCFUStringIndexSize;
        int fCFUStringTable;
        int fCFUStringTableLen;
        int fCFUStringLengths;
        int fCFUStringLengthsSize;
        int fAnyCaseTrie;
        int fAnyCaseTrieLength;
        int fLowerCaseTrie;
        int fLowerCaseTrieLength;
        int fScriptSets;
        int fScriptSetsLength;
        int[] unused = new int[15];

        public SpoofDataHeader() {
        }

        public SpoofDataHeader(DataInputStream dis) throws IOException {
            int i;
            this.fMagic = dis.readInt();
            for (i = 0; i < this.fFormatVersion.length; ++i) {
                this.fFormatVersion[i] = dis.readByte();
            }
            this.fLength = dis.readInt();
            this.fCFUKeys = dis.readInt();
            this.fCFUKeysSize = dis.readInt();
            this.fCFUStringIndex = dis.readInt();
            this.fCFUStringIndexSize = dis.readInt();
            this.fCFUStringTable = dis.readInt();
            this.fCFUStringTableLen = dis.readInt();
            this.fCFUStringLengths = dis.readInt();
            this.fCFUStringLengthsSize = dis.readInt();
            this.fAnyCaseTrie = dis.readInt();
            this.fAnyCaseTrieLength = dis.readInt();
            this.fLowerCaseTrie = dis.readInt();
            this.fLowerCaseTrieLength = dis.readInt();
            this.fScriptSets = dis.readInt();
            this.fScriptSetsLength = dis.readInt();
            for (i = 0; i < this.unused.length; ++i) {
                this.unused[i] = dis.readInt();
            }
        }

        public void output(DataOutputStream os) throws IOException {
            int i;
            os.writeInt(this.fMagic);
            for (i = 0; i < this.fFormatVersion.length; ++i) {
                os.writeByte(this.fFormatVersion[i]);
            }
            os.writeInt(this.fLength);
            os.writeInt(this.fCFUKeys);
            os.writeInt(this.fCFUKeysSize);
            os.writeInt(this.fCFUStringIndex);
            os.writeInt(this.fCFUStringIndexSize);
            os.writeInt(this.fCFUStringTable);
            os.writeInt(this.fCFUStringTableLen);
            os.writeInt(this.fCFUStringLengths);
            os.writeInt(this.fCFUStringLengthsSize);
            os.writeInt(this.fAnyCaseTrie);
            os.writeInt(this.fAnyCaseTrieLength);
            os.writeInt(this.fLowerCaseTrie);
            os.writeInt(this.fLowerCaseTrieLength);
            os.writeInt(this.fScriptSets);
            os.writeInt(this.fScriptSetsLength);
            for (i = 0; i < this.unused.length; ++i) {
                os.writeInt(this.unused[i]);
            }
        }
    }

    public static class CheckResult {
        public int checks = 0;
        public int position = 0;
        public UnicodeSet numerics;
        public RestrictionLevel restrictionLevel;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class Builder {
        int fMagic;
        int fChecks;
        SpoofData fSpoofData;
        final UnicodeSet fAllowedCharsSet = new UnicodeSet(0, 0x10FFFF);
        final Set<ULocale> fAllowedLocales = new LinkedHashSet<ULocale>();
        private RestrictionLevel fRestrictionLevel;

        public Builder() {
            this.fMagic = 944111087;
            this.fChecks = -1;
            this.fSpoofData = null;
            this.fRestrictionLevel = RestrictionLevel.HIGHLY_RESTRICTIVE;
        }

        public Builder(SpoofChecker src) {
            this.fMagic = src.fMagic;
            this.fChecks = src.fChecks;
            this.fSpoofData = null;
            this.fAllowedCharsSet.set(src.fAllowedCharsSet);
            this.fAllowedLocales.addAll(src.fAllowedLocales);
            this.fRestrictionLevel = src.fRestrictionLevel;
        }

        public SpoofChecker build() {
            if (this.fSpoofData == null) {
                try {
                    this.fSpoofData = SpoofData.getDefault();
                }
                catch (IOException e) {
                    return null;
                }
            }
            if (!SpoofData.validateDataVersion(this.fSpoofData.fRawData)) {
                return null;
            }
            SpoofChecker result = new SpoofChecker();
            result.fMagic = this.fMagic;
            result.fChecks = this.fChecks;
            result.fSpoofData = this.fSpoofData;
            result.fAllowedCharsSet = (UnicodeSet)this.fAllowedCharsSet.clone();
            result.fAllowedCharsSet.freeze();
            result.fAllowedLocales = this.fAllowedLocales;
            result.fRestrictionLevel = this.fRestrictionLevel;
            return result;
        }

        public Builder setData(Reader confusables, Reader confusablesWholeScript) throws ParseException, IOException {
            this.fSpoofData = new SpoofData();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(bos);
            ConfusabledataBuilder.buildConfusableData(this.fSpoofData, confusables);
            WSConfusableDataBuilder.buildWSConfusableData(this.fSpoofData, os, confusablesWholeScript);
            return this;
        }

        public Builder setChecks(int checks) {
            if (0 != (checks & 0)) {
                throw new IllegalArgumentException("Bad Spoof Checks value.");
            }
            this.fChecks = checks & 0xFFFFFFFF;
            return this;
        }

        public Builder setAllowedLocales(Set<ULocale> locales) {
            this.fAllowedCharsSet.clear();
            for (ULocale locale : locales) {
                this.addScriptChars(locale, this.fAllowedCharsSet);
            }
            this.fAllowedLocales.clear();
            if (locales.size() == 0) {
                this.fAllowedCharsSet.add(0, 0x10FFFF);
                this.fChecks &= 0xFFFFFFBF;
                return this;
            }
            UnicodeSet tempSet = new UnicodeSet();
            tempSet.applyIntPropertyValue(4106, 0);
            this.fAllowedCharsSet.addAll(tempSet);
            tempSet.applyIntPropertyValue(4106, 1);
            this.fAllowedCharsSet.addAll(tempSet);
            this.fAllowedLocales.addAll(locales);
            this.fChecks |= 0x40;
            return this;
        }

        private void addScriptChars(ULocale locale, UnicodeSet allowedChars) {
            int[] scripts = UScript.getCode(locale);
            UnicodeSet tmpSet = new UnicodeSet();
            for (int i = 0; i < scripts.length; ++i) {
                tmpSet.applyIntPropertyValue(4106, scripts[i]);
                allowedChars.addAll(tmpSet);
            }
        }

        public Builder setAllowedChars(UnicodeSet chars) {
            this.fAllowedCharsSet.set(chars);
            this.fAllowedLocales.clear();
            this.fChecks |= 0x40;
            return this;
        }

        public Builder setRestrictionLevel(RestrictionLevel restrictionLevel) {
            this.fRestrictionLevel = restrictionLevel;
            this.fChecks |= 0x10;
            return this;
        }

        /*
         * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
         */
        private static class ConfusabledataBuilder {
            private SpoofData fSpoofData;
            private ByteArrayOutputStream bos;
            private DataOutputStream os;
            private Hashtable<Integer, SPUString> fSLTable;
            private Hashtable<Integer, SPUString> fSATable;
            private Hashtable<Integer, SPUString> fMLTable;
            private Hashtable<Integer, SPUString> fMATable;
            private UnicodeSet fKeySet;
            private StringBuffer fStringTable;
            private Vector<Integer> fKeyVec;
            private Vector<Integer> fValueVec;
            private Vector<Integer> fStringLengthsTable;
            private SPUStringPool stringPool;
            private Pattern fParseLine;
            private Pattern fParseHexNum;
            private int fLineNum;

            ConfusabledataBuilder(SpoofData spData, ByteArrayOutputStream bos) {
                this.bos = bos;
                this.os = new DataOutputStream(bos);
                this.fSpoofData = spData;
                this.fSLTable = new Hashtable();
                this.fSATable = new Hashtable();
                this.fMLTable = new Hashtable();
                this.fMATable = new Hashtable();
                this.fKeySet = new UnicodeSet();
                this.fKeyVec = new Vector();
                this.fValueVec = new Vector();
                this.stringPool = new SPUStringPool();
            }

            void build(Reader confusables) throws ParseException, IOException {
                StringBuffer fInput = new StringBuffer();
                WSConfusableDataBuilder.readWholeFileToString(confusables, fInput);
                this.fParseLine = Pattern.compile("(?m)^[ \\t]*([0-9A-Fa-f]+)[ \\t]+;[ \\t]*([0-9A-Fa-f]+(?:[ \\t]+[0-9A-Fa-f]+)*)[ \\t]*;\\s*(?:(SL)|(SA)|(ML)|(MA))[ \\t]*(?:#.*?)?$|^([ \\t]*(?:#.*?)?)$|^(.*?)$");
                this.fParseHexNum = Pattern.compile("\\s*([0-9A-F]+)");
                if (fInput.charAt(0) == '\ufeff') {
                    fInput.setCharAt(0, ' ');
                }
                Matcher matcher = this.fParseLine.matcher(fInput);
                while (matcher.find()) {
                    Hashtable<Integer, SPUString> table;
                    ++this.fLineNum;
                    if (matcher.start(7) >= 0) continue;
                    if (matcher.start(8) >= 0) {
                        throw new ParseException("Confusables, line " + this.fLineNum + ": Unrecognized Line: " + matcher.group(8), matcher.start(8));
                    }
                    int keyChar = Integer.parseInt(matcher.group(1), 16);
                    if (keyChar > 0x10FFFF) {
                        throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + matcher.group(1), matcher.start(1));
                    }
                    Matcher m = this.fParseHexNum.matcher(matcher.group(2));
                    StringBuilder mapString = new StringBuilder();
                    while (m.find()) {
                        int c = Integer.parseInt(m.group(1), 16);
                        if (keyChar > 0x10FFFF) {
                            throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + Integer.toString(c, 16), matcher.start(2));
                        }
                        mapString.appendCodePoint(c);
                    }
                    assert (mapString.length() >= 1);
                    SPUString smapString = this.stringPool.addString(mapString.toString());
                    Hashtable<Integer, SPUString> hashtable = matcher.start(3) >= 0 ? this.fSLTable : (matcher.start(4) >= 0 ? this.fSATable : (matcher.start(5) >= 0 ? this.fMLTable : (table = matcher.start(6) >= 0 ? this.fMATable : null)));
                    assert (table != null);
                    table.put(keyChar, smapString);
                    this.fKeySet.add(keyChar);
                }
                this.stringPool.sort();
                this.fStringTable = new StringBuffer();
                this.fStringLengthsTable = new Vector();
                int previousStringLength = 0;
                int previousStringIndex = 0;
                int poolSize = this.stringPool.size();
                for (int i = 0; i < poolSize; ++i) {
                    SPUString s = this.stringPool.getByIndex(i);
                    int strLen = s.fStr.length();
                    int strIndex = this.fStringTable.length();
                    assert (strLen >= previousStringLength);
                    if (strLen == 1) {
                        s.fStrTableIndex = s.fStr.charAt(0);
                    } else {
                        if (strLen > previousStringLength && previousStringLength >= 4) {
                            this.fStringLengthsTable.addElement(previousStringIndex);
                            this.fStringLengthsTable.addElement(previousStringLength);
                        }
                        s.fStrTableIndex = strIndex;
                        this.fStringTable.append(s.fStr);
                    }
                    previousStringLength = strLen;
                    previousStringIndex = strIndex;
                }
                if (previousStringLength >= 4) {
                    this.fStringLengthsTable.addElement(previousStringIndex);
                    this.fStringLengthsTable.addElement(previousStringLength);
                }
                for (int range = 0; range < this.fKeySet.getRangeCount(); ++range) {
                    for (int keyChar = this.fKeySet.getRangeStart(range); keyChar <= this.fKeySet.getRangeEnd(range); ++keyChar) {
                        this.addKeyEntry(keyChar, this.fSLTable, 0x1000000);
                        this.addKeyEntry(keyChar, this.fSATable, 0x2000000);
                        this.addKeyEntry(keyChar, this.fMLTable, 0x4000000);
                        this.addKeyEntry(keyChar, this.fMATable, 0x8000000);
                    }
                }
                this.outputData();
            }

            void addKeyEntry(int keyChar, Hashtable<Integer, SPUString> table, int tableFlag) {
                int adjustedMappingLength;
                int key;
                SPUString targetMapping = table.get(keyChar);
                if (targetMapping == null) {
                    return;
                }
                boolean keyHasMultipleValues = false;
                for (int i = this.fKeyVec.size() - 1; i >= 0 && ((key = this.fKeyVec.elementAt(i).intValue()) & 0xFFFFFF) == keyChar; --i) {
                    String mapping = this.getMapping(i);
                    if (mapping.equals(targetMapping.fStr)) {
                        this.fKeyVec.setElementAt(key |= tableFlag, i);
                        return;
                    }
                    keyHasMultipleValues = true;
                }
                int newKey = keyChar | tableFlag;
                if (keyHasMultipleValues) {
                    newKey |= 0x10000000;
                }
                if ((adjustedMappingLength = targetMapping.fStr.length() - 1) > 3) {
                    adjustedMappingLength = 3;
                }
                int newData = targetMapping.fStrTableIndex;
                this.fKeyVec.addElement(newKey |= adjustedMappingLength << 29);
                this.fValueVec.addElement(newData);
                if (keyHasMultipleValues) {
                    int previousKeyIndex = this.fKeyVec.size() - 2;
                    int previousKey = this.fKeyVec.elementAt(previousKeyIndex);
                    this.fKeyVec.setElementAt(previousKey |= 0x10000000, previousKeyIndex);
                }
            }

            String getMapping(int index) {
                int key = this.fKeyVec.elementAt(index);
                int value = this.fValueVec.elementAt(index);
                int length = SpoofChecker.getKeyLength(key);
                switch (length) {
                    case 0: {
                        char[] cs = new char[]{(char)value};
                        return new String(cs);
                    }
                    case 1: 
                    case 2: {
                        return this.fStringTable.substring(value, value + length + 1);
                    }
                    case 3: {
                        length = 0;
                        for (int i = 0; i < this.fStringLengthsTable.size(); i += 2) {
                            int lastIndexWithLen = this.fStringLengthsTable.elementAt(i);
                            if (value > lastIndexWithLen) continue;
                            length = this.fStringLengthsTable.elementAt(i + 1);
                            break;
                        }
                        assert (length >= 3);
                        return this.fStringTable.substring(value, value + length);
                    }
                }
                assert (false);
                return "";
            }

            void outputData() throws IOException {
                int i;
                SpoofDataHeader rawData = this.fSpoofData.fRawData;
                int numKeys = this.fKeyVec.size();
                int previousKey = 0;
                rawData.output(this.os);
                rawData.fCFUKeys = this.os.size();
                assert (rawData.fCFUKeys == 128);
                rawData.fCFUKeysSize = numKeys;
                for (i = 0; i < numKeys; ++i) {
                    int key = this.fKeyVec.elementAt(i);
                    assert ((key & 0xFFFFFF) >= (previousKey & 0xFFFFFF));
                    assert ((key & 0xFF000000) != 0);
                    this.os.writeInt(key);
                    previousKey = key;
                }
                int numValues = this.fValueVec.size();
                assert (numKeys == numValues);
                rawData.fCFUStringIndex = this.os.size();
                rawData.fCFUStringIndexSize = numValues;
                for (i = 0; i < numValues; ++i) {
                    int value = this.fValueVec.elementAt(i);
                    assert (value < 65535);
                    this.os.writeShort((short)value);
                }
                int stringsLength = this.fStringTable.length();
                String strings = this.fStringTable.toString();
                rawData.fCFUStringTable = this.os.size();
                rawData.fCFUStringTableLen = stringsLength;
                for (i = 0; i < stringsLength; ++i) {
                    this.os.writeChar(strings.charAt(i));
                }
                int lengthTableLength = this.fStringLengthsTable.size();
                int previousLength = 0;
                rawData.fCFUStringLengthsSize = lengthTableLength / 2;
                rawData.fCFUStringLengths = this.os.size();
                for (i = 0; i < lengthTableLength; i += 2) {
                    int offset = this.fStringLengthsTable.elementAt(i);
                    int length = this.fStringLengthsTable.elementAt(i + 1);
                    assert (offset < stringsLength);
                    assert (length < 40);
                    assert (length > previousLength);
                    this.os.writeShort((short)offset);
                    this.os.writeShort((short)length);
                    previousLength = length;
                }
                this.os.flush();
                DataInputStream is = new DataInputStream(new ByteArrayInputStream(this.bos.toByteArray()));
                is.mark(Integer.MAX_VALUE);
                this.fSpoofData.initPtrs(is);
            }

            public static void buildConfusableData(SpoofData spData, Reader confusables) throws IOException, ParseException {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ConfusabledataBuilder builder = new ConfusabledataBuilder(spData, bos);
                builder.build(confusables);
            }

            private static class SPUStringPool {
                private Vector<SPUString> fVec = new Vector();
                private Hashtable<String, SPUString> fHash = new Hashtable();

                public int size() {
                    return this.fVec.size();
                }

                public SPUString getByIndex(int index) {
                    SPUString retString = this.fVec.elementAt(index);
                    return retString;
                }

                public SPUString addString(String src) {
                    SPUString hashedString = this.fHash.get(src);
                    if (hashedString == null) {
                        hashedString = new SPUString(src);
                        this.fHash.put(src, hashedString);
                        this.fVec.addElement(hashedString);
                    }
                    return hashedString;
                }

                public void sort() {
                    Collections.sort(this.fVec, new SPUStringComparator());
                }
            }

            /*
             * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
             */
            private static class SPUStringComparator
            implements Comparator<SPUString> {
                private SPUStringComparator() {
                }

                @Override
                public int compare(SPUString sL, SPUString sR) {
                    int lenR;
                    int lenL = sL.fStr.length();
                    if (lenL < (lenR = sR.fStr.length())) {
                        return -1;
                    }
                    if (lenL > lenR) {
                        return 1;
                    }
                    return sL.fStr.compareTo(sR.fStr);
                }
            }

            private static class SPUString {
                String fStr;
                int fStrTableIndex;

                SPUString(String s) {
                    this.fStr = s;
                    this.fStrTableIndex = 0;
                }
            }
        }

        private static class WSConfusableDataBuilder {
            static String parseExp = "(?m)^([ \\t]*(?:#.*?)?)$|^(?:\\s*([0-9A-F]{4,})(?:..([0-9A-F]{4,}))?\\s*;\\s*([A-Za-z]+)\\s*;\\s*([A-Za-z]+)\\s*;\\s*(?:(A)|(L))[ \\t]*(?:#.*?)?)$|^(.*?)$";

            private WSConfusableDataBuilder() {
            }

            static void readWholeFileToString(Reader reader, StringBuffer buffer) throws IOException {
                String line;
                LineNumberReader lnr = new LineNumberReader(reader);
                while ((line = lnr.readLine()) != null) {
                    buffer.append(line);
                    buffer.append('\n');
                }
            }

            static void buildWSConfusableData(SpoofData fSpoofData, DataOutputStream os, Reader confusablesWS) throws ParseException, IOException {
                Pattern parseRegexp = null;
                StringBuffer input = new StringBuffer();
                int lineNum = 0;
                Vector<BuilderScriptSet> scriptSets = null;
                int rtScriptSetsCount = 2;
                Trie2Writable anyCaseTrie = new Trie2Writable(0, 0);
                Trie2Writable lowerCaseTrie = new Trie2Writable(0, 0);
                scriptSets = new Vector<BuilderScriptSet>();
                scriptSets.addElement(null);
                scriptSets.addElement(null);
                WSConfusableDataBuilder.readWholeFileToString(confusablesWS, input);
                parseRegexp = Pattern.compile(parseExp);
                if (input.charAt(0) == '\ufeff') {
                    input.setCharAt(0, ' ');
                }
                Matcher matcher = parseRegexp.matcher(input);
                while (matcher.find()) {
                    ++lineNum;
                    if (matcher.start(1) >= 0) continue;
                    if (matcher.start(8) >= 0) {
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Unrecognized input: " + matcher.group(), matcher.start());
                    }
                    int startCodePoint = Integer.parseInt(matcher.group(2), 16);
                    if (startCodePoint > 0x10FFFF) {
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": out of range code point: " + matcher.group(2), matcher.start(2));
                    }
                    int endCodePoint = startCodePoint;
                    if (matcher.start(3) >= 0) {
                        endCodePoint = Integer.parseInt(matcher.group(3), 16);
                    }
                    if (endCodePoint > 0x10FFFF) {
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": out of range code point: " + matcher.group(3), matcher.start(3));
                    }
                    String srcScriptName = matcher.group(4);
                    String targScriptName = matcher.group(5);
                    int srcScript = UCharacter.getPropertyValueEnum(4106, srcScriptName);
                    int targScript = UCharacter.getPropertyValueEnum(4106, targScriptName);
                    if (srcScript == -1) {
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Invalid script code t: " + matcher.group(4), matcher.start(4));
                    }
                    if (targScript == -1) {
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Invalid script code t: " + matcher.group(5), matcher.start(5));
                    }
                    Trie2Writable table = anyCaseTrie;
                    if (matcher.start(7) >= 0) {
                        table = lowerCaseTrie;
                    }
                    for (int cp = startCodePoint; cp <= endCodePoint; ++cp) {
                        int setIndex = table.get(cp);
                        BuilderScriptSet bsset = null;
                        if (setIndex > 0) {
                            assert (setIndex < scriptSets.size());
                            bsset = (BuilderScriptSet)scriptSets.elementAt(setIndex);
                        } else {
                            bsset = new BuilderScriptSet();
                            bsset.codePoint = cp;
                            bsset.trie = table;
                            bsset.sset = new ScriptSet();
                            bsset.index = setIndex = scriptSets.size();
                            bsset.rindex = 0;
                            scriptSets.addElement(bsset);
                            table.set(cp, setIndex);
                        }
                        bsset.sset.Union(targScript);
                        bsset.sset.Union(srcScript);
                        int cpScript = UScript.getScript(cp);
                        if (cpScript == srcScript) continue;
                        throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Mismatch between source script and code point " + Integer.toString(cp, 16), matcher.start(5));
                    }
                }
                rtScriptSetsCount = 2;
                for (int outeri = 2; outeri < scriptSets.size(); ++outeri) {
                    BuilderScriptSet outerSet = (BuilderScriptSet)scriptSets.elementAt(outeri);
                    if (outerSet.index != outeri) continue;
                    outerSet.rindex = rtScriptSetsCount++;
                    for (int inneri = outeri + 1; inneri < scriptSets.size(); ++inneri) {
                        BuilderScriptSet innerSet = (BuilderScriptSet)scriptSets.elementAt(inneri);
                        if (!outerSet.sset.equals(innerSet.sset) || outerSet.sset == innerSet.sset) continue;
                        innerSet.sset = outerSet.sset;
                        innerSet.index = outeri;
                        innerSet.rindex = outerSet.rindex;
                    }
                }
                for (int i = 2; i < scriptSets.size(); ++i) {
                    BuilderScriptSet bSet = (BuilderScriptSet)scriptSets.elementAt(i);
                    if (bSet.rindex == i) continue;
                    bSet.trie.set(bSet.codePoint, bSet.rindex);
                }
                UnicodeSet ignoreSet = new UnicodeSet();
                ignoreSet.applyIntPropertyValue(4106, 0);
                UnicodeSet inheritedSet = new UnicodeSet();
                inheritedSet.applyIntPropertyValue(4106, 1);
                ignoreSet.addAll(inheritedSet);
                for (int rn = 0; rn < ignoreSet.getRangeCount(); ++rn) {
                    int rangeStart = ignoreSet.getRangeStart(rn);
                    int rangeEnd = ignoreSet.getRangeEnd(rn);
                    anyCaseTrie.setRange(rangeStart, rangeEnd, 1, true);
                    lowerCaseTrie.setRange(rangeStart, rangeEnd, 1, true);
                }
                anyCaseTrie.toTrie2_16().serialize(os);
                lowerCaseTrie.toTrie2_16().serialize(os);
                fSpoofData.fRawData.fScriptSetsLength = rtScriptSetsCount;
                int rindex = 2;
                for (int i = 2; i < scriptSets.size(); ++i) {
                    BuilderScriptSet bSet = (BuilderScriptSet)scriptSets.elementAt(i);
                    if (bSet.rindex < rindex) continue;
                    assert (rindex == bSet.rindex);
                    bSet.sset.output(os);
                    ++rindex;
                }
            }

            private static class BuilderScriptSet {
                int codePoint = -1;
                Trie2Writable trie = null;
                ScriptSet sset = null;
                int index = 0;
                int rindex = 0;

                BuilderScriptSet() {
                }
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum RestrictionLevel {
        ASCII,
        HIGHLY_RESTRICTIVE,
        MODERATELY_RESTRICTIVE,
        MINIMALLY_RESTRICTIVE,
        UNRESTRICTIVE;

    }
}

