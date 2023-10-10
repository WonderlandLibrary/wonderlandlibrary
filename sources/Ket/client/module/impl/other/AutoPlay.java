package client.module.impl.other;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.other.autoplay.MushAutoPlay;
import client.value.impl.ModeValue;

@ModuleInfo(name = "Auto Play", description = "black, black, black...", category = Category.OTHER)
public class AutoPlay extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new MushAutoPlay("Mush", this))
            .setDefault("Mush");
}
