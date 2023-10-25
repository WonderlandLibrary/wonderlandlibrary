/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.paint.Paint
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public final class LayeredBorderPaintConverter
extends StyleConverter<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> {
    private static final LayeredBorderPaintConverter LAYERED_BORDER_PAINT_CONVERTER = new LayeredBorderPaintConverter();

    public static LayeredBorderPaintConverter getInstance() {
        return LAYERED_BORDER_PAINT_CONVERTER;
    }

    private LayeredBorderPaintConverter() {
    }

    public Paint[][] convert(ParsedValue<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Paint[][] arrpaint = new Paint[arrparsedValue.length][0];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrpaint[i] = StrokeBorderPaintConverter.getInstance().convert((ParsedValue<ParsedValue<?, Paint>[], Paint[]>)arrparsedValue[i], font);
        }
        return arrpaint;
    }

    public String toString() {
        return "LayeredBorderPaintConverter";
    }
}

