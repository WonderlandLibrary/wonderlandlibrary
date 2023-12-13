package vestige.util.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import vestige.util.IMinecraft;

public class KeyboardUtil implements IMinecraft {

    public static boolean isPressed(KeyBinding key) {
        return Keyboard.isKeyDown(key.getKeyCode());
    }

    public static void resetKeybinding(KeyBinding key) {
        //if(mc.currentScreen instanceof GuiContainer) {
        if(mc.currentScreen != null) {
            key.pressed = false;
        } else {
            key.pressed = isPressed(key);
        }
    }

    public static void resetKeybindings(KeyBinding... keys) {
        for(KeyBinding key : keys) {
            resetKeybinding(key);
        }
    }

}
