/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
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
    private GuiButton killAuraButton;
    private long guiOpenTime = -1L;
    private GuiButton invManagerButton;
    private float progress = 0.0f;
    private GuiButton stealButton;
    private long lastMS = 0L;
    private GuiButton chestStealerButton;

    @Inject(method={"initGui"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectInitGui(CallbackInfo callbackInfo) {
        int n = 0;
        this.killAuraButton = new GuiButton(1024576, 5, 5, 140, 20, "Disable KillAura");
        this.field_146292_n.add(this.killAuraButton);
        this.invManagerButton = new GuiButton(321123, 5, 5 + (n += 30), 140, 20, "Disable InvCleaner");
        this.field_146292_n.add(this.invManagerButton);
        this.chestStealerButton = new GuiButton(727, 5, 5 + (n += 30), 140, 20, "Disable ChestStealer");
        this.field_146292_n.add(this.chestStealerButton);
        n += 30;
    }

    @Shadow
    protected abstract void func_184098_a(Slot var1, int var2, int var3, ClickType var4);

    @Override
    public void publicHandleMouseClick(Slot slot, int n, int n2, ClickType clickType) {
        this.func_184098_a(slot, n, n2, clickType);
    }

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")})
    protected void drawScreenHead(CallbackInfo callbackInfo) {
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / 300.0f;
        double d = EaseUtils.easeOutQuart(this.progress);
        double d2 = (double)Math.max(500L - (System.currentTimeMillis() - this.guiOpenTime), 0L) / 500.0;
        double d3 = 1.0 - d2;
        ChestStealer chestStealer = (ChestStealer)LiquidBounce.moduleManager.getModule(ChestStealer.class);
        GL11.glTranslated((double)((1.0 - d) * ((double)this.field_146294_l / 2.0)), (double)((1.0 - d) * ((double)this.field_146295_m / 2.0)), (double)0.0);
        GL11.glScaled((double)d, (double)d, (double)d);
        GL11.glPushMatrix();
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")}, cancellable=true)
    protected void drawScreenReturn(CallbackInfo callbackInfo) {
        GL11.glPopMatrix();
    }

    @Inject(method={"mouseClicked"}, at={@At(value="RETURN")})
    private void mouseClicked(int n, int n2, int n3, CallbackInfo callbackInfo) {
        for (Object e : this.field_146292_n) {
            GuiButton guiButton = (GuiButton)e;
            if (guiButton.func_146116_c(this.field_146297_k, n, n2) && guiButton.field_146127_k == 1024576) {
                LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
            }
            if (!guiButton.func_146116_c(this.field_146297_k, n, n2) || guiButton.field_146127_k != 321123) continue;
            LiquidBounce.moduleManager.getModule(InventoryCleaner.class).setState(false);
        }
    }
}

