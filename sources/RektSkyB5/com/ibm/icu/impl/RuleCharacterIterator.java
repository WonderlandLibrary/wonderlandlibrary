/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UTF16;
import java.text.ParsePosition;

public class RuleCharacterIterator {
    private String text;
    private ParsePosition pos;
    private SymbolTable sym;
    private char[] buf;
    private int bufPos;
    private boolean isEscaped;
    public static final int DONE = -1;
    public static final int PARSE_VARIABLES = 1;
    public static final int PARSE_ESCAPES = 2;
    public static final int SKIP_WHITESPACE = 4;

    public RuleCharacterIterator(String text, SymbolTable sym, ParsePosition pos) {
        if (text == null || pos.getIndex() > text.length()) {
            throw new IllegalArgumentException();
        }
        this.text = text;
        this.sym = sym;
        this.pos = pos;
        this.buf = null;
    }

    public boolean atEnd() {
        return this.buf == null && this.pos.getIndex() == this.text.length();
    }

    public int next(int options) {
        int c2;
        block6: {
            c2 = -1;
            this.isEscaped = false;
            while (true) {
                c2 = this._current();
                this._advance(UTF16.getCharCount(c2));
                if (c2 == 36 && this.buf == null && (options & 1) != 0 && this.sym != null) {
                    String name = this.sym.parseReference(this.text, this.pos, this.text.length());
                    if (name != null) {
                        this.bufPos = 0;
                        this.buf = this.sym.lookup(name);
                        if (this.buf == null) {
                            throw new IllegalArgumentException("Undefined variable: " + name);
                        }
                        if (this.buf.length != 0) continue;
                        this.buf = null;
                        continue;
                    }
                    break block6;
                }
                if ((options & 4) == 0 || !PatternProps.isWhiteSpace(c2)) break;
            }
            if (c2 == 92 && (options & 2) != 0) {
                int[] offset = new int[]{0};
                c2 = Utility.unescapeAt(this.lookahead(), offset);
                this.jumpahead(offset[0]);
                this.isEscaped = true;
                if (c2 < 0) {
                    throw new IllegalArgumentException("Invalid escape");
                }
            }
        }
        return c2;
    }

    public boolean isEscaped() {
        return this.isEscaped;
    }

    public boolean inVariable() {
        return this.buf != null;
    }

    public Object getPos(Object p2) {
        if (p2 == null) {
            return new Object[]{this.buf, new int[]{this.pos.getIndex(), this.bufPos}};
        }
        Object[] a2 = (Object[])p2;
        a2[0] = this.buf;
        int[] v2 = (int[])a2[1];
        v2[0] = this.pos.getIndex();
        v2[1] = this.bufPos;
        return p2;
    }

    public void setPos(Object p2) {
        Object[] a2 = (Object[])p2;
        this.buf = (char[])a2[0];
        int[] v2 = (int[])a2[1];
        this.pos.setIndex(v2[0]);
        this.bufPos = v2[1];
    }

    public void skipIgnored(int options) {
        if ((options & 4) != 0) {
            int a2;
            while (PatternProps.isWhiteSpace(a2 = this._current())) {
                this._advance(UTF16.getCharCount(a2));
            }
        }
    }

    public String lookahead() {
        if (this.buf != null) {
            return new String(this.buf, this.bufPos, this.buf.length - this.bufPos);
        }
        return this.text.substring(this.pos.getIndex());
    }

    public void jumpahead(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        if (this.buf != null) {
            this.bufPos += count;
            if (this.bufPos > this.buf.length) {
                throw new IllegalArgumentException();
            }
            if (this.bufPos == this.buf.length) {
                this.buf = null;
            }
        } else {
            int i2 = this.pos.getIndex() + count;
            this.pos.setIndex(i2);
            if (i2 > this.text.length()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public String toString() {
        int b2 = this.pos.getIndex();
        return this.text.substring(0, b2) + '|' + this.text.substring(b2);
    }

    private int _current() {
        if (this.buf != null) {
            return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos);
        }
        int i2 = this.pos.getIndex();
        return i2 < this.text.length() ? UTF16.charAt(this.text, i2) : -1;
    }

    private void _advance(int count) {
        if (this.buf != null) {
            this.bufPos += count;
            if (this.bufPos == this.buf.length) {
                this.buf = null;
            }
        } else {
            this.pos.setIndex(this.pos.getIndex() + count);
            if (this.pos.getIndex() > this.text.length()) {
                this.pos.setIndex(this.text.length());
            }
        }
    }
}

