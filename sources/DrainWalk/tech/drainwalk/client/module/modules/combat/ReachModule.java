package tech.drainwalk.client.module.modules.combat;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;

public class ReachModule extends Module {
    public ReachModule() {
        super("Reach", Category.COMBAT);
        addType(Type.SECONDARY);
    }
}
