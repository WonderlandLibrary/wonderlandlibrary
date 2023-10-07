package wtf.monsoon;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.Wrapper;
import wtf.monsoon.api.event.impl.EventKey;
import wtf.monsoon.api.module.Module;
import net.minecraft.client.gui.GuiChat;

public class Core implements Wrapper {
	
	public static Core INSTANCE = new Core();
	
	public void onKeyPress(int key)
	{
		for (Module m : Monsoon.INSTANCE.manager.modules)
		{
			if (m.getKey() == key)
			{
				m.toggle();
			}
		}
		
		if (key == Keyboard.KEY_PERIOD)
		{
			mc.displayGuiScreen(new GuiChat("."));
		}
		
		EventKey e = new EventKey(key);
		e.call();
	}
	

}
