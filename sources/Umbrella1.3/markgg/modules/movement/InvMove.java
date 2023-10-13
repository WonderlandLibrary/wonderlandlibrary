/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove
extends Module {
    public InvMove() {
        super("InvMove", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event e) {
        block9: {
            if (!(e instanceof EventUpdate)) break block9;
            if (this.mc.currentScreen instanceof GuiScreen) {
                if (Keyboard.isKeyDown((int)205) && !(this.mc.currentScreen instanceof GuiChat)) {
                    this.mc.thePlayer.rotationYaw += 8.0f;
                }
                if (Keyboard.isKeyDown((int)203) && !(this.mc.currentScreen instanceof GuiChat)) {
                    this.mc.thePlayer.rotationYaw -= 8.0f;
                }
                if (Keyboard.isKeyDown((int)200) && !(this.mc.currentScreen instanceof GuiChat)) {
                    this.mc.thePlayer.rotationPitch -= 8.0f;
                }
                if (Keyboard.isKeyDown((int)208) && !(this.mc.currentScreen instanceof GuiChat)) {
                    this.mc.thePlayer.rotationPitch += 8.0f;
                }
            }
            KeyBinding[] moveKeys = new KeyBinding[]{this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSprint};
            if (this.mc.currentScreen instanceof GuiScreen && !(this.mc.currentScreen instanceof GuiChat)) {
                KeyBinding[] arrkeyBinding = moveKeys;
                int n = moveKeys.length;
                for (int i = 0; i < n; ++i) {
                    KeyBinding key = arrkeyBinding[i];
                    key.pressed = Keyboard.isKeyDown((int)key.getKeyCode());
                }
            } else {
                KeyBinding[] arrkeyBinding = moveKeys;
                int n = moveKeys.length;
                for (int i = 0; i < n; ++i) {
                    KeyBinding bind = arrkeyBinding[i];
                    if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }
}

