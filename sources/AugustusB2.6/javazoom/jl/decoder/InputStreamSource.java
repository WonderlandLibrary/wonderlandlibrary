// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource implements Source
{
    private final InputStream in;
    
    public InputStreamSource(final InputStream in) {
        if (in == null) {
            throw new NullPointerException("in");
        }
        this.in = in;
    }
    
    @Override
    public int read(final byte[] b, final int offs, final int len) throws IOException {
        final int read = this.in.read(b, offs, len);
        return read;
    }
    
    @Override
    public boolean willReadBlock() {
        return true;
    }
    
    @Override
    public boolean isSeekable() {
        return false;
    }
    
    @Override
    public long tell() {
        return -1L;
    }
    
    @Override
    public long seek(final long to) {
        return -1L;
    }
    
    @Override
    public long length() {
        return -1L;
    }
}
