package today.flux.module.implement.Combat;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;

public class HitBox extends Module {
    public static FloatValue size = new FloatValue("HitBox", "Size", 0.3f, 0.1f, 0.4f, 0.1f);

    public HitBox() {
        super("HitBox", Category.Ghost, false);
    }
}
