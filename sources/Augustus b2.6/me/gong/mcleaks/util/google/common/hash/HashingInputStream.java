// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.InputStream;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.FilterInputStream;

@Beta
public final class HashingInputStream extends FilterInputStream
{
    private final Hasher hasher;
    
    public HashingInputStream(final HashFunction hashFunction, final InputStream in) {
        super(Preconditions.checkNotNull(in));
        this.hasher = Preconditions.checkNotNull(hashFunction.newHasher());
    }
    
    @CanIgnoreReturnValue
    @Override
    public int read() throws IOException {
        final int b = this.in.read();
        if (b != -1) {
            this.hasher.putByte((byte)b);
        }
        return b;
    }
    
    @CanIgnoreReturnValue
    @Override
    public int read(final byte[] bytes, final int off, final int len) throws IOException {
        final int numOfBytesRead = this.in.read(bytes, off, len);
        if (numOfBytesRead != -1) {
            this.hasher.putBytes(bytes, off, numOfBytesRead);
        }
        return numOfBytesRead;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public void mark(final int readlimit) {
    }
    
    @Override
    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }
    
    public HashCode hash() {
        return this.hasher.hash();
    }
}
