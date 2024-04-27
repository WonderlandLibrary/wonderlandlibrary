/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={GuiScreen.class})
public class GuiScreenMixin_FixWindowsIME {
    @Redirect(method={"handleKeyboardInput"}, at=@At(value="INVOKE", target="Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", remap=false))
    private boolean patcher$checkCharacter() {
        return Keyboard.getEventKey() == 0 && Keyboard.getEventCharacter() >= ' ' || Keyboard.getEventKeyState();
    }
}

