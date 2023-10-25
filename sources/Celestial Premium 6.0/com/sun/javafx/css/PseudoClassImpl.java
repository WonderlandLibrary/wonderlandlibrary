/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.PseudoClass
 */
package com.sun.javafx.css;

import javafx.css.PseudoClass;

final class PseudoClassImpl
extends PseudoClass {
    private final String pseudoClassName;
    private final int index;

    PseudoClassImpl(String string, int n) {
        this.pseudoClassName = string;
        this.index = n;
    }

    public String getPseudoClassName() {
        return this.pseudoClassName;
    }

    public String toString() {
        return this.pseudoClassName;
    }

    public int getIndex() {
        return this.index;
    }
}

