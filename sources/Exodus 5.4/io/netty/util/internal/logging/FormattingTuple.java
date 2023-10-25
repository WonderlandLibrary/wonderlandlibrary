/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

class FormattingTuple {
    private final Object[] argArray;
    static final FormattingTuple NULL = new FormattingTuple(null);
    private final String message;
    private final Throwable throwable;

    static Object[] trimmedCopy(Object[] objectArray) {
        if (objectArray == null || objectArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        int n = objectArray.length - 1;
        Object[] objectArray2 = new Object[n];
        System.arraycopy(objectArray, 0, objectArray2, 0, n);
        return objectArray2;
    }

    public Object[] getArgArray() {
        return this.argArray;
    }

    public String getMessage() {
        return this.message;
    }

    FormattingTuple(String string, Object[] objectArray, Throwable throwable) {
        this.message = string;
        this.throwable = throwable;
        this.argArray = throwable == null ? objectArray : FormattingTuple.trimmedCopy(objectArray);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    FormattingTuple(String string) {
        this(string, null, null);
    }
}

