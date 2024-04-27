/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer
extends MixinGuiScreen
implements IMixinGuiContainer {
    private GuiButton stealButton;
    private GuiButton chestStealerButton;
    private GuiButton invManagerButton;
    private GuiButton killAuraButton;
    private float progress = 0.0f;
    private long lastMS = 0L;
    private long guiOpenTime = -1L;

    @Shadow
    protected abstract void func_184098_a(Slot var1, int var2, int var3, ClickType var4);

    /*
     * Exception decompiling
     */
    @Inject(method={"initGui"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectInitGui(CallbackInfo callbackInfo) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Underrun type stack
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getEntry(StackSim.java:35)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.getCat(OperationFactoryDefault.java:62)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.checkCat(OperationFactoryDefault.java:66)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDupX1.getStackDelta(OperationFactoryDupX1.java:18)
         *     at org.benf.cfr.reader.bytecode.opcode.JVMInstr.getStackDelta(JVMInstr.java:315)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:199)
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

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")})
    protected void drawScreenHead(CallbackInfo callbackInfo) {
        Animations animMod = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / (float)((Integer)Animations.animTimeValue.get()).intValue();
        double trueAnim = EaseUtils.easeOutQuart(this.progress);
        if (animMod != null && animMod.getState()) {
            GL11.glPushMatrix();
            switch ((String)Animations.guiAnimations.get()) {
                case "Zoom": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * ((double)this.field_146294_l / 2.0)), (double)((1.0 - trueAnim) * ((double)this.field_146295_m / 2.0)), (double)0.0);
                    GL11.glScaled((double)trueAnim, (double)trueAnim, (double)trueAnim);
                    break;
                }
                case "Slide": {
                    switch ((String)Animations.hSlideValue.get()) {
                        case "Right": {
                            GL11.glTranslated((double)((1.0 - trueAnim) * (double)(-this.field_146294_l)), (double)0.0, (double)0.0);
                            break;
                        }
                        case "Left": {
                            GL11.glTranslated((double)((1.0 - trueAnim) * (double)this.field_146294_l), (double)0.0, (double)0.0);
                        }
                    }
                    switch ((String)Animations.vSlideValue.get()) {
                        case "Upward": {
                            GL11.glTranslated((double)0.0, (double)((1.0 - trueAnim) * (double)this.field_146295_m), (double)0.0);
                            break;
                        }
                        case "Downward": {
                            GL11.glTranslated((double)0.0, (double)((1.0 - trueAnim) * (double)(-this.field_146295_m)), (double)0.0);
                        }
                    }
                    break;
                }
                case "Smooth": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * (double)(-this.field_146294_l)), (double)((1.0 - trueAnim) * (double)(-this.field_146295_m) / 4.0), (double)0.0);
                }
            }
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    public void drawScreenReturn(CallbackInfo callbackInfo) {
        Animations animMod = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        ChestStealer chestStealer = (ChestStealer)LiquidBounce.moduleManager.getModule(ChestStealer.class);
        Minecraft mc = Minecraft.func_71410_x();
        if (animMod != null && animMod.getState()) {
            GL11.glPopMatrix();
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="RETURN")})
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo callbackInfo) {
        for (Object aButtonList : this.field_146292_n) {
            GuiButton var52 = (GuiButton)aButtonList;
            if (var52.func_146116_c(this.field_146297_k, mouseX, mouseY) && var52.field_146127_k == 1024576) {
                LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
            }
            if (!var52.func_146116_c(this.field_146297_k, mouseX, mouseY) || var52.field_146127_k != 321123) continue;
            LiquidBounce.moduleManager.getModule(InventoryCleaner.class).setState(false);
        }
    }

    @Override
    public void publicHandleMouseClick(Slot slot, int slotNumber, int clickedButton, ClickType clickType) {
        this.func_184098_a(slot, slotNumber, clickedButton, clickType);
    }
}

