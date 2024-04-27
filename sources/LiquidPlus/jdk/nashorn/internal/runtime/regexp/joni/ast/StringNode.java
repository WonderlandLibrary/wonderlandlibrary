/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;

public final class StringNode
extends Node
implements StringType {
    private static final int NODE_STR_MARGIN = 16;
    private static final int NODE_STR_BUF_SIZE = 24;
    public static final StringNode EMPTY = new StringNode(null, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public char[] chars;
    public int p;
    public int end;
    public int flag;

    public StringNode() {
        this.chars = new char[24];
    }

    public StringNode(char[] chars, int p, int end) {
        this.chars = chars;
        this.p = p;
        this.end = end;
        this.setShared();
    }

    public StringNode(char c) {
        this();
        this.chars[this.end++] = c;
    }

    public void ensure(int ahead) {
        int len = this.end - this.p + ahead;
        if (len >= this.chars.length) {
            char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.p, tmp, 0, this.end - this.p);
            this.chars = tmp;
        }
    }

    private void modifyEnsure(int ahead) {
        if (this.isShared()) {
            int len = this.end - this.p + ahead;
            char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.p, tmp, 0, this.end - this.p);
            this.chars = tmp;
            this.end -= this.p;
            this.p = 0;
            this.clearShared();
        } else {
            this.ensure(ahead);
        }
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public String toString(int level) {
        StringBuilder value = new StringBuilder();
        value.append("\n  bytes: '");
        for (int i = this.p; i < this.end; ++i) {
            if (this.chars[i] >= ' ' && this.chars[i] < '\u007f') {
                value.append(this.chars[i]);
                continue;
            }
            value.append(String.format("[0x%04x]", this.chars[i]));
        }
        value.append("'");
        return value.toString();
    }

    public int length() {
        return this.end - this.p;
    }

    public StringNode splitLastChar() {
        int prev;
        StringNode n = null;
        if (this.end > this.p && (prev = EncodingHelper.prevCharHead(this.p, this.end)) != -1 && prev > this.p) {
            n = new StringNode(this.chars, prev, this.end);
            if (this.isRaw()) {
                n.setRaw();
            }
            this.end = prev;
        }
        return n;
    }

    public boolean canBeSplit() {
        return this.end > this.p && 1 < this.end - this.p;
    }

    public void set(char[] chars, int p, int end) {
        this.chars = chars;
        this.p = p;
        this.end = end;
        this.setShared();
    }

    public void cat(char[] cat, int catP, int catEnd) {
        int len = catEnd - catP;
        this.modifyEnsure(len);
        System.arraycopy(cat, catP, this.chars, this.end, len);
        this.end += len;
    }

    public void cat(char c) {
        this.modifyEnsure(1);
        this.chars[this.end++] = c;
    }

    public void catCode(int code) {
        this.cat((char)code);
    }

    public void clear() {
        if (this.chars.length > 24) {
            this.chars = new char[24];
        }
        this.flag = 0;
        this.end = 0;
        this.p = 0;
    }

    public void setRaw() {
        this.flag |= 1;
    }

    public void clearRaw() {
        this.flag &= 0xFFFFFFFE;
    }

    public boolean isRaw() {
        return (this.flag & 1) != 0;
    }

    public void setAmbig() {
        this.flag |= 2;
    }

    public void clearAmbig() {
        this.flag &= 0xFFFFFFFD;
    }

    public boolean isAmbig() {
        return (this.flag & 2) != 0;
    }

    public void setDontGetOptInfo() {
        this.flag |= 4;
    }

    public void clearDontGetOptInfo() {
        this.flag &= 0xFFFFFFFB;
    }

    public boolean isDontGetOptInfo() {
        return (this.flag & 4) != 0;
    }

    public void setShared() {
        this.flag |= 8;
    }

    public void clearShared() {
        this.flag &= 0xFFFFFFF7;
    }

    public boolean isShared() {
        return (this.flag & 8) != 0;
    }
}

