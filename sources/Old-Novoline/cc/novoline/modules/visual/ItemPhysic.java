package cc.novoline.modules.visual;

import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

public final class ItemPhysic extends AbstractModule {

    /* constructors */
    public ItemPhysic(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "ItemPhysic", "Item Physic", Keyboard.KEY_NONE, EnumModuleType.VISUALS,
                "Real items physic");
    }

}
