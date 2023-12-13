/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.CodeBook;
import com.jcraft.jorbis.FuncMapping;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.Mdct;
import com.jcraft.jorbis.Util;

public class DspState {
    static final float M_PI = (float)Math.PI;
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
    Object[][] transform = new Object[2][];
    CodeBook[] fullbooks;
    Object[] mode;
    byte[] header;
    byte[] header1;
    byte[] header2;

    public DspState() {
        this.window = new float[2][][][][];
        this.window[0] = new float[2][][][];
        this.window[0][0] = new float[2][][];
        this.window[0][1] = new float[2][][];
        this.window[0][0][0] = new float[2][];
        this.window[0][0][1] = new float[2][];
        this.window[0][1][0] = new float[2][];
        this.window[0][1][1] = new float[2][];
        this.window[1] = new float[2][][][];
        this.window[1][0] = new float[2][][];
        this.window[1][1] = new float[2][][];
        this.window[1][0][0] = new float[2][];
        this.window[1][0][1] = new float[2][];
        this.window[1][1][0] = new float[2][];
        this.window[1][1][1] = new float[2][];
    }

    static float[] window(int type, int window, int left, int right) {
        float[] ret = new float[window];
        switch (type) {
            case 0: {
                float x2;
                int i2;
                int leftbegin = window / 4 - left / 2;
                int rightbegin = window - window / 4 - right / 2;
                for (i2 = 0; i2 < left; ++i2) {
                    x2 = (float)(((double)i2 + 0.5) / (double)left * 3.1415927410125732 / 2.0);
                    x2 = (float)Math.sin(x2);
                    x2 *= x2;
                    x2 = (float)((double)x2 * 1.5707963705062866);
                    ret[i2 + leftbegin] = x2 = (float)Math.sin(x2);
                }
                for (i2 = leftbegin + left; i2 < rightbegin; ++i2) {
                    ret[i2] = 1.0f;
                }
                for (i2 = 0; i2 < right; ++i2) {
                    x2 = (float)(((double)(right - i2) - 0.5) / (double)right * 3.1415927410125732 / 2.0);
                    x2 = (float)Math.sin(x2);
                    x2 *= x2;
                    x2 = (float)((double)x2 * 1.5707963705062866);
                    ret[i2 + rightbegin] = x2 = (float)Math.sin(x2);
                }
                break;
            }
            default: {
                return null;
            }
        }
        return ret;
    }

    int init(Info vi, boolean encp) {
        int i2;
        this.vi = vi;
        this.modebits = Util.ilog2(vi.modes);
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
        for (i2 = 0; i2 < 1; ++i2) {
            this.window[0][0][0][i2] = DspState.window(i2, vi.blocksizes[0], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
            this.window[1][0][0][i2] = DspState.window(i2, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
            this.window[1][0][1][i2] = DspState.window(i2, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[1] / 2);
            this.window[1][1][0][i2] = DspState.window(i2, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[0] / 2);
            this.window[1][1][1][i2] = DspState.window(i2, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[1] / 2);
        }
        this.fullbooks = new CodeBook[vi.books];
        for (i2 = 0; i2 < vi.books; ++i2) {
            this.fullbooks[i2] = new CodeBook();
            this.fullbooks[i2].init_decode(vi.book_param[i2]);
        }
        this.pcm_storage = 8192;
        this.pcm = new float[vi.channels][];
        for (i2 = 0; i2 < vi.channels; ++i2) {
            this.pcm[i2] = new float[this.pcm_storage];
        }
        this.lW = 0;
        this.W = 0;
        this.pcm_current = this.centerW = vi.blocksizes[1] / 2;
        this.mode = new Object[vi.modes];
        for (i2 = 0; i2 < vi.modes; ++i2) {
            int mapnum = vi.mode_param[i2].mapping;
            int maptype = vi.map_type[mapnum];
            this.mode[i2] = FuncMapping.mapping_P[maptype].look(this, vi.mode_param[i2], vi.map_param[mapnum]);
        }
        return 0;
    }

    public int synthesis_init(Info vi) {
        this.init(vi, false);
        this.pcm_returned = this.centerW;
        this.centerW -= vi.blocksizes[this.W] / 4 + vi.blocksizes[this.lW] / 4;
        this.granulepos = -1L;
        this.sequence = -1L;
        return 0;
    }

    DspState(Info vi) {
        this();
        this.init(vi, false);
        this.pcm_returned = this.centerW;
        this.centerW -= vi.blocksizes[this.W] / 4 + vi.blocksizes[this.lW] / 4;
        this.granulepos = -1L;
        this.sequence = -1L;
    }

    public int synthesis_blockin(Block vb) {
        if (this.centerW > this.vi.blocksizes[1] / 2 && this.pcm_returned > 8192) {
            int shiftPCM = this.centerW - this.vi.blocksizes[1] / 2;
            shiftPCM = this.pcm_returned < shiftPCM ? this.pcm_returned : shiftPCM;
            this.pcm_current -= shiftPCM;
            this.centerW -= shiftPCM;
            this.pcm_returned -= shiftPCM;
            if (shiftPCM != 0) {
                for (int i2 = 0; i2 < this.vi.channels; ++i2) {
                    System.arraycopy(this.pcm[i2], shiftPCM, this.pcm[i2], 0, this.pcm_current);
                }
            }
        }
        this.lW = this.W;
        this.W = vb.W;
        this.nW = -1;
        this.glue_bits += (long)vb.glue_bits;
        this.time_bits += (long)vb.time_bits;
        this.floor_bits += (long)vb.floor_bits;
        this.res_bits += (long)vb.res_bits;
        if (this.sequence + 1L != vb.sequence) {
            this.granulepos = -1L;
        }
        this.sequence = vb.sequence;
        int sizeW = this.vi.blocksizes[this.W];
        int _centerW = this.centerW + this.vi.blocksizes[this.lW] / 4 + sizeW / 4;
        int beginW = _centerW - sizeW / 2;
        int endW = beginW + sizeW;
        int beginSl = 0;
        int endSl = 0;
        if (endW > this.pcm_storage) {
            this.pcm_storage = endW + this.vi.blocksizes[1];
            for (int i3 = 0; i3 < this.vi.channels; ++i3) {
                float[] foo = new float[this.pcm_storage];
                System.arraycopy(this.pcm[i3], 0, foo, 0, this.pcm[i3].length);
                this.pcm[i3] = foo;
            }
        }
        switch (this.W) {
            case 0: {
                beginSl = 0;
                endSl = this.vi.blocksizes[0] / 2;
                break;
            }
            case 1: {
                beginSl = this.vi.blocksizes[1] / 4 - this.vi.blocksizes[this.lW] / 4;
                endSl = beginSl + this.vi.blocksizes[this.lW] / 2;
            }
        }
        for (int j2 = 0; j2 < this.vi.channels; ++j2) {
            int _pcm = beginW;
            int i4 = 0;
            for (i4 = beginSl; i4 < endSl; ++i4) {
                float[] fArray = this.pcm[j2];
                int n2 = _pcm + i4;
                fArray[n2] = fArray[n2] + vb.pcm[j2][i4];
            }
            while (i4 < sizeW) {
                this.pcm[j2][_pcm + i4] = vb.pcm[j2][i4];
                ++i4;
            }
        }
        if (this.granulepos == -1L) {
            this.granulepos = vb.granulepos;
        } else {
            this.granulepos += (long)(_centerW - this.centerW);
            if (vb.granulepos != -1L && this.granulepos != vb.granulepos) {
                if (this.granulepos > vb.granulepos && vb.eofflag != 0) {
                    _centerW = (int)((long)_centerW - (this.granulepos - vb.granulepos));
                }
                this.granulepos = vb.granulepos;
            }
        }
        this.centerW = _centerW;
        this.pcm_current = endW;
        if (vb.eofflag != 0) {
            this.eofflag = 1;
        }
        return 0;
    }

    public int synthesis_pcmout(float[][][] _pcm, int[] index) {
        if (this.pcm_returned < this.centerW) {
            if (_pcm != null) {
                for (int i2 = 0; i2 < this.vi.channels; ++i2) {
                    index[i2] = this.pcm_returned;
                }
                _pcm[0] = this.pcm;
            }
            return this.centerW - this.pcm_returned;
        }
        return 0;
    }

    public int synthesis_read(int bytes) {
        if (bytes != 0 && this.pcm_returned + bytes > this.centerW) {
            return -1;
        }
        this.pcm_returned += bytes;
        return 0;
    }

    public void clear() {
    }
}

