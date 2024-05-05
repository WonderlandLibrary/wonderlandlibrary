package sudo.module.combat;

import net.minecraft.util.Formatting;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class Reach extends Mod{
	
	public NumberSetting reach = new NumberSetting("Slider",  4, 7, 5, 0.5);
	
    public Reach() {
        super("Reach", "Extends the players reach", Category.COMBAT, 0);
        addSettings(reach);
    }    
    
    private static final Formatting Gray = Formatting.GRAY;

    @Override
	public void onTick() {
		this.setDisplayName("Reach" + Gray + " [R:"+reach.getValue()+"] ");
    }
    
    @Override
    public void onEnable() {
    	// TODO Auto-generated method stub
    	super.onEnable();
    }
    
    @Override
    public void onDisable() {
    	// TODO Auto-generated method stub
    	super.onDisable();
    }
}