/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BidiClassifier;
import com.ibm.icu.text.BidiLine;
import com.ibm.icu.text.BidiRun;
import com.ibm.icu.text.BidiWriter;
import com.ibm.icu.text.UTF16;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.lang.reflect.Array;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;

public class Bidi {
    public static final byte LEVEL_DEFAULT_LTR = 126;
    public static final byte LEVEL_DEFAULT_RTL = 127;
    public static final byte MAX_EXPLICIT_LEVEL = 125;
    public static final byte LEVEL_OVERRIDE = -128;
    public static final int MAP_NOWHERE = -1;
    public static final byte LTR = 0;
    public static final byte RTL = 1;
    public static final byte MIXED = 2;
    public static final byte NEUTRAL = 3;
    public static final short KEEP_BASE_COMBINING = 1;
    public static final short DO_MIRRORING = 2;
    public static final short INSERT_LRM_FOR_NUMERIC = 4;
    public static final short REMOVE_BIDI_CONTROLS = 8;
    public static final short OUTPUT_REVERSE = 16;
    public static final short REORDER_DEFAULT = 0;
    public static final short REORDER_NUMBERS_SPECIAL = 1;
    public static final short REORDER_GROUP_NUMBERS_WITH_R = 2;
    public static final short REORDER_RUNS_ONLY = 3;
    public static final short REORDER_INVERSE_NUMBERS_AS_L = 4;
    public static final short REORDER_INVERSE_LIKE_DIRECT = 5;
    public static final short REORDER_INVERSE_FOR_NUMBERS_SPECIAL = 6;
    static final short REORDER_COUNT = 7;
    static final short REORDER_LAST_LOGICAL_TO_VISUAL = 1;
    public static final int OPTION_DEFAULT = 0;
    public static final int OPTION_INSERT_MARKS = 1;
    public static final int OPTION_REMOVE_CONTROLS = 2;
    public static final int OPTION_STREAMING = 4;
    static final byte L = 0;
    static final byte R = 1;
    static final byte EN = 2;
    static final byte ES = 3;
    static final byte ET = 4;
    static final byte AN = 5;
    static final byte CS = 6;
    static final byte B = 7;
    static final byte S = 8;
    static final byte WS = 9;
    static final byte ON = 10;
    static final byte LRE = 11;
    static final byte LRO = 12;
    static final byte AL = 13;
    static final byte RLE = 14;
    static final byte RLO = 15;
    static final byte PDF = 16;
    static final byte NSM = 17;
    static final byte BN = 18;
    static final byte FSI = 19;
    static final byte LRI = 20;
    static final byte RLI = 21;
    static final byte PDI = 22;
    static final byte ENL = 23;
    static final byte ENR = 24;
    @Deprecated
    public static final int CLASS_DEFAULT = 23;
    static final int SIMPLE_PARAS_COUNT = 10;
    static final int SIMPLE_OPENINGS_COUNT = 20;
    private static final char CR = '\r';
    private static final char LF = '\n';
    static final int LRM_BEFORE = 1;
    static final int LRM_AFTER = 2;
    static final int RLM_BEFORE = 4;
    static final int RLM_AFTER = 8;
    static final byte FOUND_L = (byte)Bidi.DirPropFlag((byte)0);
    static final byte FOUND_R = (byte)Bidi.DirPropFlag((byte)1);
    static final int ISOLATE = 256;
    Bidi paraBidi;
    final UBiDiProps bdp;
    char[] text;
    int originalLength;
    int length;
    int resultLength;
    boolean mayAllocateText;
    boolean mayAllocateRuns;
    byte[] dirPropsMemory = new byte[1];
    byte[] levelsMemory = new byte[1];
    byte[] dirProps;
    byte[] levels;
    boolean isInverse;
    int reorderingMode;
    int reorderingOptions;
    boolean orderParagraphsLTR;
    byte paraLevel;
    byte defaultParaLevel;
    String prologue;
    String epilogue;
    ImpTabPair impTabPair;
    byte direction;
    int flags;
    int lastArabicPos;
    int trailingWSStart;
    int paraCount;
    int[] paras_limit = new int[10];
    byte[] paras_level = new byte[10];
    int runCount;
    BidiRun[] runsMemory = new BidiRun[0];
    BidiRun[] runs;
    BidiRun[] simpleRuns = new BidiRun[]{new BidiRun()};
    Isolate[] isolates;
    int isolateCount;
    int[] logicalToVisualRunsMap;
    boolean isGoodLogicalToVisualRunsMap;
    BidiClassifier customClassifier = null;
    InsertPoints insertPoints = new InsertPoints();
    int controlCount;
    static final int DirPropFlagMultiRuns = Bidi.DirPropFlag((byte)31);
    static final int[] DirPropFlagLR = new int[]{Bidi.DirPropFlag((byte)0), Bidi.DirPropFlag((byte)1)};
    static final int[] DirPropFlagE = new int[]{Bidi.DirPropFlag((byte)11), Bidi.DirPropFlag((byte)14)};
    static final int[] DirPropFlagO = new int[]{Bidi.DirPropFlag((byte)12), Bidi.DirPropFlag((byte)15)};
    static final int MASK_LTR = Bidi.DirPropFlag((byte)0) | Bidi.DirPropFlag((byte)2) | Bidi.DirPropFlag((byte)23) | Bidi.DirPropFlag((byte)24) | Bidi.DirPropFlag((byte)5) | Bidi.DirPropFlag((byte)11) | Bidi.DirPropFlag((byte)12) | Bidi.DirPropFlag((byte)20);
    static final int MASK_RTL = Bidi.DirPropFlag((byte)1) | Bidi.DirPropFlag((byte)13) | Bidi.DirPropFlag((byte)14) | Bidi.DirPropFlag((byte)15) | Bidi.DirPropFlag((byte)21);
    static final int MASK_R_AL = Bidi.DirPropFlag((byte)1) | Bidi.DirPropFlag((byte)13);
    static final int MASK_STRONG_EN_AN = Bidi.DirPropFlag((byte)0) | Bidi.DirPropFlag((byte)1) | Bidi.DirPropFlag((byte)13) | Bidi.DirPropFlag((byte)2) | Bidi.DirPropFlag((byte)5);
    static final int MASK_EXPLICIT = Bidi.DirPropFlag((byte)11) | Bidi.DirPropFlag((byte)12) | Bidi.DirPropFlag((byte)14) | Bidi.DirPropFlag((byte)15) | Bidi.DirPropFlag((byte)16);
    static final int MASK_BN_EXPLICIT = Bidi.DirPropFlag((byte)18) | MASK_EXPLICIT;
    static final int MASK_ISO = Bidi.DirPropFlag((byte)20) | Bidi.DirPropFlag((byte)21) | Bidi.DirPropFlag((byte)19) | Bidi.DirPropFlag((byte)22);
    static final int MASK_B_S = Bidi.DirPropFlag((byte)7) | Bidi.DirPropFlag((byte)8);
    static final int MASK_WS = MASK_B_S | Bidi.DirPropFlag((byte)9) | MASK_BN_EXPLICIT | MASK_ISO;
    static final int MASK_POSSIBLE_N = Bidi.DirPropFlag((byte)10) | Bidi.DirPropFlag((byte)6) | Bidi.DirPropFlag((byte)3) | Bidi.DirPropFlag((byte)4) | MASK_WS;
    static final int MASK_EMBEDDING = Bidi.DirPropFlag((byte)17) | MASK_POSSIBLE_N;
    static final int NOT_SEEKING_STRONG = 0;
    static final int SEEKING_STRONG_FOR_PARA = 1;
    static final int SEEKING_STRONG_FOR_FSI = 2;
    static final int LOOKING_FOR_PDI = 3;
    private static final int IMPTABPROPS_COLUMNS = 16;
    private static final int IMPTABPROPS_RES = 15;
    private static final short[] groupProp = new short[]{0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10, 4, 4, 4, 4, 13, 14};
    private static final short _L = 0;
    private static final short _R = 1;
    private static final short _EN = 2;
    private static final short _AN = 3;
    private static final short _ON = 4;
    private static final short _S = 5;
    private static final short _B = 6;
    private static final short[][] impTabProps = new short[][]{{1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 18, 21, 4}, {1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 50, 53, 0}, {33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 50, 53, 1}, {33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 50, 53, 1}, {33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 18, 21, 2}, {33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 50, 53, 3}, {33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 18, 21, 3}, {33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 50, 53, 4}, {33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 50, 53, 4}, {33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 18, 21, 4}, {97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 18, 21, 2}, {33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 18, 21, 2}, {97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 114, 117, 3}, {97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 18, 21, 3}, {33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 146, 149, 4}, {33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 50, 53, 5}, {33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 50, 53, 5}, {33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 50, 53, 6}, {33, 34, 18, 37, 39, 47, 49, 83, 20, 83, 18, 18, 35, 18, 21, 0}, {97, 98, 18, 101, 135, 111, 113, 135, 142, 135, 19, 135, 99, 18, 21, 0}, {33, 34, 18, 37, 39, 47, 49, 39, 20, 39, 20, 20, 35, 18, 21, 0}, {33, 34, 21, 37, 39, 47, 49, 86, 23, 86, 21, 21, 35, 18, 21, 3}, {97, 98, 21, 101, 135, 111, 113, 135, 142, 135, 22, 135, 99, 18, 21, 3}, {33, 34, 21, 37, 39, 47, 49, 39, 23, 39, 23, 23, 35, 18, 21, 3}};
    private static final int IMPTABLEVELS_COLUMNS = 8;
    private static final int IMPTABLEVELS_RES = 7;
    private static final byte[][] impTabL_DEFAULT = new byte[][]{{0, 1, 0, 2, 0, 0, 0, 0}, {0, 1, 3, 3, 20, 20, 0, 1}, {0, 1, 0, 2, 21, 21, 0, 2}, {0, 1, 3, 3, 20, 20, 0, 2}, {0, 33, 51, 51, 4, 4, 0, 0}, {0, 33, 0, 50, 5, 5, 0, 0}};
    private static final byte[][] impTabR_DEFAULT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 3, 20, 20, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {1, 0, 1, 3, 5, 5, 0, 1}, {33, 0, 33, 3, 4, 4, 0, 0}, {1, 0, 1, 3, 5, 5, 0, 0}};
    private static final short[] impAct0 = new short[]{0, 1, 2, 3, 4};
    private static final ImpTabPair impTab_DEFAULT = new ImpTabPair(impTabL_DEFAULT, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_NUMBERS_SPECIAL = new byte[][]{{0, 2, 17, 17, 0, 0, 0, 0}, {0, 66, 1, 1, 0, 0, 0, 0}, {0, 2, 4, 4, 19, 19, 0, 1}, {0, 34, 52, 52, 3, 3, 0, 0}, {0, 2, 4, 4, 19, 19, 0, 2}};
    private static final ImpTabPair impTab_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R = new byte[][]{{0, 3, 17, 17, 0, 0, 0, 0}, {32, 3, 1, 1, 2, 32, 32, 2}, {32, 3, 1, 1, 2, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 1}, {32, 3, 5, 5, 4, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 2}};
    private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R = new byte[][]{{2, 0, 1, 1, 0, 0, 0, 0}, {2, 0, 1, 1, 0, 0, 0, 1}, {2, 0, 20, 20, 19, 0, 0, 1}, {34, 0, 4, 4, 3, 0, 0, 0}, {34, 0, 4, 4, 3, 0, 0, 1}};
    private static final ImpTabPair impTab_GROUP_NUMBERS_WITH_R = new ImpTabPair(impTabL_GROUP_NUMBERS_WITH_R, impTabR_GROUP_NUMBERS_WITH_R, impAct0, impAct0);
    private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L = new byte[][]{{0, 1, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 20, 20, 0, 1}, {0, 1, 0, 0, 21, 21, 0, 2}, {0, 1, 0, 0, 20, 20, 0, 2}, {32, 1, 32, 32, 4, 4, 32, 1}, {32, 1, 32, 32, 5, 5, 32, 1}};
    private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L = new byte[][]{{1, 0, 1, 1, 0, 0, 0, 0}, {1, 0, 1, 1, 20, 20, 0, 1}, {1, 0, 1, 1, 0, 0, 0, 1}, {1, 0, 1, 1, 5, 5, 0, 1}, {33, 0, 33, 33, 4, 4, 0, 0}, {1, 0, 1, 1, 5, 5, 0, 0}};
    private static final ImpTabPair impTab_INVERSE_NUMBERS_AS_L = new ImpTabPair(impTabL_INVERSE_NUMBERS_AS_L, impTabR_INVERSE_NUMBERS_AS_L, impAct0, impAct0);
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 2, 19, 19, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {33, 48, 6, 4, 3, 3, 48, 0}, {33, 48, 6, 4, 5, 5, 48, 3}, {33, 48, 6, 4, 5, 5, 48, 2}, {33, 48, 6, 4, 3, 3, 48, 1}};
    private static final short[] impAct1 = new short[]{0, 1, 13, 14};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT = new ImpTabPair(impTabL_DEFAULT, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{0, 99, 0, 1, 0, 0, 0, 0}, {0, 99, 0, 1, 18, 48, 0, 4}, {32, 99, 32, 1, 2, 48, 32, 3}, {0, 99, 85, 86, 20, 48, 0, 3}, {48, 67, 85, 86, 4, 48, 48, 3}, {48, 67, 5, 86, 20, 48, 48, 4}, {48, 67, 85, 6, 20, 48, 48, 4}};
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{19, 0, 1, 1, 0, 0, 0, 0}, {35, 0, 1, 1, 2, 64, 0, 1}, {35, 0, 1, 1, 2, 64, 0, 0}, {3, 0, 3, 54, 20, 64, 0, 1}, {83, 64, 5, 54, 4, 64, 64, 0}, {83, 64, 5, 54, 4, 64, 64, 1}, {83, 64, 6, 6, 4, 64, 64, 3}};
    private static final short[] impAct2 = new short[]{0, 1, 2, 5, 6, 7, 8};
    private static final short[] impAct3 = new short[]{0, 1, 9, 10, 11, 12};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct2, impAct3);
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new byte[][]{{0, 98, 1, 1, 0, 0, 0, 0}, {0, 98, 1, 1, 0, 48, 0, 4}, {0, 98, 84, 84, 19, 48, 0, 3}, {48, 66, 84, 84, 3, 48, 48, 3}, {48, 66, 4, 4, 19, 48, 48, 4}};
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct2, impAct3);
    static final int FIRSTALLOC = 10;
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
    public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;

    static int DirPropFlag(byte dir) {
        return 1 << dir;
    }

    boolean testDirPropFlagAt(int flag, int index) {
        return (Bidi.DirPropFlag(this.dirProps[index]) & flag) != 0;
    }

    static final int DirPropFlagLR(byte level) {
        return DirPropFlagLR[level & 1];
    }

    static final int DirPropFlagE(byte level) {
        return DirPropFlagE[level & 1];
    }

    static final int DirPropFlagO(byte level) {
        return DirPropFlagO[level & 1];
    }

    static final byte DirFromStrong(byte strong) {
        return strong == 0 ? (byte)0 : 1;
    }

    static final byte NoOverride(byte level) {
        return (byte)(level & 0x7F);
    }

    static byte GetLRFromLevel(byte level) {
        return (byte)(level & 1);
    }

    static boolean IsDefaultLevel(byte level) {
        return (level & 0x7E) == 126;
    }

    static boolean IsBidiControlChar(int c2) {
        return (c2 & 0xFFFFFFFC) == 8204 || c2 >= 8234 && c2 <= 8238 || c2 >= 8294 && c2 <= 8297;
    }

    void verifyValidPara() {
        if (this != this.paraBidi) {
            throw new IllegalStateException();
        }
    }

    void verifyValidParaOrLine() {
        Bidi para = this.paraBidi;
        if (this == para) {
            return;
        }
        if (para == null || para != para.paraBidi) {
            throw new IllegalStateException();
        }
    }

    void verifyRange(int index, int start, int limit) {
        if (index < start || index >= limit) {
            throw new IllegalArgumentException("Value " + index + " is out of range " + start + " to " + limit);
        }
    }

    public Bidi() {
        this(0, 0);
    }

    public Bidi(int maxLength, int maxRunCount) {
        if (maxLength < 0 || maxRunCount < 0) {
            throw new IllegalArgumentException();
        }
        this.bdp = UBiDiProps.INSTANCE;
        if (maxLength > 0) {
            this.getInitialDirPropsMemory(maxLength);
            this.getInitialLevelsMemory(maxLength);
        } else {
            this.mayAllocateText = true;
        }
        if (maxRunCount > 0) {
            if (maxRunCount > 1) {
                this.getInitialRunsMemory(maxRunCount);
            }
        } else {
            this.mayAllocateRuns = true;
        }
    }

    private Object getMemory(String label, Object array, Class<?> arrayClass, boolean mayAllocate, int sizeNeeded) {
        int len = Array.getLength(array);
        if (sizeNeeded == len) {
            return array;
        }
        if (!mayAllocate) {
            if (sizeNeeded <= len) {
                return array;
            }
            throw new OutOfMemoryError("Failed to allocate memory for " + label);
        }
        try {
            return Array.newInstance(arrayClass, sizeNeeded);
        }
        catch (Exception e2) {
            throw new OutOfMemoryError("Failed to allocate memory for " + label);
        }
    }

    private void getDirPropsMemory(boolean mayAllocate, int len) {
        Object array = this.getMemory("DirProps", this.dirPropsMemory, Byte.TYPE, mayAllocate, len);
        this.dirPropsMemory = (byte[])array;
    }

    void getDirPropsMemory(int len) {
        this.getDirPropsMemory(this.mayAllocateText, len);
    }

    private void getLevelsMemory(boolean mayAllocate, int len) {
        Object array = this.getMemory("Levels", this.levelsMemory, Byte.TYPE, mayAllocate, len);
        this.levelsMemory = (byte[])array;
    }

    void getLevelsMemory(int len) {
        this.getLevelsMemory(this.mayAllocateText, len);
    }

    private void getRunsMemory(boolean mayAllocate, int len) {
        Object array = this.getMemory("Runs", this.runsMemory, BidiRun.class, mayAllocate, len);
        this.runsMemory = (BidiRun[])array;
    }

    void getRunsMemory(int len) {
        this.getRunsMemory(this.mayAllocateRuns, len);
    }

    private void getInitialDirPropsMemory(int len) {
        this.getDirPropsMemory(true, len);
    }

    private void getInitialLevelsMemory(int len) {
        this.getLevelsMemory(true, len);
    }

    private void getInitialRunsMemory(int len) {
        this.getRunsMemory(true, len);
    }

    public void setInverse(boolean isInverse) {
        this.isInverse = isInverse;
        this.reorderingMode = isInverse ? 4 : 0;
    }

    public boolean isInverse() {
        return this.isInverse;
    }

    public void setReorderingMode(int reorderingMode) {
        if (reorderingMode < 0 || reorderingMode >= 7) {
            return;
        }
        this.reorderingMode = reorderingMode;
        this.isInverse = reorderingMode == 4;
    }

    public int getReorderingMode() {
        return this.reorderingMode;
    }

    public void setReorderingOptions(int options) {
        this.reorderingOptions = (options & 2) != 0 ? options & 0xFFFFFFFE : options;
    }

    public int getReorderingOptions() {
        return this.reorderingOptions;
    }

    public static byte getBaseDirection(CharSequence paragraph) {
        if (paragraph == null || paragraph.length() == 0) {
            return 3;
        }
        int length = paragraph.length();
        int i2 = 0;
        while (i2 < length) {
            int c2 = UCharacter.codePointAt(paragraph, i2);
            byte direction = UCharacter.getDirectionality(c2);
            if (direction == 0) {
                return 0;
            }
            if (direction == 1 || direction == 13) {
                return 1;
            }
            i2 = UCharacter.offsetByCodePoints(paragraph, i2, 1);
        }
        return 3;
    }

    private byte firstL_R_AL() {
        int uchar;
        int result = 10;
        for (int i2 = 0; i2 < this.prologue.length(); i2 += Character.charCount(uchar)) {
            uchar = this.prologue.codePointAt(i2);
            int dirProp = this.getCustomizedClass(uchar);
            if (result != 10) continue;
            if (dirProp != 0 && dirProp != 1 && dirProp != 13) continue;
            result = dirProp;
        }
        return (byte)result;
    }

    private void checkParaCount() {
        int count = this.paraCount;
        if (count <= this.paras_level.length) {
            return;
        }
        int oldLength = this.paras_level.length;
        int[] saveLimits = this.paras_limit;
        byte[] saveLevels = this.paras_level;
        try {
            this.paras_limit = new int[count * 2];
            this.paras_level = new byte[count * 2];
        }
        catch (Exception e2) {
            throw new OutOfMemoryError("Failed to allocate memory for paras");
        }
        System.arraycopy(saveLimits, 0, this.paras_limit, 0, oldLength);
        System.arraycopy(saveLevels, 0, this.paras_level, 0, oldLength);
    }

    private void getDirProps() {
        int state;
        byte dirProp;
        int i2 = 0;
        this.flags = 0;
        byte defaultParaLevel = 0;
        boolean isDefaultLevel = Bidi.IsDefaultLevel(this.paraLevel);
        boolean isDefaultLevelInverse = isDefaultLevel && (this.reorderingMode == 5 || this.reorderingMode == 6);
        this.lastArabicPos = -1;
        int controlCount = 0;
        boolean removeBidiControls = (this.reorderingOptions & 2) != 0;
        int lastStrong = 10;
        int[] isolateStartStack = new int[126];
        byte[] previousStateStack = new byte[126];
        int stackLast = -1;
        if ((this.reorderingOptions & 4) != 0) {
            this.length = 0;
        }
        defaultParaLevel = (byte)(this.paraLevel & 1);
        if (isDefaultLevel) {
            this.paras_level[0] = defaultParaLevel;
            lastStrong = defaultParaLevel;
            if (this.prologue != null && (dirProp = this.firstL_R_AL()) != 10) {
                this.paras_level[0] = dirProp == 0 ? (byte)0 : 1;
                state = 0;
            } else {
                state = 1;
            }
        } else {
            this.paras_level[0] = this.paraLevel;
            state = 0;
        }
        i2 = 0;
        while (i2 < this.originalLength) {
            int i0 = i2;
            int uchar = UTF16.charAt(this.text, 0, this.originalLength, i2);
            int i1 = (i2 += UTF16.getCharCount(uchar)) - 1;
            dirProp = (byte)this.getCustomizedClass(uchar);
            this.flags |= Bidi.DirPropFlag(dirProp);
            this.dirProps[i1] = dirProp;
            if (i1 > i0) {
                this.flags |= Bidi.DirPropFlag((byte)18);
                do {
                    this.dirProps[--i1] = 18;
                } while (i1 > i0);
            }
            if (removeBidiControls && Bidi.IsBidiControlChar(uchar)) {
                ++controlCount;
            }
            if (dirProp == 0) {
                if (state == 1) {
                    this.paras_level[this.paraCount - 1] = 0;
                    state = 0;
                } else if (state == 2) {
                    if (stackLast <= 125) {
                        this.flags |= Bidi.DirPropFlag((byte)20);
                    }
                    state = 3;
                }
                lastStrong = 0;
                continue;
            }
            if (dirProp == 1 || dirProp == 13) {
                if (state == 1) {
                    this.paras_level[this.paraCount - 1] = 1;
                    state = 0;
                } else if (state == 2) {
                    if (stackLast <= 125) {
                        this.dirProps[isolateStartStack[stackLast]] = 21;
                        this.flags |= Bidi.DirPropFlag((byte)21);
                    }
                    state = 3;
                }
                lastStrong = 1;
                if (dirProp != 13) continue;
                this.lastArabicPos = i2 - 1;
                continue;
            }
            if (dirProp >= 19 && dirProp <= 21) {
                if (++stackLast <= 125) {
                    isolateStartStack[stackLast] = i2 - 1;
                    previousStateStack[stackLast] = state;
                }
                if (dirProp == 19) {
                    this.dirProps[i2 - 1] = 20;
                    state = 2;
                    continue;
                }
                state = 3;
                continue;
            }
            if (dirProp == 22) {
                if (state == 2 && stackLast <= 125) {
                    this.flags |= Bidi.DirPropFlag((byte)20);
                }
                if (stackLast < 0) continue;
                if (stackLast <= 125) {
                    state = previousStateStack[stackLast];
                }
                --stackLast;
                continue;
            }
            if (dirProp != 7 || i2 < this.originalLength && uchar == 13 && this.text[i2] == '\n') continue;
            this.paras_limit[this.paraCount - 1] = i2;
            if (isDefaultLevelInverse && lastStrong == 1) {
                this.paras_level[this.paraCount - 1] = 1;
            }
            if ((this.reorderingOptions & 4) != 0) {
                this.length = i2;
                this.controlCount = controlCount;
            }
            if (i2 >= this.originalLength) continue;
            ++this.paraCount;
            this.checkParaCount();
            if (isDefaultLevel) {
                this.paras_level[this.paraCount - 1] = defaultParaLevel;
                state = 1;
                lastStrong = defaultParaLevel;
            } else {
                this.paras_level[this.paraCount - 1] = this.paraLevel;
                state = 0;
            }
            stackLast = -1;
        }
        if (stackLast > 125) {
            stackLast = 125;
            state = 2;
        }
        while (stackLast >= 0) {
            if (state == 2) {
                this.flags |= Bidi.DirPropFlag((byte)20);
                break;
            }
            state = previousStateStack[stackLast];
            --stackLast;
        }
        if ((this.reorderingOptions & 4) != 0) {
            if (this.length < this.originalLength) {
                --this.paraCount;
            }
        } else {
            this.paras_limit[this.paraCount - 1] = this.originalLength;
            this.controlCount = controlCount;
        }
        if (isDefaultLevelInverse && lastStrong == 1) {
            this.paras_level[this.paraCount - 1] = 1;
        }
        if (isDefaultLevel) {
            this.paraLevel = this.paras_level[0];
        }
        for (i2 = 0; i2 < this.paraCount; ++i2) {
            this.flags |= Bidi.DirPropFlagLR(this.paras_level[i2]);
        }
        if (this.orderParagraphsLTR && (this.flags & Bidi.DirPropFlag((byte)7)) != 0) {
            this.flags |= Bidi.DirPropFlag((byte)0);
        }
    }

    byte GetParaLevelAt(int pindex) {
        int i2;
        if (this.defaultParaLevel == 0 || pindex < this.paras_limit[0]) {
            return this.paraLevel;
        }
        for (i2 = 1; i2 < this.paraCount && pindex >= this.paras_limit[i2]; ++i2) {
        }
        if (i2 >= this.paraCount) {
            i2 = this.paraCount - 1;
        }
        return this.paras_level[i2];
    }

    private void bracketInit(BracketData bd) {
        bd.isoRunLast = 0;
        bd.isoRuns[0] = new IsoRun();
        bd.isoRuns[0].start = 0;
        bd.isoRuns[0].limit = 0;
        bd.isoRuns[0].level = this.GetParaLevelAt(0);
        bd.isoRuns[0].lastBase = bd.isoRuns[0].contextDir = (byte)(this.GetParaLevelAt(0) & 1);
        bd.isoRuns[0].lastStrong = bd.isoRuns[0].contextDir;
        bd.isoRuns[0].contextPos = 0;
        bd.openings = new Opening[20];
        bd.isNumbersSpecial = this.reorderingMode == 1 || this.reorderingMode == 6;
    }

    private void bracketProcessB(BracketData bd, byte level) {
        bd.isoRunLast = 0;
        bd.isoRuns[0].limit = 0;
        bd.isoRuns[0].level = level;
        bd.isoRuns[0].lastBase = bd.isoRuns[0].contextDir = (byte)(level & 1);
        bd.isoRuns[0].lastStrong = bd.isoRuns[0].contextDir;
        bd.isoRuns[0].contextPos = 0;
    }

    private void bracketProcessBoundary(BracketData bd, int lastCcPos, byte contextLevel, byte embeddingLevel) {
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        if ((Bidi.DirPropFlag(this.dirProps[lastCcPos]) & MASK_ISO) != 0) {
            return;
        }
        if (Bidi.NoOverride(embeddingLevel) > Bidi.NoOverride(contextLevel)) {
            contextLevel = embeddingLevel;
        }
        pLastIsoRun.limit = pLastIsoRun.start;
        pLastIsoRun.level = embeddingLevel;
        pLastIsoRun.lastBase = pLastIsoRun.contextDir = (byte)(contextLevel & 1);
        pLastIsoRun.lastStrong = pLastIsoRun.contextDir;
        pLastIsoRun.contextPos = lastCcPos;
    }

    private void bracketProcessLRI_RLI(BracketData bd, byte level) {
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        pLastIsoRun.lastBase = (byte)10;
        short lastLimit = pLastIsoRun.limit;
        ++bd.isoRunLast;
        pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        if (pLastIsoRun == null) {
            pLastIsoRun = bd.isoRuns[bd.isoRunLast] = new IsoRun();
        }
        pLastIsoRun.start = pLastIsoRun.limit = lastLimit;
        pLastIsoRun.level = level;
        pLastIsoRun.lastBase = pLastIsoRun.contextDir = (byte)(level & 1);
        pLastIsoRun.lastStrong = pLastIsoRun.contextDir;
        pLastIsoRun.contextPos = 0;
    }

    private void bracketProcessPDI(BracketData bd) {
        --bd.isoRunLast;
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        pLastIsoRun.lastBase = (byte)10;
    }

    private void bracketAddOpening(BracketData bd, char match, int position) {
        Opening pOpening;
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        if (pLastIsoRun.limit >= bd.openings.length) {
            int count;
            Opening[] saveOpenings = bd.openings;
            try {
                count = bd.openings.length;
                bd.openings = new Opening[count * 2];
            }
            catch (Exception e2) {
                throw new OutOfMemoryError("Failed to allocate memory for openings");
            }
            System.arraycopy(saveOpenings, 0, bd.openings, 0, count);
        }
        if ((pOpening = bd.openings[pLastIsoRun.limit]) == null) {
            pOpening = bd.openings[pLastIsoRun.limit] = new Opening();
        }
        pOpening.position = position;
        pOpening.match = match;
        pOpening.contextDir = pLastIsoRun.contextDir;
        pOpening.contextPos = pLastIsoRun.contextPos;
        pOpening.flags = 0;
        pLastIsoRun.limit = (short)(pLastIsoRun.limit + 1);
    }

    private void fixN0c(BracketData bd, int openingIndex, int newPropPosition, byte newProp) {
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        for (int k2 = openingIndex + 1; k2 < pLastIsoRun.limit; ++k2) {
            Opening qOpening = bd.openings[k2];
            if (qOpening.match >= 0) continue;
            if (newPropPosition < qOpening.contextPos) break;
            if (newPropPosition >= qOpening.position) continue;
            if (newProp == qOpening.contextDir) break;
            int openingPosition = qOpening.position;
            this.dirProps[openingPosition] = newProp;
            int closingPosition = -qOpening.match;
            this.dirProps[closingPosition] = newProp;
            qOpening.match = 0;
            this.fixN0c(bd, k2, openingPosition, newProp);
            this.fixN0c(bd, k2, closingPosition, newProp);
        }
    }

    private byte bracketProcessClosing(BracketData bd, int openIdx, int position) {
        byte newProp;
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        Opening pOpening = bd.openings[openIdx];
        byte direction = (byte)(pLastIsoRun.level & 1);
        boolean stable = true;
        if (direction == 0 && (pOpening.flags & FOUND_L) > 0 || direction == 1 && (pOpening.flags & FOUND_R) > 0) {
            newProp = direction;
        } else if ((pOpening.flags & (FOUND_L | FOUND_R)) != 0) {
            boolean bl = stable = openIdx == pLastIsoRun.start;
            newProp = direction != pOpening.contextDir ? pOpening.contextDir : direction;
        } else {
            pLastIsoRun.limit = (short)openIdx;
            return 10;
        }
        this.dirProps[pOpening.position] = newProp;
        this.dirProps[position] = newProp;
        this.fixN0c(bd, openIdx, pOpening.position, newProp);
        if (stable) {
            pLastIsoRun.limit = (short)openIdx;
            while (pLastIsoRun.limit > pLastIsoRun.start && bd.openings[pLastIsoRun.limit - 1].position == pOpening.position) {
                pLastIsoRun.limit = (short)(pLastIsoRun.limit - 1);
            }
        } else {
            pOpening.match = -position;
            int k2 = openIdx - 1;
            while (k2 >= pLastIsoRun.start && bd.openings[k2].position == pOpening.position) {
                bd.openings[k2--].match = 0;
            }
            for (k2 = openIdx + 1; k2 < pLastIsoRun.limit; ++k2) {
                Opening qOpening = bd.openings[k2];
                if (qOpening.position < position) {
                    if (qOpening.match <= 0) continue;
                    qOpening.match = 0;
                    continue;
                }
                break;
            }
        }
        return newProp;
    }

    private void bracketProcessChar(BracketData bd, int position) {
        byte level;
        byte newProp;
        IsoRun pLastIsoRun = bd.isoRuns[bd.isoRunLast];
        byte dirProp = this.dirProps[position];
        if (dirProp == 10) {
            char match;
            char c2 = this.text[position];
            for (int idx = pLastIsoRun.limit - 1; idx >= pLastIsoRun.start; --idx) {
                if (bd.openings[idx].match != c2) continue;
                newProp = this.bracketProcessClosing(bd, idx, position);
                if (newProp == 10) {
                    c2 = '\u0000';
                    break;
                }
                pLastIsoRun.lastBase = (byte)10;
                pLastIsoRun.contextDir = newProp;
                pLastIsoRun.contextPos = position;
                byte level2 = this.levels[position];
                if ((level2 & 0xFFFFFF80) != 0) {
                    pLastIsoRun.lastStrong = newProp = (byte)(level2 & 1);
                    short flag = (short)Bidi.DirPropFlag(newProp);
                    for (int i2 = pLastIsoRun.start; i2 < idx; ++i2) {
                        bd.openings[i2].flags = (short)(bd.openings[i2].flags | flag);
                    }
                    int n2 = position;
                    this.levels[n2] = (byte)(this.levels[n2] & 0x7F);
                }
                int n3 = bd.openings[idx].position;
                this.levels[n3] = (byte)(this.levels[n3] & 0x7F);
                return;
            }
            if ((match = c2 != '\u0000' ? (char)UCharacter.getBidiPairedBracket(c2) : (char)'\u0000') != c2 && UCharacter.getIntPropertyValue(c2, 4117) == 1) {
                if (match == '\u232a') {
                    this.bracketAddOpening(bd, '\u3009', position);
                } else if (match == '\u3009') {
                    this.bracketAddOpening(bd, '\u232a', position);
                }
                this.bracketAddOpening(bd, match, position);
            }
        }
        if (((level = this.levels[position]) & 0xFFFFFF80) != 0) {
            newProp = (byte)(level & 1);
            if (dirProp != 8 && dirProp != 9 && dirProp != 10) {
                this.dirProps[position] = newProp;
            }
            pLastIsoRun.lastBase = newProp;
            pLastIsoRun.lastStrong = newProp;
            pLastIsoRun.contextDir = newProp;
            pLastIsoRun.contextPos = position;
        } else if (dirProp <= 1 || dirProp == 13) {
            newProp = Bidi.DirFromStrong(dirProp);
            pLastIsoRun.lastBase = dirProp;
            pLastIsoRun.lastStrong = dirProp;
            pLastIsoRun.contextDir = newProp;
            pLastIsoRun.contextPos = position;
        } else if (dirProp == 2) {
            pLastIsoRun.lastBase = (byte)2;
            if (pLastIsoRun.lastStrong == 0) {
                newProp = 0;
                if (!bd.isNumbersSpecial) {
                    this.dirProps[position] = 23;
                }
                pLastIsoRun.contextDir = 0;
                pLastIsoRun.contextPos = position;
            } else {
                newProp = 1;
                this.dirProps[position] = pLastIsoRun.lastStrong == 13 ? 5 : 24;
                pLastIsoRun.contextDir = 1;
                pLastIsoRun.contextPos = position;
            }
        } else if (dirProp == 5) {
            newProp = 1;
            pLastIsoRun.lastBase = (byte)5;
            pLastIsoRun.contextDir = 1;
            pLastIsoRun.contextPos = position;
        } else if (dirProp == 17) {
            newProp = pLastIsoRun.lastBase;
            if (newProp == 10) {
                this.dirProps[position] = newProp;
            }
        } else {
            newProp = dirProp;
            pLastIsoRun.lastBase = dirProp;
        }
        if (newProp <= 1 || newProp == 13) {
            short flag = (short)Bidi.DirPropFlag(Bidi.DirFromStrong(newProp));
            for (int i3 = pLastIsoRun.start; i3 < pLastIsoRun.limit; ++i3) {
                if (position <= bd.openings[i3].position) continue;
                bd.openings[i3].flags = (short)(bd.openings[i3].flags | flag);
            }
        }
    }

    private byte directionFromFlags() {
        if ((this.flags & MASK_RTL) == 0 && ((this.flags & Bidi.DirPropFlag((byte)5)) == 0 || (this.flags & MASK_POSSIBLE_N) == 0)) {
            return 0;
        }
        if ((this.flags & MASK_LTR) == 0) {
            return 1;
        }
        return 2;
    }

    private byte resolveExplicitLevels() {
        int i2 = 0;
        byte level = this.GetParaLevelAt(0);
        this.isolateCount = 0;
        byte dirct = this.directionFromFlags();
        if (dirct != 2) {
            return dirct;
        }
        if (this.reorderingMode > 1) {
            for (int paraIndex = 0; paraIndex < this.paraCount; ++paraIndex) {
                int start = paraIndex == 0 ? 0 : this.paras_limit[paraIndex - 1];
                int limit = this.paras_limit[paraIndex];
                level = this.paras_level[paraIndex];
                for (i2 = start; i2 < limit; ++i2) {
                    this.levels[i2] = level;
                }
            }
            return dirct;
        }
        if ((this.flags & (MASK_EXPLICIT | MASK_ISO)) == 0) {
            BracketData bracketData = new BracketData();
            this.bracketInit(bracketData);
            for (int paraIndex = 0; paraIndex < this.paraCount; ++paraIndex) {
                int start = paraIndex == 0 ? 0 : this.paras_limit[paraIndex - 1];
                int limit = this.paras_limit[paraIndex];
                level = this.paras_level[paraIndex];
                for (i2 = start; i2 < limit; ++i2) {
                    this.levels[i2] = level;
                    byte dirProp = this.dirProps[i2];
                    if (dirProp == 18) continue;
                    if (dirProp == 7) {
                        if (i2 + 1 >= this.length || this.text[i2] == '\r' && this.text[i2 + 1] == '\n') continue;
                        this.bracketProcessB(bracketData, level);
                        continue;
                    }
                    this.bracketProcessChar(bracketData, i2);
                }
            }
            return dirct;
        }
        byte embeddingLevel = level;
        byte previousLevel = level;
        int lastCcPos = 0;
        short[] stack = new short[127];
        int stackLast = 0;
        int overflowIsolateCount = 0;
        int overflowEmbeddingCount = 0;
        int validIsolateCount = 0;
        BracketData bracketData = new BracketData();
        this.bracketInit(bracketData);
        stack[0] = level;
        this.flags = 0;
        block12: for (i2 = 0; i2 < this.length; ++i2) {
            byte dirProp = this.dirProps[i2];
            switch (dirProp) {
                case 11: 
                case 12: 
                case 14: 
                case 15: {
                    this.flags |= Bidi.DirPropFlag((byte)18);
                    this.levels[i2] = previousLevel;
                    byte newLevel = dirProp == 11 || dirProp == 12 ? (byte)(embeddingLevel + 2 & 0x7E) : (byte)(Bidi.NoOverride(embeddingLevel) + 1 | 1);
                    if (newLevel <= 125 && overflowIsolateCount == 0 && overflowEmbeddingCount == 0) {
                        lastCcPos = i2;
                        embeddingLevel = newLevel;
                        if (dirProp == 12 || dirProp == 15) {
                            embeddingLevel = (byte)(embeddingLevel | 0xFFFFFF80);
                        }
                        stack[++stackLast] = embeddingLevel;
                        continue block12;
                    }
                    if (overflowIsolateCount != 0) continue block12;
                    ++overflowEmbeddingCount;
                    continue block12;
                }
                case 16: {
                    this.flags |= Bidi.DirPropFlag((byte)18);
                    this.levels[i2] = previousLevel;
                    if (overflowIsolateCount > 0) continue block12;
                    if (overflowEmbeddingCount > 0) {
                        --overflowEmbeddingCount;
                        continue block12;
                    }
                    if (stackLast <= 0 || stack[stackLast] >= 256) continue block12;
                    lastCcPos = i2;
                    embeddingLevel = (byte)stack[--stackLast];
                    continue block12;
                }
                case 20: 
                case 21: {
                    this.flags |= Bidi.DirPropFlag((byte)10) | Bidi.DirPropFlagLR(embeddingLevel);
                    this.levels[i2] = Bidi.NoOverride(embeddingLevel);
                    if (Bidi.NoOverride(embeddingLevel) != Bidi.NoOverride(previousLevel)) {
                        this.bracketProcessBoundary(bracketData, lastCcPos, previousLevel, embeddingLevel);
                        this.flags |= DirPropFlagMultiRuns;
                    }
                    previousLevel = embeddingLevel;
                    byte newLevel = dirProp == 20 ? (byte)(embeddingLevel + 2 & 0x7E) : (byte)(Bidi.NoOverride(embeddingLevel) + 1 | 1);
                    if (newLevel <= 125 && overflowIsolateCount == 0 && overflowEmbeddingCount == 0) {
                        this.flags |= Bidi.DirPropFlag(dirProp);
                        lastCcPos = i2;
                        if (++validIsolateCount > this.isolateCount) {
                            this.isolateCount = validIsolateCount;
                        }
                        embeddingLevel = newLevel;
                        stack[++stackLast] = (short)(embeddingLevel + 256);
                        this.bracketProcessLRI_RLI(bracketData, embeddingLevel);
                        continue block12;
                    }
                    this.dirProps[i2] = 9;
                    ++overflowIsolateCount;
                    continue block12;
                }
                case 22: {
                    if (Bidi.NoOverride(embeddingLevel) != Bidi.NoOverride(previousLevel)) {
                        this.bracketProcessBoundary(bracketData, lastCcPos, previousLevel, embeddingLevel);
                        this.flags |= DirPropFlagMultiRuns;
                    }
                    if (overflowIsolateCount > 0) {
                        --overflowIsolateCount;
                        this.dirProps[i2] = 9;
                    } else if (validIsolateCount > 0) {
                        this.flags |= Bidi.DirPropFlag((byte)22);
                        lastCcPos = i2;
                        overflowEmbeddingCount = 0;
                        while (stack[stackLast] < 256) {
                            --stackLast;
                        }
                        --stackLast;
                        --validIsolateCount;
                        this.bracketProcessPDI(bracketData);
                    } else {
                        this.dirProps[i2] = 9;
                    }
                    embeddingLevel = (byte)(stack[stackLast] & 0xFFFFFEFF);
                    this.flags |= Bidi.DirPropFlag((byte)10) | Bidi.DirPropFlagLR(embeddingLevel);
                    previousLevel = embeddingLevel;
                    this.levels[i2] = Bidi.NoOverride(embeddingLevel);
                    continue block12;
                }
                case 7: {
                    this.flags |= Bidi.DirPropFlag((byte)7);
                    this.levels[i2] = this.GetParaLevelAt(i2);
                    if (i2 + 1 >= this.length || this.text[i2] == '\r' && this.text[i2 + 1] == '\n') continue block12;
                    overflowIsolateCount = 0;
                    overflowEmbeddingCount = 0;
                    validIsolateCount = 0;
                    stackLast = 0;
                    previousLevel = embeddingLevel = this.GetParaLevelAt(i2 + 1);
                    stack[0] = embeddingLevel;
                    this.bracketProcessB(bracketData, embeddingLevel);
                    continue block12;
                }
                case 18: {
                    this.levels[i2] = previousLevel;
                    this.flags |= Bidi.DirPropFlag((byte)18);
                    continue block12;
                }
                default: {
                    if (Bidi.NoOverride(embeddingLevel) != Bidi.NoOverride(previousLevel)) {
                        this.bracketProcessBoundary(bracketData, lastCcPos, previousLevel, embeddingLevel);
                        this.flags |= DirPropFlagMultiRuns;
                        this.flags = (embeddingLevel & 0xFFFFFF80) != 0 ? (this.flags |= Bidi.DirPropFlagO(embeddingLevel)) : (this.flags |= Bidi.DirPropFlagE(embeddingLevel));
                    }
                    previousLevel = embeddingLevel;
                    this.levels[i2] = embeddingLevel;
                    this.bracketProcessChar(bracketData, i2);
                    this.flags |= Bidi.DirPropFlag(this.dirProps[i2]);
                }
            }
        }
        if ((this.flags & MASK_EMBEDDING) != 0) {
            this.flags |= Bidi.DirPropFlagLR(this.paraLevel);
        }
        if (this.orderParagraphsLTR && (this.flags & Bidi.DirPropFlag((byte)7)) != 0) {
            this.flags |= Bidi.DirPropFlag((byte)0);
        }
        dirct = this.directionFromFlags();
        return dirct;
    }

    private byte checkExplicitLevels() {
        int isolateCount = 0;
        this.flags = 0;
        this.isolateCount = 0;
        int currentParaIndex = 0;
        int currentParaLimit = this.paras_limit[0];
        byte currentParaLevel = this.paraLevel;
        for (int i2 = 0; i2 < this.length; ++i2) {
            byte level = this.levels[i2];
            byte dirProp = this.dirProps[i2];
            if (dirProp == 20 || dirProp == 21) {
                if (++isolateCount > this.isolateCount) {
                    this.isolateCount = isolateCount;
                }
            } else if (dirProp == 22) {
                --isolateCount;
            } else if (dirProp == 7) {
                isolateCount = 0;
            }
            if (this.defaultParaLevel != 0 && i2 == currentParaLimit && currentParaIndex + 1 < this.paraCount) {
                currentParaLevel = this.paras_level[++currentParaIndex];
                currentParaLimit = this.paras_limit[currentParaIndex];
            }
            int overrideFlag = level & 0xFFFFFF80;
            if ((level = (byte)(level & 0x7F)) < currentParaLevel || 125 < level) {
                if (level == 0) {
                    if (dirProp != 7) {
                        level = currentParaLevel;
                        this.levels[i2] = (byte)(level | overrideFlag);
                    }
                } else {
                    throw new IllegalArgumentException("level " + level + " out of bounds at " + i2);
                }
            }
            if (overrideFlag != 0) {
                this.flags |= Bidi.DirPropFlagO(level);
                continue;
            }
            this.flags |= Bidi.DirPropFlagE(level) | Bidi.DirPropFlag(dirProp);
        }
        if ((this.flags & MASK_EMBEDDING) != 0) {
            this.flags |= Bidi.DirPropFlagLR(this.paraLevel);
        }
        return this.directionFromFlags();
    }

    private static short GetStateProps(short cell) {
        return (short)(cell & 0x1F);
    }

    private static short GetActionProps(short cell) {
        return (short)(cell >> 5);
    }

    private static short GetState(byte cell) {
        return (short)(cell & 0xF);
    }

    private static short GetAction(byte cell) {
        return (short)(cell >> 4);
    }

    private void addPoint(int pos, int flag) {
        Point point = new Point();
        int len = this.insertPoints.points.length;
        if (len == 0) {
            this.insertPoints.points = new Point[10];
            len = 10;
        }
        if (this.insertPoints.size >= len) {
            Point[] savePoints = this.insertPoints.points;
            this.insertPoints.points = new Point[len * 2];
            System.arraycopy(savePoints, 0, this.insertPoints.points, 0, len);
        }
        point.pos = pos;
        point.flag = flag;
        this.insertPoints.points[this.insertPoints.size] = point;
        ++this.insertPoints.size;
    }

    private void setLevelsOutsideIsolates(int start, int limit, byte level) {
        int isolateCount = 0;
        for (int k2 = start; k2 < limit; ++k2) {
            byte dirProp = this.dirProps[k2];
            if (dirProp == 22) {
                --isolateCount;
            }
            if (isolateCount == 0) {
                this.levels[k2] = level;
            }
            if (dirProp != 20 && dirProp != 21) continue;
            ++isolateCount;
        }
    }

    private void processPropertySeq(LevState levState, short _prop, int start, int limit) {
        int k2;
        byte level;
        byte[][] impTab = levState.impTab;
        short[] impAct = levState.impAct;
        int start0 = start;
        short oldStateSeq = levState.state;
        byte cell = impTab[oldStateSeq][_prop];
        levState.state = Bidi.GetState(cell);
        short actionSeq = impAct[Bidi.GetAction(cell)];
        byte addLevel = impTab[levState.state][7];
        if (actionSeq != 0) {
            switch (actionSeq) {
                case 1: {
                    levState.startON = start0;
                    break;
                }
                case 2: {
                    start = levState.startON;
                    break;
                }
                case 3: {
                    level = (byte)(levState.runLevel + 1);
                    this.setLevelsOutsideIsolates(levState.startON, start0, level);
                    break;
                }
                case 4: {
                    level = (byte)(levState.runLevel + 2);
                    this.setLevelsOutsideIsolates(levState.startON, start0, level);
                    break;
                }
                case 5: {
                    if (levState.startL2EN >= 0) {
                        this.addPoint(levState.startL2EN, 1);
                    }
                    levState.startL2EN = -1;
                    if (this.insertPoints.points.length == 0 || this.insertPoints.size <= this.insertPoints.confirmed) {
                        levState.lastStrongRTL = -1;
                        level = impTab[oldStateSeq][7];
                        if ((level & 1) != 0 && levState.startON > 0) {
                            start = levState.startON;
                        }
                        if (_prop != 5) break;
                        this.addPoint(start0, 1);
                        this.insertPoints.confirmed = this.insertPoints.size;
                        break;
                    }
                    for (k2 = levState.lastStrongRTL + 1; k2 < start0; ++k2) {
                        this.levels[k2] = (byte)(this.levels[k2] - 2 & 0xFFFFFFFE);
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.lastStrongRTL = -1;
                    if (_prop != 5) break;
                    this.addPoint(start0, 1);
                    this.insertPoints.confirmed = this.insertPoints.size;
                    break;
                }
                case 6: {
                    if (this.insertPoints.points.length > 0) {
                        this.insertPoints.size = this.insertPoints.confirmed;
                    }
                    levState.startON = -1;
                    levState.startL2EN = -1;
                    levState.lastStrongRTL = limit - 1;
                    break;
                }
                case 7: {
                    if (_prop == 3 && this.dirProps[start0] == 5 && this.reorderingMode != 6) {
                        if (levState.startL2EN == -1) {
                            levState.lastStrongRTL = limit - 1;
                            break;
                        }
                        if (levState.startL2EN >= 0) {
                            this.addPoint(levState.startL2EN, 1);
                            levState.startL2EN = -2;
                        }
                        this.addPoint(start0, 1);
                        break;
                    }
                    if (levState.startL2EN != -1) break;
                    levState.startL2EN = start0;
                    break;
                }
                case 8: {
                    levState.lastStrongRTL = limit - 1;
                    levState.startON = -1;
                    break;
                }
                case 9: {
                    for (k2 = start0 - 1; k2 >= 0 && (this.levels[k2] & 1) == 0; --k2) {
                    }
                    if (k2 >= 0) {
                        this.addPoint(k2, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                    }
                    levState.startON = start0;
                    break;
                }
                case 10: {
                    this.addPoint(start0, 1);
                    this.addPoint(start0, 2);
                    break;
                }
                case 11: {
                    this.insertPoints.size = this.insertPoints.confirmed;
                    if (_prop != 5) break;
                    this.addPoint(start0, 4);
                    this.insertPoints.confirmed = this.insertPoints.size;
                    break;
                }
                case 12: {
                    level = (byte)(levState.runLevel + addLevel);
                    for (k2 = levState.startON; k2 < start0; ++k2) {
                        if (this.levels[k2] >= level) continue;
                        this.levels[k2] = level;
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.startON = start0;
                    break;
                }
                case 13: {
                    level = levState.runLevel;
                    for (k2 = start0 - 1; k2 >= levState.startON; --k2) {
                        if (this.levels[k2] == level + 3) {
                            while (this.levels[k2] == level + 3) {
                                int n2 = k2--;
                                this.levels[n2] = (byte)(this.levels[n2] - 2);
                            }
                            while (this.levels[k2] == level) {
                                --k2;
                            }
                        }
                        this.levels[k2] = this.levels[k2] == level + 2 ? level : (byte)(level + 1);
                    }
                    break;
                }
                case 14: {
                    level = (byte)(levState.runLevel + 1);
                    for (k2 = start0 - 1; k2 >= levState.startON; --k2) {
                        if (this.levels[k2] <= level) continue;
                        int n3 = k2;
                        this.levels[n3] = (byte)(this.levels[n3] - 2);
                    }
                    break;
                }
                default: {
                    throw new IllegalStateException("Internal ICU error in processPropertySeq");
                }
            }
        }
        if (addLevel != 0 || start < start0) {
            level = (byte)(levState.runLevel + addLevel);
            if (start >= levState.runStart) {
                for (k2 = start; k2 < limit; ++k2) {
                    this.levels[k2] = level;
                }
            } else {
                this.setLevelsOutsideIsolates(start, limit, level);
            }
        }
    }

    private byte lastL_R_AL() {
        int uchar;
        for (int i2 = this.prologue.length(); i2 > 0; i2 -= Character.charCount(uchar)) {
            uchar = this.prologue.codePointBefore(i2);
            byte dirProp = (byte)this.getCustomizedClass(uchar);
            if (dirProp == 0) {
                return 0;
            }
            if (dirProp != 1 && dirProp != 13) continue;
            return 1;
        }
        return 4;
    }

    private byte firstL_R_AL_EN_AN() {
        int uchar;
        for (int i2 = 0; i2 < this.epilogue.length(); i2 += Character.charCount(uchar)) {
            uchar = this.epilogue.codePointAt(i2);
            byte dirProp = (byte)this.getCustomizedClass(uchar);
            if (dirProp == 0) {
                return 0;
            }
            if (dirProp == 1 || dirProp == 13) {
                return 1;
            }
            if (dirProp != 2) continue;
            return 2;
        }
        return 4;
    }

    private void resolveImplicitLevels(int start, int limit, short sor, short eor) {
        byte firstStrong;
        byte dirProp;
        int i2;
        short stateImp;
        int start1;
        byte lastStrong;
        LevState levState = new LevState();
        int nextStrongProp = 1;
        int nextStrongPos = -1;
        boolean inverseRTL = start < this.lastArabicPos && (this.GetParaLevelAt(start) & 1) > 0 && (this.reorderingMode == 5 || this.reorderingMode == 6);
        levState.startL2EN = -1;
        levState.lastStrongRTL = -1;
        levState.runStart = start;
        levState.runLevel = this.levels[start];
        levState.impTab = this.impTabPair.imptab[levState.runLevel & 1];
        levState.impAct = this.impTabPair.impact[levState.runLevel & 1];
        if (start == 0 && this.prologue != null && (lastStrong = this.lastL_R_AL()) != 4) {
            sor = lastStrong;
        }
        if (this.dirProps[start] == 22) {
            levState.startON = this.isolates[this.isolateCount].startON;
            start1 = this.isolates[this.isolateCount].start1;
            stateImp = this.isolates[this.isolateCount].stateImp;
            levState.state = this.isolates[this.isolateCount].state;
            --this.isolateCount;
        } else {
            levState.startON = -1;
            start1 = start;
            stateImp = this.dirProps[start] == 17 ? (short)(1 + sor) : (short)0;
            levState.state = 0;
            this.processPropertySeq(levState, sor, start, start);
        }
        int start2 = start;
        block6: for (i2 = start; i2 <= limit; ++i2) {
            short gprop;
            if (i2 >= limit) {
                int k2;
                for (k2 = limit - 1; k2 > start && (Bidi.DirPropFlag(this.dirProps[k2]) & MASK_BN_EXPLICIT) != 0; --k2) {
                }
                dirProp = this.dirProps[k2];
                if (dirProp == 20 || dirProp == 21) break;
                gprop = eor;
            } else {
                int prop = this.dirProps[i2];
                if (prop == 7) {
                    this.isolateCount = -1;
                }
                if (inverseRTL) {
                    if (prop == 13) {
                        prop = 1;
                    } else if (prop == 2) {
                        if (nextStrongPos <= i2) {
                            nextStrongProp = 1;
                            nextStrongPos = limit;
                            for (int j2 = i2 + 1; j2 < limit; ++j2) {
                                byte prop1 = this.dirProps[j2];
                                if (prop1 != 0 && prop1 != 1 && prop1 != 13) continue;
                                nextStrongProp = prop1;
                                nextStrongPos = j2;
                                break;
                            }
                        }
                        if (nextStrongProp == 13) {
                            prop = 5;
                        }
                    }
                }
                gprop = groupProp[prop];
            }
            short oldStateImp = stateImp;
            short cell = impTabProps[oldStateImp][gprop];
            stateImp = Bidi.GetStateProps(cell);
            short actionImp = Bidi.GetActionProps(cell);
            if (i2 == limit && actionImp == 0) {
                actionImp = 1;
            }
            if (actionImp == 0) continue;
            short resProp = impTabProps[oldStateImp][15];
            switch (actionImp) {
                case 1: {
                    this.processPropertySeq(levState, resProp, start1, i2);
                    start1 = i2;
                    continue block6;
                }
                case 2: {
                    start2 = i2;
                    continue block6;
                }
                case 3: {
                    this.processPropertySeq(levState, resProp, start1, start2);
                    this.processPropertySeq(levState, (short)4, start2, i2);
                    start1 = i2;
                    continue block6;
                }
                case 4: {
                    this.processPropertySeq(levState, resProp, start1, start2);
                    start1 = start2;
                    start2 = i2;
                    continue block6;
                }
                default: {
                    throw new IllegalStateException("Internal ICU error in resolveImplicitLevels");
                }
            }
        }
        if (limit == this.length && this.epilogue != null && (firstStrong = this.firstL_R_AL_EN_AN()) != 4) {
            eor = firstStrong;
        }
        for (i2 = limit - 1; i2 > start && (Bidi.DirPropFlag(this.dirProps[i2]) & MASK_BN_EXPLICIT) != 0; --i2) {
        }
        dirProp = this.dirProps[i2];
        if ((dirProp == 20 || dirProp == 21) && limit < this.length) {
            ++this.isolateCount;
            if (this.isolates[this.isolateCount] == null) {
                this.isolates[this.isolateCount] = new Isolate();
            }
            this.isolates[this.isolateCount].stateImp = stateImp;
            this.isolates[this.isolateCount].state = levState.state;
            this.isolates[this.isolateCount].start1 = start1;
            this.isolates[this.isolateCount].startON = levState.startON;
        } else {
            this.processPropertySeq(levState, eor, limit, limit);
        }
    }

    private void adjustWSLevels() {
        if ((this.flags & MASK_WS) != 0) {
            int i2 = this.trailingWSStart;
            block0: while (i2 > 0) {
                int flag;
                while (i2 > 0 && ((flag = Bidi.DirPropFlag(this.dirProps[--i2])) & MASK_WS) != 0) {
                    if (this.orderParagraphsLTR && (flag & Bidi.DirPropFlag((byte)7)) != 0) {
                        this.levels[i2] = 0;
                        continue;
                    }
                    this.levels[i2] = this.GetParaLevelAt(i2);
                }
                while (i2 > 0) {
                    if (((flag = Bidi.DirPropFlag(this.dirProps[--i2])) & MASK_BN_EXPLICIT) != 0) {
                        this.levels[i2] = this.levels[i2 + 1];
                        continue;
                    }
                    if (this.orderParagraphsLTR && (flag & Bidi.DirPropFlag((byte)7)) != 0) {
                        this.levels[i2] = 0;
                        continue block0;
                    }
                    if ((flag & MASK_B_S) == 0) continue;
                    this.levels[i2] = this.GetParaLevelAt(i2);
                    continue block0;
                }
            }
        }
    }

    public void setContext(String prologue, String epilogue) {
        this.prologue = prologue != null && prologue.length() > 0 ? prologue : null;
        this.epilogue = epilogue != null && epilogue.length() > 0 ? epilogue : null;
    }

    private void setParaSuccess() {
        this.prologue = null;
        this.epilogue = null;
        this.paraBidi = this;
    }

    int Bidi_Min(int x2, int y2) {
        return x2 < y2 ? x2 : y2;
    }

    int Bidi_Abs(int x2) {
        return x2 >= 0 ? x2 : -x2;
    }

    void setParaRunsOnly(char[] parmText, byte parmParaLevel) {
        int index1;
        int index;
        int j2;
        int logicalStart;
        int runLength;
        this.reorderingMode = 0;
        int parmLength = parmText.length;
        if (parmLength == 0) {
            this.setPara(parmText, parmParaLevel, null);
            this.reorderingMode = 3;
            return;
        }
        int saveOptions = this.reorderingOptions;
        if ((saveOptions & 1) > 0) {
            this.reorderingOptions &= 0xFFFFFFFE;
            this.reorderingOptions |= 2;
        }
        parmParaLevel = (byte)(parmParaLevel & 1);
        this.setPara(parmText, parmParaLevel, null);
        byte[] saveLevels = new byte[this.length];
        System.arraycopy(this.getLevels(), 0, saveLevels, 0, this.length);
        int saveTrailingWSStart = this.trailingWSStart;
        String visualText = this.writeReordered(2);
        int[] visualMap = this.getVisualMap();
        this.reorderingOptions = saveOptions;
        int saveLength = this.length;
        byte saveDirection = this.direction;
        this.reorderingMode = 5;
        parmParaLevel = (byte)(parmParaLevel ^ 1);
        this.setPara(visualText, parmParaLevel, null);
        BidiLine.getRuns(this);
        int addedRuns = 0;
        int oldRunCount = this.runCount;
        int visualStart = 0;
        int i2 = 0;
        while (i2 < oldRunCount) {
            runLength = this.runs[i2].limit - visualStart;
            if (runLength >= 2) {
                logicalStart = this.runs[i2].start;
                for (j2 = logicalStart + 1; j2 < logicalStart + runLength; ++j2) {
                    index = visualMap[j2];
                    index1 = visualMap[j2 - 1];
                    if (this.Bidi_Abs(index - index1) == 1 && saveLevels[index] == saveLevels[index1]) continue;
                    ++addedRuns;
                }
            }
            ++i2;
            visualStart += runLength;
        }
        if (addedRuns > 0) {
            this.getRunsMemory(oldRunCount + addedRuns);
            if (this.runCount == 1) {
                this.runsMemory[0] = this.runs[0];
            } else {
                System.arraycopy(this.runs, 0, this.runsMemory, 0, this.runCount);
            }
            this.runs = this.runsMemory;
            this.runCount += addedRuns;
            for (i2 = oldRunCount; i2 < this.runCount; ++i2) {
                if (this.runs[i2] != null) continue;
                this.runs[i2] = new BidiRun(0, 0, 0);
            }
        }
        for (i2 = oldRunCount - 1; i2 >= 0; --i2) {
            int step;
            int limit;
            int start;
            int logicalPos;
            int newI = i2 + addedRuns;
            runLength = i2 == 0 ? this.runs[0].limit : this.runs[i2].limit - this.runs[i2 - 1].limit;
            logicalStart = this.runs[i2].start;
            int indexOddBit = this.runs[i2].level & 1;
            if (runLength < 2) {
                if (addedRuns > 0) {
                    this.runs[newI].copyFrom(this.runs[i2]);
                }
                this.runs[newI].start = logicalPos = visualMap[logicalStart];
                this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
                continue;
            }
            if (indexOddBit > 0) {
                start = logicalStart;
                limit = logicalStart + runLength - 1;
                step = 1;
            } else {
                start = logicalStart + runLength - 1;
                limit = logicalStart;
                step = -1;
            }
            for (j2 = start; j2 != limit; j2 += step) {
                int insertRemove;
                index = visualMap[j2];
                index1 = visualMap[j2 + step];
                if (this.Bidi_Abs(index - index1) == 1 && saveLevels[index] == saveLevels[index1]) continue;
                this.runs[newI].start = logicalPos = this.Bidi_Min(visualMap[start], index);
                this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
                this.runs[newI].limit = this.runs[i2].limit;
                this.runs[i2].limit -= this.Bidi_Abs(j2 - start) + 1;
                this.runs[newI].insertRemove = insertRemove = this.runs[i2].insertRemove & 0xA;
                this.runs[i2].insertRemove &= ~insertRemove;
                start = j2 + step;
                --addedRuns;
                --newI;
            }
            if (addedRuns > 0) {
                this.runs[newI].copyFrom(this.runs[i2]);
            }
            this.runs[newI].start = logicalPos = this.Bidi_Min(visualMap[start], visualMap[limit]);
            this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
        }
        this.paraLevel = (byte)(this.paraLevel ^ 1);
        this.text = parmText;
        this.length = saveLength;
        this.originalLength = parmLength;
        this.direction = saveDirection;
        this.levels = saveLevels;
        this.trailingWSStart = saveTrailingWSStart;
        if (this.runCount > 1) {
            this.direction = (byte)2;
        }
        this.reorderingMode = 3;
    }

    public void setPara(String text, byte paraLevel, byte[] embeddingLevels) {
        if (text == null) {
            this.setPara(new char[0], paraLevel, embeddingLevels);
        } else {
            this.setPara(text.toCharArray(), paraLevel, embeddingLevels);
        }
    }

    public void setPara(char[] chars, byte paraLevel, byte[] embeddingLevels) {
        int start;
        byte level;
        if (paraLevel < 126) {
            this.verifyRange(paraLevel, 0, 126);
        }
        if (chars == null) {
            chars = new char[]{};
        }
        if (this.reorderingMode == 3) {
            this.setParaRunsOnly(chars, paraLevel);
            return;
        }
        this.paraBidi = null;
        this.text = chars;
        this.originalLength = this.resultLength = this.text.length;
        this.length = this.resultLength;
        this.paraLevel = paraLevel;
        this.direction = (byte)(paraLevel & 1);
        this.paraCount = 1;
        this.dirProps = new byte[0];
        this.levels = new byte[0];
        this.runs = new BidiRun[0];
        this.isGoodLogicalToVisualRunsMap = false;
        this.insertPoints.size = 0;
        this.insertPoints.confirmed = 0;
        byte by = this.defaultParaLevel = Bidi.IsDefaultLevel(paraLevel) ? paraLevel : (byte)0;
        if (this.length == 0) {
            if (Bidi.IsDefaultLevel(paraLevel)) {
                this.paraLevel = (byte)(this.paraLevel & 1);
                this.defaultParaLevel = 0;
            }
            this.flags = Bidi.DirPropFlagLR(paraLevel);
            this.runCount = 0;
            this.paraCount = 0;
            this.setParaSuccess();
            return;
        }
        this.runCount = -1;
        this.getDirPropsMemory(this.length);
        this.dirProps = this.dirPropsMemory;
        this.getDirProps();
        this.trailingWSStart = this.length;
        if (embeddingLevels == null) {
            this.getLevelsMemory(this.length);
            this.levels = this.levelsMemory;
            this.direction = this.resolveExplicitLevels();
        } else {
            this.levels = embeddingLevels;
            this.direction = this.checkExplicitLevels();
        }
        if (this.isolateCount > 0 && (this.isolates == null || this.isolates.length < this.isolateCount)) {
            this.isolates = new Isolate[this.isolateCount + 3];
        }
        this.isolateCount = -1;
        switch (this.direction) {
            case 0: {
                this.trailingWSStart = 0;
                break;
            }
            case 1: {
                this.trailingWSStart = 0;
                break;
            }
            default: {
                switch (this.reorderingMode) {
                    case 0: {
                        this.impTabPair = impTab_DEFAULT;
                        break;
                    }
                    case 1: {
                        this.impTabPair = impTab_NUMBERS_SPECIAL;
                        break;
                    }
                    case 2: {
                        this.impTabPair = impTab_GROUP_NUMBERS_WITH_R;
                        break;
                    }
                    case 3: {
                        throw new InternalError("Internal ICU error in setPara");
                    }
                    case 4: {
                        this.impTabPair = impTab_INVERSE_NUMBERS_AS_L;
                        break;
                    }
                    case 5: {
                        if ((this.reorderingOptions & 1) != 0) {
                            this.impTabPair = impTab_INVERSE_LIKE_DIRECT_WITH_MARKS;
                            break;
                        }
                        this.impTabPair = impTab_INVERSE_LIKE_DIRECT;
                        break;
                    }
                    case 6: {
                        this.impTabPair = (this.reorderingOptions & 1) != 0 ? impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS : impTab_INVERSE_FOR_NUMBERS_SPECIAL;
                    }
                }
                if (embeddingLevels == null && this.paraCount <= 1 && (this.flags & DirPropFlagMultiRuns) == 0) {
                    this.resolveImplicitLevels(0, this.length, Bidi.GetLRFromLevel(this.GetParaLevelAt(0)), Bidi.GetLRFromLevel(this.GetParaLevelAt(this.length - 1)));
                } else {
                    byte nextLevel;
                    int limit = 0;
                    level = this.GetParaLevelAt(0);
                    short eor = level < (nextLevel = this.levels[0]) ? (short)Bidi.GetLRFromLevel(nextLevel) : (short)Bidi.GetLRFromLevel(level);
                    do {
                        start = limit;
                        level = nextLevel;
                        short sor = start > 0 && this.dirProps[start - 1] == 7 ? (short)Bidi.GetLRFromLevel(this.GetParaLevelAt(start)) : eor;
                        while (++limit < this.length && (this.levels[limit] == level || (Bidi.DirPropFlag(this.dirProps[limit]) & MASK_BN_EXPLICIT) != 0)) {
                        }
                        nextLevel = limit < this.length ? this.levels[limit] : this.GetParaLevelAt(this.length - 1);
                        eor = Bidi.NoOverride(level) < Bidi.NoOverride(nextLevel) ? (short)Bidi.GetLRFromLevel(nextLevel) : (short)Bidi.GetLRFromLevel(level);
                        if ((level & 0xFFFFFF80) == 0) {
                            this.resolveImplicitLevels(start, limit, sor, eor);
                            continue;
                        }
                        do {
                            int n2 = start++;
                            this.levels[n2] = (byte)(this.levels[n2] & 0x7F);
                        } while (start < limit);
                    } while (limit < this.length);
                }
                this.adjustWSLevels();
            }
        }
        if (this.defaultParaLevel > 0 && (this.reorderingOptions & 1) != 0 && (this.reorderingMode == 5 || this.reorderingMode == 6)) {
            block16: for (int i2 = 0; i2 < this.paraCount; ++i2) {
                int last = this.paras_limit[i2] - 1;
                level = this.paras_level[i2];
                if (level == 0) continue;
                start = i2 == 0 ? 0 : this.paras_limit[i2 - 1];
                for (int j2 = last; j2 >= start; --j2) {
                    byte dirProp = this.dirProps[j2];
                    if (dirProp == 0) {
                        if (j2 < last) {
                            while (this.dirProps[last] == 7) {
                                --last;
                            }
                        }
                        this.addPoint(last, 4);
                        continue block16;
                    }
                    if ((Bidi.DirPropFlag(dirProp) & MASK_R_AL) != 0) continue block16;
                }
            }
        }
        this.resultLength = (this.reorderingOptions & 2) != 0 ? (this.resultLength -= this.controlCount) : (this.resultLength += this.insertPoints.size);
        this.setParaSuccess();
    }

    public void setPara(AttributedCharacterIterator paragraph) {
        Boolean runDirection = (Boolean)paragraph.getAttribute(TextAttribute.RUN_DIRECTION);
        byte paraLvl = runDirection == null ? (byte)126 : (runDirection.equals(TextAttribute.RUN_DIRECTION_LTR) ? (byte)0 : 1);
        byte[] lvls = null;
        int len = paragraph.getEndIndex() - paragraph.getBeginIndex();
        byte[] embeddingLevels = new byte[len];
        char[] txt = new char[len];
        int i2 = 0;
        char ch = paragraph.first();
        while (ch != '\uffff') {
            byte level;
            txt[i2] = ch;
            Integer embedding = (Integer)paragraph.getAttribute(TextAttribute.BIDI_EMBEDDING);
            if (embedding != null && (level = embedding.byteValue()) != 0) {
                if (level < 0) {
                    lvls = embeddingLevels;
                    embeddingLevels[i2] = (byte)(0 - level | 0xFFFFFF80);
                } else {
                    lvls = embeddingLevels;
                    embeddingLevels[i2] = level;
                }
            }
            ch = paragraph.next();
            ++i2;
        }
        NumericShaper shaper = (NumericShaper)paragraph.getAttribute(TextAttribute.NUMERIC_SHAPING);
        if (shaper != null) {
            shaper.shape(txt, 0, len);
        }
        this.setPara(txt, paraLvl, lvls);
    }

    public void orderParagraphsLTR(boolean ordarParaLTR) {
        this.orderParagraphsLTR = ordarParaLTR;
    }

    public boolean isOrderParagraphsLTR() {
        return this.orderParagraphsLTR;
    }

    public byte getDirection() {
        this.verifyValidParaOrLine();
        return this.direction;
    }

    public String getTextAsString() {
        this.verifyValidParaOrLine();
        return new String(this.text);
    }

    public char[] getText() {
        this.verifyValidParaOrLine();
        return this.text;
    }

    public int getLength() {
        this.verifyValidParaOrLine();
        return this.originalLength;
    }

    public int getProcessedLength() {
        this.verifyValidParaOrLine();
        return this.length;
    }

    public int getResultLength() {
        this.verifyValidParaOrLine();
        return this.resultLength;
    }

    public byte getParaLevel() {
        this.verifyValidParaOrLine();
        return this.paraLevel;
    }

    public int countParagraphs() {
        this.verifyValidParaOrLine();
        return this.paraCount;
    }

    public BidiRun getParagraphByIndex(int paraIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(paraIndex, 0, this.paraCount);
        Bidi bidi = this.paraBidi;
        int paraStart = paraIndex == 0 ? 0 : bidi.paras_limit[paraIndex - 1];
        BidiRun bidiRun = new BidiRun();
        bidiRun.start = paraStart;
        bidiRun.limit = bidi.paras_limit[paraIndex];
        bidiRun.level = this.GetParaLevelAt(paraStart);
        return bidiRun;
    }

    public BidiRun getParagraph(int charIndex) {
        this.verifyValidParaOrLine();
        Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex = 0;
        while (charIndex >= bidi.paras_limit[paraIndex]) {
            ++paraIndex;
        }
        return this.getParagraphByIndex(paraIndex);
    }

    public int getParagraphIndex(int charIndex) {
        this.verifyValidParaOrLine();
        Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex = 0;
        while (charIndex >= bidi.paras_limit[paraIndex]) {
            ++paraIndex;
        }
        return paraIndex;
    }

    public void setCustomClassifier(BidiClassifier classifier) {
        this.customClassifier = classifier;
    }

    public BidiClassifier getCustomClassifier() {
        return this.customClassifier;
    }

    public int getCustomizedClass(int c2) {
        int dir;
        if (this.customClassifier == null || (dir = this.customClassifier.classify(c2)) == 23) {
            dir = this.bdp.getClass(c2);
        }
        if (dir >= 23) {
            dir = 10;
        }
        return dir;
    }

    public Bidi setLine(int start, int limit) {
        this.verifyValidPara();
        this.verifyRange(start, 0, limit);
        this.verifyRange(limit, 0, this.length + 1);
        if (this.getParagraphIndex(start) != this.getParagraphIndex(limit - 1)) {
            throw new IllegalArgumentException();
        }
        return BidiLine.setLine(this, start, limit);
    }

    public byte getLevelAt(int charIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(charIndex, 0, this.length);
        return BidiLine.getLevelAt(this, charIndex);
    }

    public byte[] getLevels() {
        this.verifyValidParaOrLine();
        if (this.length <= 0) {
            return new byte[0];
        }
        return BidiLine.getLevels(this);
    }

    public BidiRun getLogicalRun(int logicalPosition) {
        this.verifyValidParaOrLine();
        this.verifyRange(logicalPosition, 0, this.length);
        return BidiLine.getLogicalRun(this, logicalPosition);
    }

    public int countRuns() {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        return this.runCount;
    }

    public BidiRun getVisualRun(int runIndex) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(runIndex, 0, this.runCount);
        return BidiLine.getVisualRun(this, runIndex);
    }

    public int getVisualIndex(int logicalIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(logicalIndex, 0, this.length);
        return BidiLine.getVisualIndex(this, logicalIndex);
    }

    public int getLogicalIndex(int visualIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(visualIndex, 0, this.resultLength);
        if (this.insertPoints.size == 0 && this.controlCount == 0) {
            if (this.direction == 0) {
                return visualIndex;
            }
            if (this.direction == 1) {
                return this.length - visualIndex - 1;
            }
        }
        BidiLine.getRuns(this);
        return BidiLine.getLogicalIndex(this, visualIndex);
    }

    public int[] getLogicalMap() {
        this.countRuns();
        if (this.length <= 0) {
            return new int[0];
        }
        return BidiLine.getLogicalMap(this);
    }

    public int[] getVisualMap() {
        this.countRuns();
        if (this.resultLength <= 0) {
            return new int[0];
        }
        return BidiLine.getVisualMap(this);
    }

    public static int[] reorderLogical(byte[] levels) {
        return BidiLine.reorderLogical(levels);
    }

    public static int[] reorderVisual(byte[] levels) {
        return BidiLine.reorderVisual(levels);
    }

    public static int[] invertMap(int[] srcMap) {
        if (srcMap == null) {
            return null;
        }
        return BidiLine.invertMap(srcMap);
    }

    public Bidi(String paragraph, int flags) {
        this(paragraph.toCharArray(), 0, null, 0, paragraph.length(), flags);
    }

    public Bidi(AttributedCharacterIterator paragraph) {
        this();
        this.setPara(paragraph);
    }

    public Bidi(char[] text, int textStart, byte[] embeddings, int embStart, int paragraphLength, int flags) {
        this();
        byte[] paraEmbeddings;
        byte paraLvl;
        switch (flags) {
            default: {
                paraLvl = 0;
                break;
            }
            case 1: {
                paraLvl = 1;
                break;
            }
            case 126: {
                paraLvl = 126;
                break;
            }
            case 127: {
                paraLvl = 127;
            }
        }
        if (embeddings == null) {
            paraEmbeddings = null;
        } else {
            paraEmbeddings = new byte[paragraphLength];
            for (int i2 = 0; i2 < paragraphLength; ++i2) {
                byte lev = embeddings[i2 + embStart];
                if (lev < 0) {
                    lev = (byte)(-lev | 0xFFFFFF80);
                }
                paraEmbeddings[i2] = lev;
            }
        }
        if (textStart == 0 && paragraphLength == text.length) {
            this.setPara(text, paraLvl, paraEmbeddings);
        } else {
            char[] paraText = new char[paragraphLength];
            System.arraycopy(text, textStart, paraText, 0, paragraphLength);
            this.setPara(paraText, paraLvl, paraEmbeddings);
        }
    }

    public Bidi createLineBidi(int lineStart, int lineLimit) {
        return this.setLine(lineStart, lineLimit);
    }

    public boolean isMixed() {
        return !this.isLeftToRight() && !this.isRightToLeft();
    }

    public boolean isLeftToRight() {
        return this.getDirection() == 0 && (this.paraLevel & 1) == 0;
    }

    public boolean isRightToLeft() {
        return this.getDirection() == 1 && (this.paraLevel & 1) == 1;
    }

    public boolean baseIsLeftToRight() {
        return this.getParaLevel() == 0;
    }

    public int getBaseLevel() {
        return this.getParaLevel();
    }

    public int getRunCount() {
        return this.countRuns();
    }

    void getLogicalToVisualRunsMap() {
        int i2;
        if (this.isGoodLogicalToVisualRunsMap) {
            return;
        }
        int count = this.countRuns();
        if (this.logicalToVisualRunsMap == null || this.logicalToVisualRunsMap.length < count) {
            this.logicalToVisualRunsMap = new int[count];
        }
        long[] keys = new long[count];
        for (i2 = 0; i2 < count; ++i2) {
            keys[i2] = ((long)this.runs[i2].start << 32) + (long)i2;
        }
        Arrays.sort(keys);
        for (i2 = 0; i2 < count; ++i2) {
            this.logicalToVisualRunsMap[i2] = (int)(keys[i2] & 0xFFFFFFFFFFFFFFFFL);
        }
        this.isGoodLogicalToVisualRunsMap = true;
    }

    public int getRunLevel(int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[run]].level;
    }

    public int getRunStart(int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[run]].start;
    }

    public int getRunLimit(int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        int idx = this.logicalToVisualRunsMap[run];
        int len = idx == 0 ? this.runs[idx].limit : this.runs[idx].limit - this.runs[idx - 1].limit;
        return this.runs[idx].start + len;
    }

    public static boolean requiresBidi(char[] text, int start, int limit) {
        int RTLMask = 57378;
        for (int i2 = start; i2 < limit; ++i2) {
            if ((1 << UCharacter.getDirection(text[i2]) & 0xE022) == 0) continue;
            return true;
        }
        return false;
    }

    public static void reorderVisually(byte[] levels, int levelStart, Object[] objects, int objectStart, int count) {
        byte[] reorderLevels = new byte[count];
        System.arraycopy(levels, levelStart, reorderLevels, 0, count);
        int[] indexMap = Bidi.reorderVisual(reorderLevels);
        Object[] temp = new Object[count];
        System.arraycopy(objects, objectStart, temp, 0, count);
        for (int i2 = 0; i2 < count; ++i2) {
            objects[objectStart + i2] = temp[indexMap[i2]];
        }
    }

    public String writeReordered(int options) {
        this.verifyValidParaOrLine();
        if (this.length == 0) {
            return "";
        }
        return BidiWriter.writeReordered(this, options);
    }

    public static String writeReverse(String src, int options) {
        if (src == null) {
            throw new IllegalArgumentException();
        }
        if (src.length() > 0) {
            return BidiWriter.writeReverse(src, options);
        }
        return "";
    }

    private static class LevState {
        byte[][] impTab;
        short[] impAct;
        int startON;
        int startL2EN;
        int lastStrongRTL;
        int runStart;
        short state;
        byte runLevel;

        private LevState() {
        }
    }

    private static class ImpTabPair {
        byte[][][] imptab;
        short[][] impact;

        ImpTabPair(byte[][] table1, byte[][] table2, short[] act1, short[] act2) {
            this.imptab = new byte[][][]{table1, table2};
            this.impact = new short[][]{act1, act2};
        }
    }

    static class Isolate {
        int startON;
        int start1;
        short stateImp;
        short state;

        Isolate() {
        }
    }

    static class BracketData {
        Opening[] openings = new Opening[20];
        int isoRunLast;
        IsoRun[] isoRuns = new IsoRun[127];
        boolean isNumbersSpecial;

        BracketData() {
        }
    }

    static class IsoRun {
        int contextPos;
        short start;
        short limit;
        byte level;
        byte lastStrong;
        byte lastBase;
        byte contextDir;

        IsoRun() {
        }
    }

    static class Opening {
        int position;
        int match;
        int contextPos;
        short flags;
        byte contextDir;

        Opening() {
        }
    }

    static class InsertPoints {
        int size;
        int confirmed;
        Point[] points = new Point[0];

        InsertPoints() {
        }
    }

    static class Point {
        int pos;
        int flag;

        Point() {
        }
    }
}

