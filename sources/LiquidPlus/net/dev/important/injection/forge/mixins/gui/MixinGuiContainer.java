/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.injection.forge.mixins.gui;

import net.dev.important.Client;
import net.dev.important.injection.forge.mixins.gui.MixinGuiScreen;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.player.InvManager;
import net.dev.important.modules.module.modules.render.Animations;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.modules.module.modules.world.ChestStealer;
import net.dev.important.utils.render.EaseUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
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
    private GuiButton stealButton;
    private GuiButton chestStealerButton;
    private GuiButton invManagerButton;
    private GuiButton killAuraButton;
    private float progress = 0.0f;
    private long lastMS = 0L;

    @Inject(method={"initGui"}, at={@At(value="HEAD")})
    public void injectInitGui(CallbackInfo callbackInfo) {
        GuiScreen guiScreen = Minecraft.func_71410_x().field_71462_r;
        if (guiScreen instanceof GuiChest) {
            this.killAuraButton = new GuiButton(1024576, 5, 5, 140, 20, "Disable KillAura");
            this.field_146292_n.add(this.killAuraButton);
            this.invManagerButton = new GuiButton(321123, 5, 25, 140, 20, "Disable InvManager");
            this.field_146292_n.add(this.invManagerButton);
            this.chestStealerButton = new GuiButton(727, 5, 45, 140, 20, "Disable Stealer");
            this.field_146292_n.add(this.chestStealerButton);
            this.stealButton = new GuiButton(1234123, 5, 65, 140, 20, "Steal this chest");
            this.field_146292_n.add(this.stealButton);
        }
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
    }

    @Override
    protected void injectedActionPerformed(GuiButton button) {
        ChestStealer chestStealer = (ChestStealer)Client.moduleManager.getModule(ChestStealer.class);
        if (button.field_146127_k == 1024576) {
            Client.moduleManager.getModule(KillAura.class).setState(false);
        }
        if (button.field_146127_k == 321123) {
            Client.moduleManager.getModule(InvManager.class).setState(false);
        }
        if (button.field_146127_k == 727) {
            chestStealer.setState(false);
        }
        if (button.field_146127_k == 1234123 && !chestStealer.getState()) {
            chestStealer.setContentReceived(this.field_146297_k.field_71439_g.field_71070_bA.field_75152_c);
            chestStealer.setOnce(true);
            chestStealer.setState(true);
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawScreenHead(CallbackInfo callbackInfo) {
        boolean checkFullSilence;
        Animations animMod = (Animations)Client.moduleManager.getModule(Animations.class);
        ChestStealer chestStealer = (ChestStealer)Client.moduleManager.getModule(ChestStealer.class);
        HUD hud = (HUD)Client.moduleManager.getModule(HUD.class);
        Minecraft mc = Minecraft.func_71410_x();
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / 750.0f;
        double trueAnim = EaseUtils.easeOutQuart(this.progress);
        if (!(!((Boolean)hud.getContainerBackground().get()).booleanValue() || mc.field_71462_r instanceof GuiChest && chestStealer.getState() && ((Boolean)chestStealer.getSilenceValue().get()).booleanValue() && ((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue())) {
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
                case "HSlide": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * (double)(-this.field_146294_l)), (double)0.0, (double)0.0);
                    break;
                }
                case "VSlide": {
                    GL11.glTranslated((double)0.0, (double)((1.0 - trueAnim) * (double)(-this.field_146295_m)), (double)0.0);
                    break;
                }
                case "HVSlide": {
                    GL11.glTranslated((double)((1.0 - trueAnim) * (double)(-this.field_146294_l)), (double)((1.0 - trueAnim) * (double)(-this.field_146295_m)), (double)0.0);
                }
            }
        }
        try {
            GuiScreen guiScreen = mc.field_71462_r;
            if (this.stealButton != null) {
                boolean bl2 = this.stealButton.field_146124_l = !chestStealer.getState();
            }
            if (this.killAuraButton != null) {
                this.killAuraButton.field_146124_l = Client.moduleManager.getModule(KillAura.class).getState();
            }
            if (this.chestStealerButton != null) {
                this.chestStealerButton.field_146124_l = chestStealer.getState();
            }
            if (this.invManagerButton != null) {
                this.invManagerButton.field_146124_l = Client.moduleManager.getModule(InvManager.class).getState();
            }
            if (chestStealer.getState() && ((Boolean)chestStealer.getSilenceValue().get()).booleanValue() && guiScreen instanceof GuiChest) {
                mc.func_71381_h();
                mc.field_71462_r = guiScreen;
                if (((Boolean)chestStealer.getShowStringValue().get()).booleanValue() && !((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue()) {
                    String tipString = "Stealing... Press Esc to stop.";
                    mc.field_71466_p.func_175065_a(tipString, (float)(this.field_146294_l / 2 - mc.field_71466_p.func_78256_a(tipString) / 2 - 1), (float)(this.field_146295_m / 2 + 30), 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)(this.field_146294_l / 2 - mc.field_71466_p.func_78256_a(tipString) / 2 + 1), (float)(this.field_146295_m / 2 + 30), 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)(this.field_146294_l / 2 - mc.field_71466_p.func_78256_a(tipString) / 2), (float)(this.field_146295_m / 2 + 30 - 1), 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)(this.field_146294_l / 2 - mc.field_71466_p.func_78256_a(tipString) / 2), (float)(this.field_146295_m / 2 + 30 + 1), 0, false);
                    mc.field_71466_p.func_175065_a(tipString, (float)(this.field_146294_l / 2 - mc.field_71466_p.func_78256_a(tipString) / 2), (float)(this.field_146295_m / 2 + 30), -1, false);
                }
                if (!((Boolean)chestStealer.getStillDisplayValue().get()).booleanValue()) {
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
        Animations animMod = (Animations)Client.moduleManager.getModule(Animations.class);
        ChestStealer chestStealer = (ChestStealer)Client.moduleManager.getModule(ChestStealer.class);
        Minecraft mc = Minecraft.func_71410_x();
        boolean bl = checkFullSilence = chestStealer.getState() && (Boolean)chestStealer.getSilenceValue().get() != false && (Boolean)chestStealer.getStillDisplayValue().get() == false;
        if (!(animMod == null || !animMod.getState() || mc.field_71462_r instanceof GuiChest && checkFullSilence)) {
            GL11.glPopMatrix();
        }
    }
}

