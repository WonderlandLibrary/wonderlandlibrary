/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.module.impl.combat.Killaura;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.misc.ChatUtil;
import me.tojatta.api.utilities.angle.Angle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AimAssist
extends Module {
    private EntityLivingBase target;
    private String WEAPON = "WEAPON";
    private String RANGE = "RANGE";
    private String HORIZONTAL = "SPEED-H";
    private String VERTICAL = "SPEED-V";
    private String FOVYAW = "FOVYAW";
    private String FOVPITCH = "FOVPITCH";
    private String X = "RANDOM-XZ";
    private String Y = "RANDOM-Y";
    private Angle lookAngles = new Angle(Float.valueOf(0.0f), Float.valueOf(0.0f));

    public AimAssist(ModuleData data) {
        super(data);
        this.settings.put(this.WEAPON, new Setting<Boolean>(this.WEAPON, true, "Checks if you have a sword in hand."));
        this.settings.put(this.X, new Setting<Integer>(this.X, 0, "Randomization on XZ axis.", 0.01, 0.0, 0.2));
        this.settings.put(this.Y, new Setting<Integer>(this.Y, 0, "Randomization on Y axis.", 0.01, 0.0, 0.2));
        this.settings.put(this.RANGE, new Setting<Double>(this.RANGE, 4.5, "The distance in which an entity is valid to attack.", 0.1, 1.0, 10.0));
        this.settings.put(this.HORIZONTAL, new Setting<Integer>(this.HORIZONTAL, 20, "Horizontal speed.", 0.25, 0.0, 10.0));
        this.settings.put(this.VERTICAL, new Setting<Integer>(this.VERTICAL, 15, "Vertical speed.", 0.25, 0.0, 10.0));
        this.settings.put(this.FOVYAW, new Setting<Integer>(this.FOVYAW, 45, "Yaw FOV check.", 1.0, 5.0, 50.0));
        this.settings.put(this.FOVPITCH, new Setting<Integer>(this.FOVPITCH, 25, "Vertical FOV check.", 1.0, 5.0, 50.0));
    }

    private int randomNumber() {
        return -5 + (int)(Math.random() * 11.0);
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        if (event instanceof EventMotion) {
            EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                this.target = this.getBestEntity();
            } else if (em.isPost() && AimAssist.mc.currentScreen == null) {
                if (AimAssist.mc.thePlayer.getHeldItem() == null || AimAssist.mc.thePlayer.getHeldItem().getItem() == null) {
                    return;
                }
                Item heldItem = AimAssist.mc.thePlayer.getHeldItem().getItem();
                if (((Boolean)((Setting)this.settings.get(this.WEAPON)).getValue()).booleanValue() && heldItem != null && !(heldItem instanceof ItemSword)) {
                    return;
                }
                if (this.target != null && !FriendManager.isFriend(this.target.getName()) && AimAssist.mc.thePlayer.isEntityAlive()) {
                    this.stepAngle();
                } else {
                    this.lookAngles.setYaw(Float.valueOf(AimAssist.mc.thePlayer.rotationYaw));
                    this.lookAngles.setPitch(Float.valueOf(AimAssist.mc.thePlayer.rotationPitch));
                }
            }
        }
    }

    private void stepAngle() {
        float yawFactor = ((Number)((Setting)this.settings.get(this.HORIZONTAL)).getValue()).floatValue();
        float pitchFactor = ((Number)((Setting)this.settings.get(this.VERTICAL)).getValue()).floatValue();
        double xz = ((Number)((Setting)this.settings.get(this.X)).getValue()).doubleValue();
        double y = ((Number)((Setting)this.settings.get(this.Y)).getValue()).doubleValue();
        float targetYaw = RotationUtils.getYawChange(this.target.posX + (double)this.randomNumber() * xz, this.target.posZ + (double)this.randomNumber() * xz);
        AimAssist.mc.thePlayer.rotationYaw = targetYaw > 0.0f && targetYaw > yawFactor ? (AimAssist.mc.thePlayer.rotationYaw += yawFactor) : (targetYaw < 0.0f && targetYaw < - yawFactor ? (AimAssist.mc.thePlayer.rotationYaw -= yawFactor) : (AimAssist.mc.thePlayer.rotationYaw += targetYaw));
        float targetPitch = RotationUtils.getPitchChange(this.target, this.target.posY + (double)this.randomNumber() * y);
        AimAssist.mc.thePlayer.rotationPitch = targetPitch > 0.0f && targetPitch > pitchFactor ? (AimAssist.mc.thePlayer.rotationPitch += pitchFactor) : (targetPitch < 0.0f && targetPitch < - pitchFactor ? (AimAssist.mc.thePlayer.rotationPitch -= pitchFactor) : (AimAssist.mc.thePlayer.rotationPitch += targetPitch));
    }

    private EntityLivingBase getBestEntity() {
        CopyOnWriteArrayList<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
        for (Object o : AimAssist.mc.theWorld.getLoadedEntityList()) {
            EntityLivingBase ent;
            if (!(o instanceof EntityLivingBase) || !(ent = (EntityLivingBase)o).isEntityAlive() || !(ent instanceof EntityPlayer) || ent.getDistanceToEntity(AimAssist.mc.thePlayer) >= ((Number)((Setting)this.settings.get(this.RANGE)).getValue()).floatValue() || !this.fovCheck(ent)) continue;
            if (ent == Killaura.vip) {
                return ent;
            }
            loaded.add(ent);
        }
        if (loaded.isEmpty()) {
            return null;
        }
        try {
            loaded.sort((o1, o2) -> {
                float[] rot1 = RotationUtils.getRotations(o1);
                float[] rot2 = RotationUtils.getRotations(o2);
                return (int)(RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rot1[0]) + RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rot1[1]) - (RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rot2[0]) + RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rot2[1])));
            }
            );
        }
        catch (Exception e) {
            ChatUtil.printChat("Exception with TM: " + e.getMessage());
        }
        return (EntityLivingBase)loaded.get(0);
    }

    private boolean fovCheck(EntityLivingBase ent) {
        float[] rotations = RotationUtils.getRotations(ent);
        float dist = AimAssist.mc.thePlayer.getDistanceToEntity(ent);
        if (dist == 0.0f) {
            dist = 1.0f;
        }
        float yawDist = RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationYaw, rotations[0]);
        float pitchDist = RotationUtils.getDistanceBetweenAngles(AimAssist.mc.thePlayer.rotationPitch, rotations[1]);
        float fovYaw = ((Number)((Setting)this.settings.get(this.FOVYAW)).getValue()).floatValue() * 3.0f / dist;
        float fovPitch = ((Number)((Setting)this.settings.get(this.FOVPITCH)).getValue()).floatValue() * 3.0f / dist;
        return yawDist < fovYaw && pitchDist < fovPitch;
    }
}

