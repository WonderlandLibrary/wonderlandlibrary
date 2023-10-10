// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public class OutputChannels
{
    public static final int BOTH_CHANNELS = 0;
    public static final int LEFT_CHANNEL = 1;
    public static final int RIGHT_CHANNEL = 2;
    public static final int DOWNMIX_CHANNELS = 3;
    public static final OutputChannels LEFT;
    public static final OutputChannels RIGHT;
    public static final OutputChannels BOTH;
    public static final OutputChannels DOWNMIX;
    private int outputChannels;
    
    public static OutputChannels fromInt(final int code) {
        switch (code) {
            case 1: {
                return OutputChannels.LEFT;
            }
            case 2: {
                return OutputChannels.RIGHT;
            }
            case 0: {
                return OutputChannels.BOTH;
            }
            case 3: {
                return OutputChannels.DOWNMIX;
            }
            default: {
                throw new IllegalArgumentException("Invalid channel code: " + code);
            }
        }
    }
    
    private OutputChannels(final int channels) {
        this.outputChannels = channels;
        if (channels < 0 || channels > 3) {
            throw new IllegalArgumentException("channels");
        }
    }
    
    public int getChannelsOutputCode() {
        return this.outputChannels;
    }
    
    public int getChannelCount() {
        final int count = (this.outputChannels == 0) ? 2 : 1;
        return count;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = false;
        if (o instanceof OutputChannels) {
            final OutputChannels oc = (OutputChannels)o;
            equals = (oc.outputChannels == this.outputChannels);
        }
        return equals;
    }
    
    @Override
    public int hashCode() {
        return this.outputChannels;
    }
    
    static {
        LEFT = new OutputChannels(1);
        RIGHT = new OutputChannels(2);
        BOTH = new OutputChannels(0);
        DOWNMIX = new OutputChannels(3);
    }
}
