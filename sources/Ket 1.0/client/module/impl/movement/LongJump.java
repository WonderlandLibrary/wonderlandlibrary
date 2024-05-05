package client.module.impl.movement;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;

import client.module.impl.movement.longjump.MushLongJump;
import client.value.impl.ModeValue;

@ModuleInfo(name = "LongJump", description = "Assy", category = Category.MOVEMENT)
public class LongJump extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new MushLongJump("Mush", this))
            .setDefault("Mush");
}
