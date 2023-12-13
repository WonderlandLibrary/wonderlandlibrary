package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventFOVModifier;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;


@ModuleInfo(name = "Camera", description = "Contains No hurtcam, No Fov and more", category = Category.RENDER)
public class Camera extends Module {
	public static final NumberValue<Float> modifier = new NumberValue<Float>("Zoom modifier", "Zoom", 2.0F, 0.1F, 0.1F, 6.0F);
	public static BooleanValue noHurtCam = new BooleanValue("No hurtcam", "Disables the hurt cam of minecraft", false);
	public static BooleanValue smoothZoom = new BooleanValue("Smooth Zoom", "OptiFine smooth zoom", true);
	private final BooleanValue noSpeedFov = new BooleanValue("No speed fov", "Disables the Speed fov", false);
	@EventHandler
	public final Listener<EventFOVModifier> eventFOVModifierListener = e -> {
		if (noSpeedFov.getObject()) {
			e.setModifier(1.0f);
			if (mc.thePlayer.isSprinting()) {
				e.setModifier(e.getModifier() * 1.15f);
			}
		}
	};
	public BooleanValue noClip = new BooleanValue("No Clip", "Disables the clip-check of the cam of minecraft", false);
	public BooleanValue lowFire = new BooleanValue("Low Fire", "Lowers the fire effect", false);
}
