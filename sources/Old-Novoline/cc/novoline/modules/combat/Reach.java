package cc.novoline.modules.combat;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.FloatProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.gui.screen.setting.SettingType.SLIDER;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createFloat;

public final class Reach extends AbstractModule {

    /* properties @off */
    @Property("range")
    private final FloatProperty range = createFloat(5.0F).minimum(3.0F).maximum(5.0F);

    private boolean hittingBlock;

    /* constructors @on */
    public Reach(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Reach", EnumModuleType.COMBAT, "Expands reach");
        Manager.put(new Setting("Reach_Distance", "Range", SLIDER, this, this.range, 0.1D));
    }

    public float getRange() {
        return range.get();
    }
}
