/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class AppendableOutputStream<T extends Appendable>
extends OutputStream {
    private final T appendable;

    public AppendableOutputStream(T appendable) {
        this.appendable = appendable;
    }

    @Override
    public void write(int b2) throws IOException {
        this.appendable.append((char)b2);
    }

    public T getAppendable() {
        return this.appendable;
    }
}

