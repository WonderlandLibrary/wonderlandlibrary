/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.text.DictionaryBreakEngine;
import com.ibm.icu.text.DictionaryData;
import com.ibm.icu.text.DictionaryMatcher;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.UnicodeSet;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

class CjkBreakEngine
extends DictionaryBreakEngine {
    private static final UnicodeSet fHangulWordSet = new UnicodeSet();
    private static final UnicodeSet fHanWordSet = new UnicodeSet();
    private static final UnicodeSet fKatakanaWordSet = new UnicodeSet();
    private static final UnicodeSet fHiraganaWordSet = new UnicodeSet();
    private DictionaryMatcher fDictionary = DictionaryData.loadDictionaryFor("Hira");
    private static final int kMaxKatakanaLength = 8;
    private static final int kMaxKatakanaGroupLength = 20;
    private static final int maxSnlp = 255;
    private static final int kint32max = Integer.MAX_VALUE;

    public CjkBreakEngine(boolean korean) throws IOException {
        if (korean) {
            this.setCharacters(fHangulWordSet);
        } else {
            UnicodeSet cjSet = new UnicodeSet();
            cjSet.addAll(fHanWordSet);
            cjSet.addAll(fKatakanaWordSet);
            cjSet.addAll(fHiraganaWordSet);
            cjSet.add(65392);
            cjSet.add(12540);
            this.setCharacters(cjSet);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof CjkBreakEngine) {
            CjkBreakEngine other = (CjkBreakEngine)obj;
            return this.fSet.equals(other.fSet);
        }
        return false;
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }

    private static int getKatakanaCost(int wordlength) {
        int[] katakanaCost = new int[]{8192, 984, 408, 240, 204, 252, 300, 372, 480};
        return wordlength > 8 ? 8192 : katakanaCost[wordlength];
    }

    private static boolean isKatakana(int value) {
        return value >= 12449 && value <= 12542 && value != 12539 || value >= 65382 && value <= 65439;
    }

    @Override
    public int divideUpDictionaryRange(CharacterIterator inText, int startPos, int endPos, DictionaryBreakEngine.DequeI foundBreaks) {
        StringCharacterIterator text;
        if (startPos >= endPos) {
            return 0;
        }
        inText.setIndex(startPos);
        int inputLength = endPos - startPos;
        int[] charPositions = new int[inputLength + 1];
        StringBuffer s2 = new StringBuffer("");
        inText.setIndex(startPos);
        while (inText.getIndex() < endPos) {
            s2.append(inText.current());
            inText.next();
        }
        String prenormstr = s2.toString();
        boolean isNormalized = Normalizer.quickCheck(prenormstr, Normalizer.NFKC) == Normalizer.YES || Normalizer.isNormalized(prenormstr, Normalizer.NFKC, 0);
        int numCodePts = 0;
        if (isNormalized) {
            text = new StringCharacterIterator(prenormstr);
            int index = 0;
            charPositions[0] = 0;
            while (index < prenormstr.length()) {
                int codepoint = prenormstr.codePointAt(index);
                charPositions[++numCodePts] = index += Character.charCount(codepoint);
            }
        } else {
            String normStr = Normalizer.normalize(prenormstr, Normalizer.NFKC);
            text = new StringCharacterIterator(normStr);
            charPositions = new int[normStr.length() + 1];
            Normalizer normalizer = new Normalizer(prenormstr, Normalizer.NFKC, 0);
            int index = 0;
            charPositions[0] = 0;
            while (index < normalizer.endIndex()) {
                normalizer.next();
                charPositions[++numCodePts] = index = normalizer.getIndex();
            }
        }
        int[] bestSnlp = new int[numCodePts + 1];
        bestSnlp[0] = 0;
        for (int i2 = 1; i2 <= numCodePts; ++i2) {
            bestSnlp[i2] = Integer.MAX_VALUE;
        }
        int[] prev = new int[numCodePts + 1];
        for (int i3 = 0; i3 <= numCodePts; ++i3) {
            prev[i3] = -1;
        }
        int maxWordSize = 20;
        int[] values = new int[numCodePts];
        int[] lengths = new int[numCodePts];
        int ix = 0;
        text.setIndex(ix);
        boolean is_prev_katakana = false;
        for (int i4 = 0; i4 < numCodePts; ++i4) {
            ix = text.getIndex();
            if (bestSnlp[i4] != Integer.MAX_VALUE) {
                int maxSearchLength = i4 + 20 < numCodePts ? 20 : numCodePts - i4;
                int[] count_ = new int[1];
                this.fDictionary.matches(text, maxSearchLength, lengths, count_, maxSearchLength, values);
                int count = count_[0];
                text.setIndex(ix);
                if (!(count != 0 && lengths[0] == 1 || CharacterIteration.current32(text) == Integer.MAX_VALUE || fHangulWordSet.contains(CharacterIteration.current32(text)))) {
                    values[count] = 255;
                    lengths[count] = 1;
                    ++count;
                }
                for (int j2 = 0; j2 < count; ++j2) {
                    int newSnlp = bestSnlp[i4] + values[j2];
                    if (newSnlp >= bestSnlp[lengths[j2] + i4]) continue;
                    bestSnlp[lengths[j2] + i4] = newSnlp;
                    prev[lengths[j2] + i4] = i4;
                }
                boolean is_katakana = CjkBreakEngine.isKatakana(CharacterIteration.current32(text));
                if (!is_prev_katakana && is_katakana) {
                    int newSnlp;
                    int j3;
                    CharacterIteration.next32(text);
                    for (j3 = i4 + 1; j3 < numCodePts && j3 - i4 < 20 && CjkBreakEngine.isKatakana(CharacterIteration.current32(text)); ++j3) {
                        CharacterIteration.next32(text);
                    }
                    if (j3 - i4 < 20 && (newSnlp = bestSnlp[i4] + CjkBreakEngine.getKatakanaCost(j3 - i4)) < bestSnlp[j3]) {
                        bestSnlp[j3] = newSnlp;
                        prev[j3] = i4;
                    }
                }
                is_prev_katakana = is_katakana;
            }
            text.setIndex(ix);
            CharacterIteration.next32(text);
        }
        int[] t_boundary = new int[numCodePts + 1];
        int numBreaks = 0;
        if (bestSnlp[numCodePts] == Integer.MAX_VALUE) {
            t_boundary[numBreaks] = numCodePts;
            ++numBreaks;
        } else {
            int i5 = numCodePts;
            while (i5 > 0) {
                t_boundary[numBreaks] = i5;
                ++numBreaks;
                i5 = prev[i5];
            }
            Assert.assrt(prev[t_boundary[numBreaks - 1]] == 0);
        }
        if (foundBreaks.size() == 0 || foundBreaks.peek() < startPos) {
            t_boundary[numBreaks++] = 0;
        }
        int correctedNumBreaks = 0;
        for (int i6 = numBreaks - 1; i6 >= 0; --i6) {
            int pos = charPositions[t_boundary[i6]] + startPos;
            if (foundBreaks.contains(pos) || pos == startPos) continue;
            foundBreaks.push(charPositions[t_boundary[i6]] + startPos);
            ++correctedNumBreaks;
        }
        if (!foundBreaks.isEmpty() && foundBreaks.peek() == endPos) {
            foundBreaks.pop();
            --correctedNumBreaks;
        }
        if (!foundBreaks.isEmpty()) {
            inText.setIndex(foundBreaks.peek());
        }
        return correctedNumBreaks;
    }

    static {
        fHangulWordSet.applyPattern("[\\uac00-\\ud7a3]");
        fHanWordSet.applyPattern("[:Han:]");
        fKatakanaWordSet.applyPattern("[[:Katakana:]\\uff9e\\uff9f]");
        fHiraganaWordSet.applyPattern("[:Hiragana:]");
        fHangulWordSet.freeze();
        fHanWordSet.freeze();
        fKatakanaWordSet.freeze();
        fHiraganaWordSet.freeze();
    }
}

