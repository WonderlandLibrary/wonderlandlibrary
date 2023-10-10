/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import java.io.PrintStream;

public class q
extends Exception {
    private Throwable a;

    public q() {
    }

    public q(String string, Throwable throwable) {
        super(string);
        this.a = throwable;
    }

    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream printStream) {
        if (this.a == null) {
            super.printStackTrace(printStream);
            return;
        }
        this.a.printStackTrace();
    }
}

