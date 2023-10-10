// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public interface BitstreamErrors extends JavaLayerErrors
{
    public static final int UNKNOWN_ERROR = 256;
    public static final int UNKNOWN_SAMPLE_RATE = 257;
    public static final int STREAM_ERROR = 258;
    public static final int UNEXPECTED_EOF = 259;
    public static final int STREAM_EOF = 260;
    public static final int INVALIDFRAME = 261;
    public static final int BITSTREAM_LAST = 511;
}
