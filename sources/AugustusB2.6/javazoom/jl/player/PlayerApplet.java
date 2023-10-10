// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import java.applet.Applet;

public class PlayerApplet extends Applet implements Runnable
{
    public static final String AUDIO_PARAMETER = "audioURL";
    private Player player;
    private Thread playerThread;
    private String fileName;
    
    public PlayerApplet() {
        this.player = null;
        this.playerThread = null;
        this.fileName = null;
    }
    
    protected AudioDevice getAudioDevice() throws JavaLayerException {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }
    
    protected InputStream getAudioStream() {
        InputStream in = null;
        try {
            final URL url = this.getAudioURL();
            if (url != null) {
                in = url.openStream();
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
        return in;
    }
    
    protected String getAudioFileName() {
        String urlString = this.fileName;
        if (urlString == null) {
            urlString = this.getParameter("audioURL");
        }
        return urlString;
    }
    
    protected URL getAudioURL() {
        final String urlString = this.getAudioFileName();
        URL url = null;
        if (urlString != null) {
            try {
                url = new URL(this.getDocumentBase(), urlString);
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
        }
        return url;
    }
    
    public void setFileName(final String name) {
        this.fileName = name;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    protected void stopPlayer() throws JavaLayerException {
        if (this.player != null) {
            this.player.close();
            this.player = null;
            this.playerThread = null;
        }
    }
    
    protected void play(final InputStream in, final AudioDevice dev) throws JavaLayerException {
        this.stopPlayer();
        if (in != null && dev != null) {
            this.player = new Player(in, dev);
            (this.playerThread = this.createPlayerThread()).start();
        }
    }
    
    protected Thread createPlayerThread() {
        return new Thread(this, "Audio player thread");
    }
    
    @Override
    public void init() {
    }
    
    @Override
    public void start() {
        final String name = this.getAudioFileName();
        try {
            final InputStream in = this.getAudioStream();
            final AudioDevice dev = this.getAudioDevice();
            this.play(in, dev);
        }
        catch (JavaLayerException ex) {
            synchronized (System.err) {
                System.err.println("Unable to play " + name);
                ex.printStackTrace(System.err);
            }
        }
    }
    
    @Override
    public void stop() {
        try {
            this.stopPlayer();
        }
        catch (JavaLayerException ex) {
            System.err.println(ex);
        }
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void run() {
        if (this.player != null) {
            try {
                this.player.play();
            }
            catch (JavaLayerException ex) {
                System.err.println("Problem playing audio: " + ex);
            }
        }
    }
}
