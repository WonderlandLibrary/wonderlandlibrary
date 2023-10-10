// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Buffer;

public class Info
{
    private static final int OV_EBADPACKET = -136;
    private static final int OV_ENOTAUDIO = -135;
    private static byte[] _vorbis;
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
    int[] blocksizes;
    int modes;
    int maps;
    int times;
    int floors;
    int residues;
    int books;
    int psys;
    InfoMode[] mode_param;
    int[] map_type;
    Object[] map_param;
    int[] time_type;
    Object[] time_param;
    int[] floor_type;
    Object[] floor_param;
    int[] residue_type;
    Object[] residue_param;
    StaticCodeBook[] book_param;
    PsyInfo[] psy_param;
    int envelopesa;
    float preecho_thresh;
    float preecho_clamp;
    
    public Info() {
        this.blocksizes = new int[2];
        this.mode_param = null;
        this.map_type = null;
        this.map_param = null;
        this.time_type = null;
        this.time_param = null;
        this.floor_type = null;
        this.floor_param = null;
        this.residue_type = null;
        this.residue_param = null;
        this.book_param = null;
        this.psy_param = new PsyInfo[64];
    }
    
    public void init() {
        this.rate = 0;
    }
    
    public void clear() {
        for (int i = 0; i < this.modes; ++i) {
            this.mode_param[i] = null;
        }
        this.mode_param = null;
        for (int j = 0; j < this.maps; ++j) {
            FuncMapping.mapping_P[this.map_type[j]].free_info(this.map_param[j]);
        }
        this.map_param = null;
        for (int k = 0; k < this.times; ++k) {
            FuncTime.time_P[this.time_type[k]].free_info(this.time_param[k]);
        }
        this.time_param = null;
        for (int l = 0; l < this.floors; ++l) {
            FuncFloor.floor_P[this.floor_type[l]].free_info(this.floor_param[l]);
        }
        this.floor_param = null;
        for (int n = 0; n < this.residues; ++n) {
            FuncResidue.residue_P[this.residue_type[n]].free_info(this.residue_param[n]);
        }
        this.residue_param = null;
        for (int n2 = 0; n2 < this.books; ++n2) {
            if (this.book_param[n2] != null) {
                this.book_param[n2].clear();
                this.book_param[n2] = null;
            }
        }
        this.book_param = null;
        for (int n3 = 0; n3 < this.psys; ++n3) {
            this.psy_param[n3].free();
        }
    }
    
    int unpack_info(final Buffer buffer) {
        this.version = buffer.read(32);
        if (this.version != 0) {
            return -1;
        }
        this.channels = buffer.read(8);
        this.rate = buffer.read(32);
        this.bitrate_upper = buffer.read(32);
        this.bitrate_nominal = buffer.read(32);
        this.bitrate_lower = buffer.read(32);
        this.blocksizes[0] = 1 << buffer.read(4);
        this.blocksizes[1] = 1 << buffer.read(4);
        if (this.rate < 1 || this.channels < 1 || this.blocksizes[0] < 8 || this.blocksizes[1] < this.blocksizes[0] || buffer.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    int unpack_books(final Buffer buffer) {
        this.books = buffer.read(8) + 1;
        if (this.book_param == null || this.book_param.length != this.books) {
            this.book_param = new StaticCodeBook[this.books];
        }
        for (int i = 0; i < this.books; ++i) {
            this.book_param[i] = new StaticCodeBook();
            if (this.book_param[i].unpack(buffer) != 0) {
                this.clear();
                return -1;
            }
        }
        this.times = buffer.read(6) + 1;
        if (this.time_type == null || this.time_type.length != this.times) {
            this.time_type = new int[this.times];
        }
        if (this.time_param == null || this.time_param.length != this.times) {
            this.time_param = new Object[this.times];
        }
        for (int j = 0; j < this.times; ++j) {
            this.time_type[j] = buffer.read(16);
            if (this.time_type[j] < 0 || this.time_type[j] >= 1) {
                this.clear();
                return -1;
            }
            this.time_param[j] = FuncTime.time_P[this.time_type[j]].unpack(this, buffer);
            if (this.time_param[j] == null) {
                this.clear();
                return -1;
            }
        }
        this.floors = buffer.read(6) + 1;
        if (this.floor_type == null || this.floor_type.length != this.floors) {
            this.floor_type = new int[this.floors];
        }
        if (this.floor_param == null || this.floor_param.length != this.floors) {
            this.floor_param = new Object[this.floors];
        }
        for (int k = 0; k < this.floors; ++k) {
            this.floor_type[k] = buffer.read(16);
            if (this.floor_type[k] < 0 || this.floor_type[k] >= 2) {
                this.clear();
                return -1;
            }
            this.floor_param[k] = FuncFloor.floor_P[this.floor_type[k]].unpack(this, buffer);
            if (this.floor_param[k] == null) {
                this.clear();
                return -1;
            }
        }
        this.residues = buffer.read(6) + 1;
        if (this.residue_type == null || this.residue_type.length != this.residues) {
            this.residue_type = new int[this.residues];
        }
        if (this.residue_param == null || this.residue_param.length != this.residues) {
            this.residue_param = new Object[this.residues];
        }
        for (int l = 0; l < this.residues; ++l) {
            this.residue_type[l] = buffer.read(16);
            if (this.residue_type[l] < 0 || this.residue_type[l] >= 3) {
                this.clear();
                return -1;
            }
            this.residue_param[l] = FuncResidue.residue_P[this.residue_type[l]].unpack(this, buffer);
            if (this.residue_param[l] == null) {
                this.clear();
                return -1;
            }
        }
        this.maps = buffer.read(6) + 1;
        if (this.map_type == null || this.map_type.length != this.maps) {
            this.map_type = new int[this.maps];
        }
        if (this.map_param == null || this.map_param.length != this.maps) {
            this.map_param = new Object[this.maps];
        }
        for (int n = 0; n < this.maps; ++n) {
            this.map_type[n] = buffer.read(16);
            if (this.map_type[n] < 0 || this.map_type[n] >= 1) {
                this.clear();
                return -1;
            }
            this.map_param[n] = FuncMapping.mapping_P[this.map_type[n]].unpack(this, buffer);
            if (this.map_param[n] == null) {
                this.clear();
                return -1;
            }
        }
        this.modes = buffer.read(6) + 1;
        if (this.mode_param == null || this.mode_param.length != this.modes) {
            this.mode_param = new InfoMode[this.modes];
        }
        for (int n2 = 0; n2 < this.modes; ++n2) {
            this.mode_param[n2] = new InfoMode();
            this.mode_param[n2].blockflag = buffer.read(1);
            this.mode_param[n2].windowtype = buffer.read(16);
            this.mode_param[n2].transformtype = buffer.read(16);
            this.mode_param[n2].mapping = buffer.read(8);
            if (this.mode_param[n2].windowtype >= 1 || this.mode_param[n2].transformtype >= 1 || this.mode_param[n2].mapping >= this.maps) {
                this.clear();
                return -1;
            }
        }
        if (buffer.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    public int synthesis_headerin(final Comment comment, final Packet packet) {
        final Buffer buffer = new Buffer();
        if (packet != null) {
            buffer.readinit(packet.packet_base, packet.packet, packet.bytes);
            final byte[] array = new byte[6];
            final int read = buffer.read(8);
            buffer.read(array, 6);
            if (array[0] != 118 || array[1] != 111 || array[2] != 114 || array[3] != 98 || array[4] != 105 || array[5] != 115) {
                return -1;
            }
            switch (read) {
                case 1: {
                    if (packet.b_o_s == 0) {
                        return -1;
                    }
                    if (this.rate != 0) {
                        return -1;
                    }
                    return this.unpack_info(buffer);
                }
                case 3: {
                    if (this.rate == 0) {
                        return -1;
                    }
                    return comment.unpack(buffer);
                }
                case 5: {
                    if (this.rate == 0 || comment.vendor == null) {
                        return -1;
                    }
                    return this.unpack_books(buffer);
                }
            }
        }
        return -1;
    }
    
    int pack_info(final Buffer buffer) {
        buffer.write(1, 8);
        buffer.write(Info._vorbis);
        buffer.write(0, 32);
        buffer.write(this.channels, 8);
        buffer.write(this.rate, 32);
        buffer.write(this.bitrate_upper, 32);
        buffer.write(this.bitrate_nominal, 32);
        buffer.write(this.bitrate_lower, 32);
        buffer.write(ilog2(this.blocksizes[0]), 4);
        buffer.write(ilog2(this.blocksizes[1]), 4);
        buffer.write(1, 1);
        return 0;
    }
    
    int pack_books(final Buffer buffer) {
        buffer.write(5, 8);
        buffer.write(Info._vorbis);
        buffer.write(this.books - 1, 8);
        for (int i = 0; i < this.books; ++i) {
            if (this.book_param[i].pack(buffer) != 0) {
                return -1;
            }
        }
        buffer.write(this.times - 1, 6);
        for (int j = 0; j < this.times; ++j) {
            buffer.write(this.time_type[j], 16);
            FuncTime.time_P[this.time_type[j]].pack(this.time_param[j], buffer);
        }
        buffer.write(this.floors - 1, 6);
        for (int k = 0; k < this.floors; ++k) {
            buffer.write(this.floor_type[k], 16);
            FuncFloor.floor_P[this.floor_type[k]].pack(this.floor_param[k], buffer);
        }
        buffer.write(this.residues - 1, 6);
        for (int l = 0; l < this.residues; ++l) {
            buffer.write(this.residue_type[l], 16);
            FuncResidue.residue_P[this.residue_type[l]].pack(this.residue_param[l], buffer);
        }
        buffer.write(this.maps - 1, 6);
        for (int n = 0; n < this.maps; ++n) {
            buffer.write(this.map_type[n], 16);
            FuncMapping.mapping_P[this.map_type[n]].pack(this, this.map_param[n], buffer);
        }
        buffer.write(this.modes - 1, 6);
        for (int n2 = 0; n2 < this.modes; ++n2) {
            buffer.write(this.mode_param[n2].blockflag, 1);
            buffer.write(this.mode_param[n2].windowtype, 16);
            buffer.write(this.mode_param[n2].transformtype, 16);
            buffer.write(this.mode_param[n2].mapping, 8);
        }
        buffer.write(1, 1);
        return 0;
    }
    
    public int blocksize(final Packet packet) {
        final Buffer buffer = new Buffer();
        buffer.readinit(packet.packet_base, packet.packet, packet.bytes);
        if (buffer.read(1) != 0) {
            return -135;
        }
        int n = 0;
        for (int i = this.modes; i > 1; i >>>= 1) {
            ++n;
        }
        final int read = buffer.read(n);
        if (read == -1) {
            return -136;
        }
        return this.blocksizes[this.mode_param[read].blockflag];
    }
    
    private static int ilog2(int i) {
        int n = 0;
        while (i > 1) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    public String toString() {
        return "version:" + new Integer(this.version) + ", channels:" + new Integer(this.channels) + ", rate:" + new Integer(this.rate) + ", bitrate:" + new Integer(this.bitrate_upper) + "," + new Integer(this.bitrate_nominal) + "," + new Integer(this.bitrate_lower);
    }
    
    static {
        Info._vorbis = "vorbis".getBytes();
    }
}
