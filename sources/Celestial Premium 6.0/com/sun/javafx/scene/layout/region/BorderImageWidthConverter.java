/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.Size
 *  javafx.css.SizeUnits
 *  javafx.css.StyleConverter
 *  javafx.scene.layout.BorderWidths
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import javafx.css.ParsedValue;
import javafx.css.Size;
import javafx.css.SizeUnits;
import javafx.css.StyleConverter;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public class BorderImageWidthConverter
extends StyleConverter<ParsedValue[], BorderWidths> {
    private static final BorderImageWidthConverter CONVERTER_INSTANCE = new BorderImageWidthConverter();

    public static BorderImageWidthConverter getInstance() {
        return CONVERTER_INSTANCE;
    }

    private BorderImageWidthConverter() {
    }

    public BorderWidths convert(ParsedValue<ParsedValue[], BorderWidths> parsedValue, Font font) {
        Size size;
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        assert (arrparsedValue.length == 4);
        double d = 1.0;
        double d2 = 1.0;
        double d3 = 1.0;
        double d4 = 1.0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        ParsedValue parsedValue2 = arrparsedValue[0];
        if ("auto".equals(parsedValue2.getValue())) {
            d = -1.0;
        } else {
            size = (Size)parsedValue2.convert(font);
            d = size.pixels(font);
            bl = size.getUnits() == SizeUnits.PERCENT;
        }
        parsedValue2 = arrparsedValue[1];
        if ("auto".equals(parsedValue2.getValue())) {
            d2 = -1.0;
        } else {
            size = (Size)parsedValue2.convert(font);
            d2 = size.pixels(font);
            bl2 = size.getUnits() == SizeUnits.PERCENT;
        }
        parsedValue2 = arrparsedValue[2];
        if ("auto".equals(parsedValue2.getValue())) {
            d3 = -1.0;
        } else {
            size = (Size)parsedValue2.convert(font);
            d3 = size.pixels(font);
            bl3 = size.getUnits() == SizeUnits.PERCENT;
        }
        parsedValue2 = arrparsedValue[3];
        if ("auto".equals(parsedValue2.getValue())) {
            d4 = -1.0;
        } else {
            size = (Size)parsedValue2.convert(font);
            d4 = size.pixels(font);
            bl4 = size.getUnits() == SizeUnits.PERCENT;
        }
        return new BorderWidths(d, d2, d3, d4, bl, bl2, bl3, bl4);
    }

    public String toString() {
        return "BorderImageWidthConverter";
    }
}

