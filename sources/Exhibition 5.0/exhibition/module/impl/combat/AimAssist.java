// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import exhibition.util.misc.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import exhibition.util.RotationUtils;
import exhibition.event.RegisterEvent;
import net.minecraft.item.Item;
import exhibition.management.friend.FriendManager;
import net.minecraft.item.ItemSword;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.EntityLivingBase;
import exhibition.module.Module;

public class AimAssist extends Module
{
    private EntityLivingBase target;
    private String WEAPON;
    private String RANGE;
    private String HORIZONTAL;
    private String VERTICAL;
    private String FOVYAW;
    private String FOVPITCH;
    private String X;
    private String Y;
    
    public AimAssist(final ModuleData data) {
        super(data);
        this.WEAPON = "WEAPON";
        this.RANGE = "RANGE";
        this.HORIZONTAL = "SPEED-H";
        this.VERTICAL = "SPEED-V";
        this.FOVYAW = "FOVYAW";
        this.FOVPITCH = "FOVPITCH";
        this.X = "RANDOM-XZ";
        this.Y = "RANDOM-Y";
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.WEAPON, new Setting<Boolean>(this.WEAPON, true, "Checks if you have a sword in hand."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.X, new Setting<Integer>(this.X, 0, "Randomization on XZ axis.", 0.1, 0.0, 1.5));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.Y, new Setting<Integer>(this.Y, 0, "Randomization on Y axis.", 0.1, 0.0, 1.5));
        ((HashMap<String, Setting<Double>>)this.settings).put(this.RANGE, new Setting<Double>(this.RANGE, 4.5, "The distance in which an entity is valid to attack.", 0.1, 1.0, 10.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.HORIZONTAL, new Setting<Integer>(this.HORIZONTAL, 20, "Horizontal speed.", 0.25, 0.0, 10.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.VERTICAL, new Setting<Integer>(this.VERTICAL, 15, "Vertical speed.", 0.25, 0.0, 10.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.FOVYAW, new Setting<Integer>(this.FOVYAW, 45, "Yaw FOV check.", 1.0, 5.0, 50.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.FOVPITCH, new Setting<Integer>(this.FOVPITCH, 25, "Vertical FOV check.", 1.0, 5.0, 50.0));
    }
    
    private int randomNumber() {
        return -5 + (int)(Math.random() * 11.0);
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                this.target = this.getBestEntity();
            }
            else if (em.isPost() && AimAssist.mc.currentScreen == null) {
                if (AimAssist.mc.thePlayer.getHeldItem() == null || AimAssist.mc.thePlayer.getHeldItem().getItem() == null) {
                    return;
                }
                final Item heldItem = AimAssist.mc.thePlayer.getHeldItem().getItem();
                if (((HashMap<K, Setting<Boolean>>)this.settings).get(this.WEAPON).getValue() && heldItem != null && !(heldItem instanceof ItemSword)) {
                    return;
                }
                if (this.target != null && !FriendManager.isFriend(this.target.getName()) && AimAssist.mc.thePlayer.isEntityAlive()) {
                    this.stepAngle();
                }
            }
        }
    }
    
    private void stepAngle() {
        final float yawFactor = ((HashMap<K, Setting<Number>>)this.settings).get(this.HORIZONTAL).getValue().floatValue();
        final float pitchFactor = ((HashMap<K, Setting<Number>>)this.settings).get(this.VERTICAL).getValue().floatValue();
        final double xz = ((HashMap<K, Setting<Number>>)this.settings).get(this.X).getValue().doubleValue();
        final double y = ((HashMap<K, Setting<Number>>)this.settings).get(this.Y).getValue().doubleValue();
        final float targetYaw = RotationUtils.getYawChange(this.target.posX + this.randomNumber() * xz, this.target.posZ + this.randomNumber() * xz);
        if (targetYaw > 0.0f && targetYaw > yawFactor) {
            final EntityPlayerSP thePlayer = AimAssist.mc.thePlayer;
            thePlayer.rotationYaw += yawFactor;
        }
        else if (targetYaw < 0.0f && targetYaw < -yawFactor) {
            final EntityPlayerSP thePlayer2 = AimAssist.mc.thePlayer;
            thePlayer2.rotationYaw -= yawFactor;
        }
        else {
            final EntityPlayerSP thePlayer3 = AimAssist.mc.thePlayer;
            thePlayer3.rotationYaw += targetYaw;
        }
        final float targetPitch = RotationUtils.getPitchChange(this.target, this.target.posY + this.randomNumber() * y);
        if (targetPitch > 0.0f && targetPitch > pitchFactor) {
            final EntityPlayerSP thePlayer4 = AimAssist.mc.thePlayer;
            thePlayer4.rotationPitch += pitchFactor;
        }
        else if (targetPitch < 0.0f && targetPitch < -pitchFactor) {
            final EntityPlayerSP thePlayer5 = AimAssist.mc.thePlayer;
            thePlayer5.rotationPitch -= pitchFactor;
        }
        else {
            final EntityPlayerSP thePlayer6 = AimAssist.mc.thePlayer;
            thePlayer6.rotationPitch += targetPitch;
        }
    }
    
    private EntityLivingBase getBestEntity() {
        final List<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
        for (final Object o3 : AimAssist.mc.theWorld.getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase)o3;
                if (!ent.isEntityAlive() || !(ent instanceof EntityPlayer) || ent.getDistanceToEntity(AimAssist.mc.thePlayer) >= ((HashMap<K, Setting<Number>>)this.settings).get(this.RANGE).getValue().floatValue() || !this.fovCheck(ent)) {
                    continue;
                }
                if (ent == Killaura.vip) {
                    return ent;
                }
                loaded.add(ent);
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        try {
            final float[] rot1;
            final float[] rot2;
            loaded.sort((o1, o2) -> {
                rot1 = RotationUtils.getRotations(o1);
                rot2 = RotationUtils.getRotations(o2);
                return (int)(RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rot1[0]) + RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rot1[1]) - (RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rot2[0]) + RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rot2[1])));
            });
        }
        catch (Exception e) {
            ChatUtil.printChat("Exception with TM: " + e.getMessage());
        }
        return loaded.get(0);
    }
    
    private boolean fovCheck(final EntityLivingBase ent) {
        final float[] rotations = RotationUtils.getRotations(ent);
        float dist = AimAssist.mc.thePlayer.getDistanceToEntity(ent);
        if (dist == 0.0f) {
            dist = 1.0f;
        }
        final float yawDist = RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rotations[0]);
        final float pitchDist = RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rotations[1]);
        final float fovYaw = ((HashMap<K, Setting<Number>>)this.settings).get(this.FOVYAW).getValue().floatValue() * 3.0f / dist;
        final float fovPitch = ((HashMap<K, Setting<Number>>)this.settings).get(this.FOVPITCH).getValue().floatValue() * 3.0f / dist;
        return yawDist < fovYaw && pitchDist < fovPitch;
    }
}
