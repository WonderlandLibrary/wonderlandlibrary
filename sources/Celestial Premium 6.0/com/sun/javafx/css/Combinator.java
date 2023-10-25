/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

public class Combinator
extends Enum<Combinator> {
    public static final /* enum */ Combinator CHILD = new Combinator(){

        public String toString() {
            return ">";
        }
    };
    public static final /* enum */ Combinator DESCENDANT = new Combinator(){

        public String toString() {
            return " ";
        }
    };
    private static final /* synthetic */ Combinator[] $VALUES;

    public static Combinator[] values() {
        return (Combinator[])$VALUES.clone();
    }

    public static Combinator valueOf(String string) {
        return Enum.valueOf(Combinator.class, string);
    }

    private static /* synthetic */ Combinator[] $values() {
        return new Combinator[]{CHILD, DESCENDANT};
    }

    static {
        $VALUES = Combinator.$values();
    }
}

