package cc.novoline.modules.combat;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.FloatProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.createFloat;

public final class HitBox extends AbstractModule {

    /* properties @off */
    @Property("size")
    private final FloatProperty hitBoxSize = createFloat(0.3F).minimum(0.1F).maximum(1.0F);

    /* constructors @on */
    public HitBox(@NonNull ModuleManager moduleManager) {
        super(moduleManager, EnumModuleType.COMBAT, "HitBox", "Hit Box");
        Manager.put(new Setting("ENTITY_BOX", "Box Size", SettingType.SLIDER, this, this.hitBoxSize, 0.1F));
    }

    public FloatProperty getHitBoxSize() {
        return hitBoxSize;
    }
}

