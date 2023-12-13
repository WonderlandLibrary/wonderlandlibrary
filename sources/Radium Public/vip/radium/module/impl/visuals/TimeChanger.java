package vip.radium.module.impl.visuals;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.impl.EnumProperty;

@ModuleInfo(label = "Time Changer", category = ModuleCategory.VISUALS)
public final class TimeChanger extends Module {

    private final EnumProperty<Time> time = new EnumProperty<>("World Time", Time.MORNING);

    public static boolean shouldChangeTime() {
        return ModuleManager.getInstance(TimeChanger.class).isEnabled();
    }

    public static int getWorldTime() {
        return ModuleManager.getInstance(TimeChanger.class).time.getValue().worldTicks;
    }

    public TimeChanger() {
        setHidden(true);
        toggle();
    }

    private enum Time {
        NIGHT(13000),
        MIDNIGHT(18000),
        MORNING(23000),
        DAY(1000);

        private final int worldTicks;

        Time(int worldTicks) {
            this.worldTicks = worldTicks;
        }
    }

}
