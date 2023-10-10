// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class StaticCodeBook
{
    int dim;
    int entries;
    int[] lengthlist;
    int maptype;
    int q_min;
    int q_delta;
    int q_quant;
    int q_sequencep;
    int[] quantlist;
    EncodeAuxNearestMatch nearest_tree;
    EncodeAuxThreshMatch thresh_tree;
    static final int VQ_FEXP = 10;
    static final int VQ_FMAN = 21;
    static final int VQ_FEXP_BIAS = 768;
    
    StaticCodeBook() {
    }
    
    StaticCodeBook(final int dim, final int entries, final int[] lengthlist, final int maptype, final int q_min, final int q_delta, final int q_quant, final int q_sequencep, final int[] quantlist, final Object o, final Object o2) {
        this();
        this.dim = dim;
        this.entries = entries;
        this.lengthlist = lengthlist;
        this.maptype = maptype;
        this.q_min = q_min;
        this.q_delta = q_delta;
        this.q_quant = q_quant;
        this.q_sequencep = q_sequencep;
        this.quantlist = quantlist;
    }
    
    int pack(final Buffer buffer) {
        boolean b = false;
        buffer.write(5653314, 24);
        buffer.write(this.dim, 16);
        buffer.write(this.entries, 24);
        int n;
        for (n = 1; n < this.entries && this.lengthlist[n] >= this.lengthlist[n - 1]; ++n) {}
        if (n == this.entries) {
            b = true;
        }
        if (b) {
            int n2 = 0;
            buffer.write(1, 1);
            buffer.write(this.lengthlist[0] - 1, 5);
            int i;
            for (i = 1; i < this.entries; ++i) {
                final int n3 = this.lengthlist[i];
                final int n4 = this.lengthlist[i - 1];
                if (n3 > n4) {
                    for (int j = n4; j < n3; ++j) {
                        buffer.write(i - n2, ilog(this.entries - n2));
                        n2 = i;
                    }
                }
            }
            buffer.write(i - n2, ilog(this.entries - n2));
        }
        else {
            buffer.write(0, 1);
            int n5;
            for (n5 = 0; n5 < this.entries && this.lengthlist[n5] != 0; ++n5) {}
            if (n5 == this.entries) {
                buffer.write(0, 1);
                for (int k = 0; k < this.entries; ++k) {
                    buffer.write(this.lengthlist[k] - 1, 5);
                }
            }
            else {
                buffer.write(1, 1);
                for (int l = 0; l < this.entries; ++l) {
                    if (this.lengthlist[l] == 0) {
                        buffer.write(0, 1);
                    }
                    else {
                        buffer.write(1, 1);
                        buffer.write(this.lengthlist[l] - 1, 5);
                    }
                }
            }
        }
        buffer.write(this.maptype, 4);
        switch (this.maptype) {
            case 0: {
                break;
            }
            case 1:
            case 2: {
                if (this.quantlist == null) {
                    return -1;
                }
                buffer.write(this.q_min, 32);
                buffer.write(this.q_delta, 32);
                buffer.write(this.q_quant - 1, 4);
                buffer.write(this.q_sequencep, 1);
                int maptype1_quantvals = 0;
                switch (this.maptype) {
                    case 1: {
                        maptype1_quantvals = this.maptype1_quantvals();
                        break;
                    }
                    case 2: {
                        maptype1_quantvals = this.entries * this.dim;
                        break;
                    }
                }
                for (int n6 = 0; n6 < maptype1_quantvals; ++n6) {
                    buffer.write(Math.abs(this.quantlist[n6]), this.q_quant);
                }
                break;
            }
            default: {
                return -1;
            }
        }
        return 0;
    }
    
    int unpack(final Buffer buffer) {
        if (buffer.read(24) != 5653314) {
            this.clear();
            return -1;
        }
        this.dim = buffer.read(16);
        this.entries = buffer.read(24);
        if (this.entries == -1) {
            this.clear();
            return -1;
        }
        switch (buffer.read(1)) {
            case 0: {
                this.lengthlist = new int[this.entries];
                if (buffer.read(1) != 0) {
                    for (int i = 0; i < this.entries; ++i) {
                        if (buffer.read(1) != 0) {
                            final int read = buffer.read(5);
                            if (read == -1) {
                                this.clear();
                                return -1;
                            }
                            this.lengthlist[i] = read + 1;
                        }
                        else {
                            this.lengthlist[i] = 0;
                        }
                    }
                    break;
                }
                for (int j = 0; j < this.entries; ++j) {
                    final int read2 = buffer.read(5);
                    if (read2 == -1) {
                        this.clear();
                        return -1;
                    }
                    this.lengthlist[j] = read2 + 1;
                }
                break;
            }
            case 1: {
                int n = buffer.read(5) + 1;
                this.lengthlist = new int[this.entries];
                int k = 0;
                while (k < this.entries) {
                    final int read3 = buffer.read(ilog(this.entries - k));
                    if (read3 == -1) {
                        this.clear();
                        return -1;
                    }
                    for (int l = 0; l < read3; ++l, ++k) {
                        this.lengthlist[k] = n;
                    }
                    ++n;
                }
                break;
            }
            default: {
                return -1;
            }
        }
        switch (this.maptype = buffer.read(4)) {
            case 0: {
                break;
            }
            case 1:
            case 2: {
                this.q_min = buffer.read(32);
                this.q_delta = buffer.read(32);
                this.q_quant = buffer.read(4) + 1;
                this.q_sequencep = buffer.read(1);
                int maptype1_quantvals = 0;
                switch (this.maptype) {
                    case 1: {
                        maptype1_quantvals = this.maptype1_quantvals();
                        break;
                    }
                    case 2: {
                        maptype1_quantvals = this.entries * this.dim;
                        break;
                    }
                }
                this.quantlist = new int[maptype1_quantvals];
                for (int n2 = 0; n2 < maptype1_quantvals; ++n2) {
                    this.quantlist[n2] = buffer.read(this.q_quant);
                }
                if (this.quantlist[maptype1_quantvals - 1] == -1) {
                    this.clear();
                    return -1;
                }
                break;
            }
            default: {
                this.clear();
                return -1;
            }
        }
        return 0;
    }
    
    private int maptype1_quantvals() {
        int n = (int)Math.floor(Math.pow(this.entries, 1.0 / this.dim));
        while (true) {
            int n2 = 1;
            int n3 = 1;
            for (int i = 0; i < this.dim; ++i) {
                n2 *= n;
                n3 *= n + 1;
            }
            if (n2 <= this.entries && n3 > this.entries) {
                break;
            }
            if (n2 > this.entries) {
                --n;
            }
            else {
                ++n;
            }
        }
        return n;
    }
    
    void clear() {
    }
    
    float[] unquantize() {
        if (this.maptype == 1 || this.maptype == 2) {
            final float float32_unpack = float32_unpack(this.q_min);
            final float float32_unpack2 = float32_unpack(this.q_delta);
            final float[] array = new float[this.entries * this.dim];
            switch (this.maptype) {
                case 1: {
                    final int maptype1_quantvals = this.maptype1_quantvals();
                    for (int i = 0; i < this.entries; ++i) {
                        float n = 0.0f;
                        int n2 = 1;
                        for (int j = 0; j < this.dim; ++j) {
                            final float n3 = Math.abs((float)this.quantlist[i / n2 % maptype1_quantvals]) * float32_unpack2 + float32_unpack + n;
                            if (this.q_sequencep != 0) {
                                n = n3;
                            }
                            array[i * this.dim + j] = n3;
                            n2 *= maptype1_quantvals;
                        }
                    }
                    break;
                }
                case 2: {
                    for (int k = 0; k < this.entries; ++k) {
                        float n4 = 0.0f;
                        for (int l = 0; l < this.dim; ++l) {
                            final float n5 = Math.abs((float)this.quantlist[k * this.dim + l]) * float32_unpack2 + float32_unpack + n4;
                            if (this.q_sequencep != 0) {
                                n4 = n5;
                            }
                            array[k * this.dim + l] = n5;
                        }
                    }
                    break;
                }
            }
            return array;
        }
        return null;
    }
    
    private static int ilog(int i) {
        int n = 0;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    static long float32_pack(float n) {
        int n2 = 0;
        if (n < 0.0f) {
            n2 = Integer.MIN_VALUE;
            n = -n;
        }
        final int n3 = (int)Math.floor(Math.log(n) / Math.log(2.0));
        return n2 | n3 + 768 << 21 | (int)Math.rint(Math.pow(n, 20 - n3));
    }
    
    static float float32_unpack(final int n) {
        float n2 = (float)(n & 0x1FFFFF);
        final float n3 = (float)(n & Integer.MIN_VALUE);
        final float n4 = (float)((n & 0x7FE00000) >>> 21);
        if ((n & Integer.MIN_VALUE) != 0x0) {
            n2 = -n2;
        }
        return ldexp(n2, (int)n4 - 20 - 768);
    }
    
    static float ldexp(final float n, final int n2) {
        return (float)(n * Math.pow(2.0, n2));
    }
}
