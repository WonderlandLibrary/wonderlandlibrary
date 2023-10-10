/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecog_2022;
import com.ibm.icu.text.CharsetRecog_UTF8;
import com.ibm.icu.text.CharsetRecog_Unicode;
import com.ibm.icu.text.CharsetRecog_mbcs;
import com.ibm.icu.text.CharsetRecog_sbcs;
import com.ibm.icu.text.CharsetRecognizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CharsetDetector {
    private static final int kBufSize = 8000;
    byte[] fInputBytes = new byte[8000];
    int fInputLen;
    short[] fByteStats = new short[256];
    boolean fC1Bytes = false;
    String fDeclaredEncoding;
    byte[] fRawInput;
    int fRawLength;
    InputStream fInputStream;
    private boolean fStripTags = false;
    private boolean[] fEnabledRecognizers;
    private static final List<CSRecognizerInfo> ALL_CS_RECOGNIZERS;

    public CharsetDetector setDeclaredEncoding(String encoding) {
        this.fDeclaredEncoding = encoding;
        return this;
    }

    public CharsetDetector setText(byte[] in) {
        this.fRawInput = in;
        this.fRawLength = in.length;
        return this;
    }

    public CharsetDetector setText(InputStream in) throws IOException {
        int bytesRead;
        this.fInputStream = in;
        this.fInputStream.mark(8000);
        this.fRawInput = new byte[8000];
        this.fRawLength = 0;
        for (int remainingLength = 8000; remainingLength > 0 && (bytesRead = this.fInputStream.read(this.fRawInput, this.fRawLength, remainingLength)) > 0; remainingLength -= bytesRead) {
            this.fRawLength += bytesRead;
        }
        this.fInputStream.reset();
        return this;
    }

    public CharsetMatch detect() {
        CharsetMatch[] matches = this.detectAll();
        if (matches == null || matches.length == 0) {
            return null;
        }
        return matches[0];
    }

    public CharsetMatch[] detectAll() {
        ArrayList<CharsetMatch> matches = new ArrayList<CharsetMatch>();
        this.MungeInput();
        for (int i2 = 0; i2 < ALL_CS_RECOGNIZERS.size(); ++i2) {
            CharsetMatch m2;
            boolean active;
            CSRecognizerInfo rcinfo = ALL_CS_RECOGNIZERS.get(i2);
            boolean bl = active = this.fEnabledRecognizers != null ? this.fEnabledRecognizers[i2] : rcinfo.isDefaultEnabled;
            if (!active || (m2 = rcinfo.recognizer.match(this)) == null) continue;
            matches.add(m2);
        }
        Collections.sort(matches);
        Collections.reverse(matches);
        CharsetMatch[] resultArray = new CharsetMatch[matches.size()];
        resultArray = matches.toArray(resultArray);
        return resultArray;
    }

    public Reader getReader(InputStream in, String declaredEncoding) {
        this.fDeclaredEncoding = declaredEncoding;
        try {
            this.setText(in);
            CharsetMatch match = this.detect();
            if (match == null) {
                return null;
            }
            return match.getReader();
        }
        catch (IOException e2) {
            return null;
        }
    }

    public String getString(byte[] in, String declaredEncoding) {
        this.fDeclaredEncoding = declaredEncoding;
        try {
            this.setText(in);
            CharsetMatch match = this.detect();
            if (match == null) {
                return null;
            }
            return match.getString(-1);
        }
        catch (IOException e2) {
            return null;
        }
    }

    public static String[] getAllDetectableCharsets() {
        String[] allCharsetNames = new String[ALL_CS_RECOGNIZERS.size()];
        for (int i2 = 0; i2 < allCharsetNames.length; ++i2) {
            allCharsetNames[i2] = CharsetDetector.ALL_CS_RECOGNIZERS.get((int)i2).recognizer.getName();
        }
        return allCharsetNames;
    }

    public boolean inputFilterEnabled() {
        return this.fStripTags;
    }

    public boolean enableInputFilter(boolean filter) {
        boolean previous = this.fStripTags;
        this.fStripTags = filter;
        return previous;
    }

    private void MungeInput() {
        int srci = 0;
        int dsti = 0;
        boolean inMarkup = false;
        int openTags = 0;
        int badTags = 0;
        if (this.fStripTags) {
            for (srci = 0; srci < this.fRawLength && dsti < this.fInputBytes.length; ++srci) {
                byte b2 = this.fRawInput[srci];
                if (b2 == 60) {
                    if (inMarkup) {
                        ++badTags;
                    }
                    inMarkup = true;
                    ++openTags;
                }
                if (!inMarkup) {
                    this.fInputBytes[dsti++] = b2;
                }
                if (b2 != 62) continue;
                inMarkup = false;
            }
            this.fInputLen = dsti;
        }
        if (openTags < 5 || openTags / 5 < badTags || this.fInputLen < 100 && this.fRawLength > 600) {
            int limit = this.fRawLength;
            if (limit > 8000) {
                limit = 8000;
            }
            for (srci = 0; srci < limit; ++srci) {
                this.fInputBytes[srci] = this.fRawInput[srci];
            }
            this.fInputLen = srci;
        }
        Arrays.fill(this.fByteStats, (short)0);
        for (srci = 0; srci < this.fInputLen; ++srci) {
            int val2;
            int n2 = val2 = this.fInputBytes[srci] & 0xFF;
            this.fByteStats[n2] = (short)(this.fByteStats[n2] + 1);
        }
        this.fC1Bytes = false;
        for (int i2 = 128; i2 <= 159; ++i2) {
            if (this.fByteStats[i2] == 0) continue;
            this.fC1Bytes = true;
            break;
        }
    }

    @Deprecated
    public String[] getDetectableCharsets() {
        ArrayList<String> csnames = new ArrayList<String>(ALL_CS_RECOGNIZERS.size());
        for (int i2 = 0; i2 < ALL_CS_RECOGNIZERS.size(); ++i2) {
            boolean active;
            CSRecognizerInfo rcinfo = ALL_CS_RECOGNIZERS.get(i2);
            boolean bl = active = this.fEnabledRecognizers == null ? rcinfo.isDefaultEnabled : this.fEnabledRecognizers[i2];
            if (!active) continue;
            csnames.add(rcinfo.recognizer.getName());
        }
        return csnames.toArray(new String[csnames.size()]);
    }

    @Deprecated
    public CharsetDetector setDetectableCharset(String encoding, boolean enabled) {
        int i2;
        int modIdx = -1;
        boolean isDefaultVal = false;
        for (i2 = 0; i2 < ALL_CS_RECOGNIZERS.size(); ++i2) {
            CSRecognizerInfo csrinfo = ALL_CS_RECOGNIZERS.get(i2);
            if (!csrinfo.recognizer.getName().equals(encoding)) continue;
            modIdx = i2;
            isDefaultVal = csrinfo.isDefaultEnabled == enabled;
            break;
        }
        if (modIdx < 0) {
            throw new IllegalArgumentException("Invalid encoding: \"" + encoding + "\"");
        }
        if (this.fEnabledRecognizers == null && !isDefaultVal) {
            this.fEnabledRecognizers = new boolean[ALL_CS_RECOGNIZERS.size()];
            for (i2 = 0; i2 < ALL_CS_RECOGNIZERS.size(); ++i2) {
                this.fEnabledRecognizers[i2] = CharsetDetector.ALL_CS_RECOGNIZERS.get((int)i2).isDefaultEnabled;
            }
        }
        if (this.fEnabledRecognizers != null) {
            this.fEnabledRecognizers[modIdx] = enabled;
        }
        return this;
    }

    static {
        ArrayList<CSRecognizerInfo> list = new ArrayList<CSRecognizerInfo>();
        list.add(new CSRecognizerInfo(new CharsetRecog_UTF8(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_Unicode.CharsetRecog_UTF_16_BE(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_Unicode.CharsetRecog_UTF_16_LE(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_Unicode.CharsetRecog_UTF_32_BE(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_Unicode.CharsetRecog_UTF_32_LE(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_mbcs.CharsetRecog_sjis(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_2022.CharsetRecog_2022JP(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_2022.CharsetRecog_2022CN(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_2022.CharsetRecog_2022KR(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_mbcs.CharsetRecog_gb_18030(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_jp(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_kr(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_mbcs.CharsetRecog_big5(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_1(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_2(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_5_ru(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_6_ar(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_7_el(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_8_I_he(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_8_he(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_windows_1251(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_windows_1256(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_KOI8_R(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_8859_9_tr(), true));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_rtl(), false));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_ltr(), false));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_rtl(), false));
        list.add(new CSRecognizerInfo(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_ltr(), false));
        ALL_CS_RECOGNIZERS = Collections.unmodifiableList(list);
    }

    private static class CSRecognizerInfo {
        CharsetRecognizer recognizer;
        boolean isDefaultEnabled;

        CSRecognizerInfo(CharsetRecognizer recognizer, boolean isDefaultEnabled) {
            this.recognizer = recognizer;
            this.isDefaultEnabled = isDefaultEnabled;
        }
    }
}

