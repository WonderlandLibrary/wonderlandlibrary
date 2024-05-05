package net.caffeinemc.phosphor.api.event.events;

import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings("all")
public class HudRenderEvent {

	private static final HudRenderEvent INSTANCE = new HudRenderEvent();

	public MatrixStack matrices;
	public float tickDelta;

	public static HudRenderEvent get(MatrixStack matrices, float tickDelta) {
		INSTANCE.matrices = matrices;
		INSTANCE.tickDelta = tickDelta;
		return INSTANCE;
	}
}
