/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.BitSet;
import jdk.nashorn.internal.runtime.regexp.joni.BitStatus;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.Region;
import jdk.nashorn.internal.runtime.regexp.joni.StackEntry;
import jdk.nashorn.internal.runtime.regexp.joni.StackMachine;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

class ByteCodeMachine
extends StackMachine {
    private int bestLen;
    private int s = 0;
    private int range;
    private int sprev;
    private int sstart;
    private int sbegin;
    private final int[] code;
    private int ip;

    ByteCodeMachine(Regex regex, char[] chars, int p, int end) {
        super(regex, chars, p, end);
        this.code = regex.code;
    }

    private boolean stringCmpIC(int caseFlodFlag, int s1p, IntHolder ps2, int mbLen, int textEnd) {
        int s1 = s1p;
        int s2 = ps2.value;
        int end1 = s1 + mbLen;
        while (s1 < end1) {
            char c2;
            char c1;
            if ((c1 = EncodingHelper.toLowerCase(this.chars[s1++])) == (c2 = EncodingHelper.toLowerCase(this.chars[s2++]))) continue;
            return false;
        }
        ps2.value = s2;
        return true;
    }

    private void debugMatchBegin() {
        Config.log.println("match_at: str: " + this.str + ", end: " + this.end + ", start: " + this.sstart + ", sprev: " + this.sprev);
        Config.log.println("size: " + (this.end - this.str) + ", start offset: " + (this.sstart - this.str));
    }

    private void debugMatchLoop() {
    }

    @Override
    protected final int matchAt(int r, int ss, int sp) {
        this.range = r;
        this.sstart = ss;
        this.sprev = sp;
        this.stk = 0;
        this.ip = 0;
        this.init();
        this.bestLen = -1;
        this.s = ss;
        int[] c = this.code;
        block74: while (true) {
            this.sbegin = this.s;
            switch (c[this.ip++]) {
                case 1: {
                    if (!this.opEnd()) continue block74;
                    return this.finish();
                }
                case 2: {
                    this.opExact1();
                    continue block74;
                }
                case 3: {
                    this.opExact2();
                    continue block74;
                }
                case 4: {
                    this.opExact3();
                    continue block74;
                }
                case 5: {
                    this.opExact4();
                    continue block74;
                }
                case 6: {
                    this.opExact5();
                    continue block74;
                }
                case 7: {
                    this.opExactN();
                    continue block74;
                }
                case 14: {
                    this.opExact1IC();
                    continue block74;
                }
                case 15: {
                    this.opExactNIC();
                    continue block74;
                }
                case 16: {
                    this.opCClass();
                    continue block74;
                }
                case 17: {
                    this.opCClassMB();
                    continue block74;
                }
                case 18: {
                    this.opCClassMIX();
                    continue block74;
                }
                case 19: {
                    this.opCClassNot();
                    continue block74;
                }
                case 20: {
                    this.opCClassMBNot();
                    continue block74;
                }
                case 21: {
                    this.opCClassMIXNot();
                    continue block74;
                }
                case 22: {
                    this.opCClassNode();
                    continue block74;
                }
                case 23: {
                    this.opAnyChar();
                    continue block74;
                }
                case 24: {
                    this.opAnyCharML();
                    continue block74;
                }
                case 25: {
                    this.opAnyCharStar();
                    continue block74;
                }
                case 26: {
                    this.opAnyCharMLStar();
                    continue block74;
                }
                case 27: {
                    this.opAnyCharStarPeekNext();
                    continue block74;
                }
                case 28: {
                    this.opAnyCharMLStarPeekNext();
                    continue block74;
                }
                case 29: {
                    this.opWord();
                    continue block74;
                }
                case 30: {
                    this.opNotWord();
                    continue block74;
                }
                case 31: {
                    this.opWordBound();
                    continue block74;
                }
                case 32: {
                    this.opNotWordBound();
                    continue block74;
                }
                case 33: {
                    this.opWordBegin();
                    continue block74;
                }
                case 34: {
                    this.opWordEnd();
                    continue block74;
                }
                case 35: {
                    this.opBeginBuf();
                    continue block74;
                }
                case 36: {
                    this.opEndBuf();
                    continue block74;
                }
                case 37: {
                    this.opBeginLine();
                    continue block74;
                }
                case 38: {
                    this.opEndLine();
                    continue block74;
                }
                case 39: {
                    this.opSemiEndBuf();
                    continue block74;
                }
                case 40: {
                    this.opBeginPosition();
                    continue block74;
                }
                case 49: {
                    this.opMemoryStartPush();
                    continue block74;
                }
                case 48: {
                    this.opMemoryStart();
                    continue block74;
                }
                case 50: {
                    this.opMemoryEndPush();
                    continue block74;
                }
                case 52: {
                    this.opMemoryEnd();
                    continue block74;
                }
                case 51: {
                    this.opMemoryEndPushRec();
                    continue block74;
                }
                case 53: {
                    this.opMemoryEndRec();
                    continue block74;
                }
                case 41: {
                    this.opBackRef1();
                    continue block74;
                }
                case 42: {
                    this.opBackRef2();
                    continue block74;
                }
                case 43: {
                    this.opBackRefN();
                    continue block74;
                }
                case 44: {
                    this.opBackRefNIC();
                    continue block74;
                }
                case 45: {
                    this.opBackRefMulti();
                    continue block74;
                }
                case 46: {
                    this.opBackRefMultiIC();
                    continue block74;
                }
                case 47: {
                    this.opBackRefAtLevel();
                    continue block74;
                }
                case 66: {
                    this.opNullCheckStart();
                    continue block74;
                }
                case 67: {
                    this.opNullCheckEnd();
                    continue block74;
                }
                case 68: {
                    this.opNullCheckEndMemST();
                    continue block74;
                }
                case 55: {
                    this.opJump();
                    continue block74;
                }
                case 56: {
                    this.opPush();
                    continue block74;
                }
                case 57: {
                    this.opPop();
                    continue block74;
                }
                case 58: {
                    this.opPushOrJumpExact1();
                    continue block74;
                }
                case 59: {
                    this.opPushIfPeekNext();
                    continue block74;
                }
                case 60: {
                    this.opRepeat();
                    continue block74;
                }
                case 61: {
                    this.opRepeatNG();
                    continue block74;
                }
                case 62: {
                    this.opRepeatInc();
                    continue block74;
                }
                case 64: {
                    this.opRepeatIncSG();
                    continue block74;
                }
                case 63: {
                    this.opRepeatIncNG();
                    continue block74;
                }
                case 65: {
                    this.opRepeatIncNGSG();
                    continue block74;
                }
                case 70: {
                    this.opPushPos();
                    continue block74;
                }
                case 71: {
                    this.opPopPos();
                    continue block74;
                }
                case 72: {
                    this.opPushPosNot();
                    continue block74;
                }
                case 73: {
                    this.opFailPos();
                    continue block74;
                }
                case 74: {
                    this.opPushStopBT();
                    continue block74;
                }
                case 75: {
                    this.opPopStopBT();
                    continue block74;
                }
                case 76: {
                    this.opLookBehind();
                    continue block74;
                }
                case 77: {
                    this.opPushLookBehindNot();
                    continue block74;
                }
                case 78: {
                    this.opFailLookBehindNot();
                    continue block74;
                }
                case 0: {
                    return this.finish();
                }
                case 54: {
                    this.opFail();
                    continue block74;
                }
            }
            break;
        }
        throw new InternalException("undefined bytecode (bug)");
    }

    private boolean opEnd() {
        int n = this.s - this.sstart;
        if (n > this.bestLen) {
            if (Option.isFindLongest(this.regex.options)) {
                if (n > this.msaBestLen) {
                    this.msaBestLen = n;
                    this.msaBestS = this.sstart;
                } else {
                    return this.endBestLength();
                }
            }
            this.bestLen = n;
            Region region = this.msaRegion;
            if (region != null) {
                region.beg[0] = this.msaBegin = this.sstart - this.str;
                region.end[0] = this.msaEnd = this.s - this.str;
                for (int i = 1; i <= this.regex.numMem; ++i) {
                    if (this.repeatStk[this.memEndStk + i] != -1) {
                        region.beg[i] = BitStatus.bsAt(this.regex.btMemStart, i) ? this.stack[this.repeatStk[this.memStartStk + i]].getMemPStr() - this.str : this.repeatStk[this.memStartStk + i] - this.str;
                        region.end[i] = BitStatus.bsAt(this.regex.btMemEnd, i) ? this.stack[this.repeatStk[this.memEndStk + i]].getMemPStr() : this.repeatStk[this.memEndStk + i] - this.str;
                        continue;
                    }
                    region.end[i] = -1;
                    region.beg[i] = -1;
                }
            } else {
                this.msaBegin = this.sstart - this.str;
                this.msaEnd = this.s - this.str;
            }
        } else {
            Region region = this.msaRegion;
            if (region != null) {
                region.clear();
            } else {
                this.msaEnd = 0;
                this.msaBegin = 0;
            }
        }
        return this.endBestLength();
    }

    private boolean endBestLength() {
        if (Option.isFindCondition(this.regex.options)) {
            if (Option.isFindNotEmpty(this.regex.options) && this.s == this.sstart) {
                this.bestLen = -1;
                this.opFail();
                return false;
            }
            if (Option.isFindLongest(this.regex.options) && this.s < this.range) {
                this.opFail();
                return false;
            }
        }
        return true;
    }

    private void opExact1() {
        if (this.s >= this.range || this.code[this.ip] != this.chars[this.s++]) {
            this.opFail();
            return;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }

    private void opExact2() {
        if (this.s + 2 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s++;
        ++this.ip;
    }

    private void opExact3() {
        if (this.s + 3 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s++;
        ++this.ip;
    }

    private void opExact4() {
        if (this.s + 4 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s++;
        ++this.ip;
    }

    private void opExact5() {
        if (this.s + 5 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s++;
        ++this.ip;
    }

    private void opExactN() {
        int tlen;
        if (this.s + (tlen = this.code[this.ip++]) > this.range) {
            this.opFail();
            return;
        }
        char[] bs = this.regex.templates[this.code[this.ip++]];
        int ps = this.code[this.ip++];
        while (tlen-- > 0) {
            if (bs[ps++] == this.chars[this.s++]) continue;
            this.opFail();
            return;
        }
        this.sprev = this.s - 1;
    }

    private void opExact1IC() {
        if (this.s >= this.range || this.code[this.ip] != EncodingHelper.toLowerCase(this.chars[this.s++])) {
            this.opFail();
            return;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }

    private void opExactNIC() {
        int tlen;
        if (this.s + (tlen = this.code[this.ip++]) > this.range) {
            this.opFail();
            return;
        }
        char[] bs = this.regex.templates[this.code[this.ip++]];
        int ps = this.code[this.ip++];
        while (tlen-- > 0) {
            if (bs[ps++] == EncodingHelper.toLowerCase(this.chars[this.s++])) continue;
            this.opFail();
            return;
        }
        this.sprev = this.s - 1;
    }

    private boolean isInBitSet() {
        char c = this.chars[this.s];
        return c <= '\u00ff' && (this.code[this.ip + (c >>> BitSet.ROOM_SHIFT)] & '\u0001' << c) != 0;
    }

    private void opCClass() {
        if (this.s >= this.range || !this.isInBitSet()) {
            this.opFail();
            return;
        }
        this.ip += 8;
        ++this.s;
        this.sprev = this.sbegin;
    }

    private boolean isInClassMB() {
        int ss;
        char c;
        int tlen = this.code[this.ip++];
        if (this.s >= this.range) {
            return false;
        }
        if (!EncodingHelper.isInCodeRange(this.code, this.ip, c = this.chars[ss = this.s++])) {
            return false;
        }
        this.ip += tlen;
        return true;
    }

    private void opCClassMB() {
        if (this.s >= this.range || this.chars[this.s] <= '\u00ff') {
            this.opFail();
            return;
        }
        if (!this.isInClassMB()) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassMIX() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] > '\u00ff') {
            this.ip += 8;
            if (!this.isInClassMB()) {
                this.opFail();
                return;
            }
        } else {
            if (!this.isInBitSet()) {
                this.opFail();
                return;
            }
            this.ip += 8;
            int tlen = this.code[this.ip++];
            this.ip += tlen;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassNot() {
        if (this.s >= this.range || this.isInBitSet()) {
            this.opFail();
            return;
        }
        this.ip += 8;
        ++this.s;
        this.sprev = this.sbegin;
    }

    private boolean isNotInClassMB() {
        int ss;
        char c;
        int tlen = this.code[this.ip++];
        if (this.s + 1 > this.range) {
            if (this.s >= this.range) {
                return false;
            }
            this.s = this.end;
            this.ip += tlen;
            return true;
        }
        if (EncodingHelper.isInCodeRange(this.code, this.ip, c = this.chars[ss = this.s++])) {
            return false;
        }
        this.ip += tlen;
        return true;
    }

    private void opCClassMBNot() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] <= '\u00ff') {
            ++this.s;
            int tlen = this.code[this.ip++];
            this.ip += tlen;
            this.sprev = this.sbegin;
            return;
        }
        if (!this.isNotInClassMB()) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassMIXNot() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] > '\u00ff') {
            this.ip += 8;
            if (!this.isNotInClassMB()) {
                this.opFail();
                return;
            }
        } else {
            if (this.isInBitSet()) {
                this.opFail();
                return;
            }
            this.ip += 8;
            int tlen = this.code[this.ip++];
            this.ip += tlen;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassNode() {
        int ss;
        char c;
        CClassNode cc;
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (!(cc = (CClassNode)this.regex.operands[this.code[this.ip++]]).isCodeInCCLength(c = this.chars[ss = this.s++])) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }

    private void opAnyChar() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (EncodingHelper.isNewLine(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }

    private void opAnyCharML() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }

    private void opAnyCharStar() {
        char[] ch = this.chars;
        while (this.s < this.range) {
            this.pushAlt(this.ip, this.s, this.sprev);
            if (EncodingHelper.isNewLine(ch, this.s, this.end)) {
                this.opFail();
                return;
            }
            this.sprev = this.s++;
        }
        this.sprev = this.sbegin;
    }

    private void opAnyCharMLStar() {
        while (this.s < this.range) {
            this.pushAlt(this.ip, this.s, this.sprev);
            this.sprev = this.s++;
        }
        this.sprev = this.sbegin;
    }

    private void opAnyCharStarPeekNext() {
        char c = (char)this.code[this.ip];
        char[] ch = this.chars;
        while (this.s < this.range) {
            char b = ch[this.s];
            if (c == b) {
                this.pushAlt(this.ip + 1, this.s, this.sprev);
            }
            if (EncodingHelper.isNewLine(b)) {
                this.opFail();
                return;
            }
            this.sprev = this.s++;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }

    private void opAnyCharMLStarPeekNext() {
        char c = (char)this.code[this.ip];
        char[] ch = this.chars;
        while (this.s < this.range) {
            if (c == ch[this.s]) {
                this.pushAlt(this.ip + 1, this.s, this.sprev);
            }
            this.sprev = this.s++;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }

    private void opWord() {
        if (this.s >= this.range || !EncodingHelper.isWord(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }

    private void opNotWord() {
        if (this.s >= this.range || EncodingHelper.isWord(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }

    private void opWordBound() {
        if (this.s == this.str) {
            if (this.s >= this.range || !EncodingHelper.isWord(this.chars[this.s])) {
                this.opFail();
                return;
            }
        } else if (this.s == this.end) {
            if (this.sprev >= this.end || !EncodingHelper.isWord(this.chars[this.sprev])) {
                this.opFail();
                return;
            }
        } else if (EncodingHelper.isWord(this.chars[this.s]) == EncodingHelper.isWord(this.chars[this.sprev])) {
            this.opFail();
            return;
        }
    }

    private void opNotWordBound() {
        if (this.s == this.str) {
            if (this.s < this.range && EncodingHelper.isWord(this.chars[this.s])) {
                this.opFail();
                return;
            }
        } else if (this.s == this.end) {
            if (this.sprev < this.end && EncodingHelper.isWord(this.chars[this.sprev])) {
                this.opFail();
                return;
            }
        } else if (EncodingHelper.isWord(this.chars[this.s]) != EncodingHelper.isWord(this.chars[this.sprev])) {
            this.opFail();
            return;
        }
    }

    private void opWordBegin() {
        if (this.s < this.range && EncodingHelper.isWord(this.chars[this.s]) && (this.s == this.str || !EncodingHelper.isWord(this.chars[this.sprev]))) {
            return;
        }
        this.opFail();
    }

    private void opWordEnd() {
        if (this.s != this.str && EncodingHelper.isWord(this.chars[this.sprev]) && (this.s == this.end || !EncodingHelper.isWord(this.chars[this.s]))) {
            return;
        }
        this.opFail();
    }

    private void opBeginBuf() {
        if (this.s != this.str) {
            this.opFail();
        }
    }

    private void opEndBuf() {
        if (this.s != this.end) {
            this.opFail();
        }
    }

    private void opBeginLine() {
        if (this.s == this.str) {
            if (Option.isNotBol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.sprev, this.end) && this.s != this.end) {
            return;
        }
        this.opFail();
    }

    private void opEndLine() {
        if (this.s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.s, this.end)) {
            return;
        }
        this.opFail();
    }

    private void opSemiEndBuf() {
        if (this.s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.s, this.end) && this.s + 1 == this.end) {
            return;
        }
        this.opFail();
    }

    private void opBeginPosition() {
        if (this.s != this.msaStart) {
            this.opFail();
        }
    }

    private void opMemoryStartPush() {
        int mem = this.code[this.ip++];
        this.pushMemStart(mem, this.s);
    }

    private void opMemoryStart() {
        int mem = this.code[this.ip++];
        this.repeatStk[this.memStartStk + mem] = this.s;
    }

    private void opMemoryEndPush() {
        int mem = this.code[this.ip++];
        this.pushMemEnd(mem, this.s);
    }

    private void opMemoryEnd() {
        int mem = this.code[this.ip++];
        this.repeatStk[this.memEndStk + mem] = this.s;
    }

    private void opMemoryEndPushRec() {
        int mem = this.code[this.ip++];
        int stkp = this.getMemStart(mem);
        this.pushMemEnd(mem, this.s);
        this.repeatStk[this.memStartStk + mem] = stkp;
    }

    private void opMemoryEndRec() {
        int mem = this.code[this.ip++];
        this.repeatStk[this.memEndStk + mem] = this.s;
        int stkp = this.getMemStart(mem);
        this.repeatStk[this.memStartStk + mem] = BitStatus.bsAt(this.regex.btMemStart, mem) ? stkp : this.stack[stkp].getMemPStr();
        this.pushMemEndMark(mem);
    }

    private boolean backrefInvalid(int mem) {
        return this.repeatStk[this.memEndStk + mem] == -1 || this.repeatStk[this.memStartStk + mem] == -1;
    }

    private int backrefStart(int mem) {
        return BitStatus.bsAt(this.regex.btMemStart, mem) ? this.stack[this.repeatStk[this.memStartStk + mem]].getMemPStr() : this.repeatStk[this.memStartStk + mem];
    }

    private int backrefEnd(int mem) {
        return BitStatus.bsAt(this.regex.btMemEnd, mem) ? this.stack[this.repeatStk[this.memEndStk + mem]].getMemPStr() : this.repeatStk[this.memEndStk + mem];
    }

    private void backref(int mem) {
        if (mem > this.regex.numMem || this.backrefInvalid(mem)) {
            this.opFail();
            return;
        }
        int pstart = this.backrefStart(mem);
        int pend = this.backrefEnd(mem);
        int n = pend - pstart;
        if (this.s + n > this.range) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        while (n-- > 0) {
            if (this.chars[pstart++] == this.chars[this.s++]) continue;
            this.opFail();
            return;
        }
        if (this.sprev < this.range) {
            while (this.sprev + 1 < this.s) {
                ++this.sprev;
            }
        }
    }

    private void opBackRef1() {
        this.backref(1);
    }

    private void opBackRef2() {
        this.backref(2);
    }

    private void opBackRefN() {
        this.backref(this.code[this.ip++]);
    }

    private void opBackRefNIC() {
        int mem;
        if ((mem = this.code[this.ip++]) > this.regex.numMem || this.backrefInvalid(mem)) {
            this.opFail();
            return;
        }
        int pstart = this.backrefStart(mem);
        int pend = this.backrefEnd(mem);
        int n = pend - pstart;
        if (this.s + n > this.range) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        this.value = this.s;
        if (!this.stringCmpIC(this.regex.caseFoldFlag, pstart, this, n, this.end)) {
            this.opFail();
            return;
        }
        this.s = this.value;
        while (this.sprev + 1 < this.s) {
            ++this.sprev;
        }
    }

    private void opBackRefMulti() {
        int i;
        int tlen = this.code[this.ip++];
        block0: for (i = 0; i < tlen; ++i) {
            int mem;
            if (this.backrefInvalid(mem = this.code[this.ip++])) continue;
            int pstart = this.backrefStart(mem);
            int pend = this.backrefEnd(mem);
            int n = pend - pstart;
            if (this.s + n > this.range) {
                this.opFail();
                return;
            }
            this.sprev = this.s;
            int swork = this.s;
            while (n-- > 0) {
                if (this.chars[pstart++] == this.chars[swork++]) continue;
                continue block0;
            }
            this.s = swork;
            if (this.sprev < this.range) {
                while (this.sprev + 1 < this.s) {
                    ++this.sprev;
                }
            }
            this.ip += tlen - i - 1;
            break;
        }
        if (i == tlen) {
            this.opFail();
            return;
        }
    }

    private void opBackRefMultiIC() {
        int i;
        int tlen = this.code[this.ip++];
        for (i = 0; i < tlen; ++i) {
            int mem;
            if (this.backrefInvalid(mem = this.code[this.ip++])) continue;
            int pstart = this.backrefStart(mem);
            int pend = this.backrefEnd(mem);
            int n = pend - pstart;
            if (this.s + n > this.range) {
                this.opFail();
                return;
            }
            this.sprev = this.s;
            this.value = this.s;
            if (!this.stringCmpIC(this.regex.caseFoldFlag, pstart, this, n, this.end)) continue;
            this.s = this.value;
            while (this.sprev + 1 < this.s) {
                ++this.sprev;
            }
            this.ip += tlen - i - 1;
            break;
        }
        if (i == tlen) {
            this.opFail();
            return;
        }
    }

    private boolean memIsInMemp(int mem, int num, int mempp) {
        int memp = mempp;
        for (int i = 0; i < num; ++i) {
            int m;
            if (mem != (m = this.code[memp++])) continue;
            return true;
        }
        return false;
    }

    private boolean backrefMatchAtNestedLevel(boolean ignoreCase, int caseFoldFlag, int nest, int memNum, int memp) {
        int pend = -1;
        int level = 0;
        for (int k = this.stk - 1; k >= 0; --k) {
            StackEntry e = this.stack[k];
            if (e.type == 2048) {
                --level;
                continue;
            }
            if (e.type == 2304) {
                ++level;
                continue;
            }
            if (level != nest) continue;
            if (e.type == 256) {
                if (!this.memIsInMemp(e.getMemNum(), memNum, memp)) continue;
                int pstart = e.getMemPStr();
                if (pend == -1) continue;
                if (pend - pstart > this.end - this.s) {
                    return false;
                }
                int p = pstart;
                this.value = this.s;
                if (ignoreCase) {
                    if (!this.stringCmpIC(caseFoldFlag, pstart, this, pend - pstart, this.end)) {
                        return false;
                    }
                } else {
                    while (p < pend) {
                        if (this.chars[p++] == this.chars[this.value++]) continue;
                        return false;
                    }
                }
                this.s = this.value;
                return true;
            }
            if (e.type != 33280 || !this.memIsInMemp(e.getMemNum(), memNum, memp)) continue;
            pend = e.getMemPStr();
        }
        return false;
    }

    private void opBackRefAtLevel() {
        int ic = this.code[this.ip++];
        int level = this.code[this.ip++];
        int tlen = this.code[this.ip++];
        this.sprev = this.s;
        if (this.backrefMatchAtNestedLevel(ic != 0, this.regex.caseFoldFlag, level, tlen, this.ip)) {
            while (this.sprev + 1 < this.s) {
                ++this.sprev;
            }
            this.ip += tlen;
        } else {
            this.opFail();
            return;
        }
    }

    private void opNullCheckStart() {
        int mem = this.code[this.ip++];
        this.pushNullCheckStart(mem, this.s);
    }

    private void nullCheckFound() {
        switch (this.code[this.ip++]) {
            case 55: 
            case 56: {
                ++this.ip;
                break;
            }
            case 62: 
            case 63: 
            case 64: 
            case 65: {
                ++this.ip;
                break;
            }
            default: {
                throw new InternalException("unexpected bytecode (bug)");
            }
        }
    }

    private void opNullCheckEnd() {
        int mem;
        int isNull;
        if ((isNull = this.nullCheck(mem = this.code[this.ip++], this.s)) != 0) {
            this.nullCheckFound();
        }
    }

    private void opNullCheckEndMemST() {
        int mem;
        int isNull;
        if ((isNull = this.nullCheckMemSt(mem = this.code[this.ip++], this.s)) != 0) {
            if (isNull == -1) {
                this.opFail();
                return;
            }
            this.nullCheckFound();
        }
    }

    private void opJump() {
        this.ip += this.code[this.ip] + 1;
    }

    private void opPush() {
        int addr = this.code[this.ip++];
        this.pushAlt(this.ip + addr, this.s, this.sprev);
    }

    private void opPop() {
        this.popOne();
    }

    private void opPushOrJumpExact1() {
        int addr = this.code[this.ip++];
        if (this.s < this.range && this.code[this.ip] == this.chars[this.s]) {
            ++this.ip;
            this.pushAlt(this.ip + addr, this.s, this.sprev);
            return;
        }
        this.ip += addr + 1;
    }

    private void opPushIfPeekNext() {
        int addr = this.code[this.ip++];
        if (this.s < this.range && this.code[this.ip] == this.chars[this.s]) {
            ++this.ip;
            this.pushAlt(this.ip + addr, this.s, this.sprev);
            return;
        }
        ++this.ip;
    }

    private void opRepeat() {
        int mem = this.code[this.ip++];
        int addr = this.code[this.ip++];
        this.repeatStk[mem] = this.stk;
        this.pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            this.pushAlt(this.ip + addr, this.s, this.sprev);
        }
    }

    private void opRepeatNG() {
        int mem = this.code[this.ip++];
        int addr = this.code[this.ip++];
        this.repeatStk[mem] = this.stk;
        this.pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            this.pushAlt(this.ip, this.s, this.sprev);
            this.ip += addr;
        }
    }

    private void repeatInc(int mem, int si) {
        StackEntry e = this.stack[si];
        e.increaseRepeatCount();
        if (e.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                this.pushAlt(this.ip, this.s, this.sprev);
                this.ip = e.getRepeatPCode();
            } else {
                this.ip = e.getRepeatPCode();
            }
        }
        this.pushRepeatInc(si);
    }

    private void opRepeatInc() {
        int mem = this.code[this.ip++];
        int si = this.repeatStk[mem];
        this.repeatInc(mem, si);
    }

    private void opRepeatIncSG() {
        int mem = this.code[this.ip++];
        int si = this.getRepeat(mem);
        this.repeatInc(mem, si);
    }

    private void repeatIncNG(int mem, int si) {
        StackEntry e = this.stack[si];
        e.increaseRepeatCount();
        if (e.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                int pcode = e.getRepeatPCode();
                this.pushRepeatInc(si);
                this.pushAlt(pcode, this.s, this.sprev);
            } else {
                this.ip = e.getRepeatPCode();
                this.pushRepeatInc(si);
            }
        } else if (e.getRepeatCount() == this.regex.repeatRangeHi[mem]) {
            this.pushRepeatInc(si);
        }
    }

    private void opRepeatIncNG() {
        int mem = this.code[this.ip++];
        int si = this.repeatStk[mem];
        this.repeatIncNG(mem, si);
    }

    private void opRepeatIncNGSG() {
        int mem = this.code[this.ip++];
        int si = this.getRepeat(mem);
        this.repeatIncNG(mem, si);
    }

    private void opPushPos() {
        this.pushPos(this.s, this.sprev);
    }

    private void opPopPos() {
        StackEntry e = this.stack[this.posEnd()];
        this.s = e.getStatePStr();
        this.sprev = e.getStatePStrPrev();
    }

    private void opPushPosNot() {
        int addr = this.code[this.ip++];
        this.pushPosNot(this.ip + addr, this.s, this.sprev);
    }

    private void opFailPos() {
        this.popTilPosNot();
        this.opFail();
    }

    private void opPushStopBT() {
        this.pushStopBT();
    }

    private void opPopStopBT() {
        this.stopBtEnd();
    }

    private void opLookBehind() {
        int tlen = this.code[this.ip++];
        this.s = EncodingHelper.stepBack(this.str, this.s, tlen);
        if (this.s == -1) {
            this.opFail();
            return;
        }
        this.sprev = EncodingHelper.prevCharHead(this.str, this.s);
    }

    private void opPushLookBehindNot() {
        int tlen;
        int q;
        int addr = this.code[this.ip++];
        if ((q = EncodingHelper.stepBack(this.str, this.s, tlen = this.code[this.ip++])) == -1) {
            this.ip += addr;
        } else {
            this.pushLookBehindNot(this.ip + addr, this.s, this.sprev);
            this.s = q;
            this.sprev = EncodingHelper.prevCharHead(this.str, this.s);
        }
    }

    private void opFailLookBehindNot() {
        this.popTilLookBehindNot();
        this.opFail();
    }

    private void opFail() {
        if (this.stack == null) {
            this.ip = this.regex.codeLength - 1;
            return;
        }
        StackEntry e = this.pop();
        this.ip = e.getStatePCode();
        this.s = e.getStatePStr();
        this.sprev = e.getStatePStrPrev();
    }

    private int finish() {
        return this.bestLen;
    }
}

