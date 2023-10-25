/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 *  com.sun.javafx.logging.PlatformLogger$Level
 *  javafx.css.ParsedValue
 *  javafx.css.Size
 *  javafx.css.SizeUnits
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.logging.PlatformLogger;
import com.sun.javafx.util.Logging;
import javafx.css.ParsedValue;
import javafx.css.Size;
import javafx.css.SizeUnits;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public class Margins {
    final double top;
    final double right;
    final double bottom;
    final double left;
    final boolean proportional;

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public Margins(double d, double d2, double d3, double d4, boolean bl) {
        this.top = d;
        this.right = d2;
        this.bottom = d3;
        this.left = d4;
        this.proportional = bl;
    }

    public String toString() {
        return "top: " + this.top + "\nright: " + this.right + "\nbottom: " + this.bottom + "\nleft: " + this.left;
    }

    public static final class SequenceConverter
    extends StyleConverter<ParsedValue<ParsedValue[], Margins>[], Margins[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_CONVERTER_INSTANCE;
        }

        private SequenceConverter() {
        }

        public Margins[] convert(ParsedValue<ParsedValue<ParsedValue[], Margins>[], Margins[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            Margins[] arrmargins = new Margins[arrparsedValue.length];
            for (int i = 0; i < arrparsedValue.length; ++i) {
                arrmargins[i] = Converter.getInstance().convert(arrparsedValue[i], font);
            }
            return arrmargins;
        }

        public String toString() {
            return "MarginsSequenceConverter";
        }
    }

    public static final class Converter
    extends StyleConverter<ParsedValue[], Margins> {
        public static Converter getInstance() {
            return Holder.CONVERTER_INSTANCE;
        }

        private Converter() {
        }

        public Margins convert(ParsedValue<ParsedValue[], Margins> parsedValue, Font font) {
            PlatformLogger platformLogger;
            boolean bl;
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            Size size = arrparsedValue.length > 0 ? (Size)arrparsedValue[0].convert(font) : new Size(0.0, SizeUnits.PX);
            Size size2 = arrparsedValue.length > 1 ? (Size)arrparsedValue[1].convert(font) : size;
            Size size3 = arrparsedValue.length > 2 ? (Size)arrparsedValue[2].convert(font) : size;
            Size size4 = arrparsedValue.length > 3 ? (Size)arrparsedValue[3].convert(font) : size2;
            boolean bl2 = size.getUnits() == SizeUnits.PERCENT || size2.getUnits() == SizeUnits.PERCENT || size3.getUnits() == SizeUnits.PERCENT || size4.getUnits() == SizeUnits.PERCENT;
            boolean bl3 = bl = !bl2 || size.getUnits() == SizeUnits.PERCENT && size2.getUnits() == SizeUnits.PERCENT && size3.getUnits() == SizeUnits.PERCENT && size4.getUnits() == SizeUnits.PERCENT;
            if (!bl && (platformLogger = Logging.getCSSLogger()).isLoggable(PlatformLogger.Level.WARNING)) {
                String string = "units do no match: " + size.toString() + " ," + size2.toString() + " ," + size3.toString() + " ," + size4.toString();
                platformLogger.warning(string);
            }
            bl2 = bl2 && bl;
            double d = size.pixels(font);
            double d2 = size2.pixels(font);
            double d3 = size3.pixels(font);
            double d4 = size4.pixels(font);
            return new Margins(d, d2, d3, d4, bl2);
        }

        public String toString() {
            return "MarginsConverter";
        }
    }

    private static class Holder {
        static Converter CONVERTER_INSTANCE = new Converter();
        static SequenceConverter SEQUENCE_CONVERTER_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

