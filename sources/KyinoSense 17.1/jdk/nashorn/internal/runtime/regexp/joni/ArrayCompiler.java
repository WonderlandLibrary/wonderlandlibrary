/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.Analyser;
import jdk.nashorn.internal.runtime.regexp.joni.BitStatus;
import jdk.nashorn.internal.runtime.regexp.joni.CodeRangeBuffer;
import jdk.nashorn.internal.runtime.regexp.joni.Compiler;
import jdk.nashorn.internal.runtime.regexp.joni.MatcherFactory;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

final class ArrayCompiler
extends Compiler {
    private int[] code;
    private int codeLength;
    private char[][] templates;
    private int templateNum;
    private static final int REPEAT_RANGE_ALLOC = 8;
    private static final int QUANTIFIER_EXPAND_LIMIT_SIZE = 50;

    ArrayCompiler(Analyser analyser) {
        super(analyser);
    }

    @Override
    protected final void prepare() {
        int codeSize = 8;
        this.code = new int[codeSize];
        this.codeLength = 0;
    }

    @Override
    protected final void finish() {
        this.addOpcode(1);
        this.addOpcode(0);
        this.regex.code = this.code;
        this.regex.codeLength = this.codeLength;
        this.regex.templates = this.templates;
        this.regex.templateNum = this.templateNum;
        this.regex.factory = MatcherFactory.DEFAULT;
    }

    @Override
    protected void compileAltNode(ConsAltNode node) {
        ConsAltNode aln = node;
        int len = 0;
        do {
            len += this.compileLengthTree(aln.car);
            if (aln.cdr == null) continue;
            len += 4;
        } while ((aln = aln.cdr) != null);
        int pos = this.codeLength + len;
        aln = node;
        do {
            len = this.compileLengthTree(aln.car);
            if (aln.cdr != null) {
                this.addOpcodeRelAddr(56, len + 2);
            }
            this.compileTree(aln.car);
            if (aln.cdr == null) continue;
            len = pos - (this.codeLength + 2);
            this.addOpcodeRelAddr(55, len);
        } while ((aln = aln.cdr) != null);
    }

    private static boolean isNeedStrLenOpExact(int op) {
        return op == 7 || op == 15;
    }

    private static boolean opTemplated(int op) {
        return ArrayCompiler.isNeedStrLenOpExact(op);
    }

    private static int selectStrOpcode(int strLength, boolean ignoreCase) {
        int op;
        if (ignoreCase) {
            switch (strLength) {
                case 1: {
                    op = 14;
                    break;
                }
                default: {
                    op = 15;
                    break;
                }
            }
        } else {
            switch (strLength) {
                case 1: {
                    op = 2;
                    break;
                }
                case 2: {
                    op = 3;
                    break;
                }
                case 3: {
                    op = 4;
                    break;
                }
                case 4: {
                    op = 5;
                    break;
                }
                case 5: {
                    op = 6;
                    break;
                }
                default: {
                    op = 7;
                }
            }
        }
        return op;
    }

    private void compileTreeEmptyCheck(Node node, int emptyInfo) {
        int savedNumNullCheck = this.regex.numNullCheck;
        if (emptyInfo != 0) {
            this.addOpcode(66);
            this.addMemNum(this.regex.numNullCheck);
            ++this.regex.numNullCheck;
        }
        this.compileTree(node);
        if (emptyInfo != 0) {
            switch (emptyInfo) {
                case 1: {
                    this.addOpcode(67);
                    break;
                }
                case 2: {
                    this.addOpcode(68);
                    break;
                }
            }
            this.addMemNum(savedNumNullCheck);
        }
    }

    private static int addCompileStringlength(char[] chars, int p, int strLength, boolean ignoreCase) {
        int op = ArrayCompiler.selectStrOpcode(strLength, ignoreCase);
        int len = 1;
        if (ArrayCompiler.opTemplated(op)) {
            len += 3;
        } else {
            if (ArrayCompiler.isNeedStrLenOpExact(op)) {
                ++len;
            }
            len += strLength;
        }
        return len;
    }

    @Override
    protected final void addCompileString(char[] chars, int p, int strLength, boolean ignoreCase) {
        int op = ArrayCompiler.selectStrOpcode(strLength, ignoreCase);
        this.addOpcode(op);
        if (ArrayCompiler.isNeedStrLenOpExact(op)) {
            this.addLength(strLength);
        }
        if (ArrayCompiler.opTemplated(op)) {
            this.addInt(this.templateNum);
            this.addInt(p);
            this.addTemplate(chars);
        } else {
            this.addChars(chars, p, strLength);
        }
    }

    private static int compileLengthStringNode(Node node) {
        int prev;
        StringNode sn = (StringNode)node;
        if (sn.length() <= 0) {
            return 0;
        }
        boolean ambig = sn.isAmbig();
        int p = prev = sn.p;
        int end = sn.end;
        char[] chars = sn.chars;
        ++p;
        int slen = 1;
        int rlen = 0;
        while (p < end) {
            ++slen;
            ++p;
        }
        int r = ArrayCompiler.addCompileStringlength(chars, prev, slen, ambig);
        return rlen += r;
    }

    private static int compileLengthStringRawNode(StringNode sn) {
        if (sn.length() <= 0) {
            return 0;
        }
        return ArrayCompiler.addCompileStringlength(sn.chars, sn.p, sn.length(), false);
    }

    private void addMultiByteCClass(CodeRangeBuffer mbuf) {
        this.addLength(mbuf.used);
        this.addInts(mbuf.p, mbuf.used);
    }

    private static int compileLengthCClassNode(CClassNode cc) {
        int len;
        if (cc.isShare()) {
            return 2;
        }
        if (cc.mbuf == null) {
            len = 9;
        } else {
            len = cc.bs.isEmpty() ? 1 : 9;
            len += 1 + cc.mbuf.used;
        }
        return len;
    }

    @Override
    protected void compileCClassNode(CClassNode cc) {
        if (cc.isShare()) {
            this.addOpcode(22);
            this.addPointer(cc);
            return;
        }
        if (cc.mbuf == null) {
            if (cc.isNot()) {
                this.addOpcode(19);
            } else {
                this.addOpcode(16);
            }
            this.addInts(cc.bs.bits, 8);
        } else if (cc.bs.isEmpty()) {
            if (cc.isNot()) {
                this.addOpcode(20);
            } else {
                this.addOpcode(17);
            }
            this.addMultiByteCClass(cc.mbuf);
        } else {
            if (cc.isNot()) {
                this.addOpcode(21);
            } else {
                this.addOpcode(18);
            }
            this.addInts(cc.bs.bits, 8);
            this.addMultiByteCClass(cc.mbuf);
        }
    }

    @Override
    protected void compileAnyCharNode() {
        if (Option.isMultiline(this.regex.options)) {
            this.addOpcode(24);
        } else {
            this.addOpcode(23);
        }
    }

    @Override
    protected void compileBackrefNode(BackRefNode node) {
        if (Option.isIgnoreCase(this.regex.options)) {
            this.addOpcode(44);
            this.addMemNum(node.backRef);
        } else {
            switch (node.backRef) {
                case 1: {
                    this.addOpcode(41);
                    break;
                }
                case 2: {
                    this.addOpcode(42);
                    break;
                }
                default: {
                    this.addOpcode(43);
                    this.addOpcode(node.backRef);
                }
            }
        }
    }

    private void entryRepeatRange(int id, int lower, int upper) {
        if (this.regex.repeatRangeLo == null) {
            this.regex.repeatRangeLo = new int[8];
            this.regex.repeatRangeHi = new int[8];
        } else if (id >= this.regex.repeatRangeLo.length) {
            int[] tmp = new int[this.regex.repeatRangeLo.length + 8];
            System.arraycopy(this.regex.repeatRangeLo, 0, tmp, 0, this.regex.repeatRangeLo.length);
            this.regex.repeatRangeLo = tmp;
            tmp = new int[this.regex.repeatRangeHi.length + 8];
            System.arraycopy(this.regex.repeatRangeHi, 0, tmp, 0, this.regex.repeatRangeHi.length);
            this.regex.repeatRangeHi = tmp;
        }
        this.regex.repeatRangeLo[id] = lower;
        this.regex.repeatRangeHi[id] = QuantifierNode.isRepeatInfinite(upper) ? Integer.MAX_VALUE : upper;
    }

    private void compileRangeRepeatNode(QuantifierNode qn, int targetLen, int emptyInfo) {
        int numRepeat = this.regex.numRepeat;
        this.addOpcode(qn.greedy ? 60 : 61);
        this.addMemNum(numRepeat);
        ++this.regex.numRepeat;
        this.addRelAddr(targetLen + 2);
        this.entryRepeatRange(numRepeat, qn.lower, qn.upper);
        this.compileTreeEmptyCheck(qn.target, emptyInfo);
        if (qn.isInRepeat()) {
            this.addOpcode(qn.greedy ? 64 : 65);
        } else {
            this.addOpcode(qn.greedy ? 62 : 63);
        }
        this.addMemNum(numRepeat);
    }

    private static boolean cknOn(int ckn) {
        return ckn > 0;
    }

    private int compileNonCECLengthQuantifierNode(QuantifierNode qn) {
        int len;
        boolean infinite = QuantifierNode.isRepeatInfinite(qn.upper);
        int emptyInfo = qn.targetEmptyInfo;
        int tlen = this.compileLengthTree(qn.target);
        if (qn.target.getType() == 3 && qn.greedy && infinite) {
            if (qn.nextHeadExact != null) {
                return 2 + tlen * qn.lower;
            }
            return 1 + tlen * qn.lower;
        }
        int modTLen = 0;
        modTLen = emptyInfo != 0 ? tlen + 4 : tlen;
        if (infinite && (qn.lower <= 1 || tlen * qn.lower <= 50)) {
            len = qn.lower == 1 && tlen > 50 ? 2 : tlen * qn.lower;
            len = qn.greedy ? (qn.headExact != null ? (len += 3 + modTLen + 2) : (qn.nextHeadExact != null ? (len += 3 + modTLen + 2) : (len += 2 + modTLen + 2))) : (len += 2 + modTLen + 2);
        } else if (qn.upper == 0 && qn.isRefered) {
            len = 2 + tlen;
        } else if (!infinite && qn.greedy && (qn.upper == 1 || (tlen + 2) * qn.upper <= 50)) {
            len = tlen * qn.lower;
            len += (2 + tlen) * (qn.upper - qn.lower);
        } else {
            len = !qn.greedy && qn.upper == 1 && qn.lower == 0 ? 4 + tlen : 2 + modTLen + 1 + 1 + 1;
        }
        return len;
    }

    @Override
    protected void compileNonCECQuantifierNode(QuantifierNode qn) {
        boolean infinite = QuantifierNode.isRepeatInfinite(qn.upper);
        int emptyInfo = qn.targetEmptyInfo;
        int tlen = this.compileLengthTree(qn.target);
        if (qn.isAnyCharStar()) {
            this.compileTreeNTimes(qn.target, qn.lower);
            if (qn.nextHeadExact != null) {
                if (Option.isMultiline(this.regex.options)) {
                    this.addOpcode(28);
                } else {
                    this.addOpcode(27);
                }
                StringNode sn = (StringNode)qn.nextHeadExact;
                this.addChars(sn.chars, sn.p, 1);
                return;
            }
            if (Option.isMultiline(this.regex.options)) {
                this.addOpcode(26);
            } else {
                this.addOpcode(25);
            }
            return;
        }
        int modTLen = emptyInfo != 0 ? tlen + 4 : tlen;
        if (infinite && (qn.lower <= 1 || tlen * qn.lower <= 50)) {
            if (qn.lower == 1 && tlen > 50) {
                if (qn.greedy) {
                    if (qn.headExact != null) {
                        this.addOpcodeRelAddr(55, 3);
                    } else if (qn.nextHeadExact != null) {
                        this.addOpcodeRelAddr(55, 3);
                    } else {
                        this.addOpcodeRelAddr(55, 2);
                    }
                } else {
                    this.addOpcodeRelAddr(55, 2);
                }
            } else {
                this.compileTreeNTimes(qn.target, qn.lower);
            }
            if (qn.greedy) {
                if (qn.headExact != null) {
                    this.addOpcodeRelAddr(58, modTLen + 2);
                    StringNode sn = (StringNode)qn.headExact;
                    this.addChars(sn.chars, sn.p, 1);
                    this.compileTreeEmptyCheck(qn.target, emptyInfo);
                    this.addOpcodeRelAddr(55, -(modTLen + 2 + 3));
                } else if (qn.nextHeadExact != null) {
                    this.addOpcodeRelAddr(59, modTLen + 2);
                    StringNode sn = (StringNode)qn.nextHeadExact;
                    this.addChars(sn.chars, sn.p, 1);
                    this.compileTreeEmptyCheck(qn.target, emptyInfo);
                    this.addOpcodeRelAddr(55, -(modTLen + 2 + 3));
                } else {
                    this.addOpcodeRelAddr(56, modTLen + 2);
                    this.compileTreeEmptyCheck(qn.target, emptyInfo);
                    this.addOpcodeRelAddr(55, -(modTLen + 2 + 2));
                }
            } else {
                this.addOpcodeRelAddr(55, modTLen);
                this.compileTreeEmptyCheck(qn.target, emptyInfo);
                this.addOpcodeRelAddr(56, -(modTLen + 2));
            }
        } else if (qn.upper == 0 && qn.isRefered) {
            this.addOpcodeRelAddr(55, tlen);
            this.compileTree(qn.target);
        } else if (!infinite && qn.greedy && (qn.upper == 1 || (tlen + 2) * qn.upper <= 50)) {
            int n = qn.upper - qn.lower;
            this.compileTreeNTimes(qn.target, qn.lower);
            for (int i = 0; i < n; ++i) {
                this.addOpcodeRelAddr(56, (n - i) * tlen + (n - i - 1) * 2);
                this.compileTree(qn.target);
            }
        } else if (!qn.greedy && qn.upper == 1 && qn.lower == 0) {
            this.addOpcodeRelAddr(56, 2);
            this.addOpcodeRelAddr(55, tlen);
            this.compileTree(qn.target);
        } else {
            this.compileRangeRepeatNode(qn, modTLen, emptyInfo);
        }
    }

    private int compileLengthOptionNode(EncloseNode node) {
        int prev = this.regex.options;
        this.regex.options = node.option;
        int tlen = this.compileLengthTree(node.target);
        this.regex.options = prev;
        if (Option.isDynamic(prev ^ node.option)) {
            return 5 + tlen + 2;
        }
        return tlen;
    }

    @Override
    protected void compileOptionNode(EncloseNode node) {
        int prev = this.regex.options;
        if (Option.isDynamic(prev ^ node.option)) {
            this.addOpcodeOption(86, node.option);
            this.addOpcodeOption(87, prev);
            this.addOpcode(54);
        }
        this.regex.options = node.option;
        this.compileTree(node.target);
        this.regex.options = prev;
        if (Option.isDynamic(prev ^ node.option)) {
            this.addOpcodeOption(87, prev);
        }
    }

    private int compileLengthEncloseNode(EncloseNode node) {
        int len;
        if (node.isOption()) {
            return this.compileLengthOptionNode(node);
        }
        int tlen = node.target != null ? this.compileLengthTree(node.target) : 0;
        switch (node.type) {
            case 1: {
                len = BitStatus.bsAt(this.regex.btMemStart, node.regNum) ? 2 : 2;
                len += tlen + (BitStatus.bsAt(this.regex.btMemEnd, node.regNum) ? 2 : 2);
                break;
            }
            case 4: {
                if (node.isStopBtSimpleRepeat()) {
                    QuantifierNode qn = (QuantifierNode)node.target;
                    tlen = this.compileLengthTree(qn.target);
                    len = tlen * qn.lower + 2 + tlen + 1 + 2;
                    break;
                }
                len = 1 + tlen + 1;
                break;
            }
            default: {
                this.newInternalException("internal parser error (bug)");
                return 0;
            }
        }
        return len;
    }

    @Override
    protected void compileEncloseNode(EncloseNode node) {
        switch (node.type) {
            case 1: {
                if (BitStatus.bsAt(this.regex.btMemStart, node.regNum)) {
                    this.addOpcode(49);
                } else {
                    this.addOpcode(48);
                }
                this.addMemNum(node.regNum);
                this.compileTree(node.target);
                if (BitStatus.bsAt(this.regex.btMemEnd, node.regNum)) {
                    this.addOpcode(50);
                } else {
                    this.addOpcode(52);
                }
                this.addMemNum(node.regNum);
                break;
            }
            case 4: {
                if (node.isStopBtSimpleRepeat()) {
                    QuantifierNode qn = (QuantifierNode)node.target;
                    this.compileTreeNTimes(qn.target, qn.lower);
                    int len = this.compileLengthTree(qn.target);
                    this.addOpcodeRelAddr(56, len + 1 + 2);
                    this.compileTree(qn.target);
                    this.addOpcode(57);
                    this.addOpcodeRelAddr(55, -(2 + len + 1 + 2));
                    break;
                }
                this.addOpcode(74);
                this.compileTree(node.target);
                this.addOpcode(75);
                break;
            }
            default: {
                this.newInternalException("internal parser error (bug)");
            }
        }
    }

    private int compileLengthAnchorNode(AnchorNode node) {
        int len;
        int tlen = node.target != null ? this.compileLengthTree(node.target) : 0;
        switch (node.type) {
            case 1024: {
                len = 1 + tlen + 1;
                break;
            }
            case 2048: {
                len = 2 + tlen + 1;
                break;
            }
            case 4096: {
                len = 2 + tlen;
                break;
            }
            case 8192: {
                len = 3 + tlen + 1;
                break;
            }
            default: {
                len = 1;
            }
        }
        return len;
    }

    @Override
    protected void compileAnchorNode(AnchorNode node) {
        switch (node.type) {
            case 1: {
                this.addOpcode(35);
                break;
            }
            case 8: {
                this.addOpcode(36);
                break;
            }
            case 2: {
                this.addOpcode(37);
                break;
            }
            case 32: {
                this.addOpcode(38);
                break;
            }
            case 16: {
                this.addOpcode(39);
                break;
            }
            case 4: {
                this.addOpcode(40);
                break;
            }
            case 64: {
                this.addOpcode(31);
                break;
            }
            case 128: {
                this.addOpcode(32);
                break;
            }
            case 256: {
                this.addOpcode(33);
                break;
            }
            case 512: {
                this.addOpcode(34);
                break;
            }
            case 1024: {
                this.addOpcode(70);
                this.compileTree(node.target);
                this.addOpcode(71);
                break;
            }
            case 2048: {
                int len = this.compileLengthTree(node.target);
                this.addOpcodeRelAddr(72, len + 1);
                this.compileTree(node.target);
                this.addOpcode(73);
                break;
            }
            case 4096: {
                int n;
                this.addOpcode(76);
                if (node.charLength < 0) {
                    n = this.analyser.getCharLengthTree(node.target);
                    if (this.analyser.returnCode != 0) {
                        this.newSyntaxException("invalid pattern in look-behind");
                    }
                } else {
                    n = node.charLength;
                }
                this.addLength(n);
                this.compileTree(node.target);
                break;
            }
            case 8192: {
                int n;
                int len = this.compileLengthTree(node.target);
                this.addOpcodeRelAddr(77, len + 1);
                if (node.charLength < 0) {
                    n = this.analyser.getCharLengthTree(node.target);
                    if (this.analyser.returnCode != 0) {
                        this.newSyntaxException("invalid pattern in look-behind");
                    }
                } else {
                    n = node.charLength;
                }
                this.addLength(n);
                this.compileTree(node.target);
                this.addOpcode(78);
                break;
            }
            default: {
                this.newInternalException("internal parser error (bug)");
            }
        }
    }

    private int compileLengthTree(Node node) {
        int len = 0;
        switch (node.getType()) {
            case 8: {
                ConsAltNode lin = (ConsAltNode)node;
                do {
                    len += this.compileLengthTree(lin.car);
                } while ((lin = lin.cdr) != null);
                break;
            }
            case 9: {
                ConsAltNode aln = (ConsAltNode)node;
                int n = 0;
                do {
                    len += this.compileLengthTree(aln.car);
                    ++n;
                } while ((aln = aln.cdr) != null);
                len += 4 * (n - 1);
                break;
            }
            case 0: {
                StringNode sn = (StringNode)node;
                if (sn.isRaw()) {
                    len = ArrayCompiler.compileLengthStringRawNode(sn);
                    break;
                }
                len = ArrayCompiler.compileLengthStringNode(sn);
                break;
            }
            case 1: {
                len = ArrayCompiler.compileLengthCClassNode((CClassNode)node);
                break;
            }
            case 2: 
            case 3: {
                len = 1;
                break;
            }
            case 4: {
                BackRefNode br = (BackRefNode)node;
                len = !Option.isIgnoreCase(this.regex.options) && br.backRef <= 2 ? 1 : 2;
                break;
            }
            case 5: {
                len = this.compileNonCECLengthQuantifierNode((QuantifierNode)node);
                break;
            }
            case 6: {
                len = this.compileLengthEncloseNode((EncloseNode)node);
                break;
            }
            case 7: {
                len = this.compileLengthAnchorNode((AnchorNode)node);
                break;
            }
            default: {
                this.newInternalException("internal parser error (bug)");
            }
        }
        return len;
    }

    private void ensure(int size) {
        if (size >= this.code.length) {
            int length;
            for (length = this.code.length << 1; length <= size; length <<= 1) {
            }
            int[] tmp = new int[length];
            System.arraycopy(this.code, 0, tmp, 0, this.code.length);
            this.code = tmp;
        }
    }

    private void addInt(int i) {
        if (this.codeLength >= this.code.length) {
            int[] tmp = new int[this.code.length << 1];
            System.arraycopy(this.code, 0, tmp, 0, this.code.length);
            this.code = tmp;
        }
        this.code[this.codeLength++] = i;
    }

    void setInt(int i, int offset) {
        this.ensure(offset);
        this.regex.code[offset] = i;
    }

    private void addObject(Object o) {
        if (this.regex.operands == null) {
            this.regex.operands = new Object[4];
        } else if (this.regex.operandLength >= this.regex.operands.length) {
            Object[] tmp = new Object[this.regex.operands.length << 1];
            System.arraycopy(this.regex.operands, 0, tmp, 0, this.regex.operands.length);
            this.regex.operands = tmp;
        }
        this.addInt(this.regex.operandLength);
        this.regex.operands[this.regex.operandLength++] = o;
    }

    private void addChars(char[] chars, int pp, int length) {
        this.ensure(this.codeLength + length);
        int p = pp;
        int end = p + length;
        while (p < end) {
            this.code[this.codeLength++] = chars[p++];
        }
    }

    private void addInts(int[] ints, int length) {
        this.ensure(this.codeLength + length);
        System.arraycopy(ints, 0, this.code, this.codeLength, length);
        this.codeLength += length;
    }

    private void addOpcode(int opcode) {
        this.addInt(opcode);
        switch (opcode) {
            case 25: 
            case 26: 
            case 27: 
            case 28: 
            case 49: 
            case 50: 
            case 51: 
            case 53: 
            case 56: 
            case 58: 
            case 59: 
            case 60: 
            case 61: 
            case 63: 
            case 64: 
            case 65: 
            case 66: 
            case 69: 
            case 70: 
            case 72: 
            case 74: 
            case 77: 
            case 79: 
            case 80: 
            case 81: 
            case 82: 
            case 83: 
            case 84: 
            case 85: {
                this.regex.stackNeeded = true;
                break;
            }
        }
    }

    private void addStateCheckNum(int num) {
        this.addInt(num);
    }

    private void addRelAddr(int addr) {
        this.addInt(addr);
    }

    private void addAbsAddr(int addr) {
        this.addInt(addr);
    }

    private void addLength(int length) {
        this.addInt(length);
    }

    private void addMemNum(int num) {
        this.addInt(num);
    }

    private void addPointer(Object o) {
        this.addObject(o);
    }

    private void addOption(int option) {
        this.addInt(option);
    }

    private void addOpcodeRelAddr(int opcode, int addr) {
        this.addOpcode(opcode);
        this.addRelAddr(addr);
    }

    private void addOpcodeOption(int opcode, int option) {
        this.addOpcode(opcode);
        this.addOption(option);
    }

    private void addTemplate(char[] chars) {
        if (this.templateNum == 0) {
            this.templates = new char[2][];
        } else if (this.templateNum == this.templates.length) {
            char[][] tmp = new char[this.templateNum * 2][];
            System.arraycopy(this.templates, 0, tmp, 0, this.templateNum);
            this.templates = tmp;
        }
        this.templates[this.templateNum++] = chars;
    }
}

