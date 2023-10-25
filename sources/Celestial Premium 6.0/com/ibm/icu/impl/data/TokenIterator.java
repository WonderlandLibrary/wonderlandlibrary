/*
 * Decompiled with CFR 0.150.
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

    public TokenIterator(ResourceReader r) {
        this.reader = r;
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
        char c = this.line.charAt(position++);
        char quote = '\u0000';
        switch (c) {
            case '\"': 
            case '\'': {
                quote = c;
                break;
            }
            case '#': {
                return -1;
            }
            default: {
                this.buf.append(c);
            }
        }
        int[] posref = null;
        while (position < this.line.length()) {
            c = this.line.charAt(position);
            if (c == '\\') {
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
            if (quote != '\u0000' && c == quote || quote == '\u0000' && PatternProps.isWhiteSpace(c)) {
                return ++position;
            }
            if (quote == '\u0000' && c == '#') {
                return position;
            }
            this.buf.append(c);
            ++position;
        }
        if (quote != '\u0000') {
            throw new RuntimeException("Unterminated quote at " + this.reader.describePosition() + ':' + startpos);
        }
        return position;
    }
}

