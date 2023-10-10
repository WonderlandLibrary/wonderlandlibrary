/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import java.awt.Color;

public class LoseBypassColor {
    public Color color;
    public Rainbow rainbow = Rainbow.OFF;

    public LoseBypassColor(Color color) {
        this.color = color;
    }

    public static enum Rainbow {
        OFF("Off"),
        SLOW("Slow"),
        MEDIUM("Medium"),
        FAST("Fast"),
        PSYCHO("Psycho");

        private final String name;

        private Rainbow(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public Rainbow next() {
            try {
                return Rainbow.values()[this.ordinal() + 1];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return Rainbow.values()[0];
            }
        }
    }
}

