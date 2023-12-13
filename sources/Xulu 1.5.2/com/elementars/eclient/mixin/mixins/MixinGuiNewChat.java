// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.render.Chat;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.ModuleManager;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int var1, final int var2, final int var3, final int var4, final int var5) {
        if (!ModuleManager.isModuleEnabled("Chat") || !Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleT(Chat.class), "No Chat Background").getValue()) {
            Gui.drawRect(var1, var2, var3, var4, var5);
        }
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowMaybe(final FontRenderer fontRenderer, final String message, final float x, final float y, final int color) {
        if (!Chat.INSTANCE.isToggled()) {
            return fontRenderer.drawStringWithShadow(message, x, y, color);
        }
        if (Chat.INSTANCE.customFont.getValue()) {
            if (Chat.nochatshadow.getValue()) {
                return (int)Xulu.cFontRenderer.drawString(message, x, y, color);
            }
            return (int)Xulu.cFontRenderer.drawStringWithShadow(message, x, y, color);
        }
        else {
            if (Chat.nochatshadow.getValue()) {
                return fontRenderer.drawString(message, (int)x, (int)y, color);
            }
            return fontRenderer.drawStringWithShadow(message, x, y, color);
        }
    }
}
