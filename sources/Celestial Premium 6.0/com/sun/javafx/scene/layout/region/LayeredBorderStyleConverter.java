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

public final class LayeredBorderStyleConverter
extends StyleConverter<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> {
    private static final LayeredBorderStyleConverter LAYERED_BORDER_STYLE_CONVERTER = new LayeredBorderStyleConverter();

    public static LayeredBorderStyleConverter getInstance() {
        return LAYERED_BORDER_STYLE_CONVERTER;
    }

    private LayeredBorderStyleConverter() {
    }

    public BorderStrokeStyle[][] convert(ParsedValue<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        BorderStrokeStyle[][] arrborderStrokeStyle = new BorderStrokeStyle[arrparsedValue.length][0];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrborderStrokeStyle[i] = (BorderStrokeStyle[])arrparsedValue[i].convert(font);
        }
        return arrborderStrokeStyle;
    }

    public String toString() {
        return "LayeredBorderStyleConverter";
    }
}

