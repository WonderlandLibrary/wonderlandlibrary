package sudo.events;

import net.minecraft.client.util.math.MatrixStack;
import sudo.core.event.Event;

public class EventRender3D extends Event {
	
    private MatrixStack matrices;
    private static float tickDelta;
    private double offsetX, offsetY, offsetZ;

    @SuppressWarnings("static-access")
	public EventRender3D(MatrixStack matrices, float tickDelta, double offsetX, double offsetY, double offsetZ) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }
    
    public MatrixStack getMatrices() {
		return matrices;
	}
    
    public static float getTickDelta() {
		return tickDelta;
	}
    
    public double getOffsetX() {
		return offsetX;
	}
    
    public double getOffsetY() {
		return offsetY;
	}
    
    public double getOffsetZ() {
		return offsetZ;
	}
}