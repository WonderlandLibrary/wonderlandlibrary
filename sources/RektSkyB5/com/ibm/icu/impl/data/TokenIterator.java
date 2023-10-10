/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.data;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.data.ResourceReader;
import com.ibm.icu.text.UTF16;
import java.io.IOException;

public class TokenIterator {
    private ResourceReader reader;
    private String line;
    private StringBuffer buf;
    private boolean done;
    private int pos;
    private int lastpos;

    public TokenIterator(ResourceReader r2) {
        this.reader = r2;
        this.line = null;
        this.done = false;
        this.buf = new StringBuffer();
        this.lastpos = -1;
        this.pos = -1;
    }

    public String next() throws IOException {
        if (this.done) {
            return null;
        }
        while (true) {
            if (this.line == null) {
                this.line = this.reader.readLineSkippingComments();
                if (this.line == null) {
                    this.done = true;
                    return null;
                }
                this.pos = 0;
            }
            this.buf.setLength(0);
            this.lastpos = this.pos;
            this.pos = this.nextToken(this.pos);
            if (this.pos >= 0) break;
            this.line = null;
        }
        return this.buf.toString();
    }

    public int getLineNumber() {
        return this.reader.getLineNumber();
    }

    public String describePosition() {
        return this.reader.describePosition() + ':' + (this.lastpos + 1);
    }

    private int nextToken(int position) {
        if ((position = PatternProps.skipWhiteSpace(this.line, position)) == this.line.length()) {
            return -1;
        }
        int startpos = position;
        char c2 = this.line.charAt(position++);
        char quote = '\u0000';
        switch (c2) {
            case '\"': 
            case '\'': {
                quote = c2;
                break;
            }
            case '#': {
                return -1;
            }
            default: {
                this.buf.append(c2);
            }
        }
        int[] posref = null;
        while (position < this.line.length()) {
            c2 = this.line.charAt(position);
            if (c2 == '\\') {
                int c32;
                if (posref == null) {
                    posref = new int[]{position + 1};
                }
                if ((c32 = Utility.unescapeAt(this.line, posref)) < 0) {
                    throw new RuntimeException("Invalid escape at " + this.reader.describePosition() + ':' + position);
                }
                UTF16.append(this.buf, c32);
                position = posref[0];
                continue;
            }
            if (quote != '\u0000' && c2 == quote || quote == '\u0000' && PatternProps.isWhiteSpace(c2)) {
                return ++position;
            }
            if (quote == '\u0000' && c2 == '#') {
                return position;
            }
            this.buf.append(c2);
            ++position;
        }
        if (quote != '\u0000') {
            throw new RuntimeException("Unterminated quote at " + this.reader.describePosition() + ':' + startpos);
        }
        return position;
    }
}

