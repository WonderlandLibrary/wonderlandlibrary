package club.cloverhook.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import club.cloverhook.gui.screen.MainMenuGui;

/**
 * @author antja03
 */
public class GuiUtil {

    public static void closeScreenAndReturn() {
    	if (Minecraft.getMinecraft().thePlayer != null) {
	        Minecraft.getMinecraft().thePlayer.closeScreen();
	        for (KeyBinding keyBinding : new KeyBinding[]{
	                Minecraft.getMinecraft().gameSettings.keyBindForward, Minecraft.getMinecraft().gameSettings.keyBindBack,
	                Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight,
	                Minecraft.getMinecraft().gameSettings.keyBindJump, Minecraft.getMinecraft().gameSettings.keyBindSprint})
	        {
	            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
	        }
    	} else {
    		Minecraft.getMinecraft().displayGuiScreen(new MainMenuGui());
    	}
    }

}
