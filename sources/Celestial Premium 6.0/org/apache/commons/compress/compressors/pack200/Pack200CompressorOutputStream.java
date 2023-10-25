/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.jar.Pack200;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.apache.commons.compress.compressors.pack200.StreamBridge;
import org.apache.commons.compress.utils.IOUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Pack200CompressorOutputStream
extends CompressorOutputStream {
    private boolean finished = false;
    private final OutputStream originalOutput;
    private final StreamBridge streamBridge;
    private final Map<String, String> properties;

    public Pack200CompressorOutputStream(OutputStream out) throws IOException {
        this(out, Pack200Strategy.IN_MEMORY);
    }

    public Pack200CompressorOutputStream(OutputStream out, Pack200Strategy mode) throws IOException {
        this(out, mode, null);
    }

    public Pack200CompressorOutputStream(OutputStream out, Map<String, String> props) throws IOException {
        this(out, Pack200Strategy.IN_MEMORY, props);
    }

    public Pack200CompressorOutputStream(OutputStream out, Pack200Strategy mode, Map<String, String> props) throws IOException {
        this.originalOutput = out;
        this.streamBridge = mode.newStreamBridge();
        this.properties = props;
    }

    @Override
    public void write(int b) throws IOException {
        this.streamBridge.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.streamBridge.write(b);
    }

    @Override
    public void write(byte[] b, int from, int length) throws IOException {
        this.streamBridge.write(b, from, length);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        this.finish();
        try {
            this.streamBridge.stop();
        }
        finally {
            this.originalOutput.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void finish() throws IOException {
        if (this.finished) return;
        this.finished = true;
        Pack200.Packer p = Pack200.newPacker();
        if (this.properties != null) {
            p.properties().putAll(this.properties);
        }
        JarInputStream ji = null;
        boolean success = false;
        try {
            ji = new JarInputStream(this.streamBridge.getInput());
            p.pack(ji, this.originalOutput);
            return;
        }
        catch (Throwable throwable) {
            if (success) throw throwable;
            IOUtils.closeQuietly(ji);
            throw throwable;
        }
    }
}

