/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

public final class FileChooserType
extends Enum<FileChooserType> {
    public static final /* enum */ FileChooserType OPEN = new FileChooserType();
    public static final /* enum */ FileChooserType OPEN_MULTIPLE = new FileChooserType();
    public static final /* enum */ FileChooserType SAVE = new FileChooserType();
    private static final /* synthetic */ FileChooserType[] $VALUES;

    public static FileChooserType[] values() {
        return (FileChooserType[])$VALUES.clone();
    }

    public static FileChooserType valueOf(String string) {
        return Enum.valueOf(FileChooserType.class, string);
    }

    private static /* synthetic */ FileChooserType[] $values() {
        return new FileChooserType[]{OPEN, OPEN_MULTIPLE, SAVE};
    }

    static {
        $VALUES = FileChooserType.$values();
    }
}

