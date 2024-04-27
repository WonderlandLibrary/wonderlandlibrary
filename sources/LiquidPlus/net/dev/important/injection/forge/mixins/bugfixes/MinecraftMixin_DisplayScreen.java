/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiScreenWorking
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_DisplayScreen {
    @ModifyArg(method={"launchIntegratedServer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", ordinal=1))
    private GuiScreen patcher$displayWorkingScreen(GuiScreen original) {
        return new GuiScreenWorking();
    }
}

