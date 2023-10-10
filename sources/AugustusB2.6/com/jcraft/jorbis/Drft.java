// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Drft
{
    int n;
    float[] trigcache;
    int[] splitcache;
    static int[] ntryh;
    static float tpi;
    static float hsqt2;
    static float taui;
    static float taur;
    static float sqrt2;
    
    void backward(final float[] array) {
        if (this.n == 1) {
            return;
        }
        drftb1(this.n, array, this.trigcache, this.trigcache, this.n, this.splitcache);
    }
    
    void init(final int n) {
        this.n = n;
        this.trigcache = new float[3 * n];
        this.splitcache = new int[32];
        fdrffti(n, this.trigcache, this.splitcache);
    }
    
    void clear() {
        if (this.trigcache != null) {
            this.trigcache = null;
        }
        if (this.splitcache != null) {
            this.splitcache = null;
        }
    }
    
    static void drfti1(final int n, final float[] array, final int n2, final int[] array2) {
        int n3 = 0;
        int n4 = -1;
        int n5 = n;
        int n6 = 0;
        int n7 = 101;
    Label_0201:
        while (true) {
            Label_0079: {
                switch (n7) {
                    case 101: {
                        if (++n4 < 4) {
                            n3 = Drft.ntryh[n4];
                            break Label_0079;
                        }
                        n3 += 2;
                        break Label_0079;
                    }
                    case 104: {
                        final int n8 = n5 / n3;
                        if (n5 - n3 * n8 != 0) {
                            n7 = 101;
                            continue;
                        }
                        ++n6;
                        array2[n6 + 1] = n3;
                        n5 = n8;
                        if (n3 != 2) {
                            n7 = 107;
                            continue;
                        }
                        if (n6 == 1) {
                            n7 = 107;
                            continue;
                        }
                        for (int i = 1; i < n6; ++i) {
                            final int n9 = n6 - i + 1;
                            array2[n9 + 1] = array2[n9];
                        }
                        array2[2] = 2;
                    }
                    case 107: {
                        if (n5 != 1) {
                            n7 = 104;
                            continue;
                        }
                        break Label_0201;
                    }
                }
            }
        }
        array2[0] = n;
        array2[1] = n6;
        final float n10 = Drft.tpi / n;
        int n11 = 0;
        final int n12 = n6 - 1;
        int n13 = 1;
        if (n12 == 0) {
            return;
        }
        for (int j = 0; j < n12; ++j) {
            final int n14 = array2[j + 2];
            int n15 = 0;
            final int n16 = n13 * n14;
            final int n17 = n / n16;
            for (int n18 = n14 - 1, k = 0; k < n18; ++k) {
                n15 += n13;
                int n19 = n11;
                final float n20 = n15 * n10;
                float n21 = 0.0f;
                for (int l = 2; l < n17; l += 2) {
                    ++n21;
                    final float n22 = n21 * n20;
                    array[n2 + n19++] = (float)Math.cos(n22);
                    array[n2 + n19++] = (float)Math.sin(n22);
                }
                n11 += n17;
            }
            n13 = n16;
        }
    }
    
    static void fdrffti(final int n, final float[] array, final int[] array2) {
        if (n == 1) {
            return;
        }
        drfti1(n, array, n, array2);
    }
    
    static void dradf2(final int n, final int n2, final float[] array, final float[] array2, final float[] array3, final int n3) {
        int n4 = 0;
        final int n6;
        int n5 = n6 = n2 * n;
        final int n7 = n << 1;
        for (int i = 0; i < n2; ++i) {
            array2[n4 << 1] = array[n4] + array[n5];
            array2[(n4 << 1) + n7 - 1] = array[n4] - array[n5];
            n4 += n;
            n5 += n;
        }
        if (n < 2) {
            return;
        }
        if (n != 2) {
            int n8 = 0;
            int n9 = n6;
            for (int j = 0; j < n2; ++j) {
                int n10 = n9;
                int n11 = (n8 << 1) + (n << 1);
                int n12 = n8;
                int n13 = n8 + n8;
                for (int k = 2; k < n; k += 2) {
                    n10 += 2;
                    n11 -= 2;
                    n12 += 2;
                    n13 += 2;
                    final float n14 = array3[n3 + k - 2] * array[n10 - 1] + array3[n3 + k - 1] * array[n10];
                    final float n15 = array3[n3 + k - 2] * array[n10] - array3[n3 + k - 1] * array[n10 - 1];
                    array2[n13] = array[n12] + n15;
                    array2[n11] = n15 - array[n12];
                    array2[n13 - 1] = array[n12 - 1] + n14;
                    array2[n11 - 1] = array[n12 - 1] - n14;
                }
                n8 += n;
                n9 += n;
            }
            if (n % 2 == 1) {
                return;
            }
        }
        int n16 = n;
        int n18;
        int n17 = (n18 = n - 1) + n6;
        for (int l = 0; l < n2; ++l) {
            array2[n16] = -array[n17];
            array2[n16 - 1] = array[n18];
            n16 += n << 1;
            n17 += n;
            n18 += n;
        }
    }
    
    static void dradf4(final int n, final int n2, final float[] array, final float[] array2, final float[] array3, final int n3, final float[] array4, final int n4, final float[] array5, final int n5) {
        int n7;
        final int n6 = n7 = n2 * n;
        int n8 = n7 << 1;
        int n9 = n7 + (n7 << 1);
        int n10 = 0;
        for (int i = 0; i < n2; ++i) {
            final float n11 = array[n7] + array[n9];
            final float n12 = array[n10] + array[n8];
            final int n13;
            array2[n13 = n10 << 2] = n11 + n12;
            array2[(n << 2) + n13 - 1] = n12 - n11;
            final int n14;
            array2[(n14 = n13 + (n << 1)) - 1] = array[n10] - array[n8];
            array2[n14] = array[n9] - array[n7];
            n7 += n;
            n9 += n;
            n10 += n;
            n8 += n;
        }
        if (n < 2) {
            return;
        }
        if (n != 2) {
            int n15 = 0;
            for (int j = 0; j < n2; ++j) {
                int n16 = n15;
                int n17 = n15 << 2;
                final int n19;
                int n18 = (n19 = n << 1) + n17;
                for (int k = 2; k < n; k += 2) {
                    n16 += 2;
                    final int n20 = n16;
                    n17 += 2;
                    n18 -= 2;
                    final int n21 = n20 + n6;
                    final float n22 = array3[n3 + k - 2] * array[n21 - 1] + array3[n3 + k - 1] * array[n21];
                    final float n23 = array3[n3 + k - 2] * array[n21] - array3[n3 + k - 1] * array[n21 - 1];
                    final int n24 = n21 + n6;
                    final float n25 = array4[n4 + k - 2] * array[n24 - 1] + array4[n4 + k - 1] * array[n24];
                    final float n26 = array4[n4 + k - 2] * array[n24] - array4[n4 + k - 1] * array[n24 - 1];
                    final int n27 = n24 + n6;
                    final float n28 = array5[n5 + k - 2] * array[n27 - 1] + array5[n5 + k - 1] * array[n27];
                    final float n29 = array5[n5 + k - 2] * array[n27] - array5[n5 + k - 1] * array[n27 - 1];
                    final float n30 = n22 + n28;
                    final float n31 = n28 - n22;
                    final float n32 = n23 + n29;
                    final float n33 = n23 - n29;
                    final float n34 = array[n16] + n26;
                    final float n35 = array[n16] - n26;
                    final float n36 = array[n16 - 1] + n25;
                    final float n37 = array[n16 - 1] - n25;
                    array2[n17 - 1] = n30 + n36;
                    array2[n17] = n32 + n34;
                    array2[n18 - 1] = n37 - n33;
                    array2[n18] = n31 - n35;
                    array2[n17 + n19 - 1] = n33 + n37;
                    array2[n17 + n19] = n31 + n35;
                    array2[n18 + n19 - 1] = n36 - n30;
                    array2[n18 + n19] = n32 - n34;
                }
                n15 += n;
            }
            if ((n & 0x1) != 0x0) {
                return;
            }
        }
        int n39;
        int n38 = (n39 = n6 + n - 1) + (n6 << 1);
        final int n40 = n << 2;
        int n41 = n;
        final int n42 = n << 1;
        int n43 = n;
        for (int l = 0; l < n2; ++l) {
            final float n44 = -Drft.hsqt2 * (array[n39] + array[n38]);
            final float n45 = Drft.hsqt2 * (array[n39] - array[n38]);
            array2[n41 - 1] = n45 + array[n43 - 1];
            array2[n41 + n42 - 1] = array[n43 - 1] - n45;
            array2[n41] = n44 - array[n39 + n6];
            array2[n41 + n42] = n44 + array[n39 + n6];
            n39 += n;
            n38 += n;
            n41 += n40;
            n43 += n;
        }
    }
    
    static void dradfg(final int n, final int n2, final int n3, final int n4, final float[] array, final float[] array2, final float[] array3, final float[] array4, final float[] array5, final float[] array6, final int n5) {
        int n6 = 0;
        final float n7 = Drft.tpi / n2;
        final float n8 = (float)Math.cos(n7);
        final float n9 = (float)Math.sin(n7);
        final int n10 = n2 + 1 >> 1;
        final int n11 = n - 1 >> 1;
        final int n12 = n3 * n;
        final int n13 = n2 * n;
        int n14 = 100;
        while (true) {
            Label_0927: {
                switch (n14) {
                    case 101: {
                        if (n == 1) {
                            n14 = 119;
                            continue;
                        }
                        for (int i = 0; i < n4; ++i) {
                            array5[i] = array3[i];
                        }
                        int n15 = 0;
                        for (int j = 1; j < n2; ++j) {
                            int n16;
                            n15 = (n16 = n15 + n12);
                            for (int k = 0; k < n3; ++k) {
                                array4[n16] = array2[n16];
                                n16 += n;
                            }
                        }
                        int n17 = -n;
                        int n18 = 0;
                        if (n11 > n3) {
                            for (int l = 1; l < n2; ++l) {
                                n18 += n12;
                                n17 += n;
                                int n19 = -n + n18;
                                for (int n20 = 0; n20 < n3; ++n20) {
                                    int n21 = n17 - 1;
                                    int n22;
                                    n19 = (n22 = n19 + n);
                                    for (int n23 = 2; n23 < n; n23 += 2) {
                                        n21 += 2;
                                        n22 += 2;
                                        array4[n22 - 1] = array6[n5 + n21 - 1] * array2[n22 - 1] + array6[n5 + n21] * array2[n22];
                                        array4[n22] = array6[n5 + n21 - 1] * array2[n22] - array6[n5 + n21] * array2[n22 - 1];
                                    }
                                }
                            }
                        }
                        else {
                            for (int n24 = 1; n24 < n2; ++n24) {
                                n17 += n;
                                int n25 = n17 - 1;
                                int n26;
                                n18 = (n26 = n18 + n12);
                                for (int n27 = 2; n27 < n; n27 += 2) {
                                    n25 += 2;
                                    n26 += 2;
                                    int n28 = n26;
                                    for (int n29 = 0; n29 < n3; ++n29) {
                                        array4[n28 - 1] = array6[n5 + n25 - 1] * array2[n28 - 1] + array6[n5 + n25] * array2[n28];
                                        array4[n28] = array6[n5 + n25 - 1] * array2[n28] - array6[n5 + n25] * array2[n28 - 1];
                                        n28 += n;
                                    }
                                }
                            }
                        }
                        int n30 = 0;
                        int n31 = n2 * n12;
                        if (n11 < n3) {
                            for (int n32 = 1; n32 < n10; ++n32) {
                                n30 += n12;
                                n31 -= n12;
                                int n33 = n30;
                                int n34 = n31;
                                for (int n35 = 2; n35 < n; n35 += 2) {
                                    n33 += 2;
                                    n34 += 2;
                                    int n36 = n33 - n;
                                    int n37 = n34 - n;
                                    for (int n38 = 0; n38 < n3; ++n38) {
                                        n36 += n;
                                        n37 += n;
                                        array2[n36 - 1] = array4[n36 - 1] + array4[n37 - 1];
                                        array2[n37 - 1] = array4[n36] - array4[n37];
                                        array2[n36] = array4[n36] + array4[n37];
                                        array2[n37] = array4[n37 - 1] - array4[n36 - 1];
                                    }
                                }
                            }
                            break Label_0927;
                        }
                        for (int n39 = 1; n39 < n10; ++n39) {
                            n30 += n12;
                            n31 -= n12;
                            int n40 = n30;
                            int n41 = n31;
                            for (int n42 = 0; n42 < n3; ++n42) {
                                int n43 = n40;
                                int n44 = n41;
                                for (int n45 = 2; n45 < n; n45 += 2) {
                                    n43 += 2;
                                    n44 += 2;
                                    array2[n43 - 1] = array4[n43 - 1] + array4[n44 - 1];
                                    array2[n44 - 1] = array4[n43] - array4[n44];
                                    array2[n43] = array4[n43] + array4[n44];
                                    array2[n44] = array4[n44 - 1] - array4[n43 - 1];
                                }
                                n40 += n;
                                n41 += n;
                            }
                        }
                        break Label_0927;
                    }
                    case 119: {
                        for (int n46 = 0; n46 < n4; ++n46) {
                            array3[n46] = array5[n46];
                        }
                        int n47 = 0;
                        int n48 = n2 * n4;
                        for (int n49 = 1; n49 < n10; ++n49) {
                            n47 += n12;
                            n48 -= n12;
                            int n50 = n47 - n;
                            int n51 = n48 - n;
                            for (int n52 = 0; n52 < n3; ++n52) {
                                n50 += n;
                                n51 += n;
                                array2[n50] = array4[n50] + array4[n51];
                                array2[n51] = array4[n51] - array4[n50];
                            }
                        }
                        float n53 = 1.0f;
                        float n54 = 0.0f;
                        int n55 = 0;
                        n6 = n2 * n4;
                        final int n56 = (n2 - 1) * n4;
                        for (int n57 = 1; n57 < n10; ++n57) {
                            n55 += n4;
                            n6 -= n4;
                            final float n58 = n8 * n53 - n9 * n54;
                            n54 = n8 * n54 + n9 * n53;
                            n53 = n58;
                            int n59 = n55;
                            int n60 = n6;
                            int n61 = n56;
                            int n62 = n4;
                            for (int n63 = 0; n63 < n4; ++n63) {
                                array5[n59++] = array3[n63] + n53 * array3[n62++];
                                array5[n60++] = n54 * array3[n61++];
                            }
                            final float n64 = n53;
                            final float n65 = n54;
                            float n66 = n53;
                            float n67 = n54;
                            int n68 = n4;
                            int n69 = (n2 - 1) * n4;
                            for (int n70 = 2; n70 < n10; ++n70) {
                                n68 += n4;
                                n69 -= n4;
                                final float n71 = n64 * n66 - n65 * n67;
                                n67 = n64 * n67 + n65 * n66;
                                n66 = n71;
                                int n72 = n55;
                                int n73 = n6;
                                int n74 = n68;
                                int n75 = n69;
                                for (int n76 = 0; n76 < n4; ++n76) {
                                    final int n77 = n72++;
                                    array5[n77] += n66 * array3[n74++];
                                    final int n78 = n73++;
                                    array5[n78] += n67 * array3[n75++];
                                }
                            }
                        }
                        int n79 = 0;
                        for (int n80 = 1; n80 < n10; ++n80) {
                            n79 = (n6 = n79 + n4);
                            for (int n81 = 0; n81 < n4; ++n81) {
                                final int n82 = n81;
                                array5[n82] += array3[n6++];
                            }
                        }
                        if (n < n3) {
                            n14 = 132;
                            continue;
                        }
                        int n83 = 0;
                        n6 = 0;
                        for (int n84 = 0; n84 < n3; ++n84) {
                            int n85 = n83;
                            int n86 = n6;
                            for (int n87 = 0; n87 < n; ++n87) {
                                array[n86++] = array4[n85++];
                            }
                            n83 += n;
                            n6 += n13;
                        }
                        n14 = 135;
                        continue;
                    }
                    case 132: {
                        for (int n88 = 0; n88 < n; ++n88) {
                            int n89 = n88;
                            int n90 = n88;
                            for (int n91 = 0; n91 < n3; ++n91) {
                                array[n90] = array4[n89];
                                n89 += n;
                                n90 += n13;
                            }
                        }
                    }
                    case 135: {
                        int n92 = 0;
                        n6 = n << 1;
                        int n93 = 0;
                        int n94 = n2 * n12;
                        for (int n95 = 1; n95 < n10; ++n95) {
                            n92 += n6;
                            n93 += n12;
                            n94 -= n12;
                            int n96 = n92;
                            int n97 = n93;
                            int n98 = n94;
                            for (int n99 = 0; n99 < n3; ++n99) {
                                array[n96 - 1] = array4[n97];
                                array[n96] = array4[n98];
                                n96 += n13;
                                n97 += n;
                                n98 += n;
                            }
                        }
                        if (n == 1) {
                            return;
                        }
                        if (n11 < n3) {
                            n14 = 141;
                            continue;
                        }
                        int n100 = -n;
                        int n101 = 0;
                        int n102 = 0;
                        int n103 = n2 * n12;
                        for (int n104 = 1; n104 < n10; ++n104) {
                            n100 += n6;
                            n101 += n6;
                            n102 += n12;
                            n103 -= n12;
                            int n105 = n100;
                            int n106 = n101;
                            int n107 = n102;
                            int n108 = n103;
                            for (int n109 = 0; n109 < n3; ++n109) {
                                for (int n110 = 2; n110 < n; n110 += 2) {
                                    final int n111 = n - n110;
                                    array[n110 + n106 - 1] = array4[n110 + n107 - 1] + array4[n110 + n108 - 1];
                                    array[n111 + n105 - 1] = array4[n110 + n107 - 1] - array4[n110 + n108 - 1];
                                    array[n110 + n106] = array4[n110 + n107] + array4[n110 + n108];
                                    array[n111 + n105] = array4[n110 + n108] - array4[n110 + n107];
                                }
                                n105 += n13;
                                n106 += n13;
                                n107 += n;
                                n108 += n;
                            }
                        }
                    }
                    case 141: {
                        int n112 = -n;
                        int n113 = 0;
                        int n114 = 0;
                        int n115 = n2 * n12;
                        for (int n116 = 1; n116 < n10; ++n116) {
                            n112 += n6;
                            n113 += n6;
                            n114 += n12;
                            n115 -= n12;
                            for (int n117 = 2; n117 < n; n117 += 2) {
                                int n118 = n + n112 - n117;
                                int n119 = n117 + n113;
                                int n120 = n117 + n114;
                                int n121 = n117 + n115;
                                for (int n122 = 0; n122 < n3; ++n122) {
                                    array[n119 - 1] = array4[n120 - 1] + array4[n121 - 1];
                                    array[n118 - 1] = array4[n120 - 1] - array4[n121 - 1];
                                    array[n119] = array4[n120] + array4[n121];
                                    array[n118] = array4[n121] - array4[n120];
                                    n118 += n13;
                                    n119 += n13;
                                    n120 += n;
                                    n121 += n;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static void drftf1(final int n, final float[] array, final float[] array2, final float[] array3, final int[] array4) {
        final int n2 = array4[1];
        int n3 = 1;
        int n4 = n;
        int n5 = n;
        for (int i = 0; i < n2; ++i) {
            final int n6 = array4[n2 - i + 1];
            final int n7 = n4 / n6;
            final int n8 = n / n4;
            final int n9 = n8 * n7;
            n5 -= (n6 - 1) * n8;
            n3 = 1 - n3;
            int n10 = 100;
        Label_0373:
            while (true) {
                switch (n10) {
                    case 100: {
                        if (n6 != 4) {
                            n10 = 102;
                            continue;
                        }
                        final int n11 = n5 + n8;
                        final int n12 = n11 + n8;
                        if (n3 != 0) {
                            dradf4(n8, n7, array2, array, array3, n5 - 1, array3, n11 - 1, array3, n12 - 1);
                        }
                        else {
                            dradf4(n8, n7, array, array2, array3, n5 - 1, array3, n11 - 1, array3, n12 - 1);
                        }
                        n10 = 110;
                        continue;
                    }
                    case 102: {
                        if (n6 != 2) {
                            n10 = 104;
                            continue;
                        }
                        if (n3 != 0) {
                            n10 = 103;
                            continue;
                        }
                        dradf2(n8, n7, array, array2, array3, n5 - 1);
                        n10 = 110;
                        continue;
                    }
                    case 103: {
                        dradf2(n8, n7, array2, array, array3, n5 - 1);
                    }
                    case 104: {
                        if (n8 == 1) {
                            n3 = 1 - n3;
                        }
                        if (n3 != 0) {
                            n10 = 109;
                            continue;
                        }
                        dradfg(n8, n6, n7, n9, array, array, array, array2, array2, array3, n5 - 1);
                        n3 = 1;
                        n10 = 110;
                        continue;
                    }
                    case 109: {
                        dradfg(n8, n6, n7, n9, array2, array2, array2, array, array, array3, n5 - 1);
                        n3 = 0;
                    }
                    case 110: {
                        break Label_0373;
                    }
                }
            }
            n4 = n7;
        }
        if (n3 == 1) {
            return;
        }
        for (int j = 0; j < n; ++j) {
            array[j] = array2[j];
        }
    }
    
    static void dradb2(final int n, final int n2, final float[] array, final float[] array2, final float[] array3, final int n3) {
        final int n4 = n2 * n;
        int n5 = 0;
        int n6 = 0;
        final int n7 = (n << 1) - 1;
        for (int i = 0; i < n2; ++i) {
            array2[n5] = array[n6] + array[n7 + n6];
            array2[n5 + n4] = array[n6] - array[n7 + n6];
            n6 = (n5 += n) << 1;
        }
        if (n < 2) {
            return;
        }
        if (n != 2) {
            int n8 = 0;
            int n9 = 0;
            for (int j = 0; j < n2; ++j) {
                int n10 = n8;
                int n12;
                int n11 = (n12 = n9) + (n << 1);
                int n13 = n4 + n8;
                for (int k = 2; k < n; k += 2) {
                    n10 += 2;
                    n12 += 2;
                    n11 -= 2;
                    n13 += 2;
                    array2[n10 - 1] = array[n12 - 1] + array[n11 - 1];
                    final float n14 = array[n12 - 1] - array[n11 - 1];
                    array2[n10] = array[n12] - array[n11];
                    final float n15 = array[n12] + array[n11];
                    array2[n13 - 1] = array3[n3 + k - 2] * n14 - array3[n3 + k - 1] * n15;
                    array2[n13] = array3[n3 + k - 2] * n15 + array3[n3 + k - 1] * n14;
                }
                n9 = (n8 += n) << 1;
            }
            if (n % 2 == 1) {
                return;
            }
        }
        int n16 = n - 1;
        int n17 = n - 1;
        for (int l = 0; l < n2; ++l) {
            array2[n16] = array[n17] + array[n17];
            array2[n16 + n4] = -(array[n17 + 1] + array[n17 + 1]);
            n16 += n;
            n17 += n << 1;
        }
    }
    
    static void dradb3(final int n, final int n2, final float[] array, final float[] array2, final float[] array3, final int n3, final float[] array4, final int n4) {
        final int n5 = n2 * n;
        int n6 = 0;
        final int n7 = n5 << 1;
        int n8 = n << 1;
        final int n9 = n + (n << 1);
        int n10 = 0;
        for (int i = 0; i < n2; ++i) {
            final float n11 = array[n8 - 1] + array[n8 - 1];
            final float n12 = array[n10] + Drft.taur * n11;
            array2[n6] = array[n10] + n11;
            final float n13 = Drft.taui * (array[n8] + array[n8]);
            array2[n6 + n5] = n12 - n13;
            array2[n6 + n7] = n12 + n13;
            n6 += n;
            n8 += n9;
            n10 += n9;
        }
        if (n == 1) {
            return;
        }
        int n14 = 0;
        final int n15 = n << 1;
        for (int j = 0; j < n2; ++j) {
            int n16 = n14 + (n14 << 1);
            int n18;
            int n17 = n18 = n16 + n15;
            int n19 = n14;
            int n21;
            int n20 = (n21 = n14 + n5) + n5;
            for (int k = 2; k < n; k += 2) {
                n17 += 2;
                n18 -= 2;
                n16 += 2;
                n19 += 2;
                n21 += 2;
                n20 += 2;
                final float n22 = array[n17 - 1] + array[n18 - 1];
                final float n23 = array[n16 - 1] + Drft.taur * n22;
                array2[n19 - 1] = array[n16 - 1] + n22;
                final float n24 = array[n17] - array[n18];
                final float n25 = array[n16] + Drft.taur * n24;
                array2[n19] = array[n16] + n24;
                final float n26 = Drft.taui * (array[n17 - 1] - array[n18 - 1]);
                final float n27 = Drft.taui * (array[n17] + array[n18]);
                final float n28 = n23 - n27;
                final float n29 = n23 + n27;
                final float n30 = n25 + n26;
                final float n31 = n25 - n26;
                array2[n21 - 1] = array3[n3 + k - 2] * n28 - array3[n3 + k - 1] * n30;
                array2[n21] = array3[n3 + k - 2] * n30 + array3[n3 + k - 1] * n28;
                array2[n20 - 1] = array4[n4 + k - 2] * n29 - array4[n4 + k - 1] * n31;
                array2[n20] = array4[n4 + k - 2] * n31 + array4[n4 + k - 1] * n29;
            }
            n14 += n;
        }
    }
    
    static void dradb4(final int n, final int n2, final float[] array, final float[] array2, final float[] array3, final int n3, final float[] array4, final int n4, final float[] array5, final int n5) {
        final int n6 = n2 * n;
        int n7 = 0;
        final int n8 = n << 2;
        int n9 = 0;
        final int n10 = n << 1;
        for (int i = 0; i < n2; ++i) {
            final int n11 = n9 + n10;
            final int n12 = n7;
            final float n13 = array[n11 - 1] + array[n11 - 1];
            final float n14 = array[n11] + array[n11];
            final int n16;
            final float n15 = array[n9] - array[(n16 = n11 + n10) - 1];
            final float n17 = array[n9] + array[n16 - 1];
            array2[n12] = n17 + n13;
            final int n18;
            array2[n18 = n12 + n6] = n15 - n14;
            final int n19;
            array2[n19 = n18 + n6] = n17 - n13;
            array2[n19 + n6] = n15 + n14;
            n7 += n;
            n9 += n8;
        }
        if (n < 2) {
            return;
        }
        if (n != 2) {
            int n20 = 0;
            for (int j = 0; j < n2; ++j) {
                int n24;
                int n23;
                int n22;
                int n21 = (n22 = (n23 = (n24 = n20 << 2) + n10)) + n10;
                int n25 = n20;
                for (int k = 2; k < n; k += 2) {
                    n24 += 2;
                    n23 += 2;
                    n22 -= 2;
                    n21 -= 2;
                    n25 += 2;
                    final float n26 = array[n24] + array[n21];
                    final float n27 = array[n24] - array[n21];
                    final float n28 = array[n23] - array[n22];
                    final float n29 = array[n23] + array[n22];
                    final float n30 = array[n24 - 1] - array[n21 - 1];
                    final float n31 = array[n24 - 1] + array[n21 - 1];
                    final float n32 = array[n23 - 1] - array[n22 - 1];
                    final float n33 = array[n23 - 1] + array[n22 - 1];
                    array2[n25 - 1] = n31 + n33;
                    final float n34 = n31 - n33;
                    array2[n25] = n27 + n28;
                    final float n35 = n27 - n28;
                    final float n36 = n30 - n29;
                    final float n37 = n30 + n29;
                    final float n38 = n26 + n32;
                    final float n39 = n26 - n32;
                    final int n40;
                    array2[(n40 = n25 + n6) - 1] = array3[n3 + k - 2] * n36 - array3[n3 + k - 1] * n38;
                    array2[n40] = array3[n3 + k - 2] * n38 + array3[n3 + k - 1] * n36;
                    final int n41;
                    array2[(n41 = n40 + n6) - 1] = array4[n4 + k - 2] * n34 - array4[n4 + k - 1] * n35;
                    array2[n41] = array4[n4 + k - 2] * n35 + array4[n4 + k - 1] * n34;
                    final int n42;
                    array2[(n42 = n41 + n6) - 1] = array5[n5 + k - 2] * n37 - array5[n5 + k - 1] * n39;
                    array2[n42] = array5[n5 + k - 2] * n39 + array5[n5 + k - 1] * n37;
                }
                n20 += n;
            }
            if (n % 2 == 1) {
                return;
            }
        }
        int n43 = n;
        final int n44 = n << 2;
        int n45 = n - 1;
        int n46 = n + (n << 1);
        for (int l = 0; l < n2; ++l) {
            final int n47 = n45;
            final float n48 = array[n43] + array[n46];
            final float n49 = array[n46] - array[n43];
            final float n50 = array[n43 - 1] - array[n46 - 1];
            final float n51 = array[n43 - 1] + array[n46 - 1];
            array2[n47] = n51 + n51;
            final int n52;
            array2[n52 = n47 + n6] = Drft.sqrt2 * (n50 - n48);
            final int n53;
            array2[n53 = n52 + n6] = n49 + n49;
            array2[n53 + n6] = -Drft.sqrt2 * (n50 + n48);
            n45 += n;
            n43 += n44;
            n46 += n44;
        }
    }
    
    static void dradbg(final int n, final int n2, final int n3, final int n4, final float[] array, final float[] array2, final float[] array3, final float[] array4, final float[] array5, final float[] array6, final int n5) {
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        float n10 = 0.0f;
        float n11 = 0.0f;
        int n12 = 0;
        int n13 = 100;
        while (true) {
            switch (n13) {
                case 100: {
                    n8 = n2 * n;
                    n7 = n3 * n;
                    final float n14 = Drft.tpi / n2;
                    n10 = (float)Math.cos(n14);
                    n11 = (float)Math.sin(n14);
                    n9 = n - 1 >>> 1;
                    n12 = n2;
                    n6 = n2 + 1 >>> 1;
                    if (n < n3) {
                        n13 = 103;
                        continue;
                    }
                    int n15 = 0;
                    int n16 = 0;
                    for (int i = 0; i < n3; ++i) {
                        int n17 = n15;
                        int n18 = n16;
                        for (int j = 0; j < n; ++j) {
                            array4[n17] = array[n18];
                            ++n17;
                            ++n18;
                        }
                        n15 += n;
                        n16 += n8;
                    }
                    n13 = 106;
                    continue;
                }
                case 103: {
                    int n19 = 0;
                    for (int k = 0; k < n; ++k) {
                        int n20 = n19;
                        int n21 = n19;
                        for (int l = 0; l < n3; ++l) {
                            array4[n20] = array[n21];
                            n20 += n;
                            n21 += n8;
                        }
                        ++n19;
                    }
                }
                case 106: {
                    int n22 = 0;
                    int n23 = n12 * n7;
                    final int n25;
                    int n24 = n25 = n << 1;
                    for (int n26 = 1; n26 < n6; ++n26) {
                        n22 += n7;
                        n23 -= n7;
                        int n27 = n22;
                        int n28 = n23;
                        int n29 = n24;
                        for (int n30 = 0; n30 < n3; ++n30) {
                            array4[n27] = array[n29 - 1] + array[n29 - 1];
                            array4[n28] = array[n29] + array[n29];
                            n27 += n;
                            n28 += n;
                            n29 += n8;
                        }
                        n24 += n25;
                    }
                    if (n == 1) {
                        n13 = 116;
                        continue;
                    }
                    if (n9 < n3) {
                        n13 = 112;
                        continue;
                    }
                    int n31 = 0;
                    int n32 = n12 * n7;
                    int n33 = 0;
                    for (int n34 = 1; n34 < n6; ++n34) {
                        n31 += n7;
                        n32 -= n7;
                        int n35 = n31;
                        int n36 = n32;
                        int n37;
                        n33 = (n37 = n33 + (n << 1));
                        for (int n38 = 0; n38 < n3; ++n38) {
                            int n39 = n35;
                            int n40 = n36;
                            int n41 = n37;
                            int n42 = n37;
                            for (int n43 = 2; n43 < n; n43 += 2) {
                                n39 += 2;
                                n40 += 2;
                                n41 += 2;
                                n42 -= 2;
                                array4[n39 - 1] = array[n41 - 1] + array[n42 - 1];
                                array4[n40 - 1] = array[n41 - 1] - array[n42 - 1];
                                array4[n39] = array[n41] - array[n42];
                                array4[n40] = array[n41] + array[n42];
                            }
                            n35 += n;
                            n36 += n;
                            n37 += n8;
                        }
                    }
                    n13 = 116;
                    continue;
                }
                case 112: {
                    int n44 = 0;
                    int n45 = n12 * n7;
                    int n46 = 0;
                    for (int n47 = 1; n47 < n6; ++n47) {
                        n44 += n7;
                        n45 -= n7;
                        int n48 = n44;
                        int n49 = n45;
                        int n51;
                        int n50;
                        n46 = (n50 = (n51 = n46 + (n << 1)));
                        for (int n52 = 2; n52 < n; n52 += 2) {
                            n48 += 2;
                            n49 += 2;
                            n51 += 2;
                            n50 -= 2;
                            int n53 = n48;
                            int n54 = n49;
                            int n55 = n51;
                            int n56 = n50;
                            for (int n57 = 0; n57 < n3; ++n57) {
                                array4[n53 - 1] = array[n55 - 1] + array[n56 - 1];
                                array4[n54 - 1] = array[n55 - 1] - array[n56 - 1];
                                array4[n53] = array[n55] - array[n56];
                                array4[n54] = array[n55] + array[n56];
                                n53 += n;
                                n54 += n;
                                n55 += n8;
                                n56 += n8;
                            }
                        }
                    }
                }
                case 116: {
                    float n58 = 1.0f;
                    float n59 = 0.0f;
                    int n60 = 0;
                    final int n62;
                    int n61 = n62 = n12 * n4;
                    final int n63 = (n2 - 1) * n4;
                    for (int n64 = 1; n64 < n6; ++n64) {
                        n60 += n4;
                        n61 -= n4;
                        final float n65 = n10 * n58 - n11 * n59;
                        n59 = n10 * n59 + n11 * n58;
                        n58 = n65;
                        int n66 = n60;
                        int n67 = n61;
                        int n68 = 0;
                        int n69 = n4;
                        int n70 = n63;
                        for (int n71 = 0; n71 < n4; ++n71) {
                            array3[n66++] = array5[n68++] + n58 * array5[n69++];
                            array3[n67++] = n59 * array5[n70++];
                        }
                        final float n72 = n58;
                        final float n73 = n59;
                        float n74 = n58;
                        float n75 = n59;
                        int n76 = n4;
                        int n77 = n62 - n4;
                        for (int n78 = 2; n78 < n6; ++n78) {
                            n76 += n4;
                            n77 -= n4;
                            final float n79 = n72 * n74 - n73 * n75;
                            n75 = n72 * n75 + n73 * n74;
                            n74 = n79;
                            int n80 = n60;
                            int n81 = n61;
                            int n82 = n76;
                            int n83 = n77;
                            for (int n84 = 0; n84 < n4; ++n84) {
                                final int n85 = n80++;
                                array3[n85] += n74 * array5[n82++];
                                final int n86 = n81++;
                                array3[n86] += n75 * array5[n83++];
                            }
                        }
                    }
                    int n87 = 0;
                    for (int n88 = 1; n88 < n6; ++n88) {
                        int n89;
                        n87 = (n89 = n87 + n4);
                        for (int n90 = 0; n90 < n4; ++n90) {
                            final int n91 = n90;
                            array5[n91] += array5[n89++];
                        }
                    }
                    int n92 = 0;
                    int n93 = n12 * n7;
                    for (int n94 = 1; n94 < n6; ++n94) {
                        n92 += n7;
                        n93 -= n7;
                        int n95 = n92;
                        int n96 = n93;
                        for (int n97 = 0; n97 < n3; ++n97) {
                            array4[n95] = array2[n95] - array2[n96];
                            array4[n96] = array2[n95] + array2[n96];
                            n95 += n;
                            n96 += n;
                        }
                    }
                    if (n == 1) {
                        n13 = 132;
                        continue;
                    }
                    if (n9 < n3) {
                        n13 = 128;
                        continue;
                    }
                    int n98 = 0;
                    int n99 = n12 * n7;
                    for (int n100 = 1; n100 < n6; ++n100) {
                        n98 += n7;
                        n99 -= n7;
                        int n101 = n98;
                        int n102 = n99;
                        for (int n103 = 0; n103 < n3; ++n103) {
                            int n104 = n101;
                            int n105 = n102;
                            for (int n106 = 2; n106 < n; n106 += 2) {
                                n104 += 2;
                                n105 += 2;
                                array4[n104 - 1] = array2[n104 - 1] - array2[n105];
                                array4[n105 - 1] = array2[n104 - 1] + array2[n105];
                                array4[n104] = array2[n104] + array2[n105 - 1];
                                array4[n105] = array2[n104] - array2[n105 - 1];
                            }
                            n101 += n;
                            n102 += n;
                        }
                    }
                    n13 = 132;
                    continue;
                }
                case 128: {
                    int n107 = 0;
                    int n108 = n12 * n7;
                    for (int n109 = 1; n109 < n6; ++n109) {
                        n107 += n7;
                        n108 -= n7;
                        int n110 = n107;
                        int n111 = n108;
                        for (int n112 = 2; n112 < n; n112 += 2) {
                            n110 += 2;
                            n111 += 2;
                            int n113 = n110;
                            int n114 = n111;
                            for (int n115 = 0; n115 < n3; ++n115) {
                                array4[n113 - 1] = array2[n113 - 1] - array2[n114];
                                array4[n114 - 1] = array2[n113 - 1] + array2[n114];
                                array4[n113] = array2[n113] + array2[n114 - 1];
                                array4[n114] = array2[n113] - array2[n114 - 1];
                                n113 += n;
                                n114 += n;
                            }
                        }
                    }
                }
                case 132: {
                    if (n == 1) {
                        return;
                    }
                    for (int n116 = 0; n116 < n4; ++n116) {
                        array3[n116] = array5[n116];
                    }
                    int n117 = 0;
                    for (int n118 = 1; n118 < n2; ++n118) {
                        int n119;
                        n117 = (n119 = n117 + n7);
                        for (int n120 = 0; n120 < n3; ++n120) {
                            array2[n119] = array4[n119];
                            n119 += n;
                        }
                    }
                    if (n9 > n3) {
                        n13 = 139;
                        continue;
                    }
                    int n121 = -n - 1;
                    int n122 = 0;
                    for (int n123 = 1; n123 < n2; ++n123) {
                        n121 += n;
                        n122 += n7;
                        int n124 = n121;
                        int n125 = n122;
                        for (int n126 = 2; n126 < n; n126 += 2) {
                            n125 += 2;
                            n124 += 2;
                            int n127 = n125;
                            for (int n128 = 0; n128 < n3; ++n128) {
                                array2[n127 - 1] = array6[n5 + n124 - 1] * array4[n127 - 1] - array6[n5 + n124] * array4[n127];
                                array2[n127] = array6[n5 + n124 - 1] * array4[n127] + array6[n5 + n124] * array4[n127 - 1];
                                n127 += n;
                            }
                        }
                    }
                }
                case 139: {
                    int n129 = -n - 1;
                    int n130 = 0;
                    for (int n131 = 1; n131 < n2; ++n131) {
                        n129 += n;
                        int n132;
                        n130 = (n132 = n130 + n7);
                        for (int n133 = 0; n133 < n3; ++n133) {
                            int n134 = n129;
                            int n135 = n132;
                            for (int n136 = 2; n136 < n; n136 += 2) {
                                n134 += 2;
                                n135 += 2;
                                array2[n135 - 1] = array6[n5 + n134 - 1] * array4[n135 - 1] - array6[n5 + n134] * array4[n135];
                                array2[n135] = array6[n5 + n134 - 1] * array4[n135] + array6[n5 + n134] * array4[n135 - 1];
                            }
                            n132 += n;
                        }
                    }
                }
            }
        }
    }
    
    static void drftb1(final int n, final float[] array, final float[] array2, final float[] array3, final int n2, final int[] array4) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        final int n7 = array4[1];
        int n8 = 0;
        int n9 = 1;
        int n10 = 1;
        for (int i = 0; i < n7; ++i) {
            int n11 = 100;
        Label_0462:
            while (true) {
                switch (n11) {
                    case 100: {
                        n4 = array4[i + 2];
                        n3 = n4 * n9;
                        n5 = n / n3;
                        n6 = n5 * n9;
                        if (n4 != 4) {
                            n11 = 103;
                            continue;
                        }
                        final int n12 = n10 + n5;
                        final int n13 = n12 + n5;
                        if (n8 != 0) {
                            dradb4(n5, n9, array2, array, array3, n2 + n10 - 1, array3, n2 + n12 - 1, array3, n2 + n13 - 1);
                        }
                        else {
                            dradb4(n5, n9, array, array2, array3, n2 + n10 - 1, array3, n2 + n12 - 1, array3, n2 + n13 - 1);
                        }
                        n8 = 1 - n8;
                        n11 = 115;
                        continue;
                    }
                    case 103: {
                        if (n4 != 2) {
                            n11 = 106;
                            continue;
                        }
                        if (n8 != 0) {
                            dradb2(n5, n9, array2, array, array3, n2 + n10 - 1);
                        }
                        else {
                            dradb2(n5, n9, array, array2, array3, n2 + n10 - 1);
                        }
                        n8 = 1 - n8;
                        n11 = 115;
                        continue;
                    }
                    case 106: {
                        if (n4 != 3) {
                            n11 = 109;
                            continue;
                        }
                        final int n14 = n10 + n5;
                        if (n8 != 0) {
                            dradb3(n5, n9, array2, array, array3, n2 + n10 - 1, array3, n2 + n14 - 1);
                        }
                        else {
                            dradb3(n5, n9, array, array2, array3, n2 + n10 - 1, array3, n2 + n14 - 1);
                        }
                        n8 = 1 - n8;
                        n11 = 115;
                        continue;
                    }
                    case 109: {
                        if (n8 != 0) {
                            dradbg(n5, n4, n9, n6, array2, array2, array2, array, array, array3, n2 + n10 - 1);
                        }
                        else {
                            dradbg(n5, n4, n9, n6, array, array, array, array2, array2, array3, n2 + n10 - 1);
                        }
                        if (n5 == 1) {
                            n8 = 1 - n8;
                            break Label_0462;
                        }
                        break Label_0462;
                    }
                    case 115: {
                        break Label_0462;
                    }
                }
            }
            n9 = n3;
            n10 += (n4 - 1) * n5;
        }
        if (n8 == 0) {
            return;
        }
        for (int j = 0; j < n; ++j) {
            array[j] = array2[j];
        }
    }
    
    static {
        Drft.ntryh = new int[] { 4, 2, 3, 5 };
        Drft.tpi = 6.2831855f;
        Drft.hsqt2 = 0.70710677f;
        Drft.taui = 0.8660254f;
        Drft.taur = -0.5f;
        Drft.sqrt2 = 1.4142135f;
    }
}
