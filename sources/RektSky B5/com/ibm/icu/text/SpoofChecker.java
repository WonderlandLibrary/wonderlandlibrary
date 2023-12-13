/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpoofChecker {
    public static final UnicodeSet INCLUSION = new UnicodeSet("['\\-.\\:\\u00B7\\u0375\\u058A\\u05F3\\u05F4\\u06FD\\u06FE\\u0F0B\\u200C\\u200D\\u2010\\u2019\\u2027\\u30A0\\u30FB]").freeze();
    public static final UnicodeSet RECOMMENDED = new UnicodeSet("[0-9A-Z_a-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u0131\\u0134-\\u013E\\u0141-\\u0148\\u014A-\\u017E\\u018F\\u01A0\\u01A1\\u01AF\\u01B0\\u01CD-\\u01DC\\u01DE-\\u01E3\\u01E6-\\u01F0\\u01F4\\u01F5\\u01F8-\\u021B\\u021E\\u021F\\u0226-\\u0233\\u0259\\u02BB\\u02BC\\u02EC\\u0300-\\u0304\\u0306-\\u030C\\u030F-\\u0311\\u0313\\u0314\\u031B\\u0323-\\u0328\\u032D\\u032E\\u0330\\u0331\\u0335\\u0338\\u0339\\u0342\\u0345\\u037B-\\u037D\\u0386\\u0388-\\u038A\\u038C\\u038E-\\u03A1\\u03A3-\\u03CE\\u03FC-\\u045F\\u048A-\\u0529\\u052E\\u052F\\u0531-\\u0556\\u0559\\u0560-\\u0586\\u0588\\u05B4\\u05D0-\\u05EA\\u05EF-\\u05F2\\u0620-\\u063F\\u0641-\\u0655\\u0660-\\u0669\\u0670-\\u0672\\u0674\\u0679-\\u068D\\u068F-\\u06D3\\u06D5\\u06E5\\u06E6\\u06EE-\\u06FC\\u06FF\\u0750-\\u07B1\\u08A0-\\u08AC\\u08B2\\u08B6-\\u08BD\\u0901-\\u094D\\u094F\\u0950\\u0956\\u0957\\u0960-\\u0963\\u0966-\\u096F\\u0971-\\u0977\\u0979-\\u097F\\u0981-\\u0983\\u0985-\\u098C\\u098F\\u0990\\u0993-\\u09A8\\u09AA-\\u09B0\\u09B2\\u09B6-\\u09B9\\u09BC-\\u09C4\\u09C7\\u09C8\\u09CB-\\u09CE\\u09D7\\u09E0-\\u09E3\\u09E6-\\u09F1\\u09FC\\u09FE\\u0A01-\\u0A03\\u0A05-\\u0A0A\\u0A0F\\u0A10\\u0A13-\\u0A28\\u0A2A-\\u0A30\\u0A32\\u0A35\\u0A38\\u0A39\\u0A3C\\u0A3E-\\u0A42\\u0A47\\u0A48\\u0A4B-\\u0A4D\\u0A5C\\u0A66-\\u0A74\\u0A81-\\u0A83\\u0A85-\\u0A8D\\u0A8F-\\u0A91\\u0A93-\\u0AA8\\u0AAA-\\u0AB0\\u0AB2\\u0AB3\\u0AB5-\\u0AB9\\u0ABC-\\u0AC5\\u0AC7-\\u0AC9\\u0ACB-\\u0ACD\\u0AD0\\u0AE0-\\u0AE3\\u0AE6-\\u0AEF\\u0AFA-\\u0AFF\\u0B01-\\u0B03\\u0B05-\\u0B0C\\u0B0F\\u0B10\\u0B13-\\u0B28\\u0B2A-\\u0B30\\u0B32\\u0B33\\u0B35-\\u0B39\\u0B3C-\\u0B43\\u0B47\\u0B48\\u0B4B-\\u0B4D\\u0B56\\u0B57\\u0B5F-\\u0B61\\u0B66-\\u0B6F\\u0B71\\u0B82\\u0B83\\u0B85-\\u0B8A\\u0B8E-\\u0B90\\u0B92-\\u0B95\\u0B99\\u0B9A\\u0B9C\\u0B9E\\u0B9F\\u0BA3\\u0BA4\\u0BA8-\\u0BAA\\u0BAE-\\u0BB9\\u0BBE-\\u0BC2\\u0BC6-\\u0BC8\\u0BCA-\\u0BCD\\u0BD0\\u0BD7\\u0BE6-\\u0BEF\\u0C01-\\u0C0C\\u0C0E-\\u0C10\\u0C12-\\u0C28\\u0C2A-\\u0C33\\u0C35-\\u0C39\\u0C3D-\\u0C44\\u0C46-\\u0C48\\u0C4A-\\u0C4D\\u0C55\\u0C56\\u0C60\\u0C61\\u0C66-\\u0C6F\\u0C80\\u0C82\\u0C83\\u0C85-\\u0C8C\\u0C8E-\\u0C90\\u0C92-\\u0CA8\\u0CAA-\\u0CB3\\u0CB5-\\u0CB9\\u0CBC-\\u0CC4\\u0CC6-\\u0CC8\\u0CCA-\\u0CCD\\u0CD5\\u0CD6\\u0CE0-\\u0CE3\\u0CE6-\\u0CEF\\u0CF1\\u0CF2\\u0D00\\u0D02\\u0D03\\u0D05-\\u0D0C\\u0D0E-\\u0D10\\u0D12-\\u0D43\\u0D46-\\u0D48\\u0D4A-\\u0D4E\\u0D54-\\u0D57\\u0D60\\u0D61\\u0D66-\\u0D6F\\u0D7A-\\u0D7F\\u0D82\\u0D83\\u0D85-\\u0D8E\\u0D91-\\u0D96\\u0D9A-\\u0DA5\\u0DA7-\\u0DB1\\u0DB3-\\u0DBB\\u0DBD\\u0DC0-\\u0DC6\\u0DCA\\u0DCF-\\u0DD4\\u0DD6\\u0DD8-\\u0DDE\\u0DF2\\u0E01-\\u0E32\\u0E34-\\u0E3A\\u0E40-\\u0E4E\\u0E50-\\u0E59\\u0E81\\u0E82\\u0E84\\u0E87\\u0E88\\u0E8A\\u0E8D\\u0E94-\\u0E97\\u0E99-\\u0E9F\\u0EA1-\\u0EA3\\u0EA5\\u0EA7\\u0EAA\\u0EAB\\u0EAD-\\u0EB2\\u0EB4-\\u0EB9\\u0EBB-\\u0EBD\\u0EC0-\\u0EC4\\u0EC6\\u0EC8-\\u0ECD\\u0ED0-\\u0ED9\\u0EDE\\u0EDF\\u0F00\\u0F20-\\u0F29\\u0F35\\u0F37\\u0F3E-\\u0F42\\u0F44-\\u0F47\\u0F49-\\u0F4C\\u0F4E-\\u0F51\\u0F53-\\u0F56\\u0F58-\\u0F5B\\u0F5D-\\u0F68\\u0F6A-\\u0F6C\\u0F71\\u0F72\\u0F74\\u0F7A-\\u0F80\\u0F82-\\u0F84\\u0F86-\\u0F92\\u0F94-\\u0F97\\u0F99-\\u0F9C\\u0F9E-\\u0FA1\\u0FA3-\\u0FA6\\u0FA8-\\u0FAB\\u0FAD-\\u0FB8\\u0FBA-\\u0FBC\\u0FC6\\u1000-\\u1049\\u1050-\\u109D\\u10C7\\u10CD\\u10D0-\\u10F0\\u10F7-\\u10FA\\u10FD-\\u10FF\\u1200-\\u1248\\u124A-\\u124D\\u1250-\\u1256\\u1258\\u125A-\\u125D\\u1260-\\u1288\\u128A-\\u128D\\u1290-\\u12B0\\u12B2-\\u12B5\\u12B8-\\u12BE\\u12C0\\u12C2-\\u12C5\\u12C8-\\u12D6\\u12D8-\\u1310\\u1312-\\u1315\\u1318-\\u135A\\u135D-\\u135F\\u1380-\\u138F\\u1780-\\u17A2\\u17A5-\\u17A7\\u17A9-\\u17B3\\u17B6-\\u17CA\\u17D2\\u17D7\\u17DC\\u17E0-\\u17E9\\u1C80-\\u1C88\\u1C90-\\u1CBA\\u1CBD-\\u1CBF\\u1E00-\\u1E99\\u1E9E\\u1EA0-\\u1EF9\\u1F00-\\u1F15\\u1F18-\\u1F1D\\u1F20-\\u1F45\\u1F48-\\u1F4D\\u1F50-\\u1F57\\u1F59\\u1F5B\\u1F5D\\u1F5F-\\u1F70\\u1F72\\u1F74\\u1F76\\u1F78\\u1F7A\\u1F7C\\u1F80-\\u1FB4\\u1FB6-\\u1FBA\\u1FBC\\u1FC2-\\u1FC4\\u1FC6-\\u1FC8\\u1FCA\\u1FCC\\u1FD0-\\u1FD2\\u1FD6-\\u1FDA\\u1FE0-\\u1FE2\\u1FE4-\\u1FEA\\u1FEC\\u1FF2-\\u1FF4\\u1FF6-\\u1FF8\\u1FFA\\u1FFC\\u2D27\\u2D2D\\u2D80-\\u2D96\\u2DA0-\\u2DA6\\u2DA8-\\u2DAE\\u2DB0-\\u2DB6\\u2DB8-\\u2DBE\\u2DC0-\\u2DC6\\u2DC8-\\u2DCE\\u2DD0-\\u2DD6\\u2DD8-\\u2DDE\\u3005-\\u3007\\u3041-\\u3096\\u3099\\u309A\\u309D\\u309E\\u30A1-\\u30FA\\u30FC-\\u30FE\\u3105-\\u312F\\u31A0-\\u31BA\\u3400-\\u4DB5\\u4E00-\\u9FEF\\uA660\\uA661\\uA674-\\uA67B\\uA67F\\uA69F\\uA717-\\uA71F\\uA788\\uA78D\\uA78E\\uA790-\\uA793\\uA7A0-\\uA7AA\\uA7AE\\uA7AF\\uA7B8\\uA7B9\\uA7FA\\uA9E7-\\uA9FE\\uAA60-\\uAA76\\uAA7A-\\uAA7F\\uAB01-\\uAB06\\uAB09-\\uAB0E\\uAB11-\\uAB16\\uAB20-\\uAB26\\uAB28-\\uAB2E\\uAC00-\\uD7A3\\uFA0E\\uFA0F\\uFA11\\uFA13\\uFA14\\uFA1F\\uFA21\\uFA23\\uFA24\\uFA27-\\uFA29\\U0001133B\\U0001B002-\\U0001B11E\\U00020000-\\U0002A6D6\\U0002A700-\\U0002B734\\U0002B740-\\U0002B81D\\U0002B820-\\U0002CEA1\\U0002CEB0-\\U0002EBE0]").freeze();
    public static final int SINGLE_SCRIPT_CONFUSABLE = 1;
    public static final int MIXED_SCRIPT_CONFUSABLE = 2;
    public static final int WHOLE_SCRIPT_CONFUSABLE = 4;
    public static final int CONFUSABLE = 7;
    @Deprecated
    public static final int ANY_CASE = 8;
    public static final int RESTRICTION_LEVEL = 16;
    @Deprecated
    public static final int SINGLE_SCRIPT = 16;
    public static final int INVISIBLE = 32;
    public static final int CHAR_LIMIT = 64;
    public static final int MIXED_NUMBERS = 128;
    public static final int HIDDEN_OVERLAY = 256;
    public static final int ALL_CHECKS = -1;
    static final UnicodeSet ASCII = new UnicodeSet(0, 127).freeze();
    private int fChecks;
    private SpoofData fSpoofData;
    private Set<ULocale> fAllowedLocales;
    private UnicodeSet fAllowedCharsSet;
    private RestrictionLevel fRestrictionLevel;
    private static Normalizer2 nfdNormalizer = Normalizer2.getNFDInstance();

    private SpoofChecker() {
    }

    @Deprecated
    public RestrictionLevel getRestrictionLevel() {
        return this.fRestrictionLevel;
    }

    public int getChecks() {
        return this.fChecks;
    }

    public Set<ULocale> getAllowedLocales() {
        return Collections.unmodifiableSet(this.fAllowedLocales);
    }

    public Set<Locale> getAllowedJavaLocales() {
        HashSet<Locale> locales = new HashSet<Locale>(this.fAllowedLocales.size());
        for (ULocale uloc : this.fAllowedLocales) {
            locales.add(uloc.toLocale());
        }
        return locales;
    }

    public UnicodeSet getAllowedChars() {
        return this.fAllowedCharsSet;
    }

    public boolean failsChecks(String text, CheckResult checkResult) {
        int index;
        int length = text.length();
        int result = 0;
        if (checkResult != null) {
            checkResult.position = 0;
            checkResult.numerics = null;
            checkResult.restrictionLevel = null;
        }
        if (0 != (this.fChecks & 0x10)) {
            RestrictionLevel textRestrictionLevel = this.getRestrictionLevel(text);
            if (textRestrictionLevel.compareTo(this.fRestrictionLevel) > 0) {
                result |= 0x10;
            }
            if (checkResult != null) {
                checkResult.restrictionLevel = textRestrictionLevel;
            }
        }
        if (0 != (this.fChecks & 0x80)) {
            UnicodeSet numerics = new UnicodeSet();
            this.getNumerics(text, numerics);
            if (numerics.size() > 1) {
                result |= 0x80;
            }
            if (checkResult != null) {
                checkResult.numerics = numerics;
            }
        }
        if (0 != (this.fChecks & 0x100) && (index = this.findHiddenOverlay(text)) != -1) {
            result |= 0x100;
        }
        if (0 != (this.fChecks & 0x40)) {
            int i2 = 0;
            while (i2 < length) {
                int c2 = Character.codePointAt(text, i2);
                i2 = Character.offsetByCodePoints(text, i2, 1);
                if (this.fAllowedCharsSet.contains(c2)) continue;
                result |= 0x40;
                break;
            }
        }
        if (0 != (this.fChecks & 0x20)) {
            String nfdText = nfdNormalizer.normalize(text);
            int firstNonspacingMark = 0;
            boolean haveMultipleMarks = false;
            UnicodeSet marksSeenSoFar = new UnicodeSet();
            int i3 = 0;
            while (i3 < length) {
                int c3 = Character.codePointAt(nfdText, i3);
                i3 = Character.offsetByCodePoints(nfdText, i3, 1);
                if (Character.getType(c3) != 6) {
                    firstNonspacingMark = 0;
                    if (!haveMultipleMarks) continue;
                    marksSeenSoFar.clear();
                    haveMultipleMarks = false;
                    continue;
                }
                if (firstNonspacingMark == 0) {
                    firstNonspacingMark = c3;
                    continue;
                }
                if (!haveMultipleMarks) {
                    marksSeenSoFar.add(firstNonspacingMark);
                    haveMultipleMarks = true;
                }
                if (marksSeenSoFar.contains(c3)) {
                    result |= 0x20;
                    break;
                }
                marksSeenSoFar.add(c3);
            }
        }
        if (checkResult != null) {
            checkResult.checks = result;
        }
        return 0 != result;
    }

    public boolean failsChecks(String text) {
        return this.failsChecks(text, null);
    }

    public int areConfusable(String s1, String s2) {
        String s2Skeleton;
        if ((this.fChecks & 7) == 0) {
            throw new IllegalArgumentException("No confusable checks are enabled.");
        }
        String s1Skeleton = this.getSkeleton(s1);
        if (!s1Skeleton.equals(s2Skeleton = this.getSkeleton(s2))) {
            return 0;
        }
        ScriptSet s1RSS = new ScriptSet();
        this.getResolvedScriptSet(s1, s1RSS);
        ScriptSet s2RSS = new ScriptSet();
        this.getResolvedScriptSet(s2, s2RSS);
        int result = 0;
        if (s1RSS.intersects(s2RSS)) {
            result |= 1;
        } else {
            result |= 2;
            if (!s1RSS.isEmpty() && !s2RSS.isEmpty()) {
                result |= 4;
            }
        }
        return result &= this.fChecks;
    }

    public String getSkeleton(CharSequence str) {
        int c2;
        String nfdId = nfdNormalizer.normalize(str);
        int normalizedLen = nfdId.length();
        StringBuilder skelSB = new StringBuilder();
        for (int inputIndex = 0; inputIndex < normalizedLen; inputIndex += Character.charCount(c2)) {
            c2 = Character.codePointAt(nfdId, inputIndex);
            this.fSpoofData.confusableLookup(c2, skelSB);
        }
        String skelStr = skelSB.toString();
        skelStr = nfdNormalizer.normalize(skelStr);
        return skelStr;
    }

    @Deprecated
    public String getSkeleton(int type, String id) {
        return this.getSkeleton(id);
    }

    public boolean equals(Object other) {
        if (!(other instanceof SpoofChecker)) {
            return false;
        }
        SpoofChecker otherSC = (SpoofChecker)other;
        if (this.fSpoofData != otherSC.fSpoofData && this.fSpoofData != null && !this.fSpoofData.equals(otherSC.fSpoofData)) {
            return false;
        }
        if (this.fChecks != otherSC.fChecks) {
            return false;
        }
        if (this.fAllowedLocales != otherSC.fAllowedLocales && this.fAllowedLocales != null && !this.fAllowedLocales.equals(otherSC.fAllowedLocales)) {
            return false;
        }
        if (this.fAllowedCharsSet != otherSC.fAllowedCharsSet && this.fAllowedCharsSet != null && !this.fAllowedCharsSet.equals(otherSC.fAllowedCharsSet)) {
            return false;
        }
        return this.fRestrictionLevel == otherSC.fRestrictionLevel;
    }

    public int hashCode() {
        return this.fChecks ^ this.fSpoofData.hashCode() ^ this.fAllowedLocales.hashCode() ^ this.fAllowedCharsSet.hashCode() ^ this.fRestrictionLevel.ordinal();
    }

    private static void getAugmentedScriptSet(int codePoint, ScriptSet result) {
        result.clear();
        UScript.getScriptExtensions(codePoint, result);
        if (result.get(17)) {
            result.set(172);
            result.set(105);
            result.set(119);
        }
        if (result.get(20)) {
            result.set(105);
        }
        if (result.get(22)) {
            result.set(105);
        }
        if (result.get(18)) {
            result.set(119);
        }
        if (result.get(5)) {
            result.set(172);
        }
        if (result.get(0) || result.get(1)) {
            result.setAll();
        }
    }

    private void getResolvedScriptSet(CharSequence input, ScriptSet result) {
        this.getResolvedScriptSetWithout(input, 185, result);
    }

    private void getResolvedScriptSetWithout(CharSequence input, int script, ScriptSet result) {
        int codePoint;
        result.setAll();
        ScriptSet temp = new ScriptSet();
        for (int utf16Offset = 0; utf16Offset < input.length(); utf16Offset += Character.charCount(codePoint)) {
            codePoint = Character.codePointAt(input, utf16Offset);
            SpoofChecker.getAugmentedScriptSet(codePoint, temp);
            if (script != 185 && temp.get(script)) continue;
            result.and(temp);
        }
    }

    private void getNumerics(String input, UnicodeSet result) {
        int codePoint;
        result.clear();
        for (int utf16Offset = 0; utf16Offset < input.length(); utf16Offset += Character.charCount(codePoint)) {
            codePoint = Character.codePointAt(input, utf16Offset);
            if (UCharacter.getType(codePoint) != 9) continue;
            result.add(codePoint - UCharacter.getNumericValue(codePoint));
        }
    }

    private RestrictionLevel getRestrictionLevel(String input) {
        if (!this.fAllowedCharsSet.containsAll(input)) {
            return RestrictionLevel.UNRESTRICTIVE;
        }
        if (ASCII.containsAll(input)) {
            return RestrictionLevel.ASCII;
        }
        ScriptSet resolvedScriptSet = new ScriptSet();
        this.getResolvedScriptSet(input, resolvedScriptSet);
        if (!resolvedScriptSet.isEmpty()) {
            return RestrictionLevel.SINGLE_SCRIPT_RESTRICTIVE;
        }
        ScriptSet resolvedNoLatn = new ScriptSet();
        this.getResolvedScriptSetWithout(input, 25, resolvedNoLatn);
        if (resolvedNoLatn.get(172) || resolvedNoLatn.get(105) || resolvedNoLatn.get(119)) {
            return RestrictionLevel.HIGHLY_RESTRICTIVE;
        }
        if (!(resolvedNoLatn.isEmpty() || resolvedNoLatn.get(8) || resolvedNoLatn.get(14) || resolvedNoLatn.get(6))) {
            return RestrictionLevel.MODERATELY_RESTRICTIVE;
        }
        return RestrictionLevel.MINIMALLY_RESTRICTIVE;
    }

    int findHiddenOverlay(String input) {
        int cp;
        boolean sawLeadCharacter = false;
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < input.length(); i2 += UCharacter.charCount(cp)) {
            cp = input.codePointAt(i2);
            if (sawLeadCharacter && cp == 775) {
                return i2;
            }
            int combiningClass = UCharacter.getCombiningClass(cp);
            assert (UCharacter.getCombiningClass(775) == 230);
            if (combiningClass != 0 && combiningClass != 230) continue;
            sawLeadCharacter = this.isIllegalCombiningDotLeadCharacter(cp, sb);
        }
        return -1;
    }

    boolean isIllegalCombiningDotLeadCharacterNoLookup(int cp) {
        return cp == 105 || cp == 106 || cp == 305 || cp == 567 || cp == 108 || UCharacter.hasBinaryProperty(cp, 27);
    }

    boolean isIllegalCombiningDotLeadCharacter(int cp, StringBuilder sb) {
        if (this.isIllegalCombiningDotLeadCharacterNoLookup(cp)) {
            return true;
        }
        sb.setLength(0);
        this.fSpoofData.confusableLookup(cp, sb);
        int finalCp = UCharacter.codePointBefore(sb, sb.length());
        return finalCp != cp && this.isIllegalCombiningDotLeadCharacterNoLookup(finalCp);
    }

    static class ScriptSet
    extends BitSet {
        private static final long serialVersionUID = 1L;

        ScriptSet() {
        }

        public void and(int script) {
            this.clear(0, script);
            this.clear(script + 1, 185);
        }

        public void setAll() {
            this.set(0, 185);
        }

        public boolean isFull() {
            return this.cardinality() == 185;
        }

        public void appendStringTo(StringBuilder sb) {
            sb.append("{ ");
            if (this.isEmpty()) {
                sb.append("- ");
            } else if (this.isFull()) {
                sb.append("* ");
            } else {
                for (int script = 0; script < 185; ++script) {
                    if (!this.get(script)) continue;
                    sb.append(UScript.getShortName(script));
                    sb.append(" ");
                }
            }
            sb.append("}");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<ScriptSet ");
            this.appendStringTo(sb);
            sb.append(">");
            return sb.toString();
        }
    }

    private static class SpoofData {
        int[] fCFUKeys;
        short[] fCFUValues;
        String fCFUStrings;
        private static final int DATA_FORMAT = 1130788128;
        private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();

        public static SpoofData getDefault() {
            if (DefaultData.EXCEPTION != null) {
                throw new MissingResourceException("Could not load default confusables data: " + DefaultData.EXCEPTION.getMessage(), "SpoofChecker", "");
            }
            return DefaultData.INSTANCE;
        }

        private SpoofData() {
        }

        private SpoofData(ByteBuffer bytes) throws IOException {
            ICUBinary.readHeader(bytes, 1130788128, IS_ACCEPTABLE);
            bytes.mark();
            this.readData(bytes);
        }

        public boolean equals(Object other) {
            if (!(other instanceof SpoofData)) {
                return false;
            }
            SpoofData otherData = (SpoofData)other;
            if (!Arrays.equals(this.fCFUKeys, otherData.fCFUKeys)) {
                return false;
            }
            if (!Arrays.equals(this.fCFUValues, otherData.fCFUValues)) {
                return false;
            }
            return Utility.sameObjects(this.fCFUStrings, otherData.fCFUStrings) || this.fCFUStrings == null || this.fCFUStrings.equals(otherData.fCFUStrings);
        }

        public int hashCode() {
            return Arrays.hashCode(this.fCFUKeys) ^ Arrays.hashCode(this.fCFUValues) ^ this.fCFUStrings.hashCode();
        }

        private void readData(ByteBuffer bytes) throws IOException {
            int magic = bytes.getInt();
            if (magic != 944111087) {
                throw new IllegalArgumentException("Bad Spoof Check Data.");
            }
            int dataFormatVersion = bytes.getInt();
            int dataLength = bytes.getInt();
            int CFUKeysOffset = bytes.getInt();
            int CFUKeysSize = bytes.getInt();
            int CFUValuesOffset = bytes.getInt();
            int CFUValuesSize = bytes.getInt();
            int CFUStringTableOffset = bytes.getInt();
            int CFUStringTableSize = bytes.getInt();
            bytes.reset();
            ICUBinary.skipBytes(bytes, CFUKeysOffset);
            this.fCFUKeys = ICUBinary.getInts(bytes, CFUKeysSize, 0);
            bytes.reset();
            ICUBinary.skipBytes(bytes, CFUValuesOffset);
            this.fCFUValues = ICUBinary.getShorts(bytes, CFUValuesSize, 0);
            bytes.reset();
            ICUBinary.skipBytes(bytes, CFUStringTableOffset);
            this.fCFUStrings = ICUBinary.getString(bytes, CFUStringTableSize, 0);
        }

        public void confusableLookup(int inChar, StringBuilder dest) {
            int lo = 0;
            int hi = this.length();
            do {
                int mid;
                if (this.codePointAt(mid = (lo + hi) / 2) > inChar) {
                    hi = mid;
                    continue;
                }
                if (this.codePointAt(mid) < inChar) {
                    lo = mid;
                    continue;
                }
                lo = mid;
                break;
            } while (hi - lo > 1);
            if (this.codePointAt(lo) != inChar) {
                dest.appendCodePoint(inChar);
                return;
            }
            this.appendValueTo(lo, dest);
        }

        public int length() {
            return this.fCFUKeys.length;
        }

        public int codePointAt(int index) {
            return ConfusableDataUtils.keyToCodePoint(this.fCFUKeys[index]);
        }

        public void appendValueTo(int index, StringBuilder dest) {
            int stringLength = ConfusableDataUtils.keyToLength(this.fCFUKeys[index]);
            short value = this.fCFUValues[index];
            if (stringLength == 1) {
                dest.append((char)value);
            } else {
                dest.append(this.fCFUStrings, (int)value, value + stringLength);
            }
        }

        private static final class DefaultData {
            private static SpoofData INSTANCE = null;
            private static IOException EXCEPTION = null;

            private DefaultData() {
            }

            static {
                try {
                    INSTANCE = new SpoofData(ICUBinary.getRequiredData("confusables.cfu"));
                }
                catch (IOException e2) {
                    EXCEPTION = e2;
                }
            }
        }

        private static final class IsAcceptable
        implements ICUBinary.Authenticate {
            private IsAcceptable() {
            }

            @Override
            public boolean isDataVersionAcceptable(byte[] version) {
                return version[0] == 2 || version[1] != 0 || version[2] != 0 || version[3] != 0;
            }
        }
    }

    private static final class ConfusableDataUtils {
        public static final int FORMAT_VERSION = 2;

        private ConfusableDataUtils() {
        }

        public static final int keyToCodePoint(int key) {
            return key & 0xFFFFFF;
        }

        public static final int keyToLength(int key) {
            return ((key & 0xFF000000) >> 24) + 1;
        }

        public static final int codePointAndLengthToKey(int codePoint, int length) {
            assert ((codePoint & 0xFFFFFF) == codePoint);
            assert (length <= 256);
            return codePoint | length - 1 << 24;
        }
    }

    public static class CheckResult {
        public int checks = 0;
        @Deprecated
        public int position = 0;
        public UnicodeSet numerics;
        public RestrictionLevel restrictionLevel;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("checks:");
            if (this.checks == 0) {
                sb.append(" none");
            } else if (this.checks == -1) {
                sb.append(" all");
            } else {
                if ((this.checks & 1) != 0) {
                    sb.append(" SINGLE_SCRIPT_CONFUSABLE");
                }
                if ((this.checks & 2) != 0) {
                    sb.append(" MIXED_SCRIPT_CONFUSABLE");
                }
                if ((this.checks & 4) != 0) {
                    sb.append(" WHOLE_SCRIPT_CONFUSABLE");
                }
                if ((this.checks & 8) != 0) {
                    sb.append(" ANY_CASE");
                }
                if ((this.checks & 0x10) != 0) {
                    sb.append(" RESTRICTION_LEVEL");
                }
                if ((this.checks & 0x20) != 0) {
                    sb.append(" INVISIBLE");
                }
                if ((this.checks & 0x40) != 0) {
                    sb.append(" CHAR_LIMIT");
                }
                if ((this.checks & 0x80) != 0) {
                    sb.append(" MIXED_NUMBERS");
                }
            }
            sb.append(", numerics: ").append(this.numerics.toPattern(false));
            sb.append(", position: ").append(this.position);
            sb.append(", restrictionLevel: ").append((Object)this.restrictionLevel);
            return sb.toString();
        }
    }

    public static class Builder {
        int fChecks;
        SpoofData fSpoofData;
        final UnicodeSet fAllowedCharsSet = new UnicodeSet(0, 0x10FFFF);
        final Set<ULocale> fAllowedLocales = new LinkedHashSet<ULocale>();
        private RestrictionLevel fRestrictionLevel;

        public Builder() {
            this.fChecks = -1;
            this.fSpoofData = null;
            this.fRestrictionLevel = RestrictionLevel.HIGHLY_RESTRICTIVE;
        }

        public Builder(SpoofChecker src) {
            this.fChecks = src.fChecks;
            this.fSpoofData = src.fSpoofData;
            this.fAllowedCharsSet.set(src.fAllowedCharsSet);
            this.fAllowedLocales.addAll(src.fAllowedLocales);
            this.fRestrictionLevel = src.fRestrictionLevel;
        }

        public SpoofChecker build() {
            if (this.fSpoofData == null) {
                this.fSpoofData = SpoofData.getDefault();
            }
            SpoofChecker result = new SpoofChecker();
            result.fChecks = this.fChecks;
            result.fSpoofData = this.fSpoofData;
            result.fAllowedCharsSet = (UnicodeSet)this.fAllowedCharsSet.clone();
            result.fAllowedCharsSet.freeze();
            result.fAllowedLocales = new HashSet<ULocale>(this.fAllowedLocales);
            result.fRestrictionLevel = this.fRestrictionLevel;
            return result;
        }

        public Builder setData(Reader confusables) throws ParseException, IOException {
            this.fSpoofData = new SpoofData();
            ConfusabledataBuilder.buildConfusableData(confusables, this.fSpoofData);
            return this;
        }

        @Deprecated
        public Builder setData(Reader confusables, Reader confusablesWholeScript) throws ParseException, IOException {
            this.setData(confusables);
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
            this.fAllowedLocales.clear();
            this.fAllowedLocales.addAll(locales);
            this.fChecks |= 0x40;
            return this;
        }

        public Builder setAllowedJavaLocales(Set<Locale> locales) {
            HashSet<ULocale> ulocales = new HashSet<ULocale>(locales.size());
            for (Locale locale : locales) {
                ulocales.add(ULocale.forLocale(locale));
            }
            return this.setAllowedLocales(ulocales);
        }

        private void addScriptChars(ULocale locale, UnicodeSet allowedChars) {
            int[] scripts = UScript.getCode(locale);
            if (scripts != null) {
                UnicodeSet tmpSet = new UnicodeSet();
                for (int i2 = 0; i2 < scripts.length; ++i2) {
                    tmpSet.applyIntPropertyValue(4106, scripts[i2]);
                    allowedChars.addAll(tmpSet);
                }
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
            this.fChecks |= 0x90;
            return this;
        }

        private static class ConfusabledataBuilder {
            private Hashtable<Integer, SPUString> fTable = new Hashtable();
            private UnicodeSet fKeySet = new UnicodeSet();
            private StringBuffer fStringTable;
            private ArrayList<Integer> fKeyVec = new ArrayList();
            private ArrayList<Integer> fValueVec = new ArrayList();
            private SPUStringPool stringPool = new SPUStringPool();
            private Pattern fParseLine;
            private Pattern fParseHexNum;
            private int fLineNum;

            ConfusabledataBuilder() {
            }

            void build(Reader confusables, SpoofData dest) throws ParseException, IOException {
                int i2;
                String line;
                StringBuffer fInput = new StringBuffer();
                LineNumberReader lnr = new LineNumberReader(confusables);
                while ((line = lnr.readLine()) != null) {
                    fInput.append(line);
                    fInput.append('\n');
                }
                this.fParseLine = Pattern.compile("(?m)^[ \\t]*([0-9A-Fa-f]+)[ \\t]+;[ \\t]*([0-9A-Fa-f]+(?:[ \\t]+[0-9A-Fa-f]+)*)[ \\t]*;\\s*(?:(SL)|(SA)|(ML)|(MA))[ \\t]*(?:#.*?)?$|^([ \\t]*(?:#.*?)?)$|^(.*?)$");
                this.fParseHexNum = Pattern.compile("\\s*([0-9A-F]+)");
                if (fInput.charAt(0) == '\ufeff') {
                    fInput.setCharAt(0, ' ');
                }
                Matcher matcher = this.fParseLine.matcher(fInput);
                while (matcher.find()) {
                    ++this.fLineNum;
                    if (matcher.start(7) >= 0) continue;
                    if (matcher.start(8) >= 0) {
                        throw new ParseException("Confusables, line " + this.fLineNum + ": Unrecognized Line: " + matcher.group(8), matcher.start(8));
                    }
                    int keyChar = Integer.parseInt(matcher.group(1), 16);
                    if (keyChar > 0x10FFFF) {
                        throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + matcher.group(1), matcher.start(1));
                    }
                    Matcher m2 = this.fParseHexNum.matcher(matcher.group(2));
                    StringBuilder mapString = new StringBuilder();
                    while (m2.find()) {
                        int c2 = Integer.parseInt(m2.group(1), 16);
                        if (c2 > 0x10FFFF) {
                            throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + Integer.toString(c2, 16), matcher.start(2));
                        }
                        mapString.appendCodePoint(c2);
                    }
                    assert (mapString.length() >= 1);
                    SPUString smapString = this.stringPool.addString(mapString.toString());
                    this.fTable.put(keyChar, smapString);
                    this.fKeySet.add(keyChar);
                }
                this.stringPool.sort();
                this.fStringTable = new StringBuffer();
                int poolSize = this.stringPool.size();
                for (i2 = 0; i2 < poolSize; ++i2) {
                    SPUString s2 = this.stringPool.getByIndex(i2);
                    int strLen = s2.fStr.length();
                    int strIndex = this.fStringTable.length();
                    if (strLen == 1) {
                        s2.fCharOrStrTableIndex = s2.fStr.charAt(0);
                        continue;
                    }
                    s2.fCharOrStrTableIndex = strIndex;
                    this.fStringTable.append(s2.fStr);
                }
                for (String keyCharStr : this.fKeySet) {
                    int keyChar = keyCharStr.codePointAt(0);
                    SPUString targetMapping = this.fTable.get(keyChar);
                    assert (targetMapping != null);
                    if (targetMapping.fStr.length() > 256) {
                        throw new IllegalArgumentException("Confusable prototypes cannot be longer than 256 entries.");
                    }
                    int key = ConfusableDataUtils.codePointAndLengthToKey(keyChar, targetMapping.fStr.length());
                    int value = targetMapping.fCharOrStrTableIndex;
                    this.fKeyVec.add(key);
                    this.fValueVec.add(value);
                }
                int numKeys = this.fKeyVec.size();
                dest.fCFUKeys = new int[numKeys];
                int previousCodePoint = 0;
                for (i2 = 0; i2 < numKeys; ++i2) {
                    int key = this.fKeyVec.get(i2);
                    int codePoint = ConfusableDataUtils.keyToCodePoint(key);
                    assert (codePoint > previousCodePoint);
                    dest.fCFUKeys[i2] = key;
                    previousCodePoint = codePoint;
                }
                int numValues = this.fValueVec.size();
                assert (numKeys == numValues);
                dest.fCFUValues = new short[numValues];
                i2 = 0;
                for (int value : this.fValueVec) {
                    assert (value < 65535);
                    dest.fCFUValues[i2++] = (short)value;
                }
                dest.fCFUStrings = this.fStringTable.toString();
            }

            public static void buildConfusableData(Reader confusables, SpoofData dest) throws IOException, ParseException {
                ConfusabledataBuilder builder = new ConfusabledataBuilder();
                builder.build(confusables, dest);
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
                    Collections.sort(this.fVec, SPUStringComparator.INSTANCE);
                }
            }

            private static class SPUStringComparator
            implements Comparator<SPUString> {
                static final SPUStringComparator INSTANCE = new SPUStringComparator();

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
                int fCharOrStrTableIndex;

                SPUString(String s2) {
                    this.fStr = s2;
                    this.fCharOrStrTableIndex = 0;
                }
            }
        }
    }

    public static enum RestrictionLevel {
        ASCII,
        SINGLE_SCRIPT_RESTRICTIVE,
        HIGHLY_RESTRICTIVE,
        MODERATELY_RESTRICTIVE,
        MINIMALLY_RESTRICTIVE,
        UNRESTRICTIVE;

    }
}

