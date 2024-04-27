/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;

    @Shadow
    public abstract int func_146232_i();

    @Shadow
    public abstract boolean func_146241_e();

    @Shadow
    public abstract float func_146244_h();

    @Shadow
    public abstract int func_146228_f();

    /*
     * Exception decompiling
     */
    @Inject(method={"drawChat"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawChat(int p_drawChat_1_, CallbackInfo callbackInfo) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl364 : INVOKESTATIC - null : trying to set 0 previously set to 4
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    @Inject(method={"getChatComponent"}, at={@At(value="HEAD")}, cancellable=true)
    private void getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_, CallbackInfoReturnable<ITextComponent> callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getState() && ((Boolean)hud.getFontChatValue().get()).booleanValue()) {
            if (this.func_146241_e()) {
                ScaledResolution lvt_3_1_ = new ScaledResolution(this.field_146247_f);
                int lvt_4_1_ = lvt_3_1_.func_78325_e();
                float lvt_5_1_ = this.func_146244_h();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.func_76141_d((float)((float)lvt_6_1_ / lvt_5_1_));
                lvt_7_1_ = MathHelper.func_76141_d((float)((float)lvt_7_1_ / lvt_5_1_));
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    int lvt_9_1_;
                    int lvt_8_1_ = Math.min(this.func_146232_i(), this.field_146253_i.size());
                    if (lvt_6_1_ <= MathHelper.func_76141_d((float)((float)this.func_146228_f() / this.func_146244_h())) && lvt_7_1_ < FontLoaders.F18.FONT_HEIGHT * lvt_8_1_ + lvt_8_1_ && (lvt_9_1_ = lvt_7_1_ / FontLoaders.F18.FONT_HEIGHT + this.field_146250_j) >= 0 && lvt_9_1_ < this.field_146253_i.size()) {
                        ChatLine lvt_10_1_ = this.field_146253_i.get(lvt_9_1_);
                        int lvt_11_1_ = 0;
                        for (ITextComponent lvt_13_1_ : lvt_10_1_.func_151461_a()) {
                            if (!(lvt_13_1_ instanceof TextComponentString) || (lvt_11_1_ += FontLoaders.F18.getStringWidth(GuiUtilRenderComponents.func_178909_a((String)((TextComponentString)lvt_13_1_).func_150265_g(), (boolean)false))) <= lvt_6_1_) continue;
                            callbackInfo.setReturnValue((Object)lvt_13_1_);
                            return;
                        }
                    }
                }
            }
            callbackInfo.setReturnValue(null);
        }
    }
}

