// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public class Decoder implements DecoderErrors
{
    private static final Params DEFAULT_PARAMS;
    private Obuffer output;
    private SynthesisFilter filter1;
    private SynthesisFilter filter2;
    private LayerIIIDecoder l3decoder;
    private LayerIIDecoder l2decoder;
    private LayerIDecoder l1decoder;
    private int outputFrequency;
    private int outputChannels;
    private Equalizer equalizer;
    private Params params;
    private boolean initialized;
    
    public Decoder() {
        this(null);
    }
    
    public Decoder(Params params0) {
        this.equalizer = new Equalizer();
        if (params0 == null) {
            params0 = Decoder.DEFAULT_PARAMS;
        }
        this.params = params0;
        final Equalizer eq = this.params.getInitialEqualizerSettings();
        if (eq != null) {
            this.equalizer.setFrom(eq);
        }
    }
    
    public static Params getDefaultParams() {
        return (Params)Decoder.DEFAULT_PARAMS.clone();
    }
    
    public void setEqualizer(Equalizer eq) {
        if (eq == null) {
            eq = Equalizer.PASS_THRU_EQ;
        }
        this.equalizer.setFrom(eq);
        final float[] factors = this.equalizer.getBandFactors();
        if (this.filter1 != null) {
            this.filter1.setEQ(factors);
        }
        if (this.filter2 != null) {
            this.filter2.setEQ(factors);
        }
    }
    
    public Obuffer decodeFrame(final Header header, final Bitstream stream) throws DecoderException {
        if (!this.initialized) {
            this.initialize(header);
        }
        final int layer = header.layer();
        this.output.clear_buffer();
        final FrameDecoder decoder = this.retrieveDecoder(header, stream, layer);
        decoder.decodeFrame();
        this.output.write_buffer(1);
        return this.output;
    }
    
    public void setOutputBuffer(final Obuffer out) {
        this.output = out;
    }
    
    public int getOutputFrequency() {
        return this.outputFrequency;
    }
    
    public int getOutputChannels() {
        return this.outputChannels;
    }
    
    public int getOutputBlockSize() {
        return 2304;
    }
    
    protected DecoderException newDecoderException(final int errorcode) {
        return new DecoderException(errorcode, null);
    }
    
    protected DecoderException newDecoderException(final int errorcode, final Throwable throwable) {
        return new DecoderException(errorcode, throwable);
    }
    
    protected FrameDecoder retrieveDecoder(final Header header, final Bitstream stream, final int layer) throws DecoderException {
        FrameDecoder decoder = null;
        switch (layer) {
            case 3: {
                if (this.l3decoder == null) {
                    this.l3decoder = new LayerIIIDecoder(stream, header, this.filter1, this.filter2, this.output, 0);
                }
                decoder = this.l3decoder;
                break;
            }
            case 2: {
                if (this.l2decoder == null) {
                    (this.l2decoder = new LayerIIDecoder()).create(stream, header, this.filter1, this.filter2, this.output, 0);
                }
                decoder = this.l2decoder;
                break;
            }
            case 1: {
                if (this.l1decoder == null) {
                    (this.l1decoder = new LayerIDecoder()).create(stream, header, this.filter1, this.filter2, this.output, 0);
                }
                decoder = this.l1decoder;
                break;
            }
        }
        if (decoder == null) {
            throw this.newDecoderException(513, null);
        }
        return decoder;
    }
    
    private void initialize(final Header header) throws DecoderException {
        final float scalefactor = 32700.0f;
        final int mode = header.mode();
        final int layer = header.layer();
        final int channels = (mode == 3) ? 1 : 2;
        if (this.output == null) {
            this.output = new SampleBuffer(header.frequency(), channels);
        }
        final float[] factors = this.equalizer.getBandFactors();
        this.filter1 = new SynthesisFilter(0, scalefactor, factors);
        if (channels == 2) {
            this.filter2 = new SynthesisFilter(1, scalefactor, factors);
        }
        this.outputChannels = channels;
        this.outputFrequency = header.frequency();
        this.initialized = true;
    }
    
    static {
        DEFAULT_PARAMS = new Params();
    }
    
    public static class Params implements Cloneable
    {
        private OutputChannels outputChannels;
        private Equalizer equalizer;
        
        public Params() {
            this.outputChannels = OutputChannels.BOTH;
            this.equalizer = new Equalizer();
        }
        
        public Object clone() {
            try {
                return super.clone();
            }
            catch (CloneNotSupportedException ex) {
                throw new InternalError(this + ": " + ex);
            }
        }
        
        public void setOutputChannels(final OutputChannels out) {
            if (out == null) {
                throw new NullPointerException("out");
            }
            this.outputChannels = out;
        }
        
        public OutputChannels getOutputChannels() {
            return this.outputChannels;
        }
        
        public Equalizer getInitialEqualizerSettings() {
            return this.equalizer;
        }
    }
}
