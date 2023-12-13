package koks.manager.module.impl.combat;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.*;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author kroko
 * @created on 22.11.2020 : 06:50
 */

@ModuleInfo(name = "AimBot", category = Module.Category.COMBAT, description = "You aim automatically at players")
public class AimBot extends Module {

    float curYaw, curPitch;
    Entity finalEntity;

    public Setting lockView = new Setting("LockView", false, this);
    public Setting moveFix = new Setting("MoveFix", true, this);

    public Setting smooth = new Setting("Smooth", false, this);

    public Setting player = new Setting("Player", true, this);
    public Setting armorStands = new Setting("ArmorStands", false, this);
    public Setting animals = new Setting("Animals", false, this);
    public Setting villager = new Setting("Villager", false, this);
    public Setting mobs = new Setting("Mobs", false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventWalk) {
            if (lockView.isToggled() && finalEntity != null) {
                getPlayer().rotationYaw = curYaw;
                getPlayer().rotationPitch = curPitch;
            }
        }

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {

                if (finalEntity != null) {
                    float[] rots = rotationUtil.faceEntity(finalEntity, true, curYaw, curPitch, smooth.isToggled(), 0.3F, 0.1F, 0.4F);
                    ((EventMotion) event).setYaw(rots[0]);
                    ((EventMotion) event).setPitch(rots[1]);
                    curYaw = ((EventMotion) event).getYaw();
                    curPitch = ((EventMotion) event).getPitch();
                }
            }
        }

        if (event instanceof EventJump) {
            if (finalEntity != null) {
                if (moveFix.isToggled()) {
                    ((EventJump) event).setYaw(curYaw);
                }
            }
        }

        if (event instanceof EventMoveFlying) {
            if (finalEntity != null) {
                if (moveFix.isToggled()) {
                    ((EventMoveFlying) event).setYaw(curYaw);
                }
            }
        }

        if (event instanceof EventUpdate) {
            if (finalEntity == null) {
                for (Entity entity : getPlayer().worldObj.loadedEntityList) {
                    if (isValid(entity)) {
                        finalEntity = entity;
                    }
                }
            }

            if (!isValid(finalEntity) && finalEntity != null) {
                finalEntity = null;
            }
        }
    }

    public boolean isValid(Entity entity) {
        if (entity == mc.thePlayer) return false;
        if (entity == null) return false;
        if (entity.isInvisible()) return false;
        if (entity.isDead) return false;
        if (!getWorld().loadedEntityList.contains(entity)) return false;
        if (getPlayer().getDistanceToEntity(entity) > getPlayerController().getBlockReachDistance()) return false;
        if (!(entity instanceof EntityLivingBase)) return false;
        if (entity instanceof EntityPlayer && !player.isToggled()) return false;
        if (entity instanceof EntityArmorStand && !armorStands.isToggled()) return false;
        if (entity instanceof EntityAnimal && !animals.isToggled()) return false;
        if (entity instanceof EntityMob && !mobs.isToggled()) return false;
        return true;
    }

    @Override
    public void onEnable() {
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
    }

    @Override
    public void onDisable() {

    }
}
