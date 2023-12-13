/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.RBBIDataWrapper;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.BurmeseBreakEngine;
import com.ibm.icu.text.CjkBreakEngine;
import com.ibm.icu.text.DictionaryBreakEngine;
import com.ibm.icu.text.KhmerBreakEngine;
import com.ibm.icu.text.LanguageBreakEngine;
import com.ibm.icu.text.LaoBreakEngine;
import com.ibm.icu.text.RBBIRuleBuilder;
import com.ibm.icu.text.ThaiBreakEngine;
import com.ibm.icu.text.UnhandledBreakEngine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class RuleBasedBreakIterator
extends BreakIterator {
    private static final int START_STATE = 1;
    private static final int STOP_STATE = 0;
    private static final int RBBI_START = 0;
    private static final int RBBI_RUN = 1;
    private static final int RBBI_END = 2;
    private CharacterIterator fText = new StringCharacterIterator("");
    @Deprecated
    public RBBIDataWrapper fRData;
    private int fPosition;
    private int fRuleStatusIndex;
    private boolean fDone;
    private BreakCache fBreakCache = new BreakCache();
    private int fDictionaryCharCount = 0;
    private DictionaryCache fDictionaryCache = new DictionaryCache();
    private static final String RBBI_DEBUG_ARG = "rbbi";
    private static final boolean TRACE = ICUDebug.enabled("rbbi") && ICUDebug.value("rbbi").indexOf("trace") >= 0;
    private static final UnhandledBreakEngine gUnhandledBreakEngine = new UnhandledBreakEngine();
    private static final List<LanguageBreakEngine> gAllBreakEngines = new ArrayList<LanguageBreakEngine>();
    private List<LanguageBreakEngine> fBreakEngines;
    @Deprecated
    public static final String fDebugEnv;
    private static final int kMaxLookaheads = 8;
    private LookAheadResults fLookAheadMatches = new LookAheadResults();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private RuleBasedBreakIterator() {
        List<LanguageBreakEngine> list = gAllBreakEngines;
        synchronized (list) {
            this.fBreakEngines = new ArrayList<LanguageBreakEngine>(gAllBreakEngines);
        }
    }

    public static RuleBasedBreakIterator getInstanceFromCompiledRules(InputStream is) throws IOException {
        RuleBasedBreakIterator This = new RuleBasedBreakIterator();
        This.fRData = RBBIDataWrapper.get(ICUBinary.getByteBufferFromInputStreamAndCloseStream(is));
        return This;
    }

    @Deprecated
    public static RuleBasedBreakIterator getInstanceFromCompiledRules(ByteBuffer bytes) throws IOException {
        RuleBasedBreakIterator This = new RuleBasedBreakIterator();
        This.fRData = RBBIDataWrapper.get(bytes);
        return This;
    }

    public RuleBasedBreakIterator(String rules) {
        this();
        try {
            ByteArrayOutputStream ruleOS = new ByteArrayOutputStream();
            RuleBasedBreakIterator.compileRules(rules, ruleOS);
            this.fRData = RBBIDataWrapper.get(ByteBuffer.wrap(ruleOS.toByteArray()));
        }
        catch (IOException e2) {
            RuntimeException rte = new RuntimeException("RuleBasedBreakIterator rule compilation internal error: " + e2.getMessage());
            throw rte;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object clone() {
        RuleBasedBreakIterator result = (RuleBasedBreakIterator)super.clone();
        if (this.fText != null) {
            result.fText = (CharacterIterator)this.fText.clone();
        }
        List<LanguageBreakEngine> list = gAllBreakEngines;
        synchronized (list) {
            result.fBreakEngines = new ArrayList<LanguageBreakEngine>(gAllBreakEngines);
        }
        result.fLookAheadMatches = new LookAheadResults();
        RuleBasedBreakIterator ruleBasedBreakIterator = result;
        ruleBasedBreakIterator.getClass();
        result.fBreakCache = ruleBasedBreakIterator.new BreakCache(this.fBreakCache);
        RuleBasedBreakIterator ruleBasedBreakIterator2 = result;
        ruleBasedBreakIterator2.getClass();
        result.fDictionaryCache = ruleBasedBreakIterator2.new DictionaryCache(this.fDictionaryCache);
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
            if (this.fText == null || other.fText == null || !this.fText.equals(other.fText)) {
                return false;
            }
            return this.fPosition == other.fPosition;
        }
        catch (ClassCastException e2) {
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

    @Deprecated
    public void dump(PrintStream out) {
        if (out == null) {
            out = System.out;
        }
        this.fRData.dump(out);
    }

    public static void compileRules(String rules, OutputStream ruleBinary) throws IOException {
        RBBIRuleBuilder.compileRules(rules, ruleBinary);
    }

    @Override
    public int first() {
        if (this.fText == null) {
            return -1;
        }
        this.fText.first();
        int start = this.fText.getIndex();
        if (!this.fBreakCache.seek(start)) {
            this.fBreakCache.populateNear(start);
        }
        this.fBreakCache.current();
        assert (this.fPosition == start);
        return this.fPosition;
    }

    @Override
    public int last() {
        if (this.fText == null) {
            return -1;
        }
        int endPos = this.fText.getEndIndex();
        boolean endShouldBeBoundary = this.isBoundary(endPos);
        assert (endShouldBeBoundary);
        if (this.fPosition != endPos) assert (this.fPosition == endPos);
        return endPos;
    }

    @Override
    public int next(int n2) {
        int result = 0;
        if (n2 > 0) {
            while (n2 > 0 && result != -1) {
                result = this.next();
                --n2;
            }
        } else if (n2 < 0) {
            while (n2 < 0 && result != -1) {
                result = this.previous();
                ++n2;
            }
        } else {
            result = this.current();
        }
        return result;
    }

    @Override
    public int next() {
        this.fBreakCache.next();
        return this.fDone ? -1 : this.fPosition;
    }

    @Override
    public int previous() {
        this.fBreakCache.previous();
        return this.fDone ? -1 : this.fPosition;
    }

    @Override
    public int following(int startPos) {
        if (startPos < this.fText.getBeginIndex()) {
            return this.first();
        }
        startPos = RuleBasedBreakIterator.CISetIndex32(this.fText, startPos);
        this.fBreakCache.following(startPos);
        return this.fDone ? -1 : this.fPosition;
    }

    @Override
    public int preceding(int offset) {
        if (this.fText == null || offset > this.fText.getEndIndex()) {
            return this.last();
        }
        if (offset < this.fText.getBeginIndex()) {
            return this.first();
        }
        int adjustedOffset = offset;
        this.fBreakCache.preceding(adjustedOffset);
        return this.fDone ? -1 : this.fPosition;
    }

    protected static final void checkOffset(int offset, CharacterIterator text) {
        if (offset < text.getBeginIndex() || offset > text.getEndIndex()) {
            throw new IllegalArgumentException("offset out of bounds");
        }
    }

    @Override
    public boolean isBoundary(int offset) {
        RuleBasedBreakIterator.checkOffset(offset, this.fText);
        int adjustedOffset = RuleBasedBreakIterator.CISetIndex32(this.fText, offset);
        boolean result = false;
        if (this.fBreakCache.seek(adjustedOffset) || this.fBreakCache.populateNear(adjustedOffset)) {
            boolean bl = result = this.fBreakCache.current() == offset;
        }
        if (!result) {
            this.next();
        }
        return result;
    }

    @Override
    public int current() {
        return this.fText != null ? this.fPosition : -1;
    }

    @Override
    public int getRuleStatus() {
        int idx = this.fRuleStatusIndex + this.fRData.fStatusTable[this.fRuleStatusIndex];
        int tagVal = this.fRData.fStatusTable[idx];
        return tagVal;
    }

    @Override
    public int getRuleStatusVec(int[] fillInArray) {
        int numStatusVals = this.fRData.fStatusTable[this.fRuleStatusIndex];
        if (fillInArray != null) {
            int numToCopy = Math.min(numStatusVals, fillInArray.length);
            for (int i2 = 0; i2 < numToCopy; ++i2) {
                fillInArray[i2] = this.fRData.fStatusTable[this.fRuleStatusIndex + i2 + 1];
            }
        }
        return numStatusVals;
    }

    @Override
    public CharacterIterator getText() {
        return this.fText;
    }

    @Override
    public void setText(CharacterIterator newText) {
        if (newText != null) {
            this.fBreakCache.reset(newText.getBeginIndex(), 0);
        } else {
            this.fBreakCache.reset();
        }
        this.fDictionaryCache.reset();
        this.fText = newText;
        this.first();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private LanguageBreakEngine getLanguageBreakEngine(int c2) {
        for (LanguageBreakEngine languageBreakEngine : this.fBreakEngines) {
            if (!languageBreakEngine.handles(c2)) continue;
            return languageBreakEngine;
        }
        List<LanguageBreakEngine> list = gAllBreakEngines;
        synchronized (list) {
            LanguageBreakEngine eng;
            int n2;
            for (LanguageBreakEngine candidate : gAllBreakEngines) {
                if (!candidate.handles(c2)) continue;
                this.fBreakEngines.add(candidate);
                return candidate;
            }
            int n3 = UCharacter.getIntPropertyValue(c2, 4106);
            if (n3 == 22 || n3 == 20) {
                n2 = 17;
            }
            try {
                switch (n2) {
                    case 38: {
                        eng = new ThaiBreakEngine();
                        break;
                    }
                    case 24: {
                        eng = new LaoBreakEngine();
                        break;
                    }
                    case 28: {
                        eng = new BurmeseBreakEngine();
                        break;
                    }
                    case 23: {
                        eng = new KhmerBreakEngine();
                        break;
                    }
                    case 17: {
                        eng = new CjkBreakEngine(false);
                        break;
                    }
                    case 18: {
                        eng = new CjkBreakEngine(true);
                        break;
                    }
                    default: {
                        gUnhandledBreakEngine.handleChar(c2);
                        eng = gUnhandledBreakEngine;
                        break;
                    }
                }
            }
            catch (IOException e2) {
                eng = null;
            }
            if (eng != null && eng != gUnhandledBreakEngine) {
                gAllBreakEngines.add(eng);
                this.fBreakEngines.add(eng);
            }
            return eng;
        }
    }

    private int handleNext() {
        if (TRACE) {
            System.out.println("Handle Next   pos      char  state category");
        }
        this.fRuleStatusIndex = 0;
        this.fDictionaryCharCount = 0;
        CharacterIterator text = this.fText;
        Trie2 trie = this.fRData.fTrie;
        short[] stateTable = this.fRData.fFTable.fTable;
        int initialPosition = this.fPosition;
        text.setIndex(initialPosition);
        int result = initialPosition;
        int c2 = text.current();
        if (c2 >= 55296 && (c2 = CharacterIteration.nextTrail32(text, c2)) == Integer.MAX_VALUE) {
            this.fDone = true;
            return -1;
        }
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        int category = 3;
        int flagsState = this.fRData.fFTable.fFlags;
        int mode = 1;
        if ((flagsState & 2) != 0) {
            category = 2;
            mode = 0;
            if (TRACE) {
                System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                System.out.print(RBBIDataWrapper.intToHexString(c2, 10));
                System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
            }
        }
        this.fLookAheadMatches.reset();
        while (state != 0) {
            int lookaheadResult;
            short completedRule;
            if (c2 == Integer.MAX_VALUE) {
                if (mode == 2) break;
                mode = 2;
                category = 1;
            } else if (mode == 1) {
                category = (short)trie.get(c2);
                if ((category & 0x4000) != 0) {
                    ++this.fDictionaryCharCount;
                    category = (short)(category & 0xFFFFBFFF);
                }
                if (TRACE) {
                    System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                    System.out.print(RBBIDataWrapper.intToHexString(c2, 10));
                    System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
                }
                if ((c2 = (int)text.next()) >= 55296) {
                    c2 = CharacterIteration.nextTrail32(text, c2);
                }
            } else {
                mode = 1;
            }
            state = stateTable[row + 4 + category];
            row = this.fRData.getRowIndex(state);
            if (stateTable[row + 0] == -1) {
                result = text.getIndex();
                if (c2 >= 65536 && c2 <= 0x10FFFF) {
                    --result;
                }
                this.fRuleStatusIndex = stateTable[row + 2];
            }
            if ((completedRule = stateTable[row + 0]) > 0 && (lookaheadResult = this.fLookAheadMatches.getPosition(completedRule)) >= 0) {
                this.fRuleStatusIndex = stateTable[row + 2];
                this.fPosition = lookaheadResult;
                return lookaheadResult;
            }
            short rule = stateTable[row + 1];
            if (rule == 0) continue;
            int pos = text.getIndex();
            if (c2 >= 65536 && c2 <= 0x10FFFF) {
                --pos;
            }
            this.fLookAheadMatches.setPosition(rule, pos);
        }
        if (result == initialPosition) {
            if (TRACE) {
                System.out.println("Iterator did not move. Advancing by 1.");
            }
            text.setIndex(initialPosition);
            CharacterIteration.next32(text);
            result = text.getIndex();
            this.fRuleStatusIndex = 0;
        }
        this.fPosition = result;
        if (TRACE) {
            System.out.println("result = " + result);
        }
        return result;
    }

    private int handleSafePrevious(int fromPosition) {
        short category = 0;
        int result = 0;
        CharacterIterator text = this.fText;
        Trie2 trie = this.fRData.fTrie;
        short[] stateTable = this.fRData.fRTable.fTable;
        RuleBasedBreakIterator.CISetIndex32(text, fromPosition);
        if (TRACE) {
            System.out.print("Handle Previous   pos   char  state category");
        }
        if (text.getIndex() == text.getBeginIndex()) {
            return -1;
        }
        int c2 = CharacterIteration.previous32(text);
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        while (c2 != Integer.MAX_VALUE) {
            category = (short)trie.get(c2);
            category = (short)(category & 0xFFFFBFFF);
            if (TRACE) {
                System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                System.out.print(RBBIDataWrapper.intToHexString(c2, 10));
                System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
            }
            assert (category < this.fRData.fHeader.fCatCount);
            state = stateTable[row + 4 + category];
            row = this.fRData.getRowIndex(state);
            if (state == 0) break;
            c2 = CharacterIteration.previous32(text);
        }
        result = text.getIndex();
        if (TRACE) {
            System.out.println("result = " + result);
        }
        return result;
    }

    private static int CISetIndex32(CharacterIterator ci, int index) {
        if (index <= ci.getBeginIndex()) {
            ci.first();
        } else if (index >= ci.getEndIndex()) {
            ci.setIndex(ci.getEndIndex());
        } else if (Character.isLowSurrogate(ci.setIndex(index)) && !Character.isHighSurrogate(ci.previous())) {
            ci.next();
        }
        return ci.getIndex();
    }

    static {
        gAllBreakEngines.add(gUnhandledBreakEngine);
        fDebugEnv = ICUDebug.enabled(RBBI_DEBUG_ARG) ? ICUDebug.value(RBBI_DEBUG_ARG) : null;
    }

    class BreakCache {
        static final boolean RetainCachePosition = false;
        static final boolean UpdateCachePosition = true;
        static final int CACHE_SIZE = 128;
        int fStartBufIdx;
        int fEndBufIdx;
        int fTextIdx;
        int fBufIdx;
        int[] fBoundaries = new int[128];
        short[] fStatuses = new short[128];
        DictionaryBreakEngine.DequeI fSideBuffer = new DictionaryBreakEngine.DequeI();

        BreakCache() {
            this.reset();
        }

        void reset(int pos, int ruleStatus) {
            this.fStartBufIdx = 0;
            this.fEndBufIdx = 0;
            this.fTextIdx = pos;
            this.fBufIdx = 0;
            this.fBoundaries[0] = pos;
            this.fStatuses[0] = (short)ruleStatus;
        }

        void reset() {
            this.reset(0, 0);
        }

        void next() {
            if (this.fBufIdx == this.fEndBufIdx) {
                RuleBasedBreakIterator.this.fDone = !this.populateFollowing();
                RuleBasedBreakIterator.this.fPosition = this.fTextIdx;
                RuleBasedBreakIterator.this.fRuleStatusIndex = this.fStatuses[this.fBufIdx];
            } else {
                this.fBufIdx = this.modChunkSize(this.fBufIdx + 1);
                this.fTextIdx = RuleBasedBreakIterator.this.fPosition = this.fBoundaries[this.fBufIdx];
                RuleBasedBreakIterator.this.fRuleStatusIndex = this.fStatuses[this.fBufIdx];
            }
        }

        void previous() {
            int initialBufIdx = this.fBufIdx;
            if (this.fBufIdx == this.fStartBufIdx) {
                this.populatePreceding();
            } else {
                this.fBufIdx = this.modChunkSize(this.fBufIdx - 1);
                this.fTextIdx = this.fBoundaries[this.fBufIdx];
            }
            RuleBasedBreakIterator.this.fDone = this.fBufIdx == initialBufIdx;
            RuleBasedBreakIterator.this.fPosition = this.fTextIdx;
            RuleBasedBreakIterator.this.fRuleStatusIndex = this.fStatuses[this.fBufIdx];
        }

        void following(int startPos) {
            if (startPos == this.fTextIdx || this.seek(startPos) || this.populateNear(startPos)) {
                RuleBasedBreakIterator.this.fDone = false;
                this.next();
            }
        }

        void preceding(int startPos) {
            if (startPos == this.fTextIdx || this.seek(startPos) || this.populateNear(startPos)) {
                if (startPos == this.fTextIdx) {
                    this.previous();
                } else {
                    assert (startPos > this.fTextIdx);
                    this.current();
                }
            }
        }

        int current() {
            RuleBasedBreakIterator.this.fPosition = this.fTextIdx;
            RuleBasedBreakIterator.this.fRuleStatusIndex = this.fStatuses[this.fBufIdx];
            RuleBasedBreakIterator.this.fDone = false;
            return this.fTextIdx;
        }

        boolean populateNear(int position) {
            assert (position < this.fBoundaries[this.fStartBufIdx] || position > this.fBoundaries[this.fEndBufIdx]);
            if (position < this.fBoundaries[this.fStartBufIdx] - 15 || position > this.fBoundaries[this.fEndBufIdx] + 15) {
                int aBoundary = RuleBasedBreakIterator.this.fText.getBeginIndex();
                int ruleStatusIndex = 0;
                if (position > aBoundary + 20) {
                    int backupPos = RuleBasedBreakIterator.this.handleSafePrevious(position);
                    if (backupPos > aBoundary) {
                        RuleBasedBreakIterator.this.fPosition = backupPos;
                        aBoundary = RuleBasedBreakIterator.this.handleNext();
                        if (aBoundary == backupPos + 1 || aBoundary == backupPos + 2 && Character.isHighSurrogate(RuleBasedBreakIterator.this.fText.setIndex(backupPos)) && Character.isLowSurrogate(RuleBasedBreakIterator.this.fText.next())) {
                            aBoundary = RuleBasedBreakIterator.this.handleNext();
                        }
                    }
                    ruleStatusIndex = RuleBasedBreakIterator.this.fRuleStatusIndex;
                }
                this.reset(aBoundary, ruleStatusIndex);
            }
            if (this.fBoundaries[this.fEndBufIdx] < position) {
                while (this.fBoundaries[this.fEndBufIdx] < position) {
                    if (this.populateFollowing()) continue;
                    assert (false);
                    return false;
                }
                this.fBufIdx = this.fEndBufIdx;
                this.fTextIdx = this.fBoundaries[this.fBufIdx];
                while (this.fTextIdx > position) {
                    this.previous();
                }
                return true;
            }
            if (this.fBoundaries[this.fStartBufIdx] > position) {
                while (this.fBoundaries[this.fStartBufIdx] > position) {
                    this.populatePreceding();
                }
                this.fBufIdx = this.fStartBufIdx;
                this.fTextIdx = this.fBoundaries[this.fBufIdx];
                while (this.fTextIdx < position) {
                    this.next();
                }
                if (this.fTextIdx > position) {
                    this.previous();
                }
                return true;
            }
            assert (this.fTextIdx == position);
            return true;
        }

        boolean populateFollowing() {
            int fromPosition = this.fBoundaries[this.fEndBufIdx];
            short fromRuleStatusIdx = this.fStatuses[this.fEndBufIdx];
            int pos = 0;
            int ruleStatusIdx = 0;
            if (RuleBasedBreakIterator.this.fDictionaryCache.following(fromPosition)) {
                this.addFollowing(((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fBoundary, ((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fStatusIndex, true);
                return true;
            }
            RuleBasedBreakIterator.this.fPosition = fromPosition;
            pos = RuleBasedBreakIterator.this.handleNext();
            if (pos == -1) {
                return false;
            }
            ruleStatusIdx = RuleBasedBreakIterator.this.fRuleStatusIndex;
            if (RuleBasedBreakIterator.this.fDictionaryCharCount > 0) {
                RuleBasedBreakIterator.this.fDictionaryCache.populateDictionary(fromPosition, pos, fromRuleStatusIdx, ruleStatusIdx);
                if (RuleBasedBreakIterator.this.fDictionaryCache.following(fromPosition)) {
                    this.addFollowing(((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fBoundary, ((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fStatusIndex, true);
                    return true;
                }
            }
            this.addFollowing(pos, ruleStatusIdx, true);
            for (int count = 0; count < 6 && (pos = RuleBasedBreakIterator.this.handleNext()) != -1 && RuleBasedBreakIterator.this.fDictionaryCharCount <= 0; ++count) {
                this.addFollowing(pos, RuleBasedBreakIterator.this.fRuleStatusIndex, false);
            }
            return true;
        }

        boolean populatePreceding() {
            int fromPosition = this.fBoundaries[this.fStartBufIdx];
            int textBegin = RuleBasedBreakIterator.this.fText.getBeginIndex();
            if (fromPosition == textBegin) {
                return false;
            }
            int position = textBegin;
            int positionStatusIdx = 0;
            if (RuleBasedBreakIterator.this.fDictionaryCache.preceding(fromPosition)) {
                this.addPreceding(((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fBoundary, ((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fStatusIndex, true);
                return true;
            }
            int backupPosition = fromPosition;
            do {
                if ((backupPosition = (backupPosition -= 30) <= textBegin ? textBegin : RuleBasedBreakIterator.this.handleSafePrevious(backupPosition)) == -1 || backupPosition == textBegin) {
                    position = textBegin;
                    positionStatusIdx = 0;
                    continue;
                }
                RuleBasedBreakIterator.this.fPosition = backupPosition;
                position = RuleBasedBreakIterator.this.handleNext();
                if (position == backupPosition + 1 || position == backupPosition + 2 && Character.isHighSurrogate(RuleBasedBreakIterator.this.fText.setIndex(backupPosition)) && Character.isLowSurrogate(RuleBasedBreakIterator.this.fText.next())) {
                    position = RuleBasedBreakIterator.this.handleNext();
                }
                positionStatusIdx = RuleBasedBreakIterator.this.fRuleStatusIndex;
            } while (position >= fromPosition);
            this.fSideBuffer.removeAllElements();
            this.fSideBuffer.push(position);
            this.fSideBuffer.push(positionStatusIdx);
            do {
                int prevPosition = RuleBasedBreakIterator.this.fPosition = position;
                int prevStatusIdx = positionStatusIdx;
                position = RuleBasedBreakIterator.this.handleNext();
                positionStatusIdx = RuleBasedBreakIterator.this.fRuleStatusIndex;
                if (position == -1) break;
                boolean segmentHandledByDictionary = false;
                if (RuleBasedBreakIterator.this.fDictionaryCharCount != 0) {
                    int dictSegEndPosition = position;
                    RuleBasedBreakIterator.this.fDictionaryCache.populateDictionary(prevPosition, dictSegEndPosition, prevStatusIdx, positionStatusIdx);
                    while (RuleBasedBreakIterator.this.fDictionaryCache.following(prevPosition)) {
                        position = ((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fBoundary;
                        positionStatusIdx = ((RuleBasedBreakIterator)RuleBasedBreakIterator.this).fDictionaryCache.fStatusIndex;
                        segmentHandledByDictionary = true;
                        assert (position > prevPosition);
                        if (position >= fromPosition) break;
                        assert (position <= dictSegEndPosition);
                        this.fSideBuffer.push(position);
                        this.fSideBuffer.push(positionStatusIdx);
                        prevPosition = position;
                    }
                    assert (position == dictSegEndPosition || position >= fromPosition);
                }
                if (segmentHandledByDictionary || position >= fromPosition) continue;
                this.fSideBuffer.push(position);
                this.fSideBuffer.push(positionStatusIdx);
            } while (position < fromPosition);
            boolean success = false;
            if (!this.fSideBuffer.isEmpty()) {
                positionStatusIdx = this.fSideBuffer.pop();
                position = this.fSideBuffer.pop();
                this.addPreceding(position, positionStatusIdx, true);
                success = true;
            }
            while (!this.fSideBuffer.isEmpty()) {
                positionStatusIdx = this.fSideBuffer.pop();
                position = this.fSideBuffer.pop();
                if (this.addPreceding(position, positionStatusIdx, false)) continue;
                break;
            }
            return success;
        }

        void addFollowing(int position, int ruleStatusIdx, boolean update) {
            assert (position > this.fBoundaries[this.fEndBufIdx]);
            assert (ruleStatusIdx <= Short.MAX_VALUE);
            int nextIdx = this.modChunkSize(this.fEndBufIdx + 1);
            if (nextIdx == this.fStartBufIdx) {
                this.fStartBufIdx = this.modChunkSize(this.fStartBufIdx + 6);
            }
            this.fBoundaries[nextIdx] = position;
            this.fStatuses[nextIdx] = (short)ruleStatusIdx;
            this.fEndBufIdx = nextIdx;
            if (update) {
                this.fBufIdx = nextIdx;
                this.fTextIdx = position;
            } else assert (nextIdx != this.fBufIdx);
        }

        boolean addPreceding(int position, int ruleStatusIdx, boolean update) {
            assert (position < this.fBoundaries[this.fStartBufIdx]);
            assert (ruleStatusIdx <= Short.MAX_VALUE);
            int nextIdx = this.modChunkSize(this.fStartBufIdx - 1);
            if (nextIdx == this.fEndBufIdx) {
                if (this.fBufIdx == this.fEndBufIdx && !update) {
                    return false;
                }
                this.fEndBufIdx = this.modChunkSize(this.fEndBufIdx - 1);
            }
            this.fBoundaries[nextIdx] = position;
            this.fStatuses[nextIdx] = (short)ruleStatusIdx;
            this.fStartBufIdx = nextIdx;
            if (update) {
                this.fBufIdx = nextIdx;
                this.fTextIdx = position;
            }
            return true;
        }

        boolean seek(int pos) {
            if (pos < this.fBoundaries[this.fStartBufIdx] || pos > this.fBoundaries[this.fEndBufIdx]) {
                return false;
            }
            if (pos == this.fBoundaries[this.fStartBufIdx]) {
                this.fBufIdx = this.fStartBufIdx;
                this.fTextIdx = this.fBoundaries[this.fBufIdx];
                return true;
            }
            if (pos == this.fBoundaries[this.fEndBufIdx]) {
                this.fBufIdx = this.fEndBufIdx;
                this.fTextIdx = this.fBoundaries[this.fBufIdx];
                return true;
            }
            int min = this.fStartBufIdx;
            int max = this.fEndBufIdx;
            while (min != max) {
                int probe = (min + max + (min > max ? 128 : 0)) / 2;
                if (this.fBoundaries[probe = this.modChunkSize(probe)] > pos) {
                    max = probe;
                    continue;
                }
                min = this.modChunkSize(probe + 1);
            }
            assert (this.fBoundaries[max] > pos);
            this.fBufIdx = this.modChunkSize(max - 1);
            this.fTextIdx = this.fBoundaries[this.fBufIdx];
            assert (this.fTextIdx <= pos);
            return true;
        }

        BreakCache(BreakCache src) {
            this.fStartBufIdx = src.fStartBufIdx;
            this.fEndBufIdx = src.fEndBufIdx;
            this.fTextIdx = src.fTextIdx;
            this.fBufIdx = src.fBufIdx;
            this.fBoundaries = (int[])src.fBoundaries.clone();
            this.fStatuses = (short[])src.fStatuses.clone();
            this.fSideBuffer = new DictionaryBreakEngine.DequeI();
        }

        void dumpCache() {
            System.out.printf("fTextIdx:%d   fBufIdx:%d%n", this.fTextIdx, this.fBufIdx);
            int i2 = this.fStartBufIdx;
            while (true) {
                System.out.printf("%d  %d%n", i2, this.fBoundaries[i2]);
                if (i2 == this.fEndBufIdx) break;
                i2 = this.modChunkSize(i2 + 1);
            }
        }

        private final int modChunkSize(int index) {
            return index & 0x7F;
        }
    }

    class DictionaryCache {
        DictionaryBreakEngine.DequeI fBreaks;
        int fPositionInCache;
        int fStart;
        int fLimit;
        int fFirstRuleStatusIndex;
        int fOtherRuleStatusIndex;
        int fBoundary;
        int fStatusIndex;

        void reset() {
            this.fPositionInCache = -1;
            this.fStart = 0;
            this.fLimit = 0;
            this.fFirstRuleStatusIndex = 0;
            this.fOtherRuleStatusIndex = 0;
            this.fBreaks.removeAllElements();
        }

        boolean following(int fromPos) {
            if (fromPos >= this.fLimit || fromPos < this.fStart) {
                this.fPositionInCache = -1;
                return false;
            }
            int r2 = 0;
            if (this.fPositionInCache >= 0 && this.fPositionInCache < this.fBreaks.size() && this.fBreaks.elementAt(this.fPositionInCache) == fromPos) {
                ++this.fPositionInCache;
                if (this.fPositionInCache >= this.fBreaks.size()) {
                    this.fPositionInCache = -1;
                    return false;
                }
                r2 = this.fBreaks.elementAt(this.fPositionInCache);
                assert (r2 > fromPos);
                this.fBoundary = r2;
                this.fStatusIndex = this.fOtherRuleStatusIndex;
                return true;
            }
            this.fPositionInCache = 0;
            while (this.fPositionInCache < this.fBreaks.size()) {
                r2 = this.fBreaks.elementAt(this.fPositionInCache);
                if (r2 > fromPos) {
                    this.fBoundary = r2;
                    this.fStatusIndex = this.fOtherRuleStatusIndex;
                    return true;
                }
                ++this.fPositionInCache;
            }
            assert (false);
            this.fPositionInCache = -1;
            return false;
        }

        boolean preceding(int fromPos) {
            if (fromPos <= this.fStart || fromPos > this.fLimit) {
                this.fPositionInCache = -1;
                return false;
            }
            if (fromPos == this.fLimit) {
                this.fPositionInCache = this.fBreaks.size() - 1;
                if (this.fPositionInCache >= 0) assert (this.fBreaks.elementAt(this.fPositionInCache) == fromPos);
            }
            if (this.fPositionInCache > 0 && this.fPositionInCache < this.fBreaks.size() && this.fBreaks.elementAt(this.fPositionInCache) == fromPos) {
                --this.fPositionInCache;
                int r2 = this.fBreaks.elementAt(this.fPositionInCache);
                assert (r2 < fromPos);
                this.fBoundary = r2;
                this.fStatusIndex = r2 == this.fStart ? this.fFirstRuleStatusIndex : this.fOtherRuleStatusIndex;
                return true;
            }
            if (this.fPositionInCache == 0) {
                this.fPositionInCache = -1;
                return false;
            }
            this.fPositionInCache = this.fBreaks.size() - 1;
            while (this.fPositionInCache >= 0) {
                int r3 = this.fBreaks.elementAt(this.fPositionInCache);
                if (r3 < fromPos) {
                    this.fBoundary = r3;
                    this.fStatusIndex = r3 == this.fStart ? this.fFirstRuleStatusIndex : this.fOtherRuleStatusIndex;
                    return true;
                }
                --this.fPositionInCache;
            }
            assert (false);
            this.fPositionInCache = -1;
            return false;
        }

        void populateDictionary(int startPos, int endPos, int firstRuleStatus, int otherRuleStatus) {
            if (endPos - startPos <= 1) {
                return;
            }
            this.reset();
            this.fFirstRuleStatusIndex = firstRuleStatus;
            this.fOtherRuleStatusIndex = otherRuleStatus;
            int rangeStart = startPos;
            int rangeEnd = endPos;
            int foundBreakCount = 0;
            RuleBasedBreakIterator.this.fText.setIndex(rangeStart);
            int c2 = CharacterIteration.current32(RuleBasedBreakIterator.this.fText);
            short category = (short)RuleBasedBreakIterator.this.fRData.fTrie.get(c2);
            while (true) {
                int current;
                if ((current = RuleBasedBreakIterator.this.fText.getIndex()) < rangeEnd && (category & 0x4000) == 0) {
                    c2 = CharacterIteration.next32(RuleBasedBreakIterator.this.fText);
                    category = (short)RuleBasedBreakIterator.this.fRData.fTrie.get(c2);
                    continue;
                }
                if (current >= rangeEnd) break;
                LanguageBreakEngine lbe = RuleBasedBreakIterator.this.getLanguageBreakEngine(c2);
                if (lbe != null) {
                    foundBreakCount += lbe.findBreaks(RuleBasedBreakIterator.this.fText, rangeStart, rangeEnd, this.fBreaks);
                }
                c2 = CharacterIteration.current32(RuleBasedBreakIterator.this.fText);
                category = (short)RuleBasedBreakIterator.this.fRData.fTrie.get(c2);
            }
            if (foundBreakCount > 0) {
                assert (foundBreakCount == this.fBreaks.size());
                if (startPos < this.fBreaks.elementAt(0)) {
                    this.fBreaks.offer(startPos);
                }
                if (endPos > this.fBreaks.peek()) {
                    this.fBreaks.push(endPos);
                }
                this.fPositionInCache = 0;
                this.fStart = this.fBreaks.elementAt(0);
                this.fLimit = this.fBreaks.peek();
            }
        }

        DictionaryCache() {
            this.fPositionInCache = -1;
            this.fBreaks = new DictionaryBreakEngine.DequeI();
        }

        DictionaryCache(DictionaryCache src) {
            try {
                this.fBreaks = (DictionaryBreakEngine.DequeI)src.fBreaks.clone();
            }
            catch (CloneNotSupportedException e2) {
                throw new RuntimeException(e2);
            }
            this.fPositionInCache = src.fPositionInCache;
            this.fStart = src.fStart;
            this.fLimit = src.fLimit;
            this.fFirstRuleStatusIndex = src.fFirstRuleStatusIndex;
            this.fOtherRuleStatusIndex = src.fOtherRuleStatusIndex;
            this.fBoundary = src.fBoundary;
            this.fStatusIndex = src.fStatusIndex;
        }
    }

    private static class LookAheadResults {
        int fUsedSlotLimit = 0;
        int[] fPositions = new int[8];
        int[] fKeys = new int[8];

        LookAheadResults() {
        }

        int getPosition(int key) {
            for (int i2 = 0; i2 < this.fUsedSlotLimit; ++i2) {
                if (this.fKeys[i2] != key) continue;
                return this.fPositions[i2];
            }
            assert (false);
            return -1;
        }

        void setPosition(int key, int position) {
            int i2;
            for (i2 = 0; i2 < this.fUsedSlotLimit; ++i2) {
                if (this.fKeys[i2] != key) continue;
                this.fPositions[i2] = position;
                return;
            }
            if (i2 >= 8) {
                assert (false);
                i2 = 7;
            }
            this.fKeys[i2] = key;
            this.fPositions[i2] = position;
            assert (this.fUsedSlotLimit == i2);
            this.fUsedSlotLimit = i2 + 1;
        }

        void reset() {
            this.fUsedSlotLimit = 0;
        }
    }
}

