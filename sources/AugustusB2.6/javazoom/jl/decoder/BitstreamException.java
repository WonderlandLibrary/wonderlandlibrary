// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public class BitstreamException extends JavaLayerException implements BitstreamErrors
{
    private int errorcode;
    
    public BitstreamException(final String msg, final Throwable t) {
        super(msg, t);
        this.errorcode = 256;
    }
    
    public BitstreamException(final int errorcode, final Throwable t) {
        this(getErrorString(errorcode), t);
        this.errorcode = errorcode;
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
    public static String getErrorString(final int errorcode) {
        return "Bitstream errorcode " + Integer.toHexString(errorcode);
    }
}
