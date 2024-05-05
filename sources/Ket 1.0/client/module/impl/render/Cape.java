package client.module.impl.render;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.ModeValue;
import client.value.impl.SubMode;
import lombok.Getter;

@ModuleInfo(name = "Cape", description = "Removes delay between next jump", category = Category.RENDER)
public class Cape extends Module {
    @Getter
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Test"))
            .add(new SubMode("CatLicking"))
            .add(new SubMode("Kek"))
            .add(new SubMode("Dort"))
            .add(new SubMode("BichiyHui"))
            .setDefault("Test");
}