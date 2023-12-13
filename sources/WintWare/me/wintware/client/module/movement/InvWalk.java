/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvWalk
extends Module {
    public InvWalk() {
        super("InventoryWalk", Category.Player);
    }

    @EventTarget
    public void o(EventUpdate e) {
        KeyBinding[] arrkeyBinding = new KeyBinding[6];
        arrkeyBinding[0] = InvWalk.mc.gameSettings.keyBindForward;
        arrkeyBinding[1] = InvWalk.mc.gameSettings.keyBindBack;
        arrkeyBinding[2] = InvWalk.mc.gameSettings.keyBindLeft;
        arrkeyBinding[3] = InvWalk.mc.gameSettings.keyBindRight;
        arrkeyBinding[4] = InvWalk.mc.gameSettings.keyBindJump;
        arrkeyBinding[5] = InvWalk.mc.gameSettings.keyBindSprint;
        KeyBinding[] keys = arrkeyBinding;
        if (InvWalk.mc.currentScreen != null) {
            if (!(InvWalk.mc.currentScreen instanceof GuiChat)) {
                KeyBinding[] arrayOfKeyBinding = keys;
                int i = keys.length;
                for (int b = 0; b < i; b = (byte)(b + 1)) {
                    KeyBinding bind = arrayOfKeyBinding[b];
                    bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
                }
            }
        }
    }
}

