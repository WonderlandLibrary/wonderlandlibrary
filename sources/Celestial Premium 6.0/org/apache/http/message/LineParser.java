/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public interface LineParser {
    public ProtocolVersion parseProtocolVersion(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public boolean hasProtocolVersion(CharArrayBuffer var1, ParserCursor var2);

    public RequestLine parseRequestLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public StatusLine parseStatusLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public Header parseHeader(CharArrayBuffer var1) throws ParseException;
}

