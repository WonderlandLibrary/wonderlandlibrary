// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Native;

public class Kstat2StatusException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private final int kstat2Status;
    
    public Kstat2StatusException(final int ks) {
        this(ks, formatMessage(ks));
    }
    
    protected Kstat2StatusException(final int ks, final String msg) {
        super(msg);
        this.kstat2Status = ks;
    }
    
    public int getKstat2Status() {
        return this.kstat2Status;
    }
    
    private static String formatMessage(final int ks) {
        String status = Kstat2.INSTANCE.kstat2_status_string(ks);
        if (ks == 10) {
            status = status + " (errno=" + Native.getLastError() + ")";
        }
        return "Kstat2Status error code " + ks + ": " + status;
    }
}
