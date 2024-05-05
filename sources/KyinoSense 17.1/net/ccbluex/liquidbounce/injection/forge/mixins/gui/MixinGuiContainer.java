/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import me.report.liquidware.modules.render.Animations;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer
extends MixinGuiScreen {
    @Shadow
    protected int field_146999_f;
    @Shadow
    protected int field_147000_g;
    @Shadow
    protected int field_147003_i;
    @Shadow
    protected int field_147009_r;
    @Shadow
    private int field_146988_G;
    @Shadow
    private int field_146996_I;
    private GuiButton stealButton;
    private GuiButton chestStealerButton;
    private GuiButton invManagerButton;
    private GuiButton killAuraButton;
    private float progress = 0.0f;
    private long lastMS = 0L;

    @Shadow
    protected abstract boolean func_146983_a(int var1);

    @Inject(method={"initGui"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectInitGui(CallbackInfo callbackInfo) {
        GuiScreen guiScreen = Minecraft.func_71410_x().field_71462_r;
        if (guiScreen instanceof GuiChest) {
            this.killAuraButton = new GuiButton(1024576, 5, 5, 150, 20, "Disable KillAura");
            this.field_146292_n.add(this.killAuraButton);
            this.invManagerButton = new GuiButton(321123, 5, 27, 150, 20, "Disable InvCleaner");
            this.field_146292_n.add(this.invManagerButton);
            this.chestStealerButton = new GuiButton(727, 5, 49, 150, 20, "Disable Stealer");
            this.field_146292_n.add(this.chestStealerButton);
        }
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
    }

    @Override
    protected void injectedActionPerformed(GuiButton button) {
        if (button.field_146127_k == 1024576) {
            LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
        }
        if (button.field_146127_k == 321123) {
            LiquidBounce.moduleManager.getModule(InventoryCleaner.class).setState(false);
        }
        if (button.field_146127_k == 727) {
            LiquidBounce.moduleManager.getModule(ChestStealer.class).setState(false);
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawScreenHead(CallbackInfo callbackInfo) {
        boolean checkFullSilence;
        Animations animMod = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        ChestStealer chestStealer = (ChestStealer)LiquidBounce.moduleManager.getModule(ChestStealer.class);
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        Minecraft mc = Minecraft.func_71410_x();
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / 200.0f;
        double trueAnim = EaseUtils.easeOutQuart(this.progress);
        if (!(!((Boolean)hud.containerBackground.get()).booleanValue() || mc.field_71462_r instanceof GuiChest && chestStealer.getState() && ((Boolean)chestStealer.getSilenceValue().get()).booleanValue() && !((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue())) {
            RenderUtils.drawGradientRect(0, 0, this.field_146294_l, this.field_146295_m, -1072689136, -804253680);
        }
        boolean bl = checkFullSilence = chestStealer.getState() && (Boolean)chestStealer.getSilenceValue().get() != false && (Boolean)chestStealer.getStillDisplayValue().get() == false;
        if (!(animMod == null || !animMod.getState() || mc.field_71462_r instanceof GuiChest && checkFullSilence)) {
            GL11.glPushMatrix();
            switch ((String)Animations.guiAnimations.get()) {
                case "Zoom": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * ((double)this.field_146294_l / 2.0)), (double)((1.0 - trueAnim) * ((double)this.field_146295_m / 2.0)), (double)0.0);
                    GL11.glScaled((double)trueAnim, (double)trueAnim, (double)trueAnim);
                    break;
                }
                case "Slide": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * (double)this.field_146294_l), (double)0.0, (double)0.0);
                }
            }
        }
        try {
            GuiScreen guiScreen = mc.field_71462_r;
            if (this.stealButton != null) {
                boolean bl2 = this.stealButton.field_146124_l = !chestStealer.getState();
            }
            if (this.killAuraButton != null) {
                this.killAuraButton.field_146124_l = LiquidBounce.moduleManager.getModule(KillAura.class).getState();
            }
            if (this.chestStealerButton != null) {
                this.chestStealerButton.field_146124_l = chestStealer.getState();
            }
            if (this.invManagerButton != null) {
                this.invManagerButton.field_146124_l = LiquidBounce.moduleManager.getModule(InventoryCleaner.class).getState();
            }
            if (chestStealer.getState() && ((Boolean)chestStealer.getSilenceValue().get()).booleanValue() && guiScreen instanceof GuiChest) {
                mc.func_71381_h();
                mc.field_71462_r = guiScreen;
                if (((Boolean)chestStealer.getShowStringValue().get()).booleanValue() && !((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue()) {
                    String tipString = "Stealing... Press Esc to stop.";
                    mc.field_71466_p.func_175065_a(tipString, (float)this.field_146294_l / 2.0f - (float)mc.field_71466_p.func_78256_a(tipString) / 2.0f - 0.5f, (float)this.field_146295_m / 2.0f + 30.0f, 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)this.field_146294_l / 2.0f - (float)mc.field_71466_p.func_78256_a(tipString) / 2.0f + 0.5f, (float)this.field_146295_m / 2.0f + 30.0f, 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)this.field_146294_l / 2.0f - (float)mc.field_71466_p.func_78256_a(tipString) / 2.0f, (float)this.field_146295_m / 2.0f + 29.5f, 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)this.field_146294_l / 2.0f - (float)mc.field_71466_p.func_78256_a(tipString) / 2.0f, (float)this.field_146295_m / 2.0f + 30.5f, 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)this.field_146294_l / 2.0f - (float)mc.field_71466_p.func_78256_a(tipString) / 2.0f, (float)this.field_146295_m / 2.0f + 30.0f, -1, false);
                }
                if (!chestStealer.getOnce() && !((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue()) {
                    callbackInfo.cancel();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    protected boolean shouldRenderBackground() {
        return false;
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    public void drawScreenReturn(CallbackInfo callbackInfo) {
        boolean checkFullSilence;
        Animations animMod = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        ChestStealer chestStealer = (ChestStealer)LiquidBounce.moduleManager.getModule(ChestStealer.class);
        Minecraft mc = Minecraft.func_71410_x();
        boolean bl = checkFullSilence = chestStealer.getState() && (Boolean)chestStealer.getSilenceValue().get() != false && (Boolean)chestStealer.getStillDisplayValue().get() == false;
        if (!(animMod == null || !animMod.getState() || mc.field_71462_r instanceof GuiChest && checkFullSilence)) {
            GL11.glPopMatrix();
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")}, cancellable=true)
    private void checkCloseClick(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (mouseButton - 100 == this.field_146297_k.field_71474_y.field_151445_Q.func_151463_i()) {
            this.field_146297_k.field_71439_g.func_71053_j();
            ci.cancel();
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="TAIL")})
    private void checkHotbarClicks(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        this.func_146983_a(mouseButton - 100);
    }

    @Inject(method={"updateDragSplitting"}, at={@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;")}, cancellable=true)
    private void fixRemnants(CallbackInfo ci) {
        if (this.field_146988_G == 2) {
            this.field_146996_I = this.field_146297_k.field_71439_g.field_71071_by.func_70445_o().func_77976_d();
            ci.cancel();
        }
    }
}

