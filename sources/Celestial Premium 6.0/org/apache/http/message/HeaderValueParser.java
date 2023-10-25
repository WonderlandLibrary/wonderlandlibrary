/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public interface HeaderValueParser {
    public HeaderElement[] parseElements(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public HeaderElement parseHeaderElement(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public NameValuePair[] parseParameters(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

    public NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2) throws ParseException;
}

