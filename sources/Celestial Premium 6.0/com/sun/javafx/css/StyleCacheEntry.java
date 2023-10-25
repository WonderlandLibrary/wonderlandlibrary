/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.PseudoClass
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css;

import com.sun.javafx.css.CalculatedValue;
import com.sun.javafx.css.PseudoClassState;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.scene.text.Font;

public final class StyleCacheEntry {
    private Map<String, CalculatedValue> calculatedValues;

    public CalculatedValue get(String string) {
        CalculatedValue calculatedValue = null;
        if (this.calculatedValues != null && !this.calculatedValues.isEmpty()) {
            calculatedValue = this.calculatedValues.get(string);
        }
        return calculatedValue;
    }

    public void put(String string, CalculatedValue calculatedValue) {
        if (this.calculatedValues == null) {
            this.calculatedValues = new HashMap<String, CalculatedValue>(5);
        }
        this.calculatedValues.put(string, calculatedValue);
    }

    public static final class Key {
        private final Set<PseudoClass>[] pseudoClassStates;
        private final double fontSize;
        private int hash = Integer.MIN_VALUE;

        public Key(Set<PseudoClass>[] arrset, Font font) {
            this.pseudoClassStates = new Set[arrset.length];
            for (int i = 0; i < arrset.length; ++i) {
                this.pseudoClassStates[i] = new PseudoClassState();
                this.pseudoClassStates[i].addAll(arrset[i]);
            }
            this.fontSize = font != null ? font.getSize() : Font.getDefault().getSize();
        }

        public String toString() {
            return Arrays.toString(this.pseudoClassStates) + ", " + this.fontSize;
        }

        public static int hashCode(double d) {
            long l = Double.doubleToLongBits(d);
            return (int)(l ^ l >>> 32);
        }

        public int hashCode() {
            if (this.hash == Integer.MIN_VALUE) {
                this.hash = Key.hashCode(this.fontSize);
                int n = this.pseudoClassStates != null ? this.pseudoClassStates.length : 0;
                for (int i = 0; i < n; ++i) {
                    Set<PseudoClass> set = this.pseudoClassStates[i];
                    if (set == null) continue;
                    this.hash = 67 * (this.hash + set.hashCode());
                }
            }
            return this.hash;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || object.getClass() != this.getClass()) {
                return false;
            }
            Key key = (Key)object;
            if (this.hash != key.hash) {
                return false;
            }
            double d = this.fontSize - key.fontSize;
            if (d < -1.0E-6 || 1.0E-6 < d) {
                return false;
            }
            if (this.pseudoClassStates == null ^ key.pseudoClassStates == null) {
                return false;
            }
            if (this.pseudoClassStates == null) {
                return true;
            }
            if (this.pseudoClassStates.length != key.pseudoClassStates.length) {
                return false;
            }
            for (int i = 0; i < this.pseudoClassStates.length; ++i) {
                Set<PseudoClass> set = this.pseudoClassStates[i];
                Set<PseudoClass> set2 = key.pseudoClassStates[i];
                if (!(set == null ? set2 != null : !set.equals(set2))) continue;
                return false;
            }
            return true;
        }
    }
}

