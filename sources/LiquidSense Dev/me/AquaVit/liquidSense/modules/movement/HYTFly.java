package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.ChatUtil;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.List;

import static net.ccbluex.liquidbounce.utils.MovementUtils.isMoving;
import static net.ccbluex.liquidbounce.utils.MovementUtils.strafe;

@ModuleInfo(name = "HYTFly", description = "Hyt Fly Bypass:/", category = ModuleCategory.MOVEMENT)
public class HYTFly extends Module {
    private List<Packet<?>> packets = new ArrayList<>();
    boolean doAsFly = false;
    double stage = 0;
    double timer = 0;
    double y = 0;
    boolean jump = false;

    private final FloatValue motionXZ = new FloatValue("MotionXZ", 2.5f, 0f, 10f);
    private final FloatValue motionY = new FloatValue("MotionY", 1.8f, 0.42f, 6f);
    private final FloatValue Bob = new FloatValue("Bob",0.5f,0f,2f);
    private final IntegerValue delay = new IntegerValue("Delay", 2, 0, 10);
    private final IntegerValue BindSlowSpeed = new IntegerValue("BindSlowSpeed", 7, 1, 10);
    private final FloatValue DownY = new FloatValue("DownY", 1.25f, 0.42f, 2f);
    private final FloatValue NormalDownY = new FloatValue("NormalDownY", 0.05f, 0.01f, 2f);
    Velocity vl = (Velocity)LiquidBounce.moduleManager.getModule(Velocity.class);
    HUD hd = (HUD)LiquidBounce.moduleManager.getModule("HUD");

    public void move() {
        mc.thePlayer.cameraYaw = Bob.get();
        if (mc.thePlayer.posY <= y && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY = motionY.get();
        } else {
            if(mc.gameSettings.keyBindForward.isKeyDown()){
                double dir = mc.thePlayer.rotationYaw / 180 * Math.PI;
                if (mc.thePlayer.motionY < 0 && !mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = 0 - NormalDownY.get();
                if (mc.thePlayer.motionY < 0 && mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = 0 - DownY.get();
                mc.thePlayer.motionX = -Math.sin(dir) * motionXZ.get();
                mc.thePlayer.motionZ = Math.cos(dir) * motionXZ.get();
            }else{
                if (mc.thePlayer.motionY < 0 && !mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = 0 - NormalDownY.get();
                if (mc.thePlayer.motionY < 0 && mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = 0 - DownY.get();
            }
        }
    }

    @Override
    public void onEnable() {
        y = mc.thePlayer.posY;
        timer = 999;
        packets.clear();
        LiquidBounce.moduleManager.getModule("Velocity").setState(false);
        if(hd.no.get()) {
            ChatUtil.sendClientMessage("Debug Disable Velocity", Notificationsn.Type.INFO);
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        doAsFly = false;
        packets.clear();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!doAsFly) timer++;
        if (timer > delay.get()) {
            timer = 0;
            doAsFly = true;
            stage = 0;
            move();
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.timer.timerSpeed = (BindSlowSpeed.get() / 10);
        }else{
            mc.timer.timerSpeed = 1f;
        }
        if (stage >= 1) {
            doAsFly = false;
            if (packets.size() > 0) {
                for (Packet<?> packet : packets) {
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                    packets.remove(packet);
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!doAsFly) return;
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) event.cancelEvent();
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition|| packet instanceof C02PacketUseEntity || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement) {
            event.cancelEvent();
            packets.add(packet);
            stage++;
        }
    }
}
