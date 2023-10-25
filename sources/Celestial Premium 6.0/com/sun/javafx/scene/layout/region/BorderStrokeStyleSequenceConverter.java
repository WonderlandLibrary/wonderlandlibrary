/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.layout.BorderStrokeStyle
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

public final class BorderStrokeStyleSequenceConverter
extends StyleConverter<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> {
    private static final BorderStrokeStyleSequenceConverter BORDER_STYLE_SEQUENCE_CONVERTER = new BorderStrokeStyleSequenceConverter();

    public static BorderStrokeStyleSequenceConverter getInstance() {
        return BORDER_STYLE_SEQUENCE_CONVERTER;
    }

    private BorderStrokeStyleSequenceConverter() {
    }

    public BorderStrokeStyle[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> parsedValue, Font font) {
        BorderStrokeStyle[] arrborderStrokeStyle;
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        arrborderStrokeStyle = new BorderStrokeStyle[]{arrparsedValue.length > 0 ? (BorderStrokeStyle)arrparsedValue[0].convert(font) : BorderStrokeStyle.SOLID, arrparsedValue.length > 1 ? (BorderStrokeStyle)arrparsedValue[1].convert(font) : arrborderStrokeStyle[0], arrparsedValue.length > 2 ? (BorderStrokeStyle)arrparsedValue[2].convert(font) : arrborderStrokeStyle[0], arrparsedValue.length > 3 ? (BorderStrokeStyle)arrparsedValue[3].convert(font) : arrborderStrokeStyle[1]};
        return arrborderStrokeStyle;
    }

    public String toString() {
        return "BorderStrokeStyleSequenceConverter";
    }
}

