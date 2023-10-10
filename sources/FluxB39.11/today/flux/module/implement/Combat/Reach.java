package today.flux.module.implement.Combat;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

public class Reach extends Module {
    public static FloatValue range = new FloatValue("Reach", "Range", 4.0f, 3.0f, 6.0f, 0.1f);
    public static BooleanValue block = new BooleanValue("Reach", "Block Interact", true);

    public Reach() {
        super("Reach", Category.Ghost, false);
    }
}
