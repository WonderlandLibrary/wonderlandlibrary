package me.earth.earthhack.impl.core.mixins.minecraft.gui;

import me.earth.earthhack.impl.services.chat.CommandManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen
{
    @Shadow
    protected GuiTextField inputField;

    @Inject(method = "drawScreen(IIF)V", at = @At("HEAD"))
    public void drawScreenHook(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo)
    {
        CommandManager.getInstance().renderCommandGui(inputField.getText(), inputField.x, inputField.y);
    }

    @Redirect(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/TabCompleter;complete()V"))
    protected void completerHook(TabCompleter completer)
    {
        if (CommandManager.getInstance().onTabComplete(inputField))
        {
            completer.complete();
        }
    }

}
