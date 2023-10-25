/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.UTF16;

class ReplaceableContextIterator
implements UCaseProps.ContextIterator {
    protected Replaceable rep = null;
    protected int index = 0;
    protected int limit = 0;
    protected int cpStart = 0;
    protected int cpLimit = 0;
    protected int contextStart = 0;
    protected int contextLimit = 0;
    protected int dir = 0;
    protected boolean reachedLimit = false;

    ReplaceableContextIterator() {
    }

    public void setText(Replaceable rep) {
        this.rep = rep;
        this.limit = this.contextLimit = rep.length();
        this.contextStart = 0;
        this.index = 0;
        this.cpLimit = 0;
        this.cpStart = 0;
        this.dir = 0;
        this.reachedLimit = false;
    }

    public void setIndex(int index) {
        this.cpStart = this.cpLimit = index;
        this.index = 0;
        this.dir = 0;
        this.reachedLimit = false;
    }

    public int getCaseMapCPStart() {
        return this.cpStart;
    }

    public void setLimit(int lim) {
        this.limit = 0 <= lim && lim <= this.rep.length() ? lim : this.rep.length();
        this.reachedLimit = false;
    }

    public void setContextLimits(int contextStart, int contextLimit) {
        this.contextStart = contextStart < 0 ? 0 : (contextStart <= this.rep.length() ? contextStart : this.rep.length());
        this.contextLimit = contextLimit < this.contextStart ? this.contextStart : (contextLimit <= this.rep.length() ? contextLimit : this.rep.length());
        this.reachedLimit = false;
    }

    public int nextCaseMapCP() {
        if (this.cpLimit < this.limit) {
            this.cpStart = this.cpLimit;
            int c = this.rep.char32At(this.cpLimit);
            this.cpLimit += UTF16.getCharCount(c);
            return c;
        }
        return -1;
    }

    public int replace(String text) {
        int delta = text.length() - (this.cpLimit - this.cpStart);
        this.rep.replace(this.cpStart, this.cpLimit, text);
        this.cpLimit += delta;
        this.limit += delta;
        this.contextLimit += delta;
        return delta;
    }

    public boolean didReachLimit() {
        return this.reachedLimit;
    }

    public void reset(int direction) {
        if (direction > 0) {
            this.dir = 1;
            this.index = this.cpLimit;
        } else if (direction < 0) {
            this.dir = -1;
            this.index = this.cpStart;
        } else {
            this.dir = 0;
            this.index = 0;
        }
        this.reachedLimit = false;
    }

    public int next() {
        if (this.dir > 0) {
            if (this.index < this.contextLimit) {
                int c = this.rep.char32At(this.index);
                this.index += UTF16.getCharCount(c);
                return c;
            }
            this.reachedLimit = true;
        } else if (this.dir < 0 && this.index > this.contextStart) {
            int c = this.rep.char32At(this.index - 1);
            this.index -= UTF16.getCharCount(c);
            return c;
        }
        return -1;
    }
}

