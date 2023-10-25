/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.Size
 *  javafx.css.SizeUnits
 *  javafx.css.StyleConverter
 *  javafx.scene.layout.CornerRadii
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import javafx.css.ParsedValue;
import javafx.css.Size;
import javafx.css.SizeUnits;
import javafx.css.StyleConverter;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Font;

public final class CornerRadiiConverter
extends StyleConverter<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> {
    private static final CornerRadiiConverter INSTANCE = new CornerRadiiConverter();

    public static CornerRadiiConverter getInstance() {
        return INSTANCE;
    }

    private CornerRadiiConverter() {
    }

    public CornerRadii[] convert(ParsedValue<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        CornerRadii[] arrcornerRadii = new CornerRadii[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            ParsedValue[][] arrparsedValue2 = (ParsedValue[][])arrparsedValue[i].getValue();
            Size size = (Size)arrparsedValue2[0][0].convert(font);
            Size size2 = (Size)arrparsedValue2[0][1].convert(font);
            Size size3 = (Size)arrparsedValue2[0][2].convert(font);
            Size size4 = (Size)arrparsedValue2[0][3].convert(font);
            Size size5 = (Size)arrparsedValue2[1][0].convert(font);
            Size size6 = (Size)arrparsedValue2[1][1].convert(font);
            Size size7 = (Size)arrparsedValue2[1][2].convert(font);
            Size size8 = (Size)arrparsedValue2[1][3].convert(font);
            arrcornerRadii[i] = new CornerRadii(size.pixels(font), size5.pixels(font), size6.pixels(font), size2.pixels(font), size3.pixels(font), size7.pixels(font), size8.pixels(font), size4.pixels(font), size.getUnits() == SizeUnits.PERCENT, size5.getUnits() == SizeUnits.PERCENT, size6.getUnits() == SizeUnits.PERCENT, size2.getUnits() == SizeUnits.PERCENT, size3.getUnits() == SizeUnits.PERCENT, size7.getUnits() == SizeUnits.PERCENT, size7.getUnits() == SizeUnits.PERCENT, size4.getUnits() == SizeUnits.PERCENT);
        }
        return arrcornerRadii;
    }
}

