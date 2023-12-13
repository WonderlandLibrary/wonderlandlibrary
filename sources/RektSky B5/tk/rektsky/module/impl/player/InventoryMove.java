/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class InventoryMove
extends Module {
    public KeyBinding[] affectedKeys;

    public InventoryMove() {
        super("InvMove", "Allowed you to move when you opened your inventory", 0, Category.PLAYER);
        this.affectedKeys = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSprint};
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof WorldTickEvent)) {
            return;
        }
        Class[] notAffectedScreens = new Class[]{GuiChat.class, GuiCommandBlock.class};
        if (this.mc.currentScreen == null) {
            return;
        }
        for (Class screen : notAffectedScreens) {
            if (!screen.getName().equals(this.mc.currentScreen.getClass().getName())) continue;
            return;
        }
        for (KeyBinding key : this.affectedKeys) {
            key.pressed = GameSettings.isKeyDown(key);
        }
    }
}

