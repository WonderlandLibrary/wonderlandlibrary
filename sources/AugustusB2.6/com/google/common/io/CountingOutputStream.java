// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import com.google.common.annotations.GwtIncompatible;
import java.io.FilterOutputStream;

@ElementTypesAreNonnullByDefault
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
