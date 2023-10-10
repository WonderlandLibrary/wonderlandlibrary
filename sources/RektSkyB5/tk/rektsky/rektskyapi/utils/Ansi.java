/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.utils;

import java.util.ArrayList;
import java.util.Collections;

public final class Ansi {
    public static final String SANE = "\u001b[0m";
    public static final String HIGH_INTENSITY = "\u001b[1m";
    public static final String LOW_INTENSITY = "\u001b[2m";
    public static final String ITALIC = "\u001b[3m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String BLINK = "\u001b[5m";
    public static final String RAPID_BLINK = "\u001b[6m";
    public static final String REVERSE_VIDEO = "\u001b[7m";
    public static final String INVISIBLE_TEXT = "\u001b[8m";
    public static final String BLACK = "\u001b[30m";
    public static final String RED = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE = "\u001b[34m";
    public static final String MAGENTA = "\u001b[35m";
    public static final String CYAN = "\u001b[36m";
    public static final String WHITE = "\u001b[37m";
    public static final String GRAY = "\u001b[90m";
    public static final String DEFAULT_WHITE = "\u001b[97m";
    public static final String BACKGROUND_BLACK = "\u001b[40m";
    public static final String BACKGROUND_RED = "\u001b[41m";
    public static final String BACKGROUND_GREEN = "\u001b[42m";
    public static final String BACKGROUND_YELLOW = "\u001b[43m";
    public static final String BACKGROUND_BLUE = "\u001b[44m";
    public static final String BACKGROUND_MAGENTA = "\u001b[45m";
    public static final String BACKGROUND_CYAN = "\u001b[46m";
    public static final String BACKGROUND_WHITE = "\u001b[47m";
    public static final Ansi HighIntensity;
    public static final Ansi Bold;
    public static final Ansi LowIntensity;
    public static final Ansi Normal;
    public static final Ansi Italic;
    public static final Ansi Underline;
    public static final Ansi Blink;
    public static final Ansi RapidBlink;
    public static final Ansi Black;
    public static final Ansi Red;
    public static final Ansi Green;
    public static final Ansi Yellow;
    public static final Ansi Blue;
    public static final Ansi Magenta;
    public static final Ansi Cyan;
    public static final Ansi White;
    public static final Ansi BgBlack;
    public static final Ansi BgRed;
    public static final Ansi BgGreen;
    public static final Ansi BgYellow;
    public static final Ansi BgBlue;
    public static final Ansi BgMagenta;
    public static final Ansi BgCyan;
    public static final Ansi BgWhite;
    private final String[] codes;
    private final String codes_str;

    public Ansi(String ... codes) {
        this.codes = codes;
        String _codes_str = "";
        for (String code : codes) {
            _codes_str = _codes_str + code;
        }
        this.codes_str = _codes_str;
    }

    public Ansi and(Ansi other) {
        ArrayList both = new ArrayList();
        Collections.addAll(both, this.codes);
        Collections.addAll(both, other.codes);
        return new Ansi(both.toArray(new String[0]));
    }

    public String colorize(String original) {
        return this.codes_str + original + SANE;
    }

    public String format(String template, Object ... args) {
        return this.colorize(String.format(template, args));
    }

    static {
        Bold = HighIntensity = new Ansi(HIGH_INTENSITY);
        Normal = LowIntensity = new Ansi(LOW_INTENSITY);
        Italic = new Ansi(ITALIC);
        Underline = new Ansi(UNDERLINE);
        Blink = new Ansi(BLINK);
        RapidBlink = new Ansi(RAPID_BLINK);
        Black = new Ansi(BLACK);
        Red = new Ansi(RED);
        Green = new Ansi(GREEN);
        Yellow = new Ansi(YELLOW);
        Blue = new Ansi(BLUE);
        Magenta = new Ansi(MAGENTA);
        Cyan = new Ansi(CYAN);
        White = new Ansi(WHITE);
        BgBlack = new Ansi(BACKGROUND_BLACK);
        BgRed = new Ansi(BACKGROUND_RED);
        BgGreen = new Ansi(BACKGROUND_GREEN);
        BgYellow = new Ansi(BACKGROUND_YELLOW);
        BgBlue = new Ansi(BACKGROUND_BLUE);
        BgMagenta = new Ansi(BACKGROUND_MAGENTA);
        BgCyan = new Ansi(BACKGROUND_CYAN);
        BgWhite = new Ansi(BACKGROUND_WHITE);
    }
}

