package club.cloverhook.event.minecraft;

/**
 * @author antja03
 */
public class BlockStepEvent {
    public double stepHeight;
    public boolean bypass;
    public boolean cancel;
    private boolean isPre;

    public BlockStepEvent(double stepHeight) {
        this.stepHeight = stepHeight;
        isPre = true;
    }
    public BlockStepEvent() {
        isPre = false;
    }
    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean state) {
        this.cancel = state;
    }

    public boolean isPre() {
        return isPre;
    }
}
