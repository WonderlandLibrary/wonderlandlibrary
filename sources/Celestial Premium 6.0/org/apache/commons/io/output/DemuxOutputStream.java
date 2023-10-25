/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class DemuxOutputStream
extends OutputStream {
    private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal();

    public OutputStream bindStream(OutputStream output) {
        OutputStream stream = (OutputStream)this.outputStreamThreadLocal.get();
        this.outputStreamThreadLocal.set(output);
        return stream;
    }

    @Override
    public void close() throws IOException {
        OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.close();
        }
    }

    @Override
    public void flush() throws IOException {
        OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.flush();
        }
    }

    @Override
    public void write(int ch) throws IOException {
        OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.write(ch);
        }
    }
}

