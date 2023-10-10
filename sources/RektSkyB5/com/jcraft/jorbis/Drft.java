/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

class Drft {
    int n;
    float[] trigcache;
    int[] splitcache;
    static int[] ntryh = new int[]{4, 2, 3, 5};
    static float tpi = (float)Math.PI * 2;
    static float hsqt2 = 0.70710677f;
    static float taui = 0.8660254f;
    static float taur = -0.5f;
    static float sqrt2 = 1.4142135f;

    Drft() {
    }

    void backward(float[] data) {
        if (this.n == 1) {
            return;
        }
        Drft.drftb1(this.n, data, this.trigcache, this.trigcache, this.n, this.splitcache);
    }

    void init(int n2) {
        this.n = n2;
        this.trigcache = new float[3 * n2];
        this.splitcache = new int[32];
        Drft.fdrffti(n2, this.trigcache, this.splitcache);
    }

    void clear() {
        if (this.trigcache != null) {
            this.trigcache = null;
        }
        if (this.splitcache != null) {
            this.splitcache = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    static void drfti1(int n2, float[] wa, int index, int[] ifac) {
        int ntry = 0;
        int j2 = -1;
        int nl = n2;
        int nf = 0;
        int state = 101;
        while (true) {
            switch (state) {
                case 101: {
                    ntry = ++j2 < 4 ? ntryh[j2] : (ntry += 2);
                }
                case 104: {
                    int i2;
                    int nq = nl / ntry;
                    int nr = nl - ntry * nq;
                    if (nr != 0) {
                        state = 101;
                        break;
                    }
                    ifac[++nf + 1] = ntry;
                    nl = nq;
                    if (ntry != 2) {
                        state = 107;
                        break;
                    }
                    if (nf == 1) {
                        state = 107;
                        break;
                    }
                    for (i2 = 1; i2 < nf; ++i2) {
                        int ib = nf - i2 + 1;
                        ifac[ib + 1] = ifac[ib];
                    }
                    ifac[2] = 2;
                }
                case 107: {
                    int i2;
                    if (nl != 1) {
                        state = 104;
                        break;
                    }
                    ifac[0] = n2;
                    ifac[1] = nf;
                    float argh = tpi / (float)n2;
                    int is = 0;
                    int nfm1 = nf - 1;
                    int l1 = 1;
                    if (nfm1 == 0) {
                        return;
                    }
                    for (int k1 = 0; k1 < nfm1; ++k1) {
                        int ip = ifac[k1 + 2];
                        int ld = 0;
                        int l2 = l1 * ip;
                        int ido = n2 / l2;
                        int ipm = ip - 1;
                        for (j2 = 0; j2 < ipm; is += ido, ++j2) {
                            i2 = is;
                            float argld = (float)(ld += l1) * argh;
                            float fi = 0.0f;
                            for (int ii = 2; ii < ido; ii += 2) {
                                float arg = (fi += 1.0f) * argld;
                                wa[index + i2++] = (float)Math.cos(arg);
                                wa[index + i2++] = (float)Math.sin(arg);
                            }
                        }
                        l1 = l2;
                    }
                    return;
                }
            }
        }
    }

    static void fdrffti(int n2, float[] wsave, int[] ifac) {
        if (n2 == 1) {
            return;
        }
        Drft.drfti1(n2, wsave, n2, ifac);
    }

    static void dradf2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
        int k2;
        int t2;
        int t1 = 0;
        int t0 = t2 = l1 * ido;
        int t3 = ido << 1;
        for (k2 = 0; k2 < l1; ++k2) {
            ch[t1 << 1] = cc[t1] + cc[t2];
            ch[(t1 << 1) + t3 - 1] = cc[t1] - cc[t2];
            t1 += ido;
            t2 += ido;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            t2 = t0;
            for (k2 = 0; k2 < l1; ++k2) {
                t3 = t2;
                int t4 = (t1 << 1) + (ido << 1);
                int t5 = t1;
                int t6 = t1 + t1;
                for (int i2 = 2; i2 < ido; i2 += 2) {
                    float tr2 = wa1[index + i2 - 2] * cc[(t3 += 2) - 1] + wa1[index + i2 - 1] * cc[t3];
                    float ti2 = wa1[index + i2 - 2] * cc[t3] - wa1[index + i2 - 1] * cc[t3 - 1];
                    ch[t6 += 2] = cc[t5 += 2] + ti2;
                    ch[t4 -= 2] = ti2 - cc[t5];
                    ch[t6 - 1] = cc[t5 - 1] + tr2;
                    ch[t4 - 1] = cc[t5 - 1] - tr2;
                }
                t1 += ido;
                t2 += ido;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido;
        t3 = t2 = t1 - 1;
        t2 += t0;
        for (k2 = 0; k2 < l1; ++k2) {
            ch[t1] = -cc[t2];
            ch[t1 - 1] = cc[t3];
            t1 += ido << 1;
            t2 += ido;
            t3 += ido;
        }
    }

    static void dradf4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
        float ti1;
        int t6;
        int t5;
        float tr2;
        float tr1;
        int k2;
        int t0;
        int t1 = t0 = l1 * ido;
        int t4 = t1 << 1;
        int t2 = t1 + (t1 << 1);
        int t3 = 0;
        for (k2 = 0; k2 < l1; ++k2) {
            tr1 = cc[t1] + cc[t2];
            tr2 = cc[t3] + cc[t4];
            t5 = t3 << 2;
            ch[t5] = tr1 + tr2;
            ch[(ido << 2) + t5 - 1] = tr2 - tr1;
            ch[(t5 += ido << 1) - 1] = cc[t3] - cc[t4];
            ch[t5] = cc[t2] - cc[t1];
            t1 += ido;
            t2 += ido;
            t3 += ido;
            t4 += ido;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            for (k2 = 0; k2 < l1; ++k2) {
                t2 = t1;
                t4 = t1 << 2;
                t6 = ido << 1;
                t5 = t6 + t4;
                for (int i2 = 2; i2 < ido; i2 += 2) {
                    t3 = t2 += 2;
                    t4 += 2;
                    t5 -= 2;
                    float cr2 = wa1[index1 + i2 - 2] * cc[(t3 += t0) - 1] + wa1[index1 + i2 - 1] * cc[t3];
                    float ci2 = wa1[index1 + i2 - 2] * cc[t3] - wa1[index1 + i2 - 1] * cc[t3 - 1];
                    float cr3 = wa2[index2 + i2 - 2] * cc[(t3 += t0) - 1] + wa2[index2 + i2 - 1] * cc[t3];
                    float ci3 = wa2[index2 + i2 - 2] * cc[t3] - wa2[index2 + i2 - 1] * cc[t3 - 1];
                    float cr4 = wa3[index3 + i2 - 2] * cc[(t3 += t0) - 1] + wa3[index3 + i2 - 1] * cc[t3];
                    float ci4 = wa3[index3 + i2 - 2] * cc[t3] - wa3[index3 + i2 - 1] * cc[t3 - 1];
                    tr1 = cr2 + cr4;
                    float tr4 = cr4 - cr2;
                    ti1 = ci2 + ci4;
                    float ti4 = ci2 - ci4;
                    float ti2 = cc[t2] + ci3;
                    float ti3 = cc[t2] - ci3;
                    tr2 = cc[t2 - 1] + cr3;
                    float tr3 = cc[t2 - 1] - cr3;
                    ch[t4 - 1] = tr1 + tr2;
                    ch[t4] = ti1 + ti2;
                    ch[t5 - 1] = tr3 - ti4;
                    ch[t5] = tr4 - ti3;
                    ch[t4 + t6 - 1] = ti4 + tr3;
                    ch[t4 + t6] = tr4 + ti3;
                    ch[t5 + t6 - 1] = tr2 - tr1;
                    ch[t5 + t6] = ti1 - ti2;
                }
                t1 += ido;
            }
            if ((ido & 1) != 0) {
                return;
            }
        }
        t1 = t0 + ido - 1;
        t2 = t1 + (t0 << 1);
        t3 = ido << 2;
        t4 = ido;
        t5 = ido << 1;
        t6 = ido;
        for (k2 = 0; k2 < l1; ++k2) {
            ti1 = -hsqt2 * (cc[t1] + cc[t2]);
            tr1 = hsqt2 * (cc[t1] - cc[t2]);
            ch[t4 - 1] = tr1 + cc[t6 - 1];
            ch[t4 + t5 - 1] = cc[t6 - 1] - tr1;
            ch[t4] = ti1 - cc[t1 + t0];
            ch[t4 + t5] = ti1 + cc[t1 + t0];
            t1 += ido;
            t2 += ido;
            t4 += t3;
            t6 += ido;
        }
    }

    /*
     * Unable to fully structure code
     */
    static void dradfg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
        t2 = 0;
        dcp = 0.0f;
        dsp = 0.0f;
        arg = Drft.tpi / (float)ip;
        dcp = (float)Math.cos(arg);
        dsp = (float)Math.sin(arg);
        ipph = ip + 1 >> 1;
        ipp2 = ip;
        idp2 = ido;
        nbd = ido - 1 >> 1;
        t0 = l1 * ido;
        t10 = ip * ido;
        state = 100;
        block7: while (true) {
            switch (state) {
                case 101: {
                    if (ido != 1) ** GOTO lbl20
                    state = 119;
                    ** GOTO lbl258
lbl20:
                    // 2 sources

                    for (ik = 0; ik < idl1; ++ik) {
                        ch2[ik] = c2[ik];
                    }
                    t1 = 0;
                    for (j = 1; j < ip; ++j) {
                        t2 = t1 += t0;
                        for (k = 0; k < l1; ++k) {
                            ch[t2] = c1[t2];
                            t2 += ido;
                        }
                    }
                    is = -ido;
                    t1 = 0;
                    if (nbd > l1) {
                        for (j = 1; j < ip; ++j) {
                            is += ido;
                            t2 = -ido + (t1 += t0);
                            for (k = 0; k < l1; ++k) {
                                idij = is - 1;
                                t3 = t2 += ido;
                                for (i = 2; i < ido; i += 2) {
                                    ch[(t3 += 2) - 1] = wa[index + (idij += 2) - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3];
                                    ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1];
                                }
                            }
                        }
                    } else {
                        for (j = 1; j < ip; ++j) {
                            idij = (is += ido) - 1;
                            t2 = t1 += t0;
                            for (i = 2; i < ido; i += 2) {
                                idij += 2;
                                t3 = t2 += 2;
                                for (k = 0; k < l1; ++k) {
                                    ch[t3 - 1] = wa[index + idij - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3];
                                    ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1];
                                    t3 += ido;
                                }
                            }
                        }
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    if (nbd < l1) {
                        for (j = 1; j < ipph; ++j) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            for (i = 2; i < ido; i += 2) {
                                t5 = (t3 += 2) - ido;
                                t6 = (t4 += 2) - ido;
                                for (k = 0; k < l1; ++k) {
                                    c1[(t5 += ido) - 1] = ch[t5 - 1] + ch[(t6 += ido) - 1];
                                    c1[t6 - 1] = ch[t5] - ch[t6];
                                    c1[t5] = ch[t5] + ch[t6];
                                    c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                                }
                            }
                        }
                    } else {
                        for (j = 1; j < ipph; ++j) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            for (k = 0; k < l1; ++k) {
                                t5 = t3;
                                t6 = t4;
                                for (i = 2; i < ido; i += 2) {
                                    c1[(t5 += 2) - 1] = ch[t5 - 1] + ch[(t6 += 2) - 1];
                                    c1[t6 - 1] = ch[t5] - ch[t6];
                                    c1[t5] = ch[t5] + ch[t6];
                                    c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                                }
                                t3 += ido;
                                t4 += ido;
                            }
                        }
                    }
                }
                case 119: {
                    for (ik = 0; ik < idl1; ++ik) {
                        c2[ik] = ch2[ik];
                    }
                    t1 = 0;
                    t2 = ipp2 * idl1;
                    for (j = 1; j < ipph; ++j) {
                        t3 = (t1 += t0) - ido;
                        t4 = (t2 -= t0) - ido;
                        for (k = 0; k < l1; ++k) {
                            c1[t3 += ido] = ch[t3] + ch[t4 += ido];
                            c1[t4] = ch[t4] - ch[t3];
                        }
                    }
                    ar1 = 1.0f;
                    ai1 = 0.0f;
                    t1 = 0;
                    t2 = ipp2 * idl1;
                    t3 = (ip - 1) * idl1;
                    for (l = 1; l < ipph; ++l) {
                        ar1h = dcp * ar1 - dsp * ai1;
                        ai1 = dcp * ai1 + dsp * ar1;
                        ar1 = ar1h;
                        t4 = t1 += idl1;
                        t5 = t2 -= idl1;
                        t6 = t3;
                        t7 = idl1;
                        for (ik = 0; ik < idl1; ++ik) {
                            ch2[t4++] = c2[ik] + ar1 * c2[t7++];
                            ch2[t5++] = ai1 * c2[t6++];
                        }
                        dc2 = ar1;
                        ds2 = ai1;
                        ar2 = ar1;
                        ai2 = ai1;
                        t4 = idl1;
                        t5 = (ipp2 - 1) * idl1;
                        for (j = 2; j < ipph; ++j) {
                            t4 += idl1;
                            t5 -= idl1;
                            ar2h = dc2 * ar2 - ds2 * ai2;
                            ai2 = dc2 * ai2 + ds2 * ar2;
                            ar2 = ar2h;
                            t6 = t1;
                            t7 = t2;
                            t8 = t4;
                            t9 = t5;
                            for (ik = 0; ik < idl1; ++ik) {
                                v0 = t6++;
                                ch2[v0] = ch2[v0] + ar2 * c2[t8++];
                                v1 = t7++;
                                ch2[v1] = ch2[v1] + ai2 * c2[t9++];
                            }
                        }
                    }
                    t1 = 0;
                    for (j = 1; j < ipph; ++j) {
                        t2 = t1 += idl1;
                        ik = 0;
                        while (ik < idl1) {
                            v2 = ik++;
                            ch2[v2] = ch2[v2] + c2[t2++];
                        }
                    }
                    if (ido < l1) {
                        state = 132;
                    } else {
                        t1 = 0;
                        t2 = 0;
                        for (k = 0; k < l1; ++k) {
                            t3 = t1;
                            t4 = t2;
                            for (i = 0; i < ido; ++i) {
                                cc[t4++] = ch[t3++];
                            }
                            t1 += ido;
                            t2 += t10;
                        }
                        state = 135;
                    }
                    ** GOTO lbl258
                }
                case 132: {
                    for (i = 0; i < ido; ++i) {
                        t1 = i;
                        t2 = i;
                        for (k = 0; k < l1; ++k) {
                            cc[t2] = ch[t1];
                            t1 += ido;
                            t2 += t10;
                        }
                    }
                }
                case 135: {
                    t1 = 0;
                    t2 = ido << 1;
                    t3 = 0;
                    t4 = ipp2 * t0;
                    for (j = 1; j < ipph; ++j) {
                        t5 = t1 += t2;
                        t6 = t3 += t0;
                        t7 = t4 -= t0;
                        for (k = 0; k < l1; ++k) {
                            cc[t5 - 1] = ch[t6];
                            cc[t5] = ch[t7];
                            t5 += t10;
                            t6 += ido;
                            t7 += ido;
                        }
                    }
                    if (ido == 1) {
                        return;
                    }
                    if (nbd >= l1) ** GOTO lbl206
                    state = 141;
                    ** GOTO lbl258
lbl206:
                    // 1 sources

                    t1 = -ido;
                    t3 = 0;
                    t4 = 0;
                    t5 = ipp2 * t0;
                    for (j = 1; j < ipph; ++j) {
                        t6 = t1 += t2;
                        t7 = t3 += t2;
                        t8 = t4 += t0;
                        t9 = t5 -= t0;
                        for (k = 0; k < l1; ++k) {
                            for (i = 2; i < ido; i += 2) {
                                ic = idp2 - i;
                                cc[i + t7 - 1] = ch[i + t8 - 1] + ch[i + t9 - 1];
                                cc[ic + t6 - 1] = ch[i + t8 - 1] - ch[i + t9 - 1];
                                cc[i + t7] = ch[i + t8] + ch[i + t9];
                                cc[ic + t6] = ch[i + t9] - ch[i + t8];
                            }
                            t6 += t10;
                            t7 += t10;
                            t8 += ido;
                            t9 += ido;
                        }
                    }
                    return;
                }
                case 141: {
                    t1 = -ido;
                    t3 = 0;
                    t4 = 0;
                    t5 = ipp2 * t0;
                    for (j = 1; j < ipph; ++j) {
                        t1 += t2;
                        t3 += t2;
                        t4 += t0;
                        t5 -= t0;
                        for (i = 2; i < ido; i += 2) {
                            t6 = idp2 + t1 - i;
                            t7 = i + t3;
                            t8 = i + t4;
                            t9 = i + t5;
                            for (k = 0; k < l1; ++k) {
                                cc[t7 - 1] = ch[t8 - 1] + ch[t9 - 1];
                                cc[t6 - 1] = ch[t8 - 1] - ch[t9 - 1];
                                cc[t7] = ch[t8] + ch[t9];
                                cc[t6] = ch[t9] - ch[t8];
                                t6 += t10;
                                t7 += t10;
                                t8 += ido;
                                t9 += ido;
                            }
                        }
                    }
                    break block7;
                }
lbl258:
                // 5 sources

                default: {
                    continue block7;
                }
            }
            break;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    static void drftf1(int n2, float[] c2, float[] ch, float[] wa, int[] ifac) {
        int nf = ifac[1];
        int na = 1;
        int l2 = n2;
        int iw = n2;
        int k1 = 0;
        while (true) {
            int idl1;
            int ido;
            int l1;
            int ip;
            if (k1 < nf) {
                int kh = nf - k1;
                ip = ifac[kh + 1];
                l1 = l2 / ip;
                ido = n2 / l2;
                idl1 = ido * l1;
                iw -= (ip - 1) * ido;
            } else {
                if (na == 1) {
                    return;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= n2) {
                        return;
                    }
                    c2[i2] = ch[i2];
                    ++i2;
                }
            }
            na = 1 - na;
            int state = 100;
            block10: while (true) {
                switch (state) {
                    case 100: {
                        if (ip != 4) {
                            state = 102;
                            break;
                        }
                        int ix2 = iw + ido;
                        int ix3 = ix2 + ido;
                        if (na != 0) {
                            Drft.dradf4(ido, l1, ch, c2, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        } else {
                            Drft.dradf4(ido, l1, c2, ch, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        }
                        state = 110;
                        break;
                    }
                    case 102: {
                        if (ip != 2) {
                            state = 104;
                            break;
                        }
                        if (na != 0) {
                            state = 103;
                            break;
                        }
                        Drft.dradf2(ido, l1, c2, ch, wa, iw - 1);
                        state = 110;
                        break;
                    }
                    case 103: {
                        Drft.dradf2(ido, l1, ch, c2, wa, iw - 1);
                    }
                    case 104: {
                        if (ido == 1) {
                            na = 1 - na;
                        }
                        if (na != 0) {
                            state = 109;
                            break;
                        }
                        Drft.dradfg(ido, ip, l1, idl1, c2, c2, c2, ch, ch, wa, iw - 1);
                        na = 1;
                        state = 110;
                        break;
                    }
                    case 109: {
                        Drft.dradfg(ido, ip, l1, idl1, ch, ch, ch, c2, c2, wa, iw - 1);
                        na = 0;
                    }
                    case 110: {
                        l2 = l1;
                        break block10;
                    }
                }
            }
            ++k1;
        }
    }

    static void dradb2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
        int k2;
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = 0;
        int t3 = (ido << 1) - 1;
        for (k2 = 0; k2 < l1; ++k2) {
            ch[t1] = cc[t2] + cc[t3 + t2];
            ch[t1 + t0] = cc[t2] - cc[t3 + t2];
            t2 = (t1 += ido) << 1;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            t2 = 0;
            for (k2 = 0; k2 < l1; ++k2) {
                t3 = t1;
                int t4 = t2;
                int t5 = t4 + (ido << 1);
                int t6 = t0 + t1;
                for (int i2 = 2; i2 < ido; i2 += 2) {
                    ch[(t3 += 2) - 1] = cc[(t4 += 2) - 1] + cc[(t5 -= 2) - 1];
                    float tr2 = cc[t4 - 1] - cc[t5 - 1];
                    ch[t3] = cc[t4] - cc[t5];
                    float ti2 = cc[t4] + cc[t5];
                    ch[(t6 += 2) - 1] = wa1[index + i2 - 2] * tr2 - wa1[index + i2 - 1] * ti2;
                    ch[t6] = wa1[index + i2 - 2] * ti2 + wa1[index + i2 - 1] * tr2;
                }
                t2 = (t1 += ido) << 1;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido - 1;
        t2 = ido - 1;
        for (k2 = 0; k2 < l1; ++k2) {
            ch[t1] = cc[t2] + cc[t2];
            ch[t1 + t0] = -(cc[t2 + 1] + cc[t2 + 1]);
            t1 += ido;
            t2 += ido << 1;
        }
    }

    static void dradb3(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2) {
        float ci3;
        float cr2;
        float tr2;
        int k2;
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = t0 << 1;
        int t3 = ido << 1;
        int t4 = ido + (ido << 1);
        int t5 = 0;
        for (k2 = 0; k2 < l1; ++k2) {
            tr2 = cc[t3 - 1] + cc[t3 - 1];
            cr2 = cc[t5] + taur * tr2;
            ch[t1] = cc[t5] + tr2;
            ci3 = taui * (cc[t3] + cc[t3]);
            ch[t1 + t0] = cr2 - ci3;
            ch[t1 + t2] = cr2 + ci3;
            t1 += ido;
            t3 += t4;
            t5 += t4;
        }
        if (ido == 1) {
            return;
        }
        t1 = 0;
        t3 = ido << 1;
        for (k2 = 0; k2 < l1; ++k2) {
            int t7 = t1 + (t1 << 1);
            int t6 = t5 = t7 + t3;
            int t8 = t1;
            int t9 = t1 + t0;
            int t10 = t9 + t0;
            for (int i2 = 2; i2 < ido; i2 += 2) {
                t9 += 2;
                t10 += 2;
                tr2 = cc[(t5 += 2) - 1] + cc[(t6 -= 2) - 1];
                cr2 = cc[(t7 += 2) - 1] + taur * tr2;
                ch[(t8 += 2) - 1] = cc[t7 - 1] + tr2;
                float ti2 = cc[t5] - cc[t6];
                float ci2 = cc[t7] + taur * ti2;
                ch[t8] = cc[t7] + ti2;
                float cr3 = taui * (cc[t5 - 1] - cc[t6 - 1]);
                ci3 = taui * (cc[t5] + cc[t6]);
                float dr2 = cr2 - ci3;
                float dr3 = cr2 + ci3;
                float di2 = ci2 + cr3;
                float di3 = ci2 - cr3;
                ch[t9 - 1] = wa1[index1 + i2 - 2] * dr2 - wa1[index1 + i2 - 1] * di2;
                ch[t9] = wa1[index1 + i2 - 2] * di2 + wa1[index1 + i2 - 1] * dr2;
                ch[t10 - 1] = wa2[index2 + i2 - 2] * dr3 - wa2[index2 + i2 - 1] * di3;
                ch[t10] = wa2[index2 + i2 - 2] * di3 + wa2[index2 + i2 - 1] * dr3;
            }
            t1 += ido;
        }
    }

    static void dradb4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
        float ti2;
        float ti1;
        float tr2;
        float tr1;
        float tr4;
        float tr3;
        int t5;
        int t4;
        int k2;
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = ido << 2;
        int t3 = 0;
        int t6 = ido << 1;
        for (k2 = 0; k2 < l1; ++k2) {
            t4 = t3 + t6;
            t5 = t1;
            tr3 = cc[t4 - 1] + cc[t4 - 1];
            tr4 = cc[t4] + cc[t4];
            tr1 = cc[t3] - cc[(t4 += t6) - 1];
            tr2 = cc[t3] + cc[t4 - 1];
            ch[t5] = tr2 + tr3;
            ch[t5 += t0] = tr1 - tr4;
            ch[t5 += t0] = tr2 - tr3;
            ch[t5 += t0] = tr1 + tr4;
            t1 += ido;
            t3 += t2;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            for (k2 = 0; k2 < l1; ++k2) {
                t2 = t1 << 2;
                t4 = t3 = t2 + t6;
                t5 = t3 + t6;
                int t7 = t1;
                for (int i2 = 2; i2 < ido; i2 += 2) {
                    t7 += 2;
                    ti1 = cc[t2 += 2] + cc[t5 -= 2];
                    ti2 = cc[t2] - cc[t5];
                    float ti3 = cc[t3 += 2] - cc[t4 -= 2];
                    tr4 = cc[t3] + cc[t4];
                    tr1 = cc[t2 - 1] - cc[t5 - 1];
                    tr2 = cc[t2 - 1] + cc[t5 - 1];
                    float ti4 = cc[t3 - 1] - cc[t4 - 1];
                    tr3 = cc[t3 - 1] + cc[t4 - 1];
                    ch[t7 - 1] = tr2 + tr3;
                    float cr3 = tr2 - tr3;
                    ch[t7] = ti2 + ti3;
                    float ci3 = ti2 - ti3;
                    float cr2 = tr1 - tr4;
                    float cr4 = tr1 + tr4;
                    float ci2 = ti1 + ti4;
                    float ci4 = ti1 - ti4;
                    int t8 = t7 + t0;
                    ch[t8 - 1] = wa1[index1 + i2 - 2] * cr2 - wa1[index1 + i2 - 1] * ci2;
                    ch[t8] = wa1[index1 + i2 - 2] * ci2 + wa1[index1 + i2 - 1] * cr2;
                    ch[(t8 += t0) - 1] = wa2[index2 + i2 - 2] * cr3 - wa2[index2 + i2 - 1] * ci3;
                    ch[t8] = wa2[index2 + i2 - 2] * ci3 + wa2[index2 + i2 - 1] * cr3;
                    ch[(t8 += t0) - 1] = wa3[index3 + i2 - 2] * cr4 - wa3[index3 + i2 - 1] * ci4;
                    ch[t8] = wa3[index3 + i2 - 2] * ci4 + wa3[index3 + i2 - 1] * cr4;
                }
                t1 += ido;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido;
        t2 = ido << 2;
        t3 = ido - 1;
        t4 = ido + (ido << 1);
        for (k2 = 0; k2 < l1; ++k2) {
            t5 = t3;
            ti1 = cc[t1] + cc[t4];
            ti2 = cc[t4] - cc[t1];
            tr1 = cc[t1 - 1] - cc[t4 - 1];
            tr2 = cc[t1 - 1] + cc[t4 - 1];
            ch[t5] = tr2 + tr2;
            ch[t5 += t0] = sqrt2 * (tr1 - ti1);
            ch[t5 += t0] = ti2 + ti2;
            ch[t5 += t0] = -sqrt2 * (tr1 + ti1);
            t3 += ido;
            t1 += t2;
            t4 += t2;
        }
    }

    /*
     * Unable to fully structure code
     */
    static void dradbg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
        ipph = 0;
        t0 = 0;
        t10 = 0;
        nbd = 0;
        dcp = 0.0f;
        dsp = 0.0f;
        ipp2 = 0;
        state = 100;
        block10: while (true) {
            switch (state) {
                case 100: {
                    t10 = ip * ido;
                    t0 = l1 * ido;
                    arg = Drft.tpi / (float)ip;
                    dcp = (float)Math.cos(arg);
                    dsp = (float)Math.sin(arg);
                    nbd = ido - 1 >>> 1;
                    ipp2 = ip;
                    ipph = ip + 1 >>> 1;
                    if (ido < l1) {
                        state = 103;
                    } else {
                        t1 = 0;
                        t2 = 0;
                        for (k = 0; k < l1; ++k) {
                            t3 = t1;
                            t4 = t2;
                            for (i = 0; i < ido; ++i) {
                                ch[t3] = cc[t4];
                                ++t3;
                                ++t4;
                            }
                            t1 += ido;
                            t2 += t10;
                        }
                        state = 106;
                    }
                    ** GOTO lbl283
                }
                case 103: {
                    t1 = 0;
                    for (i = 0; i < ido; ++i) {
                        t2 = t1;
                        t3 = t1;
                        for (k = 0; k < l1; ++k) {
                            ch[t2] = cc[t3];
                            t2 += ido;
                            t3 += t10;
                        }
                        ++t1;
                    }
                }
                case 106: {
                    t1 = 0;
                    t2 = ipp2 * t0;
                    t7 = t5 = ido << 1;
                    for (j = 1; j < ipph; ++j) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        t6 = t5;
                        for (k = 0; k < l1; ++k) {
                            ch[t3] = cc[t6 - 1] + cc[t6 - 1];
                            ch[t4] = cc[t6] + cc[t6];
                            t3 += ido;
                            t4 += ido;
                            t6 += t10;
                        }
                        t5 += t7;
                    }
                    if (ido == 1) {
                        state = 116;
                    } else if (nbd < l1) {
                        state = 112;
                    } else {
                        t1 = 0;
                        t2 = ipp2 * t0;
                        t7 = 0;
                        for (j = 1; j < ipph; ++j) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            t8 = t7 += ido << 1;
                            for (k = 0; k < l1; ++k) {
                                t5 = t3;
                                t6 = t4;
                                t9 = t8;
                                t11 = t8;
                                for (i = 2; i < ido; i += 2) {
                                    ch[(t5 += 2) - 1] = cc[(t9 += 2) - 1] + cc[(t11 -= 2) - 1];
                                    ch[(t6 += 2) - 1] = cc[t9 - 1] - cc[t11 - 1];
                                    ch[t5] = cc[t9] - cc[t11];
                                    ch[t6] = cc[t9] + cc[t11];
                                }
                                t3 += ido;
                                t4 += ido;
                                t8 += t10;
                            }
                        }
                        state = 116;
                    }
                    ** GOTO lbl283
                }
                case 112: {
                    t1 = 0;
                    t2 = ipp2 * t0;
                    t7 = 0;
                    for (j = 1; j < ipph; ++j) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        t8 = t7 += ido << 1;
                        t9 = t7;
                        for (i = 2; i < ido; i += 2) {
                            t5 = t3 += 2;
                            t6 = t4 += 2;
                            t11 = t8 += 2;
                            t12 = t9 -= 2;
                            for (k = 0; k < l1; ++k) {
                                ch[t5 - 1] = cc[t11 - 1] + cc[t12 - 1];
                                ch[t6 - 1] = cc[t11 - 1] - cc[t12 - 1];
                                ch[t5] = cc[t11] - cc[t12];
                                ch[t6] = cc[t11] + cc[t12];
                                t5 += ido;
                                t6 += ido;
                                t11 += t10;
                                t12 += t10;
                            }
                        }
                    }
                }
                case 116: {
                    ar1 = 1.0f;
                    ai1 = 0.0f;
                    t1 = 0;
                    t9 = t2 = ipp2 * idl1;
                    t3 = (ip - 1) * idl1;
                    for (l = 1; l < ipph; ++l) {
                        ar1h = dcp * ar1 - dsp * ai1;
                        ai1 = dcp * ai1 + dsp * ar1;
                        ar1 = ar1h;
                        t4 = t1 += idl1;
                        t5 = t2 -= idl1;
                        t6 = 0;
                        t7 = idl1;
                        t8 = t3;
                        for (ik = 0; ik < idl1; ++ik) {
                            c2[t4++] = ch2[t6++] + ar1 * ch2[t7++];
                            c2[t5++] = ai1 * ch2[t8++];
                        }
                        dc2 = ar1;
                        ds2 = ai1;
                        ar2 = ar1;
                        ai2 = ai1;
                        t6 = idl1;
                        t7 = t9 - idl1;
                        for (j = 2; j < ipph; ++j) {
                            t6 += idl1;
                            t7 -= idl1;
                            ar2h = dc2 * ar2 - ds2 * ai2;
                            ai2 = dc2 * ai2 + ds2 * ar2;
                            ar2 = ar2h;
                            t4 = t1;
                            t5 = t2;
                            t11 = t6;
                            t12 = t7;
                            for (ik = 0; ik < idl1; ++ik) {
                                v0 = t4++;
                                c2[v0] = c2[v0] + ar2 * ch2[t11++];
                                v1 = t5++;
                                c2[v1] = c2[v1] + ai2 * ch2[t12++];
                            }
                        }
                    }
                    t1 = 0;
                    for (j = 1; j < ipph; ++j) {
                        t2 = t1 += idl1;
                        ik = 0;
                        while (ik < idl1) {
                            v2 = ik++;
                            ch2[v2] = ch2[v2] + ch2[t2++];
                        }
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    for (j = 1; j < ipph; ++j) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        for (k = 0; k < l1; ++k) {
                            ch[t3] = c1[t3] - c1[t4];
                            ch[t4] = c1[t3] + c1[t4];
                            t3 += ido;
                            t4 += ido;
                        }
                    }
                    if (ido == 1) {
                        state = 132;
                    } else if (nbd < l1) {
                        state = 128;
                    } else {
                        t1 = 0;
                        t2 = ipp2 * t0;
                        for (j = 1; j < ipph; ++j) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            for (k = 0; k < l1; ++k) {
                                t5 = t3;
                                t6 = t4;
                                for (i = 2; i < ido; i += 2) {
                                    ch[(t5 += 2) - 1] = c1[t5 - 1] - c1[t6 += 2];
                                    ch[t6 - 1] = c1[t5 - 1] + c1[t6];
                                    ch[t5] = c1[t5] + c1[t6 - 1];
                                    ch[t6] = c1[t5] - c1[t6 - 1];
                                }
                                t3 += ido;
                                t4 += ido;
                            }
                        }
                        state = 132;
                    }
                    ** GOTO lbl283
                }
                case 128: {
                    t1 = 0;
                    t2 = ipp2 * t0;
                    for (j = 1; j < ipph; ++j) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        for (i = 2; i < ido; i += 2) {
                            t5 = t3 += 2;
                            t6 = t4 += 2;
                            for (k = 0; k < l1; ++k) {
                                ch[t5 - 1] = c1[t5 - 1] - c1[t6];
                                ch[t6 - 1] = c1[t5 - 1] + c1[t6];
                                ch[t5] = c1[t5] + c1[t6 - 1];
                                ch[t6] = c1[t5] - c1[t6 - 1];
                                t5 += ido;
                                t6 += ido;
                            }
                        }
                    }
                }
                case 132: {
                    if (ido == 1) {
                        return;
                    }
                    for (ik = 0; ik < idl1; ++ik) {
                        c2[ik] = ch2[ik];
                    }
                    t1 = 0;
                    for (j = 1; j < ip; ++j) {
                        t2 = t1 += t0;
                        for (k = 0; k < l1; ++k) {
                            c1[t2] = ch[t2];
                            t2 += ido;
                        }
                    }
                    if (nbd <= l1) ** GOTO lbl250
                    state = 139;
                    ** GOTO lbl283
lbl250:
                    // 1 sources

                    is = -ido - 1;
                    t1 = 0;
                    for (j = 1; j < ip; ++j) {
                        idij = is += ido;
                        t2 = t1 += t0;
                        for (i = 2; i < ido; i += 2) {
                            idij += 2;
                            t3 = t2 += 2;
                            for (k = 0; k < l1; ++k) {
                                c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
                                c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
                                t3 += ido;
                            }
                        }
                    }
                    return;
                }
                case 139: {
                    is = -ido - 1;
                    t1 = 0;
                    for (j = 1; j < ip; ++j) {
                        is += ido;
                        t2 = t1 += t0;
                        for (k = 0; k < l1; ++k) {
                            idij = is;
                            t3 = t2;
                            for (i = 2; i < ido; i += 2) {
                                c1[(t3 += 2) - 1] = wa[index + (idij += 2) - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
                                c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
                            }
                            t2 += ido;
                        }
                    }
                    break block10;
                }
lbl283:
                // 10 sources

                default: {
                    continue block10;
                }
            }
            break;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    static void drftb1(int n2, float[] c2, float[] ch, float[] wa, int index, int[] ifac) {
        int l2 = 0;
        int ip = 0;
        int ido = 0;
        int idl1 = 0;
        int nf = ifac[1];
        int na = 0;
        int l1 = 1;
        int iw = 1;
        int k1 = 0;
        while (true) {
            if (k1 >= nf) {
                if (na == 0) {
                    return;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= n2) {
                        return;
                    }
                    c2[i2] = ch[i2];
                    ++i2;
                }
            }
            int state = 100;
            block9: while (true) {
                switch (state) {
                    case 100: {
                        ip = ifac[k1 + 2];
                        l2 = ip * l1;
                        ido = n2 / l2;
                        idl1 = ido * l1;
                        if (ip != 4) {
                            state = 103;
                            break;
                        }
                        int ix2 = iw + ido;
                        int ix3 = ix2 + ido;
                        if (na != 0) {
                            Drft.dradb4(ido, l1, ch, c2, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        } else {
                            Drft.dradb4(ido, l1, c2, ch, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        break;
                    }
                    case 103: {
                        if (ip != 2) {
                            state = 106;
                            break;
                        }
                        if (na != 0) {
                            Drft.dradb2(ido, l1, ch, c2, wa, index + iw - 1);
                        } else {
                            Drft.dradb2(ido, l1, c2, ch, wa, index + iw - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        break;
                    }
                    case 106: {
                        if (ip != 3) {
                            state = 109;
                            break;
                        }
                        int ix2 = iw + ido;
                        if (na != 0) {
                            Drft.dradb3(ido, l1, ch, c2, wa, index + iw - 1, wa, index + ix2 - 1);
                        } else {
                            Drft.dradb3(ido, l1, c2, ch, wa, index + iw - 1, wa, index + ix2 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        break;
                    }
                    case 109: {
                        if (na != 0) {
                            Drft.dradbg(ido, ip, l1, idl1, ch, ch, ch, c2, c2, wa, index + iw - 1);
                        } else {
                            Drft.dradbg(ido, ip, l1, idl1, c2, c2, c2, ch, ch, wa, index + iw - 1);
                        }
                        if (ido == 1) {
                            na = 1 - na;
                        }
                    }
                    case 115: {
                        l1 = l2;
                        iw += (ip - 1) * ido;
                        break block9;
                    }
                }
            }
            ++k1;
        }
    }
}

