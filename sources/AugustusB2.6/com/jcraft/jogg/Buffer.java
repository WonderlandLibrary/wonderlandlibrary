// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class Buffer
{
    private static final int BUFFER_INCREMENT = 256;
    private static final int[] mask;
    int ptr;
    byte[] buffer;
    int endbit;
    int endbyte;
    int storage;
    
    public Buffer() {
        this.ptr = 0;
        this.buffer = null;
        this.endbit = 0;
        this.endbyte = 0;
        this.storage = 0;
    }
    
    public void writeinit() {
        this.buffer = new byte[256];
        this.ptr = 0;
        this.buffer[0] = 0;
        this.storage = 256;
    }
    
    public void write(final byte[] array) {
        for (int n = 0; n < array.length && array[n] != 0; ++n) {
            this.write(array[n], 8);
        }
    }
    
    public void read(final byte[] array, int n) {
        int n2 = 0;
        while (n-- != 0) {
            array[n2++] = (byte)this.read(8);
        }
    }
    
    void reset() {
        this.ptr = 0;
        this.buffer[0] = 0;
        final int n = 0;
        this.endbyte = n;
        this.endbit = n;
    }
    
    public void writeclear() {
        this.buffer = null;
    }
    
    public void readinit(final byte[] array, final int n) {
        this.readinit(array, 0, n);
    }
    
    public void readinit(final byte[] buffer, final int ptr, final int storage) {
        this.ptr = ptr;
        this.buffer = buffer;
        final int n = 0;
        this.endbyte = n;
        this.endbit = n;
        this.storage = storage;
    }
    
    public void write(int n, int n2) {
        if (this.endbyte + 4 >= this.storage) {
            final byte[] buffer = new byte[this.storage + 256];
            System.arraycopy(this.buffer, 0, buffer, 0, this.storage);
            this.buffer = buffer;
            this.storage += 256;
        }
        n &= Buffer.mask[n2];
        n2 += this.endbit;
        final byte[] buffer2 = this.buffer;
        final int ptr = this.ptr;
        buffer2[ptr] |= (byte)(n << this.endbit);
        if (n2 >= 8) {
            this.buffer[this.ptr + 1] = (byte)(n >>> 8 - this.endbit);
            if (n2 >= 16) {
                this.buffer[this.ptr + 2] = (byte)(n >>> 16 - this.endbit);
                if (n2 >= 24) {
                    this.buffer[this.ptr + 3] = (byte)(n >>> 24 - this.endbit);
                    if (n2 >= 32) {
                        if (this.endbit > 0) {
                            this.buffer[this.ptr + 4] = (byte)(n >>> 32 - this.endbit);
                        }
                        else {
                            this.buffer[this.ptr + 4] = 0;
                        }
                    }
                }
            }
        }
        this.endbyte += n2 / 8;
        this.ptr += n2 / 8;
        this.endbit = (n2 & 0x7);
    }
    
    public int look(int n) {
        final int n2 = Buffer.mask[n];
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage && this.endbyte + (n - 1) / 8 >= this.storage) {
            return -1;
        }
        int n3 = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (n > 8) {
            n3 |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (n > 16) {
                n3 |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (n > 24) {
                    n3 |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n3 |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        return n2 & n3;
    }
    
    public int look1() {
        if (this.endbyte >= this.storage) {
            return -1;
        }
        return this.buffer[this.ptr] >> this.endbit & 0x1;
    }
    
    public void adv(int n) {
        n += this.endbit;
        this.ptr += n / 8;
        this.endbyte += n / 8;
        this.endbit = (n & 0x7);
    }
    
    public void adv1() {
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
    }
    
    public int read(int n) {
        final int n2 = Buffer.mask[n];
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            final int n3 = -1;
            if (this.endbyte + (n - 1) / 8 >= this.storage) {
                this.ptr += n / 8;
                this.endbyte += n / 8;
                this.endbit = (n & 0x7);
                return n3;
            }
        }
        int n4 = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (n > 8) {
            n4 |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (n > 16) {
                n4 |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (n > 24) {
                    n4 |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n4 |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        final int n5 = n4 & n2;
        this.ptr += n / 8;
        this.endbyte += n / 8;
        this.endbit = (n & 0x7);
        return n5;
    }
    
    public int readB(int n) {
        final int n2 = 32 - n;
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            final int n3 = -1;
            if (this.endbyte * 8 + n > this.storage * 8) {
                this.ptr += n / 8;
                this.endbyte += n / 8;
                this.endbit = (n & 0x7);
                return n3;
            }
        }
        int n4 = (this.buffer[this.ptr] & 0xFF) << 24 + this.endbit;
        if (n > 8) {
            n4 |= (this.buffer[this.ptr + 1] & 0xFF) << 16 + this.endbit;
            if (n > 16) {
                n4 |= (this.buffer[this.ptr + 2] & 0xFF) << 8 + this.endbit;
                if (n > 24) {
                    n4 |= (this.buffer[this.ptr + 3] & 0xFF) << this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n4 |= (this.buffer[this.ptr + 4] & 0xFF) >> 8 - this.endbit;
                    }
                }
            }
        }
        final int n5 = n4 >>> (n2 >> 1) >>> (n2 + 1 >> 1);
        this.ptr += n / 8;
        this.endbyte += n / 8;
        this.endbit = (n & 0x7);
        return n5;
    }
    
    public int read1() {
        if (this.endbyte >= this.storage) {
            final int n = -1;
            ++this.endbit;
            if (this.endbit > 7) {
                this.endbit = 0;
                ++this.ptr;
                ++this.endbyte;
            }
            return n;
        }
        final int n2 = this.buffer[this.ptr] >> this.endbit & 0x1;
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
        return n2;
    }
    
    public int bytes() {
        return this.endbyte + (this.endbit + 7) / 8;
    }
    
    public int bits() {
        return this.endbyte * 8 + this.endbit;
    }
    
    public byte[] buffer() {
        return this.buffer;
    }
    
    public static int ilog(int i) {
        int n = 0;
        while (i > 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    public static void report(final String x) {
        System.err.println(x);
        System.exit(1);
    }
    
    static {
        mask = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1 };
    }
}
