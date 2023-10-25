/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.layout.BorderWidths
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public class BorderImageWidthsSequenceConverter
extends StyleConverter<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> {
    private static final BorderImageWidthsSequenceConverter CONVERTER = new BorderImageWidthsSequenceConverter();

    public static BorderImageWidthsSequenceConverter getInstance() {
        return CONVERTER;
    }

    public BorderWidths[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        BorderWidths[] arrborderWidths = new BorderWidths[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrborderWidths[i] = BorderImageWidthConverter.getInstance().convert((ParsedValue<ParsedValue[], BorderWidths>)arrparsedValue[i], font);
        }
        return arrborderWidths;
    }

    public String toString() {
        return "BorderImageWidthsSequenceConverter";
    }
}

