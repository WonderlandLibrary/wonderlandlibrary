// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.converter;

import javazoom.jl.decoder.Crc16;
import javazoom.jl.decoder.JavaLayerException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class jlc
{
    public static void main(final String[] args) {
        final long start = System.currentTimeMillis();
        final int argc = args.length + 1;
        final String[] argv = new String[argc];
        argv[0] = "jlc";
        for (int i = 0; i < args.length; ++i) {
            argv[i + 1] = args[i];
        }
        final jlcArgs ma = new jlcArgs();
        if (!ma.processArgs(argv)) {
            System.exit(1);
        }
        final Converter conv = new Converter();
        final int detail = ma.verbose_mode ? ma.verbose_level : 0;
        final Converter.ProgressListener listener = new Converter.PrintWriterProgressListener(new PrintWriter(System.out, true), detail);
        try {
            conv.convert(ma.filename, ma.output_filename, listener);
        }
        catch (JavaLayerException ex) {
            System.err.println("Convertion failure: " + ex);
        }
        System.exit(0);
    }
    
    static class jlcArgs
    {
        public int which_c;
        public int output_mode;
        public boolean use_own_scalefactor;
        public float scalefactor;
        public String output_filename;
        public String filename;
        public boolean verbose_mode;
        public int verbose_level;
        
        public jlcArgs() {
            this.verbose_level = 3;
            this.which_c = 0;
            this.use_own_scalefactor = false;
            this.scalefactor = 32768.0f;
            this.verbose_mode = false;
        }
        
        public boolean processArgs(final String[] argv) {
            this.filename = null;
            final Crc16[] crc = { null };
            final int argc = argv.length;
            this.verbose_mode = false;
            this.output_mode = 0;
            this.output_filename = "";
            if (argc < 2 || argv[1].equals("-h")) {
                return this.Usage();
            }
            for (int i = 1; i < argc; ++i) {
                if (argv[i].charAt(0) == '-') {
                    if (argv[i].startsWith("-v")) {
                        this.verbose_mode = true;
                        if (argv[i].length() > 2) {
                            try {
                                final String level = argv[i].substring(2);
                                this.verbose_level = Integer.parseInt(level);
                            }
                            catch (NumberFormatException ex) {
                                System.err.println("Invalid verbose level. Using default.");
                            }
                        }
                        System.out.println("Verbose Activated (level " + this.verbose_level + ")");
                    }
                    else {
                        if (!argv[i].equals("-p")) {
                            return this.Usage();
                        }
                        if (++i == argc) {
                            System.out.println("Please specify an output filename after the -p option!");
                            System.exit(1);
                        }
                        this.output_filename = argv[i];
                    }
                }
                else {
                    this.filename = argv[i];
                    System.out.println("FileName = " + argv[i]);
                    if (this.filename == null) {
                        return this.Usage();
                    }
                }
            }
            return this.filename != null || this.Usage();
        }
        
        public boolean Usage() {
            System.out.println("JavaLayer Converter :");
            System.out.println("  -v[x]         verbose mode. ");
            System.out.println("                default = 2");
            System.out.println("  -p name    output as a PCM wave file");
            System.out.println("");
            System.out.println("  More info on http://www.javazoom.net");
            return false;
        }
    }
}
