/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class CheckKey {
    private static Wrapper wrap = new Wrapper();
    private boolean[] keyStates = new boolean[256];

    public boolean checkKey(int i2) {
        if (Wrapper.mc.currentScreen != null) {
            return false;
        }
        if (Keyboard.isKeyDown(i2) != this.keyStates[i2]) {
            this.keyStates[i2] = !this.keyStates[i2];
            return this.keyStates[i2];
        }
        return false;
    }
}

