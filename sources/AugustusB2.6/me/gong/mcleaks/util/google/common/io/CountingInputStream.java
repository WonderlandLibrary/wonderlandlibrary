// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.InputStream;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.FilterInputStream;

@Beta
@GwtIncompatible
public final class CountingInputStream extends FilterInputStream
{
    private long count;
    private long mark;
    
    public CountingInputStream(final InputStream in) {
        super(Preconditions.checkNotNull(in));
        this.mark = -1L;
    }
    
    public long getCount() {
        return this.count;
    }
    
    @Override
    public int read() throws IOException {
        final int result = this.in.read();
        if (result != -1) {
            ++this.count;
        }
        return result;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int result = this.in.read(b, off, len);
        if (result != -1) {
            this.count += result;
        }
        return result;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        final long result = this.in.skip(n);
        this.count += result;
        return result;
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.in.mark(readlimit);
        this.mark = this.count;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
        }
        if (this.mark == -1L) {
            throw new IOException("Mark not set");
        }
        this.in.reset();
        this.count = this.mark;
    }
}
