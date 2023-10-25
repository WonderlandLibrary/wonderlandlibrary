/*
 * Decompiled with CFR 0.150.
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

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Bidi {
    public static final byte LEVEL_DEFAULT_LTR = 126;
    public static final byte LEVEL_DEFAULT_RTL = 127;
    public static final byte MAX_EXPLICIT_LEVEL = 61;
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
    static final int MASK_R_AL = 8194;
    public static final int CLASS_DEFAULT = 19;
    private static final char CR = '\r';
    private static final char LF = '\n';
    static final int LRM_BEFORE = 1;
    static final int LRM_AFTER = 2;
    static final int RLM_BEFORE = 4;
    static final int RLM_AFTER = 8;
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
    int[] parasMemory = new int[1];
    int[] paras;
    int[] simpleParas = new int[]{0};
    int runCount;
    BidiRun[] runsMemory = new BidiRun[0];
    BidiRun[] runs;
    BidiRun[] simpleRuns = new BidiRun[]{new BidiRun()};
    int[] logicalToVisualRunsMap;
    boolean isGoodLogicalToVisualRunsMap;
    BidiClassifier customClassifier = null;
    InsertPoints insertPoints = new InsertPoints();
    int controlCount;
    static final byte CONTEXT_RTL_SHIFT = 6;
    static final byte CONTEXT_RTL = 64;
    static final int DirPropFlagMultiRuns = Bidi.DirPropFlag((byte)31);
    static final int[] DirPropFlagLR = new int[]{Bidi.DirPropFlag((byte)0), Bidi.DirPropFlag((byte)1)};
    static final int[] DirPropFlagE = new int[]{Bidi.DirPropFlag((byte)11), Bidi.DirPropFlag((byte)14)};
    static final int[] DirPropFlagO = new int[]{Bidi.DirPropFlag((byte)12), Bidi.DirPropFlag((byte)15)};
    static final int MASK_LTR = Bidi.DirPropFlag((byte)0) | Bidi.DirPropFlag((byte)2) | Bidi.DirPropFlag((byte)5) | Bidi.DirPropFlag((byte)11) | Bidi.DirPropFlag((byte)12);
    static final int MASK_RTL = Bidi.DirPropFlag((byte)1) | Bidi.DirPropFlag((byte)13) | Bidi.DirPropFlag((byte)14) | Bidi.DirPropFlag((byte)15);
    static final int MASK_LRX = Bidi.DirPropFlag((byte)11) | Bidi.DirPropFlag((byte)12);
    static final int MASK_RLX = Bidi.DirPropFlag((byte)14) | Bidi.DirPropFlag((byte)15);
    static final int MASK_OVERRIDE = Bidi.DirPropFlag((byte)12) | Bidi.DirPropFlag((byte)15);
    static final int MASK_EXPLICIT = MASK_LRX | MASK_RLX | Bidi.DirPropFlag((byte)16);
    static final int MASK_BN_EXPLICIT = Bidi.DirPropFlag((byte)18) | MASK_EXPLICIT;
    static final int MASK_B_S = Bidi.DirPropFlag((byte)7) | Bidi.DirPropFlag((byte)8);
    static final int MASK_WS = MASK_B_S | Bidi.DirPropFlag((byte)9) | MASK_BN_EXPLICIT;
    static final int MASK_N = Bidi.DirPropFlag((byte)10) | MASK_WS;
    static final int MASK_ET_NSM_BN = Bidi.DirPropFlag((byte)4) | Bidi.DirPropFlag((byte)17) | MASK_BN_EXPLICIT;
    static final int MASK_POSSIBLE_N = Bidi.DirPropFlag((byte)6) | Bidi.DirPropFlag((byte)3) | Bidi.DirPropFlag((byte)4) | MASK_N;
    static final int MASK_EMBEDDING = Bidi.DirPropFlag((byte)17) | MASK_POSSIBLE_N;
    private static final int IMPTABPROPS_COLUMNS = 14;
    private static final int IMPTABPROPS_RES = 13;
    private static final short[] groupProp = new short[]{0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10};
    private static final short _L = 0;
    private static final short _R = 1;
    private static final short _EN = 2;
    private static final short _AN = 3;
    private static final short _ON = 4;
    private static final short _S = 5;
    private static final short _B = 6;
    private static final short[][] impTabProps = new short[][]{{1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 4}, {1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 0}, {33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 1}, {33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 1}, {33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 2}, {33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 3}, {33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 3}, {33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 4}, {33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 4}, {33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 4}, {97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 2}, {33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 2}, {97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 3}, {97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 3}, {33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 4}, {33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 5}, {33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 5}, {33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 6}};
    private static final int IMPTABLEVELS_COLUMNS = 8;
    private static final int IMPTABLEVELS_RES = 7;
    private static final byte[][] impTabL_DEFAULT = new byte[][]{{0, 1, 0, 2, 0, 0, 0, 0}, {0, 1, 3, 3, 20, 20, 0, 1}, {0, 1, 0, 2, 21, 21, 0, 2}, {0, 1, 3, 3, 20, 20, 0, 2}, {32, 1, 3, 3, 4, 4, 32, 1}, {32, 1, 32, 2, 5, 5, 32, 1}};
    private static final byte[][] impTabR_DEFAULT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 3, 20, 20, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {1, 0, 1, 3, 5, 5, 0, 1}, {33, 0, 33, 3, 4, 4, 0, 0}, {1, 0, 1, 3, 5, 5, 0, 0}};
    private static final short[] impAct0 = new short[]{0, 1, 2, 3, 4, 5, 6};
    private static final ImpTabPair impTab_DEFAULT = new ImpTabPair(impTabL_DEFAULT, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_NUMBERS_SPECIAL = new byte[][]{{0, 2, 1, 1, 0, 0, 0, 0}, {0, 2, 1, 1, 0, 0, 0, 2}, {0, 2, 4, 4, 19, 0, 0, 1}, {32, 2, 4, 4, 3, 3, 32, 1}, {0, 2, 4, 4, 19, 19, 0, 2}};
    private static final ImpTabPair impTab_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R = new byte[][]{{0, 3, 17, 17, 0, 0, 0, 0}, {32, 3, 1, 1, 2, 32, 32, 2}, {32, 3, 1, 1, 2, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 1}, {32, 3, 5, 5, 4, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 2}};
    private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R = new byte[][]{{2, 0, 1, 1, 0, 0, 0, 0}, {2, 0, 1, 1, 0, 0, 0, 1}, {2, 0, 20, 20, 19, 0, 0, 1}, {34, 0, 4, 4, 3, 0, 0, 0}, {34, 0, 4, 4, 3, 0, 0, 1}};
    private static final ImpTabPair impTab_GROUP_NUMBERS_WITH_R = new ImpTabPair(impTabL_GROUP_NUMBERS_WITH_R, impTabR_GROUP_NUMBERS_WITH_R, impAct0, impAct0);
    private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L = new byte[][]{{0, 1, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 20, 20, 0, 1}, {0, 1, 0, 0, 21, 21, 0, 2}, {0, 1, 0, 0, 20, 20, 0, 2}, {32, 1, 32, 32, 4, 4, 32, 1}, {32, 1, 32, 32, 5, 5, 32, 1}};
    private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L = new byte[][]{{1, 0, 1, 1, 0, 0, 0, 0}, {1, 0, 1, 1, 20, 20, 0, 1}, {1, 0, 1, 1, 0, 0, 0, 1}, {1, 0, 1, 1, 5, 5, 0, 1}, {33, 0, 33, 33, 4, 4, 0, 0}, {1, 0, 1, 1, 5, 5, 0, 0}};
    private static final ImpTabPair impTab_INVERSE_NUMBERS_AS_L = new ImpTabPair(impTabL_INVERSE_NUMBERS_AS_L, impTabR_INVERSE_NUMBERS_AS_L, impAct0, impAct0);
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 2, 19, 19, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {33, 48, 6, 4, 3, 3, 48, 0}, {33, 48, 6, 4, 5, 5, 48, 3}, {33, 48, 6, 4, 5, 5, 48, 2}, {33, 48, 6, 4, 3, 3, 48, 1}};
    private static final short[] impAct1 = new short[]{0, 1, 11, 12};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT = new ImpTabPair(impTabL_DEFAULT, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{0, 99, 0, 1, 0, 0, 0, 0}, {0, 99, 0, 1, 18, 48, 0, 4}, {32, 99, 32, 1, 2, 48, 32, 3}, {0, 99, 85, 86, 20, 48, 0, 3}, {48, 67, 85, 86, 4, 48, 48, 3}, {48, 67, 5, 86, 20, 48, 48, 4}, {48, 67, 85, 6, 20, 48, 48, 4}};
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{19, 0, 1, 1, 0, 0, 0, 0}, {35, 0, 1, 1, 2, 64, 0, 1}, {35, 0, 1, 1, 2, 64, 0, 0}, {3, 0, 3, 54, 20, 64, 0, 1}, {83, 64, 5, 54, 4, 64, 64, 0}, {83, 64, 5, 54, 4, 64, 64, 1}, {83, 64, 6, 6, 4, 64, 64, 3}};
    private static final short[] impAct2 = new short[]{0, 1, 7, 8, 9, 10};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new byte[][]{{0, 98, 1, 1, 0, 0, 0, 0}, {0, 98, 1, 1, 0, 48, 0, 4}, {0, 98, 84, 84, 19, 48, 0, 3}, {48, 66, 84, 84, 3, 48, 48, 3}, {48, 66, 4, 4, 19, 48, 48, 4}};
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
    static final int FIRSTALLOC = 10;
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
    public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;

    static int DirPropFlag(byte dir) {
        return 1 << dir;
    }

    boolean testDirPropFlagAt(int flag, int index) {
        return (Bidi.DirPropFlag((byte)(this.dirProps[index] & 0xFFFFFFBF)) & flag) != 0;
    }

    static byte NoContextRTL(byte dir) {
        return (byte)(dir & 0xFFFFFFBF);
    }

    static int DirPropFlagNC(byte dir) {
        return 1 << (dir & 0xFFFFFFBF);
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

    static byte GetLRFromLevel(byte level) {
        return (byte)(level & 1);
    }

    static boolean IsDefaultLevel(byte level) {
        return (level & 0x7E) == 126;
    }

    byte GetParaLevelAt(int index) {
        return this.defaultParaLevel != 0 ? (byte)(this.dirProps[index] >> 6) : this.paraLevel;
    }

    static boolean IsBidiControlChar(int c) {
        return (c & 0xFFFFFFFC) == 8204 || c >= 8234 && c <= 8238;
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
        catch (Exception e) {
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

    private void getInitialParasMemory(int len) {
        Object array = this.getMemory("Paras", this.parasMemory, Integer.TYPE, true, len);
        this.parasMemory = (int[])array;
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

    private byte firstL_R_AL() {
        int uchar;
        int result = 10;
        for (int i = 0; i < this.prologue.length(); i += Character.charCount(uchar)) {
            uchar = this.prologue.codePointAt(i);
            int dirProp = this.getCustomizedClass(uchar);
            if (result != 10) continue;
            if (dirProp != 0 && dirProp != 1 && dirProp != 13) continue;
            result = dirProp;
        }
        return (byte)result;
    }

    private void getDirProps() {
        int i1;
        int state;
        int paraDir;
        int i = 0;
        this.flags = 0;
        int paraDirDefault = 0;
        boolean isDefaultLevel = Bidi.IsDefaultLevel(this.paraLevel);
        boolean isDefaultLevelInverse = isDefaultLevel && (this.reorderingMode == 5 || this.reorderingMode == 6);
        this.lastArabicPos = -1;
        this.controlCount = 0;
        boolean removeBidiControls = (this.reorderingOptions & 2) != 0;
        boolean NOT_CONTEXTUAL = false;
        boolean LOOKING_FOR_STRONG = true;
        int FOUND_STRONG_CHAR = 2;
        int paraStart = 0;
        int lastStrongDir = 0;
        int lastStrongLTR = 0;
        if ((this.reorderingOptions & 4) > 0) {
            this.length = 0;
            lastStrongLTR = 0;
        }
        if (isDefaultLevel) {
            byte lastStrong;
            int n = paraDirDefault = (this.paraLevel & 1) != 0 ? 64 : 0;
            if (this.prologue != null && (lastStrong = this.firstL_R_AL()) != 10) {
                paraDir = lastStrong == 0 ? 0 : 64;
                state = 2;
            } else {
                paraDir = paraDirDefault;
                state = 1;
            }
            state = 1;
        } else {
            state = 0;
            paraDir = 0;
        }
        i = 0;
        while (i < this.originalLength) {
            int i0 = i;
            int uchar = UTF16.charAt(this.text, 0, this.originalLength, i);
            i1 = (i += UTF16.getCharCount(uchar)) - 1;
            byte dirProp = (byte)this.getCustomizedClass(uchar);
            this.flags |= Bidi.DirPropFlag(dirProp);
            this.dirProps[i1] = (byte)(dirProp | paraDir);
            if (i1 > i0) {
                this.flags |= Bidi.DirPropFlag((byte)18);
                do {
                    this.dirProps[--i1] = (byte)(0x12 | paraDir);
                } while (i1 > i0);
            }
            if (state == 1) {
                if (dirProp == 0) {
                    state = 2;
                    if (paraDir == 0) continue;
                    paraDir = 0;
                    i1 = paraStart;
                    while (i1 < i) {
                        int n = i1++;
                        this.dirProps[n] = (byte)(this.dirProps[n] & 0xFFFFFFBF);
                    }
                    continue;
                }
                if (dirProp == 1 || dirProp == 13) {
                    state = 2;
                    if (paraDir != 0) continue;
                    paraDir = 64;
                    i1 = paraStart;
                    while (i1 < i) {
                        int n = i1++;
                        this.dirProps[n] = (byte)(this.dirProps[n] | 0x40);
                    }
                    continue;
                }
            }
            if (dirProp == 0) {
                lastStrongDir = 0;
                lastStrongLTR = i;
            } else if (dirProp == 1) {
                lastStrongDir = 64;
            } else if (dirProp == 13) {
                lastStrongDir = 64;
                this.lastArabicPos = i - 1;
            } else if (dirProp == 7) {
                if ((this.reorderingOptions & 4) != 0) {
                    this.length = i;
                }
                if (isDefaultLevelInverse && lastStrongDir == 64 && paraDir != lastStrongDir) {
                    while (paraStart < i) {
                        int n = paraStart++;
                        this.dirProps[n] = (byte)(this.dirProps[n] | 0x40);
                    }
                }
                if (i < this.originalLength) {
                    if (uchar != 13 || this.text[i] != '\n') {
                        ++this.paraCount;
                    }
                    if (isDefaultLevel) {
                        state = 1;
                        paraStart = i;
                        paraDir = paraDirDefault;
                        lastStrongDir = paraDirDefault;
                    }
                }
            }
            if (!removeBidiControls || !Bidi.IsBidiControlChar(uchar)) continue;
            ++this.controlCount;
        }
        if (isDefaultLevelInverse && lastStrongDir == 64 && paraDir != lastStrongDir) {
            i1 = paraStart;
            while (i1 < this.originalLength) {
                int n = i1++;
                this.dirProps[n] = (byte)(this.dirProps[n] | 0x40);
            }
        }
        if (isDefaultLevel) {
            this.paraLevel = this.GetParaLevelAt(0);
        }
        if ((this.reorderingOptions & 4) > 0) {
            if (lastStrongLTR > this.length && this.GetParaLevelAt(lastStrongLTR) == 0) {
                this.length = lastStrongLTR;
            }
            if (this.length < this.originalLength) {
                --this.paraCount;
            }
        }
        this.flags |= Bidi.DirPropFlagLR(this.paraLevel);
        if (this.orderParagraphsLTR && (this.flags & Bidi.DirPropFlag((byte)7)) != 0) {
            this.flags |= Bidi.DirPropFlag((byte)0);
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
        int i = 0;
        byte level = this.GetParaLevelAt(0);
        int paraIndex = 0;
        byte dirct = this.directionFromFlags();
        if (dirct == 2 || this.paraCount != 1) {
            if (this.paraCount == 1 && ((this.flags & MASK_EXPLICIT) == 0 || this.reorderingMode > 1)) {
                for (i = 0; i < this.length; ++i) {
                    this.levels[i] = level;
                }
            } else {
                byte embeddingLevel = level;
                int stackTop = 0;
                byte[] stack = new byte[61];
                int countOver60 = 0;
                int countOver61 = 0;
                this.flags = 0;
                for (i = 0; i < this.length; ++i) {
                    byte dirProp = Bidi.NoContextRTL(this.dirProps[i]);
                    switch (dirProp) {
                        case 11: 
                        case 12: {
                            byte newLevel = (byte)(embeddingLevel + 2 & 0x7E);
                            if (newLevel <= 61) {
                                stack[stackTop] = embeddingLevel;
                                stackTop = (byte)(stackTop + 1);
                                embeddingLevel = newLevel;
                                if (dirProp == 12) {
                                    embeddingLevel = (byte)(embeddingLevel | 0xFFFFFF80);
                                }
                            } else if ((embeddingLevel & 0x7F) == 61) {
                                ++countOver61;
                            } else {
                                ++countOver60;
                            }
                            this.flags |= Bidi.DirPropFlag((byte)18);
                            break;
                        }
                        case 14: 
                        case 15: {
                            byte newLevel = (byte)((embeddingLevel & 0x7F) + 1 | 1);
                            if (newLevel <= 61) {
                                stack[stackTop] = embeddingLevel;
                                stackTop = (byte)(stackTop + 1);
                                embeddingLevel = newLevel;
                                if (dirProp == 15) {
                                    embeddingLevel = (byte)(embeddingLevel | 0xFFFFFF80);
                                }
                            } else {
                                ++countOver61;
                            }
                            this.flags |= Bidi.DirPropFlag((byte)18);
                            break;
                        }
                        case 16: {
                            if (countOver61 > 0) {
                                --countOver61;
                            } else if (countOver60 > 0 && (embeddingLevel & 0x7F) != 61) {
                                --countOver60;
                            } else if (stackTop > 0) {
                                stackTop = (byte)(stackTop - 1);
                                embeddingLevel = stack[stackTop];
                            }
                            this.flags |= Bidi.DirPropFlag((byte)18);
                            break;
                        }
                        case 7: {
                            stackTop = 0;
                            countOver60 = 0;
                            countOver61 = 0;
                            level = this.GetParaLevelAt(i);
                            if (i + 1 < this.length) {
                                embeddingLevel = this.GetParaLevelAt(i + 1);
                                if (this.text[i] != '\r' || this.text[i + 1] != '\n') {
                                    this.paras[paraIndex++] = i + 1;
                                }
                            }
                            this.flags |= Bidi.DirPropFlag((byte)7);
                            break;
                        }
                        case 18: {
                            this.flags |= Bidi.DirPropFlag((byte)18);
                            break;
                        }
                        default: {
                            if (level != embeddingLevel) {
                                level = embeddingLevel;
                                this.flags = (level & 0xFFFFFF80) != 0 ? (this.flags |= Bidi.DirPropFlagO(level) | DirPropFlagMultiRuns) : (this.flags |= Bidi.DirPropFlagE(level) | DirPropFlagMultiRuns);
                            }
                            if ((level & 0xFFFFFF80) != 0) break;
                            this.flags |= Bidi.DirPropFlag(dirProp);
                        }
                    }
                    this.levels[i] = level;
                }
                if ((this.flags & MASK_EMBEDDING) != 0) {
                    this.flags |= Bidi.DirPropFlagLR(this.paraLevel);
                }
                if (this.orderParagraphsLTR && (this.flags & Bidi.DirPropFlag((byte)7)) != 0) {
                    this.flags |= Bidi.DirPropFlag((byte)0);
                }
                dirct = this.directionFromFlags();
            }
        }
        return dirct;
    }

    private byte checkExplicitLevels() {
        this.flags = 0;
        int paraIndex = 0;
        for (int i = 0; i < this.length; ++i) {
            byte level = this.levels[i];
            byte dirProp = Bidi.NoContextRTL(this.dirProps[i]);
            if ((level & 0xFFFFFF80) != 0) {
                level = (byte)(level & 0x7F);
                this.flags |= Bidi.DirPropFlagO(level);
            } else {
                this.flags |= Bidi.DirPropFlagE(level) | Bidi.DirPropFlag(dirProp);
            }
            if (level < this.GetParaLevelAt(i) && (0 != level || dirProp != 7) || 61 < level) {
                throw new IllegalArgumentException("level " + level + " out of bounds at " + i);
            }
            if (dirProp != 7 || i + 1 >= this.length || this.text[i] == '\r' && this.text[i + 1] == '\n') continue;
            this.paras[paraIndex++] = i + 1;
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

    private void processPropertySeq(LevState levState, short _prop, int start, int limit) {
        int k;
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
                    for (k = levState.lastStrongRTL + 1; k < start0; ++k) {
                        this.levels[k] = (byte)(this.levels[k] - 2 & 0xFFFFFFFE);
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.lastStrongRTL = -1;
                    if (_prop != 5) break;
                    this.addPoint(start0, 1);
                    this.insertPoints.confirmed = this.insertPoints.size;
                    break;
                }
                case 4: {
                    if (this.insertPoints.points.length > 0) {
                        this.insertPoints.size = this.insertPoints.confirmed;
                    }
                    levState.startON = -1;
                    levState.startL2EN = -1;
                    levState.lastStrongRTL = limit - 1;
                    break;
                }
                case 5: {
                    if (_prop == 3 && Bidi.NoContextRTL(this.dirProps[start0]) == 5 && this.reorderingMode != 6) {
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
                case 6: {
                    levState.lastStrongRTL = limit - 1;
                    levState.startON = -1;
                    break;
                }
                case 7: {
                    for (k = start0 - 1; k >= 0 && (this.levels[k] & 1) == 0; --k) {
                    }
                    if (k >= 0) {
                        this.addPoint(k, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                    }
                    levState.startON = start0;
                    break;
                }
                case 8: {
                    this.addPoint(start0, 1);
                    this.addPoint(start0, 2);
                    break;
                }
                case 9: {
                    this.insertPoints.size = this.insertPoints.confirmed;
                    if (_prop != 5) break;
                    this.addPoint(start0, 4);
                    this.insertPoints.confirmed = this.insertPoints.size;
                    break;
                }
                case 10: {
                    level = (byte)(levState.runLevel + addLevel);
                    for (k = levState.startON; k < start0; ++k) {
                        if (this.levels[k] >= level) continue;
                        this.levels[k] = level;
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.startON = start0;
                    break;
                }
                case 11: {
                    level = levState.runLevel;
                    for (k = start0 - 1; k >= levState.startON; --k) {
                        if (this.levels[k] == level + 3) {
                            while (this.levels[k] == level + 3) {
                                int n = k--;
                                this.levels[n] = (byte)(this.levels[n] - 2);
                            }
                            while (this.levels[k] == level) {
                                --k;
                            }
                        }
                        this.levels[k] = this.levels[k] == level + 2 ? level : (byte)(level + 1);
                    }
                    break;
                }
                case 12: {
                    level = (byte)(levState.runLevel + 1);
                    for (k = start0 - 1; k >= levState.startON; --k) {
                        if (this.levels[k] <= level) continue;
                        int n = k;
                        this.levels[n] = (byte)(this.levels[n] - 2);
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
            for (k = start; k < limit; ++k) {
                this.levels[k] = level;
            }
        }
    }

    private byte lastL_R_AL() {
        int uchar;
        for (int i = this.prologue.length(); i > 0; i -= Character.charCount(uchar)) {
            uchar = this.prologue.codePointBefore(i);
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
        for (int i = 0; i < this.epilogue.length(); i += Character.charCount(uchar)) {
            uchar = this.epilogue.codePointAt(i);
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
        byte lastStrong;
        LevState levState = new LevState();
        int nextStrongProp = 1;
        int nextStrongPos = -1;
        boolean inverseRTL = start < this.lastArabicPos && (this.GetParaLevelAt(start) & 1) > 0 && (this.reorderingMode == 5 || this.reorderingMode == 6);
        levState.startL2EN = -1;
        levState.lastStrongRTL = -1;
        levState.state = 0;
        levState.runLevel = this.levels[start];
        levState.impTab = this.impTabPair.imptab[levState.runLevel & 1];
        levState.impAct = this.impTabPair.impact[levState.runLevel & 1];
        if (start == 0 && this.prologue != null && (lastStrong = this.lastL_R_AL()) != 4) {
            sor = lastStrong;
        }
        this.processPropertySeq(levState, sor, start, start);
        short stateImp = Bidi.NoContextRTL(this.dirProps[start]) == 17 ? (short)(1 + sor) : (short)0;
        int start1 = start;
        int start2 = 0;
        block6: for (int i = start; i <= limit; ++i) {
            short gprop;
            if (i >= limit) {
                gprop = eor;
            } else {
                int prop = Bidi.NoContextRTL(this.dirProps[i]);
                if (inverseRTL) {
                    if (prop == 13) {
                        prop = 1;
                    } else if (prop == 2) {
                        if (nextStrongPos <= i) {
                            nextStrongProp = 1;
                            nextStrongPos = limit;
                            for (int j = i + 1; j < limit; ++j) {
                                short prop1 = Bidi.NoContextRTL(this.dirProps[j]);
                                if (prop1 != 0 && prop1 != 1 && prop1 != 13) continue;
                                nextStrongProp = prop1;
                                nextStrongPos = j;
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
            if (i == limit && actionImp == 0) {
                actionImp = 1;
            }
            if (actionImp == 0) continue;
            short resProp = impTabProps[oldStateImp][13];
            switch (actionImp) {
                case 1: {
                    this.processPropertySeq(levState, resProp, start1, i);
                    start1 = i;
                    continue block6;
                }
                case 2: {
                    start2 = i;
                    continue block6;
                }
                case 3: {
                    this.processPropertySeq(levState, resProp, start1, start2);
                    this.processPropertySeq(levState, (short)4, start2, i);
                    start1 = i;
                    continue block6;
                }
                case 4: {
                    this.processPropertySeq(levState, resProp, start1, start2);
                    start1 = start2;
                    start2 = i;
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
        this.processPropertySeq(levState, eor, limit, limit);
    }

    private void adjustWSLevels() {
        if ((this.flags & MASK_WS) != 0) {
            int i = this.trailingWSStart;
            block0: while (i > 0) {
                int flag;
                while (i > 0 && ((flag = Bidi.DirPropFlagNC(this.dirProps[--i])) & MASK_WS) != 0) {
                    if (this.orderParagraphsLTR && (flag & Bidi.DirPropFlag((byte)7)) != 0) {
                        this.levels[i] = 0;
                        continue;
                    }
                    this.levels[i] = this.GetParaLevelAt(i);
                }
                while (i > 0) {
                    if (((flag = Bidi.DirPropFlagNC(this.dirProps[--i])) & MASK_BN_EXPLICIT) != 0) {
                        this.levels[i] = this.levels[i + 1];
                        continue;
                    }
                    if (this.orderParagraphsLTR && (flag & Bidi.DirPropFlag((byte)7)) != 0) {
                        this.levels[i] = 0;
                        continue block0;
                    }
                    if ((flag & MASK_B_S) == 0) continue;
                    this.levels[i] = this.GetParaLevelAt(i);
                    continue block0;
                }
            }
        }
    }

    int Bidi_Min(int x, int y) {
        return x < y ? x : y;
    }

    int Bidi_Abs(int x) {
        return x >= 0 ? x : -x;
    }

    void setParaRunsOnly(char[] parmText, byte parmParaLevel) {
        int index1;
        int index;
        int j;
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
        int i = 0;
        while (i < oldRunCount) {
            runLength = this.runs[i].limit - visualStart;
            if (runLength >= 2) {
                logicalStart = this.runs[i].start;
                for (j = logicalStart + 1; j < logicalStart + runLength; ++j) {
                    index = visualMap[j];
                    index1 = visualMap[j - 1];
                    if (this.Bidi_Abs(index - index1) == 1 && saveLevels[index] == saveLevels[index1]) continue;
                    ++addedRuns;
                }
            }
            ++i;
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
            for (i = oldRunCount; i < this.runCount; ++i) {
                if (this.runs[i] != null) continue;
                this.runs[i] = new BidiRun(0, 0, 0);
            }
        }
        for (i = oldRunCount - 1; i >= 0; --i) {
            int step;
            int limit;
            int start;
            int logicalPos;
            int newI = i + addedRuns;
            runLength = i == 0 ? this.runs[0].limit : this.runs[i].limit - this.runs[i - 1].limit;
            logicalStart = this.runs[i].start;
            int indexOddBit = this.runs[i].level & 1;
            if (runLength < 2) {
                if (addedRuns > 0) {
                    this.runs[newI].copyFrom(this.runs[i]);
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
            for (j = start; j != limit; j += step) {
                int insertRemove;
                index = visualMap[j];
                index1 = visualMap[j + step];
                if (this.Bidi_Abs(index - index1) == 1 && saveLevels[index] == saveLevels[index1]) continue;
                this.runs[newI].start = logicalPos = this.Bidi_Min(visualMap[start], index);
                this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
                this.runs[newI].limit = this.runs[i].limit;
                this.runs[i].limit -= this.Bidi_Abs(j - start) + 1;
                this.runs[newI].insertRemove = insertRemove = this.runs[i].insertRemove & 0xA;
                this.runs[i].insertRemove &= ~insertRemove;
                start = j + step;
                --addedRuns;
                --newI;
            }
            if (addedRuns > 0) {
                this.runs[newI].copyFrom(this.runs[i]);
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

    private void setParaSuccess() {
        this.prologue = null;
        this.epilogue = null;
        this.paraBidi = this;
    }

    public void setContext(String prologue, String epilogue) {
        this.prologue = prologue != null && prologue.length() > 0 ? prologue : null;
        this.epilogue = epilogue != null && epilogue.length() > 0 ? epilogue : null;
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
        if (paraLevel < 126) {
            this.verifyRange(paraLevel, 0, 62);
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
        this.direction = 0;
        this.paraCount = 1;
        this.dirProps = new byte[0];
        this.levels = new byte[0];
        this.runs = new BidiRun[0];
        this.isGoodLogicalToVisualRunsMap = false;
        this.insertPoints.size = 0;
        this.insertPoints.confirmed = 0;
        this.defaultParaLevel = Bidi.IsDefaultLevel(paraLevel) ? paraLevel : (byte)0;
        if (this.length == 0) {
            if (Bidi.IsDefaultLevel(paraLevel)) {
                this.paraLevel = (byte)(this.paraLevel & 1);
                this.defaultParaLevel = 0;
            }
            if ((this.paraLevel & 1) != 0) {
                this.flags = Bidi.DirPropFlag((byte)1);
                this.direction = 1;
            } else {
                this.flags = Bidi.DirPropFlag((byte)0);
                this.direction = 0;
            }
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
        if (this.paraCount > 1) {
            this.getInitialParasMemory(this.paraCount);
            this.paras = this.parasMemory;
            this.paras[this.paraCount - 1] = this.length;
        } else {
            this.paras = this.simpleParas;
            this.simpleParas[0] = this.length;
        }
        if (embeddingLevels == null) {
            this.getLevelsMemory(this.length);
            this.levels = this.levelsMemory;
            this.direction = this.resolveExplicitLevels();
        } else {
            this.levels = embeddingLevels;
            this.direction = this.checkExplicitLevels();
        }
        switch (this.direction) {
            case 0: {
                paraLevel = (byte)(paraLevel + 1 & 0xFFFFFFFE);
                this.trailingWSStart = 0;
                break;
            }
            case 1: {
                paraLevel = (byte)(paraLevel | 1);
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
                    byte level = this.GetParaLevelAt(0);
                    short eor = level < (nextLevel = this.levels[0]) ? (short)Bidi.GetLRFromLevel(nextLevel) : (short)Bidi.GetLRFromLevel(level);
                    do {
                        start = limit;
                        level = nextLevel;
                        short sor = start > 0 && Bidi.NoContextRTL(this.dirProps[start - 1]) == 7 ? (short)Bidi.GetLRFromLevel(this.GetParaLevelAt(start)) : eor;
                        while (++limit < this.length && this.levels[limit] == level) {
                        }
                        nextLevel = limit < this.length ? this.levels[limit] : this.GetParaLevelAt(this.length - 1);
                        eor = (level & 0x7F) < (nextLevel & 0x7F) ? (short)Bidi.GetLRFromLevel(nextLevel) : (short)Bidi.GetLRFromLevel(level);
                        if ((level & 0xFFFFFF80) == 0) {
                            this.resolveImplicitLevels(start, limit, sor, eor);
                            continue;
                        }
                        do {
                            int n = start++;
                            this.levels[n] = (byte)(this.levels[n] & 0x7F);
                        } while (start < limit);
                    } while (limit < this.length);
                }
                this.adjustWSLevels();
            }
        }
        if (this.defaultParaLevel > 0 && (this.reorderingOptions & 1) != 0 && (this.reorderingMode == 5 || this.reorderingMode == 6)) {
            block16: for (int i = 0; i < this.paraCount; ++i) {
                int last = this.paras[i] - 1;
                if ((this.dirProps[last] & 0x40) == 0) continue;
                start = i == 0 ? 0 : this.paras[i - 1];
                for (int j = last; j >= start; --j) {
                    byte dirProp = Bidi.NoContextRTL(this.dirProps[j]);
                    if (dirProp == 0) {
                        if (j < last) {
                            while (Bidi.NoContextRTL(this.dirProps[last]) == 7) {
                                --last;
                            }
                        }
                        this.addPoint(last, 4);
                        continue block16;
                    }
                    if ((Bidi.DirPropFlag(dirProp) & 0x2002) != 0) continue block16;
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
        int i = 0;
        char ch = paragraph.first();
        while (ch != '\uffff') {
            byte level;
            txt[i] = ch;
            Integer embedding = (Integer)paragraph.getAttribute(TextAttribute.BIDI_EMBEDDING);
            if (embedding != null && (level = embedding.byteValue()) != 0) {
                if (level < 0) {
                    lvls = embeddingLevels;
                    embeddingLevels[i] = (byte)(0 - level | 0xFFFFFF80);
                } else {
                    lvls = embeddingLevels;
                    embeddingLevels[i] = level;
                }
            }
            ch = paragraph.next();
            ++i;
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
        int paraStart = paraIndex == 0 ? 0 : bidi.paras[paraIndex - 1];
        BidiRun bidiRun = new BidiRun();
        bidiRun.start = paraStart;
        bidiRun.limit = bidi.paras[paraIndex];
        bidiRun.level = this.GetParaLevelAt(paraStart);
        return bidiRun;
    }

    public BidiRun getParagraph(int charIndex) {
        this.verifyValidParaOrLine();
        Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex = 0;
        while (charIndex >= bidi.paras[paraIndex]) {
            ++paraIndex;
        }
        return this.getParagraphByIndex(paraIndex);
    }

    public int getParagraphIndex(int charIndex) {
        this.verifyValidParaOrLine();
        Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex = 0;
        while (charIndex >= bidi.paras[paraIndex]) {
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

    public int getCustomizedClass(int c) {
        int dir;
        if (this.customClassifier == null || (dir = this.customClassifier.classify(c)) == 19) {
            return this.bdp.getClass(c);
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
            for (int i = 0; i < paragraphLength; ++i) {
                byte lev = embeddings[i + embStart];
                if (lev < 0) {
                    lev = (byte)(-lev | 0xFFFFFF80);
                } else if (lev == 0) {
                    lev = paraLvl;
                    if (paraLvl > 61) {
                        lev = (byte)(lev & 1);
                    }
                }
                paraEmbeddings[i] = lev;
            }
        }
        if (textStart == 0 && embStart == 0 && paragraphLength == text.length) {
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
        int i;
        if (this.isGoodLogicalToVisualRunsMap) {
            return;
        }
        int count = this.countRuns();
        if (this.logicalToVisualRunsMap == null || this.logicalToVisualRunsMap.length < count) {
            this.logicalToVisualRunsMap = new int[count];
        }
        long[] keys = new long[count];
        for (i = 0; i < count; ++i) {
            keys[i] = ((long)this.runs[i].start << 32) + (long)i;
        }
        Arrays.sort(keys);
        for (i = 0; i < count; ++i) {
            this.logicalToVisualRunsMap[i] = (int)(keys[i] & 0xFFFFFFFFFFFFFFFFL);
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
        for (int i = start; i < limit; ++i) {
            if ((1 << UCharacter.getDirection(text[i]) & 0xE022) == 0) continue;
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
        for (int i = 0; i < count; ++i) {
            objects[objectStart + i] = temp[indexMap[i]];
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

    public static byte getBaseDirection(CharSequence paragraph) {
        if (paragraph == null || paragraph.length() == 0) {
            return 3;
        }
        int length = paragraph.length();
        int i = 0;
        while (i < length) {
            int c = UCharacter.codePointAt(paragraph, i);
            byte direction = UCharacter.getDirectionality(c);
            if (direction == 0) {
                return 0;
            }
            if (direction == 1 || direction == 13) {
                return 1;
            }
            i = UCharacter.offsetByCodePoints(paragraph, i, 1);
        }
        return 3;
    }

    private static class LevState {
        byte[][] impTab;
        short[] impAct;
        int startON;
        int startL2EN;
        int lastStrongRTL;
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

