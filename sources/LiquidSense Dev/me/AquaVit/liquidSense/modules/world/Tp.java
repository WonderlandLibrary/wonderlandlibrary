package me.AquaVit.liquidSense.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.CustomVec3;
import net.ccbluex.liquidbounce.utils.PathfindingUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import static net.ccbluex.liquidbounce.utils.MovementUtils.getMaxFallDist;

@ModuleInfo(name = "Tp",description = "Allows you to teleport around.", category = ModuleCategory.WORLD,array = false)
public class Tp extends Module {
    public static float x;
    public static float y;
    public static float z;
    boolean tp;

    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;
        tp = false;
        ClientUtils.displayChatMessage("Wait For Server LagBack");
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = true;
        mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(0, (short)(-1), false));
        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                mc.thePlayer.posY + 0.17, mc.thePlayer.posZ, true));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
        mc.thePlayer.stepHeight = 0.0f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.stepHeight = 0.625f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (tp) {
            double offset = 0.060100000351667404;
            NetHandlerPlayClient netHandler = mc.getNetHandler();
            EntityPlayerSP player = mc.thePlayer;
            double x2 = player.posX;
            double y2 = player.posY;
            double z2 = player.posZ;
            int i = 0;
            while (i < getMaxFallDist() / 0.05510000046342611 + 1.0) {
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2 + 0.060100000351667404, z2, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2 + 5.000000237487257E-4, z2, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2 + 0.004999999888241291 + 6.01000003516674E-8, z2, false));
                ++i;
            }
            netHandler.addToSendQueue(new C03PacketPlayer(true));
            mc.getNetHandler().addToSendQueue(new C0CPacketInput(0.0f, 0.0f, true, true));
            double lastY = mc.thePlayer.posY, downY = 0;
            for (CustomVec3 vec3 : PathfindingUtils.computePath(
                    new CustomVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new CustomVec3(x, y, z))) {
                if (vec3.getY() < lastY) {
                    downY += (lastY - vec3.getY());
                }
                if (downY > 2.5) {
                    downY = 0;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(),
                            vec3.getY(), vec3.getZ(), true));
                } else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(),
                            vec3.getY(), vec3.getZ(), false));
                }
                lastY = vec3.getY();
            }
            //Teleported
            mc.thePlayer.setPosition(x, y, z);
            this.setState(false);
        }
    }


    @EventTarget
    public void onMove(MoveEvent e) {
        if (!tp) {
            e.cancelEvent();
        }
    }
    @EventTarget
    public final void onSendPacket(PacketEvent event) {
        if (!tp && event.getPacket() instanceof C03PacketPlayer) {
            event.cancelEvent();
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            if (!this.tp) {
                this.tp = true;
                ClientUtils.displayChatMessage("Start TelePort");
            }
        }
    }
}
