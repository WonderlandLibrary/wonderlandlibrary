package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.EnumModuleType.VISUALS;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createDouble;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createString;

public final class Atmosphere extends AbstractModule {

    /* properties @off */
    @Property("time")
    private final DoubleProperty time = createDouble(12.0).minimum(1.0).maximum(24.0);
    @Property("weather-mode")
    private final StringProperty weather_mode = createString("Clean").acceptableValues("Clean", "Rain", "Thunder", "Snowfall", "Snowstorm");
    @Property("weather-control")
    private final BooleanProperty weather_control = PropertyFactory.booleanFalse();

    public long getTime() {
        return time.get().longValue() * 1000L;
    }

    /* constructors @on */
    public Atmosphere(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Atmosphere", VISUALS);
        Manager.put(new Setting("WORLD_TIME", "Hour", SettingType.SLIDER, this, this.time, 0.5));
        Manager.put(new Setting("WEATHER_CONTROL", "Weather control", SettingType.CHECKBOX, this, weather_control));
        Manager.put(new Setting("WEATHER_MODE", "Weather state", SettingType.COMBOBOX, this, weather_mode, weather_control::get));
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (weather_control.get()) {
            switch (weather_mode.get()) {
                case "Snowfall":
                case "Rain":
                    mc.world.setRainStrength(1);
                    mc.world.setThunderStrength(0);
                    break;
                case "Snowstorm":
                case "Thunder":
                    mc.world.setRainStrength(1);
                    mc.world.setThunderStrength(1);
                    break;
                default:
                    mc.world.setRainStrength(0);
                    mc.world.setThunderStrength(0);
            }
        }
    }

    public StringProperty getWeather_mode() {
        return weather_mode;
    }

}
