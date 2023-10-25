/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum DisplayContext {
    STANDARD_NAMES(Type.DIALECT_HANDLING, 0),
    DIALECT_NAMES(Type.DIALECT_HANDLING, 1),
    CAPITALIZATION_NONE(Type.CAPITALIZATION, 0),
    CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE(Type.CAPITALIZATION, 1),
    CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE(Type.CAPITALIZATION, 2),
    CAPITALIZATION_FOR_UI_LIST_OR_MENU(Type.CAPITALIZATION, 3),
    CAPITALIZATION_FOR_STANDALONE(Type.CAPITALIZATION, 4);

    private final Type type;
    private final int value;

    private DisplayContext(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public Type type() {
        return this.type;
    }

    public int value() {
        return this.value;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Type {
        DIALECT_HANDLING,
        CAPITALIZATION;

    }
}

