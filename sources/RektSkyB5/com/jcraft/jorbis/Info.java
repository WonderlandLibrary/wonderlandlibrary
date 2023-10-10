/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.FuncFloor;
import com.jcraft.jorbis.FuncMapping;
import com.jcraft.jorbis.FuncResidue;
import com.jcraft.jorbis.FuncTime;
import com.jcraft.jorbis.InfoMode;
import com.jcraft.jorbis.PsyInfo;
import com.jcraft.jorbis.StaticCodeBook;
import com.jcraft.jorbis.Util;

public class Info {
    private static final int OV_EBADPACKET = -136;
    private static final int OV_ENOTAUDIO = -135;
    private static byte[] _vorbis = "vorbis".getBytes();
    private static final int VI_TIMEB = 1;
    private static final int VI_FLOORB = 2;
    private static final int VI_RESB = 3;
    private static final int VI_MAPB = 1;
    private static final int VI_WINDOWB = 1;
    public int version;
    public int channels;
    public int rate;
    int bitrate_upper;
    int bitrate_nominal;
    int bitrate_lower;
    int[] blocksizes = new int[2];
    int modes;
    int maps;
    int times;
    int floors;
    int residues;
    int books;
    int psys;
    InfoMode[] mode_param = null;
    int[] map_type = null;
    Object[] map_param = null;
    int[] time_type = null;
    Object[] time_param = null;
    int[] floor_type = null;
    Object[] floor_param = null;
    int[] residue_type = null;
    Object[] residue_param = null;
    StaticCodeBook[] book_param = null;
    PsyInfo[] psy_param = new PsyInfo[64];
    int envelopesa;
    float preecho_thresh;
    float preecho_clamp;

    public void init() {
        this.rate = 0;
    }

    public void clear() {
        int i2;
        for (i2 = 0; i2 < this.modes; ++i2) {
            this.mode_param[i2] = null;
        }
        this.mode_param = null;
        for (i2 = 0; i2 < this.maps; ++i2) {
            FuncMapping.mapping_P[this.map_type[i2]].free_info(this.map_param[i2]);
        }
        this.map_param = null;
        for (i2 = 0; i2 < this.times; ++i2) {
            FuncTime.time_P[this.time_type[i2]].free_info(this.time_param[i2]);
        }
        this.time_param = null;
        for (i2 = 0; i2 < this.floors; ++i2) {
            FuncFloor.floor_P[this.floor_type[i2]].free_info(this.floor_param[i2]);
        }
        this.floor_param = null;
        for (i2 = 0; i2 < this.residues; ++i2) {
            FuncResidue.residue_P[this.residue_type[i2]].free_info(this.residue_param[i2]);
        }
        this.residue_param = null;
        for (i2 = 0; i2 < this.books; ++i2) {
            if (this.book_param[i2] == null) continue;
            this.book_param[i2].clear();
            this.book_param[i2] = null;
        }
        this.book_param = null;
        for (i2 = 0; i2 < this.psys; ++i2) {
            this.psy_param[i2].free();
        }
    }

    int unpack_info(Buffer opb) {
        this.version = opb.read(32);
        if (this.version != 0) {
            return -1;
        }
        this.channels = opb.read(8);
        this.rate = opb.read(32);
        this.bitrate_upper = opb.read(32);
        this.bitrate_nominal = opb.read(32);
        this.bitrate_lower = opb.read(32);
        this.blocksizes[0] = 1 << opb.read(4);
        this.blocksizes[1] = 1 << opb.read(4);
        if (this.rate < 1 || this.channels < 1 || this.blocksizes[0] < 8 || this.blocksizes[1] < this.blocksizes[0] || opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }

    int unpack_books(Buffer opb) {
        int i2;
        this.books = opb.read(8) + 1;
        if (this.book_param == null || this.book_param.length != this.books) {
            this.book_param = new StaticCodeBook[this.books];
        }
        for (i2 = 0; i2 < this.books; ++i2) {
            this.book_param[i2] = new StaticCodeBook();
            if (this.book_param[i2].unpack(opb) == 0) continue;
            this.clear();
            return -1;
        }
        this.times = opb.read(6) + 1;
        if (this.time_type == null || this.time_type.length != this.times) {
            this.time_type = new int[this.times];
        }
        if (this.time_param == null || this.time_param.length != this.times) {
            this.time_param = new Object[this.times];
        }
        for (i2 = 0; i2 < this.times; ++i2) {
            this.time_type[i2] = opb.read(16);
            if (this.time_type[i2] < 0 || this.time_type[i2] >= 1) {
                this.clear();
                return -1;
            }
            this.time_param[i2] = FuncTime.time_P[this.time_type[i2]].unpack(this, opb);
            if (this.time_param[i2] != null) continue;
            this.clear();
            return -1;
        }
        this.floors = opb.read(6) + 1;
        if (this.floor_type == null || this.floor_type.length != this.floors) {
            this.floor_type = new int[this.floors];
        }
        if (this.floor_param == null || this.floor_param.length != this.floors) {
            this.floor_param = new Object[this.floors];
        }
        for (i2 = 0; i2 < this.floors; ++i2) {
            this.floor_type[i2] = opb.read(16);
            if (this.floor_type[i2] < 0 || this.floor_type[i2] >= 2) {
                this.clear();
                return -1;
            }
            this.floor_param[i2] = FuncFloor.floor_P[this.floor_type[i2]].unpack(this, opb);
            if (this.floor_param[i2] != null) continue;
            this.clear();
            return -1;
        }
        this.residues = opb.read(6) + 1;
        if (this.residue_type == null || this.residue_type.length != this.residues) {
            this.residue_type = new int[this.residues];
        }
        if (this.residue_param == null || this.residue_param.length != this.residues) {
            this.residue_param = new Object[this.residues];
        }
        for (i2 = 0; i2 < this.residues; ++i2) {
            this.residue_type[i2] = opb.read(16);
            if (this.residue_type[i2] < 0 || this.residue_type[i2] >= 3) {
                this.clear();
                return -1;
            }
            this.residue_param[i2] = FuncResidue.residue_P[this.residue_type[i2]].unpack(this, opb);
            if (this.residue_param[i2] != null) continue;
            this.clear();
            return -1;
        }
        this.maps = opb.read(6) + 1;
        if (this.map_type == null || this.map_type.length != this.maps) {
            this.map_type = new int[this.maps];
        }
        if (this.map_param == null || this.map_param.length != this.maps) {
            this.map_param = new Object[this.maps];
        }
        for (i2 = 0; i2 < this.maps; ++i2) {
            this.map_type[i2] = opb.read(16);
            if (this.map_type[i2] < 0 || this.map_type[i2] >= 1) {
                this.clear();
                return -1;
            }
            this.map_param[i2] = FuncMapping.mapping_P[this.map_type[i2]].unpack(this, opb);
            if (this.map_param[i2] != null) continue;
            this.clear();
            return -1;
        }
        this.modes = opb.read(6) + 1;
        if (this.mode_param == null || this.mode_param.length != this.modes) {
            this.mode_param = new InfoMode[this.modes];
        }
        for (i2 = 0; i2 < this.modes; ++i2) {
            this.mode_param[i2] = new InfoMode();
            this.mode_param[i2].blockflag = opb.read(1);
            this.mode_param[i2].windowtype = opb.read(16);
            this.mode_param[i2].transformtype = opb.read(16);
            this.mode_param[i2].mapping = opb.read(8);
            if (this.mode_param[i2].windowtype < 1 && this.mode_param[i2].transformtype < 1 && this.mode_param[i2].mapping < this.maps) continue;
            this.clear();
            return -1;
        }
        if (opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }

    public int synthesis_headerin(Comment vc, Packet op) {
        Buffer opb = new Buffer();
        if (op != null) {
            opb.readinit(op.packet_base, op.packet, op.bytes);
            byte[] buffer = new byte[6];
            int packtype = opb.read(8);
            opb.read(buffer, 6);
            if (buffer[0] != 118 || buffer[1] != 111 || buffer[2] != 114 || buffer[3] != 98 || buffer[4] != 105 || buffer[5] != 115) {
                return -1;
            }
            switch (packtype) {
                case 1: {
                    if (op.b_o_s == 0) {
                        return -1;
                    }
                    if (this.rate != 0) {
                        return -1;
                    }
                    return this.unpack_info(opb);
                }
                case 3: {
                    if (this.rate == 0) {
                        return -1;
                    }
                    return vc.unpack(opb);
                }
                case 5: {
                    if (this.rate == 0 || vc.vendor == null) {
                        return -1;
                    }
                    return this.unpack_books(opb);
                }
            }
        }
        return -1;
    }

    int pack_info(Buffer opb) {
        opb.write(1, 8);
        opb.write(_vorbis);
        opb.write(0, 32);
        opb.write(this.channels, 8);
        opb.write(this.rate, 32);
        opb.write(this.bitrate_upper, 32);
        opb.write(this.bitrate_nominal, 32);
        opb.write(this.bitrate_lower, 32);
        opb.write(Util.ilog2(this.blocksizes[0]), 4);
        opb.write(Util.ilog2(this.blocksizes[1]), 4);
        opb.write(1, 1);
        return 0;
    }

    int pack_books(Buffer opb) {
        int i2;
        opb.write(5, 8);
        opb.write(_vorbis);
        opb.write(this.books - 1, 8);
        for (i2 = 0; i2 < this.books; ++i2) {
            if (this.book_param[i2].pack(opb) == 0) continue;
            return -1;
        }
        opb.write(this.times - 1, 6);
        for (i2 = 0; i2 < this.times; ++i2) {
            opb.write(this.time_type[i2], 16);
            FuncTime.time_P[this.time_type[i2]].pack(this.time_param[i2], opb);
        }
        opb.write(this.floors - 1, 6);
        for (i2 = 0; i2 < this.floors; ++i2) {
            opb.write(this.floor_type[i2], 16);
            FuncFloor.floor_P[this.floor_type[i2]].pack(this.floor_param[i2], opb);
        }
        opb.write(this.residues - 1, 6);
        for (i2 = 0; i2 < this.residues; ++i2) {
            opb.write(this.residue_type[i2], 16);
            FuncResidue.residue_P[this.residue_type[i2]].pack(this.residue_param[i2], opb);
        }
        opb.write(this.maps - 1, 6);
        for (i2 = 0; i2 < this.maps; ++i2) {
            opb.write(this.map_type[i2], 16);
            FuncMapping.mapping_P[this.map_type[i2]].pack(this, this.map_param[i2], opb);
        }
        opb.write(this.modes - 1, 6);
        for (i2 = 0; i2 < this.modes; ++i2) {
            opb.write(this.mode_param[i2].blockflag, 1);
            opb.write(this.mode_param[i2].windowtype, 16);
            opb.write(this.mode_param[i2].transformtype, 16);
            opb.write(this.mode_param[i2].mapping, 8);
        }
        opb.write(1, 1);
        return 0;
    }

    public int blocksize(Packet op) {
        Buffer opb = new Buffer();
        opb.readinit(op.packet_base, op.packet, op.bytes);
        if (opb.read(1) != 0) {
            return -135;
        }
        int modebits = 0;
        for (int v2 = this.modes; v2 > 1; v2 >>>= 1) {
            ++modebits;
        }
        int mode = opb.read(modebits);
        if (mode == -1) {
            return -136;
        }
        return this.blocksizes[this.mode_param[mode].blockflag];
    }

    public String toString() {
        return "version:" + new Integer(this.version) + ", channels:" + new Integer(this.channels) + ", rate:" + new Integer(this.rate) + ", bitrate:" + new Integer(this.bitrate_upper) + "," + new Integer(this.bitrate_nominal) + "," + new Integer(this.bitrate_lower);
    }
}

