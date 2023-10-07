package cc.novoline.modules.misc;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;

public final class FastPlace extends AbstractModule {

    /* properties @off */
    @Property("place-delay")
    public final IntProperty placeDelay = createInt(3).minimum(1).maximum(4);
    @Property("blocks-only")
    public final BooleanProperty blocksOnly = PropertyFactory.booleanTrue();

    /* constructors @on */
    public FastPlace(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "FastPlace", "Fast Place", EnumModuleType.MISC, "place blocks faster");
        Manager.put(new Setting("PLACE_DELAY", "Place Delay", SettingType.SLIDER, this, this.placeDelay, 1));
        Manager.put(new Setting("BLOCKS_ONLY", "Blocks Only", SettingType.CHECKBOX, this, this.blocksOnly));
    }
}
