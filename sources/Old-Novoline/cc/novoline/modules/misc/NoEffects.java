package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.ListProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.visual.Brightness;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.gui.screen.setting.SettingType.SELECTBOX;
import static cc.novoline.modules.EnumModuleType.MISC;

public final class NoEffects extends AbstractModule {

    /* properties @off */
    @Property("effects")
    private final ListProperty<String> effects = PropertyFactory.createList("Night Vision", "Blindness", "Nausea").acceptableValues("Night Vision", "Blindness", "Nausea");

    /* constructors @on */
    public NoEffects(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "NoEffects", "No Effects", MISC, "remove bad potion effects");
        Manager.put(new Setting("NF_EFFECTS", "Effects", SELECTBOX, this, effects));
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (effects.contains("Nausea") && mc.player.isPotionActive(9)) {
            mc.player.removePotionEffectClient(9);
        }

        //todo
        if (!isEnabled(Brightness.class) || !getModule(Brightness.class).getMode().equals("Effect")) {
            if (effects.contains("Night Vision") && mc.player.isPotionActive(16)) {
                mc.player.removePotionEffectClient(16);
            }
        }

        if (effects.contains("Blindness") && mc.player.isPotionActive(15)) {
            mc.player.removePotionEffectClient(15);
        }
    }

    /* events */
    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S1DPacketEntityEffect) {
                S1DPacketEntityEffect packet = (S1DPacketEntityEffect) event.getPacket();

                if (effects.contains("Nausea") && packet.getEffect().getPotionID() == 9 ||
                        effects.contains("Night Vision") && packet.getEffect().getPotionID() == 16 ||
                        effects.contains("Blindness") && packet.getEffect().getPotionID() == 15) {
                    packet.getEffect().setShowParticles(false);
                    event.setCancelled(true);
                }
            }
        }
    }
}
