/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jorbis.StaticCodeBook;
import com.jcraft.jorbis.Util;

class CodeBook {
    int dim;
    int entries;
    StaticCodeBook c = new StaticCodeBook();
    float[] valuelist;
    int[] codelist;
    DecodeAux decode_tree;
    private int[] t = new int[15];

    CodeBook() {
    }

    int encode(int a2, Buffer b2) {
        b2.write(this.codelist[a2], this.c.lengthlist[a2]);
        return this.c.lengthlist[a2];
    }

    int errorv(float[] a2) {
        int best = this.best(a2, 1);
        for (int k2 = 0; k2 < this.dim; ++k2) {
            a2[k2] = this.valuelist[best * this.dim + k2];
        }
        return best;
    }

    int encodev(int best, float[] a2, Buffer b2) {
        for (int k2 = 0; k2 < this.dim; ++k2) {
            a2[k2] = this.valuelist[best * this.dim + k2];
        }
        return this.encode(best, b2);
    }

    int encodevs(float[] a2, Buffer b2, int step, int addmul) {
        int best = this.besterror(a2, step, addmul);
        return this.encode(best, b2);
    }

    synchronized int decodevs_add(float[] a2, int offset, Buffer b2, int n2) {
        int i2;
        int step = n2 / this.dim;
        if (this.t.length < step) {
            this.t = new int[step];
        }
        for (i2 = 0; i2 < step; ++i2) {
            int entry = this.decode(b2);
            if (entry == -1) {
                return -1;
            }
            this.t[i2] = entry * this.dim;
        }
        i2 = 0;
        int o2 = 0;
        while (i2 < this.dim) {
            for (int j2 = 0; j2 < step; ++j2) {
                int n3 = offset + o2 + j2;
                a2[n3] = a2[n3] + this.valuelist[this.t[j2] + i2];
            }
            ++i2;
            o2 += step;
        }
        return 0;
    }

    int decodev_add(float[] a2, int offset, Buffer b2, int n2) {
        if (this.dim > 8) {
            int i2 = 0;
            while (i2 < n2) {
                int entry = this.decode(b2);
                if (entry == -1) {
                    return -1;
                }
                int t2 = entry * this.dim;
                int j2 = 0;
                while (j2 < this.dim) {
                    int n3 = offset + i2++;
                    a2[n3] = a2[n3] + this.valuelist[t2 + j2++];
                }
            }
        } else {
            int i3 = 0;
            while (i3 < n2) {
                int entry = this.decode(b2);
                if (entry == -1) {
                    return -1;
                }
                int t3 = entry * this.dim;
                int j3 = 0;
                switch (this.dim) {
                    case 8: {
                        int n4 = offset + i3++;
                        a2[n4] = a2[n4] + this.valuelist[t3 + j3++];
                    }
                    case 7: {
                        int n5 = offset + i3++;
                        a2[n5] = a2[n5] + this.valuelist[t3 + j3++];
                    }
                    case 6: {
                        int n6 = offset + i3++;
                        a2[n6] = a2[n6] + this.valuelist[t3 + j3++];
                    }
                    case 5: {
                        int n7 = offset + i3++;
                        a2[n7] = a2[n7] + this.valuelist[t3 + j3++];
                    }
                    case 4: {
                        int n8 = offset + i3++;
                        a2[n8] = a2[n8] + this.valuelist[t3 + j3++];
                    }
                    case 3: {
                        int n9 = offset + i3++;
                        a2[n9] = a2[n9] + this.valuelist[t3 + j3++];
                    }
                    case 2: {
                        int n10 = offset + i3++;
                        a2[n10] = a2[n10] + this.valuelist[t3 + j3++];
                    }
                    case 1: {
                        int n11 = offset + i3++;
                        a2[n11] = a2[n11] + this.valuelist[t3 + j3++];
                    }
                }
            }
        }
        return 0;
    }

    int decodev_set(float[] a2, int offset, Buffer b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            int entry = this.decode(b2);
            if (entry == -1) {
                return -1;
            }
            int t2 = entry * this.dim;
            int j2 = 0;
            while (j2 < this.dim) {
                a2[offset + i2++] = this.valuelist[t2 + j2++];
            }
        }
        return 0;
    }

    int decodevv_add(float[][] a2, int offset, int ch, Buffer b2, int n2) {
        int chptr = 0;
        int i2 = offset / ch;
        while (i2 < (offset + n2) / ch) {
            int entry = this.decode(b2);
            if (entry == -1) {
                return -1;
            }
            int t2 = entry * this.dim;
            for (int j2 = 0; j2 < this.dim; ++j2) {
                float[] fArray = a2[chptr++];
                int n3 = i2++;
                fArray[n3] = fArray[n3] + this.valuelist[t2 + j2];
                if (chptr != ch) continue;
                chptr = 0;
            }
        }
        return 0;
    }

    int decode(Buffer b2) {
        int ptr = 0;
        DecodeAux t2 = this.decode_tree;
        int lok = b2.look(t2.tabn);
        if (lok >= 0) {
            ptr = t2.tab[lok];
            b2.adv(t2.tabl[lok]);
            if (ptr <= 0) {
                return -ptr;
            }
        }
        do {
            switch (b2.read1()) {
                case 0: {
                    ptr = t2.ptr0[ptr];
                    break;
                }
                case 1: {
                    ptr = t2.ptr1[ptr];
                    break;
                }
                default: {
                    return -1;
                }
            }
        } while (ptr > 0);
        return -ptr;
    }

    int decodevs(float[] a2, int index, Buffer b2, int step, int addmul) {
        int entry = this.decode(b2);
        if (entry == -1) {
            return -1;
        }
        switch (addmul) {
            case -1: {
                int i2 = 0;
                int o2 = 0;
                while (i2 < this.dim) {
                    a2[index + o2] = this.valuelist[entry * this.dim + i2];
                    ++i2;
                    o2 += step;
                }
                break;
            }
            case 0: {
                int i3 = 0;
                int o3 = 0;
                while (i3 < this.dim) {
                    int n2 = index + o3;
                    a2[n2] = a2[n2] + this.valuelist[entry * this.dim + i3];
                    ++i3;
                    o3 += step;
                }
                break;
            }
            case 1: {
                int i4 = 0;
                int o4 = 0;
                while (i4 < this.dim) {
                    int n3 = index + o4;
                    a2[n3] = a2[n3] * this.valuelist[entry * this.dim + i4];
                    ++i4;
                    o4 += step;
                }
                break;
            }
        }
        return entry;
    }

    int best(float[] a2, int step) {
        int besti = -1;
        float best = 0.0f;
        int e2 = 0;
        for (int i2 = 0; i2 < this.entries; ++i2) {
            if (this.c.lengthlist[i2] > 0) {
                float _this = CodeBook.dist(this.dim, this.valuelist, e2, a2, step);
                if (besti == -1 || _this < best) {
                    best = _this;
                    besti = i2;
                }
            }
            e2 += this.dim;
        }
        return besti;
    }

    int besterror(float[] a2, int step, int addmul) {
        int best = this.best(a2, step);
        switch (addmul) {
            case 0: {
                int i2 = 0;
                int o2 = 0;
                while (i2 < this.dim) {
                    int n2 = o2;
                    a2[n2] = a2[n2] - this.valuelist[best * this.dim + i2];
                    ++i2;
                    o2 += step;
                }
                break;
            }
            case 1: {
                int i3 = 0;
                int o3 = 0;
                while (i3 < this.dim) {
                    float val2 = this.valuelist[best * this.dim + i3];
                    if (val2 == 0.0f) {
                        a2[o3] = 0.0f;
                    } else {
                        int n3 = o3;
                        a2[n3] = a2[n3] / val2;
                    }
                    ++i3;
                    o3 += step;
                }
                break;
            }
        }
        return best;
    }

    void clear() {
    }

    private static float dist(int el, float[] ref, int index, float[] b2, int step) {
        float acc = 0.0f;
        for (int i2 = 0; i2 < el; ++i2) {
            float val2 = ref[index + i2] - b2[i2 * step];
            acc += val2 * val2;
        }
        return acc;
    }

    int init_decode(StaticCodeBook s2) {
        this.c = s2;
        this.entries = s2.entries;
        this.dim = s2.dim;
        this.valuelist = s2.unquantize();
        this.decode_tree = this.make_decode_tree();
        if (this.decode_tree == null) {
            this.clear();
            return -1;
        }
        return 0;
    }

    static int[] make_words(int[] l2, int n2) {
        int i2;
        int[] marker = new int[33];
        int[] r2 = new int[n2];
        for (i2 = 0; i2 < n2; ++i2) {
            int length = l2[i2];
            if (length <= 0) continue;
            int entry = marker[length];
            if (length < 32 && entry >>> length != 0) {
                return null;
            }
            r2[i2] = entry;
            int j2 = length;
            while (j2 > 0) {
                if ((marker[j2] & 1) != 0) {
                    if (j2 == 1) {
                        marker[1] = marker[1] + 1;
                        break;
                    }
                    marker[j2] = marker[j2 - 1] << 1;
                    break;
                }
                int n3 = j2--;
                marker[n3] = marker[n3] + 1;
            }
            for (j2 = length + 1; j2 < 33 && marker[j2] >>> 1 == entry; ++j2) {
                entry = marker[j2];
                marker[j2] = marker[j2 - 1] << 1;
            }
        }
        for (i2 = 0; i2 < n2; ++i2) {
            int temp = 0;
            for (int j3 = 0; j3 < l2[i2]; ++j3) {
                temp <<= 1;
                temp |= r2[i2] >>> j3 & 1;
            }
            r2[i2] = temp;
        }
        return r2;
    }

    DecodeAux make_decode_tree() {
        int top = 0;
        DecodeAux t2 = new DecodeAux();
        t2.ptr0 = new int[this.entries * 2];
        int[] ptr0 = t2.ptr0;
        t2.ptr1 = new int[this.entries * 2];
        int[] ptr1 = t2.ptr1;
        int[] codelist = CodeBook.make_words(this.c.lengthlist, this.c.entries);
        if (codelist == null) {
            return null;
        }
        t2.aux = this.entries * 2;
        for (int i2 = 0; i2 < this.entries; ++i2) {
            int j2;
            if (this.c.lengthlist[i2] <= 0) continue;
            int ptr = 0;
            for (j2 = 0; j2 < this.c.lengthlist[i2] - 1; ++j2) {
                int bit = codelist[i2] >>> j2 & 1;
                if (bit == 0) {
                    if (ptr0[ptr] == 0) {
                        ptr0[ptr] = ++top;
                    }
                    ptr = ptr0[ptr];
                    continue;
                }
                if (ptr1[ptr] == 0) {
                    ptr1[ptr] = ++top;
                }
                ptr = ptr1[ptr];
            }
            if ((codelist[i2] >>> j2 & 1) == 0) {
                ptr0[ptr] = -i2;
                continue;
            }
            ptr1[ptr] = -i2;
        }
        t2.tabn = Util.ilog(this.entries) - 4;
        if (t2.tabn < 5) {
            t2.tabn = 5;
        }
        int n2 = 1 << t2.tabn;
        t2.tab = new int[n2];
        t2.tabl = new int[n2];
        for (int i3 = 0; i3 < n2; ++i3) {
            int p2 = 0;
            int j3 = 0;
            for (j3 = 0; j3 < t2.tabn && (p2 > 0 || j3 == 0); ++j3) {
                p2 = (i3 & 1 << j3) != 0 ? ptr1[p2] : ptr0[p2];
            }
            t2.tab[i3] = p2;
            t2.tabl[i3] = j3;
        }
        return t2;
    }

    class DecodeAux {
        int[] tab;
        int[] tabl;
        int tabn;
        int[] ptr0;
        int[] ptr1;
        int aux;

        DecodeAux() {
        }
    }
}

