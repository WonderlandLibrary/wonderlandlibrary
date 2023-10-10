/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.FuncFloor;
import com.jcraft.jorbis.FuncMapping;
import com.jcraft.jorbis.FuncResidue;
import com.jcraft.jorbis.FuncTime;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.InfoMode;
import com.jcraft.jorbis.Mdct;
import com.jcraft.jorbis.PsyLook;
import com.jcraft.jorbis.Util;

class Mapping0
extends FuncMapping {
    static int seq = 0;
    float[][] pcmbundle = null;
    int[] zerobundle = null;
    int[] nonzero = null;
    Object[] floormemo = null;

    Mapping0() {
    }

    void free_info(Object imap) {
    }

    void free_look(Object imap) {
    }

    Object look(DspState vd, InfoMode vm, Object m2) {
        Info vi = vd.vi;
        LookMapping0 look = new LookMapping0();
        InfoMapping0 info = look.map = (InfoMapping0)m2;
        look.mode = vm;
        look.time_look = new Object[info.submaps];
        look.floor_look = new Object[info.submaps];
        look.residue_look = new Object[info.submaps];
        look.time_func = new FuncTime[info.submaps];
        look.floor_func = new FuncFloor[info.submaps];
        look.residue_func = new FuncResidue[info.submaps];
        for (int i2 = 0; i2 < info.submaps; ++i2) {
            int timenum = info.timesubmap[i2];
            int floornum = info.floorsubmap[i2];
            int resnum = info.residuesubmap[i2];
            look.time_func[i2] = FuncTime.time_P[vi.time_type[timenum]];
            look.time_look[i2] = look.time_func[i2].look(vd, vm, vi.time_param[timenum]);
            look.floor_func[i2] = FuncFloor.floor_P[vi.floor_type[floornum]];
            look.floor_look[i2] = look.floor_func[i2].look(vd, vm, vi.floor_param[floornum]);
            look.residue_func[i2] = FuncResidue.residue_P[vi.residue_type[resnum]];
            look.residue_look[i2] = look.residue_func[i2].look(vd, vm, vi.residue_param[resnum]);
        }
        if (vi.psys == 0 || vd.analysisp != 0) {
            // empty if block
        }
        look.ch = vi.channels;
        return look;
    }

    void pack(Info vi, Object imap, Buffer opb) {
        int i2;
        InfoMapping0 info = (InfoMapping0)imap;
        if (info.submaps > 1) {
            opb.write(1, 1);
            opb.write(info.submaps - 1, 4);
        } else {
            opb.write(0, 1);
        }
        if (info.coupling_steps > 0) {
            opb.write(1, 1);
            opb.write(info.coupling_steps - 1, 8);
            for (i2 = 0; i2 < info.coupling_steps; ++i2) {
                opb.write(info.coupling_mag[i2], Util.ilog2(vi.channels));
                opb.write(info.coupling_ang[i2], Util.ilog2(vi.channels));
            }
        } else {
            opb.write(0, 1);
        }
        opb.write(0, 2);
        if (info.submaps > 1) {
            for (i2 = 0; i2 < vi.channels; ++i2) {
                opb.write(info.chmuxlist[i2], 4);
            }
        }
        for (i2 = 0; i2 < info.submaps; ++i2) {
            opb.write(info.timesubmap[i2], 8);
            opb.write(info.floorsubmap[i2], 8);
            opb.write(info.residuesubmap[i2], 8);
        }
    }

    Object unpack(Info vi, Buffer opb) {
        int i2;
        InfoMapping0 info = new InfoMapping0();
        info.submaps = opb.read(1) != 0 ? opb.read(4) + 1 : 1;
        if (opb.read(1) != 0) {
            info.coupling_steps = opb.read(8) + 1;
            for (i2 = 0; i2 < info.coupling_steps; ++i2) {
                int testM = info.coupling_mag[i2] = opb.read(Util.ilog2(vi.channels));
                int testA = info.coupling_ang[i2] = opb.read(Util.ilog2(vi.channels));
                if (testM >= 0 && testA >= 0 && testM != testA && testM < vi.channels && testA < vi.channels) continue;
                info.free();
                return null;
            }
        }
        if (opb.read(2) > 0) {
            info.free();
            return null;
        }
        if (info.submaps > 1) {
            for (i2 = 0; i2 < vi.channels; ++i2) {
                info.chmuxlist[i2] = opb.read(4);
                if (info.chmuxlist[i2] < info.submaps) continue;
                info.free();
                return null;
            }
        }
        for (i2 = 0; i2 < info.submaps; ++i2) {
            info.timesubmap[i2] = opb.read(8);
            if (info.timesubmap[i2] >= vi.times) {
                info.free();
                return null;
            }
            info.floorsubmap[i2] = opb.read(8);
            if (info.floorsubmap[i2] >= vi.floors) {
                info.free();
                return null;
            }
            info.residuesubmap[i2] = opb.read(8);
            if (info.residuesubmap[i2] < vi.residues) continue;
            info.free();
            return null;
        }
        return info;
    }

    synchronized int inverse(Block vb, Object l2) {
        int j2;
        int i2;
        DspState vd = vb.vd;
        Info vi = vd.vi;
        LookMapping0 look = (LookMapping0)l2;
        InfoMapping0 info = look.map;
        InfoMode mode = look.mode;
        int n2 = vb.pcmend = vi.blocksizes[vb.W];
        float[] window = vd.window[vb.W][vb.lW][vb.nW][mode.windowtype];
        if (this.pcmbundle == null || this.pcmbundle.length < vi.channels) {
            this.pcmbundle = new float[vi.channels][];
            this.nonzero = new int[vi.channels];
            this.zerobundle = new int[vi.channels];
            this.floormemo = new Object[vi.channels];
        }
        for (i2 = 0; i2 < vi.channels; ++i2) {
            float[] pcm = vb.pcm[i2];
            int submap = info.chmuxlist[i2];
            this.floormemo[i2] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], this.floormemo[i2]);
            this.nonzero[i2] = this.floormemo[i2] != null ? 1 : 0;
            for (j2 = 0; j2 < n2 / 2; ++j2) {
                pcm[j2] = 0.0f;
            }
        }
        for (i2 = 0; i2 < info.coupling_steps; ++i2) {
            if (this.nonzero[info.coupling_mag[i2]] == 0 && this.nonzero[info.coupling_ang[i2]] == 0) continue;
            this.nonzero[info.coupling_mag[i2]] = 1;
            this.nonzero[info.coupling_ang[i2]] = 1;
        }
        for (i2 = 0; i2 < info.submaps; ++i2) {
            int ch_in_bundle = 0;
            for (int j3 = 0; j3 < vi.channels; ++j3) {
                if (info.chmuxlist[j3] != i2) continue;
                this.zerobundle[ch_in_bundle] = this.nonzero[j3] != 0 ? 1 : 0;
                this.pcmbundle[ch_in_bundle++] = vb.pcm[j3];
            }
            look.residue_func[i2].inverse(vb, look.residue_look[i2], this.pcmbundle, this.zerobundle, ch_in_bundle);
        }
        for (i2 = info.coupling_steps - 1; i2 >= 0; --i2) {
            float[] pcmM = vb.pcm[info.coupling_mag[i2]];
            float[] pcmA = vb.pcm[info.coupling_ang[i2]];
            for (j2 = 0; j2 < n2 / 2; ++j2) {
                float mag = pcmM[j2];
                float ang = pcmA[j2];
                if (mag > 0.0f) {
                    if (ang > 0.0f) {
                        pcmM[j2] = mag;
                        pcmA[j2] = mag - ang;
                        continue;
                    }
                    pcmA[j2] = mag;
                    pcmM[j2] = mag + ang;
                    continue;
                }
                if (ang > 0.0f) {
                    pcmM[j2] = mag;
                    pcmA[j2] = mag + ang;
                    continue;
                }
                pcmA[j2] = mag;
                pcmM[j2] = mag - ang;
            }
        }
        for (i2 = 0; i2 < vi.channels; ++i2) {
            float[] pcm = vb.pcm[i2];
            int submap = info.chmuxlist[i2];
            look.floor_func[submap].inverse2(vb, look.floor_look[submap], this.floormemo[i2], pcm);
        }
        for (i2 = 0; i2 < vi.channels; ++i2) {
            float[] pcm = vb.pcm[i2];
            ((Mdct)vd.transform[vb.W][0]).backward(pcm, pcm);
        }
        for (i2 = 0; i2 < vi.channels; ++i2) {
            int j4;
            float[] pcm = vb.pcm[i2];
            if (this.nonzero[i2] != 0) {
                for (j4 = 0; j4 < n2; ++j4) {
                    int n3 = j4;
                    pcm[n3] = pcm[n3] * window[j4];
                }
                continue;
            }
            for (j4 = 0; j4 < n2; ++j4) {
                pcm[j4] = 0.0f;
            }
        }
        return 0;
    }

    class LookMapping0 {
        InfoMode mode;
        InfoMapping0 map;
        Object[] time_look;
        Object[] floor_look;
        Object[] floor_state;
        Object[] residue_look;
        PsyLook[] psy_look;
        FuncTime[] time_func;
        FuncFloor[] floor_func;
        FuncResidue[] residue_func;
        int ch;
        float[][] decay;
        int lastframe;

        LookMapping0() {
        }
    }

    class InfoMapping0 {
        int submaps;
        int[] chmuxlist = new int[256];
        int[] timesubmap = new int[16];
        int[] floorsubmap = new int[16];
        int[] residuesubmap = new int[16];
        int[] psysubmap = new int[16];
        int coupling_steps;
        int[] coupling_mag = new int[256];
        int[] coupling_ang = new int[256];

        InfoMapping0() {
        }

        void free() {
            this.chmuxlist = null;
            this.timesubmap = null;
            this.floorsubmap = null;
            this.residuesubmap = null;
            this.psysubmap = null;
            this.coupling_mag = null;
            this.coupling_ang = null;
        }
    }
}

