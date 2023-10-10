// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

public class DspState
{
    static final float M_PI = 3.1415927f;
    static final int VI_TRANSFORMB = 1;
    static final int VI_WINDOWB = 1;
    int analysisp;
    Info vi;
    int modebits;
    float[][] pcm;
    int pcm_storage;
    int pcm_current;
    int pcm_returned;
    float[] multipliers;
    int envelope_storage;
    int envelope_current;
    int eofflag;
    int lW;
    int W;
    int nW;
    int centerW;
    long granulepos;
    long sequence;
    long glue_bits;
    long time_bits;
    long floor_bits;
    long res_bits;
    float[][][][][] window;
    Object[][] transform;
    CodeBook[] fullbooks;
    Object[] mode;
    byte[] header;
    byte[] header1;
    byte[] header2;
    
    public DspState() {
        this.transform = new Object[2][];
        this.window = new float[2][][][][];
        (this.window[0] = new float[2][][][])[0] = new float[2][][];
        this.window[0][1] = new float[2][][];
        this.window[0][0][0] = new float[2][];
        this.window[0][0][1] = new float[2][];
        this.window[0][1][0] = new float[2][];
        this.window[0][1][1] = new float[2][];
        (this.window[1] = new float[2][][][])[0] = new float[2][][];
        this.window[1][1] = new float[2][][];
        this.window[1][0][0] = new float[2][];
        this.window[1][0][1] = new float[2][];
        this.window[1][1][0] = new float[2][];
        this.window[1][1][1] = new float[2][];
    }
    
    private static int ilog2(int i) {
        int n = 0;
        while (i > 1) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    static float[] window(final int n, final int n2, final int n3, final int n4) {
        final float[] array = new float[n2];
        switch (n) {
            case 0: {
                final int n5 = n2 / 4 - n3 / 2;
                final int n6 = n2 - n2 / 4 - n4 / 2;
                for (int i = 0; i < n3; ++i) {
                    final float n7 = (float)Math.sin((float)((i + 0.5) / n3 * 3.1415927410125732 / 2.0));
                    array[i + n5] = (float)Math.sin((float)(n7 * n7 * 1.5707963705062866));
                }
                for (int j = n5 + n3; j < n6; ++j) {
                    array[j] = 1.0f;
                }
                for (int k = 0; k < n4; ++k) {
                    final float n8 = (float)Math.sin((float)((n4 - k - 0.5) / n4 * 3.1415927410125732 / 2.0));
                    array[k + n6] = (float)Math.sin((float)(n8 * n8 * 1.5707963705062866));
                }
                return array;
            }
            default: {
                return null;
            }
        }
    }
    
    int init(final Info vi, final boolean b) {
        this.vi = vi;
        this.modebits = ilog2(vi.modes);
        this.transform[0] = new Object[1];
        this.transform[1] = new Object[1];
        this.transform[0][0] = new Mdct();
        this.transform[1][0] = new Mdct();
        ((Mdct)this.transform[0][0]).init(vi.blocksizes[0]);
        ((Mdct)this.transform[1][0]).init(vi.blocksizes[1]);
        this.window[0][0][0] = new float[1][];
        this.window[0][0][1] = this.window[0][0][0];
        this.window[0][1][0] = this.window[0][0][0];
        this.window[0][1][1] = this.window[0][0][0];
        this.window[1][0][0] = new float[1][];
        this.window[1][0][1] = new float[1][];
        this.window[1][1][0] = new float[1][];
        this.window[1][1][1] = new float[1][];
        for (int i = 0; i < 1; ++i) {
            this.window[0][0][0][i] = window(i, vi.blocksizes[0], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
            this.window[1][0][0][i] = window(i, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
            this.window[1][0][1][i] = window(i, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[1] / 2);
            this.window[1][1][0][i] = window(i, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[0] / 2);
            this.window[1][1][1][i] = window(i, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[1] / 2);
        }
        this.fullbooks = new CodeBook[vi.books];
        for (int j = 0; j < vi.books; ++j) {
            (this.fullbooks[j] = new CodeBook()).init_decode(vi.book_param[j]);
        }
        this.pcm_storage = 8192;
        this.pcm = new float[vi.channels][];
        for (int k = 0; k < vi.channels; ++k) {
            this.pcm[k] = new float[this.pcm_storage];
        }
        this.lW = 0;
        this.W = 0;
        this.centerW = vi.blocksizes[1] / 2;
        this.pcm_current = this.centerW;
        this.mode = new Object[vi.modes];
        for (int l = 0; l < vi.modes; ++l) {
            final int mapping = vi.mode_param[l].mapping;
            this.mode[l] = FuncMapping.mapping_P[vi.map_type[mapping]].look(this, vi.mode_param[l], vi.map_param[mapping]);
        }
        return 0;
    }
    
    public int synthesis_init(final Info info) {
        this.init(info, false);
        this.pcm_returned = this.centerW;
        this.centerW -= info.blocksizes[this.W] / 4 + info.blocksizes[this.lW] / 4;
        this.granulepos = -1L;
        this.sequence = -1L;
        return 0;
    }
    
    DspState(final Info info) {
        this();
        this.init(info, false);
        this.pcm_returned = this.centerW;
        this.centerW -= info.blocksizes[this.W] / 4 + info.blocksizes[this.lW] / 4;
        this.granulepos = -1L;
        this.sequence = -1L;
    }
    
    public int synthesis_blockin(final Block block) {
        if (this.centerW > this.vi.blocksizes[1] / 2 && this.pcm_returned > 8192) {
            final int n = this.centerW - this.vi.blocksizes[1] / 2;
            final int n2 = (this.pcm_returned < n) ? this.pcm_returned : n;
            this.pcm_current -= n2;
            this.centerW -= n2;
            this.pcm_returned -= n2;
            if (n2 != 0) {
                for (int i = 0; i < this.vi.channels; ++i) {
                    System.arraycopy(this.pcm[i], n2, this.pcm[i], 0, this.pcm_current);
                }
            }
        }
        this.lW = this.W;
        this.W = block.W;
        this.nW = -1;
        this.glue_bits += block.glue_bits;
        this.time_bits += block.time_bits;
        this.floor_bits += block.floor_bits;
        this.res_bits += block.res_bits;
        if (this.sequence + 1L != block.sequence) {
            this.granulepos = -1L;
        }
        this.sequence = block.sequence;
        final int n3 = this.vi.blocksizes[this.W];
        int centerW = this.centerW + this.vi.blocksizes[this.lW] / 4 + n3 / 4;
        final int n4 = centerW - n3 / 2;
        final int pcm_current = n4 + n3;
        int n5 = 0;
        int n6 = 0;
        if (pcm_current > this.pcm_storage) {
            this.pcm_storage = pcm_current + this.vi.blocksizes[1];
            for (int j = 0; j < this.vi.channels; ++j) {
                final float[] array = new float[this.pcm_storage];
                System.arraycopy(this.pcm[j], 0, array, 0, this.pcm[j].length);
                this.pcm[j] = array;
            }
        }
        switch (this.W) {
            case 0: {
                n5 = 0;
                n6 = this.vi.blocksizes[0] / 2;
                break;
            }
            case 1: {
                n5 = this.vi.blocksizes[1] / 4 - this.vi.blocksizes[this.lW] / 4;
                n6 = n5 + this.vi.blocksizes[this.lW] / 2;
                break;
            }
        }
        for (int k = 0; k < this.vi.channels; ++k) {
            final int n7 = n4;
            int l;
            for (l = n5; l < n6; ++l) {
                final float[] array2 = this.pcm[k];
                final int n8 = n7 + l;
                array2[n8] += block.pcm[k][l];
            }
            while (l < n3) {
                this.pcm[k][n7 + l] = block.pcm[k][l];
                ++l;
            }
        }
        if (this.granulepos == -1L) {
            this.granulepos = block.granulepos;
        }
        else {
            this.granulepos += centerW - this.centerW;
            if (block.granulepos != -1L && this.granulepos != block.granulepos) {
                if (this.granulepos > block.granulepos && block.eofflag != 0) {
                    centerW -= (int)(this.granulepos - block.granulepos);
                }
                this.granulepos = block.granulepos;
            }
        }
        this.centerW = centerW;
        this.pcm_current = pcm_current;
        if (block.eofflag != 0) {
            this.eofflag = 1;
        }
        return 0;
    }
    
    public int synthesis_pcmout(final float[][][] array, final int[] array2) {
        if (this.pcm_returned < this.centerW) {
            if (array != null) {
                for (int i = 0; i < this.vi.channels; ++i) {
                    array2[i] = this.pcm_returned;
                }
                array[0] = this.pcm;
            }
            return this.centerW - this.pcm_returned;
        }
        return 0;
    }
    
    public int synthesis_read(final int n) {
        if (n != 0 && this.pcm_returned + n > this.centerW) {
            return -1;
        }
        this.pcm_returned += n;
        return 0;
    }
    
    public void clear() {
    }
}
