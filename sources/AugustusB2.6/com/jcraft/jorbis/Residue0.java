// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0 extends FuncResidue
{
    static int[][][] partword;
    
    void pack(final Object o, final Buffer buffer) {
        final InfoResidue0 infoResidue0 = (InfoResidue0)o;
        int n = 0;
        buffer.write(infoResidue0.begin, 24);
        buffer.write(infoResidue0.end, 24);
        buffer.write(infoResidue0.grouping - 1, 24);
        buffer.write(infoResidue0.partitions - 1, 6);
        buffer.write(infoResidue0.groupbook, 8);
        for (int i = 0; i < infoResidue0.partitions; ++i) {
            if (ilog(infoResidue0.secondstages[i]) > 3) {
                buffer.write(infoResidue0.secondstages[i], 3);
                buffer.write(1, 1);
                buffer.write(infoResidue0.secondstages[i] >>> 3, 5);
            }
            else {
                buffer.write(infoResidue0.secondstages[i], 4);
            }
            n += icount(infoResidue0.secondstages[i]);
        }
        for (int j = 0; j < n; ++j) {
            buffer.write(infoResidue0.booklist[j], 8);
        }
    }
    
    Object unpack(final Info info, final Buffer buffer) {
        int n = 0;
        final InfoResidue0 infoResidue0 = new InfoResidue0();
        infoResidue0.begin = buffer.read(24);
        infoResidue0.end = buffer.read(24);
        infoResidue0.grouping = buffer.read(24) + 1;
        infoResidue0.partitions = buffer.read(6) + 1;
        infoResidue0.groupbook = buffer.read(8);
        for (int i = 0; i < infoResidue0.partitions; ++i) {
            int read = buffer.read(3);
            if (buffer.read(1) != 0) {
                read |= buffer.read(5) << 3;
            }
            infoResidue0.secondstages[i] = read;
            n += icount(read);
        }
        for (int j = 0; j < n; ++j) {
            infoResidue0.booklist[j] = buffer.read(8);
        }
        if (infoResidue0.groupbook >= info.books) {
            this.free_info(infoResidue0);
            return null;
        }
        for (int k = 0; k < n; ++k) {
            if (infoResidue0.booklist[k] >= info.books) {
                this.free_info(infoResidue0);
                return null;
            }
        }
        return infoResidue0;
    }
    
    Object look(final DspState dspState, final InfoMode infoMode, final Object o) {
        final InfoResidue0 info = (InfoResidue0)o;
        final LookResidue0 lookResidue0 = new LookResidue0();
        int n = 0;
        int stages = 0;
        lookResidue0.info = info;
        lookResidue0.map = infoMode.mapping;
        lookResidue0.parts = info.partitions;
        lookResidue0.fullbooks = dspState.fullbooks;
        lookResidue0.phrasebook = dspState.fullbooks[info.groupbook];
        final int dim = lookResidue0.phrasebook.dim;
        lookResidue0.partbooks = new int[lookResidue0.parts][];
        for (int i = 0; i < lookResidue0.parts; ++i) {
            final int ilog = ilog(info.secondstages[i]);
            if (ilog != 0) {
                if (ilog > stages) {
                    stages = ilog;
                }
                lookResidue0.partbooks[i] = new int[ilog];
                for (int j = 0; j < ilog; ++j) {
                    if ((info.secondstages[i] & 1 << j) != 0x0) {
                        lookResidue0.partbooks[i][j] = info.booklist[n++];
                    }
                }
            }
        }
        lookResidue0.partvals = (int)Math.rint(Math.pow(lookResidue0.parts, dim));
        lookResidue0.stages = stages;
        lookResidue0.decodemap = new int[lookResidue0.partvals][];
        for (int k = 0; k < lookResidue0.partvals; ++k) {
            int n2 = k;
            int n3 = lookResidue0.partvals / lookResidue0.parts;
            lookResidue0.decodemap[k] = new int[dim];
            for (int l = 0; l < dim; ++l) {
                final int n4 = n2 / n3;
                n2 -= n4 * n3;
                n3 /= lookResidue0.parts;
                lookResidue0.decodemap[k][l] = n4;
            }
        }
        return lookResidue0;
    }
    
    void free_info(final Object o) {
    }
    
    void free_look(final Object o) {
    }
    
    int forward(final Block block, final Object o, final float[][] array, final int n) {
        System.err.println("Residue0.forward: not implemented");
        return 0;
    }
    
    static synchronized int _01inverse(final Block block, final Object o, final float[][] array, final int n, final int n2) {
        final LookResidue0 lookResidue0 = (LookResidue0)o;
        final InfoResidue0 info = lookResidue0.info;
        final int grouping = info.grouping;
        final int dim = lookResidue0.phrasebook.dim;
        final int n3 = (info.end - info.begin) / grouping;
        final int n4 = (n3 + dim - 1) / dim;
        if (Residue0.partword.length < n) {
            Residue0.partword = new int[n][][];
            for (int i = 0; i < n; ++i) {
                Residue0.partword[i] = new int[n4][];
            }
        }
        else {
            for (int j = 0; j < n; ++j) {
                if (Residue0.partword[j] == null || Residue0.partword[j].length < n4) {
                    Residue0.partword[j] = new int[n4][];
                }
            }
        }
        for (int k = 0; k < lookResidue0.stages; ++k) {
            int l = 0;
            int n5 = 0;
            while (l < n3) {
                if (k == 0) {
                    for (int n6 = 0; n6 < n; ++n6) {
                        final int decode = lookResidue0.phrasebook.decode(block.opb);
                        if (decode == -1) {
                            return 0;
                        }
                        Residue0.partword[n6][n5] = lookResidue0.decodemap[decode];
                        if (Residue0.partword[n6][n5] == null) {
                            return 0;
                        }
                    }
                }
                for (int n7 = 0; n7 < dim && l < n3; ++n7, ++l) {
                    for (int n8 = 0; n8 < n; ++n8) {
                        final int n9 = info.begin + l * grouping;
                        if ((info.secondstages[Residue0.partword[n8][n5][n7]] & 1 << k) != 0x0) {
                            final CodeBook codeBook = lookResidue0.fullbooks[lookResidue0.partbooks[Residue0.partword[n8][n5][n7]][k]];
                            if (codeBook != null) {
                                if (n2 == 0) {
                                    if (codeBook.decodevs_add(array[n8], n9, block.opb, grouping) == -1) {
                                        return 0;
                                    }
                                }
                                else if (n2 == 1 && codeBook.decodev_add(array[n8], n9, block.opb, grouping) == -1) {
                                    return 0;
                                }
                            }
                        }
                    }
                }
                ++n5;
            }
        }
        return 0;
    }
    
    static int _2inverse(final Block block, final Object o, final float[][] array, final int n) {
        final LookResidue0 lookResidue0 = (LookResidue0)o;
        final InfoResidue0 info = lookResidue0.info;
        final int grouping = info.grouping;
        final int dim = lookResidue0.phrasebook.dim;
        final int n2 = (info.end - info.begin) / grouping;
        final int[][] array2 = new int[(n2 + dim - 1) / dim][];
        for (int i = 0; i < lookResidue0.stages; ++i) {
            int j = 0;
            int n3 = 0;
            while (j < n2) {
                if (i == 0) {
                    final int decode = lookResidue0.phrasebook.decode(block.opb);
                    if (decode == -1) {
                        return 0;
                    }
                    array2[n3] = lookResidue0.decodemap[decode];
                    if (array2[n3] == null) {
                        return 0;
                    }
                }
                for (int n4 = 0; n4 < dim && j < n2; ++n4, ++j) {
                    final int n5 = info.begin + j * grouping;
                    if ((info.secondstages[array2[n3][n4]] & 1 << i) != 0x0) {
                        final CodeBook codeBook = lookResidue0.fullbooks[lookResidue0.partbooks[array2[n3][n4]][i]];
                        if (codeBook != null && codeBook.decodevv_add(array, n5, n, block.opb, grouping) == -1) {
                            return 0;
                        }
                    }
                }
                ++n3;
            }
        }
        return 0;
    }
    
    int inverse(final Block block, final Object o, final float[][] array, final int[] array2, final int n) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (array2[i] != 0) {
                array[n2++] = array[i];
            }
        }
        if (n2 != 0) {
            return _01inverse(block, o, array, n2, 0);
        }
        return 0;
    }
    
    private static int ilog(int i) {
        int n = 0;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    private static int icount(int i) {
        int n = 0;
        while (i != 0) {
            n += (i & 0x1);
            i >>>= 1;
        }
        return n;
    }
    
    static {
        Residue0.partword = new int[2][][];
    }
}
