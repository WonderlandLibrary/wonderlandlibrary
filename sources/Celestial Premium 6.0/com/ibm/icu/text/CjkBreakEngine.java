/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.text.DictionaryData;
import com.ibm.icu.text.DictionaryMatcher;
import com.ibm.icu.text.LanguageBreakEngine;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.UnicodeSet;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class CjkBreakEngine
implements LanguageBreakEngine {
    private static final UnicodeSet fHangulWordSet = new UnicodeSet();
    private static final UnicodeSet fHanWordSet = new UnicodeSet();
    private static final UnicodeSet fKatakanaWordSet = new UnicodeSet();
    private static final UnicodeSet fHiraganaWordSet = new UnicodeSet();
    private final UnicodeSet fWordSet;
    private DictionaryMatcher fDictionary = DictionaryData.loadDictionaryFor("Hira");
    private static final int kMaxKatakanaLength = 8;
    private static final int kMaxKatakanaGroupLength = 20;
    private static final int maxSnlp = 255;
    private static final int kint32max = Integer.MAX_VALUE;

    public CjkBreakEngine(boolean korean) throws IOException {
        if (korean) {
            this.fWordSet = fHangulWordSet;
        } else {
            this.fWordSet = new UnicodeSet();
            this.fWordSet.addAll(fHanWordSet);
            this.fWordSet.addAll(fKatakanaWordSet);
            this.fWordSet.addAll(fHiraganaWordSet);
            this.fWordSet.add("\\uff70\\u30fc");
        }
    }

    @Override
    public boolean handles(int c, int breakType) {
        return breakType == 1 && this.fWordSet.contains(c);
    }

    private static int getKatakanaCost(int wordlength) {
        int[] katakanaCost = new int[]{8192, 984, 408, 240, 204, 252, 300, 372, 480};
        return wordlength > 8 ? 8192 : katakanaCost[wordlength];
    }

    private static boolean isKatakana(int value) {
        return value >= 12449 && value <= 12542 && value != 12539 || value >= 65382 && value <= 65439;
    }

    @Override
    public int findBreaks(CharacterIterator inText, int startPos, int endPos, boolean reverse, int breakType, Stack<Integer> foundBreaks) {
        if (startPos >= endPos) {
            return 0;
        }
        inText.setIndex(startPos);
        int inputLength = endPos - startPos;
        int[] charPositions = new int[inputLength + 1];
        StringBuffer s = new StringBuffer("");
        inText.setIndex(startPos);
        while (inText.getIndex() < endPos) {
            s.append(inText.current());
            inText.next();
        }
        String prenormstr = s.toString();
        boolean isNormalized = Normalizer.quickCheck(prenormstr, Normalizer.NFKC) == Normalizer.YES || Normalizer.isNormalized(prenormstr, Normalizer.NFKC, 0);
        CharacterIterator text = inText;
        int numChars = 0;
        if (isNormalized) {
            int index = 0;
            charPositions[0] = 0;
            while (index < prenormstr.length()) {
                int codepoint = prenormstr.codePointAt(index);
                charPositions[++numChars] = index += Character.charCount(codepoint);
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
                charPositions[++numChars] = index = normalizer.getIndex();
            }
        }
        int[] bestSnlp = new int[numChars + 1];
        bestSnlp[0] = 0;
        for (int i = 1; i <= numChars; ++i) {
            bestSnlp[i] = Integer.MAX_VALUE;
        }
        int[] prev = new int[numChars + 1];
        for (int i = 0; i <= numChars; ++i) {
            prev[i] = -1;
        }
        int maxWordSize = 20;
        int[] values = new int[numChars];
        int[] lengths = new int[numChars];
        boolean is_prev_katakana = false;
        for (int i = 0; i < numChars; ++i) {
            text.setIndex(i);
            if (bestSnlp[i] == Integer.MAX_VALUE) continue;
            int maxSearchLength = i + 20 < numChars ? 20 : numChars - i;
            int[] count_ = new int[1];
            this.fDictionary.matches(text, maxSearchLength, lengths, count_, maxSearchLength, values);
            int count = count_[0];
            if (!(count != 0 && lengths[0] == 1 || CharacterIteration.current32(text) == Integer.MAX_VALUE || fHangulWordSet.contains(CharacterIteration.current32(text)))) {
                values[count] = 255;
                lengths[count] = 1;
                ++count;
            }
            for (int j = 0; j < count; ++j) {
                int newSnlp = bestSnlp[i] + values[j];
                if (newSnlp >= bestSnlp[lengths[j] + i]) continue;
                bestSnlp[lengths[j] + i] = newSnlp;
                prev[lengths[j] + i] = i;
            }
            text.setIndex(i);
            boolean is_katakana = CjkBreakEngine.isKatakana(CharacterIteration.current32(text));
            if (!is_prev_katakana && is_katakana) {
                int newSnlp;
                int j;
                CharacterIteration.next32(text);
                for (j = i + 1; j < numChars && j - i < 20 && CjkBreakEngine.isKatakana(CharacterIteration.current32(text)); ++j) {
                    CharacterIteration.next32(text);
                }
                if (j - i < 20 && (newSnlp = bestSnlp[i] + CjkBreakEngine.getKatakanaCost(j - i)) < bestSnlp[j]) {
                    bestSnlp[j] = newSnlp;
                    prev[j] = i;
                }
            }
            is_prev_katakana = is_katakana;
        }
        int[] t_boundary = new int[numChars + 1];
        int numBreaks = 0;
        if (bestSnlp[numChars] == Integer.MAX_VALUE) {
            t_boundary[numBreaks] = numChars;
            ++numBreaks;
        } else {
            int i = numChars;
            while (i > 0) {
                t_boundary[numBreaks] = i;
                ++numBreaks;
                i = prev[i];
            }
            Assert.assrt(prev[t_boundary[numBreaks - 1]] == 0);
        }
        if (foundBreaks.size() == 0 || foundBreaks.peek() < startPos) {
            t_boundary[numBreaks++] = 0;
        }
        for (int i = numBreaks - 1; i >= 0; --i) {
            int pos = charPositions[t_boundary[i]] + startPos;
            if (foundBreaks.contains(pos) || pos == startPos) continue;
            foundBreaks.push(charPositions[t_boundary[i]] + startPos);
        }
        if (!foundBreaks.empty() && foundBreaks.peek() == endPos) {
            foundBreaks.pop();
        }
        if (!foundBreaks.empty()) {
            inText.setIndex(foundBreaks.peek());
        }
        return 0;
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

