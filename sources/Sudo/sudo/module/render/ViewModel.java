package sudo.module.render;

import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class ViewModel extends Mod {

    public static ViewModel INSTANCE;

    public ViewModel() {
    	super("ViewModel", "Customize the held items position", Category.RENDER, 0); 
    	addSettings(x,y,z,swingLeft,swingRight);
    	 INSTANCE = this;
    }

    public static final NumberSetting x = new NumberSetting("X", -5, 5, 0, 0.01);
	public static final NumberSetting y = new NumberSetting("Y", -5, 5, 0, 0.01);
	public static final NumberSetting z = new NumberSetting("Z", -5, 5, 0, 0.01);
	
	public static final NumberSetting swingLeft = new NumberSetting("Swing Left", 0,1,0,0.1);
	public static final NumberSetting swingRight = new NumberSetting("Swing Right", 0,1,0,0.1);
}