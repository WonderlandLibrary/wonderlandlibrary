package cc.novoline.modules.combat;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.exploits.Blink;
import cc.novoline.modules.move.Speed;
import cc.novoline.utils.DebugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanFalse;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;

public final class Velocity extends AbstractModule {

    /* properties @off */
    @Property("alerts")
    private final BooleanProperty alerts = booleanFalse();
    @Property("horizontal")
    private final IntProperty horizontal = createInt(0).minimum(0).maximum(100);
    @Property("vertical")
    private final IntProperty vertical = createInt(0).minimum(0).maximum(100);
    @Property("chance")
    private final IntProperty chance = createInt(100).minimum(0).maximum(100);

    /* constructors @on */
    public Velocity(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Velocity", "Velocity", Keyboard.KEY_NONE, EnumModuleType.COMBAT, "Don't take knockback");
        put(new Setting("ALERTS", "Alerts", SettingType.CHECKBOX, this, alerts));
        put(new Setting("VEL_HOR", "Horizontal", SettingType.SLIDER, this, horizontal, 5));
        put(new Setting("VEL_VER", "Vertical", SettingType.SLIDER, this, vertical, 5));
        put(new Setting("VEL_CHANCE", "Chance", SettingType.SLIDER, this, chance, 5));
    }

    /* methods */
    public boolean shouldCancel() {
        return isEnabled(Blink.class) || horizontal.get().equals(0) && vertical.get().equals(0) || isEnabled(Speed.class);
    }

    @EventTarget
    private void onVelocity(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

                if (packet.getEntityID() == mc.player.getEntityID()) {
                    if (!shouldCancel()) {
                        if (Math.random() <= chance.get() / 100) {
                            packet.setMotionX(packet.getMotionX() * horizontal.get() / 100);
                            packet.setMotionY(packet.getMotionY() * vertical.get() / 100);
                            packet.setMotionZ(packet.getMotionZ() * horizontal.get() / 100);
                        } else {
                            packet.setMotionX(packet.getMotionX());
                            packet.setMotionY(packet.getMotionY());
                            packet.setMotionZ(packet.getMotionZ());
                        }

                    } else {
                        event.setCancelled(true);
                    }

                    if (alerts.get()) {
                        DebugUtil.log("Velocity", String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000)));
                    }
                }
            }
        }
    }

    @EventTarget
    private void onExplosion(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (shouldCancel() && event.getPacket() instanceof S27PacketExplosion) {
                event.setCancelled(true);
            }
        }
    }

    public void handleExplosion(Minecraft gameController, S27PacketExplosion packet) {
        if (!shouldCancel()) {
            if (Math.random() <= chance.get() / 100) {
                gameController.player.motionX += packet.getMotionX() * horizontal.get() / 100;
                gameController.player.motionY += packet.getMotionY() * vertical.get() / 100;
                gameController.player.motionZ += packet.getMotionZ() * horizontal.get() / 100;
            } else {
                gameController.player.motionX += packet.getMotionX();
                gameController.player.motionY += packet.getMotionY();
                gameController.player.motionZ += packet.getMotionZ();
            }
        }
    }

    @EventTarget
    public void onUpdate(TickUpdateEvent event) {
        setSuffix(horizontal.get() + ".0%" + " " + vertical.get() + ".0%");
    }

    @Override
    public void onEnable() {
        setSuffix(horizontal.get() + ".0%" + " " + vertical.get() + ".0%");
    }
}
