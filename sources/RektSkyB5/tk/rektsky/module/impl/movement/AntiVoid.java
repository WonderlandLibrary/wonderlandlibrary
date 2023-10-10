/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.impl.world.Scaffold;
import tk.rektsky.utils.MovementUtil;

public class AntiVoid
extends Module {
    double lastX;
    double lastY;
    double lastZ;

    public AntiVoid() {
        super("AntiVoid", "You are bad if you have to use this LLLL", Category.MOVEMENT, false);
    }

    @Override
    public void onEnable() {
        this.lastX = this.mc.thePlayer.posX;
        this.lastY = this.mc.thePlayer.posY;
        this.lastZ = this.mc.thePlayer.posZ;
    }

    @Subscribe
    public void motionUpdateEvent(MotionUpdateEvent event) {
        if (!this.mc.thePlayer.onGround && this.isOverDaVoid()) {
            this.mc.thePlayer.motionY = 0.0;
            MovementUtil.strafe(0.33);
            if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.5, this.mc.thePlayer.posZ);
            }
        }
    }

    @Subscribe
    public void packetSendEvent(PacketSentEvent event) {
        if (!this.mc.thePlayer.onGround && this.isOverDaVoid()) {
            double diff = 0.0;
            diff += Math.abs(this.lastX - this.mc.thePlayer.posX);
            diff += Math.abs(this.lastY - this.mc.thePlayer.posY);
            diff += Math.abs(this.lastZ - this.mc.thePlayer.posZ);
            Packet<?> packet = event.getPacket();
            if (packet instanceof C03PacketPlayer) {
                event.setCanceled(true);
                Fly fly = ModulesManager.getModuleByClass(Fly.class);
                assert (fly != null);
                if (fly.enabledTicks % 20 == 0) {
                    this.mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer(this.mc.thePlayer.onGround));
                }
                if (diff > 5.0) {
                    this.lastX = this.mc.thePlayer.posX;
                    this.lastY = this.mc.thePlayer.posY;
                    this.lastZ = this.mc.thePlayer.posZ;
                    this.mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, this.mc.thePlayer.onGround));
                }
            }
        }
    }

    private boolean isOverDaVoid() {
        int start;
        boolean overDaVoid = true;
        Fly fly = ModulesManager.getModuleByClass(Fly.class);
        Scaffold scaffold = ModulesManager.getModuleByClass(Scaffold.class);
        if (scaffold.isToggled() && MovementUtil.isMoving() || fly.isToggled()) {
            return false;
        }
        for (int i2 = start = (int)this.mc.thePlayer.posY; i2 > 0; --i2) {
            Block block = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, (double)i2, this.mc.thePlayer.posZ)).getBlock();
            Material mat = block.getMaterial();
            if (!overDaVoid || mat == Material.air || mat == Material.water || mat == Material.lava) continue;
            overDaVoid = false;
        }
        return overDaVoid;
    }
}

