// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.util.EdidUtil;
import java.util.Arrays;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.Display;

@Immutable
public abstract class AbstractDisplay implements Display
{
    private final byte[] edid;
    
    protected AbstractDisplay(final byte[] edid) {
        this.edid = Arrays.copyOf(edid, edid.length);
    }
    
    @Override
    public byte[] getEdid() {
        return Arrays.copyOf(this.edid, this.edid.length);
    }
    
    @Override
    public String toString() {
        return EdidUtil.toString(this.edid);
    }
}
