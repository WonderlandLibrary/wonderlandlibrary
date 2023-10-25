/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.layout.BackgroundPosition
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

public final class LayeredBackgroundPositionConverter
extends StyleConverter<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> {
    private static final LayeredBackgroundPositionConverter LAYERED_BACKGROUND_POSITION_CONVERTER = new LayeredBackgroundPositionConverter();

    public static LayeredBackgroundPositionConverter getInstance() {
        return LAYERED_BACKGROUND_POSITION_CONVERTER;
    }

    private LayeredBackgroundPositionConverter() {
    }

    public BackgroundPosition[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        BackgroundPosition[] arrbackgroundPosition = new BackgroundPosition[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrbackgroundPosition[i] = (BackgroundPosition)arrparsedValue[i].convert(font);
        }
        return arrbackgroundPosition;
    }

    public String toString() {
        return "LayeredBackgroundPositionConverter";
    }
}

