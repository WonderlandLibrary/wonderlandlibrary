// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player.advanced;

import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.decoder.JavaLayerException;
import java.io.InputStream;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Bitstream;

public class AdvancedPlayer
{
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audio;
    private boolean closed;
    private boolean complete;
    private int lastPosition;
    private PlaybackListener listener;
    
    public AdvancedPlayer(final InputStream stream) throws JavaLayerException {
        this(stream, null);
    }
    
    public AdvancedPlayer(final InputStream stream, final AudioDevice device) throws JavaLayerException {
        this.closed = false;
        this.complete = false;
        this.lastPosition = 0;
        this.bitstream = new Bitstream(stream);
        if (device != null) {
            this.audio = device;
        }
        else {
            this.audio = FactoryRegistry.systemRegistry().createAudioDevice();
        }
        this.audio.open(this.decoder = new Decoder());
    }
    
    public void play() throws JavaLayerException {
        this.play(Integer.MAX_VALUE);
    }
    
    public boolean play(int frames) throws JavaLayerException {
        boolean ret = true;
        if (this.listener != null) {
            this.listener.playbackStarted(this.createEvent(PlaybackEvent.STARTED));
        }
        while (frames-- > 0 && ret) {
            ret = this.decodeFrame();
        }
        final AudioDevice out = this.audio;
        if (out != null) {
            out.flush();
            synchronized (this) {
                this.complete = !this.closed;
                this.close();
            }
            if (this.listener != null) {
                this.listener.playbackFinished(this.createEvent(out, PlaybackEvent.STOPPED));
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
            catch (BitstreamException ex) {}
        }
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
        catch (RuntimeException ex) {
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }
    
    protected boolean skipFrame() throws JavaLayerException {
        final Header h = this.bitstream.readFrame();
        if (h == null) {
            return false;
        }
        this.bitstream.closeFrame();
        return true;
    }
    
    public boolean play(final int start, final int end) throws JavaLayerException {
        boolean ret = true;
        for (int offset = start; offset-- > 0 && ret; ret = this.skipFrame()) {}
        return this.play(end - start);
    }
    
    private PlaybackEvent createEvent(final int id) {
        return this.createEvent(this.audio, id);
    }
    
    private PlaybackEvent createEvent(final AudioDevice dev, final int id) {
        return new PlaybackEvent(this, id, dev.getPosition());
    }
    
    public void setPlayBackListener(final PlaybackListener listener) {
        this.listener = listener;
    }
    
    public PlaybackListener getPlayBackListener() {
        return this.listener;
    }
    
    public void stop() {
        this.listener.playbackFinished(this.createEvent(PlaybackEvent.STOPPED));
        this.close();
    }
}
