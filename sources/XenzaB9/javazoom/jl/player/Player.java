// 
// Decompiled by Procyon v0.6.0
// 

package javazoom.jl.player;

import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import java.io.InputStream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Bitstream;

public class Player
{
    private int frame;
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audio;
    private boolean closed;
    private boolean complete;
    private int lastPosition;
    
    public Player(final InputStream stream) throws JavaLayerException {
        this(stream, null);
    }
    
    public Player(final InputStream stream, final AudioDevice device) throws JavaLayerException {
        this.frame = 0;
        this.closed = false;
        this.complete = false;
        this.lastPosition = 0;
        this.bitstream = new Bitstream(stream);
        this.decoder = new Decoder();
        if (device != null) {
            this.audio = device;
        }
        else {
            final FactoryRegistry r = FactoryRegistry.systemRegistry();
            this.audio = r.createAudioDevice();
        }
        this.audio.open(this.decoder);
    }
    
    public void play() throws JavaLayerException {
        this.play(Integer.MAX_VALUE);
    }
    
    public boolean play(int frames) throws JavaLayerException {
        boolean ret;
        for (ret = true; frames-- > 0 && ret; ret = this.decodeFrame()) {}
        if (!ret) {
            final AudioDevice out = this.audio;
            if (out != null) {
                out.flush();
                synchronized (this) {
                    this.complete = !this.closed;
                    this.close();
                }
            }
        }
        return ret;
    }
    
    public synchronized void close() {
        final AudioDevice out = this.audio;
        if (out != null) {
            this.closed = true;
            this.audio = null;
            out.close();
            this.lastPosition = out.getPosition();
            try {
                this.bitstream.close();
            }
            catch (final BitstreamException ex) {}
        }
    }
    
    public synchronized boolean isComplete() {
        return this.complete;
    }
    
    public int getPosition() {
        int position = this.lastPosition;
        final AudioDevice out = this.audio;
        if (out != null) {
            position = out.getPosition();
        }
        return position;
    }
    
    protected boolean decodeFrame() throws JavaLayerException {
        try {
            AudioDevice out = this.audio;
            if (out == null) {
                return false;
            }
            final Header h = this.bitstream.readFrame();
            if (h == null) {
                return false;
            }
            final SampleBuffer output = (SampleBuffer)this.decoder.decodeFrame(h, this.bitstream);
            synchronized (this) {
                out = this.audio;
                if (out != null) {
                    out.write(output.getBuffer(), 0, output.getBufferLength());
                }
            }
            this.bitstream.closeFrame();
        }
        catch (final RuntimeException ex) {
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }
}
