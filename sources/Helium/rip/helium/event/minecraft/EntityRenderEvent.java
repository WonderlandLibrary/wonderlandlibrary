package rip.helium.event.minecraft;

/**
 * @author antja03
 */
public class EntityRenderEvent {
    private final float partialTicks;

    public EntityRenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
