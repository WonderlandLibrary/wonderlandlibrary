// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class Page
{
    private static int[] crc_lookup;
    public byte[] header_base;
    public int header;
    public int header_len;
    public byte[] body_base;
    public int body;
    public int body_len;
    
    private static int crc_entry(final int n) {
        int n2 = n << 24;
        for (int i = 0; i < 8; ++i) {
            if ((n2 & Integer.MIN_VALUE) != 0x0) {
                n2 = (n2 << 1 ^ 0x4C11DB7);
            }
            else {
                n2 <<= 1;
            }
        }
        return n2 & -1;
    }
    
    int version() {
        return this.header_base[this.header + 4] & 0xFF;
    }
    
    int continued() {
        return this.header_base[this.header + 5] & 0x1;
    }
    
    public int bos() {
        return this.header_base[this.header + 5] & 0x2;
    }
    
    public int eos() {
        return this.header_base[this.header + 5] & 0x4;
    }
    
    public long granulepos() {
        return (((((((long)(this.header_base[this.header + 13] & 0xFF) << 8 | (long)(this.header_base[this.header + 12] & 0xFF)) << 8 | (long)(this.header_base[this.header + 11] & 0xFF)) << 8 | (long)(this.header_base[this.header + 10] & 0xFF)) << 8 | (long)(this.header_base[this.header + 9] & 0xFF)) << 8 | (long)(this.header_base[this.header + 8] & 0xFF)) << 8 | (long)(this.header_base[this.header + 7] & 0xFF)) << 8 | (long)(this.header_base[this.header + 6] & 0xFF);
    }
    
    public int serialno() {
        return (this.header_base[this.header + 14] & 0xFF) | (this.header_base[this.header + 15] & 0xFF) << 8 | (this.header_base[this.header + 16] & 0xFF) << 16 | (this.header_base[this.header + 17] & 0xFF) << 24;
    }
    
    int pageno() {
        return (this.header_base[this.header + 18] & 0xFF) | (this.header_base[this.header + 19] & 0xFF) << 8 | (this.header_base[this.header + 20] & 0xFF) << 16 | (this.header_base[this.header + 21] & 0xFF) << 24;
    }
    
    void checksum() {
        int n = 0;
        for (int i = 0; i < this.header_len; ++i) {
            n = (n << 8 ^ Page.crc_lookup[(n >>> 24 & 0xFF) ^ (this.header_base[this.header + i] & 0xFF)]);
        }
        for (int j = 0; j < this.body_len; ++j) {
            n = (n << 8 ^ Page.crc_lookup[(n >>> 24 & 0xFF) ^ (this.body_base[this.body + j] & 0xFF)]);
        }
        this.header_base[this.header + 22] = (byte)n;
        this.header_base[this.header + 23] = (byte)(n >>> 8);
        this.header_base[this.header + 24] = (byte)(n >>> 16);
        this.header_base[this.header + 25] = (byte)(n >>> 24);
    }
    
    public Page copy() {
        return this.copy(new Page());
    }
    
    public Page copy(final Page page) {
        final byte[] header_base = new byte[this.header_len];
        System.arraycopy(this.header_base, this.header, header_base, 0, this.header_len);
        page.header_len = this.header_len;
        page.header_base = header_base;
        page.header = 0;
        final byte[] body_base = new byte[this.body_len];
        System.arraycopy(this.body_base, this.body, body_base, 0, this.body_len);
        page.body_len = this.body_len;
        page.body_base = body_base;
        page.body = 0;
        return page;
    }
    
    static {
        Page.crc_lookup = new int[256];
        for (int i = 0; i < Page.crc_lookup.length; ++i) {
            Page.crc_lookup[i] = crc_entry(i);
        }
    }
}
