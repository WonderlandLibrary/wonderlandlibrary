/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.Size
 *  javafx.css.SizeUnits
 *  javafx.css.StyleConverter
 *  javafx.css.StyleConverter$StringStore
 *  javafx.scene.paint.Color
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.css.ParsedValue;
import javafx.css.Size;
import javafx.css.SizeUnits;
import javafx.css.StyleConverter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ParsedValueImpl<V, T>
extends ParsedValue<V, T> {
    private final boolean lookup;
    private final boolean containsLookups;
    private static int indent = 0;
    private int hash = Integer.MIN_VALUE;
    private static final byte NULL_VALUE = 0;
    private static final byte VALUE = 1;
    private static final byte VALUE_ARRAY = 2;
    private static final byte ARRAY_OF_VALUE_ARRAY = 3;
    private static final byte STRING = 4;
    private static final byte COLOR = 5;
    private static final byte ENUM = 6;
    private static final byte BOOLEAN = 7;
    private static final byte URL = 8;
    private static final byte SIZE = 9;

    public final boolean isLookup() {
        return this.lookup;
    }

    public final boolean isContainsLookups() {
        return this.containsLookups;
    }

    private static boolean getContainsLookupsFlag(Object object) {
        boolean bl;
        block4: {
            block6: {
                block5: {
                    block3: {
                        bl = false;
                        if (!(object instanceof Size)) break block3;
                        bl = false;
                        break block4;
                    }
                    if (!(object instanceof ParsedValueImpl)) break block5;
                    ParsedValueImpl parsedValueImpl = (ParsedValueImpl)((Object)object);
                    bl = parsedValueImpl.lookup || parsedValueImpl.containsLookups;
                    break block4;
                }
                if (!(object instanceof ParsedValueImpl[])) break block6;
                ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])object;
                for (int i = 0; i < arrparsedValueImpl.length && !bl; ++i) {
                    if (arrparsedValueImpl[i] == null) continue;
                    bl = bl || arrparsedValueImpl[i].lookup || arrparsedValueImpl[i].containsLookups;
                }
                break block4;
            }
            if (!(object instanceof ParsedValueImpl[][])) break block4;
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])object;
            for (int i = 0; i < arrparsedValueImpl.length && !bl; ++i) {
                if (arrparsedValueImpl[i] == null) continue;
                for (int j = 0; j < arrparsedValueImpl[i].length && !bl; ++j) {
                    if (arrparsedValueImpl[i][j] == null) continue;
                    bl = bl || arrparsedValueImpl[i][j].lookup || arrparsedValueImpl[i][j].containsLookups;
                }
            }
        }
        return bl;
    }

    public static boolean containsFontRelativeSize(ParsedValue parsedValue, boolean bl) {
        boolean bl2;
        block4: {
            Object object;
            block6: {
                block5: {
                    block3: {
                        bl2 = false;
                        object = parsedValue.getValue();
                        if (!(object instanceof Size)) break block3;
                        Size size = (Size)object;
                        bl2 = size.getUnits() == SizeUnits.PERCENT ? bl : !size.isAbsolute();
                        break block4;
                    }
                    if (!(object instanceof ParsedValue)) break block5;
                    ParsedValueImpl parsedValueImpl = (ParsedValueImpl)((Object)object);
                    bl2 = ParsedValueImpl.containsFontRelativeSize(parsedValueImpl, bl);
                    break block4;
                }
                if (!(object instanceof ParsedValue[])) break block6;
                ParsedValue[] arrparsedValue = (ParsedValue[])object;
                for (int i = 0; i < arrparsedValue.length && !bl2; ++i) {
                    if (arrparsedValue[i] == null) continue;
                    bl2 = ParsedValueImpl.containsFontRelativeSize(arrparsedValue[i], bl);
                }
                break block4;
            }
            if (!(object instanceof ParsedValueImpl[][])) break block4;
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])object;
            for (int i = 0; i < arrparsedValueImpl.length && !bl2; ++i) {
                if (arrparsedValueImpl[i] == null) continue;
                for (int j = 0; j < arrparsedValueImpl[i].length && !bl2; ++j) {
                    if (arrparsedValueImpl[i][j] == null) continue;
                    bl2 = ParsedValueImpl.containsFontRelativeSize(arrparsedValueImpl[i][j], bl);
                }
            }
        }
        return bl2;
    }

    public ParsedValueImpl(V v, StyleConverter<V, T> styleConverter, boolean bl) {
        super(v, styleConverter);
        this.lookup = bl;
        this.containsLookups = bl || ParsedValueImpl.getContainsLookupsFlag(v);
    }

    public ParsedValueImpl(V v, StyleConverter<V, T> styleConverter) {
        this(v, styleConverter, false);
    }

    public T convert(Font font) {
        return (T)(this.converter != null ? this.converter.convert((ParsedValue)this, font) : this.value);
    }

    private static String spaces() {
        return new String(new char[indent]).replace('\u0000', ' ');
    }

    private static void indent() {
        indent += 2;
    }

    private static void outdent() {
        indent = Math.max(0, indent - 2);
    }

    public String toString() {
        String string = System.lineSeparator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ParsedValueImpl.spaces()).append(this.lookup ? "<Value lookup=\"true\">" : "<Value>").append(string);
        ParsedValueImpl.indent();
        if (this.value != null) {
            this.appendValue(stringBuilder, this.value, "value");
        } else {
            this.appendValue(stringBuilder, "null", "value");
        }
        stringBuilder.append(ParsedValueImpl.spaces()).append("<converter>").append((Object)this.converter).append("</converter>").append(string);
        ParsedValueImpl.outdent();
        stringBuilder.append(ParsedValueImpl.spaces()).append("</Value>");
        return stringBuilder.toString();
    }

    private void appendValue(StringBuilder stringBuilder, Object object, String string) {
        String string2 = System.lineSeparator();
        if (object instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])object;
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append(" layers=\"").append(arrparsedValueImpl.length).append("\">").append(string2);
            ParsedValueImpl.indent();
            for (ParsedValueImpl[] arrparsedValueImpl2 : arrparsedValueImpl) {
                stringBuilder.append(ParsedValueImpl.spaces()).append("<layer>").append(string2);
                ParsedValueImpl.indent();
                if (arrparsedValueImpl2 == null) {
                    stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                    continue;
                }
                for (ParsedValueImpl parsedValueImpl : arrparsedValueImpl2) {
                    if (parsedValueImpl == null) {
                        stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                        continue;
                    }
                    stringBuilder.append((Object)parsedValueImpl);
                }
                ParsedValueImpl.outdent();
                stringBuilder.append(ParsedValueImpl.spaces()).append("</layer>").append(string2);
            }
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else if (object instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])object;
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append(" values=\"").append(arrparsedValueImpl.length).append("\">").append(string2);
            ParsedValueImpl.indent();
            for (ParsedValueImpl parsedValueImpl : arrparsedValueImpl) {
                if (parsedValueImpl == null) {
                    stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                    continue;
                }
                stringBuilder.append((Object)parsedValueImpl);
            }
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else if (object instanceof ParsedValueImpl) {
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append('>').append(string2);
            ParsedValueImpl.indent();
            stringBuilder.append(object);
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else {
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append('>');
            stringBuilder.append(object);
            stringBuilder.append("</").append(string).append('>').append(string2);
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != ((Object)((Object)this)).getClass()) {
            return false;
        }
        ParsedValueImpl parsedValueImpl = (ParsedValueImpl)((Object)object);
        if (this.hash != parsedValueImpl.hash) {
            return false;
        }
        if (this.value instanceof ParsedValueImpl[][]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[][])) {
                return false;
            }
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])this.value;
            ParsedValueImpl[][] arrparsedValueImpl2 = (ParsedValueImpl[][])parsedValueImpl.value;
            if (arrparsedValueImpl.length != arrparsedValueImpl2.length) {
                return false;
            }
            for (int i = 0; i < arrparsedValueImpl.length; ++i) {
                if (arrparsedValueImpl[i] == null && arrparsedValueImpl2[i] == null) continue;
                if (arrparsedValueImpl[i] == null || arrparsedValueImpl2[i] == null) {
                    return false;
                }
                if (arrparsedValueImpl[i].length != arrparsedValueImpl2[i].length) {
                    return false;
                }
                for (int j = 0; j < arrparsedValueImpl[i].length; ++j) {
                    ParsedValueImpl parsedValueImpl2 = arrparsedValueImpl[i][j];
                    ParsedValueImpl parsedValueImpl3 = arrparsedValueImpl2[i][j];
                    if (!(parsedValueImpl2 != null ? !parsedValueImpl2.equals((Object)parsedValueImpl3) : parsedValueImpl3 != null)) continue;
                    return false;
                }
            }
            return true;
        }
        if (this.value instanceof ParsedValueImpl[]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[])) {
                return false;
            }
            ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])this.value;
            ParsedValueImpl[] arrparsedValueImpl3 = (ParsedValueImpl[])parsedValueImpl.value;
            if (arrparsedValueImpl.length != arrparsedValueImpl3.length) {
                return false;
            }
            for (int i = 0; i < arrparsedValueImpl.length; ++i) {
                ParsedValueImpl parsedValueImpl4 = arrparsedValueImpl[i];
                ParsedValueImpl parsedValueImpl5 = arrparsedValueImpl3[i];
                if (!(parsedValueImpl4 != null ? !parsedValueImpl4.equals((Object)parsedValueImpl5) : parsedValueImpl5 != null)) continue;
                return false;
            }
            return true;
        }
        if (this.value instanceof String && parsedValueImpl.value instanceof String) {
            return this.value.toString().equalsIgnoreCase(parsedValueImpl.value.toString());
        }
        return this.value != null ? this.value.equals(parsedValueImpl.value) : parsedValueImpl.value == null;
    }

    public int hashCode() {
        if (this.hash == Integer.MIN_VALUE) {
            this.hash = 17;
            if (this.value instanceof ParsedValueImpl[][]) {
                ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])this.value;
                for (int i = 0; i < arrparsedValueImpl.length; ++i) {
                    for (int j = 0; j < arrparsedValueImpl[i].length; ++j) {
                        ParsedValueImpl parsedValueImpl = arrparsedValueImpl[i][j];
                        this.hash = 37 * this.hash + (parsedValueImpl != null && parsedValueImpl.value != null ? parsedValueImpl.value.hashCode() : 0);
                    }
                }
            } else if (this.value instanceof ParsedValueImpl[]) {
                ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])this.value;
                for (int i = 0; i < arrparsedValueImpl.length; ++i) {
                    if (arrparsedValueImpl[i] == null || arrparsedValueImpl[i].value == null) continue;
                    ParsedValueImpl parsedValueImpl = arrparsedValueImpl[i];
                    this.hash = 37 * this.hash + (parsedValueImpl != null && parsedValueImpl.value != null ? parsedValueImpl.value.hashCode() : 0);
                }
            } else {
                this.hash = 37 * this.hash + (this.value != null ? this.value.hashCode() : 0);
            }
        }
        return this.hash;
    }

    public final void writeBinary(DataOutputStream dataOutputStream, StyleConverter.StringStore stringStore) throws IOException {
        dataOutputStream.writeBoolean(this.lookup);
        if (this.converter != null) {
            dataOutputStream.writeBoolean(true);
            this.converter.writeBinary(dataOutputStream, stringStore);
        } else {
            dataOutputStream.writeBoolean(false);
        }
        if (this.value instanceof ParsedValue) {
            dataOutputStream.writeByte(1);
            ParsedValue parsedValue = (ParsedValue)this.value;
            if (parsedValue instanceof ParsedValueImpl) {
                ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
            } else {
                ParsedValueImpl<Object, T> parsedValueImpl = new ParsedValueImpl<Object, T>(parsedValue.getValue(), parsedValue.getConverter());
                parsedValueImpl.writeBinary(dataOutputStream, stringStore);
            }
        } else if (this.value instanceof ParsedValue[]) {
            dataOutputStream.writeByte(2);
            ParsedValue[] arrparsedValue = (ParsedValue[])this.value;
            if (arrparsedValue != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int n = arrparsedValue != null ? arrparsedValue.length : 0;
            dataOutputStream.writeInt(n);
            for (int i = 0; i < n; ++i) {
                if (arrparsedValue[i] != null) {
                    dataOutputStream.writeByte(1);
                    ParsedValue parsedValue = arrparsedValue[i];
                    if (parsedValue instanceof ParsedValueImpl) {
                        ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
                        continue;
                    }
                    ParsedValueImpl<Object, T> parsedValueImpl = new ParsedValueImpl<Object, T>(parsedValue.getValue(), parsedValue.getConverter());
                    parsedValueImpl.writeBinary(dataOutputStream, stringStore);
                    continue;
                }
                dataOutputStream.writeByte(0);
            }
        } else if (this.value instanceof ParsedValue[][]) {
            dataOutputStream.writeByte(3);
            ParsedValue[][] arrparsedValue = (ParsedValue[][])this.value;
            if (arrparsedValue != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int n = arrparsedValue != null ? arrparsedValue.length : 0;
            dataOutputStream.writeInt(n);
            for (int i = 0; i < n; ++i) {
                ParsedValue[] arrparsedValue2 = arrparsedValue[i];
                if (arrparsedValue2 != null) {
                    dataOutputStream.writeByte(1);
                } else {
                    dataOutputStream.writeByte(0);
                }
                int n2 = arrparsedValue2 != null ? arrparsedValue2.length : 0;
                dataOutputStream.writeInt(n2);
                for (int j = 0; j < n2; ++j) {
                    if (arrparsedValue2[j] != null) {
                        dataOutputStream.writeByte(1);
                        ParsedValue parsedValue = arrparsedValue2[j];
                        if (parsedValue instanceof ParsedValueImpl) {
                            ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
                            continue;
                        }
                        ParsedValueImpl<Object, T> parsedValueImpl = new ParsedValueImpl<Object, T>(parsedValue.getValue(), parsedValue.getConverter());
                        parsedValueImpl.writeBinary(dataOutputStream, stringStore);
                        continue;
                    }
                    dataOutputStream.writeByte(0);
                }
            }
        } else if (this.value instanceof Color) {
            Color color = (Color)this.value;
            dataOutputStream.writeByte(5);
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getRed()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getGreen()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getBlue()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getOpacity()));
        } else if (this.value instanceof Enum) {
            Enum enum_ = (Enum)this.value;
            int n = stringStore.addString(enum_.name());
            dataOutputStream.writeByte(6);
            dataOutputStream.writeShort(n);
        } else if (this.value instanceof Boolean) {
            Boolean bl = (Boolean)this.value;
            dataOutputStream.writeByte(7);
            dataOutputStream.writeBoolean(bl);
        } else if (this.value instanceof Size) {
            Size size = (Size)this.value;
            dataOutputStream.writeByte(9);
            double d = size.getValue();
            long l = Double.doubleToLongBits(d);
            dataOutputStream.writeLong(l);
            int n = stringStore.addString(size.getUnits().name());
            dataOutputStream.writeShort(n);
        } else if (this.value instanceof String) {
            dataOutputStream.writeByte(4);
            int n = stringStore.addString((String)this.value);
            dataOutputStream.writeShort(n);
        } else if (this.value instanceof URL) {
            dataOutputStream.writeByte(8);
            int n = stringStore.addString(this.value.toString());
            dataOutputStream.writeShort(n);
        } else if (this.value == null) {
            dataOutputStream.writeByte(0);
        } else {
            throw new InternalError("cannot writeBinary " + this);
        }
    }

    public static ParsedValueImpl readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        boolean bl = dataInputStream.readBoolean();
        boolean bl2 = dataInputStream.readBoolean();
        StyleConverter styleConverter = bl2 ? StyleConverter.readBinary((DataInputStream)dataInputStream, (String[])arrstring) : null;
        byte by = dataInputStream.readByte();
        if (by == 1) {
            ParsedValueImpl parsedValueImpl = ParsedValueImpl.readBinary(n, dataInputStream, arrstring);
            return new ParsedValueImpl(parsedValueImpl, styleConverter, bl);
        }
        if (by == 2) {
            int n2;
            if (n >= 4) {
                dataInputStream.readByte();
            }
            ParsedValueImpl[] arrparsedValueImpl = (n2 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n2] : null;
            for (int i = 0; i < n2; ++i) {
                byte by2 = dataInputStream.readByte();
                arrparsedValueImpl[i] = by2 == 1 ? ParsedValueImpl.readBinary(n, dataInputStream, arrstring) : null;
            }
            return new ParsedValueImpl(arrparsedValueImpl, styleConverter, bl);
        }
        if (by == 3) {
            int n3;
            if (n >= 4) {
                dataInputStream.readByte();
            }
            ParsedValueImpl[][] arrparsedValueImpl = (n3 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n3][0] : null;
            for (int i = 0; i < n3; ++i) {
                int n4;
                if (n >= 4) {
                    dataInputStream.readByte();
                }
                arrparsedValueImpl[i] = (n4 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n4] : null;
                for (int j = 0; j < n4; ++j) {
                    byte by3 = dataInputStream.readByte();
                    arrparsedValueImpl[i][j] = by3 == 1 ? ParsedValueImpl.readBinary(n, dataInputStream, arrstring) : null;
                }
            }
            return new ParsedValueImpl(arrparsedValueImpl, styleConverter, bl);
        }
        if (by == 5) {
            double d = Double.longBitsToDouble(dataInputStream.readLong());
            double d2 = Double.longBitsToDouble(dataInputStream.readLong());
            double d3 = Double.longBitsToDouble(dataInputStream.readLong());
            double d4 = Double.longBitsToDouble(dataInputStream.readLong());
            return new ParsedValueImpl(Color.color((double)d, (double)d2, (double)d3, (double)d4), styleConverter, bl);
        }
        if (by == 6) {
            short s;
            short s2 = dataInputStream.readShort();
            String string = arrstring[s2];
            if (n == 2 && (s = dataInputStream.readShort()) >= arrstring.length) {
                throw new IllegalArgumentException("bad version " + n);
            }
            ParsedValueImpl parsedValueImpl = new ParsedValueImpl(string, styleConverter, bl);
            return parsedValueImpl;
        }
        if (by == 7) {
            Boolean bl3 = dataInputStream.readBoolean();
            return new ParsedValueImpl(bl3, styleConverter, bl);
        }
        if (by == 9) {
            double d = Double.longBitsToDouble(dataInputStream.readLong());
            SizeUnits sizeUnits = SizeUnits.PX;
            String string = arrstring[dataInputStream.readShort()];
            try {
                sizeUnits = Enum.valueOf(SizeUnits.class, string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.err.println(illegalArgumentException.toString());
            }
            catch (NullPointerException nullPointerException) {
                System.err.println(nullPointerException.toString());
            }
            return new ParsedValueImpl(new Size(d, sizeUnits), styleConverter, bl);
        }
        if (by == 4) {
            String string = arrstring[dataInputStream.readShort()];
            return new ParsedValueImpl(string, styleConverter, bl);
        }
        if (by == 8) {
            String string = arrstring[dataInputStream.readShort()];
            try {
                URL uRL = new URL(string);
                return new ParsedValueImpl(uRL, styleConverter, bl);
            }
            catch (MalformedURLException malformedURLException) {
                throw new InternalError("Exception in Value.readBinary: " + malformedURLException);
            }
        }
        if (by == 0) {
            return new ParsedValueImpl(null, styleConverter, bl);
        }
        throw new InternalError("unknown type: " + by);
    }
}

