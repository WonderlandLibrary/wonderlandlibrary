package wtf.dawn.module.impl.combat;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import wtf.dawn.event.Event;
import wtf.dawn.event.events.EventUpdate;
import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;
import wtf.dawn.utils.Timer;

import java.util.ArrayList;
import java.util.stream.Collectors;


@ModuleInfo(getName = "Kill Aura", getDescription = "Automatically attacks players for you.", getCategory = Category.Ghost)
public class KillAura extends Module {

    private final Timer timer = new Timer();

    public KillAura() {
        setKeyCode(Keyboard.KEY_R);
    }


 //   private final NumberSetting cps = new NumberSetting("Vertical Value", 14, 30, 0.0, 0.1);

    private static ArrayList<EntityLivingBase> targets = new ArrayList<>();


    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        super.onDisable();
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "troll was disabled.", 1));
    }

    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "KillAura was enabled.", 1));
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            targets.add((EntityLivingBase) mc.theWorld.loadedEntityList);
            targets = (ArrayList<EntityLivingBase>) targets.stream().filter(target -> target.getDistanceToEntity(mc.thePlayer) < 5 && target != mc.thePlayer && !target.isDead).collect(Collectors.toList());
            for(Entity target : targets) {
                if(target == mc.thePlayer) {
                    continue;
                }

                mc.thePlayer.rotationYaw = (getRotations(target)[0]);
                mc.thePlayer.rotationPitch = (getRotations(target)[1]);
                if(timer.timeElapsed((long) (1000 / Math.random() * 17), true) && target.getDistanceToEntity(mc.thePlayer) < 3.2) {
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    mc.thePlayer.swingItem();
                }
            }
            super.onEvent(e);
        }
    }

    // ZEHUY INV ME BACK TO DAWN DC I GOT TERMED!! NEW ACC: Kellohylly#4980
    float cameraShakeSpeed = (float) Math.random();

    public float[] getRotations(Entity e) {
        double deltaX = e.posX - (e.posX - e.lastTickPosX) - mc.thePlayer.posX;
        double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2))));
        float deltaYaw = yaw - mc.thePlayer.rotationYaw;
        float deltaPitch = pitch - mc.thePlayer.rotationPitch;
        deltaYaw = MathHelper.wrapAngleTo180_float(deltaYaw);
        deltaPitch = MathHelper.wrapAngleTo180_float(deltaPitch);
        float adjustedRotationSpeed = 350;
        deltaYaw = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaYaw));
        deltaPitch = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaPitch));
        yaw = mc.thePlayer.rotationYaw + deltaYaw + this.cameraShakeSpeed;
        pitch = mc.thePlayer.rotationPitch + deltaPitch + this.cameraShakeSpeed;

        return new float[] { yaw, pitch };
    }

}