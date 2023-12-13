// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.OutputStream;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.FilterOutputStream;

@Beta
@GwtIncompatible
public final class CountingOutputStream extends FilterOutputStream
{
    private long count;
    
    public CountingOutputStream(final OutputStream out) {
        super(Preconditions.checkNotNull(out));
    }
    
    public long getCount() {
        return this.count;
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
        this.count += len;
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.out.write(b);
        ++this.count;
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
    }
}
