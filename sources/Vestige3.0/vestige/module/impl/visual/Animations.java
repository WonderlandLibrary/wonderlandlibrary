package vestige.module.impl.visual;

import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;

public class Animations extends Module {

    public final DoubleSetting swingSlowdown = new DoubleSetting("Swing slowdown", 1, 0.1, 8, 0.1);

    public Animations() {
        super("Animations", Category.VISUAL);
        this.addSettings(swingSlowdown);
    }

}
