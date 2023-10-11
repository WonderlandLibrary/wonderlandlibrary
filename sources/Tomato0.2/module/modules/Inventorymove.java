/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Inventorymove
extends Module {
    public Inventorymove() {
        super("InvMove", 0, Category.MOVEMENT, 52224, true, "Allows a player to move in their inventory.", new String[]{"invmove", "imove"});
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public void call(EventPreMotionUpdates event) {
        KeyBinding[] moveKeys = new KeyBinding[]{Wrapper.mc.gameSettings.keyBindRight, Wrapper.mc.gameSettings.keyBindLeft, Wrapper.mc.gameSettings.keyBindBack, Wrapper.mc.gameSettings.keyBindForward, Wrapper.mc.gameSettings.keyBindJump, Wrapper.mc.gameSettings.keyBindSprint};
        if (Wrapper.mc.currentScreen instanceof GuiContainer) {
            KeyBinding[] array = moveKeys;
            int length = array.length;
            int i2 = 0;
            while (i2 < length) {
                KeyBinding key = array[i2];
                key.pressed = Keyboard.isKeyDown(key.getKeyCode());
                ++i2;
            }
            return;
        } else {
            if (!Objects.isNull(Wrapper.mc.currentScreen)) return;
            KeyBinding[] array2 = moveKeys;
            int length2 = array2.length;
            int j2 = 0;
            while (j2 < length2) {
                KeyBinding bind = array2[j2];
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
                ++j2;
            }
        }
    }
}

