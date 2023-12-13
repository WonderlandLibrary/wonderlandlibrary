/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.number.Modifier;

public class ParameterizedModifier {
    private final Modifier positive;
    private final Modifier zero;
    private final Modifier negative;
    final Modifier[] mods;
    boolean frozen;

    public ParameterizedModifier(Modifier positive, Modifier zero, Modifier negative) {
        this.positive = positive;
        this.zero = zero;
        this.negative = negative;
        this.mods = null;
        this.frozen = true;
    }

    public ParameterizedModifier() {
        this.positive = null;
        this.zero = null;
        this.negative = null;
        this.mods = new Modifier[3 * StandardPlural.COUNT];
        this.frozen = false;
    }

    public void setModifier(int signum, StandardPlural plural, Modifier mod) {
        assert (!this.frozen);
        this.mods[ParameterizedModifier.getModIndex((int)signum, (StandardPlural)plural)] = mod;
    }

    public void freeze() {
        this.frozen = true;
    }

    public Modifier getModifier(int signum) {
        assert (this.frozen);
        assert (this.mods == null);
        return signum == 0 ? this.zero : (signum < 0 ? this.negative : this.positive);
    }

    public Modifier getModifier(int signum, StandardPlural plural) {
        assert (this.frozen);
        assert (this.positive == null);
        return this.mods[ParameterizedModifier.getModIndex(signum, plural)];
    }

    private static int getModIndex(int signum, StandardPlural plural) {
        return plural.ordinal() * 3 + (signum + 1);
    }
}

