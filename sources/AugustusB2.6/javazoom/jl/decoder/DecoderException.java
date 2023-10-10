// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public class DecoderException extends JavaLayerException implements DecoderErrors
{
    private int errorcode;
    
    public DecoderException(final String msg, final Throwable t) {
        super(msg, t);
        this.errorcode = 512;
    }
    
    public DecoderException(final int errorcode, final Throwable t) {
        this(getErrorString(errorcode), t);
        this.errorcode = errorcode;
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
    public static String getErrorString(final int errorcode) {
        return "Decoder errorcode " + Integer.toHexString(errorcode);
    }
}
