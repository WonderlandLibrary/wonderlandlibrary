/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.util.text.ITextComponent
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    @Inject(method={"drawChat"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawChat(int p_drawChat_1_, CallbackInfo callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
    }

    @Inject(method={"getChatComponent"}, at={@At(value="HEAD")}, cancellable=true)
    private void getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_, CallbackInfoReturnable<ITextComponent> callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getState() && ((Boolean)hud.getFontChatValue().get()).booleanValue()) {
            callbackInfo.setReturnValue(null);
        }
    }
}

