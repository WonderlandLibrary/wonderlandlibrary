/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.Stylesheet
 */
package com.sun.javafx.css.parser;

import java.io.File;
import java.io.IOException;
import javafx.css.Stylesheet;

public final class Css2Bin {
    public static void main(String[] arrstring) throws Exception {
        if (arrstring.length < 1) {
            throw new IllegalArgumentException("expected file name as argument");
        }
        try {
            String string = arrstring[0];
            String string2 = arrstring.length > 1 ? arrstring[1] : string.substring(0, string.lastIndexOf(46) + 1).concat("bss");
            Css2Bin.convertToBinary(string, string2);
        }
        catch (Exception exception) {
            System.err.println(exception.toString());
            exception.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public static void convertToBinary(String string, String string2) throws IOException {
        if (string == null || string2 == null) {
            throw new IllegalArgumentException("parameters cannot be null");
        }
        if (string.equals(string2)) {
            throw new IllegalArgumentException("input file and output file cannot be the same");
        }
        File file = new File(string);
        File file2 = new File(string2);
        Stylesheet.convertToBinary((File)file, (File)file2);
    }
}

