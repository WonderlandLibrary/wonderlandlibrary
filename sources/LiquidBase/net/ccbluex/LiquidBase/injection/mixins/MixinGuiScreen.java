package net.ccbluex.LiquidBase.injection.mixins;

import net.ccbluex.LiquidBase.LiquidBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@SideOnly(Side.CLIENT)
@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Shadow public Minecraft mc;

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void chatMessage(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        if(msg.startsWith(".")) {
            LiquidBase.CLIENT_INSTANCE.commandManager.callCommand(msg);
            mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            callbackInfo.cancel();
        }
    }
}