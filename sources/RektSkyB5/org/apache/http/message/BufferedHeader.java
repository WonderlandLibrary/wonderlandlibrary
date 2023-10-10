/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.FormattedHeader;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class BufferedHeader
implements FormattedHeader,
Cloneable,
Serializable {
    private static final long serialVersionUID = -2768352615787625448L;
    private final String name;
    private final CharArrayBuffer buffer;
    private final int valuePos;

    public BufferedHeader(CharArrayBuffer buffer) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        int colon = buffer.indexOf(58);
        if (colon == -1) {
            throw new ParseException("Invalid header: " + buffer.toString());
        }
        String s2 = buffer.substringTrimmed(0, colon);
        if (s2.length() == 0) {
            throw new ParseException("Invalid header: " + buffer.toString());
        }
        this.buffer = buffer;
        this.name = s2;
        this.valuePos = colon + 1;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        ParserCursor cursor = new ParserCursor(0, this.buffer.length());
        cursor.updatePos(this.valuePos);
        return BasicHeaderValueParser.INSTANCE.parseElements(this.buffer, cursor);
    }

    @Override
    public int getValuePos() {
        return this.valuePos;
    }

    @Override
    public CharArrayBuffer getBuffer() {
        return this.buffer;
    }

    public String toString() {
        return this.buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

