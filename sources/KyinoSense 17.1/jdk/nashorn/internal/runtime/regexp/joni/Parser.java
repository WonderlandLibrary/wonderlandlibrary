/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ApplyCaseFold;
import jdk.nashorn.internal.runtime.regexp.joni.ApplyCaseFoldArg;
import jdk.nashorn.internal.runtime.regexp.joni.BitStatus;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.Lexer;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnyCharNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StateNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCSTATE;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCVALTYPE;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

class Parser
extends Lexer {
    protected final Regex regex;
    protected Node root;
    protected int returnCode;

    protected Parser(ScanEnvironment env, char[] chars, int p, int end) {
        super(env, chars, p, end);
        this.regex = env.reg;
    }

    protected final Node parse() {
        this.root = this.parseRegexp();
        this.regex.numMem = this.env.numMem;
        return this.root;
    }

    private boolean codeExistCheck(int code, boolean ignoreEscaped) {
        this.mark();
        boolean inEsc = false;
        while (this.left()) {
            if (ignoreEscaped && inEsc) {
                inEsc = false;
                continue;
            }
            this.fetch();
            if (this.c == code) {
                this.restore();
                return true;
            }
            if (this.c != this.syntax.metaCharTable.esc) continue;
            inEsc = true;
        }
        this.restore();
        return false;
    }

    private CClassNode parseCharClass() {
        boolean neg;
        this.fetchTokenInCC();
        if (this.token.type == TokenType.CHAR && this.token.getC() == 94 && !this.token.escaped) {
            neg = true;
            this.fetchTokenInCC();
        } else {
            neg = false;
        }
        if (this.token.type == TokenType.CC_CLOSE) {
            if (!this.codeExistCheck(93, true)) {
                throw new SyntaxException("empty char-class");
            }
            this.env.ccEscWarn("]");
            this.token.type = TokenType.CHAR;
        }
        CClassNode cc = new CClassNode();
        CClassNode prevCC = null;
        CClassNode workCC = null;
        CClassNode.CCStateArg arg = new CClassNode.CCStateArg();
        boolean andStart = false;
        arg.state = CCSTATE.START;
        while (this.token.type != TokenType.CC_CLOSE) {
            boolean fetched = false;
            switch (this.token.type) {
                case CHAR: {
                    arg.inType = this.token.getC() > 255 ? CCVALTYPE.CODE_POINT : CCVALTYPE.SB;
                    arg.v = this.token.getC();
                    arg.vIsRaw = false;
                    this.parseCharClassValEntry2(cc, arg);
                    break;
                }
                case RAW_BYTE: {
                    arg.v = this.token.getC();
                    arg.inType = CCVALTYPE.SB;
                    arg.vIsRaw = true;
                    this.parseCharClassValEntry2(cc, arg);
                    break;
                }
                case CODE_POINT: {
                    arg.v = this.token.getCode();
                    arg.vIsRaw = true;
                    this.parseCharClassValEntry(cc, arg);
                    break;
                }
                case CHAR_TYPE: {
                    cc.addCType(this.token.getPropCType(), this.token.getPropNot(), this.env, this);
                    cc.nextStateClass(arg, this.env);
                    break;
                }
                case CC_RANGE: {
                    if (arg.state == CCSTATE.VALUE) {
                        this.fetchTokenInCC();
                        fetched = true;
                        if (this.token.type == TokenType.CC_CLOSE) {
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        if (this.token.type == TokenType.CC_AND) {
                            this.env.ccEscWarn("-");
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        arg.state = CCSTATE.RANGE;
                        break;
                    }
                    if (arg.state == CCSTATE.START) {
                        arg.v = this.token.getC();
                        arg.vIsRaw = false;
                        this.fetchTokenInCC();
                        fetched = true;
                        if (this.token.type == TokenType.CC_RANGE || andStart) {
                            this.env.ccEscWarn("-");
                        }
                        this.parseCharClassValEntry(cc, arg);
                        break;
                    }
                    if (arg.state == CCSTATE.RANGE) {
                        this.env.ccEscWarn("-");
                        this.parseCharClassSbChar(cc, arg);
                        break;
                    }
                    this.fetchTokenInCC();
                    fetched = true;
                    if (this.token.type == TokenType.CC_CLOSE) {
                        this.parseCharClassRangeEndVal(cc, arg);
                        break;
                    }
                    if (this.token.type == TokenType.CC_AND) {
                        this.env.ccEscWarn("-");
                        this.parseCharClassRangeEndVal(cc, arg);
                        break;
                    }
                    if (this.syntax.allowDoubleRangeOpInCC()) {
                        this.env.ccEscWarn("-");
                        arg.inType = CCVALTYPE.SB;
                        arg.v = 45;
                        arg.vIsRaw = false;
                        this.parseCharClassValEntry2(cc, arg);
                        break;
                    }
                    throw new SyntaxException("unmatched range specifier in char-class");
                }
                case CC_CC_OPEN: {
                    CClassNode acc = this.parseCharClass();
                    cc.or(acc);
                    break;
                }
                case CC_AND: {
                    if (arg.state == CCSTATE.VALUE) {
                        arg.v = 0;
                        arg.vIsRaw = false;
                        cc.nextStateValue(arg, this.env);
                    }
                    andStart = true;
                    arg.state = CCSTATE.START;
                    if (prevCC != null) {
                        prevCC.and(cc);
                    } else {
                        prevCC = cc;
                        if (workCC == null) {
                            workCC = new CClassNode();
                        }
                        cc = workCC;
                    }
                    cc.clear();
                    break;
                }
                case EOT: {
                    throw new SyntaxException("premature end of char-class");
                }
                default: {
                    throw new InternalException("internal parser error (bug)");
                }
            }
            if (fetched) continue;
            this.fetchTokenInCC();
        }
        if (arg.state == CCSTATE.VALUE) {
            arg.v = 0;
            arg.vIsRaw = false;
            cc.nextStateValue(arg, this.env);
        }
        if (prevCC != null) {
            prevCC.and(cc);
            cc = prevCC;
        }
        if (neg) {
            cc.setNot();
        } else {
            cc.clearNot();
        }
        if (cc.isNot() && this.syntax.notNewlineInNegativeCC() && !cc.isEmpty()) {
            int NEW_LINE = 10;
            if (EncodingHelper.isNewLine(10)) {
                cc.bs.set(10);
            }
        }
        return cc;
    }

    private void parseCharClassSbChar(CClassNode cc, CClassNode.CCStateArg arg) {
        arg.inType = CCVALTYPE.SB;
        arg.v = this.token.getC();
        arg.vIsRaw = false;
        this.parseCharClassValEntry2(cc, arg);
    }

    private void parseCharClassRangeEndVal(CClassNode cc, CClassNode.CCStateArg arg) {
        arg.v = 45;
        arg.vIsRaw = false;
        this.parseCharClassValEntry(cc, arg);
    }

    private void parseCharClassValEntry(CClassNode cc, CClassNode.CCStateArg arg) {
        arg.inType = arg.v <= 255 ? CCVALTYPE.SB : CCVALTYPE.CODE_POINT;
        this.parseCharClassValEntry2(cc, arg);
    }

    private void parseCharClassValEntry2(CClassNode cc, CClassNode.CCStateArg arg) {
        cc.nextStateValue(arg, this.env);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Node parseEnclose(TokenType term) {
        Node node;
        block33: {
            int num;
            EncloseNode en;
            block32: {
                node = null;
                if (!this.left()) {
                    throw new SyntaxException("end pattern with unmatched parenthesis");
                }
                int option = this.env.option;
                if (!this.peekIs(63) || !this.syntax.op2QMarkGroupEffect()) break block32;
                this.inc();
                if (!this.left()) {
                    throw new SyntaxException("end pattern in group");
                }
                this.fetch();
                switch (this.c) {
                    case 58: {
                        this.fetchToken();
                        node = this.parseSubExp(term);
                        this.returnCode = 1;
                        return node;
                    }
                    case 61: {
                        node = new AnchorNode(1024);
                        break block33;
                    }
                    case 33: {
                        node = new AnchorNode(2048);
                        break block33;
                    }
                    case 62: {
                        node = new EncloseNode(4);
                        break block33;
                    }
                    case 39: {
                        break block33;
                    }
                    case 60: {
                        this.fetch();
                        if (this.c == 61) {
                            node = new AnchorNode(4096);
                        } else {
                            if (this.c != 33) throw new SyntaxException("undefined group option");
                            node = new AnchorNode(8192);
                        }
                        break block33;
                    }
                    case 64: {
                        if (!this.syntax.op2AtMarkCaptureHistory()) throw new SyntaxException("undefined group option");
                        en = new EncloseNode();
                        num = this.env.addMemEntry();
                        if (num >= 32) {
                            throw new ValueException("group number is too big for capture history");
                        }
                        en.regNum = num;
                        node = en;
                        break block33;
                    }
                    case 45: 
                    case 105: 
                    case 109: 
                    case 115: 
                    case 120: {
                        boolean neg = false;
                        while (true) {
                            switch (this.c) {
                                case 41: 
                                case 58: {
                                    break;
                                }
                                case 45: {
                                    neg = true;
                                    break;
                                }
                                case 120: {
                                    option = BitStatus.bsOnOff(option, 2, neg);
                                    break;
                                }
                                case 105: {
                                    option = BitStatus.bsOnOff(option, 1, neg);
                                    break;
                                }
                                case 115: {
                                    if (!this.syntax.op2OptionPerl()) throw new SyntaxException("undefined group option");
                                    option = BitStatus.bsOnOff(option, 4, neg);
                                    break;
                                }
                                case 109: {
                                    if (this.syntax.op2OptionPerl()) {
                                        option = BitStatus.bsOnOff(option, 8, !neg);
                                        break;
                                    }
                                    if (!this.syntax.op2OptionRuby()) throw new SyntaxException("undefined group option");
                                    option = BitStatus.bsOnOff(option, 4, neg);
                                    break;
                                }
                                default: {
                                    throw new SyntaxException("undefined group option");
                                }
                            }
                            if (this.c == 41) {
                                EncloseNode en2 = new EncloseNode(option, 0);
                                node = en2;
                                this.returnCode = 2;
                                return node;
                            }
                            if (this.c == 58) {
                                int prev = this.env.option;
                                this.env.option = option;
                                this.fetchToken();
                                Node target = this.parseSubExp(term);
                                this.env.option = prev;
                                EncloseNode en3 = new EncloseNode(option, 0);
                                en3.setTarget(target);
                                node = en3;
                                this.returnCode = 0;
                                return node;
                            }
                            if (!this.left()) {
                                throw new SyntaxException("end pattern in group");
                            }
                            this.fetch();
                        }
                    }
                    default: {
                        throw new SyntaxException("undefined group option");
                    }
                }
            }
            if (Option.isDontCaptureGroup(this.env.option)) {
                this.fetchToken();
                node = this.parseSubExp(term);
                this.returnCode = 1;
                return node;
            }
            en = new EncloseNode();
            en.regNum = num = this.env.addMemEntry();
            node = en;
        }
        this.fetchToken();
        Node target = this.parseSubExp(term);
        if (node.getType() == 7) {
            AnchorNode an = (AnchorNode)node;
            an.setTarget(target);
        } else {
            EncloseNode en = (EncloseNode)node;
            en.setTarget(target);
            if (en.type == 1) {
                this.env.setMemNode(en.regNum, node);
            }
        }
        this.returnCode = 0;
        return node;
    }

    private Node parseExp(TokenType term) {
        if (this.token.type == term) {
            return StringNode.EMPTY;
        }
        Node node = null;
        boolean group = false;
        block0 : switch (this.token.type) {
            case EOT: 
            case ALT: {
                return StringNode.EMPTY;
            }
            case SUBEXP_OPEN: {
                node = this.parseEnclose(TokenType.SUBEXP_CLOSE);
                if (this.returnCode == 1) {
                    group = true;
                    break;
                }
                if (this.returnCode != 2) break;
                int prev = this.env.option;
                EncloseNode en = (EncloseNode)node;
                this.env.option = en.option;
                this.fetchToken();
                Node target = this.parseSubExp(term);
                this.env.option = prev;
                en.setTarget(target);
                return node;
            }
            case SUBEXP_CLOSE: {
                if (!this.syntax.allowUnmatchedCloseSubexp()) {
                    throw new SyntaxException("unmatched close parenthesis");
                }
                if (this.token.escaped) {
                    return this.parseExpTkRawByte(group);
                }
                return this.parseExpTkByte(group);
            }
            case STRING: {
                return this.parseExpTkByte(group);
            }
            case RAW_BYTE: {
                return this.parseExpTkRawByte(group);
            }
            case CODE_POINT: {
                char[] buf = new char[]{(char)this.token.getCode()};
                node = new StringNode(buf, 0, 1);
                break;
            }
            case CHAR_TYPE: {
                switch (this.token.getPropCType()) {
                    case 260: 
                    case 265: 
                    case 268: {
                        CClassNode cc = new CClassNode();
                        cc.addCType(this.token.getPropCType(), false, this.env, this);
                        if (this.token.getPropNot()) {
                            cc.setNot();
                        }
                        node = cc;
                        break block0;
                    }
                    case 4: 
                    case 9: 
                    case 11: {
                        CClassNode ccn = new CClassNode();
                        ccn.addCType(this.token.getPropCType(), false, this.env, this);
                        if (this.token.getPropNot()) {
                            ccn.setNot();
                        }
                        node = ccn;
                        break block0;
                    }
                }
                throw new InternalException("internal parser error (bug)");
            }
            case CC_CC_OPEN: {
                CClassNode cc = this.parseCharClass();
                node = cc;
                if (!Option.isIgnoreCase(this.env.option)) break;
                ApplyCaseFoldArg arg = new ApplyCaseFoldArg(this.env, cc);
                EncodingHelper.applyAllCaseFold(this.env.caseFoldFlag, ApplyCaseFold.INSTANCE, arg);
                if (arg.altRoot == null) break;
                node = ConsAltNode.newAltNode(node, arg.altRoot);
                break;
            }
            case ANYCHAR: {
                node = new AnyCharNode();
                break;
            }
            case ANYCHAR_ANYTIME: {
                node = new AnyCharNode();
                QuantifierNode qn = new QuantifierNode(0, -1, false);
                qn.setTarget(node);
                node = qn;
                break;
            }
            case BACKREF: {
                int backRef = this.token.getBackrefRef();
                node = new BackRefNode(backRef, this.env);
                break;
            }
            case ANCHOR: {
                node = new AnchorNode(this.token.getAnchor());
                break;
            }
            case OP_REPEAT: 
            case INTERVAL: {
                if (this.syntax.contextIndepRepeatOps()) {
                    if (this.syntax.contextInvalidRepeatOps()) {
                        throw new SyntaxException("target of repeat operator is not specified");
                    }
                    node = StringNode.EMPTY;
                    break;
                }
                return this.parseExpTkByte(group);
            }
            default: {
                throw new InternalException("internal parser error (bug)");
            }
        }
        this.fetchToken();
        return this.parseExpRepeat(node, group);
    }

    private Node parseExpTkByte(boolean group) {
        StringNode node = new StringNode(this.chars, this.token.backP, this.p);
        while (true) {
            this.fetchToken();
            if (this.token.type != TokenType.STRING) break;
            if (this.token.backP == node.end) {
                node.end = this.p;
                continue;
            }
            node.cat(this.chars, this.token.backP, this.p);
        }
        return this.parseExpRepeat(node, group);
    }

    private Node parseExpTkRawByte(boolean group) {
        StringNode node = new StringNode((char)this.token.getC());
        node.setRaw();
        this.fetchToken();
        node.clearRaw();
        return this.parseExpRepeat(node, group);
    }

    private Node parseExpRepeat(Node targetp, boolean group) {
        Node target = targetp;
        while (this.token.type == TokenType.OP_REPEAT || this.token.type == TokenType.INTERVAL) {
            if (target.isInvalidQuantifier()) {
                throw new SyntaxException("target of repeat operator is invalid");
            }
            QuantifierNode qtfr = new QuantifierNode(this.token.getRepeatLower(), this.token.getRepeatUpper(), this.token.type == TokenType.INTERVAL);
            qtfr.greedy = this.token.getRepeatGreedy();
            int ret = qtfr.setQuantifier(target, group, this.env, this.chars, this.getBegin(), this.getEnd());
            StateNode qn = qtfr;
            if (this.token.getRepeatPossessive()) {
                EncloseNode en = new EncloseNode(4);
                en.setTarget(qn);
                qn = en;
            }
            if (ret == 0) {
                target = qn;
            } else if (ret == 2) {
                target = ConsAltNode.newListNode(target, null);
                ConsAltNode tmp = ((ConsAltNode)target).setCdr(ConsAltNode.newListNode(qn, null));
                this.fetchToken();
                return this.parseExpRepeatForCar(target, tmp, group);
            }
            this.fetchToken();
        }
        return target;
    }

    private Node parseExpRepeatForCar(Node top, ConsAltNode target, boolean group) {
        while (this.token.type == TokenType.OP_REPEAT || this.token.type == TokenType.INTERVAL) {
            if (target.car.isInvalidQuantifier()) {
                throw new SyntaxException("target of repeat operator is invalid");
            }
            QuantifierNode qtfr = new QuantifierNode(this.token.getRepeatLower(), this.token.getRepeatUpper(), this.token.type == TokenType.INTERVAL);
            qtfr.greedy = this.token.getRepeatGreedy();
            int ret = qtfr.setQuantifier(target.car, group, this.env, this.chars, this.getBegin(), this.getEnd());
            StateNode qn = qtfr;
            if (this.token.getRepeatPossessive()) {
                EncloseNode en = new EncloseNode(4);
                en.setTarget(qn);
                qn = en;
            }
            if (ret == 0) {
                target.setCar(qn);
            } else if (ret == 2) assert (false);
            this.fetchToken();
        }
        return top;
    }

    private Node parseBranch(TokenType term) {
        ConsAltNode top;
        Node node = this.parseExp(term);
        if (this.token.type == TokenType.EOT || this.token.type == term || this.token.type == TokenType.ALT) {
            return node;
        }
        ConsAltNode t = top = ConsAltNode.newListNode(node, null);
        while (this.token.type != TokenType.EOT && this.token.type != term && this.token.type != TokenType.ALT) {
            node = this.parseExp(term);
            if (node.getType() == 8) {
                t.setCdr((ConsAltNode)node);
                while (((ConsAltNode)node).cdr != null) {
                    node = ((ConsAltNode)node).cdr;
                }
                t = (ConsAltNode)node;
                continue;
            }
            t.setCdr(ConsAltNode.newListNode(node, null));
            t = t.cdr;
        }
        return top;
    }

    private Node parseSubExp(TokenType term) {
        Node node = this.parseBranch(term);
        if (this.token.type == term) {
            return node;
        }
        if (this.token.type == TokenType.ALT) {
            ConsAltNode top;
            ConsAltNode t = top = ConsAltNode.newAltNode(node, null);
            while (this.token.type == TokenType.ALT) {
                this.fetchToken();
                node = this.parseBranch(term);
                t.setCdr(ConsAltNode.newAltNode(node, null));
                t = t.cdr;
            }
            if (this.token.type != term) {
                Parser.parseSubExpError(term);
            }
            return top;
        }
        Parser.parseSubExpError(term);
        return null;
    }

    private static void parseSubExpError(TokenType term) {
        if (term == TokenType.SUBEXP_CLOSE) {
            throw new SyntaxException("end pattern with unmatched parenthesis");
        }
        throw new InternalException("internal parser error (bug)");
    }

    private Node parseRegexp() {
        this.fetchToken();
        return this.parseSubExp(TokenType.EOT);
    }
}

