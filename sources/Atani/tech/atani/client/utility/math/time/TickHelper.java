package tech.atani.client.utility.math.time;

public class TickHelper {
    private int tick = 0;

    public boolean hasReached(int ticks) { return tick >= ticks; }

    public boolean hasReached(int ticks, boolean reset) {
        if(hasReached(ticks)) {
            if(reset)
                reset();
            return true;
        }
        return false;
    }

    public void update() { tick++; }

    public void reset() { tick = 0; }

    public int getTick() { return tick;}

    public void setTick(int tick) { this.tick = tick; }

}