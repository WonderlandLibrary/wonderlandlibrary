// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class StreamState
{
    byte[] body_data;
    int body_storage;
    int body_fill;
    private int body_returned;
    int[] lacing_vals;
    long[] granule_vals;
    int lacing_storage;
    int lacing_fill;
    int lacing_packet;
    int lacing_returned;
    byte[] header;
    int header_fill;
    public int e_o_s;
    int b_o_s;
    int serialno;
    int pageno;
    long packetno;
    long granulepos;
    
    public StreamState() {
        this.header = new byte[282];
        this.init();
    }
    
    StreamState(final int n) {
        this();
        this.init(n);
    }
    
    void init() {
        this.body_storage = 16384;
        this.body_data = new byte[this.body_storage];
        this.lacing_storage = 1024;
        this.lacing_vals = new int[this.lacing_storage];
        this.granule_vals = new long[this.lacing_storage];
    }
    
    public void init(final int serialno) {
        if (this.body_data == null) {
            this.init();
        }
        else {
            for (int i = 0; i < this.body_data.length; ++i) {
                this.body_data[i] = 0;
            }
            for (int j = 0; j < this.lacing_vals.length; ++j) {
                this.lacing_vals[j] = 0;
            }
            for (int k = 0; k < this.granule_vals.length; ++k) {
                this.granule_vals[k] = 0L;
            }
        }
        this.serialno = serialno;
    }
    
    public void clear() {
        this.body_data = null;
        this.lacing_vals = null;
        this.granule_vals = null;
    }
    
    void destroy() {
        this.clear();
    }
    
    void body_expand(final int n) {
        if (this.body_storage <= this.body_fill + n) {
            this.body_storage += n + 1024;
            final byte[] body_data = new byte[this.body_storage];
            System.arraycopy(this.body_data, 0, body_data, 0, this.body_data.length);
            this.body_data = body_data;
        }
    }
    
    void lacing_expand(final int n) {
        if (this.lacing_storage <= this.lacing_fill + n) {
            this.lacing_storage += n + 32;
            final int[] lacing_vals = new int[this.lacing_storage];
            System.arraycopy(this.lacing_vals, 0, lacing_vals, 0, this.lacing_vals.length);
            this.lacing_vals = lacing_vals;
            final long[] granule_vals = new long[this.lacing_storage];
            System.arraycopy(this.granule_vals, 0, granule_vals, 0, this.granule_vals.length);
            this.granule_vals = granule_vals;
        }
    }
    
    public int packetin(final Packet packet) {
        final int n = packet.bytes / 255 + 1;
        if (this.body_returned != 0) {
            this.body_fill -= this.body_returned;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
            }
            this.body_returned = 0;
        }
        this.body_expand(packet.bytes);
        this.lacing_expand(n);
        System.arraycopy(packet.packet_base, packet.packet, this.body_data, this.body_fill, packet.bytes);
        this.body_fill += packet.bytes;
        int i;
        for (i = 0; i < n - 1; ++i) {
            this.lacing_vals[this.lacing_fill + i] = 255;
            this.granule_vals[this.lacing_fill + i] = this.granulepos;
        }
        this.lacing_vals[this.lacing_fill + i] = packet.bytes % 255;
        final long[] granule_vals = this.granule_vals;
        final int n2 = this.lacing_fill + i;
        final long granulepos = packet.granulepos;
        granule_vals[n2] = granulepos;
        this.granulepos = granulepos;
        final int[] lacing_vals = this.lacing_vals;
        final int lacing_fill = this.lacing_fill;
        lacing_vals[lacing_fill] |= 0x100;
        this.lacing_fill += n;
        ++this.packetno;
        if (packet.e_o_s != 0) {
            this.e_o_s = 1;
        }
        return 0;
    }
    
    public int packetout(final Packet packet) {
        int lacing_returned = this.lacing_returned;
        if (this.lacing_packet <= lacing_returned) {
            return 0;
        }
        if ((this.lacing_vals[lacing_returned] & 0x400) != 0x0) {
            ++this.lacing_returned;
            ++this.packetno;
            return -1;
        }
        int i = this.lacing_vals[lacing_returned] & 0xFF;
        final int n = 0;
        packet.packet_base = this.body_data;
        packet.packet = this.body_returned;
        packet.e_o_s = (this.lacing_vals[lacing_returned] & 0x200);
        packet.b_o_s = (this.lacing_vals[lacing_returned] & 0x100);
        int bytes = n + i;
        while (i == 255) {
            final int n2 = this.lacing_vals[++lacing_returned];
            i = (n2 & 0xFF);
            if ((n2 & 0x200) != 0x0) {
                packet.e_o_s = 512;
            }
            bytes += i;
        }
        packet.packetno = this.packetno;
        packet.granulepos = this.granule_vals[lacing_returned];
        packet.bytes = bytes;
        this.body_returned += bytes;
        this.lacing_returned = lacing_returned + 1;
        ++this.packetno;
        return 1;
    }
    
    public int pagein(final Page page) {
        final byte[] header_base = page.header_base;
        final int header = page.header;
        final byte[] body_base = page.body_base;
        int body = page.body;
        int body_len = page.body_len;
        int i = 0;
        final int version = page.version();
        final int continued = page.continued();
        int bos = page.bos();
        final int eos = page.eos();
        final long granulepos = page.granulepos();
        final int serialno = page.serialno();
        final int pageno = page.pageno();
        final int n = header_base[header + 26] & 0xFF;
        final int lacing_returned = this.lacing_returned;
        final int body_returned = this.body_returned;
        if (body_returned != 0) {
            this.body_fill -= body_returned;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, body_returned, this.body_data, 0, this.body_fill);
            }
            this.body_returned = 0;
        }
        if (lacing_returned != 0) {
            if (this.lacing_fill - lacing_returned != 0) {
                System.arraycopy(this.lacing_vals, lacing_returned, this.lacing_vals, 0, this.lacing_fill - lacing_returned);
                System.arraycopy(this.granule_vals, lacing_returned, this.granule_vals, 0, this.lacing_fill - lacing_returned);
            }
            this.lacing_fill -= lacing_returned;
            this.lacing_packet -= lacing_returned;
            this.lacing_returned = 0;
        }
        if (serialno != this.serialno) {
            return -1;
        }
        if (version > 0) {
            return -1;
        }
        this.lacing_expand(n + 1);
        if (pageno != this.pageno) {
            for (int j = this.lacing_packet; j < this.lacing_fill; ++j) {
                this.body_fill -= (this.lacing_vals[j] & 0xFF);
            }
            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
                this.lacing_vals[this.lacing_fill++] = 1024;
                ++this.lacing_packet;
            }
            if (continued != 0) {
                bos = 0;
                while (i < n) {
                    final int n2 = header_base[header + 27 + i] & 0xFF;
                    body += n2;
                    body_len -= n2;
                    if (n2 < 255) {
                        ++i;
                        break;
                    }
                    ++i;
                }
            }
        }
        if (body_len != 0) {
            this.body_expand(body_len);
            System.arraycopy(body_base, body, this.body_data, this.body_fill, body_len);
            this.body_fill += body_len;
        }
        int lacing_fill = -1;
        while (i < n) {
            final int n3 = header_base[header + 27 + i] & 0xFF;
            this.lacing_vals[this.lacing_fill] = n3;
            this.granule_vals[this.lacing_fill] = -1L;
            if (bos != 0) {
                final int[] lacing_vals = this.lacing_vals;
                final int lacing_fill2 = this.lacing_fill;
                lacing_vals[lacing_fill2] |= 0x100;
                bos = 0;
            }
            if (n3 < 255) {
                lacing_fill = this.lacing_fill;
            }
            ++this.lacing_fill;
            ++i;
            if (n3 < 255) {
                this.lacing_packet = this.lacing_fill;
            }
        }
        if (lacing_fill != -1) {
            this.granule_vals[lacing_fill] = granulepos;
        }
        if (eos != 0) {
            this.e_o_s = 1;
            if (this.lacing_fill > 0) {
                final int[] lacing_vals2 = this.lacing_vals;
                final int n4 = this.lacing_fill - 1;
                lacing_vals2[n4] |= 0x200;
            }
        }
        this.pageno = pageno + 1;
        return 0;
    }
    
    public int flush(final Page page) {
        final int n = (this.lacing_fill > 255) ? 255 : this.lacing_fill;
        int body_len = 0;
        int n2 = 0;
        long n3 = this.granule_vals[0];
        if (n == 0) {
            return 0;
        }
        int i;
        if (this.b_o_s == 0) {
            n3 = 0L;
            for (i = 0; i < n; ++i) {
                if ((this.lacing_vals[i] & 0xFF) < 255) {
                    ++i;
                    break;
                }
            }
        }
        else {
            for (i = 0; i < n; ++i) {
                if (n2 > 4096) {
                    break;
                }
                n2 += (this.lacing_vals[i] & 0xFF);
                n3 = this.granule_vals[i];
            }
        }
        System.arraycopy("OggS".getBytes(), 0, this.header, 0, 4);
        this.header[4] = 0;
        this.header[5] = 0;
        if ((this.lacing_vals[0] & 0x100) == 0x0) {
            final byte[] header = this.header;
            final int n4 = 5;
            header[n4] |= 0x1;
        }
        if (this.b_o_s == 0) {
            final byte[] header2 = this.header;
            final int n5 = 5;
            header2[n5] |= 0x2;
        }
        if (this.e_o_s != 0 && this.lacing_fill == i) {
            final byte[] header3 = this.header;
            final int n6 = 5;
            header3[n6] |= 0x4;
        }
        this.b_o_s = 1;
        for (int j = 6; j < 14; ++j) {
            this.header[j] = (byte)n3;
            n3 >>>= 8;
        }
        int serialno = this.serialno;
        for (int k = 14; k < 18; ++k) {
            this.header[k] = (byte)serialno;
            serialno >>>= 8;
        }
        if (this.pageno == -1) {
            this.pageno = 0;
        }
        int n7 = this.pageno++;
        for (int l = 18; l < 22; ++l) {
            this.header[l] = (byte)n7;
            n7 >>>= 8;
        }
        this.header[22] = 0;
        this.header[23] = 0;
        this.header[24] = 0;
        this.header[25] = 0;
        this.header[26] = (byte)i;
        for (int n8 = 0; n8 < i; ++n8) {
            this.header[n8 + 27] = (byte)this.lacing_vals[n8];
            body_len += (this.header[n8 + 27] & 0xFF);
        }
        page.header_base = this.header;
        page.header = 0;
        final int n9 = i + 27;
        this.header_fill = n9;
        page.header_len = n9;
        page.body_base = this.body_data;
        page.body = this.body_returned;
        page.body_len = body_len;
        this.lacing_fill -= i;
        System.arraycopy(this.lacing_vals, i, this.lacing_vals, 0, this.lacing_fill * 4);
        System.arraycopy(this.granule_vals, i, this.granule_vals, 0, this.lacing_fill * 8);
        this.body_returned += body_len;
        page.checksum();
        return 1;
    }
    
    public int pageout(final Page page) {
        if ((this.e_o_s != 0 && this.lacing_fill != 0) || this.body_fill - this.body_returned > 4096 || this.lacing_fill >= 255 || (this.lacing_fill != 0 && this.b_o_s == 0)) {
            return this.flush(page);
        }
        return 0;
    }
    
    public int eof() {
        return this.e_o_s;
    }
    
    public int reset() {
        this.body_fill = 0;
        this.body_returned = 0;
        this.lacing_fill = 0;
        this.lacing_packet = 0;
        this.lacing_returned = 0;
        this.header_fill = 0;
        this.e_o_s = 0;
        this.b_o_s = 0;
        this.pageno = -1;
        this.packetno = 0L;
        this.granulepos = 0L;
        return 0;
    }
}
