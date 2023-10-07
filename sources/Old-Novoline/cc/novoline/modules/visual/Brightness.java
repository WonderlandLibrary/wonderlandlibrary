package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.events.events.SettingEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.EnumModuleType.VISUALS;

public final class Brightness extends AbstractModule {

    private float oldGamma;

    /* properties */
    @Property("mode")
    private final StringProperty mode = PropertyFactory.createString("Gamma").acceptableValues("Gamma", "Effect");

    /* constructors */
    public Brightness(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Brightness", VISUALS, "It's too dark in here");
        Manager.put(new Setting("BN_MODE", "Mode", SettingType.COMBOBOX, this, mode));
    }

    /* methods */

    @Override
    public void onEnable() {
        setSuffix(mode.get());

        if (mode.equals("Gamma")) {
            if (mc.player.isPotionActive(16)) {
                mc.player.removePotionEffect(16);
            }

            oldGamma = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 10_000.0F;
        }
    }

    @Override
    public void onDisable() {
        if (mode.equals("Effect")) {
            this.mc.player.removePotionEffect(Potion.nightVision.getId());
        } else {
            mc.gameSettings.gammaSetting = oldGamma;
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        setSuffix(mode.get());
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (mode.equals("Effect")) {
            this.mc.player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5201, 1));
        } else if (mc.currentScreen instanceof GuiVideoSettings && mc.gameSettings.gammaSetting != 10_000.0F) {
            mc.gameSettings.gammaSetting = 10_000.0F;
        }
    }

    @EventTarget
    public void onSetting(SettingEvent event) {
        if (event.getSettingName().equals("BN_MODE")) {
            if (mode.equals("Gamma")) {
                if (mc.player.isPotionActive(16)) {
                    mc.player.removePotionEffect(16);
                }

                oldGamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 10_000.0F;

            } else if (mc.gameSettings.gammaSetting != oldGamma) {
                mc.gameSettings.gammaSetting = oldGamma;
            }
        }
    }

    public StringProperty getMode() {
        return mode;
    }
}
