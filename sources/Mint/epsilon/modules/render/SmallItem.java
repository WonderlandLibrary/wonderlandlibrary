package epsilon.modules.render;
import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.settings.setting.NumberSetting;

public class SmallItem extends Module{
	
	public NumberSetting scale = new NumberSetting("Scale", 0.5, -1, 2, 0.1);
	public NumberSetting x = new NumberSetting("X", 0.3, -1, 2, 0.1);
	public NumberSetting y = new NumberSetting("Y", 0, -1, 2, 0.1);
	public NumberSetting z = new NumberSetting("Z", -0.4, -1, 2, 0.1);
	
	public static double xItem,yItem,zItem,itemScale;
	
	public SmallItem() {
		super("SmallItem", Keyboard.KEY_NONE, Category.RENDER, "Tiny Tiny Items");
		this.addSettings(scale, x, y, z);
	}
	
	public void onDisable() {
		xItem = yItem = zItem = itemScale = 1;
	}
	
	
	public void onEvent(Event e) {
		xItem = x.getValue(); yItem = y.getValue(); zItem = z.getValue(); itemScale = scale.getValue();
	}
	
}
