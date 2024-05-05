package tech.drainwalk.client.module.modules.combat;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;

public class NoFriendDamageModule extends Module {
    public NoFriendDamageModule() {
        super("NoFriendDamage", Category.COMBAT);
        addType(Type.SECONDARY);
    }
}
