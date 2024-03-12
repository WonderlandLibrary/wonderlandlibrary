// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import java.util.function.Consumer;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.movement.Speed;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.network.Packet;
import java.util.concurrent.ConcurrentLinkedQueue;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class AntiVoid extends Module
{
    private final ModeSetting mode;
    private final NumberSetting fallDist;
    private double lastGroundY;
    private static final ConcurrentLinkedQueue<Packet<?>> packets;
    
    public AntiVoid() {
        super("AntiVoid", "Anti Void", Category.PLAYER, "saves you from the void");
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Watchdog", "Blink" });
        this.fallDist = new NumberSetting("Fall Distance", 3.0, 20.0, 1.0, 0.5);
        this.addSettings(this.mode, this.fallDist);
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (this.mode.getMode().equals("Watchdog") && !Tenacity.INSTANCE.getModuleCollection().getModule(Speed.class).isEnabled() && event.getPacket() instanceof C03PacketPlayer) {
            if (!this.isBlockUnder()) {
                if (AntiVoid.mc.thePlayer.fallDistance < this.fallDist.getValue()) {
                    event.cancel();
                    AntiVoid.packets.add(event.getPacket());
                }
                else if (!AntiVoid.packets.isEmpty()) {
                    for (final Packet packet : AntiVoid.packets) {
                        final C03PacketPlayer c03 = (C03PacketPlayer)packet;
                        c03.setY(this.lastGroundY);
                        PacketUtils.sendPacketNoEvent(packet);
                    }
                    AntiVoid.packets.clear();
                }
            }
            else {
                this.lastGroundY = AntiVoid.mc.thePlayer.posY;
                if (!AntiVoid.packets.isEmpty()) {
                    AntiVoid.packets.forEach(PacketUtils::sendPacketNoEvent);
                    AntiVoid.packets.clear();
                }
            }
        }
    }
    
    private boolean isBlockUnder() {
        if (AntiVoid.mc.thePlayer.posY < 0.0) {
            return false;
        }
        for (int offset = 0; offset < (int)AntiVoid.mc.thePlayer.posY + 2; offset += 2) {
            final AxisAlignedBB bb = AntiVoid.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!AntiVoid.mc.theWorld.getCollidingBoundingBoxes(AntiVoid.mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        packets = new ConcurrentLinkedQueue<Packet<?>>();
    }
}
