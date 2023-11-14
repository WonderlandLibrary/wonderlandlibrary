package net.ccbluex.LiquidBase.injection.mixins;

import net.ccbluex.LiquidBase.LiquidBase;
import net.ccbluex.LiquidBase.event.EventManager;
import net.ccbluex.LiquidBase.event.events.KeyEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
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
@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public class MixinMinecraft {

    @Shadow public GuiScreen currentScreen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMinecraft(CallbackInfo callbackInfo) {
        new LiquidBase();
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startClient(CallbackInfo callbackInfo) {
        LiquidBase.CLIENT_INSTANCE.startClient();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void stopClient(CallbackInfo callbackInfo) {
        LiquidBase.CLIENT_INSTANCE.stopClient();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void keyPress(CallbackInfo callbackInfo) {
        if(Keyboard.getEventKeyState() && currentScreen == null)
            EventManager.callEvent(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
    }
}