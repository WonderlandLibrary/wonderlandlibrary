package club.cloverhook.event.minecraft;

/**
 * @author antja03
 */
public class EntityRenderEvent {
    private float partialTicks;

    public EntityRenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
