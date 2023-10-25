/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic
implements StringEncoder {
    private static final char[] AEIJOUY = new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
    private static final char[] SCZ = new char[]{'S', 'C', 'Z'};
    private static final char[] WFPV = new char[]{'W', 'F', 'P', 'V'};
    private static final char[] GKQ = new char[]{'G', 'K', 'Q'};
    private static final char[] CKQ = new char[]{'C', 'K', 'Q'};
    private static final char[] AHKLOQRUX = new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
    private static final char[] SZ = new char[]{'S', 'Z'};
    private static final char[] AHOUKQX = new char[]{'A', 'H', 'O', 'U', 'K', 'Q', 'X'};
    private static final char[] TDX = new char[]{'T', 'D', 'X'};
    private static final char[][] PREPROCESS_MAP = new char[][]{{'\u00c4', 'A'}, {'\u00dc', 'U'}, {'\u00d6', 'O'}, {'\u00df', 'S'}};

    private static boolean arrayContains(char[] arr, char key) {
        for (char element : arr) {
            if (element != key) continue;
            return true;
        }
        return false;
    }

    public String colognePhonetic(String text) {
        if (text == null) {
            return null;
        }
        text = this.preprocess(text);
        CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
        CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
        char lastChar = '-';
        int lastCode = 47;
        int rightLength = input.length();
        while (rightLength > 0) {
            int code;
            char chr = input.removeNext();
            rightLength = input.length();
            char nextChar = rightLength > 0 ? (char)input.getNextChar() : (char)'-';
            if (ColognePhonetic.arrayContains(AEIJOUY, chr)) {
                code = 48;
            } else if (chr == 'H' || chr < 'A' || chr > 'Z') {
                if (lastCode == 47) continue;
                code = 45;
            } else if (chr == 'B' || chr == 'P' && nextChar != 'H') {
                code = 49;
            } else if (!(chr != 'D' && chr != 'T' || ColognePhonetic.arrayContains(SCZ, nextChar))) {
                code = 50;
            } else if (ColognePhonetic.arrayContains(WFPV, chr)) {
                code = 51;
            } else if (ColognePhonetic.arrayContains(GKQ, chr)) {
                code = 52;
            } else if (chr == 'X' && !ColognePhonetic.arrayContains(CKQ, lastChar)) {
                code = 52;
                input.addLeft('S');
                ++rightLength;
            } else {
                code = chr == 'S' || chr == 'Z' ? 56 : (chr == 'C' ? (lastCode == 47 ? (ColognePhonetic.arrayContains(AHKLOQRUX, nextChar) ? 52 : 56) : (ColognePhonetic.arrayContains(SZ, lastChar) || !ColognePhonetic.arrayContains(AHOUKQX, nextChar) ? 56 : 52)) : (ColognePhonetic.arrayContains(TDX, chr) ? 56 : (chr == 'R' ? 55 : (chr == 'L' ? 53 : (chr == 'M' || chr == 'N' ? 54 : (int)chr)))));
            }
            if (code != 45 && (lastCode != code && (code != 48 || lastCode == 47) || code < 48 || code > 56)) {
                output.addRight((char)code);
            }
            lastChar = chr;
            lastCode = code;
        }
        return output.toString();
    }

    @Override
    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
        }
        return this.encode((String)object);
    }

    @Override
    public String encode(String text) {
        return this.colognePhonetic(text);
    }

    public boolean isEncodeEqual(String text1, String text2) {
        return this.colognePhonetic(text1).equals(this.colognePhonetic(text2));
    }

    private String preprocess(String text) {
        text = text.toUpperCase(Locale.GERMAN);
        char[] chrs = text.toCharArray();
        block0: for (int index = 0; index < chrs.length; ++index) {
            if (chrs[index] <= 'Z') continue;
            for (char[] element : PREPROCESS_MAP) {
                if (chrs[index] != element[0]) continue;
                chrs[index] = element[1];
                continue block0;
            }
        }
        return new String(chrs);
    }

    private class CologneInputBuffer
    extends CologneBuffer {
        public CologneInputBuffer(char[] data) {
            super(data);
        }

        public void addLeft(char ch) {
            ++this.length;
            this.data[this.getNextPos()] = ch;
        }

        @Override
        protected char[] copyData(int start, int length) {
            char[] newData = new char[length];
            System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
            return newData;
        }

        public char getNextChar() {
            return this.data[this.getNextPos()];
        }

        protected int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            char ch = this.getNextChar();
            --this.length;
            return ch;
        }
    }

    private class CologneOutputBuffer
    extends CologneBuffer {
        public CologneOutputBuffer(int buffSize) {
            super(buffSize);
        }

        public void addRight(char chr) {
            this.data[this.length] = chr;
            ++this.length;
        }

        @Override
        protected char[] copyData(int start, int length) {
            char[] newData = new char[length];
            System.arraycopy(this.data, start, newData, 0, length);
            return newData;
        }
    }

    private abstract class CologneBuffer {
        protected final char[] data;
        protected int length = 0;

        public CologneBuffer(char[] data) {
            this.data = data;
            this.length = data.length;
        }

        public CologneBuffer(int buffSize) {
            this.data = new char[buffSize];
            this.length = 0;
        }

        protected abstract char[] copyData(int var1, int var2);

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(this.copyData(0, this.length));
        }
    }
}

