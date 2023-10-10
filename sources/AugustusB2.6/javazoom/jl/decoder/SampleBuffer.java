// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public class SampleBuffer extends Obuffer
{
    private short[] buffer;
    private int[] bufferp;
    private int channels;
    private int frequency;
    
    public SampleBuffer(final int sample_frequency, final int number_of_channels) {
        this.buffer = new short[2304];
        this.bufferp = new int[2];
        this.channels = number_of_channels;
        this.frequency = sample_frequency;
        for (int i = 0; i < number_of_channels; ++i) {
            this.bufferp[i] = (short)i;
        }
    }
    
    public int getChannelCount() {
        return this.channels;
    }
    
    public int getSampleFrequency() {
        return this.frequency;
    }
    
    public short[] getBuffer() {
        return this.buffer;
    }
    
    public int getBufferLength() {
        return this.bufferp[0];
    }
    
    @Override
    public void append(final int channel, final short value) {
        this.buffer[this.bufferp[channel]] = value;
        final int[] bufferp = this.bufferp;
        bufferp[channel] += this.channels;
    }
    
    @Override
    public void appendSamples(final int channel, final float[] f) {
        int pos = this.bufferp[channel];
        float fs;
        short s;
        for (int i = 0; i < 32; fs = f[i++], fs = ((fs > 32767.0f) ? 32767.0f : ((fs < -32767.0f) ? -32767.0f : fs)), s = (short)fs, this.buffer[pos] = s, pos += this.channels) {}
        this.bufferp[channel] = pos;
    }
    
    @Override
    public void write_buffer(final int val) {
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void clear_buffer() {
        for (int i = 0; i < this.channels; ++i) {
            this.bufferp[i] = (short)i;
        }
    }
    
    @Override
    public void set_stop_flag() {
    }
}
