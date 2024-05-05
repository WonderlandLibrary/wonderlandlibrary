package io.github.raze.utilities.collection.math;

import io.github.raze.utilities.system.Methods;

public class TickUtil implements Methods {

    //Only should be used on UpdateEvent//

    private int tick = 0; // 20 Ticks = 1 Second

    public void update() { tick++; }

    public void reset() { tick = 0; }

    public boolean elapsed(int ticks) { return tick >= ticks; }
}
