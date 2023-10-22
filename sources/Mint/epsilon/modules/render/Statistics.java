package epsilon.modules.render;

import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.modules.Module;

public class Statistics extends Module{
	
	public Statistics() {
		super("Statistics", Keyboard.KEY_NONE, Category.RENDER, "Displays statistics like Time, KD, etc, from when you join a server");
	}
}
