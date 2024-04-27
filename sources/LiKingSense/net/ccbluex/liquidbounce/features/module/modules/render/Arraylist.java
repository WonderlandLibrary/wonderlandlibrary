/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.math.MathHelper
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(name="Arraylist", description="Jello.", category=ModuleCategory.RENDER, array=false)
public class Arraylist
extends Module {
    List<Module> modules;
    private final BoolValue shadow;
    private final BoolValue useTrueFont;
    private final IntegerValue animateSpeed;

    /*
     * Exception decompiling
     */
    public Arraylist() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl28 : PUTFIELD - null : Stack underflow
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

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.updateElements(event.getPartialTicks());
        this.renderArraylist();
    }

    public void updateElements(float partialTicks) {
        this.modules = LiquidBounce.moduleManager.getModules().stream().filter(mod -> mod.getArray()).sorted(new ModComparator()).collect(Collectors.toCollection(ArrayList::new));
        float tick = 1.0f - partialTicks;
        for (Module module : this.modules) {
            module.setAnimation2(module.getAnimation2() + (float)(module.getState() ? (Integer)this.animateSpeed.get() : -((Integer)this.animateSpeed.get()).intValue()) * tick);
            module.setAnimation2(MathHelper.func_76131_a((float)module.getAnimation2(), (float)0.0f, (float)20.0f));
        }
    }

    public void renderArraylist() {
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float yStart = 1.0f;
        float xStart = 0.0f;
        for (Module module : this.modules) {
            if (module.getAnimation2() <= 0.0f) continue;
            xStart = sr.getScaledWidth() - FontLoaders.SF22.getStringWidth(module.getTagName()) - 5;
            if (((Boolean)this.shadow.get()).booleanValue()) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179118_c();
                RenderUtils.drawImage(classProvider.createResourceLocation("likingsense/shadow/shadow2.png"), (int)(xStart - 8.0f - 2.0f - 1.0f), (int)(yStart + 2.0f - 2.5f - 1.5f - 1.5f - 1.5f - 6.0f - 1.0f), FontLoaders.SF22.getStringWidth(module.getTagName()) * 1 + 20 + 10, 38);
                GlStateManager.func_179141_d();
                GlStateManager.func_179121_F();
            }
            yStart += 12.75f * (module.getAnimation2() / 20.0f);
        }
        yStart = 1.0f;
        xStart = 0.0f;
        for (Module module : this.modules) {
            if (module.getAnimation2() <= 0.0f) continue;
            xStart = sr.getScaledWidth() - FontLoaders.SF22.getStringWidth(module.getTagName()) - 5;
            GlStateManager.func_179094_E();
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.func_179118_c();
            }
            FontLoaders.SF22.drawString(module.getTagName(), xStart, yStart + 7.5f, new Color(1.0f, 1.0f, 1.0f, module.getAnimation2() / 20.0f * 0.7f).getRGB());
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.func_179141_d();
            }
            GlStateManager.func_179121_F();
            yStart += 12.75f * (module.getAnimation2() / 20.0f);
        }
        GlStateManager.func_179117_G();
    }

    class ModComparator
    implements Comparator<Module> {
        ModComparator() {
        }

        @Override
        public int compare(Module e1, Module e2) {
            return FontLoaders.SF22.getStringWidth(e1.getTagName()) < FontLoaders.SF22.getStringWidth(e2.getTagName()) ? 1 : -1;
        }
    }
}

