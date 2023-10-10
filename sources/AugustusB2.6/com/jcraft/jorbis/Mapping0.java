// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Mapping0 extends FuncMapping
{
    static int seq;
    float[][] pcmbundle;
    int[] zerobundle;
    int[] nonzero;
    Object[] floormemo;
    
    Mapping0() {
        this.pcmbundle = null;
        this.zerobundle = null;
        this.nonzero = null;
        this.floormemo = null;
    }
    
    void free_info(final Object o) {
    }
    
    void free_look(final Object o) {
    }
    
    Object look(final DspState dspState, final InfoMode mode, final Object o) {
        final Info vi = dspState.vi;
        final LookMapping0 lookMapping2;
        final LookMapping0 lookMapping0 = lookMapping2 = new LookMapping0();
        final InfoMapping0 map = (InfoMapping0)o;
        lookMapping2.map = map;
        final InfoMapping0 infoMapping0 = map;
        lookMapping0.mode = mode;
        lookMapping0.time_look = new Object[infoMapping0.submaps];
        lookMapping0.floor_look = new Object[infoMapping0.submaps];
        lookMapping0.residue_look = new Object[infoMapping0.submaps];
        lookMapping0.time_func = new FuncTime[infoMapping0.submaps];
        lookMapping0.floor_func = new FuncFloor[infoMapping0.submaps];
        lookMapping0.residue_func = new FuncResidue[infoMapping0.submaps];
        for (int i = 0; i < infoMapping0.submaps; ++i) {
            final int n = infoMapping0.timesubmap[i];
            final int n2 = infoMapping0.floorsubmap[i];
            final int n3 = infoMapping0.residuesubmap[i];
            lookMapping0.time_func[i] = FuncTime.time_P[vi.time_type[n]];
            lookMapping0.time_look[i] = lookMapping0.time_func[i].look(dspState, mode, vi.time_param[n]);
            lookMapping0.floor_func[i] = FuncFloor.floor_P[vi.floor_type[n2]];
            lookMapping0.floor_look[i] = lookMapping0.floor_func[i].look(dspState, mode, vi.floor_param[n2]);
            lookMapping0.residue_func[i] = FuncResidue.residue_P[vi.residue_type[n3]];
            lookMapping0.residue_look[i] = lookMapping0.residue_func[i].look(dspState, mode, vi.residue_param[n3]);
        }
        if (vi.psys == 0 || dspState.analysisp != 0) {}
        lookMapping0.ch = vi.channels;
        return lookMapping0;
    }
    
    void pack(final Info info, final Object o, final Buffer buffer) {
        final InfoMapping0 infoMapping0 = (InfoMapping0)o;
        if (infoMapping0.submaps > 1) {
            buffer.write(1, 1);
            buffer.write(infoMapping0.submaps - 1, 4);
        }
        else {
            buffer.write(0, 1);
        }
        if (infoMapping0.coupling_steps > 0) {
            buffer.write(1, 1);
            buffer.write(infoMapping0.coupling_steps - 1, 8);
            for (int i = 0; i < infoMapping0.coupling_steps; ++i) {
                buffer.write(infoMapping0.coupling_mag[i], ilog2(info.channels));
                buffer.write(infoMapping0.coupling_ang[i], ilog2(info.channels));
            }
        }
        else {
            buffer.write(0, 1);
        }
        buffer.write(0, 2);
        if (infoMapping0.submaps > 1) {
            for (int j = 0; j < info.channels; ++j) {
                buffer.write(infoMapping0.chmuxlist[j], 4);
            }
        }
        for (int k = 0; k < infoMapping0.submaps; ++k) {
            buffer.write(infoMapping0.timesubmap[k], 8);
            buffer.write(infoMapping0.floorsubmap[k], 8);
            buffer.write(infoMapping0.residuesubmap[k], 8);
        }
    }
    
    Object unpack(final Info info, final Buffer buffer) {
        final InfoMapping0 infoMapping0 = new InfoMapping0();
        if (buffer.read(1) != 0) {
            infoMapping0.submaps = buffer.read(4) + 1;
        }
        else {
            infoMapping0.submaps = 1;
        }
        if (buffer.read(1) != 0) {
            infoMapping0.coupling_steps = buffer.read(8) + 1;
            for (int i = 0; i < infoMapping0.coupling_steps; ++i) {
                final int[] coupling_mag = infoMapping0.coupling_mag;
                final int n = i;
                final int read = buffer.read(ilog2(info.channels));
                coupling_mag[n] = read;
                final int n2 = read;
                final int[] coupling_ang = infoMapping0.coupling_ang;
                final int n3 = i;
                final int read2 = buffer.read(ilog2(info.channels));
                coupling_ang[n3] = read2;
                final int n4 = read2;
                if (n2 < 0 || n4 < 0 || n2 == n4 || n2 >= info.channels || n4 >= info.channels) {
                    infoMapping0.free();
                    return null;
                }
            }
        }
        if (buffer.read(2) > 0) {
            infoMapping0.free();
            return null;
        }
        if (infoMapping0.submaps > 1) {
            for (int j = 0; j < info.channels; ++j) {
                infoMapping0.chmuxlist[j] = buffer.read(4);
                if (infoMapping0.chmuxlist[j] >= infoMapping0.submaps) {
                    infoMapping0.free();
                    return null;
                }
            }
        }
        for (int k = 0; k < infoMapping0.submaps; ++k) {
            infoMapping0.timesubmap[k] = buffer.read(8);
            if (infoMapping0.timesubmap[k] >= info.times) {
                infoMapping0.free();
                return null;
            }
            infoMapping0.floorsubmap[k] = buffer.read(8);
            if (infoMapping0.floorsubmap[k] >= info.floors) {
                infoMapping0.free();
                return null;
            }
            infoMapping0.residuesubmap[k] = buffer.read(8);
            if (infoMapping0.residuesubmap[k] >= info.residues) {
                infoMapping0.free();
                return null;
            }
        }
        return infoMapping0;
    }
    
    synchronized int inverse(final Block block, final Object o) {
        final DspState vd = block.vd;
        final Info vi = vd.vi;
        final LookMapping0 lookMapping0 = (LookMapping0)o;
        final InfoMapping0 map = lookMapping0.map;
        final InfoMode mode = lookMapping0.mode;
        final int pcmend = vi.blocksizes[block.W];
        block.pcmend = pcmend;
        final int n = pcmend;
        final float[] array = vd.window[block.W][block.lW][block.nW][mode.windowtype];
        if (this.pcmbundle == null || this.pcmbundle.length < vi.channels) {
            this.pcmbundle = new float[vi.channels][];
            this.nonzero = new int[vi.channels];
            this.zerobundle = new int[vi.channels];
            this.floormemo = new Object[vi.channels];
        }
        for (int i = 0; i < vi.channels; ++i) {
            final float[] array2 = block.pcm[i];
            final int n2 = map.chmuxlist[i];
            this.floormemo[i] = lookMapping0.floor_func[n2].inverse1(block, lookMapping0.floor_look[n2], this.floormemo[i]);
            if (this.floormemo[i] != null) {
                this.nonzero[i] = 1;
            }
            else {
                this.nonzero[i] = 0;
            }
            for (int j = 0; j < n / 2; ++j) {
                array2[j] = 0.0f;
            }
        }
        for (int k = 0; k < map.coupling_steps; ++k) {
            if (this.nonzero[map.coupling_mag[k]] != 0 || this.nonzero[map.coupling_ang[k]] != 0) {
                this.nonzero[map.coupling_mag[k]] = 1;
                this.nonzero[map.coupling_ang[k]] = 1;
            }
        }
        for (int l = 0; l < map.submaps; ++l) {
            int n3 = 0;
            for (int n4 = 0; n4 < vi.channels; ++n4) {
                if (map.chmuxlist[n4] == l) {
                    if (this.nonzero[n4] != 0) {
                        this.zerobundle[n3] = 1;
                    }
                    else {
                        this.zerobundle[n3] = 0;
                    }
                    this.pcmbundle[n3++] = block.pcm[n4];
                }
            }
            lookMapping0.residue_func[l].inverse(block, lookMapping0.residue_look[l], this.pcmbundle, this.zerobundle, n3);
        }
        for (int n5 = map.coupling_steps - 1; n5 >= 0; --n5) {
            final float[] array3 = block.pcm[map.coupling_mag[n5]];
            final float[] array4 = block.pcm[map.coupling_ang[n5]];
            for (int n6 = 0; n6 < n / 2; ++n6) {
                final float n7 = array3[n6];
                final float n8 = array4[n6];
                if (n7 > 0.0f) {
                    if (n8 > 0.0f) {
                        array4[n6] = (array3[n6] = n7) - n8;
                    }
                    else {
                        array3[n6] = (array4[n6] = n7) + n8;
                    }
                }
                else if (n8 > 0.0f) {
                    array4[n6] = (array3[n6] = n7) + n8;
                }
                else {
                    array3[n6] = (array4[n6] = n7) - n8;
                }
            }
        }
        for (int n9 = 0; n9 < vi.channels; ++n9) {
            final float[] array5 = block.pcm[n9];
            final int n10 = map.chmuxlist[n9];
            lookMapping0.floor_func[n10].inverse2(block, lookMapping0.floor_look[n10], this.floormemo[n9], array5);
        }
        for (int n11 = 0; n11 < vi.channels; ++n11) {
            final float[] array6 = block.pcm[n11];
            ((Mdct)vd.transform[block.W][0]).backward(array6, array6);
        }
        for (int n12 = 0; n12 < vi.channels; ++n12) {
            final float[] array7 = block.pcm[n12];
            if (this.nonzero[n12] != 0) {
                for (int n13 = 0; n13 < n; ++n13) {
                    final float[] array8 = array7;
                    final int n14 = n13;
                    array8[n14] *= array[n13];
                }
            }
            else {
                for (int n15 = 0; n15 < n; ++n15) {
                    array7[n15] = 0.0f;
                }
            }
        }
        return 0;
    }
    
    private static int ilog2(int i) {
        int n = 0;
        while (i > 1) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    static {
        Mapping0.seq = 0;
    }
}
