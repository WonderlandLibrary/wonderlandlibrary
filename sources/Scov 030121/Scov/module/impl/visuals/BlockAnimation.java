package Scov.module.impl.visuals;

import Scov.api.annotations.Handler;
import Scov.events.player.EventMotionUpdate;
import Scov.module.Module;
import Scov.value.impl.EnumValue;
import Scov.value.impl.NumberValue;

public class BlockAnimation extends Module {
	
	public EnumValue<AnimationsMode> animationsMode = new EnumValue<>("BlockAnimation Mode", AnimationsMode.Felix);
	
	public NumberValue<Integer> swingSpeed = new NumberValue<>("Swing Speed", 6, 2, 12, 1);

	public NumberValue<Float> scale = new NumberValue<>("Item Scale", 0.4f, 0.0f, 1.0f);
	
    public final NumberValue<Float> x = new NumberValue<>("X", 0.0f, -1f, 1f, 0.05f);
    public final NumberValue<Float> y = new NumberValue<>("Y", 0.0f, -1f, 1f, 0.05f);
    public final NumberValue<Float> z = new NumberValue<>("Z", 0.0f, -1f, 1f, 0.05f);
	
	public BlockAnimation() {
		super("BlockAnimation", 0, ModuleCategory.VISUALS);
		addValues(animationsMode, swingSpeed, scale, x, y, z);
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	public enum AnimationsMode {
		Swang, Swank, Swing, Life, Table, Stab, Felix, Sensation, Virtue, Sigma, Exhibition, Remix, Knife, Tap;
	}
	
	@Handler
	public void onMotionUpdate(final EventMotionUpdate event) {
		setSuffix(animationsMode.getValueAsString());
	}
}
