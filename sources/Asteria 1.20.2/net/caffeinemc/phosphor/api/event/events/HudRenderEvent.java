package net.caffeinemc.phosphor.api.event.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings("all")
public class HudRenderEvent {

	private static final HudRenderEvent INSTANCE = new HudRenderEvent();

	public DrawContext context;
	public float tickDelta;

	public static HudRenderEvent get(DrawContext context, float tickDelta) {
		INSTANCE.context = context;
		INSTANCE.tickDelta = tickDelta;
		return INSTANCE;
	}
}
