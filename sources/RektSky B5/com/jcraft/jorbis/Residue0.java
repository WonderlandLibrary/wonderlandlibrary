/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.CodeBook;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.FuncResidue;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.InfoMode;
import com.jcraft.jorbis.Util;

class Residue0
extends FuncResidue {
    private static int[][][] _01inverse_partword = new int[2][][];
    static int[][] _2inverse_partword = null;

    Residue0() {
    }

    void pack(Object vr, Buffer opb) {
        int j2;
        InfoResidue0 info = (InfoResidue0)vr;
        int acc = 0;
        opb.write(info.begin, 24);
        opb.write(info.end, 24);
        opb.write(info.grouping - 1, 24);
        opb.write(info.partitions - 1, 6);
        opb.write(info.groupbook, 8);
        for (j2 = 0; j2 < info.partitions; ++j2) {
            int i2 = info.secondstages[j2];
            if (Util.ilog(i2) > 3) {
                opb.write(i2, 3);
                opb.write(1, 1);
                opb.write(i2 >>> 3, 5);
            } else {
                opb.write(i2, 4);
            }
            acc += Util.icount(i2);
        }
        for (j2 = 0; j2 < acc; ++j2) {
            opb.write(info.booklist[j2], 8);
        }
    }

    Object unpack(Info vi, Buffer opb) {
        int j2;
        int acc = 0;
        InfoResidue0 info = new InfoResidue0();
        info.begin = opb.read(24);
        info.end = opb.read(24);
        info.grouping = opb.read(24) + 1;
        info.partitions = opb.read(6) + 1;
        info.groupbook = opb.read(8);
        for (j2 = 0; j2 < info.partitions; ++j2) {
            int cascade = opb.read(3);
            if (opb.read(1) != 0) {
                cascade |= opb.read(5) << 3;
            }
            info.secondstages[j2] = cascade;
            acc += Util.icount(cascade);
        }
        for (j2 = 0; j2 < acc; ++j2) {
            info.booklist[j2] = opb.read(8);
        }
        if (info.groupbook >= vi.books) {
            this.free_info(info);
            return null;
        }
        for (j2 = 0; j2 < acc; ++j2) {
            if (info.booklist[j2] < vi.books) continue;
            this.free_info(info);
            return null;
        }
        return info;
    }

    Object look(DspState vd, InfoMode vm, Object vr) {
        int k2;
        int j2;
        InfoResidue0 info = (InfoResidue0)vr;
        LookResidue0 look = new LookResidue0();
        int acc = 0;
        int maxstage = 0;
        look.info = info;
        look.map = vm.mapping;
        look.parts = info.partitions;
        look.fullbooks = vd.fullbooks;
        look.phrasebook = vd.fullbooks[info.groupbook];
        int dim = look.phrasebook.dim;
        look.partbooks = new int[look.parts][];
        for (j2 = 0; j2 < look.parts; ++j2) {
            int i2 = info.secondstages[j2];
            int stages = Util.ilog(i2);
            if (stages == 0) continue;
            if (stages > maxstage) {
                maxstage = stages;
            }
            look.partbooks[j2] = new int[stages];
            for (k2 = 0; k2 < stages; ++k2) {
                if ((i2 & 1 << k2) == 0) continue;
                look.partbooks[j2][k2] = info.booklist[acc++];
            }
        }
        look.partvals = (int)Math.rint(Math.pow(look.parts, dim));
        look.stages = maxstage;
        look.decodemap = new int[look.partvals][];
        for (j2 = 0; j2 < look.partvals; ++j2) {
            int val2 = j2;
            int mult = look.partvals / look.parts;
            look.decodemap[j2] = new int[dim];
            for (k2 = 0; k2 < dim; ++k2) {
                int deco = val2 / mult;
                val2 -= deco * mult;
                mult /= look.parts;
                look.decodemap[j2][k2] = deco;
            }
        }
        return look;
    }

    void free_info(Object i2) {
    }

    void free_look(Object i2) {
    }

    static synchronized int _01inverse(Block vb, Object vl, float[][] in, int ch, int decodepart) {
        int j2;
        LookResidue0 look = (LookResidue0)vl;
        InfoResidue0 info = look.info;
        int samples_per_partition = info.grouping;
        int partitions_per_word = look.phrasebook.dim;
        int n2 = info.end - info.begin;
        int partvals = n2 / samples_per_partition;
        int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (_01inverse_partword.length < ch) {
            _01inverse_partword = new int[ch][][];
        }
        for (j2 = 0; j2 < ch; ++j2) {
            if (_01inverse_partword[j2] != null && _01inverse_partword[j2].length >= partwords) continue;
            Residue0._01inverse_partword[j2] = new int[partwords][];
        }
        for (int s2 = 0; s2 < look.stages; ++s2) {
            int i2 = 0;
            int l2 = 0;
            while (i2 < partvals) {
                if (s2 == 0) {
                    for (j2 = 0; j2 < ch; ++j2) {
                        int temp = look.phrasebook.decode(vb.opb);
                        if (temp == -1) {
                            return 0;
                        }
                        Residue0._01inverse_partword[j2][l2] = look.decodemap[temp];
                        if (_01inverse_partword[j2][l2] != null) continue;
                        return 0;
                    }
                }
                for (int k2 = 0; k2 < partitions_per_word && i2 < partvals; ++k2, ++i2) {
                    for (j2 = 0; j2 < ch; ++j2) {
                        CodeBook stagebook;
                        int offset = info.begin + i2 * samples_per_partition;
                        int index = _01inverse_partword[j2][l2][k2];
                        if ((info.secondstages[index] & 1 << s2) == 0 || (stagebook = look.fullbooks[look.partbooks[index][s2]]) == null || !(decodepart == 0 ? stagebook.decodevs_add(in[j2], offset, vb.opb, samples_per_partition) == -1 : decodepart == 1 && stagebook.decodev_add(in[j2], offset, vb.opb, samples_per_partition) == -1)) continue;
                        return 0;
                    }
                }
                ++l2;
            }
        }
        return 0;
    }

    static synchronized int _2inverse(Block vb, Object vl, float[][] in, int ch) {
        LookResidue0 look = (LookResidue0)vl;
        InfoResidue0 info = look.info;
        int samples_per_partition = info.grouping;
        int partitions_per_word = look.phrasebook.dim;
        int n2 = info.end - info.begin;
        int partvals = n2 / samples_per_partition;
        int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (_2inverse_partword == null || _2inverse_partword.length < partwords) {
            _2inverse_partword = new int[partwords][];
        }
        for (int s2 = 0; s2 < look.stages; ++s2) {
            int i2 = 0;
            int l2 = 0;
            while (i2 < partvals) {
                if (s2 == 0) {
                    int temp = look.phrasebook.decode(vb.opb);
                    if (temp == -1) {
                        return 0;
                    }
                    Residue0._2inverse_partword[l2] = look.decodemap[temp];
                    if (_2inverse_partword[l2] == null) {
                        return 0;
                    }
                }
                for (int k2 = 0; k2 < partitions_per_word && i2 < partvals; ++k2, ++i2) {
                    CodeBook stagebook;
                    int offset = info.begin + i2 * samples_per_partition;
                    int index = _2inverse_partword[l2][k2];
                    if ((info.secondstages[index] & 1 << s2) == 0 || (stagebook = look.fullbooks[look.partbooks[index][s2]]) == null || stagebook.decodevv_add(in, offset, ch, vb.opb, samples_per_partition) != -1) continue;
                    return 0;
                }
                ++l2;
            }
        }
        return 0;
    }

    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int used = 0;
        for (int i2 = 0; i2 < ch; ++i2) {
            if (nonzero[i2] == 0) continue;
            in[used++] = in[i2];
        }
        if (used != 0) {
            return Residue0._01inverse(vb, vl, in, used, 0);
        }
        return 0;
    }

    class InfoResidue0 {
        int begin;
        int end;
        int grouping;
        int partitions;
        int groupbook;
        int[] secondstages = new int[64];
        int[] booklist = new int[256];
        float[] entmax = new float[64];
        float[] ampmax = new float[64];
        int[] subgrp = new int[64];
        int[] blimit = new int[64];

        InfoResidue0() {
        }
    }

    class LookResidue0 {
        InfoResidue0 info;
        int map;
        int parts;
        int stages;
        CodeBook[] fullbooks;
        CodeBook phrasebook;
        int[][] partbooks;
        int partvals;
        int[][] decodemap;
        int postbits;
        int phrasebits;
        int frames;

        LookResidue0() {
        }
    }
}

