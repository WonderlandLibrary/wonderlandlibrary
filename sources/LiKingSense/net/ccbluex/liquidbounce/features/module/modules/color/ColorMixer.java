/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.color;

import java.awt.Color;
import java.lang.reflect.Field;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.color.ColorElement;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="ColorMixer", description="Mix two colors together.", category=ModuleCategory.COLOR, canEnable=false)
public class ColorMixer
extends Module {
    private static float[] lastFraction = new float[0];
    public static Color[] lastColors = new Color[0];
    public final IntegerValue blendAmount;
    public final ColorElement col1RedValue;
    public final ColorElement col1GreenValue;
    public final ColorElement col1BlueValue;
    public final ColorElement col2RedValue;
    public final ColorElement col2GreenValue;
    public final ColorElement col2BlueValue;
    public final ColorElement col3RedValue;
    public final ColorElement col3GreenValue;
    public final ColorElement col3BlueValue;
    public final ColorElement col4RedValue;
    public final ColorElement col4GreenValue;
    public final ColorElement col4BlueValue;
    public final ColorElement col5RedValue;
    public final ColorElement col5GreenValue;
    public final ColorElement col5BlueValue;
    public final ColorElement col6RedValue;
    public final ColorElement col6GreenValue;
    public final ColorElement col6BlueValue;
    public final ColorElement col7RedValue;
    public final ColorElement col7GreenValue;
    public final ColorElement col7BlueValue;
    public final ColorElement col8RedValue;
    public final ColorElement col8GreenValue;
    public final ColorElement col8BlueValue;
    public final ColorElement col9RedValue;
    public final ColorElement col9GreenValue;
    public final ColorElement col9BlueValue;
    public final ColorElement col10RedValue;
    public final ColorElement col10GreenValue;
    public final ColorElement col10BlueValue;

    /*
     * Exception decompiling
     */
    public ColorMixer() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl10 : PUTFIELD - null : Stack underflow
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

    public static Color getMixedColor(int index, int seconds) {
        ColorMixer colMixer = (ColorMixer)LiquidBounce.moduleManager.getModule(ColorMixer.class);
        if (colMixer == null) {
            return Color.white;
        }
        if (lastColors.length <= 0 || lastFraction.length <= 0) {
            ColorMixer.regenerateColors(true);
        }
        return BlendUtils.blendColors(lastFraction, lastColors, (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000));
    }

    public static void regenerateColors(boolean forceValue) {
        int i;
        ColorMixer colMixer = (ColorMixer)LiquidBounce.moduleManager.getModule(ColorMixer.class);
        if (colMixer == null) {
            return;
        }
        if (forceValue || lastColors.length <= 0 || lastColors.length != (Integer)colMixer.blendAmount.get() * 2 - 1) {
            Color[] generator = new Color[(Integer)colMixer.blendAmount.get() * 2 - 1];
            for (i = 1; i <= (Integer)colMixer.blendAmount.get(); ++i) {
                Color result = Color.white;
                try {
                    Field red = ColorMixer.class.getField("col" + i + "RedValue");
                    Field green = ColorMixer.class.getField("col" + i + "GreenValue");
                    Field blue = ColorMixer.class.getField("col" + i + "BlueValue");
                    int r = (Integer)((ColorElement)red.get(colMixer)).get();
                    int g = (Integer)((ColorElement)green.get(colMixer)).get();
                    int b = (Integer)((ColorElement)blue.get(colMixer)).get();
                    result = new Color(Math.max(0, Math.min(r, 255)), Math.max(0, Math.min(g, 255)), Math.max(0, Math.min(b, 255)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                generator[i - 1] = result;
            }
            int h = (Integer)colMixer.blendAmount.get();
            for (int z = (Integer)colMixer.blendAmount.get() - 2; z >= 0; --z) {
                generator[h] = generator[z];
                ++h;
            }
            lastColors = generator;
        }
        if (forceValue || lastFraction.length <= 0 || lastFraction.length != (Integer)colMixer.blendAmount.get() * 2 - 1) {
            float[] colorFraction = new float[(Integer)colMixer.blendAmount.get() * 2 - 1];
            for (i = 0; i <= (Integer)colMixer.blendAmount.get() * 2 - 2; ++i) {
                colorFraction[i] = (float)i / (float)((Integer)colMixer.blendAmount.get() * 2 - 2);
            }
            lastFraction = colorFraction;
        }
    }
}

