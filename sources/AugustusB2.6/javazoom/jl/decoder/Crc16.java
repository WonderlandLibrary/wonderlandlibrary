// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public final class Crc16
{
    private static short polynomial;
    private short crc;
    
    public Crc16() {
        this.crc = -1;
    }
    
    public void add_bits(final int bitstring, final int length) {
        int bitmask = 1 << length - 1;
        do {
            if ((this.crc & 0x8000) == 0x0 ^ (bitstring & bitmask) == 0x0) {
                this.crc <<= 1;
                this.crc ^= Crc16.polynomial;
            }
            else {
                this.crc <<= 1;
            }
        } while ((bitmask >>>= 1) != 0);
    }
    
    public short checksum() {
        final short sum = this.crc;
        this.crc = -1;
        return sum;
    }
    
    static {
        Crc16.polynomial = -32763;
    }
}
