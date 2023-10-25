/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream
extends InputStream {
    private final InheritableThreadLocal<InputStream> m_streams = new InheritableThreadLocal();

    public InputStream bindStream(InputStream input) {
        InputStream oldValue = (InputStream)this.m_streams.get();
        this.m_streams.set(input);
        return oldValue;
    }

    @Override
    public void close() throws IOException {
        InputStream input = (InputStream)this.m_streams.get();
        if (null != input) {
            input.close();
        }
    }

    @Override
    public int read() throws IOException {
        InputStream input = (InputStream)this.m_streams.get();
        if (null != input) {
            return input.read();
        }
        return -1;
    }
}

