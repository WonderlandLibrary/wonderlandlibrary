/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package ad;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.novoline.ScaleUtils;
import net.minecraft.entity.EntityLivingBase;

public final class Colors
extends Enum<Colors> {
    public static final /* enum */ Colors BLACK;
    public static final /* enum */ Colors BLUE;
    public static final /* enum */ Colors DARKBLUE;
    public static final /* enum */ Colors GREEN;
    public static final /* enum */ Colors DARKGREEN;
    public static final /* enum */ Colors WHITE;
    public static final /* enum */ Colors AQUA;
    public static final /* enum */ Colors DARKAQUA;
    public static final /* enum */ Colors GREY;
    public static final /* enum */ Colors DARKGREY;
    public static final /* enum */ Colors RED;
    public static final /* enum */ Colors DARKRED;
    public static final /* enum */ Colors ORANGE;
    public static final /* enum */ Colors DARKORANGE;
    public static final /* enum */ Colors YELLOW;
    public static final /* enum */ Colors DARKYELLOW;
    public static final /* enum */ Colors MAGENTA;
    public static final /* enum */ Colors DARKMAGENTA;
    public int c;
    private static final /* synthetic */ Colors[] $VALUES;

    public static Colors[] values() {
        return (Colors[])$VALUES.clone();
    }

    public static Colors valueOf(String name) {
        return Enum.valueOf(Colors.class, name);
    }

    private Colors(int co) {
        this.c = co;
    }

    public static int getColor(Color color) {
        return Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return Colors.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return Colors.getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return Colors.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        return color1 |= blue;
    }

    /*
     * Exception decompiling
     */
    public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl35 : AASTORE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] indicies = Colors.getFractionIndicies(fractions, progress);
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return ScaleUtils.blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    /*
     * Exception decompiling
     */
    public static int getHealthColor(float health, float maxHealth) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl12 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    static {
        Colors colors;
        Colors colors2;
        Colors colors3;
        Colors colors4;
        Colors colors5;
        Colors colors6;
        Colors colors7;
        Colors colors8;
        Colors colors9;
        Colors colors10;
        Colors colors11;
        Colors colors12;
        Colors colors13;
        Colors colors14;
        Colors colors15;
        Colors colors16;
        Colors colors17;
        Colors colors18;
        Colors colors19 = colors18;
        Colors colors20 = colors18;
        String string = "BLACK";
        BLACK = (Colors)0;
        Colors colors21 = colors17;
        Colors colors22 = colors17;
        String string2 = "BLUE";
        BLUE = (Colors)0;
        Colors colors23 = colors16;
        Colors colors24 = colors16;
        String string3 = "DARKBLUE";
        DARKBLUE = (Colors)0;
        Colors colors25 = colors15;
        Colors colors26 = colors15;
        String string4 = "GREEN";
        GREEN = (Colors)0;
        Colors colors27 = colors14;
        Colors colors28 = colors14;
        String string5 = "DARKGREEN";
        DARKGREEN = (Colors)0;
        Colors colors29 = colors13;
        Colors colors30 = colors13;
        String string6 = "WHITE";
        WHITE = (Colors)0;
        Colors colors31 = colors12;
        Colors colors32 = colors12;
        String string7 = "AQUA";
        AQUA = (Colors)0;
        Colors colors33 = colors11;
        Colors colors34 = colors11;
        String string8 = "DARKAQUA";
        DARKAQUA = (Colors)0;
        Colors colors35 = colors10;
        Colors colors36 = colors10;
        String string9 = "GREY";
        GREY = (Colors)0;
        Colors colors37 = colors9;
        Colors colors38 = colors9;
        String string10 = "DARKGREY";
        DARKGREY = (Colors)0;
        Colors colors39 = colors8;
        Colors colors40 = colors8;
        String string11 = "RED";
        RED = (Colors)0;
        Colors colors41 = colors7;
        Colors colors42 = colors7;
        String string12 = "DARKRED";
        DARKRED = (Colors)0;
        Colors colors43 = colors6;
        Colors colors44 = colors6;
        String string13 = "ORANGE";
        ORANGE = (Colors)0;
        Colors colors45 = colors5;
        Colors colors46 = colors5;
        String string14 = "DARKORANGE";
        DARKORANGE = (Colors)0;
        Colors colors47 = colors4;
        Colors colors48 = colors4;
        String string15 = "YELLOW";
        YELLOW = (Colors)0;
        Colors colors49 = colors3;
        Colors colors50 = colors3;
        String string16 = "DARKYELLOW";
        DARKYELLOW = (Colors)0;
        Colors colors51 = colors2;
        Colors colors52 = colors2;
        String string17 = "MAGENTA";
        MAGENTA = (Colors)0;
        Colors colors53 = colors;
        Colors colors54 = colors;
        String string18 = "DARKMAGENTA";
        DARKMAGENTA = (Colors)0;
        $VALUES = new Colors[]{BLACK, BLUE, DARKBLUE, GREEN, DARKGREEN, WHITE, AQUA, DARKAQUA, GREY, DARKGREY, RED, DARKRED, ORANGE, DARKORANGE, YELLOW, DARKYELLOW, MAGENTA, DARKMAGENTA};
    }
}

