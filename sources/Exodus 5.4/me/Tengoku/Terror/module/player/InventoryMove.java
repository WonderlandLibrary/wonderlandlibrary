/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.module.player;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove
extends Module {
    public InventoryMove() {
        super("Inventory Move", 0, Category.PLAYER, "Move while you have your inventory opened.");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        block5: {
            KeyBinding[] keyBindingArray;
            KeyBinding[] keyBindingArray2;
            block4: {
                KeyBinding[] keyBindingArray3 = new KeyBinding[5];
                keyBindingArray3[0] = Minecraft.gameSettings.keyBindForward;
                keyBindingArray3[1] = Minecraft.gameSettings.keyBindBack;
                keyBindingArray3[2] = Minecraft.gameSettings.keyBindLeft;
                keyBindingArray3[3] = Minecraft.gameSettings.keyBindRight;
                keyBindingArray3[4] = Minecraft.gameSettings.keyBindJump;
                keyBindingArray = keyBindingArray2 = keyBindingArray3;
                if (InventoryMove.mc.currentScreen instanceof GuiContainer && !(InventoryMove.mc.currentScreen instanceof GuiIngameMenu)) break block4;
                if (InventoryMove.mc.currentScreen != null) break block5;
                int n = keyBindingArray2.length;
                int n2 = 0;
                while (n2 < n) {
                    KeyBinding keyBinding = keyBindingArray[n2];
                    if (!Keyboard.isKeyDown((int)keyBinding.getKeyCode())) {
                        KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
                    }
                    ++n2;
                }
                break block5;
            }
            if (InventoryMove.mc.currentScreen instanceof GuiChat) {
                return;
            }
            keyBindingArray = keyBindingArray2;
            int n = keyBindingArray2.length;
            int n3 = 0;
            while (n3 < n) {
                KeyBinding keyBinding = keyBindingArray[n3];
                keyBinding.pressed = Keyboard.isKeyDown((int)keyBinding.getKeyCode());
                ++n3;
            }
        }
    }
}

