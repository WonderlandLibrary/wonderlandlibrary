/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.AbstractParser;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.SpillProperty;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.scripts.JD;
import jdk.nashorn.internal.scripts.JO;

public class JSONParser {
    private final String source;
    private final Global global;
    private final boolean dualFields;
    final int length;
    int pos = 0;
    private static final int EOF = -1;
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NULL = "null";
    private static final int STATE_EMPTY = 0;
    private static final int STATE_ELEMENT_PARSED = 1;
    private static final int STATE_COMMA_PARSED = 2;

    public JSONParser(String source, Global global, boolean dualFields) {
        this.source = source;
        this.global = global;
        this.length = source.length();
        this.dualFields = dualFields;
    }

    public static String quote(String value) {
        StringBuilder product = new StringBuilder();
        product.append("\"");
        block9: for (char ch : value.toCharArray()) {
            switch (ch) {
                case '\\': {
                    product.append("\\\\");
                    continue block9;
                }
                case '\"': {
                    product.append("\\\"");
                    continue block9;
                }
                case '\b': {
                    product.append("\\b");
                    continue block9;
                }
                case '\f': {
                    product.append("\\f");
                    continue block9;
                }
                case '\n': {
                    product.append("\\n");
                    continue block9;
                }
                case '\r': {
                    product.append("\\r");
                    continue block9;
                }
                case '\t': {
                    product.append("\\t");
                    continue block9;
                }
                default: {
                    if (ch < ' ') {
                        product.append(Lexer.unicodeEscape(ch));
                        continue block9;
                    }
                    product.append(ch);
                }
            }
        }
        product.append("\"");
        return product.toString();
    }

    public Object parse() {
        Object value = this.parseLiteral();
        this.skipWhiteSpace();
        if (this.pos < this.length) {
            throw this.expectedError(this.pos, "eof", JSONParser.toString(this.peek()));
        }
        return value;
    }

    private Object parseLiteral() {
        this.skipWhiteSpace();
        int c = this.peek();
        if (c == -1) {
            throw this.expectedError(this.pos, "json literal", "eof");
        }
        switch (c) {
            case 123: {
                return this.parseObject();
            }
            case 91: {
                return this.parseArray();
            }
            case 34: {
                return this.parseString();
            }
            case 102: {
                return this.parseKeyword(FALSE, Boolean.FALSE);
            }
            case 116: {
                return this.parseKeyword(TRUE, Boolean.TRUE);
            }
            case 110: {
                return this.parseKeyword(NULL, null);
            }
        }
        if (this.isDigit(c) || c == 45) {
            return this.parseNumber();
        }
        if (c == 46) {
            throw this.numberError(this.pos);
        }
        throw this.expectedError(this.pos, "json literal", JSONParser.toString(c));
    }

    private Object parseObject() {
        PropertyMap propertyMap = this.dualFields ? JD.getInitialMap() : JO.getInitialMap();
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        ArrayList<Object> values2 = new ArrayList<Object>();
        int state = 0;
        assert (this.peek() == 123);
        ++this.pos;
        block5: while (this.pos < this.length) {
            this.skipWhiteSpace();
            int c = this.peek();
            switch (c) {
                case 34: {
                    if (state == 1) {
                        throw this.expectedError(this.pos - 1, ", or }", JSONParser.toString(c));
                    }
                    String id = this.parseString();
                    this.expectColon();
                    Object value = this.parseLiteral();
                    int index = ArrayIndex.getArrayIndex(id);
                    if (ArrayIndex.isValidArrayIndex(index)) {
                        arrayData = JSONParser.addArrayElement(arrayData, index, value);
                    } else {
                        propertyMap = this.addObjectProperty(propertyMap, values2, id, value);
                    }
                    state = 1;
                    continue block5;
                }
                case 44: {
                    if (state != 1) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    ++this.pos;
                    continue block5;
                }
                case 125: {
                    if (state == 2) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    ++this.pos;
                    return this.createObject(propertyMap, values2, arrayData);
                }
            }
            throw this.expectedError(this.pos, ", or }", JSONParser.toString(c));
        }
        throw this.expectedError(this.pos, ", or }", "eof");
    }

    private static ArrayData addArrayElement(ArrayData arrayData, int index, Object value) {
        long oldLength = arrayData.length();
        long longIndex = ArrayIndex.toLongIndex(index);
        ArrayData newArrayData = arrayData;
        if (longIndex >= oldLength) {
            newArrayData = newArrayData.ensure(longIndex);
            if (longIndex > oldLength) {
                newArrayData = newArrayData.delete(oldLength, longIndex - 1L);
            }
        }
        return newArrayData.set(index, value, false);
    }

    private PropertyMap addObjectProperty(PropertyMap propertyMap, List<Object> values2, String id, Object value) {
        PropertyMap newMap;
        int flags;
        Class type;
        Property oldProperty = propertyMap.findProperty(id);
        if (this.dualFields) {
            type = JSONParser.getType(value);
            flags = 2048;
        } else {
            type = Object.class;
            flags = 0;
        }
        if (oldProperty != null) {
            values2.set(oldProperty.getSlot(), value);
            newMap = propertyMap.replaceProperty(oldProperty, new SpillProperty(id, flags, oldProperty.getSlot(), type));
        } else {
            values2.add(value);
            newMap = propertyMap.addProperty(new SpillProperty(id, flags, propertyMap.size(), type));
        }
        return newMap;
    }

    private Object createObject(PropertyMap propertyMap, List<Object> values2, ArrayData arrayData) {
        long[] primitiveSpill = this.dualFields ? new long[values2.size()] : null;
        Object[] objectSpill = new Object[values2.size()];
        for (Property property : propertyMap.getProperties()) {
            if (!this.dualFields || property.getType() == Object.class) {
                objectSpill[property.getSlot()] = values2.get(property.getSlot());
                continue;
            }
            primitiveSpill[property.getSlot()] = ObjectClassGenerator.pack((Number)values2.get(property.getSlot()));
        }
        ScriptObject object = this.dualFields ? new JD(propertyMap, primitiveSpill, objectSpill) : new JO(propertyMap, null, objectSpill);
        object.setInitialProto(this.global.getObjectPrototype());
        object.setArray(arrayData);
        return object;
    }

    private static Class<?> getType(Object value) {
        if (value instanceof Integer) {
            return Integer.TYPE;
        }
        if (value instanceof Double) {
            return Double.TYPE;
        }
        return Object.class;
    }

    private void expectColon() {
        this.skipWhiteSpace();
        int n = this.next();
        if (n != 58) {
            throw this.expectedError(this.pos - 1, ":", JSONParser.toString(n));
        }
    }

    private Object parseArray() {
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        int state = 0;
        assert (this.peek() == 91);
        ++this.pos;
        block4: while (this.pos < this.length) {
            this.skipWhiteSpace();
            int c = this.peek();
            switch (c) {
                case 44: {
                    if (state != 1) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    ++this.pos;
                    continue block4;
                }
                case 93: {
                    if (state == 2) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    ++this.pos;
                    return this.global.wrapAsObject(arrayData);
                }
            }
            if (state == 1) {
                throw this.expectedError(this.pos, ", or ]", JSONParser.toString(c));
            }
            long index = arrayData.length();
            arrayData = arrayData.ensure(index).set((int)index, this.parseLiteral(), true);
            state = 1;
        }
        throw this.expectedError(this.pos, ", or ]", "eof");
    }

    private String parseString() {
        int start = ++this.pos;
        StringBuilder sb = null;
        while (this.pos < this.length) {
            int c = this.next();
            if (c <= 31) {
                throw this.syntaxError(this.pos, "String contains control character");
            }
            if (c == 92) {
                if (sb == null) {
                    sb = new StringBuilder(this.pos - start + 16);
                }
                sb.append(this.source, start, this.pos - 1);
                sb.append(this.parseEscapeSequence());
                start = this.pos;
                continue;
            }
            if (c != 34) continue;
            if (sb != null) {
                sb.append(this.source, start, this.pos - 1);
                return sb.toString();
            }
            return this.source.substring(start, this.pos - 1);
        }
        throw this.error(Lexer.message("missing.close.quote", new String[0]), this.pos, this.length);
    }

    private char parseEscapeSequence() {
        int c = this.next();
        switch (c) {
            case 34: {
                return '\"';
            }
            case 92: {
                return '\\';
            }
            case 47: {
                return '/';
            }
            case 98: {
                return '\b';
            }
            case 102: {
                return '\f';
            }
            case 110: {
                return '\n';
            }
            case 114: {
                return '\r';
            }
            case 116: {
                return '\t';
            }
            case 117: {
                return this.parseUnicodeEscape();
            }
        }
        throw this.error(Lexer.message("invalid.escape.char", new String[0]), this.pos - 1, this.length);
    }

    private char parseUnicodeEscape() {
        return (char)(this.parseHexDigit() << 12 | this.parseHexDigit() << 8 | this.parseHexDigit() << 4 | this.parseHexDigit());
    }

    private int parseHexDigit() {
        int c = this.next();
        if (c >= 48 && c <= 57) {
            return c - 48;
        }
        if (c >= 65 && c <= 70) {
            return c + 10 - 65;
        }
        if (c >= 97 && c <= 102) {
            return c + 10 - 97;
        }
        throw this.error(Lexer.message("invalid.hex", new String[0]), this.pos - 1, this.length);
    }

    private boolean isDigit(int c) {
        return c >= 48 && c <= 57;
    }

    private void skipDigits() {
        int c;
        while (this.pos < this.length && this.isDigit(c = this.peek())) {
            ++this.pos;
        }
    }

    private Number parseNumber() {
        double d;
        int start = this.pos;
        int c = this.next();
        if (c == 45) {
            c = this.next();
        }
        if (!this.isDigit(c)) {
            throw this.numberError(start);
        }
        if (c != 48) {
            this.skipDigits();
        }
        if (this.peek() == 46) {
            ++this.pos;
            if (!this.isDigit(this.next())) {
                throw this.numberError(this.pos - 1);
            }
            this.skipDigits();
        }
        if ((c = this.peek()) == 101 || c == 69) {
            ++this.pos;
            c = this.next();
            if (c == 45 || c == 43) {
                c = this.next();
            }
            if (!this.isDigit(c)) {
                throw this.numberError(this.pos - 1);
            }
            this.skipDigits();
        }
        if (JSType.isRepresentableAsInt(d = Double.parseDouble(this.source.substring(start, this.pos)))) {
            return (int)d;
        }
        return d;
    }

    private Object parseKeyword(String keyword, Object value) {
        if (!this.source.regionMatches(this.pos, keyword, 0, keyword.length())) {
            throw this.expectedError(this.pos, "json literal", "ident");
        }
        this.pos += keyword.length();
        return value;
    }

    private int peek() {
        if (this.pos >= this.length) {
            return -1;
        }
        return this.source.charAt(this.pos);
    }

    private int next() {
        int next = this.peek();
        ++this.pos;
        return next;
    }

    private void skipWhiteSpace() {
        block3: while (this.pos < this.length) {
            switch (this.peek()) {
                case 9: 
                case 10: 
                case 13: 
                case 32: {
                    ++this.pos;
                    continue block3;
                }
            }
            return;
        }
    }

    private static String toString(int c) {
        return c == -1 ? "eof" : String.valueOf((char)c);
    }

    ParserException error(String message, int start, int length) throws ParserException {
        long token = Token.toDesc(TokenType.STRING, start, length);
        int pos = Token.descPosition(token);
        Source src = Source.sourceFor("<json>", this.source);
        int lineNum = src.getLine(pos);
        int columnNum = src.getColumn(pos);
        String formatted = ErrorManager.format(message, src, lineNum, columnNum, token);
        return new ParserException(JSErrorType.SYNTAX_ERROR, formatted, src, lineNum, columnNum, token);
    }

    private ParserException error(String message, int start) {
        return this.error(message, start, this.length);
    }

    private ParserException numberError(int start) {
        return this.error(Lexer.message("json.invalid.number", new String[0]), start);
    }

    private ParserException expectedError(int start, String expected, String found) {
        return this.error(AbstractParser.message("expected", expected, found), start);
    }

    private ParserException syntaxError(int start, String reason) {
        String message = ECMAErrors.getMessage("syntax.error.invalid.json", reason);
        return this.error(message, start);
    }
}

