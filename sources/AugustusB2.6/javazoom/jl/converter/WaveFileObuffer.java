// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.converter;

import javazoom.jl.decoder.Obuffer;

public class WaveFileObuffer extends Obuffer
{
    private short[] buffer;
    private short[] bufferp;
    private int channels;
    private WaveFile outWave;
    short[] myBuffer;
    
    public WaveFileObuffer(final int number_of_channels, final int freq, final String FileName) {
        this.myBuffer = new short[2];
        if (FileName == null) {
            throw new NullPointerException("FileName");
        }
        this.buffer = new short[2304];
        this.bufferp = new short[2];
        this.channels = number_of_channels;
        for (int i = 0; i < number_of_channels; ++i) {
            this.bufferp[i] = (short)i;
        }
        this.outWave = new WaveFile();
        final int rc = this.outWave.OpenForWrite(FileName, freq, (short)16, (short)this.channels);
    }
    
    @Override
    public void append(final int channel, final short value) {
        this.buffer[this.bufferp[channel]] = value;
        final short[] bufferp = this.bufferp;
        bufferp[channel] += (short)this.channels;
    }
    
    @Override
    public void write_buffer(final int val) {
        final int k = 0;
        int rc = 0;
        rc = this.outWave.WriteData(this.buffer, this.bufferp[0]);
        for (int i = 0; i < this.channels; ++i) {
            this.bufferp[i] = (short)i;
        }
    }
    
    @Override
    public void close() {
        this.outWave.Close();
    }
    
    @Override
    public void clear_buffer() {
    }
    
    @Override
    public void set_stop_flag() {
    }
}
