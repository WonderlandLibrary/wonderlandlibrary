/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import me.report.liquidware.utils.ui.FuckerNMSL;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngameMenu.class})
public abstract class MixinGuiIngameMenu
extends MixinGuiScreen {
    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        if (!this.field_146297_k.func_71387_A()) {
            this.field_146292_n.add(new GuiButton(1337, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 128, "Reconnect"));
        }
    }

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.field_146127_k == 1337) {
            this.field_146297_k.field_71441_e.func_72882_A();
            ServerUtils.connectToLastServer();
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawScreen(CallbackInfo callbackInfo) {
        Fonts.minecraftFont.func_175063_a("\u00a77Username: \u00a7a" + this.field_146297_k.func_110432_I().func_111285_a(), 6.0f, 6.0f, 0xFFFFFF);
        Fonts.minecraftFont.func_175063_a("\u00a77Username client: \u00a7a" + FuckerNMSL.username.getText(), 6.0f, 16.0f, 0xFFFFFF);
        if (!this.field_146297_k.func_71387_A()) {
            Fonts.minecraftFont.func_175063_a("\u00a77IP: \u00a7a" + this.field_146297_k.func_147104_D().field_78845_b, 6.0f, 26.0f, 0xFFFFFF);
            Fonts.minecraftFont.func_175063_a("\u00a77Ping: \u00a7a" + EntityUtils.getPing((EntityPlayer)this.field_146297_k.field_71439_g), 6.0f, 36.0f, 0xFFFFFF);
        }
    }
}

