// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.render.Breadcrumbs;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import java.util.function.Consumer;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import net.minecraft.util.Vec3;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import net.minecraft.network.Packet;
import java.util.concurrent.ConcurrentLinkedQueue;
import dev.tenacity.module.Module;

public final class Blink extends Module
{
    final ConcurrentLinkedQueue<Packet<?>> packets;
    private final BooleanSetting pulse;
    private final NumberSetting delayPulse;
    private EntityOtherPlayerMP blinkEntity;
    List<Vec3> path;
    
    public Blink() {
        super("Blink", "Blink", Category.PLAYER, "holds movement packets");
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.pulse = new BooleanSetting("Pulse", false);
        this.delayPulse = new NumberSetting("Tick Delay", 20.0, 100.0, 4.0, 1.0);
        this.path = new ArrayList<Vec3>();
        this.delayPulse.addParent(this.pulse, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.pulse, this.delayPulse);
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (Blink.mc.thePlayer == null || Blink.mc.thePlayer.isDead || Blink.mc.isSingleplayer() || Blink.mc.thePlayer.ticksExisted < 50) {
            this.packets.clear();
            return;
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            this.packets.add(event.getPacket());
            event.cancel();
        }
        if (this.pulse.isEnabled() && !this.packets.isEmpty() && Blink.mc.thePlayer.ticksExisted % this.delayPulse.getValue().intValue() == 0 && Math.random() > 0.1) {
            this.packets.forEach(PacketUtils::sendPacketNoEvent);
            this.packets.clear();
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre()) {
            if (Blink.mc.thePlayer.ticksExisted < 50) {
                return;
            }
            if (Blink.mc.thePlayer.lastTickPosX != Blink.mc.thePlayer.posX || Blink.mc.thePlayer.lastTickPosY != Blink.mc.thePlayer.posY || Blink.mc.thePlayer.lastTickPosZ != Blink.mc.thePlayer.posZ) {
                this.path.add(new Vec3(Blink.mc.thePlayer.posX, Blink.mc.thePlayer.posY, Blink.mc.thePlayer.posZ));
            }
            if (this.pulse.isEnabled()) {
                while (this.path.size() > this.delayPulse.getValue().intValue()) {
                    this.path.remove(0);
                }
            }
            if (this.pulse.isEnabled() && this.blinkEntity != null) {
                Blink.mc.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
            }
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        Tenacity.INSTANCE.getModuleCollection().getModule(Breadcrumbs.class).renderLine(this.path);
    }
    
    @Override
    public void onEnable() {
        this.path.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.packets.forEach(PacketUtils::sendPacketNoEvent);
        this.packets.clear();
        super.onDisable();
    }
}
