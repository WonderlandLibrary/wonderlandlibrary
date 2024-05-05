package astronaut.modules.combat;


import astronaut.Duckware;
import astronaut.events.EventPreMotion;
import astronaut.events.EventUpdate;
import astronaut.events.Movefix;
import astronaut.modules.Category;
import astronaut.modules.Module;
import astronaut.utils.RotationUtil;
import astronaut.utils.TimeUtils;

import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

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
        final float[] rotations = RotationUtil.basicRotation(mc.thePlayer, target);
        event.setPitch(RotationUtil.pitch);
        event.setYaw(RotationUtil.yaw);
        RotationUtil.setYaw(rotations[0], 40);
        RotationUtil.setPitch(rotations[1], 40);
    }

    @EventTarget
    public void onmove(Movefix e) {
        if (target != null) {
            e.setYaw(RotationUtil.yaw);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        fakeBlock = Duckware.setmgr.getSettingByName("FakeBlock").getValBoolean();
        keepSprint = Duckware.setmgr.getSettingByName("KeepSprint").getValBoolean();

        target = searchTargets();
        if (target != null) {
            final float maxCPS = (float) Duckware.setmgr.getSettingByName("MaxCPS").getValDouble();
            final float minCPS = (float) Duckware.setmgr.getSettingByName("MinCPS").getValDouble();
            final float cps = (float) (Math.random() * (maxCPS - minCPS) + minCPS);
            if (timer.hasReached((long) (180 / cps))) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);
                timer.reset();
            }
            if(Duckware.setmgr.getSettingByName("RealBlock").getValBoolean() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword){
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
            }
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
        final double range = Duckware.setmgr.getSettingByName("Range").getValDouble();
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


    @Override
    public void setup() {
        Duckware.setmgr.rSetting(new Setting("Range", this, 3.1, 3, 6, false));
        Duckware.setmgr.rSetting(new Setting("MaxCPS", this, 20, 1, 20, true));
        Duckware.setmgr.rSetting(new Setting("MinCPS", this, 15, 1, 20, true));
        Duckware.setmgr.rSetting(new Setting("KeepSprint", this, false));
        Duckware.setmgr.rSetting(new Setting("FakeBlock", this, false));
        Duckware.setmgr.rSetting(new Setting("RealBlock", this, false));
    }
}
