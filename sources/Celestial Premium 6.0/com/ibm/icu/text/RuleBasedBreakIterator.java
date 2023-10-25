/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharTrie;
import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.CjkBreakEngine;
import com.ibm.icu.text.LanguageBreakEngine;
import com.ibm.icu.text.RBBIDataWrapper;
import com.ibm.icu.text.RBBIRuleBuilder;
import com.ibm.icu.text.ThaiBreakEngine;
import com.ibm.icu.text.UnhandledBreakEngine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class RuleBasedBreakIterator
extends BreakIterator {
    public static final int WORD_NONE = 0;
    public static final int WORD_NONE_LIMIT = 100;
    public static final int WORD_NUMBER = 100;
    public static final int WORD_NUMBER_LIMIT = 200;
    public static final int WORD_LETTER = 200;
    public static final int WORD_LETTER_LIMIT = 300;
    public static final int WORD_KANA = 300;
    public static final int WORD_KANA_LIMIT = 400;
    public static final int WORD_IDEO = 400;
    public static final int WORD_IDEO_LIMIT = 500;
    private static final int START_STATE = 1;
    private static final int STOP_STATE = 0;
    private static final int RBBI_START = 0;
    private static final int RBBI_RUN = 1;
    private static final int RBBI_END = 2;
    private CharacterIterator fText = new StringCharacterIterator("");
    RBBIDataWrapper fRData;
    private int fLastRuleStatusIndex;
    private boolean fLastStatusIndexValid = true;
    private int fDictionaryCharCount = 0;
    private static final String RBBI_DEBUG_ARG = "rbbi";
    private static final boolean TRACE = ICUDebug.enabled("rbbi") && ICUDebug.value("rbbi").indexOf("trace") >= 0;
    private int fBreakType = 2;
    private final UnhandledBreakEngine fUnhandledBreakEngine = new UnhandledBreakEngine();
    private int[] fCachedBreakPositions;
    private int fPositionInCache;
    private boolean fUseDictionary = true;
    private final Set<LanguageBreakEngine> fBreakEngines = Collections.synchronizedSet(new HashSet());
    static final String fDebugEnv = ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null;

    private RuleBasedBreakIterator() {
        this.fBreakEngines.add(this.fUnhandledBreakEngine);
    }

    public static RuleBasedBreakIterator getInstanceFromCompiledRules(InputStream is) throws IOException {
        RuleBasedBreakIterator This = new RuleBasedBreakIterator();
        This.fRData = RBBIDataWrapper.get(is);
        return This;
    }

    public RuleBasedBreakIterator(String rules) {
        this();
        try {
            ByteArrayOutputStream ruleOS = new ByteArrayOutputStream();
            RuleBasedBreakIterator.compileRules(rules, ruleOS);
            byte[] ruleBA = ruleOS.toByteArray();
            ByteArrayInputStream ruleIS = new ByteArrayInputStream(ruleBA);
            this.fRData = RBBIDataWrapper.get(ruleIS);
        }
        catch (IOException e) {
            RuntimeException rte = new RuntimeException("RuleBasedBreakIterator rule compilation internal error: " + e.getMessage());
            throw rte;
        }
    }

    public Object clone() {
        RuleBasedBreakIterator result = (RuleBasedBreakIterator)super.clone();
        if (this.fText != null) {
            result.fText = (CharacterIterator)this.fText.clone();
        }
        return result;
    }

    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        try {
            RuleBasedBreakIterator other = (RuleBasedBreakIterator)that;
            if (this.fRData != other.fRData && (this.fRData == null || other.fRData == null)) {
                return false;
            }
            if (this.fRData != null && other.fRData != null && !this.fRData.fRuleSource.equals(other.fRData.fRuleSource)) {
                return false;
            }
            if (this.fText == null && other.fText == null) {
                return true;
            }
            if (this.fText == null || other.fText == null) {
                return false;
            }
            return this.fText.equals(other.fText);
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        String retStr = "";
        if (this.fRData != null) {
            retStr = this.fRData.fRuleSource;
        }
        return retStr;
    }

    public int hashCode() {
        return this.fRData.fRuleSource.hashCode();
    }

    public void dump() {
        this.fRData.dump();
    }

    public static void compileRules(String rules, OutputStream ruleBinary) throws IOException {
        RBBIRuleBuilder.compileRules(rules, ruleBinary);
    }

    public int first() {
        this.fCachedBreakPositions = null;
        this.fDictionaryCharCount = 0;
        this.fPositionInCache = 0;
        this.fLastRuleStatusIndex = 0;
        this.fLastStatusIndexValid = true;
        if (this.fText == null) {
            return -1;
        }
        this.fText.first();
        return this.fText.getIndex();
    }

    public int last() {
        this.fCachedBreakPositions = null;
        this.fDictionaryCharCount = 0;
        this.fPositionInCache = 0;
        if (this.fText == null) {
            this.fLastRuleStatusIndex = 0;
            this.fLastStatusIndexValid = true;
            return -1;
        }
        this.fLastStatusIndexValid = false;
        int pos = this.fText.getEndIndex();
        this.fText.setIndex(pos);
        return pos;
    }

    public int next(int n) {
        int result = this.current();
        while (n > 0) {
            result = this.handleNext();
            --n;
        }
        while (n < 0) {
            result = this.previous();
            ++n;
        }
        return result;
    }

    public int next() {
        return this.handleNext();
    }

    public int previous() {
        int nextResult;
        CharacterIterator text = this.getText();
        this.fLastStatusIndexValid = false;
        if (this.fCachedBreakPositions != null && this.fPositionInCache > 0) {
            --this.fPositionInCache;
            text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
            return this.fCachedBreakPositions[this.fPositionInCache];
        }
        this.fCachedBreakPositions = null;
        int offset = this.current();
        int result = this.rulesPrevious();
        if (result == -1) {
            return result;
        }
        if (this.fDictionaryCharCount == 0) {
            return result;
        }
        if (this.fCachedBreakPositions != null) {
            this.fPositionInCache = this.fCachedBreakPositions.length - 2;
            return result;
        }
        while (result < offset && (nextResult = this.handleNext()) < offset) {
            result = nextResult;
        }
        if (this.fCachedBreakPositions != null) {
            this.fPositionInCache = 0;
            while (this.fPositionInCache < this.fCachedBreakPositions.length) {
                if (this.fCachedBreakPositions[this.fPositionInCache] >= offset) {
                    --this.fPositionInCache;
                    break;
                }
                ++this.fPositionInCache;
            }
        }
        this.fLastStatusIndexValid = false;
        text.setIndex(result);
        return result;
    }

    private int rulesPrevious() {
        if (this.fText == null || this.current() == this.fText.getBeginIndex()) {
            this.fLastRuleStatusIndex = 0;
            this.fLastStatusIndexValid = true;
            return -1;
        }
        if (this.fRData.fSRTable != null || this.fRData.fSFTable != null) {
            return this.handlePrevious(this.fRData.fRTable);
        }
        int start = this.current();
        CharacterIteration.previous32(this.fText);
        int lastResult = this.handlePrevious(this.fRData.fRTable);
        if (lastResult == -1) {
            lastResult = this.fText.getBeginIndex();
            this.fText.setIndex(lastResult);
        }
        int result = lastResult;
        int lastTag = 0;
        boolean breakTagValid = false;
        while ((result = this.handleNext()) != -1 && result < start) {
            lastResult = result;
            lastTag = this.fLastRuleStatusIndex;
            breakTagValid = true;
        }
        this.fText.setIndex(lastResult);
        this.fLastRuleStatusIndex = lastTag;
        this.fLastStatusIndexValid = breakTagValid;
        return lastResult;
    }

    public int following(int offset) {
        CharacterIterator text = this.getText();
        if (this.fCachedBreakPositions == null || offset < this.fCachedBreakPositions[0] || offset >= this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
            this.fCachedBreakPositions = null;
            return this.rulesFollowing(offset);
        }
        this.fPositionInCache = 0;
        while (this.fPositionInCache < this.fCachedBreakPositions.length && offset >= this.fCachedBreakPositions[this.fPositionInCache]) {
            ++this.fPositionInCache;
        }
        text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
        return text.getIndex();
    }

    private int rulesFollowing(int offset) {
        this.fLastRuleStatusIndex = 0;
        this.fLastStatusIndexValid = true;
        if (this.fText == null || offset >= this.fText.getEndIndex()) {
            this.last();
            return this.next();
        }
        if (offset < this.fText.getBeginIndex()) {
            return this.first();
        }
        int result = 0;
        if (this.fRData.fSRTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.next32(this.fText);
            this.handlePrevious(this.fRData.fSRTable);
            result = this.next();
            while (result <= offset) {
                result = this.next();
            }
            return result;
        }
        if (this.fRData.fSFTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.previous32(this.fText);
            this.handleNext(this.fRData.fSFTable);
            int oldresult = this.previous();
            while (oldresult > offset) {
                result = this.previous();
                if (result <= offset) {
                    return oldresult;
                }
                oldresult = result;
            }
            result = this.next();
            if (result <= offset) {
                return this.next();
            }
            return result;
        }
        this.fText.setIndex(offset);
        if (offset == this.fText.getBeginIndex()) {
            return this.handleNext();
        }
        result = this.previous();
        while (result != -1 && result <= offset) {
            result = this.next();
        }
        return result;
    }

    public int preceding(int offset) {
        CharacterIterator text = this.getText();
        if (this.fCachedBreakPositions == null || offset <= this.fCachedBreakPositions[0] || offset > this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
            this.fCachedBreakPositions = null;
            return this.rulesPreceding(offset);
        }
        this.fPositionInCache = 0;
        while (this.fPositionInCache < this.fCachedBreakPositions.length && offset > this.fCachedBreakPositions[this.fPositionInCache]) {
            ++this.fPositionInCache;
        }
        --this.fPositionInCache;
        text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
        return text.getIndex();
    }

    private int rulesPreceding(int offset) {
        if (this.fText == null || offset > this.fText.getEndIndex()) {
            return this.last();
        }
        if (offset < this.fText.getBeginIndex()) {
            return this.first();
        }
        if (this.fRData.fSFTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.previous32(this.fText);
            this.handleNext(this.fRData.fSFTable);
            int result = this.previous();
            while (result >= offset) {
                result = this.previous();
            }
            return result;
        }
        if (this.fRData.fSRTable != null) {
            int result;
            this.fText.setIndex(offset);
            CharacterIteration.next32(this.fText);
            this.handlePrevious(this.fRData.fSRTable);
            int oldresult = this.next();
            while (oldresult < offset) {
                result = this.next();
                if (result >= offset) {
                    return oldresult;
                }
                oldresult = result;
            }
            result = this.previous();
            if (result >= offset) {
                return this.previous();
            }
            return result;
        }
        this.fText.setIndex(offset);
        return this.previous();
    }

    protected static final void checkOffset(int offset, CharacterIterator text) {
        if (offset < text.getBeginIndex() || offset > text.getEndIndex()) {
            throw new IllegalArgumentException("offset out of bounds");
        }
    }

    public boolean isBoundary(int offset) {
        RuleBasedBreakIterator.checkOffset(offset, this.fText);
        if (offset == this.fText.getBeginIndex()) {
            this.first();
            return true;
        }
        if (offset == this.fText.getEndIndex()) {
            this.last();
            return true;
        }
        this.fText.setIndex(offset);
        CharacterIteration.previous32(this.fText);
        int pos = this.fText.getIndex();
        boolean result = this.following(pos) == offset;
        return result;
    }

    public int current() {
        return this.fText != null ? this.fText.getIndex() : -1;
    }

    private void makeRuleStatusValid() {
        if (!this.fLastStatusIndexValid) {
            int curr = this.current();
            if (curr == -1 || curr == this.fText.getBeginIndex()) {
                this.fLastRuleStatusIndex = 0;
                this.fLastStatusIndexValid = true;
            } else {
                int pa = this.fText.getIndex();
                this.first();
                int pb = this.current();
                while (this.fText.getIndex() < pa) {
                    pb = this.next();
                }
                Assert.assrt(pa == pb);
            }
            Assert.assrt(this.fLastStatusIndexValid);
            Assert.assrt(this.fLastRuleStatusIndex >= 0 && this.fLastRuleStatusIndex < this.fRData.fStatusTable.length);
        }
    }

    public int getRuleStatus() {
        this.makeRuleStatusValid();
        int idx = this.fLastRuleStatusIndex + this.fRData.fStatusTable[this.fLastRuleStatusIndex];
        int tagVal = this.fRData.fStatusTable[idx];
        return tagVal;
    }

    public int getRuleStatusVec(int[] fillInArray) {
        this.makeRuleStatusValid();
        int numStatusVals = this.fRData.fStatusTable[this.fLastRuleStatusIndex];
        if (fillInArray != null) {
            int numToCopy = Math.min(numStatusVals, fillInArray.length);
            for (int i = 0; i < numToCopy; ++i) {
                fillInArray[i] = this.fRData.fStatusTable[this.fLastRuleStatusIndex + i + 1];
            }
        }
        return numStatusVals;
    }

    public CharacterIterator getText() {
        return this.fText;
    }

    public void setText(CharacterIterator newText) {
        this.fText = newText;
        int firstIdx = this.first();
        if (newText != null) {
            this.fUseDictionary = (this.fBreakType == 1 || this.fBreakType == 2) && newText.getEndIndex() != firstIdx;
        }
    }

    void setBreakType(int type) {
        this.fBreakType = type;
        if (type != 1 && type != 2) {
            this.fUseDictionary = false;
        }
    }

    int getBreakType() {
        return this.fBreakType;
    }

    private LanguageBreakEngine getEngineFor(int c) {
        if (c == Integer.MAX_VALUE || !this.fUseDictionary) {
            return null;
        }
        for (LanguageBreakEngine candidate : this.fBreakEngines) {
            if (!candidate.handles(c, this.fBreakType)) continue;
            return candidate;
        }
        int script = UCharacter.getIntPropertyValue(c, 4106);
        LanguageBreakEngine eng = null;
        try {
            switch (script) {
                case 38: {
                    eng = new ThaiBreakEngine();
                    break;
                }
                case 17: 
                case 20: 
                case 22: {
                    if (this.getBreakType() == 1) {
                        eng = new CjkBreakEngine(false);
                        break;
                    }
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
                case 18: {
                    if (this.getBreakType() == 1) {
                        eng = new CjkBreakEngine(true);
                        break;
                    }
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
                default: {
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
            }
        }
        catch (IOException e) {
            eng = null;
        }
        if (eng != null) {
            this.fBreakEngines.add(eng);
        }
        return eng;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int handleNext() {
        if (this.fCachedBreakPositions == null || this.fPositionInCache == this.fCachedBreakPositions.length - 1) {
            int startPos = this.fText.getIndex();
            this.fDictionaryCharCount = 0;
            int result = this.handleNext(this.fRData.fFTable);
            if (this.fDictionaryCharCount > 1 && result - startPos > 1) {
                this.fText.setIndex(startPos);
                LanguageBreakEngine e = this.getEngineFor(CharacterIteration.current32(this.fText));
                if (e == null) {
                    this.fText.setIndex(result);
                    return result;
                }
                Stack<Integer> breaks = new Stack<Integer>();
                e.findBreaks(this.fText, startPos, result, false, this.getBreakType(), breaks);
                int breaksSize = breaks.size();
                this.fCachedBreakPositions = new int[breaksSize + 2];
                this.fCachedBreakPositions[0] = startPos;
                for (int i = 0; i < breaksSize; ++i) {
                    this.fCachedBreakPositions[i + 1] = (Integer)breaks.elementAt(i);
                }
                this.fCachedBreakPositions[breaksSize + 1] = result;
                this.fPositionInCache = 0;
            } else {
                this.fCachedBreakPositions = null;
                return result;
            }
        }
        if (this.fCachedBreakPositions != null) {
            ++this.fPositionInCache;
            this.fText.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
            return this.fCachedBreakPositions[this.fPositionInCache];
        }
        Assert.assrt(false);
        return -1;
    }

    private int handleNext(short[] stateTable) {
        int initialPosition;
        if (TRACE) {
            System.out.println("Handle Next   pos      char  state category");
        }
        this.fLastStatusIndexValid = true;
        this.fLastRuleStatusIndex = 0;
        CharacterIterator text = this.fText;
        CharTrie trie = this.fRData.fTrie;
        int c = text.current();
        if (c >= 55296 && (c = CharacterIteration.nextTrail32(text, c)) == Integer.MAX_VALUE) {
            return -1;
        }
        int result = initialPosition = text.getIndex();
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        int category = 3;
        short flagsState = stateTable[5];
        int mode = 1;
        if ((flagsState & 2) != 0) {
            category = 2;
            mode = 0;
            if (TRACE) {
                System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                System.out.print(RBBIDataWrapper.intToHexString(c, 10));
                System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
            }
        }
        short lookaheadStatus = 0;
        int lookaheadTagIdx = 0;
        int lookaheadResult = 0;
        while (state != 0) {
            if (c == Integer.MAX_VALUE) {
                if (mode == 2) {
                    if (lookaheadResult <= result) break;
                    result = lookaheadResult;
                    this.fLastRuleStatusIndex = lookaheadTagIdx;
                    break;
                }
                mode = 2;
                category = 1;
            } else if (mode == 1) {
                category = (short)trie.getCodePointValue(c);
                if ((category & 0x4000) != 0) {
                    ++this.fDictionaryCharCount;
                    category = (short)(category & 0xFFFFBFFF);
                }
                if (TRACE) {
                    System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                    System.out.print(RBBIDataWrapper.intToHexString(c, 10));
                    System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
                }
                if ((c = (int)text.next()) >= 55296) {
                    c = CharacterIteration.nextTrail32(text, c);
                }
            } else {
                mode = 1;
            }
            state = stateTable[row + 4 + category];
            row = this.fRData.getRowIndex(state);
            if (stateTable[row + 0] == -1) {
                result = text.getIndex();
                if (c >= 65536 && c <= 0x10FFFF) {
                    --result;
                }
                this.fLastRuleStatusIndex = stateTable[row + 2];
            }
            if (stateTable[row + 1] != 0) {
                if (lookaheadStatus != 0 && stateTable[row + 0] == lookaheadStatus) {
                    result = lookaheadResult;
                    this.fLastRuleStatusIndex = lookaheadTagIdx;
                    lookaheadStatus = 0;
                    if ((flagsState & 1) == 0) continue;
                    text.setIndex(result);
                    return result;
                }
                lookaheadResult = text.getIndex();
                if (c >= 65536 && c <= 0x10FFFF) {
                    --lookaheadResult;
                }
                lookaheadStatus = stateTable[row + 1];
                lookaheadTagIdx = stateTable[row + 2];
                continue;
            }
            if (stateTable[row + 0] == 0) continue;
            lookaheadStatus = 0;
        }
        if (result == initialPosition) {
            if (TRACE) {
                System.out.println("Iterator did not move. Advancing by 1.");
            }
            text.setIndex(initialPosition);
            CharacterIteration.next32(text);
            result = text.getIndex();
        } else {
            text.setIndex(result);
        }
        if (TRACE) {
            System.out.println("result = " + result);
        }
        return result;
    }

    private int handlePrevious(short[] stateTable) {
        if (this.fText == null || stateTable == null) {
            return 0;
        }
        int category = 0;
        short lookaheadStatus = 0;
        int result = 0;
        int initialPosition = 0;
        int lookaheadResult = 0;
        boolean lookAheadHardBreak = (stateTable[5] & 1) != 0;
        this.fLastStatusIndexValid = false;
        this.fLastRuleStatusIndex = 0;
        result = initialPosition = this.fText.getIndex();
        int c = CharacterIteration.previous32(this.fText);
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        category = 3;
        int mode = 1;
        if ((stateTable[5] & 2) != 0) {
            category = 2;
            mode = 0;
        }
        if (TRACE) {
            System.out.println("Handle Prev   pos   char  state category ");
        }
        while (true) {
            if (c == Integer.MAX_VALUE) {
                if (mode == 2 || this.fRData.fHeader.fVersion == 1) {
                    if (lookaheadResult < result) {
                        result = lookaheadResult;
                        lookaheadStatus = 0;
                        break;
                    }
                    if (result != initialPosition) break;
                    this.fText.setIndex(initialPosition);
                    CharacterIteration.previous32(this.fText);
                    break;
                }
                mode = 2;
                category = 1;
            }
            if (mode == 1 && ((category = (int)((short)this.fRData.fTrie.getCodePointValue(c))) & 0x4000) != 0) {
                ++this.fDictionaryCharCount;
                category &= 0xFFFFBFFF;
            }
            if (TRACE) {
                System.out.print("             " + this.fText.getIndex() + "   ");
                if (32 <= c && c < 127) {
                    System.out.print("  " + c + "  ");
                } else {
                    System.out.print(" " + Integer.toHexString(c) + " ");
                }
                System.out.println(" " + state + "  " + category + " ");
            }
            if (stateTable[(row = this.fRData.getRowIndex(state = stateTable[row + 4 + category])) + 0] == -1) {
                result = this.fText.getIndex();
            }
            if (stateTable[row + 1] != 0) {
                if (lookaheadStatus != 0 && stateTable[row + 0] == lookaheadStatus) {
                    result = lookaheadResult;
                    lookaheadStatus = 0;
                    if (lookAheadHardBreak) {
                        break;
                    }
                } else {
                    lookaheadResult = this.fText.getIndex();
                    lookaheadStatus = stateTable[row + 1];
                }
            } else if (stateTable[row + 0] != 0 && !lookAheadHardBreak) {
                lookaheadStatus = 0;
            }
            if (state == 0) break;
            if (mode == 1) {
                c = CharacterIteration.previous32(this.fText);
                continue;
            }
            if (mode != 0) continue;
            mode = 1;
        }
        if (result == initialPosition) {
            result = this.fText.setIndex(initialPosition);
            CharacterIteration.previous32(this.fText);
            result = this.fText.getIndex();
        }
        this.fText.setIndex(result);
        if (TRACE) {
            System.out.println("Result = " + result);
        }
        return result;
    }
}

