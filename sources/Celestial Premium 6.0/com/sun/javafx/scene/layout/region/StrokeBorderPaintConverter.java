/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class StrokeBorderPaintConverter
extends StyleConverter<ParsedValue<?, Paint>[], Paint[]> {
    private static final StrokeBorderPaintConverter STROKE_BORDER_PAINT_CONVERTER = new StrokeBorderPaintConverter();

    public static StrokeBorderPaintConverter getInstance() {
        return STROKE_BORDER_PAINT_CONVERTER;
    }

    private StrokeBorderPaintConverter() {
    }

    public Paint[] convert(ParsedValue<ParsedValue<?, Paint>[], Paint[]> parsedValue, Font font) {
        Paint[] arrpaint;
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        arrpaint = new Paint[]{arrparsedValue.length > 0 ? (Paint)arrparsedValue[0].convert(font) : Color.BLACK, arrparsedValue.length > 1 ? (Paint)arrparsedValue[1].convert(font) : arrpaint[0], arrparsedValue.length > 2 ? (Paint)arrparsedValue[2].convert(font) : arrpaint[0], arrparsedValue.length > 3 ? (Paint)arrparsedValue[3].convert(font) : arrpaint[1]};
        return arrpaint;
    }

    public String toString() {
        return "StrokeBorderPaintConverter";
    }
}

