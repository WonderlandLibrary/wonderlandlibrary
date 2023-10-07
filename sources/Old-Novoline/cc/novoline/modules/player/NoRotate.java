package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.EnumModuleType.PLAYER;

public final class NoRotate extends AbstractModule {

    @Property("ground-check")
    private final BooleanProperty ground_check = PropertyFactory.booleanTrue();

    /* constructors */
    public NoRotate(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "NoRotate", "No Rotate", PLAYER, "Blocks server-sided rotate packets to prevent your head from rotating on flag");
        Manager.put(new Setting("GROUND_CHECK", "On Ground", SettingType.CHECKBOX, this, ground_check));
    }

    /* events */
    @EventTarget
    public void onReceive(PacketEvent e) {
        if (e.getState().equals(PacketEvent.State.INCOMING)) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                if (mc.player.onGround || !ground_check.get()) {
                    S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();

                    packet.setPitch(mc.player.rotationPitch);
                    packet.setYaw(mc.player.rotationYaw);
                }
            }
        }
    }
}
