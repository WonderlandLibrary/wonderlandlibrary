/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jboss.marshalling.ByteInput
 */
package io.netty.handler.codec.marshalling;

import java.io.IOException;
import org.jboss.marshalling.ByteInput;

class LimitingByteInput
implements ByteInput {
    private final ByteInput input;
    private static final TooBigObjectException EXCEPTION = new TooBigObjectException();
    private long read;
    private final long limit;

    public long skip(long l) throws IOException {
        int n = this.readable((int)l);
        if (n > 0) {
            long l2 = this.input.skip((long)n);
            this.read += l2;
            return l2;
        }
        throw EXCEPTION;
    }

    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3 = this.readable(n2);
        if (n3 > 0) {
            int n4 = this.input.read(byArray, n, n3);
            this.read += (long)n4;
            return n4;
        }
        throw EXCEPTION;
    }

    public void close() throws IOException {
    }

    public int read() throws IOException {
        int n = this.readable(1);
        if (n > 0) {
            int n2 = this.input.read();
            ++this.read;
            return n2;
        }
        throw EXCEPTION;
    }

    private int readable(int n) {
        return (int)Math.min((long)n, this.limit - this.read);
    }

    LimitingByteInput(ByteInput byteInput, long l) {
        if (l <= 0L) {
            throw new IllegalArgumentException("The limit MUST be > 0");
        }
        this.input = byteInput;
        this.limit = l;
    }

    public int available() throws IOException {
        return this.readable(this.input.available());
    }

    static final class TooBigObjectException
    extends IOException {
        private static final long serialVersionUID = 1L;

        TooBigObjectException() {
        }
    }
}

