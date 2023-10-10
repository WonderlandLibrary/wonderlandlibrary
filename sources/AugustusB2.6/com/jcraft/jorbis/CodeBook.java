// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook
{
    int dim;
    int entries;
    StaticCodeBook c;
    float[] valuelist;
    int[] codelist;
    DecodeAux decode_tree;
    private int[] t;
    
    CodeBook() {
        this.c = new StaticCodeBook();
        this.t = new int[15];
    }
    
    int encode(final int n, final Buffer buffer) {
        buffer.write(this.codelist[n], this.c.lengthlist[n]);
        return this.c.lengthlist[n];
    }
    
    int errorv(final float[] array) {
        final int best = this.best(array, 1);
        for (int i = 0; i < this.dim; ++i) {
            array[i] = this.valuelist[best * this.dim + i];
        }
        return best;
    }
    
    int encodev(final int n, final float[] array, final Buffer buffer) {
        for (int i = 0; i < this.dim; ++i) {
            array[i] = this.valuelist[n * this.dim + i];
        }
        return this.encode(n, buffer);
    }
    
    int encodevs(final float[] array, final Buffer buffer, final int n, final int n2) {
        return this.encode(this.besterror(array, n, n2), buffer);
    }
    
    synchronized int decodevs_add(final float[] array, final int n, final Buffer buffer, final int n2) {
        final int n3 = n2 / this.dim;
        if (this.t.length < n3) {
            this.t = new int[n3];
        }
        for (int i = 0; i < n3; ++i) {
            final int decode = this.decode(buffer);
            if (decode == -1) {
                return -1;
            }
            this.t[i] = decode * this.dim;
        }
        for (int j = 0, n4 = 0; j < this.dim; ++j, n4 += n3) {
            for (int k = 0; k < n3; ++k) {
                final int n5 = n + n4 + k;
                array[n5] += this.valuelist[this.t[k] + j];
            }
        }
        return 0;
    }
    
    int decodev_add(final float[] array, final int n, final Buffer buffer, final int n2) {
        if (this.dim > 8) {
            int i = 0;
            while (i < n2) {
                final int decode = this.decode(buffer);
                if (decode == -1) {
                    return -1;
                }
                int n4;
                for (int n3 = decode * this.dim, j = 0; j < this.dim; array[n4] += this.valuelist[n3 + j++]) {
                    n4 = n + i++;
                }
            }
        }
        else {
            int k = 0;
            while (k < n2) {
                final int decode2 = this.decode(buffer);
                if (decode2 == -1) {
                    return -1;
                }
                final int n5 = decode2 * this.dim;
                int n6 = 0;
                switch (this.dim) {
                    case 8: {
                        final int n7 = n + k++;
                        array[n7] += this.valuelist[n5 + n6++];
                    }
                    case 7: {
                        final int n8 = n + k++;
                        array[n8] += this.valuelist[n5 + n6++];
                    }
                    case 6: {
                        final int n9 = n + k++;
                        array[n9] += this.valuelist[n5 + n6++];
                    }
                    case 5: {
                        final int n10 = n + k++;
                        array[n10] += this.valuelist[n5 + n6++];
                    }
                    case 4: {
                        final int n11 = n + k++;
                        array[n11] += this.valuelist[n5 + n6++];
                    }
                    case 3: {
                        final int n12 = n + k++;
                        array[n12] += this.valuelist[n5 + n6++];
                    }
                    case 2: {
                        final int n13 = n + k++;
                        array[n13] += this.valuelist[n5 + n6++];
                    }
                    case 1: {
                        final int n14 = n + k++;
                        array[n14] += this.valuelist[n5 + n6++];
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        return 0;
    }
    
    int decodev_set(final float[] array, final int n, final Buffer buffer, final int n2) {
        int i = 0;
        while (i < n2) {
            final int decode = this.decode(buffer);
            if (decode == -1) {
                return -1;
            }
            for (int n3 = decode * this.dim, j = 0; j < this.dim; array[n + i++] = this.valuelist[n3 + j++]) {}
        }
        return 0;
    }
    
    int decodevv_add(final float[][] array, final int n, final int n2, final Buffer buffer, final int n3) {
        int n4 = 0;
        int i = n / n2;
        while (i < (n + n3) / n2) {
            final int decode = this.decode(buffer);
            if (decode == -1) {
                return -1;
            }
            final int n5 = decode * this.dim;
            for (int j = 0; j < this.dim; ++j) {
                final float[] array2 = array[n4++];
                final int n6 = i;
                array2[n6] += this.valuelist[n5 + j];
                if (n4 == n2) {
                    n4 = 0;
                    ++i;
                }
            }
        }
        return 0;
    }
    
    int decode(final Buffer buffer) {
        int i = 0;
        final DecodeAux decode_tree = this.decode_tree;
        final int look = buffer.look(decode_tree.tabn);
        if (look >= 0) {
            i = decode_tree.tab[look];
            buffer.adv(decode_tree.tabl[look]);
            if (i <= 0) {
                return -i;
            }
        }
        do {
            switch (buffer.read1()) {
                case 0: {
                    i = decode_tree.ptr0[i];
                    continue;
                }
                case 1: {
                    i = decode_tree.ptr1[i];
                    continue;
                }
                default: {
                    return -1;
                }
            }
        } while (i > 0);
        return -i;
    }
    
    int decodevs(final float[] array, final int n, final Buffer buffer, final int n2, final int n3) {
        final int decode = this.decode(buffer);
        if (decode == -1) {
            return -1;
        }
        switch (n3) {
            case -1: {
                for (int i = 0, n4 = 0; i < this.dim; ++i, n4 += n2) {
                    array[n + n4] = this.valuelist[decode * this.dim + i];
                }
                break;
            }
            case 0: {
                for (int j = 0, n5 = 0; j < this.dim; ++j, n5 += n2) {
                    final int n6 = n + n5;
                    array[n6] += this.valuelist[decode * this.dim + j];
                }
                break;
            }
            case 1: {
                for (int k = 0, n7 = 0; k < this.dim; ++k, n7 += n2) {
                    final int n8 = n + n7;
                    array[n8] *= this.valuelist[decode * this.dim + k];
                }
                break;
            }
        }
        return decode;
    }
    
    int best(final float[] array, final int n) {
        final EncodeAuxNearestMatch nearest_tree = this.c.nearest_tree;
        final EncodeAuxThreshMatch thresh_tree = this.c.thresh_tree;
        int i = 0;
        if (thresh_tree != null) {
            int n2 = 0;
            for (int j = 0, n3 = n * (this.dim - 1); j < this.dim; ++j, n3 -= n) {
                int n4;
                for (n4 = 0; n4 < thresh_tree.threshvals - 1 && array[n3] >= thresh_tree.quantthresh[n4]; ++n4) {}
                n2 = n2 * thresh_tree.quantvals + thresh_tree.quantmap[n4];
            }
            if (this.c.lengthlist[n2] > 0) {
                return n2;
            }
        }
        if (nearest_tree != null) {
            do {
                float n5 = 0.0f;
                final int n6 = nearest_tree.p[i];
                final int n7 = nearest_tree.q[i];
                for (int k = 0, n8 = 0; k < this.dim; ++k, n8 += n) {
                    n5 += (float)((this.valuelist[n6 + k] - this.valuelist[n7 + k]) * (array[n8] - (this.valuelist[n6 + k] + this.valuelist[n7 + k]) * 0.5));
                }
                if (n5 > 0.0) {
                    i = -nearest_tree.ptr0[i];
                }
                else {
                    i = -nearest_tree.ptr1[i];
                }
            } while (i > 0);
            return -i;
        }
        int n9 = -1;
        float n10 = 0.0f;
        int n11 = 0;
        for (int l = 0; l < this.entries; ++l) {
            if (this.c.lengthlist[l] > 0) {
                final float dist = dist(this.dim, this.valuelist, n11, array, n);
                if (n9 == -1 || dist < n10) {
                    n10 = dist;
                    n9 = l;
                }
            }
            n11 += this.dim;
        }
        return n9;
    }
    
    int besterror(final float[] array, final int n, final int n2) {
        final int best = this.best(array, n);
        switch (n2) {
            case 0: {
                for (int i = 0, n3 = 0; i < this.dim; ++i, n3 += n) {
                    final int n4 = n3;
                    array[n4] -= this.valuelist[best * this.dim + i];
                }
                break;
            }
            case 1: {
                for (int j = 0, n5 = 0; j < this.dim; ++j, n5 += n) {
                    final float n6 = this.valuelist[best * this.dim + j];
                    if (n6 == 0.0f) {
                        array[n5] = 0.0f;
                    }
                    else {
                        final int n7 = n5;
                        array[n7] /= n6;
                    }
                }
                break;
            }
        }
        return best;
    }
    
    void clear() {
    }
    
    private static float dist(final int n, final float[] array, final int n2, final float[] array2, final int n3) {
        float n4 = 0.0f;
        for (int i = 0; i < n; ++i) {
            final float n5 = array[n2 + i] - array2[i * n3];
            n4 += n5 * n5;
        }
        return n4;
    }
    
    int init_decode(final StaticCodeBook c) {
        this.c = c;
        this.entries = c.entries;
        this.dim = c.dim;
        this.valuelist = c.unquantize();
        this.decode_tree = this.make_decode_tree();
        if (this.decode_tree == null) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    static int[] make_words(final int[] array, final int n) {
        final int[] array2 = new int[33];
        final int[] array3 = new int[n];
        for (int i = 0; i < n; ++i) {
            final int n2 = array[i];
            if (n2 > 0) {
                int n3 = array2[n2];
                if (n2 < 32 && n3 >>> n2 != 0) {
                    return null;
                }
                array3[i] = n3;
                int j = n2;
                while (j > 0) {
                    if ((array2[j] & 0x1) != 0x0) {
                        if (j == 1) {
                            final int[] array4 = array2;
                            final int n4 = 1;
                            ++array4[n4];
                            break;
                        }
                        array2[j] = array2[j - 1] << 1;
                        break;
                    }
                    else {
                        final int[] array5 = array2;
                        final int n5 = j;
                        ++array5[n5];
                        --j;
                    }
                }
                for (int n6 = n2 + 1; n6 < 33 && array2[n6] >>> 1 == n3; n3 = array2[n6], array2[n6] = array2[n6 - 1] << 1, ++n6) {}
            }
        }
        for (int k = 0; k < n; ++k) {
            int n7 = 0;
            for (int l = 0; l < array[k]; ++l) {
                n7 = (n7 << 1 | (array3[k] >>> l & 0x1));
            }
            array3[k] = n7;
        }
        return array3;
    }
    
    DecodeAux make_decode_tree() {
        int n = 0;
        final DecodeAux decodeAux2;
        final DecodeAux decodeAux = decodeAux2 = new DecodeAux();
        final int[] ptr0 = new int[this.entries * 2];
        decodeAux2.ptr0 = ptr0;
        final int[] array = ptr0;
        final DecodeAux decodeAux3 = decodeAux;
        final int[] ptr2 = new int[this.entries * 2];
        decodeAux3.ptr1 = ptr2;
        final int[] array2 = ptr2;
        final int[] make_words = make_words(this.c.lengthlist, this.c.entries);
        if (make_words == null) {
            return null;
        }
        decodeAux.aux = this.entries * 2;
        for (int i = 0; i < this.entries; ++i) {
            if (this.c.lengthlist[i] > 0) {
                int n2 = 0;
                int j;
                for (j = 0; j < this.c.lengthlist[i] - 1; ++j) {
                    if ((make_words[i] >>> j & 0x1) == 0x0) {
                        if (array[n2] == 0) {
                            array[n2] = ++n;
                        }
                        n2 = array[n2];
                    }
                    else {
                        if (array2[n2] == 0) {
                            array2[n2] = ++n;
                        }
                        n2 = array2[n2];
                    }
                }
                if ((make_words[i] >>> j & 0x1) == 0x0) {
                    array[n2] = -i;
                }
                else {
                    array2[n2] = -i;
                }
            }
        }
        decodeAux.tabn = ilog(this.entries) - 4;
        if (decodeAux.tabn < 5) {
            decodeAux.tabn = 5;
        }
        final int n3 = 1 << decodeAux.tabn;
        decodeAux.tab = new int[n3];
        decodeAux.tabl = new int[n3];
        for (int k = 0; k < n3; ++k) {
            int n4;
            int n5;
            for (n4 = 0, n5 = 0; n5 < decodeAux.tabn && (n4 > 0 || n5 == 0); ++n5) {
                if ((k & 1 << n5) != 0x0) {
                    n4 = array2[n4];
                }
                else {
                    n4 = array[n4];
                }
            }
            decodeAux.tab[k] = n4;
            decodeAux.tabl[k] = n5;
        }
        return decodeAux;
    }
    
    private static int ilog(int i) {
        int n = 0;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
}
