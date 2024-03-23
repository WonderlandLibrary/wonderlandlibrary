package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.KnockbackEvent;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import java.util.ArrayList;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Module {

    public FloatSetting chance;
    public DragSetting vertical;
    public DragSetting horizontal;
    public BooleanSetting ignoreExplosion;
    public ArrayList<ExplosionS2CPacket> packets;

    public Velocity() {
        super("Velocity", Category.Movement, "Reduces knockback you take");
        chance = setting("Chance", 60f, 0f, 100f).description("Chance of taking reduced/no knockback");
        vertical = setting("Vertical", 80, 100, 0, 100).description("How much vertical knockback to take");
        horizontal = setting("Horizontal", 80, 100, 0, 100).description("How much horizontal knockback to take");
        ignoreExplosion = setting("Ignore Explosions", true).description("Ignores knockback when flying away from explosions");
        packets = new ArrayList();
    }

    @EventListener
    public void event(KnockbackEvent event) {
        if (packets.isEmpty() || !ignoreExplosion.getObject()) {
            if (RandomUtil.INSTANCE.getRandom().nextFloat(100.0f) <= chance.getObject()) {
                float f = vertical.getValue() / 100;
                float f2 = horizontal.getValue() / 100;
                event.setX(event.getX() * f2);
                event.setY(event.getY() * f);
                event.setZ(event.getZ() * f2);
            }
        }
    }

    @EventListener
    public void event(TickEvent event) {
        for (ExplosionS2CPacket packet : this.packets) {
            if (getMc().player != null) {
                if (getMc().player.isOnGround()) {
                    packets.remove(packet);
                }
            }
        }
    }

    @EventListener
    public void event(PacketReceiveEvent event) {
        if (event.getPacket() instanceof ExplosionS2CPacket packet && (packet.getPlayerVelocityX() > 0 || packet.getPlayerVelocityY() > 0 || packet.getPlayerVelocityZ() > 0)) {
            packets.add(packet);
        }
    }
}
