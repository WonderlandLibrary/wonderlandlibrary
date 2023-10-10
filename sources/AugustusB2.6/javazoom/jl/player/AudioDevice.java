// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Decoder;

public interface AudioDevice
{
    void open(final Decoder p0) throws JavaLayerException;
    
    boolean isOpen();
    
    void write(final short[] p0, final int p1, final int p2) throws JavaLayerException;
    
    void close();
    
    void flush();
    
    int getPosition();
}
