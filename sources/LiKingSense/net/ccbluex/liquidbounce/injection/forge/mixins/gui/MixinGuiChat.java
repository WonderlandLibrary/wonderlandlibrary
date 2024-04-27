/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Direction;
import net.ccbluex.liquidbounce.utils.render.miku.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiChat.class})
public abstract class MixinGuiChat
extends MixinGuiScreen {
    @Shadow
    protected GuiTextField field_146415_a;
    private boolean buttonAction;
    private Element selectedElement;
    private float yPosOfInputField;
    private float fade = 0.0f;
    private static Animation openingAnimation = new DecelerateAnimation(175, 1.0, Direction.BACKWARDS);

    @Shadow
    public abstract void func_184072_a(String ... var1);

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void init(CallbackInfo callbackInfo) {
        this.field_146415_a.field_146210_g = this.field_146295_m + 1;
        this.yPosOfInputField = this.field_146415_a.field_146210_g;
    }

    @Inject(method={"keyTyped"}, at={@At(value="RETURN")})
    private void updateLength(CallbackInfo callbackInfo) {
        if (!this.field_146415_a.func_146179_b().startsWith(String.valueOf(LiquidBounce.commandManager.getPrefix()))) {
            return;
        }
        LiquidBounce.commandManager.autoComplete(this.field_146415_a.func_146179_b());
        if (!this.field_146415_a.func_146179_b().startsWith(LiquidBounce.commandManager.getPrefix() + "lc")) {
            this.field_146415_a.func_146203_f(10000);
        } else {
            this.field_146415_a.func_146203_f(100);
        }
    }

    @Inject(method={"updateScreen"}, at={@At(value="HEAD")})
    private void updateScreen(CallbackInfo callbackInfo) {
        int delta = RenderUtils.deltaTime;
        if (this.fade < 14.0f) {
            this.fade += 0.4f * (float)delta;
        }
        if (this.fade > 14.0f) {
            this.fade = 14.0f;
        }
        if (this.yPosOfInputField > (float)(this.field_146295_m - 12)) {
            this.yPosOfInputField -= 0.4f * (float)delta;
        }
        if (this.yPosOfInputField < (float)(this.field_146295_m - 12)) {
            this.yPosOfInputField = this.field_146295_m - 12;
        }
        this.field_146415_a.field_146210_g = (int)this.yPosOfInputField;
    }

    /*
     * Exception decompiling
     */
    @Override
    @Overwrite
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl104 : INVOKEVIRTUAL - null : Stack underflow
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

    @Inject(method={"onGuiClosed"}, at={@At(value="HEAD")})
    public void onGuiClosed(CallbackInfo ci) {
        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.hudConfig);
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (this.buttonAction) {
            this.buttonAction = false;
            return;
        }
        LiquidBounce.hud.handleMouseClick(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
                if (!element.isInBorder((double)((float)mouseX / element.getScale()) - element.getRenderX(), (double)((float)mouseY / element.getScale()) - element.getRenderY())) continue;
                this.selectedElement = element;
                break;
            }
        }
    }
}

