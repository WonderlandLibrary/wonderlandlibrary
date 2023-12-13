/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vector3d;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.BlockBBEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.utils.MovementUtil;

public class VerusFlat {
    private Vector3d jumpPos = new Vector3d(0.0, 0.0, 0.0);
    private final Minecraft mc = Client.mc;

    public void onEnable() {
        this.mc.thePlayer.jump();
        if (!this.mc.thePlayer.isCollidedHorizontally && !this.mc.gameSettings.keyBindJump.pressed) {
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688698, this.mc.thePlayer.posZ);
            this.mc.thePlayer.onGround = false;
            MovementUtil.strafe(0.35);
        }
        this.jumpPos = new Vector3d(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
    }

    public void onDisable() {
    }

    @Subscribe
    public void onPacketSent(PacketSentEvent event) {
        double landedTicks;
        if (event.getPacket() instanceof C03PacketPlayer && (landedTicks = (double)(this.mc.thePlayer.lastGroundTick - this.mc.thePlayer.lastAirTick)) >= 10.0) {
            event.setCanceled(true);
        }
    }

    @Subscribe
    public void onWorldTick(WorldTickEvent event) {
        double landedTicks = this.mc.thePlayer.lastGroundTick - this.mc.thePlayer.lastAirTick;
        if ((landedTicks += 1.0) <= 9.0 && landedTicks > 0.0 && this.mc.thePlayer.onGround) {
            this.mc.thePlayer.setSpeed((float)(landedTicks * (double)0.15f));
        } else if (landedTicks <= 10.0) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
        if (landedTicks == 11.0) {
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688698, this.mc.thePlayer.posZ, false));
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.3415999853611, this.mc.thePlayer.posZ, false));
        }
        if (landedTicks == 12.0) {
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1863679808445, this.mc.thePlayer.posZ, false));
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.lastGroundTick = this.mc.thePlayer.ticksExisted;
            this.mc.thePlayer.lastAirTick = this.mc.thePlayer.ticksExisted;
        }
    }

    @Subscribe
    public void onBB(BlockBBEvent event) {
        if (event.getBlock() instanceof BlockAir && (double)event.getPos().getY() <= this.jumpPos.y) {
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1.0, this.jumpPos.y, event.getZ() + 1.0));
        }
    }
}

