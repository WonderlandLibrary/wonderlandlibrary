/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.utils.MovementUtil;

public class ClanCraftFly {
    Minecraft mc = Minecraft.getMinecraft();
    double lastX;
    double lastY;
    double lastZ;

    public void onEnable() {
        this.mc = Minecraft.getMinecraft();
        this.lastX = this.mc.thePlayer.posX;
        this.lastY = this.mc.thePlayer.posY;
        this.lastZ = this.mc.thePlayer.posZ;
    }

    public void onDisable() {
    }

    @Subscribe
    public void onPacketSend(PacketSentEvent event) {
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

    @Subscribe
    public void onMotion(MotionUpdateEvent event) {
        this.mc.thePlayer.motionY = 0.0;
        MovementUtil.strafe(0.65);
        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1, this.mc.thePlayer.posZ);
        }
    }
}

