package today.flux.module.implement.Misc;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Misc.disabler.Hypixel;

public class Disabler extends Module {

    public Disabler() {
        super("Disabler", Category.Misc, true, new Hypixel());
    }
}
