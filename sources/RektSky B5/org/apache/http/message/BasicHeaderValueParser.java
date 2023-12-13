/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import java.util.ArrayList;
import java.util.BitSet;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicHeaderValueParser
implements HeaderValueParser {
    @Deprecated
    public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
    public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
    private static final char PARAM_DELIMITER = ';';
    private static final char ELEM_DELIMITER = ',';
    private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(61, 59, 44);
    private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(59, 44);
    private final TokenParser tokenParser = TokenParser.INSTANCE;

    public static HeaderElement[] parseElements(String value, HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseElements(buffer, cursor);
    }

    @Override
    public HeaderElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        ArrayList<HeaderElement> elements = new ArrayList<HeaderElement>();
        while (!cursor.atEnd()) {
            HeaderElement element = this.parseHeaderElement(buffer, cursor);
            if (element.getName().length() == 0 && element.getValue() == null) continue;
            elements.add(element);
        }
        return elements.toArray(new HeaderElement[elements.size()]);
    }

    public static HeaderElement parseHeaderElement(String value, HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseHeaderElement(buffer, cursor);
    }

    @Override
    public HeaderElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor) {
        char ch;
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        NameValuePair nvp = this.parseNameValuePair(buffer, cursor);
        NameValuePair[] params = null;
        if (!cursor.atEnd() && (ch = buffer.charAt(cursor.getPos() - 1)) != ',') {
            params = this.parseParameters(buffer, cursor);
        }
        return this.createHeaderElement(nvp.getName(), nvp.getValue(), params);
    }

    protected HeaderElement createHeaderElement(String name, String value, NameValuePair[] params) {
        return new BasicHeaderElement(name, value, params);
    }

    public static NameValuePair[] parseParameters(String value, HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseParameters(buffer, cursor);
    }

    @Override
    public NameValuePair[] parseParameters(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        this.tokenParser.skipWhiteSpace(buffer, cursor);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        while (!cursor.atEnd()) {
            NameValuePair param = this.parseNameValuePair(buffer, cursor);
            params.add(param);
            char ch = buffer.charAt(cursor.getPos() - 1);
            if (ch != ',') continue;
            break;
        }
        return params.toArray(new NameValuePair[params.size()]);
    }

    public static NameValuePair parseNameValuePair(String value, HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return (parser != null ? parser : INSTANCE).parseNameValuePair(buffer, cursor);
    }

    @Override
    public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        String name = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
        if (cursor.atEnd()) {
            return new BasicNameValuePair(name, null);
        }
        char delim = buffer.charAt(cursor.getPos());
        cursor.updatePos(cursor.getPos() + 1);
        if (delim != '=') {
            return this.createNameValuePair(name, null);
        }
        String value = this.tokenParser.parseValue(buffer, cursor, VALUE_DELIMS);
        if (!cursor.atEnd()) {
            cursor.updatePos(cursor.getPos() + 1);
        }
        return this.createNameValuePair(name, value);
    }

    @Deprecated
    public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor, char[] delimiters) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        BitSet delimSet = new BitSet();
        if (delimiters != null) {
            for (char delimiter : delimiters) {
                delimSet.set(delimiter);
            }
        }
        delimSet.set(61);
        String name = this.tokenParser.parseToken(buffer, cursor, delimSet);
        if (cursor.atEnd()) {
            return new BasicNameValuePair(name, null);
        }
        char delim = buffer.charAt(cursor.getPos());
        cursor.updatePos(cursor.getPos() + 1);
        if (delim != '=') {
            return this.createNameValuePair(name, null);
        }
        delimSet.clear(61);
        String value = this.tokenParser.parseValue(buffer, cursor, delimSet);
        if (!cursor.atEnd()) {
            cursor.updatePos(cursor.getPos() + 1);
        }
        return this.createNameValuePair(name, value);
    }

    protected NameValuePair createNameValuePair(String name, String value) {
        return new BasicNameValuePair(name, value);
    }
}

