// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

final class BitReserve
{
    private static final int BUFSIZE = 32768;
    private static final int BUFSIZE_MASK = 32767;
    private int offset;
    private int totbit;
    private int buf_byte_idx;
    private final int[] buf;
    private int buf_bit_idx;
    
    BitReserve() {
        this.buf = new int[32768];
        this.offset = 0;
        this.totbit = 0;
        this.buf_byte_idx = 0;
    }
    
    public int hsstell() {
        return this.totbit;
    }
    
    public int hgetbits(int N) {
        this.totbit += N;
        int val = 0;
        int pos = this.buf_byte_idx;
        if (pos + N < 32768) {
            while (N-- > 0) {
                val <<= 1;
                val |= ((this.buf[pos++] != 0) ? 1 : 0);
            }
        }
        else {
            while (N-- > 0) {
                val <<= 1;
                val |= ((this.buf[pos] != 0) ? 1 : 0);
                pos = (pos + 1 & 0x7FFF);
            }
        }
        this.buf_byte_idx = pos;
        return val;
    }
    
    public int hget1bit() {
        ++this.totbit;
        final int val = this.buf[this.buf_byte_idx];
        this.buf_byte_idx = (this.buf_byte_idx + 1 & 0x7FFF);
        return val;
    }
    
    public void hputbuf(final int val) {
        int ofs = this.offset;
        this.buf[ofs++] = (val & 0x80);
        this.buf[ofs++] = (val & 0x40);
        this.buf[ofs++] = (val & 0x20);
        this.buf[ofs++] = (val & 0x10);
        this.buf[ofs++] = (val & 0x8);
        this.buf[ofs++] = (val & 0x4);
        this.buf[ofs++] = (val & 0x2);
        this.buf[ofs++] = (val & 0x1);
        if (ofs == 32768) {
            this.offset = 0;
        }
        else {
            this.offset = ofs;
        }
    }
    
    public void rewindNbits(final int N) {
        this.totbit -= N;
        this.buf_byte_idx -= N;
        if (this.buf_byte_idx < 0) {
            this.buf_byte_idx += 32768;
        }
    }
    
    public void rewindNbytes(final int N) {
        final int bits = N << 3;
        this.totbit -= bits;
        this.buf_byte_idx -= bits;
        if (this.buf_byte_idx < 0) {
            this.buf_byte_idx += 32768;
        }
    }
}
