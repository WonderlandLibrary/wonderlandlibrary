package net.caffeinemc.phosphor.api.event.events;

import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings("all")
public class WorldRenderEvent {

	private static final WorldRenderEvent INSTANCE = new WorldRenderEvent();

	public MatrixStack matrices;
	public float tickDelta;

	public static WorldRenderEvent get(MatrixStack matrices, float tickDelta) {
		INSTANCE.matrices = matrices;
		INSTANCE.tickDelta = tickDelta;
		return INSTANCE;
	}
}
