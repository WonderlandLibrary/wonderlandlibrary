/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.DictionaryBreakEngine;
import com.ibm.icu.text.DictionaryData;
import com.ibm.icu.text.DictionaryMatcher;
import com.ibm.icu.text.UnicodeSet;
import java.io.IOException;
import java.text.CharacterIterator;

class BurmeseBreakEngine
extends DictionaryBreakEngine {
    private static final byte BURMESE_LOOKAHEAD = 3;
    private static final byte BURMESE_ROOT_COMBINE_THRESHOLD = 3;
    private static final byte BURMESE_PREFIX_COMBINE_THRESHOLD = 3;
    private static final byte BURMESE_MIN_WORD = 2;
    private DictionaryMatcher fDictionary;
    private static UnicodeSet fBurmeseWordSet = new UnicodeSet();
    private static UnicodeSet fEndWordSet;
    private static UnicodeSet fBeginWordSet;
    private static UnicodeSet fMarkSet;

    public BurmeseBreakEngine() throws IOException {
        this.setCharacters(fBurmeseWordSet);
        this.fDictionary = DictionaryData.loadDictionaryFor("Mymr");
    }

    public boolean equals(Object obj) {
        return obj instanceof BurmeseBreakEngine;
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean handles(int c2) {
        int script = UCharacter.getIntPropertyValue(c2, 4106);
        return script == 28;
    }

    @Override
    public int divideUpDictionaryRange(CharacterIterator fIter, int rangeStart, int rangeEnd, DictionaryBreakEngine.DequeI foundBreaks) {
        int current;
        if (rangeEnd - rangeStart < 2) {
            return 0;
        }
        int wordsFound = 0;
        DictionaryBreakEngine.PossibleWord[] words = new DictionaryBreakEngine.PossibleWord[3];
        for (int i2 = 0; i2 < 3; ++i2) {
            words[i2] = new DictionaryBreakEngine.PossibleWord();
        }
        fIter.setIndex(rangeStart);
        while ((current = fIter.getIndex()) < rangeEnd) {
            int currPos;
            int wordLength = 0;
            int candidates = words[wordsFound % 3].candidates(fIter, this.fDictionary, rangeEnd);
            if (candidates == 1) {
                wordLength = words[wordsFound % 3].acceptMarked(fIter);
                ++wordsFound;
            } else if (candidates > 1) {
                boolean foundBest = false;
                if (fIter.getIndex() < rangeEnd) {
                    block2: do {
                        int wordsMatched = 1;
                        if (words[(wordsFound + 1) % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0) continue;
                        if (wordsMatched < 2) {
                            words[wordsFound % 3].markCurrent();
                            wordsMatched = 2;
                        }
                        if (fIter.getIndex() >= rangeEnd) break;
                        do {
                            if (words[(wordsFound + 2) % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0) continue;
                            words[wordsFound % 3].markCurrent();
                            foundBest = true;
                            continue block2;
                        } while (words[(wordsFound + 1) % 3].backUp(fIter));
                    } while (words[wordsFound % 3].backUp(fIter) && !foundBest);
                }
                wordLength = words[wordsFound % 3].acceptMarked(fIter);
                ++wordsFound;
            }
            if (fIter.getIndex() < rangeEnd && wordLength < 3) {
                if (words[wordsFound % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0 && (wordLength == 0 || words[wordsFound % 3].longestPrefix() < 3)) {
                    int remaining = rangeEnd - (current + wordLength);
                    char pc = fIter.current();
                    int chars = 0;
                    while (true) {
                        fIter.next();
                        char uc = fIter.current();
                        ++chars;
                        if (--remaining <= 0) break;
                        if (fEndWordSet.contains(pc) && fBeginWordSet.contains(uc)) {
                            int candidate = words[(wordsFound + 1) % 3].candidates(fIter, this.fDictionary, rangeEnd);
                            fIter.setIndex(current + wordLength + chars);
                            if (candidate > 0) break;
                        }
                        pc = uc;
                    }
                    if (wordLength <= 0) {
                        ++wordsFound;
                    }
                    wordLength += chars;
                } else {
                    fIter.setIndex(current + wordLength);
                }
            }
            while ((currPos = fIter.getIndex()) < rangeEnd && fMarkSet.contains(fIter.current())) {
                fIter.next();
                wordLength += fIter.getIndex() - currPos;
            }
            if (wordLength <= 0) continue;
            foundBreaks.push(current + wordLength);
        }
        if (foundBreaks.peek() >= rangeEnd) {
            foundBreaks.pop();
            --wordsFound;
        }
        return wordsFound;
    }

    static {
        fMarkSet = new UnicodeSet();
        fBeginWordSet = new UnicodeSet();
        fBurmeseWordSet.applyPattern("[[:Mymr:]&[:LineBreak=SA:]]");
        fBurmeseWordSet.compact();
        fMarkSet.applyPattern("[[:Mymr:]&[:LineBreak=SA:]&[:M:]]");
        fMarkSet.add(32);
        fEndWordSet = new UnicodeSet(fBurmeseWordSet);
        fBeginWordSet.add(4096, 4138);
        fMarkSet.compact();
        fEndWordSet.compact();
        fBeginWordSet.compact();
        fBurmeseWordSet.freeze();
        fMarkSet.freeze();
        fEndWordSet.freeze();
        fBeginWordSet.freeze();
    }
}

