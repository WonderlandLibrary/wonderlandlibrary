// 
// Decompiled by Procyon v0.6.0
// 

package javazoom.jl.player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Line;
import javax.sound.sampled.AudioSystem;
import javazoom.jl.decoder.JavaLayerException;
import javax.sound.sampled.DataLine;
import javazoom.jl.decoder.Decoder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

public class JavaSoundAudioDevice extends AudioDeviceBase
{
    private SourceDataLine source;
    private AudioFormat fmt;
    private byte[] byteBuf;
    
    public JavaSoundAudioDevice() {
        this.source = null;
        this.fmt = null;
        this.byteBuf = new byte[4096];
    }
    
    protected void setAudioFormat(final AudioFormat fmt0) {
        this.fmt = fmt0;
    }
    
    protected AudioFormat getAudioFormat() {
        if (this.fmt == null) {
            final Decoder decoder = this.getDecoder();
            this.fmt = new AudioFormat((float)decoder.getOutputFrequency(), 16, decoder.getOutputChannels(), true, false);
        }
        return this.fmt;
    }
    
    protected DataLine.Info getSourceLineInfo() {
        final AudioFormat fmt = this.getAudioFormat();
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
        return info;
    }
    
    public void open(final AudioFormat fmt) throws JavaLayerException {
        if (!this.isOpen()) {
            this.setAudioFormat(fmt);
            this.openImpl();
            this.setOpen(true);
        }
    }
    
    @Override
    protected void openImpl() throws JavaLayerException {
    }
    
    protected void createSource() throws JavaLayerException {
        Throwable t = null;
        try {
            final Line line = AudioSystem.getLine(this.getSourceLineInfo());
            if (line instanceof SourceDataLine) {
                (this.source = (SourceDataLine)line).open(this.fmt);
                this.source.start();
            }
        }
        catch (final RuntimeException ex) {
            t = ex;
        }
        catch (final LinkageError ex2) {
            t = ex2;
        }
        catch (final LineUnavailableException ex3) {
            t = ex3;
        }
        if (this.source == null) {
            throw new JavaLayerException("cannot obtain source audio line", t);
        }
    }
    
    public int millisecondsToBytes(final AudioFormat fmt, final int time) {
        return (int)(time * (fmt.getSampleRate() * fmt.getChannels() * fmt.getSampleSizeInBits()) / 8000.0);
    }
    
    @Override
    protected void closeImpl() {
        if (this.source != null) {
            this.source.close();
        }
    }
    
    @Override
    protected void writeImpl(final short[] samples, final int offs, final int len) throws JavaLayerException {
        if (this.source == null) {
            this.createSource();
        }
        final byte[] b = this.toByteArray(samples, offs, len);
        this.source.write(b, 0, len * 2);
    }
    
    protected byte[] getByteArray(final int length) {
        if (this.byteBuf.length < length) {
            this.byteBuf = new byte[length + 1024];
        }
        return this.byteBuf;
    }
    
    protected byte[] toByteArray(final short[] samples, int offs, int len) {
        final byte[] b = this.getByteArray(len * 2);
        int idx = 0;
        while (len-- > 0) {
            final short s = samples[offs++];
            b[idx++] = (byte)s;
            b[idx++] = (byte)(s >>> 8);
        }
        return b;
    }
    
    @Override
    protected void flushImpl() {
        if (this.source != null) {
            this.source.drain();
        }
    }
    
    @Override
    public int getPosition() {
        int pos = 0;
        if (this.source != null) {
            pos = (int)(this.source.getMicrosecondPosition() / 1000L);
        }
        return pos;
    }
    
    public void test() throws JavaLayerException {
        try {
            this.open(new AudioFormat(22050.0f, 16, 1, true, false));
            final short[] data = new short[2205];
            this.write(data, 0, data.length);
            this.flush();
            this.close();
        }
        catch (final RuntimeException ex) {
            throw new JavaLayerException("Device test failed: " + ex);
        }
    }
}
