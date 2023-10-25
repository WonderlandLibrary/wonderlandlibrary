/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.material;

public class MapColor {
    public static final MapColor emeraldColor;
    public static final MapColor lightBlueColor;
    public static final MapColor silverColor;
    public static final MapColor obsidianColor;
    public final int colorIndex;
    public static final MapColor grassColor;
    public static final MapColor sandColor;
    public static final MapColor woodColor;
    public static final MapColor greenColor;
    public static final MapColor yellowColor;
    public static final MapColor netherrackColor;
    public static final MapColor pinkColor;
    public static final MapColor purpleColor;
    public static final MapColor adobeColor;
    public static final MapColor iceColor;
    public static final MapColor clothColor;
    public static final MapColor tntColor;
    public static final MapColor[] mapColorArray;
    public static final MapColor foliageColor;
    public static final MapColor lapisColor;
    public static final MapColor clayColor;
    public static final MapColor blueColor;
    public static final MapColor limeColor;
    public final int colorValue;
    public static final MapColor ironColor;
    public static final MapColor grayColor;
    public static final MapColor redColor;
    public static final MapColor cyanColor;
    public static final MapColor stoneColor;
    public static final MapColor magentaColor;
    public static final MapColor diamondColor;
    public static final MapColor dirtColor;
    public static final MapColor goldColor;
    public static final MapColor airColor;
    public static final MapColor brownColor;
    public static final MapColor quartzColor;
    public static final MapColor snowColor;
    public static final MapColor blackColor;
    public static final MapColor waterColor;

    public int func_151643_b(int n) {
        int n2 = 220;
        if (n == 3) {
            n2 = 135;
        }
        if (n == 2) {
            n2 = 255;
        }
        if (n == 1) {
            n2 = 220;
        }
        if (n == 0) {
            n2 = 180;
        }
        int n3 = (this.colorValue >> 16 & 0xFF) * n2 / 255;
        int n4 = (this.colorValue >> 8 & 0xFF) * n2 / 255;
        int n5 = (this.colorValue & 0xFF) * n2 / 255;
        return 0xFF000000 | n3 << 16 | n4 << 8 | n5;
    }

    private MapColor(int n, int n2) {
        if (n < 0 || n > 63) {
            throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
        }
        this.colorIndex = n;
        this.colorValue = n2;
        MapColor.mapColorArray[n] = this;
    }

    static {
        mapColorArray = new MapColor[64];
        airColor = new MapColor(0, 0);
        grassColor = new MapColor(1, 8368696);
        sandColor = new MapColor(2, 16247203);
        clothColor = new MapColor(3, 0xC7C7C7);
        tntColor = new MapColor(4, 0xFF0000);
        iceColor = new MapColor(5, 0xA0A0FF);
        ironColor = new MapColor(6, 0xA7A7A7);
        foliageColor = new MapColor(7, 31744);
        snowColor = new MapColor(8, 0xFFFFFF);
        clayColor = new MapColor(9, 10791096);
        dirtColor = new MapColor(10, 9923917);
        stoneColor = new MapColor(11, 0x707070);
        waterColor = new MapColor(12, 0x4040FF);
        woodColor = new MapColor(13, 9402184);
        quartzColor = new MapColor(14, 0xFFFCF5);
        adobeColor = new MapColor(15, 14188339);
        magentaColor = new MapColor(16, 11685080);
        lightBlueColor = new MapColor(17, 6724056);
        yellowColor = new MapColor(18, 0xE5E533);
        limeColor = new MapColor(19, 8375321);
        pinkColor = new MapColor(20, 15892389);
        grayColor = new MapColor(21, 0x4C4C4C);
        silverColor = new MapColor(22, 0x999999);
        cyanColor = new MapColor(23, 5013401);
        purpleColor = new MapColor(24, 8339378);
        blueColor = new MapColor(25, 3361970);
        brownColor = new MapColor(26, 6704179);
        greenColor = new MapColor(27, 6717235);
        redColor = new MapColor(28, 0x993333);
        blackColor = new MapColor(29, 0x191919);
        goldColor = new MapColor(30, 16445005);
        diamondColor = new MapColor(31, 6085589);
        lapisColor = new MapColor(32, 4882687);
        emeraldColor = new MapColor(33, 55610);
        obsidianColor = new MapColor(34, 8476209);
        netherrackColor = new MapColor(35, 0x700200);
    }
}

