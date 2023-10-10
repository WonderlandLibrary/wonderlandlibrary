/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.OutputStream;
import org.apache.commons.io.output.ProxyOutputStream;

public class CountingOutputStream
extends ProxyOutputStream {
    private long count = 0L;

    public CountingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    protected synchronized void beforeWrite(int n2) {
        this.count += (long)n2;
    }

    public int getCount() {
        long result = this.getByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int)result;
    }

    public int resetCount() {
        long result = this.resetByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int)result;
    }

    public synchronized long getByteCount() {
        return this.count;
    }

    public synchronized long resetByteCount() {
        long tmp = this.count;
        this.count = 0L;
        return tmp;
    }
}

