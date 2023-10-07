package sudo.module.render;

import sudo.module.Mod;
import sudo.module.settings.ISimpleOption;

public class Fullbright extends Mod {
	
    public Fullbright() {
        super("Fullbright", "Sets the brightness to maximum", Category.RENDER, 0);
    }
    
	@SuppressWarnings("unchecked")
	@Override
    public void onTick() {
        if (this.isEnabled()) {
        	((ISimpleOption<Double>) (Object) mc.options.getGamma()).setValueUnrestricted(100.0d);
	    }
	}
    
    @Override
    public void onDisable() {
    	mc.options.getGamma().setValue(0.0);
    	super.onDisable();
    }
}