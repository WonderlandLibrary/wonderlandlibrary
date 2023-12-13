/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventBoundingBox;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Freecam
extends Module {
    double x;
    double y;
    double z;
    private EntityOtherPlayerMP prayerCopy;
    private double startX;
    private double startY;
    private double startZ;
    private float startYaw;
    private float startPitch;

    public Freecam() {
        super("Freecam", 22, Category.PLAYER, 16763904, true, "Freecam", new String[]{"fcam"});
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Wrapper.mc.thePlayer.setPosition(this.x, this.y, this.z);
        Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.01, Wrapper.getPlayer().posZ, Wrapper.getPlayer().onGround));
        Wrapper.mc.thePlayer.capabilities.isFlying = false;
        Wrapper.mc.thePlayer.noClip = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.x = Wrapper.mc.thePlayer.posX;
        this.y = Wrapper.mc.thePlayer.posY;
        this.z = Wrapper.mc.thePlayer.posZ;
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdates e2) {
        Wrapper.mc.thePlayer.capabilities.isFlying = true;
        Wrapper.mc.thePlayer.noClip = true;
        Wrapper.mc.thePlayer.capabilities.setFlySpeed(0.1f);
        e2.setCancelled(true);
    }

    @EventTarget
    public void onPacketSend(EventPacketSent e2) {
        if (e2.getPacket() instanceof C03PacketPlayer) {
            e2.setCancelled(true);
        }
    }

    @EventTarget
    public void onBB(EventBoundingBox e2) {
        e2.setBoundingBox(null);
    }
}

