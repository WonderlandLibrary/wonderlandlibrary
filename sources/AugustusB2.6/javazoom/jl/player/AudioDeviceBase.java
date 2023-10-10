// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Decoder;

public abstract class AudioDeviceBase implements AudioDevice
{
    private boolean open;
    private Decoder decoder;
    
    public AudioDeviceBase() {
        this.open = false;
        this.decoder = null;
    }
    
    @Override
    public synchronized void open(final Decoder decoder) throws JavaLayerException {
        if (!this.isOpen()) {
            this.decoder = decoder;
            this.openImpl();
            this.setOpen(true);
        }
    }
    
    protected void openImpl() throws JavaLayerException {
    }
    
    protected void setOpen(final boolean open) {
        this.open = open;
    }
    
    @Override
    public synchronized boolean isOpen() {
        return this.open;
    }
    
    @Override
    public synchronized void close() {
        if (this.isOpen()) {
            this.closeImpl();
            this.setOpen(false);
            this.decoder = null;
        }
    }
    
    protected void closeImpl() {
    }
    
    @Override
    public void write(final short[] samples, final int offs, final int len) throws JavaLayerException {
        if (this.isOpen()) {
            this.writeImpl(samples, offs, len);
        }
    }
    
    protected void writeImpl(final short[] samples, final int offs, final int len) throws JavaLayerException {
    }
    
    @Override
    public void flush() {
        if (this.isOpen()) {
            this.flushImpl();
        }
    }
    
    protected void flushImpl() {
    }
    
    protected Decoder getDecoder() {
        return this.decoder;
    }
}
