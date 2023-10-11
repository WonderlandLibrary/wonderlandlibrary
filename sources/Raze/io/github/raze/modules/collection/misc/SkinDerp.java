package io.github.raze.modules.collection.misc;

import io.github.raze.Raze;
import io.github.raze.events.collection.game.EventUpdate;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Set;

public class SkinDerp extends AbstractModule {

    private final NumberSetting delay;
    private final TimeUtil timer;

    public SkinDerp() {
        super("SkinDerp", "Makes your skin layers blink.", ModuleCategory.MISC);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(
                delay = new NumberSetting(this, "Delay", 10, 1000, 100)
        );

        timer = new TimeUtil();
    }

    @Override
    public void onDisable() {
        for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
            mc.gameSettings.setModelPartEnabled(part, true);
        }
    }

    @Listen
    public void onUpdate(EventUpdate eventUpdate) {
        if (timer.elapsed(delay.get().longValue(), true)) {
            Set<EnumPlayerModelParts> activeParts = mc.gameSettings.getModelParts();
            for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
                mc.gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
            }
        }
    }

}
