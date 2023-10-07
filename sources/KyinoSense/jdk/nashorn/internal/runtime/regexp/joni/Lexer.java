/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.ScannerSupport;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import jdk.nashorn.internal.runtime.regexp.joni.Token;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

class Lexer
extends ScannerSupport {
    protected final ScanEnvironment env;
    protected final Syntax syntax;
    protected final Token token = new Token();

    protected Lexer(ScanEnvironment env, char[] chars, int p, int end) {
        super(chars, p, end);
        this.env = env;
        this.syntax = env.syntax;
    }

    private int fetchRangeQuantifier() {
        int up;
        int low;
        this.mark();
        boolean synAllow = this.syntax.allowInvalidInterval();
        if (!this.left()) {
            if (synAllow) {
                return 1;
            }
            throw new SyntaxException("end pattern at left brace");
        }
        if (!synAllow) {
            this.c = this.peek();
            if (this.c == 41 || this.c == 40 || this.c == 124) {
                throw new SyntaxException("end pattern at left brace");
            }
        }
        if ((low = this.scanUnsignedNumber()) < 0) {
            throw new SyntaxException("too big number for repeat range");
        }
        if (low > 100000) {
            throw new SyntaxException("too big number for repeat range");
        }
        boolean nonLow = false;
        if (this.p == this._p) {
            if (this.syntax.allowIntervalLowAbbrev()) {
                low = 0;
                nonLow = true;
            } else {
                return this.invalidRangeQuantifier(synAllow);
            }
        }
        if (!this.left()) {
            return this.invalidRangeQuantifier(synAllow);
        }
        this.fetch();
        int ret = 0;
        if (this.c == 44) {
            int prev = this.p;
            up = this.scanUnsignedNumber();
            if (up < 0) {
                throw new ValueException("too big number for repeat range");
            }
            if (up > 100000) {
                throw new ValueException("too big number for repeat range");
            }
            if (this.p == prev) {
                if (nonLow) {
                    return this.invalidRangeQuantifier(synAllow);
                }
                up = -1;
            }
        } else {
            if (nonLow) {
                return this.invalidRangeQuantifier(synAllow);
            }
            this.unfetch();
            up = low;
            ret = 2;
        }
        if (!this.left()) {
            return this.invalidRangeQuantifier(synAllow);
        }
        this.fetch();
        if (this.syntax.opEscBraceInterval()) {
            if (this.c != this.syntax.metaCharTable.esc) {
                return this.invalidRangeQuantifier(synAllow);
            }
            this.fetch();
        }
        if (this.c != 125) {
            return this.invalidRangeQuantifier(synAllow);
        }
        if (!QuantifierNode.isRepeatInfinite(up) && low > up) {
            throw new ValueException("upper is smaller than lower in repeat range");
        }
        this.token.type = TokenType.INTERVAL;
        this.token.setRepeatLower(low);
        this.token.setRepeatUpper(up);
        return ret;
    }

    private int invalidRangeQuantifier(boolean synAllow) {
        if (synAllow) {
            this.restore();
            return 1;
        }
        throw new SyntaxException("invalid repeat range {lower,upper}");
    }

    private int fetchEscapedValue() {
        if (!this.left()) {
            throw new SyntaxException("end pattern at escape");
        }
        this.fetch();
        switch (this.c) {
            case 77: {
                if (this.syntax.op2EscCapitalMBarMeta()) {
                    if (!this.left()) {
                        throw new SyntaxException("end pattern at meta");
                    }
                    this.fetch();
                    if (this.c != 45) {
                        throw new SyntaxException("invalid meta-code syntax");
                    }
                    if (!this.left()) {
                        throw new SyntaxException("end pattern at meta");
                    }
                    this.fetch();
                    if (this.c == this.syntax.metaCharTable.esc) {
                        this.c = this.fetchEscapedValue();
                    }
                    this.c = this.c & 0xFF | 0x80;
                    break;
                }
                this.fetchEscapedValueBackSlash();
                break;
            }
            case 67: {
                if (this.syntax.op2EscCapitalCBarControl()) {
                    if (!this.left()) {
                        throw new SyntaxException("end pattern at control");
                    }
                    this.fetch();
                    if (this.c != 45) {
                        throw new SyntaxException("invalid control-code syntax");
                    }
                    this.fetchEscapedValueControl();
                    break;
                }
                this.fetchEscapedValueBackSlash();
                break;
            }
            case 99: {
                if (this.syntax.opEscCControl()) {
                    this.fetchEscapedValueControl();
                }
            }
            default: {
                this.fetchEscapedValueBackSlash();
            }
        }
        return this.c;
    }

    private void fetchEscapedValueBackSlash() {
        this.c = this.env.convertBackslashValue(this.c);
    }

    private void fetchEscapedValueControl() {
        if (!this.left()) {
            throw new SyntaxException("end pattern at control");
        }
        this.fetch();
        if (this.c == 63) {
            this.c = 127;
        } else {
            if (this.c == this.syntax.metaCharTable.esc) {
                this.c = this.fetchEscapedValue();
            }
            this.c &= 0x9F;
        }
    }

    private void fetchTokenInCCFor_charType(boolean flag, int type) {
        this.token.type = TokenType.CHAR_TYPE;
        this.token.setPropCType(type);
        this.token.setPropNot(flag);
    }

    private void fetchTokenInCCFor_x() {
        if (!this.left()) {
            return;
        }
        int last = this.p;
        if (this.peekIs(123) && this.syntax.opEscXBraceHex8()) {
            int c2;
            this.inc();
            int num = this.scanUnsignedHexadecimalNumber(8);
            if (num < 0) {
                throw new ValueException("too big wide-char value");
            }
            if (this.left() && EncodingHelper.isXDigit(c2 = this.peek())) {
                throw new ValueException("too long wide-char value");
            }
            if (this.p > last + 1 && this.left() && this.peekIs(125)) {
                this.inc();
                this.token.type = TokenType.CODE_POINT;
                this.token.setCode(num);
            } else {
                this.p = last;
            }
        } else if (this.syntax.opEscXHex2()) {
            int num = this.scanUnsignedHexadecimalNumber(2);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.RAW_BYTE;
            this.token.setC(num);
        }
    }

    private void fetchTokenInCCFor_u() {
        if (!this.left()) {
            return;
        }
        int last = this.p;
        if (this.syntax.op2EscUHex4()) {
            int num = this.scanUnsignedHexadecimalNumber(4);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.CODE_POINT;
            this.token.setCode(num);
        }
    }

    private void fetchTokenInCCFor_digit() {
        if (this.syntax.opEscOctal3()) {
            this.unfetch();
            int last = this.p;
            int num = this.scanUnsignedOctalNumber(3);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.RAW_BYTE;
            this.token.setC(num);
        }
    }

    private void fetchTokenInCCFor_and() {
        if (this.syntax.op2CClassSetOp() && this.left() && this.peekIs(38)) {
            this.inc();
            this.token.type = TokenType.CC_AND;
        }
    }

    protected final TokenType fetchTokenInCC() {
        block21: {
            block23: {
                block22: {
                    block20: {
                        if (!this.left()) {
                            this.token.type = TokenType.EOT;
                            return this.token.type;
                        }
                        this.fetch();
                        this.token.type = TokenType.CHAR;
                        this.token.setC(this.c);
                        this.token.escaped = false;
                        if (this.c != 93) break block20;
                        this.token.type = TokenType.CC_CLOSE;
                        break block21;
                    }
                    if (this.c != 45) break block22;
                    this.token.type = TokenType.CC_RANGE;
                    break block21;
                }
                if (this.c != this.syntax.metaCharTable.esc) break block23;
                if (!this.syntax.backSlashEscapeInCC()) {
                    return this.token.type;
                }
                if (!this.left()) {
                    throw new SyntaxException("end pattern at escape");
                }
                this.fetch();
                this.token.escaped = true;
                this.token.setC(this.c);
                switch (this.c) {
                    case 119: {
                        this.fetchTokenInCCFor_charType(false, 268);
                        break;
                    }
                    case 87: {
                        this.fetchTokenInCCFor_charType(true, 268);
                        break;
                    }
                    case 100: {
                        this.fetchTokenInCCFor_charType(false, 260);
                        break;
                    }
                    case 68: {
                        this.fetchTokenInCCFor_charType(true, 260);
                        break;
                    }
                    case 115: {
                        this.fetchTokenInCCFor_charType(false, 265);
                        break;
                    }
                    case 83: {
                        this.fetchTokenInCCFor_charType(true, 265);
                        break;
                    }
                    case 104: {
                        if (this.syntax.op2EscHXDigit()) {
                            this.fetchTokenInCCFor_charType(false, 11);
                            break;
                        }
                        break block21;
                    }
                    case 72: {
                        if (this.syntax.op2EscHXDigit()) {
                            this.fetchTokenInCCFor_charType(true, 11);
                            break;
                        }
                        break block21;
                    }
                    case 120: {
                        this.fetchTokenInCCFor_x();
                        break;
                    }
                    case 117: {
                        this.fetchTokenInCCFor_u();
                        break;
                    }
                    case 48: 
                    case 49: 
                    case 50: 
                    case 51: 
                    case 52: 
                    case 53: 
                    case 54: 
                    case 55: {
                        this.fetchTokenInCCFor_digit();
                        break;
                    }
                    default: {
                        this.unfetch();
                        int num = this.fetchEscapedValue();
                        if (this.token.getC() != num) {
                            this.token.setCode(num);
                            this.token.type = TokenType.CODE_POINT;
                            break;
                        }
                        break block21;
                    }
                }
                break block21;
            }
            if (this.c == 38) {
                this.fetchTokenInCCFor_and();
            }
        }
        return this.token.type;
    }

    private void fetchTokenFor_repeat(int lower, int upper) {
        this.token.type = TokenType.OP_REPEAT;
        this.token.setRepeatLower(lower);
        this.token.setRepeatUpper(upper);
        this.greedyCheck();
    }

    private void fetchTokenFor_openBrace() {
        switch (this.fetchRangeQuantifier()) {
            case 0: {
                this.greedyCheck();
                break;
            }
            case 2: {
                if (this.syntax.fixedIntervalIsGreedyOnly()) {
                    this.possessiveCheck();
                    break;
                }
                this.greedyCheck();
                break;
            }
        }
    }

    private void fetchTokenFor_anchor(int subType) {
        this.token.type = TokenType.ANCHOR;
        this.token.setAnchor(subType);
    }

    private void fetchTokenFor_xBrace() {
        if (!this.left()) {
            return;
        }
        int last = this.p;
        if (this.peekIs(123) && this.syntax.opEscXBraceHex8()) {
            this.inc();
            int num = this.scanUnsignedHexadecimalNumber(8);
            if (num < 0) {
                throw new ValueException("too big wide-char value");
            }
            if (this.left() && EncodingHelper.isXDigit(this.peek())) {
                throw new ValueException("too long wide-char value");
            }
            if (this.p > last + 1 && this.left() && this.peekIs(125)) {
                this.inc();
                this.token.type = TokenType.CODE_POINT;
                this.token.setCode(num);
            } else {
                this.p = last;
            }
        } else if (this.syntax.opEscXHex2()) {
            int num = this.scanUnsignedHexadecimalNumber(2);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.RAW_BYTE;
            this.token.setC(num);
        }
    }

    private void fetchTokenFor_uHex() {
        if (!this.left()) {
            return;
        }
        int last = this.p;
        if (this.syntax.op2EscUHex4()) {
            int num = this.scanUnsignedHexadecimalNumber(4);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.CODE_POINT;
            this.token.setCode(num);
        }
    }

    private void fetchTokenFor_digit() {
        this.unfetch();
        int last = this.p;
        int num = this.scanUnsignedNumber();
        if (num >= 0 && num <= 1000 && this.syntax.opDecimalBackref() && (num <= this.env.numMem || num <= 9)) {
            if (this.syntax.strictCheckBackref() && (num > this.env.numMem || this.env.memNodes == null || this.env.memNodes[num] == null)) {
                throw new ValueException("invalid backref number");
            }
            this.token.type = TokenType.BACKREF;
            this.token.setBackrefRef(num);
            return;
        }
        if (this.c == 56 || this.c == 57) {
            this.p = last;
            this.inc();
            return;
        }
        this.p = last;
        this.fetchTokenFor_zero();
    }

    private void fetchTokenFor_zero() {
        if (this.syntax.opEscOctal3()) {
            int last = this.p;
            int num = this.scanUnsignedOctalNumber(this.c == 48 ? 2 : 3);
            if (num < 0) {
                throw new ValueException("too big number");
            }
            if (this.p == last) {
                num = 0;
            }
            this.token.type = TokenType.RAW_BYTE;
            this.token.setC(num);
        } else if (this.c != 48) {
            this.inc();
        }
    }

    private void fetchTokenFor_metaChars() {
        if (this.c == this.syntax.metaCharTable.anyChar) {
            this.token.type = TokenType.ANYCHAR;
        } else if (this.c == this.syntax.metaCharTable.anyTime) {
            this.fetchTokenFor_repeat(0, -1);
        } else if (this.c == this.syntax.metaCharTable.zeroOrOneTime) {
            this.fetchTokenFor_repeat(0, 1);
        } else if (this.c == this.syntax.metaCharTable.oneOrMoreTime) {
            this.fetchTokenFor_repeat(1, -1);
        } else if (this.c == this.syntax.metaCharTable.anyCharAnyTime) {
            this.token.type = TokenType.ANYCHAR_ANYTIME;
        }
    }

    protected final TokenType fetchToken() {
        block47: while (true) {
            if (!this.left()) {
                this.token.type = TokenType.EOT;
                return this.token.type;
            }
            this.token.type = TokenType.STRING;
            this.token.backP = this.p;
            this.fetch();
            if (this.c == this.syntax.metaCharTable.esc && !this.syntax.op2IneffectiveEscape()) {
                if (!this.left()) {
                    throw new SyntaxException("end pattern at escape");
                }
                this.token.backP = this.p;
                this.fetch();
                this.token.setC(this.c);
                this.token.escaped = true;
                switch (this.c) {
                    case 42: {
                        if (!this.syntax.opEscAsteriskZeroInf()) break block47;
                        this.fetchTokenFor_repeat(0, -1);
                        break;
                    }
                    case 43: {
                        if (!this.syntax.opEscPlusOneInf()) break block47;
                        this.fetchTokenFor_repeat(1, -1);
                        break;
                    }
                    case 63: {
                        if (!this.syntax.opEscQMarkZeroOne()) break block47;
                        this.fetchTokenFor_repeat(0, 1);
                        break;
                    }
                    case 123: {
                        if (!this.syntax.opEscBraceInterval()) break block47;
                        this.fetchTokenFor_openBrace();
                        break;
                    }
                    case 124: {
                        if (!this.syntax.opEscVBarAlt()) break block47;
                        this.token.type = TokenType.ALT;
                        break;
                    }
                    case 40: {
                        if (!this.syntax.opEscLParenSubexp()) break block47;
                        this.token.type = TokenType.SUBEXP_OPEN;
                        break;
                    }
                    case 41: {
                        if (!this.syntax.opEscLParenSubexp()) break block47;
                        this.token.type = TokenType.SUBEXP_CLOSE;
                        break;
                    }
                    case 119: {
                        if (!this.syntax.opEscWWord()) break block47;
                        this.fetchTokenInCCFor_charType(false, 268);
                        break;
                    }
                    case 87: {
                        if (!this.syntax.opEscWWord()) break block47;
                        this.fetchTokenInCCFor_charType(true, 268);
                        break;
                    }
                    case 98: {
                        if (!this.syntax.opEscBWordBound()) break block47;
                        this.fetchTokenFor_anchor(64);
                        break;
                    }
                    case 66: {
                        if (!this.syntax.opEscBWordBound()) break block47;
                        this.fetchTokenFor_anchor(128);
                        break;
                    }
                    case 60: {
                        if (!this.syntax.opEscLtGtWordBeginEnd()) break block47;
                        this.fetchTokenFor_anchor(256);
                        break;
                    }
                    case 62: {
                        if (!this.syntax.opEscLtGtWordBeginEnd()) break block47;
                        this.fetchTokenFor_anchor(512);
                        break;
                    }
                    case 115: {
                        if (!this.syntax.opEscSWhiteSpace()) break block47;
                        this.fetchTokenInCCFor_charType(false, 265);
                        break;
                    }
                    case 83: {
                        if (!this.syntax.opEscSWhiteSpace()) break block47;
                        this.fetchTokenInCCFor_charType(true, 265);
                        break;
                    }
                    case 100: {
                        if (!this.syntax.opEscDDigit()) break block47;
                        this.fetchTokenInCCFor_charType(false, 260);
                        break;
                    }
                    case 68: {
                        if (!this.syntax.opEscDDigit()) break block47;
                        this.fetchTokenInCCFor_charType(true, 260);
                        break;
                    }
                    case 104: {
                        if (!this.syntax.op2EscHXDigit()) break block47;
                        this.fetchTokenInCCFor_charType(false, 11);
                        break;
                    }
                    case 72: {
                        if (!this.syntax.op2EscHXDigit()) break block47;
                        this.fetchTokenInCCFor_charType(true, 11);
                        break;
                    }
                    case 65: {
                        if (!this.syntax.opEscAZBufAnchor()) break block47;
                        this.fetchTokenFor_anchor(1);
                        break;
                    }
                    case 90: {
                        if (!this.syntax.opEscAZBufAnchor()) break block47;
                        this.fetchTokenFor_anchor(16);
                        break;
                    }
                    case 122: {
                        if (!this.syntax.opEscAZBufAnchor()) break block47;
                        this.fetchTokenFor_anchor(8);
                        break;
                    }
                    case 71: {
                        if (!this.syntax.opEscCapitalGBeginAnchor()) break block47;
                        this.fetchTokenFor_anchor(4);
                        break;
                    }
                    case 96: {
                        if (!this.syntax.op2EscGnuBufAnchor()) break block47;
                        this.fetchTokenFor_anchor(1);
                        break;
                    }
                    case 39: {
                        if (!this.syntax.op2EscGnuBufAnchor()) break block47;
                        this.fetchTokenFor_anchor(8);
                        break;
                    }
                    case 120: {
                        this.fetchTokenFor_xBrace();
                        break;
                    }
                    case 117: {
                        this.fetchTokenFor_uHex();
                        break;
                    }
                    case 49: 
                    case 50: 
                    case 51: 
                    case 52: 
                    case 53: 
                    case 54: 
                    case 55: 
                    case 56: 
                    case 57: {
                        this.fetchTokenFor_digit();
                        break;
                    }
                    case 48: {
                        this.fetchTokenFor_zero();
                        break;
                    }
                    default: {
                        this.unfetch();
                        int num = this.fetchEscapedValue();
                        if (this.token.getC() != num) {
                            this.token.type = TokenType.CODE_POINT;
                            this.token.setCode(num);
                            break;
                        }
                        this.p = this.token.backP + 1;
                        break;
                    }
                }
                break;
            }
            this.token.setC(this.c);
            this.token.escaped = false;
            if (this.c != 0 && this.syntax.opVariableMetaCharacters()) {
                this.fetchTokenFor_metaChars();
                break;
            }
            switch (this.c) {
                case 46: {
                    if (!this.syntax.opDotAnyChar()) break block47;
                    this.token.type = TokenType.ANYCHAR;
                    break block47;
                }
                case 42: {
                    if (!this.syntax.opAsteriskZeroInf()) break block47;
                    this.fetchTokenFor_repeat(0, -1);
                    break block47;
                }
                case 43: {
                    if (!this.syntax.opPlusOneInf()) break block47;
                    this.fetchTokenFor_repeat(1, -1);
                    break block47;
                }
                case 63: {
                    if (!this.syntax.opQMarkZeroOne()) break block47;
                    this.fetchTokenFor_repeat(0, 1);
                    break block47;
                }
                case 123: {
                    if (!this.syntax.opBraceInterval()) break block47;
                    this.fetchTokenFor_openBrace();
                    break block47;
                }
                case 124: {
                    if (!this.syntax.opVBarAlt()) break block47;
                    this.token.type = TokenType.ALT;
                    break block47;
                }
                case 40: {
                    if (this.peekIs(63) && this.syntax.op2QMarkGroupEffect()) {
                        this.inc();
                        if (this.peekIs(35)) {
                            this.fetch();
                            while (true) {
                                if (!this.left()) {
                                    throw new SyntaxException("end pattern in group");
                                }
                                this.fetch();
                                if (this.c == this.syntax.metaCharTable.esc) {
                                    if (!this.left()) continue;
                                    this.fetch();
                                    continue;
                                }
                                if (this.c == 41) break;
                            }
                            continue block47;
                        }
                        this.unfetch();
                    }
                    if (!this.syntax.opLParenSubexp()) break block47;
                    this.token.type = TokenType.SUBEXP_OPEN;
                    break block47;
                }
                case 41: {
                    if (!this.syntax.opLParenSubexp()) break block47;
                    this.token.type = TokenType.SUBEXP_CLOSE;
                    break block47;
                }
                case 94: {
                    if (!this.syntax.opLineAnchor()) break block47;
                    this.fetchTokenFor_anchor(Option.isSingleline(this.env.option) ? 1 : 2);
                    break block47;
                }
                case 36: {
                    if (!this.syntax.opLineAnchor()) break block47;
                    this.fetchTokenFor_anchor(Option.isSingleline(this.env.option) ? 8 : 32);
                    break block47;
                }
                case 91: {
                    if (!this.syntax.opBracketCC()) break block47;
                    this.token.type = TokenType.CC_CC_OPEN;
                    break block47;
                }
                case 93: {
                    break block47;
                }
                case 35: {
                    if (!Option.isExtend(this.env.option)) break block47;
                    do {
                        if (!this.left()) continue block47;
                        this.fetch();
                    } while (!EncodingHelper.isNewLine(this.c));
                    continue block47;
                }
                case 9: 
                case 10: 
                case 12: 
                case 13: 
                case 32: {
                    if (!Option.isExtend(this.env.option)) break block47;
                    continue block47;
                }
            }
            break;
        }
        return this.token.type;
    }

    private void greedyCheck() {
        if (this.left() && this.peekIs(63) && this.syntax.opQMarkNonGreedy()) {
            this.fetch();
            this.token.setRepeatGreedy(false);
            this.token.setRepeatPossessive(false);
        } else {
            this.possessiveCheck();
        }
    }

    private void possessiveCheck() {
        if (this.left() && this.peekIs(43) && (this.syntax.op2PlusPossessiveRepeat() && this.token.type != TokenType.INTERVAL || this.syntax.op2PlusPossessiveInterval() && this.token.type == TokenType.INTERVAL)) {
            this.fetch();
            this.token.setRepeatGreedy(true);
            this.token.setRepeatPossessive(true);
        } else {
            this.token.setRepeatGreedy(true);
            this.token.setRepeatPossessive(false);
        }
    }

    protected final void syntaxWarn(String message, char ch) {
        this.syntaxWarn(message.replace("<%n>", Character.toString(ch)));
    }

    protected final void syntaxWarn(String message) {
        this.env.reg.warnings.warn(message + ": /" + new String(this.chars, this.getBegin(), this.getEnd()) + "/");
    }
}

