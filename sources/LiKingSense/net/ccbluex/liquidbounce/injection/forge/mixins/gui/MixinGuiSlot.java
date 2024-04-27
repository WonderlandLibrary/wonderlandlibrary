/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiSlot.class})
public abstract class MixinGuiSlot
implements IMixinGuiSlot {
    @Shadow
    public int field_148152_e;
    @Shadow
    public int field_148153_b;
    @Shadow
    public int field_148155_a;
    @Shadow
    public int field_148151_d;
    @Shadow
    public int field_148154_c;
    @Shadow
    public int field_148158_l;
    @Shadow
    protected int field_148150_g;
    @Shadow
    protected int field_148162_h;
    @Shadow
    protected float field_148169_q;
    @Shadow
    protected boolean field_148165_u;
    @Shadow
    @Final
    protected Minecraft field_148161_k;
    @Shadow
    protected boolean field_178041_q;
    private int listWidth = 220;
    private boolean enableScissor = false;

    @Shadow
    protected abstract void func_148123_a();

    @Shadow
    protected abstract void func_148121_k();

    @Shadow
    protected abstract void func_148129_a(int var1, int var2, Tessellator var3);

    @Shadow
    protected abstract int func_148138_e();

    @Shadow
    protected abstract void func_192638_a(int var1, int var2, int var3, int var4, float var5);

    @Shadow
    protected abstract void func_148136_c(int var1, int var2, int var3, int var4);

    @Shadow
    public abstract int func_148135_f();

    @Shadow
    protected abstract void drawContainerBackground(Tessellator var1);

    @Shadow
    protected abstract void func_148142_b(int var1, int var2);

    /*
     * Exception decompiling
     */
    @Overwrite
    public void func_148128_a(int mouseXIn, int mouseYIn, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl96 : INVOKESTATIC - null : Stack underflow
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

    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }

    @Override
    public void setEnableScissor(boolean enableScissor) {
        this.enableScissor = enableScissor;
    }

    @Overwrite
    public int func_148139_c() {
        return this.listWidth;
    }

    @Override
    public void setListWidth(int listWidth) {
        this.listWidth = listWidth;
    }
}

