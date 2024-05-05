package tech.drainwalk.client.module.modules.combat;


import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;

public class KeepSprintModule extends Module {
    public KeepSprintModule() {
        super("KeepSprint", Category.COMBAT);
        addType(Type.SECONDARY);
    }
}
