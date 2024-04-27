/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={GameSettings.class})
public class GameSettingsMixin_ResolveCrash {
    @Overwrite
    public static boolean func_100015_a(KeyBinding key) {
        int keyCode = key.func_151463_i();
        if (keyCode != 0 && keyCode < 256) {
            return keyCode < 0 ? Mouse.isButtonDown((int)(keyCode + 100)) : Keyboard.isKeyDown((int)keyCode);
        }
        return false;
    }
}

