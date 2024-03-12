package de.theBest.MythicX.modules.combat;


import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventPacket;
import de.theBest.MythicX.events.EventPreMotion;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.events.Movefix;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.utils.RotationUtil;
import de.theBest.MythicX.utils.TimeUtils;

import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.awt.*;

public class Killaura extends Module {
    public Killaura() {
        super("Killaura", Type.Combat, 0, Category.COMBAT, Color.green, "Attack Entities");
    }

    public static EntityLivingBase target;
    public TimeUtils timer = new TimeUtils();
    public static boolean fakeBlock;
    public static boolean keepSprint;

    @EventTarget
    public void onRotate(EventPreMotion event) {
        final float[] rotations = RotationUtil.Intave(mc.thePlayer, target);
        float[] rota = RotationUtil.Intave(mc.thePlayer, (EntityLivingBase)target);
        event.setPitch(RotationUtil.pitch);
        event.setYaw(RotationUtil.yaw);
        RotationUtil.setYaw(rotations[0], (float) MythicX.setmgr.getSettingByName("ScaffoldRotationSpeed").getValDouble());
        RotationUtil.setPitch(rotations[1], (float) MythicX.setmgr.getSettingByName("ScaffoldRotationSpeed").getValDouble());
    }

    @EventTarget
    public void onmove(Movefix e) {
        if (target != null && MythicX.setmgr.getSettingByName("AuraMoveFix").getValBoolean()) {
            e.setYaw(RotationUtil.yaw);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        fakeBlock = MythicX.setmgr.getSettingByName("FakeBlock").getValBoolean();
        keepSprint = MythicX.setmgr.getSettingByName("KeepSprint").getValBoolean();

        target = searchTargets();
        if (target != null) {
            final float maxCPS = (float) MythicX.setmgr.getSettingByName("MaxCPS").getValDouble();
            final float minCPS = (float) MythicX.setmgr.getSettingByName("MinCPS").getValDouble();
            final float cps = (float) (Math.random() * (maxCPS - minCPS) + minCPS);
            if (timer.hasReached((long) (1000 / cps))) {
                //mc.thePlayer.swingItem();
                //mc.playerController.attackEntity(mc.thePlayer, target);
                mc.clickMouse();
                timer.reset();
            }

            }
            if(MythicX.setmgr.getSettingByName("RealBlock").getValBoolean() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && target != null){
                this.block(false, true);
            }
        }

        @EventTarget
        public void onPacket(EventPacket eventPacket){
            if(MythicX.setmgr.getSettingByName("Wtap").getValBoolean() &&target != null && target.hurtTime == 9){
                mc.thePlayer.sprintReset = true;
            }
        }


    @Override
    public void onDisable() {
        target = null;
    }

    @Override
    public void onEnable() {

    }

    public EntityLivingBase searchTargets() {
        final double range = MythicX.setmgr.getSettingByName("Range").getValDouble();
        EntityLivingBase closestPlayer = null;
        double closestDist = Double.MAX_VALUE;

        for (final Entity o : mc.theWorld.loadedEntityList) {
            if (!o.getName().equals(mc.thePlayer.getName()) && o instanceof EntityPlayer) {
                final double dist = mc.thePlayer.getDistanceToEntity(o);
                if (dist < range) {
                    closestDist = Math.min(closestDist, dist);
                    if(closestDist == dist)
                        closestPlayer = (EntityLivingBase) o;
                }
            }
        }
        return closestPlayer;
    }

    private void block(final boolean check, final boolean interact) {
        if (!mc.thePlayer.isBlocking() || !check) {
            if (target != null && mc.objectMouseOver.entityHit == target) {
                mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
        }
    }



    @Override
    public void setup() {
        MythicX.setmgr.rSetting(new Setting("Range", this, 3.1, 3, 6, false));
        MythicX.setmgr.rSetting(new Setting("MaxCPS", this, 20, 1, 20, true));
        MythicX.setmgr.rSetting(new Setting("MinCPS", this, 15, 1, 20, true));
        MythicX.setmgr.rSetting(new Setting("AuraRotationSpeed", this, 50,1,360,true));
        MythicX.setmgr.rSetting(new Setting("KeepSprint", this, false));
        MythicX.setmgr.rSetting(new Setting("FakeBlock", this, false));
        MythicX.setmgr.rSetting(new Setting("RealBlock", this, false));
        MythicX.setmgr.rSetting(new Setting("JumpFix", this, true));
        MythicX.setmgr.rSetting(new Setting("AuraMoveFix", this, true));
        MythicX.setmgr.rSetting(new Setting("AuraRandom", this, true));
        MythicX.setmgr.rSetting(new Setting("AuraClampPitch", this, true));
        MythicX.setmgr.rSetting(new Setting("AuraMouseSensiFix", this, true));
        MythicX.setmgr.rSetting(new Setting("Wtap", this, true));
    }
}
