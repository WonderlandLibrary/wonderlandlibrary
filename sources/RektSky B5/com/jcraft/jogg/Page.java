/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jogg;

public class Page {
    private static int[] crc_lookup = new int[256];
    public byte[] header_base;
    public int header;
    public int header_len;
    public byte[] body_base;
    public int body;
    public int body_len;

    private static int crc_entry(int index) {
        int r2 = index << 24;
        for (int i2 = 0; i2 < 8; ++i2) {
            if ((r2 & Integer.MIN_VALUE) != 0) {
                r2 = r2 << 1 ^ 0x4C11DB7;
                continue;
            }
            r2 <<= 1;
        }
        return r2 & 0xFFFFFFFF;
    }

    int version() {
        return this.header_base[this.header + 4] & 0xFF;
    }

    int continued() {
        return this.header_base[this.header + 5] & 1;
    }

    public int bos() {
        return this.header_base[this.header + 5] & 2;
    }

    public int eos() {
        return this.header_base[this.header + 5] & 4;
    }

    public long granulepos() {
        long foo = this.header_base[this.header + 13] & 0xFF;
        foo = foo << 8 | (long)(this.header_base[this.header + 12] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 11] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 10] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 9] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 8] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 7] & 0xFF);
        foo = foo << 8 | (long)(this.header_base[this.header + 6] & 0xFF);
        return foo;
    }

    public int serialno() {
        return this.header_base[this.header + 14] & 0xFF | (this.header_base[this.header + 15] & 0xFF) << 8 | (this.header_base[this.header + 16] & 0xFF) << 16 | (this.header_base[this.header + 17] & 0xFF) << 24;
    }

    int pageno() {
        return this.header_base[this.header + 18] & 0xFF | (this.header_base[this.header + 19] & 0xFF) << 8 | (this.header_base[this.header + 20] & 0xFF) << 16 | (this.header_base[this.header + 21] & 0xFF) << 24;
    }

    void checksum() {
        int i2;
        int crc_reg = 0;
        for (i2 = 0; i2 < this.header_len; ++i2) {
            crc_reg = crc_reg << 8 ^ crc_lookup[crc_reg >>> 24 & 0xFF ^ this.header_base[this.header + i2] & 0xFF];
        }
        for (i2 = 0; i2 < this.body_len; ++i2) {
            crc_reg = crc_reg << 8 ^ crc_lookup[crc_reg >>> 24 & 0xFF ^ this.body_base[this.body + i2] & 0xFF];
        }
        this.header_base[this.header + 22] = (byte)crc_reg;
        this.header_base[this.header + 23] = (byte)(crc_reg >>> 8);
        this.header_base[this.header + 24] = (byte)(crc_reg >>> 16);
        this.header_base[this.header + 25] = (byte)(crc_reg >>> 24);
    }

    public Page copy() {
        return this.copy(new Page());
    }

    public Page copy(Page p2) {
        byte[] tmp = new byte[this.header_len];
        System.arraycopy(this.header_base, this.header, tmp, 0, this.header_len);
        p2.header_len = this.header_len;
        p2.header_base = tmp;
        p2.header = 0;
        tmp = new byte[this.body_len];
        System.arraycopy(this.body_base, this.body, tmp, 0, this.body_len);
        p2.body_len = this.body_len;
        p2.body_base = tmp;
        p2.body = 0;
        return p2;
    }

    static {
        for (int i2 = 0; i2 < crc_lookup.length; ++i2) {
            Page.crc_lookup[i2] = Page.crc_entry(i2);
        }
    }
}

