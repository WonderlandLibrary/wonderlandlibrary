package me.AquaVit.liquidSense.modules.movement;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.ChatUtil;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;

@ModuleInfo(name = "LastestHypixelFly",description = "Allows you to Fly.", category = ModuleCategory.MOVEMENT)
public class LastestHypixelFly extends Module {
    boolean tp;
    private final FloatValue motionSpeedValue = new FloatValue("Speed", 5F, 0F, 5F);
    private final MSTimer s08 = new MSTimer();
    HUD hd = (HUD)LiquidBounce.moduleManager.getModule("HUD");
    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;
        tp = false;
        s08.reset();
        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidBounce.CLIENT_NAME+ "§8] §3"+ "Now Disabler!"));
        if(hd.no.get()){
            ChatUtil.sendClientMessage("Now disabler!", Notificationsn.Type.INFO);
        }

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
        s08.reset();
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = false;
        mc.thePlayer.onGround = true;
        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.stepHeight = 0.625f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event){
        mc.thePlayer.onGround = false;
        if (mc.thePlayer.movementInput.jump) {
            mc.thePlayer.motionY = 1.5;
        } else if (mc.thePlayer.movementInput.sneak) {
            mc.thePlayer.motionY = -1.5;
        } else {
            mc.thePlayer.motionY = 0;
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!tp) {
            event.cancelEvent();
        }
        if(tp){
            double speed = motionSpeedValue.get();
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            float yawm = mc.thePlayer.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                event.setX(0.0D);
                event.setZ(0.0D);
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yawm += (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yawm += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1;
                    } else if (forward < 0.0D) {
                        forward = -1;
                    }
                }
                event.setX(forward * speed * Math.cos(Math.toRadians(yawm + 90.0F))
                        + strafe * speed * Math.sin(Math.toRadians(yawm + 90.0F)));
                event.setZ(forward * speed * Math.sin(Math.toRadians(yawm + 90.0F))
                        - strafe * speed * Math.cos(Math.toRadians(yawm + 90.0F)));
            }
        }
    }
    @EventTarget
    public final void onSendPacket(PacketEvent event) {
        if (!tp && event.getPacket() instanceof C03PacketPlayer) {
            event.cancelEvent();
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            if (!this.tp) {
                s08.reset();
                this.tp = true;
                mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidBounce.CLIENT_NAME+ "§8] §3"+ "Fly Now!"));
                if(hd.no.get()) {
                    ChatUtil.sendClientMessage("Now u can fly!", Notificationsn.Type.SUCCESS);
                }
            }
        }
        if (this.tp){
            if(s08.hasTimePassed(1500)){
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    ClientUtils.displayChatMessage("§8[§c§lDisablerHypixel-§a§lFly§8] §cSetback detected.");
                    s08.reset();
                    toggle();
                }
            }
        }
    }

}
