package vestige.module.impl.combat;

import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;

public class Reach extends Module {

    public final DoubleSetting startingReach = new DoubleSetting("Starting reach", 3.5, 3, 6, 0.05);
    public final DoubleSetting reach = new DoubleSetting("Reach", 3.5, 3, 6, 0.05);

    public Reach() {
        super("Reach", Category.COMBAT);
        this.addSettings(startingReach, reach);
    }

}
