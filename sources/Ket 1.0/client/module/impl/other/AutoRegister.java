package client.module.impl.other;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.other.autoregister.BlocksMCAutoRegister;
import client.module.impl.other.autoregister.MushAutoRegister;
import client.value.impl.BooleanValue;
import client.value.impl.ModeValue;
import lombok.Getter;

@ModuleInfo(name = "Auto Register", description = "dadada", category = Category.OTHER)
public class AutoRegister extends Module {
    @Getter
    private final BooleanValue mushGen = new BooleanValue("MushGen", this, false);
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new MushAutoRegister("Mush", this))
            .add(new BlocksMCAutoRegister("BlocksMC", this))
            .setDefault("Mush");
}
