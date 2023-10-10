/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicLineParser
implements LineParser {
    @Deprecated
    public static final BasicLineParser DEFAULT = new BasicLineParser();
    public static final BasicLineParser INSTANCE = new BasicLineParser();
    protected final ProtocolVersion protocol;

    public BasicLineParser(ProtocolVersion proto) {
        this.protocol = proto != null ? proto : HttpVersion.HTTP_1_1;
    }

    public BasicLineParser() {
        this(null);
    }

    public static ProtocolVersion parseProtocolVersion(String value, LineParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseProtocolVersion(buffer, cursor);
    }

    @Override
    public ProtocolVersion parseProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
        int minor;
        int major;
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        String protoname = this.protocol.getProtocol();
        int protolength = protoname.length();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        this.skipWhitespace(buffer, cursor);
        int i2 = cursor.getPos();
        if (i2 + protolength + 4 > indexTo) {
            throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
        }
        boolean ok = true;
        for (int j2 = 0; ok && j2 < protolength; ++j2) {
            ok = buffer.charAt(i2 + j2) == protoname.charAt(j2);
        }
        if (ok) {
            boolean bl = ok = buffer.charAt(i2 + protolength) == '/';
        }
        if (!ok) {
            throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
        }
        int period = buffer.indexOf(46, i2 += protolength + 1, indexTo);
        if (period == -1) {
            throw new ParseException("Invalid protocol version number: " + buffer.substring(indexFrom, indexTo));
        }
        try {
            major = Integer.parseInt(buffer.substringTrimmed(i2, period));
        }
        catch (NumberFormatException e2) {
            throw new ParseException("Invalid protocol major version number: " + buffer.substring(indexFrom, indexTo));
        }
        i2 = period + 1;
        int blank = buffer.indexOf(32, i2, indexTo);
        if (blank == -1) {
            blank = indexTo;
        }
        try {
            minor = Integer.parseInt(buffer.substringTrimmed(i2, blank));
        }
        catch (NumberFormatException e3) {
            throw new ParseException("Invalid protocol minor version number: " + buffer.substring(indexFrom, indexTo));
        }
        cursor.updatePos(blank);
        return this.createProtocolVersion(major, minor);
    }

    protected ProtocolVersion createProtocolVersion(int major, int minor) {
        return this.protocol.forVersion(major, minor);
    }

    @Override
    public boolean hasProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) {
        int index;
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        String protoname = this.protocol.getProtocol();
        int protolength = protoname.length();
        if (buffer.length() < protolength + 4) {
            return false;
        }
        if (index < 0) {
            index = buffer.length() - 4 - protolength;
        } else if (index == 0) {
            for (index = cursor.getPos(); index < buffer.length() && HTTP.isWhitespace(buffer.charAt(index)); ++index) {
            }
        }
        if (index + protolength + 4 > buffer.length()) {
            return false;
        }
        boolean ok = true;
        for (int j2 = 0; ok && j2 < protolength; ++j2) {
            ok = buffer.charAt(index + j2) == protoname.charAt(j2);
        }
        if (ok) {
            ok = buffer.charAt(index + protolength) == '/';
        }
        return ok;
    }

    public static RequestLine parseRequestLine(String value, LineParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseRequestLine(buffer, cursor);
    }

    @Override
    public RequestLine parseRequestLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        try {
            this.skipWhitespace(buffer, cursor);
            int i2 = cursor.getPos();
            int blank = buffer.indexOf(32, i2, indexTo);
            if (blank < 0) {
                throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
            }
            String method = buffer.substringTrimmed(i2, blank);
            cursor.updatePos(blank);
            this.skipWhitespace(buffer, cursor);
            i2 = cursor.getPos();
            blank = buffer.indexOf(32, i2, indexTo);
            if (blank < 0) {
                throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
            }
            String uri = buffer.substringTrimmed(i2, blank);
            cursor.updatePos(blank);
            ProtocolVersion ver = this.parseProtocolVersion(buffer, cursor);
            this.skipWhitespace(buffer, cursor);
            if (!cursor.atEnd()) {
                throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
            }
            return this.createRequestLine(method, uri, ver);
        }
        catch (IndexOutOfBoundsException e2) {
            throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
        }
    }

    protected RequestLine createRequestLine(String method, String uri, ProtocolVersion ver) {
        return new BasicRequestLine(method, uri, ver);
    }

    public static StatusLine parseStatusLine(String value, LineParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseStatusLine(buffer, cursor);
    }

    @Override
    public StatusLine parseStatusLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        try {
            int statusCode;
            ProtocolVersion ver = this.parseProtocolVersion(buffer, cursor);
            this.skipWhitespace(buffer, cursor);
            int i2 = cursor.getPos();
            int blank = buffer.indexOf(32, i2, indexTo);
            if (blank < 0) {
                blank = indexTo;
            }
            String s2 = buffer.substringTrimmed(i2, blank);
            for (int j2 = 0; j2 < s2.length(); ++j2) {
                if (Character.isDigit(s2.charAt(j2))) continue;
                throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
            }
            try {
                statusCode = Integer.parseInt(s2);
            }
            catch (NumberFormatException e2) {
                throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
            }
            i2 = blank;
            String reasonPhrase = i2 < indexTo ? buffer.substringTrimmed(i2, indexTo) : "";
            return this.createStatusLine(ver, statusCode, reasonPhrase);
        }
        catch (IndexOutOfBoundsException e3) {
            throw new ParseException("Invalid status line: " + buffer.substring(indexFrom, indexTo));
        }
    }

    protected StatusLine createStatusLine(ProtocolVersion ver, int status, String reason) {
        return new BasicStatusLine(ver, status, reason);
    }

    public static Header parseHeader(String value, LineParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        return (parser != null ? parser : INSTANCE).parseHeader(buffer);
    }

    @Override
    public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
        return new BufferedHeader(buffer);
    }

    protected void skipWhitespace(CharArrayBuffer buffer, ParserCursor cursor) {
        int pos;
        int indexTo = cursor.getUpperBound();
        for (pos = cursor.getPos(); pos < indexTo && HTTP.isWhitespace(buffer.charAt(pos)); ++pos) {
        }
        cursor.updatePos(pos);
    }
}

