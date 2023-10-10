// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.io.OutputStreamWriter;
import java.io.Writer;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.InputStream;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract class ByteSink
{
    protected ByteSink() {
    }
    
    public CharSink asCharSink(final Charset charset) {
        return new AsCharSink(charset);
    }
    
    public abstract OutputStream openStream() throws IOException;
    
    public OutputStream openBufferedStream() throws IOException {
        final OutputStream out = this.openStream();
        return (out instanceof BufferedOutputStream) ? out : new BufferedOutputStream(out);
    }
    
    public void write(final byte[] bytes) throws IOException {
        Preconditions.checkNotNull(bytes);
        final Closer closer = Closer.create();
        try {
            final OutputStream out = closer.register(this.openStream());
            out.write(bytes);
            out.flush();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CanIgnoreReturnValue
    public long writeFrom(final InputStream input) throws IOException {
        Preconditions.checkNotNull(input);
        final Closer closer = Closer.create();
        try {
            final OutputStream out = closer.register(this.openStream());
            final long written = ByteStreams.copy(input, out);
            out.flush();
            return written;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    private final class AsCharSink extends CharSink
    {
        private final Charset charset;
        
        private AsCharSink(final Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }
        
        @Override
        public Writer openStream() throws IOException {
            return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
        }
        
        @Override
        public String toString() {
            return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
        }
    }
}
