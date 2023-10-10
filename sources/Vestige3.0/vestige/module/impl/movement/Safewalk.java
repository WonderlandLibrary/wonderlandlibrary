package vestige.module.impl.movement;

import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;

public class Safewalk extends Module {

    public final BooleanSetting offGround = new BooleanSetting("Offground", false);

    public Safewalk() {
        super("Safewalk", Category.MOVEMENT);
        this.addSettings(offGround);
    }

}
