/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.HoverEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiScreen.class})
public abstract class MixinGuiScreen {
    @Shadow
    public Minecraft field_146297_k;
    @Shadow
    public List<GuiButton> field_146292_n;
    @Shadow
    public int field_146294_l;
    @Shadow
    public int field_146295_m;
    @Shadow
    public FontRenderer field_146289_q;

    @Shadow
    public void func_73876_c() {
    }

    @Shadow
    protected abstract void func_175272_a(ITextComponent var1, int var2, int var3);

    @Shadow
    public abstract void func_146283_a(List<String> var1, int var2, int var3);

    @Shadow
    public abstract void func_146276_q_();

    @Shadow
    public abstract void func_73863_a(int var1, int var2, float var3);

    @Inject(method={"drawWorldBackground"}, at={@At(value="HEAD")})
    private void drawWorldBackground(CallbackInfo callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (((Boolean)hud.getInventoryParticle().get()).booleanValue() && this.field_146297_k.field_71439_g != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            int width = scaledResolution.func_78326_a();
            int height = scaledResolution.func_78328_b();
            ParticleUtils.drawParticles(Mouse.getX() * width / this.field_146297_k.field_71443_c, height - Mouse.getY() * height / this.field_146297_k.field_71440_d - 1);
        }
    }

    /*
     * Exception decompiling
     */
    @Inject(method={"drawBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawClientBackground(CallbackInfo callbackInfo) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl91 : INVOKESTATIC - null : Stack underflow
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

    @Inject(method={"drawBackground"}, at={@At(value="RETURN")})
    private void drawParticles(CallbackInfo callbackInfo) {
        if (GuiBackground.Companion.getParticles()) {
            ParticleUtils.drawParticles(Mouse.getX() * this.field_146294_l / this.field_146297_k.field_71443_c, this.field_146295_m - Mouse.getY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1);
        }
    }

    @Inject(method={"sendChatMessage(Ljava/lang/String;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void messageSend(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        if (msg.startsWith(String.valueOf(LiquidBounce.commandManager.getPrefix())) && addToChat) {
            this.field_146297_k.field_71456_v.func_146158_b().func_146239_a(msg);
            LiquidBounce.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }

    @Inject(method={"handleComponentHover"}, at={@At(value="HEAD")})
    private void handleHoverOverComponent(ITextComponent component, int x, int y, CallbackInfo callbackInfo) {
        if (component == null || component.func_150256_b().func_150235_h() == null || !LiquidBounce.moduleManager.getModule(ComponentOnHover.class).getState()) {
            return;
        }
        Style chatStyle = component.func_150256_b();
        ClickEvent clickEvent = chatStyle.func_150235_h();
        HoverEvent hoverEvent = chatStyle.func_150210_i();
        this.func_146283_a(Collections.singletonList("\u00a7c\u00a7l" + clickEvent.func_150669_a().func_150673_b().toUpperCase() + ": \u00a7a" + clickEvent.func_150668_b()), x, y - (hoverEvent != null ? 17 : 0));
    }

    @Overwrite
    protected void func_146284_a(GuiButton button) throws IOException {
        this.injectedActionPerformed(button);
    }

    protected void injectedActionPerformed(GuiButton button) {
    }
}

