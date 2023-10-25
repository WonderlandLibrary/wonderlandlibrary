package ru.salam4ik.bot.bot.network;

import java.util.HashMap;
import java.util.Map;

public class BasicColor {
    final int g;
    final int b;
    public static final BasicColor TRANSPARENT;
    final int r;
    public static Map<Integer, BasicColor> colors;

    private static BasicColor toCol(int n, int n2, int n3) {
        return new BasicColor(n, n2, n3);
    }

    static {
        TRANSPARENT = new BasicColor(0, 0, 0){

            @Override
            public int shaded(byte by) {
                return 0;
            }
        };
        colors = new HashMap<Integer, BasicColor>();
        colors.put(0, TRANSPARENT);
        colors.put(1, BasicColor.toCol(127, 178, 56));
        colors.put(2, BasicColor.toCol(247, 233, 163));
        colors.put(3, BasicColor.toCol(199, 199, 199));
        colors.put(4, BasicColor.toCol(255, 0, 0));
        colors.put(5, BasicColor.toCol(160, 160, 255));
        colors.put(6, BasicColor.toCol(167, 167, 167));
        colors.put(7, BasicColor.toCol(0, 124, 0));
        colors.put(8, BasicColor.toCol(255, 255, 255));
        colors.put(9, BasicColor.toCol(164, 168, 184));
        colors.put(10, BasicColor.toCol(151, 109, 77));
        colors.put(11, BasicColor.toCol(112, 112, 112));
        colors.put(12, BasicColor.toCol(64, 64, 255));
        colors.put(13, BasicColor.toCol(143, 119, 72));
        colors.put(14, BasicColor.toCol(255, 252, 245));
        colors.put(15, BasicColor.toCol(216, 127, 51));
        colors.put(16, BasicColor.toCol(178, 76, 216));
        colors.put(17, BasicColor.toCol(102, 153, 216));
        colors.put(18, BasicColor.toCol(229, 229, 51));
        colors.put(19, BasicColor.toCol(127, 204, 25));
        colors.put(20, BasicColor.toCol(242, 127, 165));
        colors.put(21, BasicColor.toCol(76, 76, 76));
        colors.put(22, BasicColor.toCol(153, 153, 153));
        colors.put(23, BasicColor.toCol(76, 127, 153));
        colors.put(24, BasicColor.toCol(127, 63, 178));
        colors.put(25, BasicColor.toCol(51, 76, 178));
        colors.put(26, BasicColor.toCol(102, 76, 51));
        colors.put(27, BasicColor.toCol(102, 127, 51));
        colors.put(28, BasicColor.toCol(153, 51, 51));
        colors.put(29, BasicColor.toCol(25, 25, 25));
        colors.put(30, BasicColor.toCol(250, 238, 77));
        colors.put(31, BasicColor.toCol(92, 219, 213));
        colors.put(32, BasicColor.toCol(74, 128, 255));
        colors.put(33, BasicColor.toCol(0, 217, 58));
        colors.put(34, BasicColor.toCol(129, 86, 49));
        colors.put(35, BasicColor.toCol(112, 2, 0));
        colors.put(36, BasicColor.toCol(209, 177, 161));
        colors.put(37, BasicColor.toCol(159, 82, 36));
        colors.put(38, BasicColor.toCol(149, 87, 108));
        colors.put(39, BasicColor.toCol(112, 108, 138));
        colors.put(40, BasicColor.toCol(186, 133, 36));
        colors.put(41, BasicColor.toCol(103, 117, 53));
        colors.put(42, BasicColor.toCol(160, 77, 78));
        colors.put(43, BasicColor.toCol(57, 41, 35));
        colors.put(44, BasicColor.toCol(135, 107, 98));
        colors.put(45, BasicColor.toCol(87, 92, 92));
        colors.put(46, BasicColor.toCol(122, 73, 88));
        colors.put(47, BasicColor.toCol(76, 62, 92));
        colors.put(48, BasicColor.toCol(76, 50, 35));
        colors.put(49, BasicColor.toCol(76, 82, 42));
        colors.put(50, BasicColor.toCol(142, 60, 46));
        colors.put(51, BasicColor.toCol(37, 22, 16));
        colors.put(52, BasicColor.toCol(189, 48, 49));
        colors.put(53, BasicColor.toCol(148, 63, 97));
        colors.put(54, BasicColor.toCol(92, 25, 29));
        colors.put(55, BasicColor.toCol(22, 126, 134));
        colors.put(56, BasicColor.toCol(58, 142, 140));
        colors.put(57, BasicColor.toCol(86, 44, 62));
        colors.put(58, BasicColor.toCol(20, 180, 133));
    }

    public BasicColor(int n, int n2, int n3) {
        this.r = n;
        this.g = n2;
        this.b = n3;
    }

    public int shaded(byte by) {
        double d = 0.0;
        switch (by) {
            case 0: {
                d = 0.71;
                break;
            }
            case 1: {
                d = 0.85;
                break;
            }
            case 2: {
                d = 1.0;
                break;
            }
            case 3: {
                d = 0.53;
            }
        }
        return 0xFF000000 | this.toInt(this.r, d) << 16 | this.toInt(this.g, d) << 8 | this.toInt(this.b, d);
    }

    private int toInt(int n, double d) {
        return (int)Math.round((double)n * d);
    }
}