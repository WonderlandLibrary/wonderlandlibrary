/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package ad;

import ad.novoline.font.Fonts;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import net.ccbluex.liquidbounce.event.BlurEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Direction;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name="ArrayListMod", description="IDK", category=ModuleCategory.RENDER)
public class ArraylistMod
extends Module {
    private final BoolValue importantModules = new BoolValue("Important", false);
    public final FloatValue height = new FloatValue("Height", 11.0f, 9.0f, 20.0f);
    private final ListValue animation = new ListValue("Animation", new String[]{"Move in", "Scale in"}, "Scale in");
    private final FloatValue colorIndex = new FloatValue("Color Seperation", 20.0f, 5.0f, 100.0f);
    private final FloatValue colorSpeed = new FloatValue("Color Speed", 15.0f, 2.0f, 30.0f);
    private final BoolValue background = new BoolValue("Background", true);
    private final FloatValue backgroundAlpha = new FloatValue("Background Alpha", 0.35f, 0.01f, 1.0f);
    public List<Module> modules;
    private final Comparator<Object> SORT_METHOD = Comparator.comparingDouble(m -> {
        Module module = (Module)m;
        String name = module.getName() + module.getName();
        return Fonts.posterama.posterama20.posterama20.stringWidth(name);
    }).reversed();
    public String longest = "";
    double longestWidth = 0.0;

    @EventTarget
    public void blur(BlurEvent event) {
        double yOffset = 0.0;
        ScaledResolution sr = new ScaledResolution(mc2);
        for (Module module : this.modules) {
            if (((Boolean)this.importantModules.get()).booleanValue() && module.getCategory() == ModuleCategory.RENDER) continue;
            Animation moduleAnimation = module.getAnimation();
            if (!module.getState() && moduleAnimation.finished(Direction.BACKWARDS)) continue;
            String displayText = module.getName() + module.getTagName();
            double textWidth = Fonts.posterama.posterama20.posterama20.stringWidth(displayText);
            double xValue = sr.func_78326_a() - 2;
            boolean flip = xValue <= (double)((float)sr.func_78326_a() / 2.0f);
            double x = flip ? xValue : (double)sr.func_78326_a() - (textWidth + 2.0);
            double y = yOffset + 3.0;
            double heightVal = ((Float)this.height.getValue()).floatValue() + 1.0f;
            switch ((String)this.animation.get()) {
                case "Move in": {
                    if (flip) {
                        x -= Math.abs((moduleAnimation.getOutput() - 1.0) * ((double)sr.func_78326_a() - (2.0 + textWidth)));
                        break;
                    }
                    x += Math.abs((moduleAnimation.getOutput() - 1.0) * (2.0 + textWidth));
                    break;
                }
                case "Scale in": {
                    RenderUtils.scaleStart((float)(x + (double)((float)Fonts.posterama.posterama20.posterama20.stringWidth(displayText) / 2.0f)), (float)(y + heightVal / 2.0 - (double)((float)Fonts.posterama.posterama20.posterama20.getHeight() / 2.0f)), (float)moduleAnimation.getOutput());
                }
            }
            if (((Boolean)this.background.get()).booleanValue()) {
                RenderUtils.drawRect2(x - 2.0, y - 3.0, Fonts.posterama.posterama20.posterama20.stringWidth(displayText) + 5, heightVal, Color.WHITE.getRGB());
            }
            if (this.animation.get() == "Scale in") {
                RenderUtils.scaleEnd();
            }
            yOffset += moduleAnimation.getOutput() * heightVal;
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl269 : INVOKESTATIC - null : Stack underflow
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
}

