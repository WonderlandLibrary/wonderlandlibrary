/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class SliceSequenceConverter
extends StyleConverter<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> {
    private static final SliceSequenceConverter BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER = new SliceSequenceConverter();

    public static SliceSequenceConverter getInstance() {
        return BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER;
    }

    public BorderImageSlices[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        BorderImageSlices[] arrborderImageSlices = new BorderImageSlices[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrborderImageSlices[i] = BorderImageSliceConverter.getInstance().convert(arrparsedValue[i], font);
        }
        return arrborderImageSlices;
    }

    public String toString() {
        return "BorderImageSliceSequenceConverter";
    }
}

