/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.helpers.Strings;

public class Integers {
    public static int parseInt(String s, int defaultValue) {
        return Strings.isEmpty(s) ? defaultValue : Integer.parseInt(s);
    }

    public static int parseInt(String s) {
        return Integers.parseInt(s, 0);
    }
}

