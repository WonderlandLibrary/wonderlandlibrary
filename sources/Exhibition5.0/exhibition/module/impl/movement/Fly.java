// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMove;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class Fly extends Module
{
    public static String SPEED;
    public static final String KICK = "KICK";
    public static final String GLIDE = "GLIDE";
    Timer kickTimer;
    private double flyHeight;
    private double startY;
    
    public Fly(final ModuleData data) {
        super(data);
        this.kickTimer = new Timer();
        ((HashMap<String, Setting<Float>>)this.settings).put(Fly.SPEED, new Setting<Float>(Fly.SPEED, 2.0f, "Movement speed."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("KICK", new Setting<Boolean>("KICK", false, "Prevents kicking from vanilla servers."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("GLIDE", new Setting<Boolean>("GLIDE", true, "Glides the player down, prevents Y increase."));
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public void updateFlyHeight() {
        double h = 1.0;
        final AxisAlignedBB box = Fly.mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
        this.flyHeight = 0.0;
        while (this.flyHeight < Fly.mc.thePlayer.posY) {
            final AxisAlignedBB nextBox = box.offset(0.0, -this.flyHeight, 0.0);
            if (Fly.mc.theWorld.checkBlockCollision(nextBox)) {
                if (h < 0.0625) {
                    break;
                }
                this.flyHeight -= h;
                h /= 2.0;
            }
            this.flyHeight += h;
        }
    }
    
    public void goToGround() {
        if (this.flyHeight > 300.0) {
            return;
        }
        final double minY = Fly.mc.thePlayer.posY - this.flyHeight;
        if (minY <= 0.0) {
            return;
        }
        double y = Fly.mc.thePlayer.posY;
        while (y > minY) {
            y -= 8.0;
            if (y < minY) {
                y = minY;
            }
            final C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, y, Fly.mc.thePlayer.posZ, true);
            Fly.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        y = minY;
        while (y < Fly.mc.thePlayer.posY) {
            y += 8.0;
            if (y > Fly.mc.thePlayer.posY) {
                y = Fly.mc.thePlayer.posY;
            }
            final C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, y, Fly.mc.thePlayer.posZ, true);
            Fly.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }
    
    @Override
    public void onEnable() {
        Fly.mc.timer.timerSpeed = 1.0f;
        this.startY = Fly.mc.thePlayer.posY;
    }
    
    @RegisterEvent(events = { EventMove.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            final double speed = Math.max(((HashMap<K, Setting<Number>>)this.settings).get(Fly.SPEED).getValue().floatValue() / 10.0f, getBaseMoveSpeed());
            if (em.isPre()) {
                if (!((HashMap<K, Setting<Boolean>>)this.settings).get("GLIDE").getValue()) {
                    if (Fly.mc.thePlayer.movementInput.jump) {
                        Fly.mc.thePlayer.motionY = speed;
                    }
                    else if (Fly.mc.thePlayer.movementInput.sneak) {
                        Fly.mc.thePlayer.motionY = -speed;
                    }
                    else {
                        Fly.mc.thePlayer.motionY = 0.0;
                    }
                }
                else {
                    final boolean shouldBlock = Fly.mc.thePlayer.posY + 0.1 >= this.startY && Fly.mc.gameSettings.keyBindJump.getIsKeyPressed() && ((HashMap<K, Setting<Boolean>>)this.settings).get("GLIDE").getValue();
                    if (Fly.mc.thePlayer.isSneaking()) {
                        Fly.mc.thePlayer.motionY = -0.4000000059604645;
                    }
                    else if (Fly.mc.gameSettings.keyBindJump.getIsKeyPressed() && !shouldBlock) {
                        Fly.mc.thePlayer.motionY = 0.4000000059604645;
                    }
                    else {
                        Fly.mc.thePlayer.motionY = -0.05;
                    }
                }
                if (((HashMap<K, Setting<Boolean>>)this.settings).get("KICK").getValue()) {
                    this.updateFlyHeight();
                    Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    if ((this.flyHeight <= 290.0 && this.kickTimer.delay(500.0f)) || (this.flyHeight > 290.0 && this.kickTimer.delay(100.0f))) {
                        this.goToGround();
                        this.kickTimer.reset();
                    }
                }
            }
        }
        if (event instanceof EventMove) {
            final EventMove em2 = (EventMove)event;
            final double speed = Math.max(((HashMap<K, Setting<Number>>)this.settings).get(Fly.SPEED).getValue().floatValue() / 10.0f, getBaseMoveSpeed());
            double forward = Fly.mc.thePlayer.movementInput.moveForward;
            double strafe = Fly.mc.thePlayer.movementInput.moveStrafe;
            float yaw = Fly.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                em2.setX(0.0);
                em2.setZ(0.0);
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        strafe = 1.0;
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                em2.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                em2.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
            }
        }
    }
    
    static {
        Fly.SPEED = "SPEED";
    }
}
