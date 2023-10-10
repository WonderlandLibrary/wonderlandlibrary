// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;

public class jlp
{
    private String fFilename;
    private boolean remote;
    
    public static void main(final String[] args) {
        int retval = 0;
        try {
            final jlp player = createInstance(args);
            if (player != null) {
                player.play();
            }
        }
        catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace(System.err);
            retval = 1;
        }
        System.exit(retval);
    }
    
    public static jlp createInstance(final String[] args) {
        jlp player = new jlp();
        if (!player.parseArgs(args)) {
            player = null;
        }
        return player;
    }
    
    private jlp() {
        this.fFilename = null;
        this.remote = false;
    }
    
    public jlp(final String filename) {
        this.fFilename = null;
        this.remote = false;
        this.init(filename);
    }
    
    protected void init(final String filename) {
        this.fFilename = filename;
    }
    
    protected boolean parseArgs(final String[] args) {
        boolean parsed = false;
        if (args.length == 1) {
            this.init(args[0]);
            parsed = true;
            this.remote = false;
        }
        else if (args.length == 2) {
            if (!args[0].equals("-url")) {
                this.showUsage();
            }
            else {
                this.init(args[1]);
                parsed = true;
                this.remote = true;
            }
        }
        else {
            this.showUsage();
        }
        return parsed;
    }
    
    public void showUsage() {
        System.out.println("Usage: jlp [-url] <filename>");
        System.out.println("");
        System.out.println(" e.g. : java javazoom.jl.player.jlp localfile.mp3");
        System.out.println("        java javazoom.jl.player.jlp -url http://www.server.com/remotefile.mp3");
        System.out.println("        java javazoom.jl.player.jlp -url http://www.shoutcastserver.com:8000");
    }
    
    public void play() throws JavaLayerException {
        try {
            System.out.println("playing " + this.fFilename + "...");
            InputStream in = null;
            if (this.remote) {
                in = this.getURLInputStream();
            }
            else {
                in = this.getInputStream();
            }
            final AudioDevice dev = this.getAudioDevice();
            final Player player = new Player(in, dev);
            player.play();
        }
        catch (IOException ex) {
            throw new JavaLayerException("Problem playing file " + this.fFilename, ex);
        }
        catch (Exception ex2) {
            throw new JavaLayerException("Problem playing file " + this.fFilename, ex2);
        }
    }
    
    protected InputStream getURLInputStream() throws Exception {
        final URL url = new URL(this.fFilename);
        final InputStream fin = url.openStream();
        final BufferedInputStream bin = new BufferedInputStream(fin);
        return bin;
    }
    
    protected InputStream getInputStream() throws IOException {
        final FileInputStream fin = new FileInputStream(this.fFilename);
        final BufferedInputStream bin = new BufferedInputStream(fin);
        return bin;
    }
    
    protected AudioDevice getAudioDevice() throws JavaLayerException {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }
}
