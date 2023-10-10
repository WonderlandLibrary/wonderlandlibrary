// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

final class LayerIIIDecoder implements FrameDecoder
{
    final double d43 = 1.3333333333333333;
    public int[] scalefac_buffer;
    private int CheckSumHuff;
    private int[] is_1d;
    private float[][][] ro;
    private float[][][] lr;
    private float[] out_1d;
    private float[][] prevblck;
    private float[][] k;
    private int[] nonzero;
    private Bitstream stream;
    private Header header;
    private SynthesisFilter filter1;
    private SynthesisFilter filter2;
    private Obuffer buffer;
    private int which_channels;
    private BitReserve br;
    private III_side_info_t si;
    private temporaire2[] III_scalefac_t;
    private temporaire2[] scalefac;
    private int max_gr;
    private int frame_start;
    private int part2_start;
    private int channels;
    private int first_channel;
    private int last_channel;
    private int sfreq;
    private float[] samples1;
    private float[] samples2;
    private final int[] new_slen;
    int[] x;
    int[] y;
    int[] v;
    int[] w;
    int[] is_pos;
    float[] is_ratio;
    float[] tsOutCopy;
    float[] rawout;
    private int counter;
    private static final int SSLIMIT = 18;
    private static final int SBLIMIT = 32;
    private static final int[][] slen;
    public static final int[] pretab;
    private SBI[] sfBandIndex;
    public static final float[] two_to_negative_half_pow;
    public static final float[] t_43;
    public static final float[][] io;
    public static final float[] TAN12;
    private static int[][] reorder_table;
    private static final float[] cs;
    private static final float[] ca;
    public static final float[][] win;
    public Sftable sftable;
    public static final int[][][] nr_of_sfb_block;
    
    public LayerIIIDecoder(final Bitstream stream0, final Header header0, final SynthesisFilter filtera, final SynthesisFilter filterb, final Obuffer buffer0, final int which_ch0) {
        this.CheckSumHuff = 0;
        this.samples1 = new float[32];
        this.samples2 = new float[32];
        this.new_slen = new int[4];
        this.x = new int[] { 0 };
        this.y = new int[] { 0 };
        this.v = new int[] { 0 };
        this.w = new int[] { 0 };
        this.is_pos = new int[576];
        this.is_ratio = new float[576];
        this.tsOutCopy = new float[18];
        this.rawout = new float[36];
        this.counter = 0;
        huffcodetab.inithuff();
        this.is_1d = new int[580];
        this.ro = new float[2][32][18];
        this.lr = new float[2][32][18];
        this.out_1d = new float[576];
        this.prevblck = new float[2][576];
        this.k = new float[2][576];
        this.nonzero = new int[2];
        (this.III_scalefac_t = new temporaire2[2])[0] = new temporaire2();
        this.III_scalefac_t[1] = new temporaire2();
        this.scalefac = this.III_scalefac_t;
        this.sfBandIndex = new SBI[9];
        final int[] l0 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
        final int[] s0 = { 0, 4, 8, 12, 18, 24, 32, 42, 56, 74, 100, 132, 174, 192 };
        final int[] l2 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 114, 136, 162, 194, 232, 278, 330, 394, 464, 540, 576 };
        final int[] s2 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 136, 180, 192 };
        final int[] l3 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
        final int[] s3 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
        final int[] l4 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 52, 62, 74, 90, 110, 134, 162, 196, 238, 288, 342, 418, 576 };
        final int[] s4 = { 0, 4, 8, 12, 16, 22, 30, 40, 52, 66, 84, 106, 136, 192 };
        final int[] l5 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 42, 50, 60, 72, 88, 106, 128, 156, 190, 230, 276, 330, 384, 576 };
        final int[] s5 = { 0, 4, 8, 12, 16, 22, 28, 38, 50, 64, 80, 100, 126, 192 };
        final int[] l6 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 54, 66, 82, 102, 126, 156, 194, 240, 296, 364, 448, 550, 576 };
        final int[] s6 = { 0, 4, 8, 12, 16, 22, 30, 42, 58, 78, 104, 138, 180, 192 };
        final int[] l7 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
        final int[] s7 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
        final int[] l8 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
        final int[] s8 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
        final int[] l9 = { 0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570, 572, 574, 576 };
        final int[] s9 = { 0, 8, 16, 24, 36, 52, 72, 96, 124, 160, 162, 164, 166, 192 };
        this.sfBandIndex[0] = new SBI(l0, s0);
        this.sfBandIndex[1] = new SBI(l2, s2);
        this.sfBandIndex[2] = new SBI(l3, s3);
        this.sfBandIndex[3] = new SBI(l4, s4);
        this.sfBandIndex[4] = new SBI(l5, s5);
        this.sfBandIndex[5] = new SBI(l6, s6);
        this.sfBandIndex[6] = new SBI(l7, s7);
        this.sfBandIndex[7] = new SBI(l8, s8);
        this.sfBandIndex[8] = new SBI(l9, s9);
        if (LayerIIIDecoder.reorder_table == null) {
            LayerIIIDecoder.reorder_table = new int[9][];
            for (int i = 0; i < 9; ++i) {
                LayerIIIDecoder.reorder_table[i] = reorder(this.sfBandIndex[i].s);
            }
        }
        final int[] ll0 = { 0, 6, 11, 16, 21 };
        final int[] ss0 = { 0, 6, 12 };
        this.sftable = new Sftable(ll0, ss0);
        this.scalefac_buffer = new int[54];
        this.stream = stream0;
        this.header = header0;
        this.filter1 = filtera;
        this.filter2 = filterb;
        this.buffer = buffer0;
        this.which_channels = which_ch0;
        this.frame_start = 0;
        this.channels = ((this.header.mode() == 3) ? 1 : 2);
        this.max_gr = ((this.header.version() == 1) ? 2 : 1);
        this.sfreq = this.header.sample_frequency() + ((this.header.version() == 1) ? 3 : ((this.header.version() == 2) ? 6 : 0));
        if (this.channels == 2) {
            switch (this.which_channels) {
                case 1:
                case 3: {
                    final int n = 0;
                    this.last_channel = n;
                    this.first_channel = n;
                    break;
                }
                case 2: {
                    final int n2 = 1;
                    this.last_channel = n2;
                    this.first_channel = n2;
                    break;
                }
                default: {
                    this.first_channel = 0;
                    this.last_channel = 1;
                    break;
                }
            }
        }
        else {
            final int n3 = 0;
            this.last_channel = n3;
            this.first_channel = n3;
        }
        for (int ch = 0; ch < 2; ++ch) {
            for (int j = 0; j < 576; ++j) {
                this.prevblck[ch][j] = 0.0f;
            }
        }
        this.nonzero[0] = (this.nonzero[1] = 576);
        this.br = new BitReserve();
        this.si = new III_side_info_t();
    }
    
    public void seek_notify() {
        this.frame_start = 0;
        for (int ch = 0; ch < 2; ++ch) {
            for (int j = 0; j < 576; ++j) {
                this.prevblck[ch][j] = 0.0f;
            }
        }
        this.br = new BitReserve();
    }
    
    @Override
    public void decodeFrame() {
        this.decode();
    }
    
    public void decode() {
        final int nSlots = this.header.slots();
        this.get_side_info();
        for (int i = 0; i < nSlots; ++i) {
            this.br.hputbuf(this.stream.get_bits(8));
        }
        int main_data_end = this.br.hsstell() >>> 3;
        final int flush_main;
        if ((flush_main = (this.br.hsstell() & 0x7)) != 0) {
            this.br.hgetbits(8 - flush_main);
            ++main_data_end;
        }
        int bytes_to_discard = this.frame_start - main_data_end - this.si.main_data_begin;
        this.frame_start += nSlots;
        if (bytes_to_discard < 0) {
            return;
        }
        if (main_data_end > 4096) {
            this.frame_start -= 4096;
            this.br.rewindNbytes(4096);
        }
        while (bytes_to_discard > 0) {
            this.br.hgetbits(8);
            --bytes_to_discard;
        }
        for (int gr = 0; gr < this.max_gr; ++gr) {
            for (int ch = 0; ch < this.channels; ++ch) {
                this.part2_start = this.br.hsstell();
                if (this.header.version() == 1) {
                    this.get_scale_factors(ch, gr);
                }
                else {
                    this.get_LSF_scale_factors(ch, gr);
                }
                this.huffman_decode(ch, gr);
                this.dequantize_sample(this.ro[ch], ch, gr);
            }
            this.stereo(gr);
            if (this.which_channels == 3 && this.channels > 1) {
                this.do_downmix();
            }
            for (int ch = this.first_channel; ch <= this.last_channel; ++ch) {
                this.reorder(this.lr[ch], ch, gr);
                this.antialias(ch, gr);
                this.hybrid(ch, gr);
                for (int sb18 = 18; sb18 < 576; sb18 += 36) {
                    for (int ss = 1; ss < 18; ss += 2) {
                        this.out_1d[sb18 + ss] = -this.out_1d[sb18 + ss];
                    }
                }
                if (ch == 0 || this.which_channels == 2) {
                    for (int ss = 0; ss < 18; ++ss) {
                        int sb19 = 0;
                        for (int sb18 = 0; sb18 < 576; sb18 += 18) {
                            this.samples1[sb19] = this.out_1d[sb18 + ss];
                            ++sb19;
                        }
                        this.filter1.input_samples(this.samples1);
                        this.filter1.calculate_pcm_samples(this.buffer);
                    }
                }
                else {
                    for (int ss = 0; ss < 18; ++ss) {
                        int sb19 = 0;
                        for (int sb18 = 0; sb18 < 576; sb18 += 18) {
                            this.samples2[sb19] = this.out_1d[sb18 + ss];
                            ++sb19;
                        }
                        this.filter2.input_samples(this.samples2);
                        this.filter2.calculate_pcm_samples(this.buffer);
                    }
                }
            }
        }
        ++this.counter;
        this.buffer.write_buffer(1);
    }
    
    private boolean get_side_info() {
        if (this.header.version() == 1) {
            this.si.main_data_begin = this.stream.get_bits(9);
            if (this.channels == 1) {
                this.si.private_bits = this.stream.get_bits(5);
            }
            else {
                this.si.private_bits = this.stream.get_bits(3);
            }
            for (int ch = 0; ch < this.channels; ++ch) {
                this.si.ch[ch].scfsi[0] = this.stream.get_bits(1);
                this.si.ch[ch].scfsi[1] = this.stream.get_bits(1);
                this.si.ch[ch].scfsi[2] = this.stream.get_bits(1);
                this.si.ch[ch].scfsi[3] = this.stream.get_bits(1);
            }
            for (int gr = 0; gr < 2; ++gr) {
                for (int ch = 0; ch < this.channels; ++ch) {
                    this.si.ch[ch].gr[gr].part2_3_length = this.stream.get_bits(12);
                    this.si.ch[ch].gr[gr].big_values = this.stream.get_bits(9);
                    this.si.ch[ch].gr[gr].global_gain = this.stream.get_bits(8);
                    this.si.ch[ch].gr[gr].scalefac_compress = this.stream.get_bits(4);
                    this.si.ch[ch].gr[gr].window_switching_flag = this.stream.get_bits(1);
                    if (this.si.ch[ch].gr[gr].window_switching_flag != 0) {
                        this.si.ch[ch].gr[gr].block_type = this.stream.get_bits(2);
                        this.si.ch[ch].gr[gr].mixed_block_flag = this.stream.get_bits(1);
                        this.si.ch[ch].gr[gr].table_select[0] = this.stream.get_bits(5);
                        this.si.ch[ch].gr[gr].table_select[1] = this.stream.get_bits(5);
                        this.si.ch[ch].gr[gr].subblock_gain[0] = this.stream.get_bits(3);
                        this.si.ch[ch].gr[gr].subblock_gain[1] = this.stream.get_bits(3);
                        this.si.ch[ch].gr[gr].subblock_gain[2] = this.stream.get_bits(3);
                        if (this.si.ch[ch].gr[gr].block_type == 0) {
                            return false;
                        }
                        if (this.si.ch[ch].gr[gr].block_type == 2 && this.si.ch[ch].gr[gr].mixed_block_flag == 0) {
                            this.si.ch[ch].gr[gr].region0_count = 8;
                        }
                        else {
                            this.si.ch[ch].gr[gr].region0_count = 7;
                        }
                        this.si.ch[ch].gr[gr].region1_count = 20 - this.si.ch[ch].gr[gr].region0_count;
                    }
                    else {
                        this.si.ch[ch].gr[gr].table_select[0] = this.stream.get_bits(5);
                        this.si.ch[ch].gr[gr].table_select[1] = this.stream.get_bits(5);
                        this.si.ch[ch].gr[gr].table_select[2] = this.stream.get_bits(5);
                        this.si.ch[ch].gr[gr].region0_count = this.stream.get_bits(4);
                        this.si.ch[ch].gr[gr].region1_count = this.stream.get_bits(3);
                        this.si.ch[ch].gr[gr].block_type = 0;
                    }
                    this.si.ch[ch].gr[gr].preflag = this.stream.get_bits(1);
                    this.si.ch[ch].gr[gr].scalefac_scale = this.stream.get_bits(1);
                    this.si.ch[ch].gr[gr].count1table_select = this.stream.get_bits(1);
                }
            }
        }
        else {
            this.si.main_data_begin = this.stream.get_bits(8);
            if (this.channels == 1) {
                this.si.private_bits = this.stream.get_bits(1);
            }
            else {
                this.si.private_bits = this.stream.get_bits(2);
            }
            for (int ch = 0; ch < this.channels; ++ch) {
                this.si.ch[ch].gr[0].part2_3_length = this.stream.get_bits(12);
                this.si.ch[ch].gr[0].big_values = this.stream.get_bits(9);
                this.si.ch[ch].gr[0].global_gain = this.stream.get_bits(8);
                this.si.ch[ch].gr[0].scalefac_compress = this.stream.get_bits(9);
                this.si.ch[ch].gr[0].window_switching_flag = this.stream.get_bits(1);
                if (this.si.ch[ch].gr[0].window_switching_flag != 0) {
                    this.si.ch[ch].gr[0].block_type = this.stream.get_bits(2);
                    this.si.ch[ch].gr[0].mixed_block_flag = this.stream.get_bits(1);
                    this.si.ch[ch].gr[0].table_select[0] = this.stream.get_bits(5);
                    this.si.ch[ch].gr[0].table_select[1] = this.stream.get_bits(5);
                    this.si.ch[ch].gr[0].subblock_gain[0] = this.stream.get_bits(3);
                    this.si.ch[ch].gr[0].subblock_gain[1] = this.stream.get_bits(3);
                    this.si.ch[ch].gr[0].subblock_gain[2] = this.stream.get_bits(3);
                    if (this.si.ch[ch].gr[0].block_type == 0) {
                        return false;
                    }
                    if (this.si.ch[ch].gr[0].block_type == 2 && this.si.ch[ch].gr[0].mixed_block_flag == 0) {
                        this.si.ch[ch].gr[0].region0_count = 8;
                    }
                    else {
                        this.si.ch[ch].gr[0].region0_count = 7;
                        this.si.ch[ch].gr[0].region1_count = 20 - this.si.ch[ch].gr[0].region0_count;
                    }
                }
                else {
                    this.si.ch[ch].gr[0].table_select[0] = this.stream.get_bits(5);
                    this.si.ch[ch].gr[0].table_select[1] = this.stream.get_bits(5);
                    this.si.ch[ch].gr[0].table_select[2] = this.stream.get_bits(5);
                    this.si.ch[ch].gr[0].region0_count = this.stream.get_bits(4);
                    this.si.ch[ch].gr[0].region1_count = this.stream.get_bits(3);
                    this.si.ch[ch].gr[0].block_type = 0;
                }
                this.si.ch[ch].gr[0].scalefac_scale = this.stream.get_bits(1);
                this.si.ch[ch].gr[0].count1table_select = this.stream.get_bits(1);
            }
        }
        return true;
    }
    
    private void get_scale_factors(final int ch, final int gr) {
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        final int scale_comp = gr_info.scalefac_compress;
        final int length0 = LayerIIIDecoder.slen[0][scale_comp];
        final int length2 = LayerIIIDecoder.slen[1][scale_comp];
        if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
            if (gr_info.mixed_block_flag != 0) {
                for (int sfb = 0; sfb < 8; ++sfb) {
                    this.scalefac[ch].l[sfb] = this.br.hgetbits(LayerIIIDecoder.slen[0][gr_info.scalefac_compress]);
                }
                for (int sfb = 3; sfb < 6; ++sfb) {
                    for (int window = 0; window < 3; ++window) {
                        this.scalefac[ch].s[window][sfb] = this.br.hgetbits(LayerIIIDecoder.slen[0][gr_info.scalefac_compress]);
                    }
                }
                for (int sfb = 6; sfb < 12; ++sfb) {
                    for (int window = 0; window < 3; ++window) {
                        this.scalefac[ch].s[window][sfb] = this.br.hgetbits(LayerIIIDecoder.slen[1][gr_info.scalefac_compress]);
                    }
                }
                int sfb = 12;
                for (int window = 0; window < 3; ++window) {
                    this.scalefac[ch].s[window][sfb] = 0;
                }
            }
            else {
                this.scalefac[ch].s[0][0] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][0] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][0] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][1] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][1] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][1] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][2] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][2] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][2] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][3] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][3] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][3] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][4] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][4] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][4] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][5] = this.br.hgetbits(length0);
                this.scalefac[ch].s[1][5] = this.br.hgetbits(length0);
                this.scalefac[ch].s[2][5] = this.br.hgetbits(length0);
                this.scalefac[ch].s[0][6] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][6] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][6] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][7] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][7] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][7] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][8] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][8] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][8] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][9] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][9] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][9] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][10] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][10] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][10] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][11] = this.br.hgetbits(length2);
                this.scalefac[ch].s[1][11] = this.br.hgetbits(length2);
                this.scalefac[ch].s[2][11] = this.br.hgetbits(length2);
                this.scalefac[ch].s[0][12] = 0;
                this.scalefac[ch].s[1][12] = 0;
                this.scalefac[ch].s[2][12] = 0;
            }
        }
        else {
            if (this.si.ch[ch].scfsi[0] == 0 || gr == 0) {
                this.scalefac[ch].l[0] = this.br.hgetbits(length0);
                this.scalefac[ch].l[1] = this.br.hgetbits(length0);
                this.scalefac[ch].l[2] = this.br.hgetbits(length0);
                this.scalefac[ch].l[3] = this.br.hgetbits(length0);
                this.scalefac[ch].l[4] = this.br.hgetbits(length0);
                this.scalefac[ch].l[5] = this.br.hgetbits(length0);
            }
            if (this.si.ch[ch].scfsi[1] == 0 || gr == 0) {
                this.scalefac[ch].l[6] = this.br.hgetbits(length0);
                this.scalefac[ch].l[7] = this.br.hgetbits(length0);
                this.scalefac[ch].l[8] = this.br.hgetbits(length0);
                this.scalefac[ch].l[9] = this.br.hgetbits(length0);
                this.scalefac[ch].l[10] = this.br.hgetbits(length0);
            }
            if (this.si.ch[ch].scfsi[2] == 0 || gr == 0) {
                this.scalefac[ch].l[11] = this.br.hgetbits(length2);
                this.scalefac[ch].l[12] = this.br.hgetbits(length2);
                this.scalefac[ch].l[13] = this.br.hgetbits(length2);
                this.scalefac[ch].l[14] = this.br.hgetbits(length2);
                this.scalefac[ch].l[15] = this.br.hgetbits(length2);
            }
            if (this.si.ch[ch].scfsi[3] == 0 || gr == 0) {
                this.scalefac[ch].l[16] = this.br.hgetbits(length2);
                this.scalefac[ch].l[17] = this.br.hgetbits(length2);
                this.scalefac[ch].l[18] = this.br.hgetbits(length2);
                this.scalefac[ch].l[19] = this.br.hgetbits(length2);
                this.scalefac[ch].l[20] = this.br.hgetbits(length2);
            }
            this.scalefac[ch].l[21] = 0;
            this.scalefac[ch].l[22] = 0;
        }
    }
    
    private void get_LSF_scale_data(final int ch, final int gr) {
        final int mode_ext = this.header.mode_extension();
        int blocknumber = 0;
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        final int scalefac_comp = gr_info.scalefac_compress;
        int blocktypenumber;
        if (gr_info.block_type == 2) {
            if (gr_info.mixed_block_flag == 0) {
                blocktypenumber = 1;
            }
            else if (gr_info.mixed_block_flag == 1) {
                blocktypenumber = 2;
            }
            else {
                blocktypenumber = 0;
            }
        }
        else {
            blocktypenumber = 0;
        }
        if ((mode_ext != 1 && mode_ext != 3) || ch != 1) {
            if (scalefac_comp < 400) {
                this.new_slen[0] = (scalefac_comp >>> 4) / 5;
                this.new_slen[1] = (scalefac_comp >>> 4) % 5;
                this.new_slen[2] = (scalefac_comp & 0xF) >>> 2;
                this.new_slen[3] = (scalefac_comp & 0x3);
                this.si.ch[ch].gr[gr].preflag = 0;
                blocknumber = 0;
            }
            else if (scalefac_comp < 500) {
                this.new_slen[0] = (scalefac_comp - 400 >>> 2) / 5;
                this.new_slen[1] = (scalefac_comp - 400 >>> 2) % 5;
                this.new_slen[2] = (scalefac_comp - 400 & 0x3);
                this.new_slen[3] = 0;
                this.si.ch[ch].gr[gr].preflag = 0;
                blocknumber = 1;
            }
            else if (scalefac_comp < 512) {
                this.new_slen[0] = (scalefac_comp - 500) / 3;
                this.new_slen[1] = (scalefac_comp - 500) % 3;
                this.new_slen[2] = 0;
                this.new_slen[3] = 0;
                this.si.ch[ch].gr[gr].preflag = 1;
                blocknumber = 2;
            }
        }
        if ((mode_ext == 1 || mode_ext == 3) && ch == 1) {
            final int int_scalefac_comp = scalefac_comp >>> 1;
            if (int_scalefac_comp < 180) {
                this.new_slen[0] = int_scalefac_comp / 36;
                this.new_slen[1] = int_scalefac_comp % 36 / 6;
                this.new_slen[2] = int_scalefac_comp % 36 % 6;
                this.new_slen[3] = 0;
                this.si.ch[ch].gr[gr].preflag = 0;
                blocknumber = 3;
            }
            else if (int_scalefac_comp < 244) {
                this.new_slen[0] = (int_scalefac_comp - 180 & 0x3F) >>> 4;
                this.new_slen[1] = (int_scalefac_comp - 180 & 0xF) >>> 2;
                this.new_slen[2] = (int_scalefac_comp - 180 & 0x3);
                this.new_slen[3] = 0;
                this.si.ch[ch].gr[gr].preflag = 0;
                blocknumber = 4;
            }
            else if (int_scalefac_comp < 255) {
                this.new_slen[0] = (int_scalefac_comp - 244) / 3;
                this.new_slen[1] = (int_scalefac_comp - 244) % 3;
                this.new_slen[2] = 0;
                this.new_slen[3] = 0;
                this.si.ch[ch].gr[gr].preflag = 0;
                blocknumber = 5;
            }
        }
        for (int x = 0; x < 45; ++x) {
            this.scalefac_buffer[x] = 0;
        }
        int m = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < LayerIIIDecoder.nr_of_sfb_block[blocknumber][blocktypenumber][i]; ++j) {
                this.scalefac_buffer[m] = ((this.new_slen[i] == 0) ? 0 : this.br.hgetbits(this.new_slen[i]));
                ++m;
            }
        }
    }
    
    private void get_LSF_scale_factors(final int ch, final int gr) {
        int m = 0;
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        this.get_LSF_scale_data(ch, gr);
        if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
            if (gr_info.mixed_block_flag != 0) {
                for (int sfb = 0; sfb < 8; ++sfb) {
                    this.scalefac[ch].l[sfb] = this.scalefac_buffer[m];
                    ++m;
                }
                for (int sfb = 3; sfb < 12; ++sfb) {
                    for (int window = 0; window < 3; ++window) {
                        this.scalefac[ch].s[window][sfb] = this.scalefac_buffer[m];
                        ++m;
                    }
                }
                for (int window = 0; window < 3; ++window) {
                    this.scalefac[ch].s[window][12] = 0;
                }
            }
            else {
                for (int sfb = 0; sfb < 12; ++sfb) {
                    for (int window = 0; window < 3; ++window) {
                        this.scalefac[ch].s[window][sfb] = this.scalefac_buffer[m];
                        ++m;
                    }
                }
                for (int window = 0; window < 3; ++window) {
                    this.scalefac[ch].s[window][12] = 0;
                }
            }
        }
        else {
            for (int sfb = 0; sfb < 21; ++sfb) {
                this.scalefac[ch].l[sfb] = this.scalefac_buffer[m];
                ++m;
            }
            this.scalefac[ch].l[21] = 0;
            this.scalefac[ch].l[22] = 0;
        }
    }
    
    private void huffman_decode(final int ch, final int gr) {
        this.x[0] = 0;
        this.y[0] = 0;
        this.v[0] = 0;
        this.w[0] = 0;
        final int part2_3_end = this.part2_start + this.si.ch[ch].gr[gr].part2_3_length;
        int region1Start;
        int region2Start;
        if (this.si.ch[ch].gr[gr].window_switching_flag != 0 && this.si.ch[ch].gr[gr].block_type == 2) {
            region1Start = ((this.sfreq == 8) ? 72 : 36);
            region2Start = 576;
        }
        else {
            final int buf = this.si.ch[ch].gr[gr].region0_count + 1;
            int buf2 = buf + this.si.ch[ch].gr[gr].region1_count + 1;
            if (buf2 > this.sfBandIndex[this.sfreq].l.length - 1) {
                buf2 = this.sfBandIndex[this.sfreq].l.length - 1;
            }
            region1Start = this.sfBandIndex[this.sfreq].l[buf];
            region2Start = this.sfBandIndex[this.sfreq].l[buf2];
        }
        int index = 0;
        for (int i = 0; i < this.si.ch[ch].gr[gr].big_values << 1; i += 2) {
            huffcodetab h;
            if (i < region1Start) {
                h = huffcodetab.ht[this.si.ch[ch].gr[gr].table_select[0]];
            }
            else if (i < region2Start) {
                h = huffcodetab.ht[this.si.ch[ch].gr[gr].table_select[1]];
            }
            else {
                h = huffcodetab.ht[this.si.ch[ch].gr[gr].table_select[2]];
            }
            huffcodetab.huffman_decoder(h, this.x, this.y, this.v, this.w, this.br);
            this.is_1d[index++] = this.x[0];
            this.is_1d[index++] = this.y[0];
            this.CheckSumHuff = this.CheckSumHuff + this.x[0] + this.y[0];
        }
        huffcodetab h = huffcodetab.ht[this.si.ch[ch].gr[gr].count1table_select + 32];
        int num_bits;
        for (num_bits = this.br.hsstell(); num_bits < part2_3_end && index < 576; this.is_1d[index++] = this.v[0], this.is_1d[index++] = this.w[0], this.is_1d[index++] = this.x[0], this.is_1d[index++] = this.y[0], this.CheckSumHuff = this.CheckSumHuff + this.v[0] + this.w[0] + this.x[0] + this.y[0], num_bits = this.br.hsstell()) {
            huffcodetab.huffman_decoder(h, this.x, this.y, this.v, this.w, this.br);
        }
        if (num_bits > part2_3_end) {
            this.br.rewindNbits(num_bits - part2_3_end);
            index -= 4;
        }
        num_bits = this.br.hsstell();
        if (num_bits < part2_3_end) {
            this.br.hgetbits(part2_3_end - num_bits);
        }
        if (index < 576) {
            this.nonzero[ch] = index;
        }
        else {
            this.nonzero[ch] = 576;
        }
        if (index < 0) {
            index = 0;
        }
        while (index < 576) {
            this.is_1d[index] = 0;
            ++index;
        }
    }
    
    private void i_stereo_k_values(final int is_pos, final int io_type, final int i) {
        if (is_pos == 0) {
            this.k[0][i] = 1.0f;
            this.k[1][i] = 1.0f;
        }
        else if ((is_pos & 0x1) != 0x0) {
            this.k[0][i] = LayerIIIDecoder.io[io_type][is_pos + 1 >>> 1];
            this.k[1][i] = 1.0f;
        }
        else {
            this.k[0][i] = 1.0f;
            this.k[1][i] = LayerIIIDecoder.io[io_type][is_pos >>> 1];
        }
    }
    
    private void dequantize_sample(final float[][] xr, final int ch, final int gr) {
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        int cb = 0;
        int cb_begin = 0;
        int cb_width = 0;
        int index = 0;
        final float[][] xr_1d = xr;
        int next_cb_boundary;
        if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
            if (gr_info.mixed_block_flag != 0) {
                next_cb_boundary = this.sfBandIndex[this.sfreq].l[1];
            }
            else {
                cb_width = this.sfBandIndex[this.sfreq].s[1];
                next_cb_boundary = (cb_width << 2) - cb_width;
                cb_begin = 0;
            }
        }
        else {
            next_cb_boundary = this.sfBandIndex[this.sfreq].l[1];
        }
        final float g_gain = (float)Math.pow(2.0, 0.25 * (gr_info.global_gain - 210.0));
        for (int j = 0; j < this.nonzero[ch]; ++j) {
            final int reste = j % 18;
            final int quotien = (j - reste) / 18;
            if (this.is_1d[j] == 0) {
                xr_1d[quotien][reste] = 0.0f;
            }
            else {
                final int abv = this.is_1d[j];
                if (abv < LayerIIIDecoder.t_43.length) {
                    if (this.is_1d[j] > 0) {
                        xr_1d[quotien][reste] = g_gain * LayerIIIDecoder.t_43[abv];
                    }
                    else if (-abv < LayerIIIDecoder.t_43.length) {
                        xr_1d[quotien][reste] = -g_gain * LayerIIIDecoder.t_43[-abv];
                    }
                    else {
                        xr_1d[quotien][reste] = -g_gain * (float)Math.pow(-abv, 1.3333333333333333);
                    }
                }
                else if (this.is_1d[j] > 0) {
                    xr_1d[quotien][reste] = g_gain * (float)Math.pow(abv, 1.3333333333333333);
                }
                else {
                    xr_1d[quotien][reste] = -g_gain * (float)Math.pow(-abv, 1.3333333333333333);
                }
            }
        }
        for (int j = 0; j < this.nonzero[ch]; ++j) {
            final int reste = j % 18;
            final int quotien = (j - reste) / 18;
            if (index == next_cb_boundary) {
                if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
                    if (gr_info.mixed_block_flag != 0) {
                        if (index == this.sfBandIndex[this.sfreq].l[8]) {
                            next_cb_boundary = this.sfBandIndex[this.sfreq].s[4];
                            next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
                            cb = 3;
                            cb_width = this.sfBandIndex[this.sfreq].s[4] - this.sfBandIndex[this.sfreq].s[3];
                            cb_begin = this.sfBandIndex[this.sfreq].s[3];
                            cb_begin = (cb_begin << 2) - cb_begin;
                        }
                        else if (index < this.sfBandIndex[this.sfreq].l[8]) {
                            next_cb_boundary = this.sfBandIndex[this.sfreq].l[++cb + 1];
                        }
                        else {
                            next_cb_boundary = this.sfBandIndex[this.sfreq].s[++cb + 1];
                            next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
                            cb_begin = this.sfBandIndex[this.sfreq].s[cb];
                            cb_width = this.sfBandIndex[this.sfreq].s[cb + 1] - cb_begin;
                            cb_begin = (cb_begin << 2) - cb_begin;
                        }
                    }
                    else {
                        next_cb_boundary = this.sfBandIndex[this.sfreq].s[++cb + 1];
                        next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
                        cb_begin = this.sfBandIndex[this.sfreq].s[cb];
                        cb_width = this.sfBandIndex[this.sfreq].s[cb + 1] - cb_begin;
                        cb_begin = (cb_begin << 2) - cb_begin;
                    }
                }
                else {
                    next_cb_boundary = this.sfBandIndex[this.sfreq].l[++cb + 1];
                }
            }
            if (gr_info.window_switching_flag != 0 && ((gr_info.block_type == 2 && gr_info.mixed_block_flag == 0) || (gr_info.block_type == 2 && gr_info.mixed_block_flag != 0 && j >= 36))) {
                final int t_index = (index - cb_begin) / cb_width;
                int idx = this.scalefac[ch].s[t_index][cb] << gr_info.scalefac_scale;
                idx += gr_info.subblock_gain[t_index] << 2;
                final float[] array = xr_1d[quotien];
                final int n = reste;
                array[n] *= LayerIIIDecoder.two_to_negative_half_pow[idx];
            }
            else {
                int idx = this.scalefac[ch].l[cb];
                if (gr_info.preflag != 0) {
                    idx += LayerIIIDecoder.pretab[cb];
                }
                idx <<= gr_info.scalefac_scale;
                final float[] array2 = xr_1d[quotien];
                final int n2 = reste;
                array2[n2] *= LayerIIIDecoder.two_to_negative_half_pow[idx];
            }
            ++index;
        }
        for (int j = this.nonzero[ch]; j < 576; ++j) {
            int reste = j % 18;
            int quotien = (j - reste) / 18;
            if (reste < 0) {
                reste = 0;
            }
            if (quotien < 0) {
                quotien = 0;
            }
            xr_1d[quotien][reste] = 0.0f;
        }
    }
    
    private void reorder(final float[][] xr, final int ch, final int gr) {
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        final float[][] xr_1d = xr;
        if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
            for (int index = 0; index < 576; ++index) {
                this.out_1d[index] = 0.0f;
            }
            if (gr_info.mixed_block_flag != 0) {
                for (int index = 0; index < 36; ++index) {
                    final int reste = index % 18;
                    final int quotien = (index - reste) / 18;
                    this.out_1d[index] = xr_1d[quotien][reste];
                }
                for (int sfb = 3; sfb < 13; ++sfb) {
                    final int sfb_start = this.sfBandIndex[this.sfreq].s[sfb];
                    final int sfb_lines = this.sfBandIndex[this.sfreq].s[sfb + 1] - sfb_start;
                    final int sfb_start2 = (sfb_start << 2) - sfb_start;
                    for (int freq = 0, freq2 = 0; freq < sfb_lines; ++freq, freq2 += 3) {
                        int src_line = sfb_start2 + freq;
                        int des_line = sfb_start2 + freq2;
                        int reste2 = src_line % 18;
                        int quotien2 = (src_line - reste2) / 18;
                        this.out_1d[des_line] = xr_1d[quotien2][reste2];
                        src_line += sfb_lines;
                        ++des_line;
                        reste2 = src_line % 18;
                        quotien2 = (src_line - reste2) / 18;
                        this.out_1d[des_line] = xr_1d[quotien2][reste2];
                        src_line += sfb_lines;
                        ++des_line;
                        reste2 = src_line % 18;
                        quotien2 = (src_line - reste2) / 18;
                        this.out_1d[des_line] = xr_1d[quotien2][reste2];
                    }
                }
            }
            else {
                for (int index = 0; index < 576; ++index) {
                    final int j = LayerIIIDecoder.reorder_table[this.sfreq][index];
                    final int reste2 = j % 18;
                    final int quotien2 = (j - reste2) / 18;
                    this.out_1d[index] = xr_1d[quotien2][reste2];
                }
            }
        }
        else {
            for (int index = 0; index < 576; ++index) {
                final int reste = index % 18;
                final int quotien = (index - reste) / 18;
                this.out_1d[index] = xr_1d[quotien][reste];
            }
        }
    }
    
    private void stereo(final int gr) {
        if (this.channels == 1) {
            for (int sb = 0; sb < 32; ++sb) {
                for (int ss = 0; ss < 18; ss += 3) {
                    this.lr[0][sb][ss] = this.ro[0][sb][ss];
                    this.lr[0][sb][ss + 1] = this.ro[0][sb][ss + 1];
                    this.lr[0][sb][ss + 2] = this.ro[0][sb][ss + 2];
                }
            }
        }
        else {
            final gr_info_s gr_info = this.si.ch[0].gr[gr];
            final int mode_ext = this.header.mode_extension();
            final boolean ms_stereo = this.header.mode() == 1 && (mode_ext & 0x2) != 0x0;
            final boolean i_stereo = this.header.mode() == 1 && (mode_ext & 0x1) != 0x0;
            final boolean lsf = this.header.version() == 0 || this.header.version() == 2;
            final int io_type = gr_info.scalefac_compress & 0x1;
            for (int i = 0; i < 576; ++i) {
                this.is_pos[i] = 7;
                this.is_ratio[i] = 0.0f;
            }
            if (i_stereo) {
                if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2) {
                    if (gr_info.mixed_block_flag != 0) {
                        int max_sfb = 0;
                        for (int j = 0; j < 3; ++j) {
                            int sfbcnt = 2;
                            for (int sfb = 12; sfb >= 3; --sfb) {
                                int i;
                                int lines;
                                for (i = this.sfBandIndex[this.sfreq].s[sfb], lines = this.sfBandIndex[this.sfreq].s[sfb + 1] - i, i = (i << 2) - i + (j + 1) * lines - 1; lines > 0; --lines, --i) {
                                    if (this.ro[1][i / 18][i % 18] != 0.0f) {
                                        sfbcnt = sfb;
                                        sfb = -10;
                                        lines = -10;
                                    }
                                }
                            }
                            int sfb = sfbcnt + 1;
                            if (sfb > max_sfb) {
                                max_sfb = sfb;
                            }
                            while (sfb < 12) {
                                final int temp = this.sfBandIndex[this.sfreq].s[sfb];
                                int sb = this.sfBandIndex[this.sfreq].s[sfb + 1] - temp;
                                int i = (temp << 2) - temp + j * sb;
                                while (sb > 0) {
                                    this.is_pos[i] = this.scalefac[1].s[j][sfb];
                                    if (this.is_pos[i] != 7) {
                                        if (lsf) {
                                            this.i_stereo_k_values(this.is_pos[i], io_type, i);
                                        }
                                        else {
                                            this.is_ratio[i] = LayerIIIDecoder.TAN12[this.is_pos[i]];
                                        }
                                    }
                                    ++i;
                                    --sb;
                                }
                                ++sfb;
                            }
                            sfb = this.sfBandIndex[this.sfreq].s[10];
                            int sb = this.sfBandIndex[this.sfreq].s[11] - sfb;
                            sfb = (sfb << 2) - sfb + j * sb;
                            final int temp = this.sfBandIndex[this.sfreq].s[11];
                            sb = this.sfBandIndex[this.sfreq].s[12] - temp;
                            int i = (temp << 2) - temp + j * sb;
                            while (sb > 0) {
                                this.is_pos[i] = this.is_pos[sfb];
                                if (lsf) {
                                    this.k[0][i] = this.k[0][sfb];
                                    this.k[1][i] = this.k[1][sfb];
                                }
                                else {
                                    this.is_ratio[i] = this.is_ratio[sfb];
                                }
                                ++i;
                                --sb;
                            }
                        }
                        if (max_sfb <= 3) {
                            int i = 2;
                            int ss = 17;
                            int sb = -1;
                            while (i >= 0) {
                                if (this.ro[1][i][ss] != 0.0f) {
                                    sb = (i << 4) + (i << 1) + ss;
                                    i = -1;
                                }
                                else {
                                    if (--ss >= 0) {
                                        continue;
                                    }
                                    --i;
                                    ss = 17;
                                }
                            }
                            for (i = 0; this.sfBandIndex[this.sfreq].l[i] <= sb; ++i) {}
                            int sfb = i;
                            i = this.sfBandIndex[this.sfreq].l[i];
                            while (sfb < 8) {
                                for (sb = this.sfBandIndex[this.sfreq].l[sfb + 1] - this.sfBandIndex[this.sfreq].l[sfb]; sb > 0; --sb) {
                                    this.is_pos[i] = this.scalefac[1].l[sfb];
                                    if (this.is_pos[i] != 7) {
                                        if (lsf) {
                                            this.i_stereo_k_values(this.is_pos[i], io_type, i);
                                        }
                                        else {
                                            this.is_ratio[i] = LayerIIIDecoder.TAN12[this.is_pos[i]];
                                        }
                                    }
                                    ++i;
                                }
                                ++sfb;
                            }
                        }
                    }
                    else {
                        for (int k = 0; k < 3; ++k) {
                            int sfbcnt2 = -1;
                            for (int sfb = 12; sfb >= 0; --sfb) {
                                final int temp = this.sfBandIndex[this.sfreq].s[sfb];
                                for (int lines = this.sfBandIndex[this.sfreq].s[sfb + 1] - temp, i = (temp << 2) - temp + (k + 1) * lines - 1; lines > 0; --lines, --i) {
                                    if (this.ro[1][i / 18][i % 18] != 0.0f) {
                                        sfbcnt2 = sfb;
                                        sfb = -10;
                                        lines = -10;
                                    }
                                }
                            }
                            for (int sfb = sfbcnt2 + 1; sfb < 12; ++sfb) {
                                final int temp = this.sfBandIndex[this.sfreq].s[sfb];
                                int sb = this.sfBandIndex[this.sfreq].s[sfb + 1] - temp;
                                int i = (temp << 2) - temp + k * sb;
                                while (sb > 0) {
                                    this.is_pos[i] = this.scalefac[1].s[k][sfb];
                                    if (this.is_pos[i] != 7) {
                                        if (lsf) {
                                            this.i_stereo_k_values(this.is_pos[i], io_type, i);
                                        }
                                        else {
                                            this.is_ratio[i] = LayerIIIDecoder.TAN12[this.is_pos[i]];
                                        }
                                    }
                                    ++i;
                                    --sb;
                                }
                            }
                            final int temp = this.sfBandIndex[this.sfreq].s[10];
                            final int temp2 = this.sfBandIndex[this.sfreq].s[11];
                            int sb = temp2 - temp;
                            int sfb = (temp << 2) - temp + k * sb;
                            sb = this.sfBandIndex[this.sfreq].s[12] - temp2;
                            int i = (temp2 << 2) - temp2 + k * sb;
                            while (sb > 0) {
                                this.is_pos[i] = this.is_pos[sfb];
                                if (lsf) {
                                    this.k[0][i] = this.k[0][sfb];
                                    this.k[1][i] = this.k[1][sfb];
                                }
                                else {
                                    this.is_ratio[i] = this.is_ratio[sfb];
                                }
                                ++i;
                                --sb;
                            }
                        }
                    }
                }
                else {
                    int i = 31;
                    int ss = 17;
                    int sb = 0;
                    while (i >= 0) {
                        if (this.ro[1][i][ss] != 0.0f) {
                            sb = (i << 4) + (i << 1) + ss;
                            i = -1;
                        }
                        else {
                            if (--ss >= 0) {
                                continue;
                            }
                            --i;
                            ss = 17;
                        }
                    }
                    for (i = 0; this.sfBandIndex[this.sfreq].l[i] <= sb; ++i) {}
                    int sfb = i;
                    i = this.sfBandIndex[this.sfreq].l[i];
                    while (sfb < 21) {
                        for (sb = this.sfBandIndex[this.sfreq].l[sfb + 1] - this.sfBandIndex[this.sfreq].l[sfb]; sb > 0; --sb) {
                            this.is_pos[i] = this.scalefac[1].l[sfb];
                            if (this.is_pos[i] != 7) {
                                if (lsf) {
                                    this.i_stereo_k_values(this.is_pos[i], io_type, i);
                                }
                                else {
                                    this.is_ratio[i] = LayerIIIDecoder.TAN12[this.is_pos[i]];
                                }
                            }
                            ++i;
                        }
                        ++sfb;
                    }
                    sfb = this.sfBandIndex[this.sfreq].l[20];
                    for (sb = 576 - this.sfBandIndex[this.sfreq].l[21]; sb > 0 && i < 576; ++i, --sb) {
                        this.is_pos[i] = this.is_pos[sfb];
                        if (lsf) {
                            this.k[0][i] = this.k[0][sfb];
                            this.k[1][i] = this.k[1][sfb];
                        }
                        else {
                            this.is_ratio[i] = this.is_ratio[sfb];
                        }
                    }
                }
            }
            int i = 0;
            for (int sb = 0; sb < 32; ++sb) {
                for (int ss = 0; ss < 18; ++ss) {
                    if (this.is_pos[i] == 7) {
                        if (ms_stereo) {
                            this.lr[0][sb][ss] = (this.ro[0][sb][ss] + this.ro[1][sb][ss]) * 0.70710677f;
                            this.lr[1][sb][ss] = (this.ro[0][sb][ss] - this.ro[1][sb][ss]) * 0.70710677f;
                        }
                        else {
                            this.lr[0][sb][ss] = this.ro[0][sb][ss];
                            this.lr[1][sb][ss] = this.ro[1][sb][ss];
                        }
                    }
                    else if (i_stereo) {
                        if (lsf) {
                            this.lr[0][sb][ss] = this.ro[0][sb][ss] * this.k[0][i];
                            this.lr[1][sb][ss] = this.ro[0][sb][ss] * this.k[1][i];
                        }
                        else {
                            this.lr[1][sb][ss] = this.ro[0][sb][ss] / (1.0f + this.is_ratio[i]);
                            this.lr[0][sb][ss] = this.lr[1][sb][ss] * this.is_ratio[i];
                        }
                    }
                    ++i;
                }
            }
        }
    }
    
    private void antialias(final int ch, final int gr) {
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        if (gr_info.window_switching_flag != 0 && gr_info.block_type == 2 && gr_info.mixed_block_flag == 0) {
            return;
        }
        int sb18lim;
        if (gr_info.window_switching_flag != 0 && gr_info.mixed_block_flag != 0 && gr_info.block_type == 2) {
            sb18lim = 18;
        }
        else {
            sb18lim = 558;
        }
        for (int sb18 = 0; sb18 < sb18lim; sb18 += 18) {
            for (int ss = 0; ss < 8; ++ss) {
                final int src_idx1 = sb18 + 17 - ss;
                final int src_idx2 = sb18 + 18 + ss;
                final float bu = this.out_1d[src_idx1];
                final float bd = this.out_1d[src_idx2];
                this.out_1d[src_idx1] = bu * LayerIIIDecoder.cs[ss] - bd * LayerIIIDecoder.ca[ss];
                this.out_1d[src_idx2] = bd * LayerIIIDecoder.cs[ss] + bu * LayerIIIDecoder.ca[ss];
            }
        }
    }
    
    private void hybrid(final int ch, final int gr) {
        final gr_info_s gr_info = this.si.ch[ch].gr[gr];
        for (int sb18 = 0; sb18 < 576; sb18 += 18) {
            final int bt = (gr_info.window_switching_flag != 0 && gr_info.mixed_block_flag != 0 && sb18 < 36) ? 0 : gr_info.block_type;
            final float[] tsOut = this.out_1d;
            for (int cc = 0; cc < 18; ++cc) {
                this.tsOutCopy[cc] = tsOut[cc + sb18];
            }
            this.inv_mdct(this.tsOutCopy, this.rawout, bt);
            for (int cc = 0; cc < 18; ++cc) {
                tsOut[cc + sb18] = this.tsOutCopy[cc];
            }
            final float[][] prvblk = this.prevblck;
            tsOut[0 + sb18] = this.rawout[0] + prvblk[ch][sb18 + 0];
            prvblk[ch][sb18 + 0] = this.rawout[18];
            tsOut[1 + sb18] = this.rawout[1] + prvblk[ch][sb18 + 1];
            prvblk[ch][sb18 + 1] = this.rawout[19];
            tsOut[2 + sb18] = this.rawout[2] + prvblk[ch][sb18 + 2];
            prvblk[ch][sb18 + 2] = this.rawout[20];
            tsOut[3 + sb18] = this.rawout[3] + prvblk[ch][sb18 + 3];
            prvblk[ch][sb18 + 3] = this.rawout[21];
            tsOut[4 + sb18] = this.rawout[4] + prvblk[ch][sb18 + 4];
            prvblk[ch][sb18 + 4] = this.rawout[22];
            tsOut[5 + sb18] = this.rawout[5] + prvblk[ch][sb18 + 5];
            prvblk[ch][sb18 + 5] = this.rawout[23];
            tsOut[6 + sb18] = this.rawout[6] + prvblk[ch][sb18 + 6];
            prvblk[ch][sb18 + 6] = this.rawout[24];
            tsOut[7 + sb18] = this.rawout[7] + prvblk[ch][sb18 + 7];
            prvblk[ch][sb18 + 7] = this.rawout[25];
            tsOut[8 + sb18] = this.rawout[8] + prvblk[ch][sb18 + 8];
            prvblk[ch][sb18 + 8] = this.rawout[26];
            tsOut[9 + sb18] = this.rawout[9] + prvblk[ch][sb18 + 9];
            prvblk[ch][sb18 + 9] = this.rawout[27];
            tsOut[10 + sb18] = this.rawout[10] + prvblk[ch][sb18 + 10];
            prvblk[ch][sb18 + 10] = this.rawout[28];
            tsOut[11 + sb18] = this.rawout[11] + prvblk[ch][sb18 + 11];
            prvblk[ch][sb18 + 11] = this.rawout[29];
            tsOut[12 + sb18] = this.rawout[12] + prvblk[ch][sb18 + 12];
            prvblk[ch][sb18 + 12] = this.rawout[30];
            tsOut[13 + sb18] = this.rawout[13] + prvblk[ch][sb18 + 13];
            prvblk[ch][sb18 + 13] = this.rawout[31];
            tsOut[14 + sb18] = this.rawout[14] + prvblk[ch][sb18 + 14];
            prvblk[ch][sb18 + 14] = this.rawout[32];
            tsOut[15 + sb18] = this.rawout[15] + prvblk[ch][sb18 + 15];
            prvblk[ch][sb18 + 15] = this.rawout[33];
            tsOut[16 + sb18] = this.rawout[16] + prvblk[ch][sb18 + 16];
            prvblk[ch][sb18 + 16] = this.rawout[34];
            tsOut[17 + sb18] = this.rawout[17] + prvblk[ch][sb18 + 17];
            prvblk[ch][sb18 + 17] = this.rawout[35];
        }
    }
    
    private void do_downmix() {
        for (int sb = 0; sb < 18; ++sb) {
            for (int ss = 0; ss < 18; ss += 3) {
                this.lr[0][sb][ss] = (this.lr[0][sb][ss] + this.lr[1][sb][ss]) * 0.5f;
                this.lr[0][sb][ss + 1] = (this.lr[0][sb][ss + 1] + this.lr[1][sb][ss + 1]) * 0.5f;
                this.lr[0][sb][ss + 2] = (this.lr[0][sb][ss + 2] + this.lr[1][sb][ss + 2]) * 0.5f;
            }
        }
    }
    
    public void inv_mdct(final float[] in, final float[] out, final int block_type) {
        float tmpf_18;
        float tmpf_17;
        float tmpf_16;
        float tmpf_15;
        float tmpf_14;
        float tmpf_13;
        float tmpf_12;
        float tmpf_11;
        float tmpf_10;
        float tmpf_9;
        float tmpf_8;
        float tmpf_7;
        float tmpf_6;
        float tmpf_5;
        float tmpf_4;
        float tmpf_3;
        float tmpf_2;
        float tmpf_1 = tmpf_2 = (tmpf_3 = (tmpf_4 = (tmpf_5 = (tmpf_6 = (tmpf_7 = (tmpf_8 = (tmpf_9 = (tmpf_10 = (tmpf_11 = (tmpf_12 = (tmpf_13 = (tmpf_14 = (tmpf_15 = (tmpf_16 = (tmpf_17 = (tmpf_18 = 0.0f))))))))))))))));
        if (block_type == 2) {
            out[1] = (out[0] = 0.0f);
            out[3] = (out[2] = 0.0f);
            out[5] = (out[4] = 0.0f);
            out[7] = (out[6] = 0.0f);
            out[9] = (out[8] = 0.0f);
            out[11] = (out[10] = 0.0f);
            out[13] = (out[12] = 0.0f);
            out[15] = (out[14] = 0.0f);
            out[17] = (out[16] = 0.0f);
            out[19] = (out[18] = 0.0f);
            out[21] = (out[20] = 0.0f);
            out[23] = (out[22] = 0.0f);
            out[25] = (out[24] = 0.0f);
            out[27] = (out[26] = 0.0f);
            out[29] = (out[28] = 0.0f);
            out[31] = (out[30] = 0.0f);
            out[33] = (out[32] = 0.0f);
            out[35] = (out[34] = 0.0f);
            int six_i = 0;
            for (int i = 0; i < 3; ++i) {
                final int n = 15 + i;
                in[n] += in[12 + i];
                final int n2 = 12 + i;
                in[n2] += in[9 + i];
                final int n3 = 9 + i;
                in[n3] += in[6 + i];
                final int n4 = 6 + i;
                in[n4] += in[3 + i];
                final int n5 = 3 + i;
                in[n5] += in[0 + i];
                final int n6 = 15 + i;
                in[n6] += in[9 + i];
                final int n7 = 9 + i;
                in[n7] += in[3 + i];
                float pp2 = in[12 + i] * 0.5f;
                float pp3 = in[6 + i] * 0.8660254f;
                float sum = in[0 + i] + pp2;
                tmpf_1 = in[0 + i] - in[12 + i];
                tmpf_2 = sum + pp3;
                tmpf_3 = sum - pp3;
                pp2 = in[15 + i] * 0.5f;
                pp3 = in[9 + i] * 0.8660254f;
                sum = in[3 + i] + pp2;
                tmpf_5 = in[3 + i] - in[15 + i];
                tmpf_6 = sum + pp3;
                tmpf_4 = sum - pp3;
                tmpf_4 *= 1.9318516f;
                tmpf_5 *= 0.70710677f;
                tmpf_6 *= 0.5176381f;
                float save = tmpf_2;
                tmpf_2 += tmpf_6;
                tmpf_6 = save - tmpf_6;
                save = tmpf_1;
                tmpf_1 += tmpf_5;
                tmpf_5 = save - tmpf_5;
                save = tmpf_3;
                tmpf_3 += tmpf_4;
                tmpf_4 = save - tmpf_4;
                tmpf_2 *= 0.5043145f;
                tmpf_1 *= 0.5411961f;
                tmpf_3 *= 0.6302362f;
                tmpf_4 *= 0.8213398f;
                tmpf_5 *= 1.306563f;
                tmpf_6 *= 3.830649f;
                tmpf_9 = -tmpf_2 * 0.7933533f;
                tmpf_10 = -tmpf_2 * 0.6087614f;
                tmpf_8 = -tmpf_1 * 0.9238795f;
                tmpf_11 = -tmpf_1 * 0.38268343f;
                tmpf_7 = -tmpf_3 * 0.9914449f;
                tmpf_12 = -tmpf_3 * 0.13052619f;
                tmpf_2 = tmpf_4;
                tmpf_1 = tmpf_5 * 0.38268343f;
                tmpf_3 = tmpf_6 * 0.6087614f;
                tmpf_4 = -tmpf_6 * 0.7933533f;
                tmpf_5 = -tmpf_5 * 0.9238795f;
                tmpf_6 = -tmpf_2 * 0.9914449f;
                tmpf_2 *= 0.13052619f;
                final int n8 = six_i + 6;
                out[n8] += tmpf_2;
                final int n9 = six_i + 7;
                out[n9] += tmpf_1;
                final int n10 = six_i + 8;
                out[n10] += tmpf_3;
                final int n11 = six_i + 9;
                out[n11] += tmpf_4;
                final int n12 = six_i + 10;
                out[n12] += tmpf_5;
                final int n13 = six_i + 11;
                out[n13] += tmpf_6;
                final int n14 = six_i + 12;
                out[n14] += tmpf_7;
                final int n15 = six_i + 13;
                out[n15] += tmpf_8;
                final int n16 = six_i + 14;
                out[n16] += tmpf_9;
                final int n17 = six_i + 15;
                out[n17] += tmpf_10;
                final int n18 = six_i + 16;
                out[n18] += tmpf_11;
                final int n19 = six_i + 17;
                out[n19] += tmpf_12;
                six_i += 6;
            }
        }
        else {
            final int n20 = 17;
            in[n20] += in[16];
            final int n21 = 16;
            in[n21] += in[15];
            final int n22 = 15;
            in[n22] += in[14];
            final int n23 = 14;
            in[n23] += in[13];
            final int n24 = 13;
            in[n24] += in[12];
            final int n25 = 12;
            in[n25] += in[11];
            final int n26 = 11;
            in[n26] += in[10];
            final int n27 = 10;
            in[n27] += in[9];
            final int n28 = 9;
            in[n28] += in[8];
            final int n29 = 8;
            in[n29] += in[7];
            final int n30 = 7;
            in[n30] += in[6];
            final int n31 = 6;
            in[n31] += in[5];
            final int n32 = 5;
            in[n32] += in[4];
            final int n33 = 4;
            in[n33] += in[3];
            final int n34 = 3;
            in[n34] += in[2];
            final int n35 = 2;
            in[n35] += in[1];
            final int n36 = 1;
            in[n36] += in[0];
            final int n37 = 17;
            in[n37] += in[15];
            final int n38 = 15;
            in[n38] += in[13];
            final int n39 = 13;
            in[n39] += in[11];
            final int n40 = 11;
            in[n40] += in[9];
            final int n41 = 9;
            in[n41] += in[7];
            final int n42 = 7;
            in[n42] += in[5];
            final int n43 = 5;
            in[n43] += in[3];
            final int n44 = 3;
            in[n44] += in[1];
            final float i2 = in[0] + in[0];
            final float iip12 = i2 + in[12];
            final float tmp0 = iip12 + in[4] * 1.8793852f + in[8] * 1.5320889f + in[16] * 0.34729636f;
            final float tmp2 = i2 + in[4] - in[8] - in[12] - in[12] - in[16];
            final float tmp3 = iip12 - in[4] * 0.34729636f - in[8] * 1.8793852f + in[16] * 1.5320889f;
            final float tmp4 = iip12 - in[4] * 1.5320889f + in[8] * 0.34729636f - in[16] * 1.8793852f;
            final float tmp5 = in[0] - in[4] + in[8] - in[12] + in[16];
            final float i66_ = in[6] * 1.7320508f;
            final float tmp0_ = in[2] * 1.9696155f + i66_ + in[10] * 1.2855753f + in[14] * 0.6840403f;
            final float tmp1_ = (in[2] - in[10] - in[14]) * 1.7320508f;
            final float tmp2_ = in[2] * 1.2855753f - i66_ - in[10] * 0.6840403f + in[14] * 1.9696155f;
            final float tmp3_ = in[2] * 0.6840403f - i66_ + in[10] * 1.9696155f - in[14] * 1.2855753f;
            final float i3 = in[1] + in[1];
            final float i0p12 = i3 + in[13];
            final float tmp0o = i0p12 + in[5] * 1.8793852f + in[9] * 1.5320889f + in[17] * 0.34729636f;
            final float tmp1o = i3 + in[5] - in[9] - in[13] - in[13] - in[17];
            final float tmp2o = i0p12 - in[5] * 0.34729636f - in[9] * 1.8793852f + in[17] * 1.5320889f;
            final float tmp3o = i0p12 - in[5] * 1.5320889f + in[9] * 0.34729636f - in[17] * 1.8793852f;
            final float tmp4o = (in[1] - in[5] + in[9] - in[13] + in[17]) * 0.70710677f;
            final float i6_ = in[7] * 1.7320508f;
            final float tmp0_o = in[3] * 1.9696155f + i6_ + in[11] * 1.2855753f + in[15] * 0.6840403f;
            final float tmp1_o = (in[3] - in[11] - in[15]) * 1.7320508f;
            final float tmp2_o = in[3] * 1.2855753f - i6_ - in[11] * 0.6840403f + in[15] * 1.9696155f;
            final float tmp3_o = in[3] * 0.6840403f - i6_ + in[11] * 1.9696155f - in[15] * 1.2855753f;
            float e = tmp0 + tmp0_;
            float o = (tmp0o + tmp0_o) * 0.5019099f;
            tmpf_2 = e + o;
            tmpf_18 = e - o;
            e = tmp2 + tmp1_;
            o = (tmp1o + tmp1_o) * 0.5176381f;
            tmpf_1 = e + o;
            tmpf_17 = e - o;
            e = tmp3 + tmp2_;
            o = (tmp2o + tmp2_o) * 0.55168897f;
            tmpf_3 = e + o;
            tmpf_16 = e - o;
            e = tmp4 + tmp3_;
            o = (tmp3o + tmp3_o) * 0.61038727f;
            tmpf_4 = e + o;
            tmpf_15 = e - o;
            tmpf_5 = tmp5 + tmp4o;
            tmpf_14 = tmp5 - tmp4o;
            e = tmp4 - tmp3_;
            o = (tmp3o - tmp3_o) * 0.8717234f;
            tmpf_6 = e + o;
            tmpf_13 = e - o;
            e = tmp3 - tmp2_;
            o = (tmp2o - tmp2_o) * 1.1831008f;
            tmpf_7 = e + o;
            tmpf_12 = e - o;
            e = tmp2 - tmp1_;
            o = (tmp1o - tmp1_o) * 1.9318516f;
            tmpf_8 = e + o;
            tmpf_11 = e - o;
            e = tmp0 - tmp0_;
            o = (tmp0o - tmp0_o) * 5.7368565f;
            tmpf_9 = e + o;
            tmpf_10 = e - o;
            final float[] win_bt = LayerIIIDecoder.win[block_type];
            out[0] = -tmpf_10 * win_bt[0];
            out[1] = -tmpf_11 * win_bt[1];
            out[2] = -tmpf_12 * win_bt[2];
            out[3] = -tmpf_13 * win_bt[3];
            out[4] = -tmpf_14 * win_bt[4];
            out[5] = -tmpf_15 * win_bt[5];
            out[6] = -tmpf_16 * win_bt[6];
            out[7] = -tmpf_17 * win_bt[7];
            out[8] = -tmpf_18 * win_bt[8];
            out[9] = tmpf_18 * win_bt[9];
            out[10] = tmpf_17 * win_bt[10];
            out[11] = tmpf_16 * win_bt[11];
            out[12] = tmpf_15 * win_bt[12];
            out[13] = tmpf_14 * win_bt[13];
            out[14] = tmpf_13 * win_bt[14];
            out[15] = tmpf_12 * win_bt[15];
            out[16] = tmpf_11 * win_bt[16];
            out[17] = tmpf_10 * win_bt[17];
            out[18] = tmpf_9 * win_bt[18];
            out[19] = tmpf_8 * win_bt[19];
            out[20] = tmpf_7 * win_bt[20];
            out[21] = tmpf_6 * win_bt[21];
            out[22] = tmpf_5 * win_bt[22];
            out[23] = tmpf_4 * win_bt[23];
            out[24] = tmpf_3 * win_bt[24];
            out[25] = tmpf_1 * win_bt[25];
            out[26] = tmpf_2 * win_bt[26];
            out[27] = tmpf_2 * win_bt[27];
            out[28] = tmpf_1 * win_bt[28];
            out[29] = tmpf_3 * win_bt[29];
            out[30] = tmpf_4 * win_bt[30];
            out[31] = tmpf_5 * win_bt[31];
            out[32] = tmpf_6 * win_bt[32];
            out[33] = tmpf_7 * win_bt[33];
            out[34] = tmpf_8 * win_bt[34];
            out[35] = tmpf_9 * win_bt[35];
        }
    }
    
    private static float[] create_t_43() {
        final float[] t43 = new float[8192];
        final double d43 = 1.3333333333333333;
        for (int i = 0; i < 8192; ++i) {
            t43[i] = (float)Math.pow(i, 1.3333333333333333);
        }
        return t43;
    }
    
    static int[] reorder(final int[] scalefac_band) {
        int j = 0;
        final int[] ix = new int[576];
        for (int sfb = 0; sfb < 13; ++sfb) {
            final int start = scalefac_band[sfb];
            final int end = scalefac_band[sfb + 1];
            for (int window = 0; window < 3; ++window) {
                for (int i = start; i < end; ++i) {
                    ix[3 * i + window] = j++;
                }
            }
        }
        return ix;
    }
    
    static {
        slen = new int[][] { { 0, 0, 0, 0, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4 }, { 0, 1, 2, 3, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 2, 3 } };
        pretab = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 3, 2, 0 };
        two_to_negative_half_pow = new float[] { 1.0f, 0.70710677f, 0.5f, 0.35355338f, 0.25f, 0.17677669f, 0.125f, 0.088388346f, 0.0625f, 0.044194173f, 0.03125f, 0.022097087f, 0.015625f, 0.011048543f, 0.0078125f, 0.0055242716f, 0.00390625f, 0.0027621358f, 0.001953125f, 0.0013810679f, 9.765625E-4f, 6.9053395E-4f, 4.8828125E-4f, 3.4526698E-4f, 2.4414062E-4f, 1.7263349E-4f, 1.2207031E-4f, 8.6316744E-5f, 6.1035156E-5f, 4.3158372E-5f, 3.0517578E-5f, 2.1579186E-5f, 1.5258789E-5f, 1.0789593E-5f, 7.6293945E-6f, 5.3947965E-6f, 3.8146973E-6f, 2.6973983E-6f, 1.9073486E-6f, 1.3486991E-6f, 9.536743E-7f, 6.7434956E-7f, 4.7683716E-7f, 3.3717478E-7f, 2.3841858E-7f, 1.6858739E-7f, 1.1920929E-7f, 8.4293696E-8f, 5.9604645E-8f, 4.2146848E-8f, 2.9802322E-8f, 2.1073424E-8f, 1.4901161E-8f, 1.0536712E-8f, 7.4505806E-9f, 5.268356E-9f, 3.7252903E-9f, 2.634178E-9f, 1.8626451E-9f, 1.317089E-9f, 9.313226E-10f, 6.585445E-10f, 4.656613E-10f, 3.2927225E-10f };
        t_43 = create_t_43();
        io = new float[][] { { 1.0f, 0.8408964f, 0.70710677f, 0.59460354f, 0.5f, 0.4204482f, 0.35355338f, 0.29730177f, 0.25f, 0.2102241f, 0.17677669f, 0.14865088f, 0.125f, 0.10511205f, 0.088388346f, 0.07432544f, 0.0625f, 0.052556027f, 0.044194173f, 0.03716272f, 0.03125f, 0.026278013f, 0.022097087f, 0.01858136f, 0.015625f, 0.013139007f, 0.011048543f, 0.00929068f, 0.0078125f, 0.0065695033f, 0.0055242716f, 0.00464534f }, { 1.0f, 0.70710677f, 0.5f, 0.35355338f, 0.25f, 0.17677669f, 0.125f, 0.088388346f, 0.0625f, 0.044194173f, 0.03125f, 0.022097087f, 0.015625f, 0.011048543f, 0.0078125f, 0.0055242716f, 0.00390625f, 0.0027621358f, 0.001953125f, 0.0013810679f, 9.765625E-4f, 6.9053395E-4f, 4.8828125E-4f, 3.4526698E-4f, 2.4414062E-4f, 1.7263349E-4f, 1.2207031E-4f, 8.6316744E-5f, 6.1035156E-5f, 4.3158372E-5f, 3.0517578E-5f, 2.1579186E-5f } };
        TAN12 = new float[] { 0.0f, 0.2679492f, 0.57735026f, 1.0f, 1.7320508f, 3.732051f, 9.9999998E10f, -3.732051f, -1.7320508f, -1.0f, -0.57735026f, -0.2679492f, 0.0f, 0.2679492f, 0.57735026f, 1.0f };
        cs = new float[] { 0.8574929f, 0.881742f, 0.94962865f, 0.9833146f, 0.9955178f, 0.9991606f, 0.9998992f, 0.99999315f };
        ca = new float[] { -0.51449573f, -0.47173196f, -0.31337744f, -0.1819132f, -0.09457419f, -0.040965583f, -0.014198569f, -0.0036999746f };
        win = new float[][] { { -0.016141215f, -0.05360318f, -0.100707136f, -0.16280818f, -0.5f, -0.38388735f, -0.6206114f, -1.1659756f, -3.8720753f, -4.225629f, -1.519529f, -0.97416484f, -0.73744076f, -1.2071068f, -0.5163616f, -0.45426053f, -0.40715656f, -0.3696946f, -0.3387627f, -0.31242222f, -0.28939587f, -0.26880082f, -0.5f, -0.23251417f, -0.21596715f, -0.20004979f, -0.18449493f, -0.16905846f, -0.15350361f, -0.13758625f, -0.12103922f, -0.20710678f, -0.084752575f, -0.06415752f, -0.041131172f, -0.014790705f }, { -0.016141215f, -0.05360318f, -0.100707136f, -0.16280818f, -0.5f, -0.38388735f, -0.6206114f, -1.1659756f, -3.8720753f, -4.225629f, -1.519529f, -0.97416484f, -0.73744076f, -1.2071068f, -0.5163616f, -0.45426053f, -0.40715656f, -0.3696946f, -0.33908543f, -0.3151181f, -0.29642227f, -0.28184548f, -0.5411961f, -0.2621323f, -0.25387916f, -0.2329629f, -0.19852729f, -0.15233535f, -0.0964964f, -0.03342383f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, { -0.0483008f, -0.15715657f, -0.28325045f, -0.42953748f, -1.2071068f, -0.8242648f, -1.1451749f, -1.769529f, -4.5470223f, -3.489053f, -0.7329629f, -0.15076515f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.15076514f, -0.7329629f, -3.489053f, -4.5470223f, -1.769529f, -1.1451749f, -0.8313774f, -1.306563f, -0.54142016f, -0.46528974f, -0.4106699f, -0.3700468f, -0.3387627f, -0.31242222f, -0.28939587f, -0.26880082f, -0.5f, -0.23251417f, -0.21596715f, -0.20004979f, -0.18449493f, -0.16905846f, -0.15350361f, -0.13758625f, -0.12103922f, -0.20710678f, -0.084752575f, -0.06415752f, -0.041131172f, -0.014790705f } };
        nr_of_sfb_block = new int[][][] { { { 6, 5, 5, 5 }, { 9, 9, 9, 9 }, { 6, 9, 9, 9 } }, { { 6, 5, 7, 3 }, { 9, 9, 12, 6 }, { 6, 9, 12, 6 } }, { { 11, 10, 0, 0 }, { 18, 18, 0, 0 }, { 15, 18, 0, 0 } }, { { 7, 7, 7, 0 }, { 12, 12, 12, 0 }, { 6, 15, 12, 0 } }, { { 6, 6, 6, 3 }, { 12, 9, 9, 6 }, { 6, 12, 9, 6 } }, { { 8, 8, 5, 0 }, { 15, 12, 9, 0 }, { 6, 18, 9, 0 } } };
    }
    
    static class SBI
    {
        public int[] l;
        public int[] s;
        
        public SBI() {
            this.l = new int[23];
            this.s = new int[14];
        }
        
        public SBI(final int[] thel, final int[] thes) {
            this.l = thel;
            this.s = thes;
        }
    }
    
    static class gr_info_s
    {
        public int part2_3_length;
        public int big_values;
        public int global_gain;
        public int scalefac_compress;
        public int window_switching_flag;
        public int block_type;
        public int mixed_block_flag;
        public int[] table_select;
        public int[] subblock_gain;
        public int region0_count;
        public int region1_count;
        public int preflag;
        public int scalefac_scale;
        public int count1table_select;
        
        public gr_info_s() {
            this.part2_3_length = 0;
            this.big_values = 0;
            this.global_gain = 0;
            this.scalefac_compress = 0;
            this.window_switching_flag = 0;
            this.block_type = 0;
            this.mixed_block_flag = 0;
            this.region0_count = 0;
            this.region1_count = 0;
            this.preflag = 0;
            this.scalefac_scale = 0;
            this.count1table_select = 0;
            this.table_select = new int[3];
            this.subblock_gain = new int[3];
        }
    }
    
    static class temporaire
    {
        public int[] scfsi;
        public gr_info_s[] gr;
        
        public temporaire() {
            this.scfsi = new int[4];
            (this.gr = new gr_info_s[2])[0] = new gr_info_s();
            this.gr[1] = new gr_info_s();
        }
    }
    
    static class III_side_info_t
    {
        public int main_data_begin;
        public int private_bits;
        public temporaire[] ch;
        
        public III_side_info_t() {
            this.main_data_begin = 0;
            this.private_bits = 0;
            (this.ch = new temporaire[2])[0] = new temporaire();
            this.ch[1] = new temporaire();
        }
    }
    
    static class temporaire2
    {
        public int[] l;
        public int[][] s;
        
        public temporaire2() {
            this.l = new int[23];
            this.s = new int[3][13];
        }
    }
    
    class Sftable
    {
        public int[] l;
        public int[] s;
        
        public Sftable() {
            this.l = new int[5];
            this.s = new int[3];
        }
        
        public Sftable(final int[] thel, final int[] thes) {
            this.l = thel;
            this.s = thes;
        }
    }
}
