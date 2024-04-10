package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;

public final class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", Category.Movement, "Doesn't let you run off edges");
    }
}
