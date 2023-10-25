/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

public class LogTable {
    private static final int TABLE_SHIFT = 7;
    private static final int INTERP_SHIFT = 8;
    private static final int INTERP_MASK = 255;
    private static final int[] exp_2_table = new int[]{32768, 32945, 33124, 33304, 33485, 33667, 33850, 34033, 34218, 34404, 34591, 34779, 34968, 35157, 35348, 35540, 35733, 35927, 36122, 36319, 36516, 36714, 36913, 37114, 37315, 37518, 37722, 37926, 38132, 38339, 38548, 38757, 38967, 39179, 39392, 39606, 39821, 40037, 40254, 40473, 40693, 40914, 41136, 41359, 41584, 41810, 42037, 42265, 42494, 42725, 42957, 43190, 43425, 43661, 43898, 44136, 44376, 44617, 44859, 45103, 45347, 45594, 45841, 46090, 46340, 46592, 46845, 47099, 47355, 47612, 47871, 48131, 48392, 48655, 48919, 49185, 49452, 49720, 49990, 50262, 50535, 50809, 51085, 51362, 51641, 51922, 52204, 52487, 52772, 53059, 53347, 53636, 53928, 54220, 54515, 54811, 55108, 55408, 55709, 56011, 56315, 56621, 56928, 57238, 57548, 57861, 58175, 58491, 58809, 59128, 59449, 59772, 60096, 60423, 60751, 61081, 61412, 61746, 62081, 62418, 62757, 63098, 63440, 63785, 64131, 64479, 64830, 65182, 65536};
    private static final int[] log_2_table;

    public static int raise_2(int n) {
        int n2 = LogTable.eval_table(exp_2_table, n & Short.MAX_VALUE) << 15;
        return n2 >> 15 - (n >> 15);
    }

    public static int log_2(int n) {
        int n2 = 15;
        while (n < 32768) {
            n <<= 1;
            --n2;
        }
        while (n >= 65536) {
            n >>= 1;
            ++n2;
        }
        return 32768 * n2 + LogTable.eval_table(log_2_table, n - 32768);
    }

    private static int eval_table(int[] nArray, int n) {
        int n2 = n >> 8;
        int n3 = n & 0xFF;
        int n4 = nArray[n2];
        int n5 = nArray[n2 + 1] - n4;
        int n6 = (n5 * n3 >> 8) + n4;
        return n6 >> 0;
    }

    static {
        int[] nArray = new int[129];
        nArray[1] = 367;
        nArray[2] = 732;
        nArray[3] = 1095;
        nArray[4] = 1454;
        nArray[5] = 1811;
        nArray[6] = 2165;
        nArray[7] = 2517;
        nArray[8] = 2865;
        nArray[9] = 3212;
        nArray[10] = 3556;
        nArray[11] = 3897;
        nArray[12] = 4236;
        nArray[13] = 4572;
        nArray[14] = 4906;
        nArray[15] = 5238;
        nArray[16] = 5568;
        nArray[17] = 5895;
        nArray[18] = 6220;
        nArray[19] = 6542;
        nArray[20] = 6863;
        nArray[21] = 7181;
        nArray[22] = 7497;
        nArray[23] = 7812;
        nArray[24] = 8124;
        nArray[25] = 8434;
        nArray[26] = 8742;
        nArray[27] = 9048;
        nArray[28] = 9352;
        nArray[29] = 9654;
        nArray[30] = 9954;
        nArray[31] = 10252;
        nArray[32] = 10548;
        nArray[33] = 10843;
        nArray[34] = 11136;
        nArray[35] = 11427;
        nArray[36] = 11716;
        nArray[37] = 12003;
        nArray[38] = 12289;
        nArray[39] = 12573;
        nArray[40] = 12855;
        nArray[41] = 13136;
        nArray[42] = 13414;
        nArray[43] = 13692;
        nArray[44] = 13967;
        nArray[45] = 14241;
        nArray[46] = 14514;
        nArray[47] = 14785;
        nArray[48] = 15054;
        nArray[49] = 15322;
        nArray[50] = 15588;
        nArray[51] = 15853;
        nArray[52] = 16117;
        nArray[53] = 16378;
        nArray[54] = 16639;
        nArray[55] = 16898;
        nArray[56] = 17156;
        nArray[57] = 17412;
        nArray[58] = 17667;
        nArray[59] = 17920;
        nArray[60] = 18172;
        nArray[61] = 18423;
        nArray[62] = 18673;
        nArray[63] = 18921;
        nArray[64] = 19168;
        nArray[65] = 19413;
        nArray[66] = 19657;
        nArray[67] = 19900;
        nArray[68] = 20142;
        nArray[69] = 20383;
        nArray[70] = 20622;
        nArray[71] = 20860;
        nArray[72] = 21097;
        nArray[73] = 21333;
        nArray[74] = 21568;
        nArray[75] = 21801;
        nArray[76] = 22034;
        nArray[77] = 22265;
        nArray[78] = 22495;
        nArray[79] = 22724;
        nArray[80] = 22952;
        nArray[81] = 23178;
        nArray[82] = 23404;
        nArray[83] = 23628;
        nArray[84] = 23852;
        nArray[85] = 24074;
        nArray[86] = 24296;
        nArray[87] = 24516;
        nArray[88] = 24736;
        nArray[89] = 24954;
        nArray[90] = 25171;
        nArray[91] = 25388;
        nArray[92] = 25603;
        nArray[93] = 25817;
        nArray[94] = 26031;
        nArray[95] = 26243;
        nArray[96] = 26455;
        nArray[97] = 26665;
        nArray[98] = 26875;
        nArray[99] = 27084;
        nArray[100] = 27292;
        nArray[101] = 27499;
        nArray[102] = 27705;
        nArray[103] = 27910;
        nArray[104] = 28114;
        nArray[105] = 28317;
        nArray[106] = 28520;
        nArray[107] = 28721;
        nArray[108] = 28922;
        nArray[109] = 29122;
        nArray[110] = 29321;
        nArray[111] = 29519;
        nArray[112] = 29716;
        nArray[113] = 29913;
        nArray[114] = 30109;
        nArray[115] = 30304;
        nArray[116] = 30498;
        nArray[117] = 30691;
        nArray[118] = 30884;
        nArray[119] = 31076;
        nArray[120] = 31267;
        nArray[121] = 31457;
        nArray[122] = 31646;
        nArray[123] = 31835;
        nArray[124] = 32023;
        nArray[125] = 32210;
        nArray[126] = 32397;
        nArray[127] = 32582;
        nArray[128] = 32768;
        log_2_table = nArray;
    }
}

