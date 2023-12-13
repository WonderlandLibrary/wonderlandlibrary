package com.ohare.client.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ohare.client.Client;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

public class AutoBackStab extends Module {
    public EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList();
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 100, 1, 2000, 1);
    private NumberValue<Float> range = new NumberValue("Range", 7.0f, 1.0f, 7.0f, 0.1f);
    private BooleanValue teleportshid = new BooleanValue("Teleport", true);
    private BooleanValue invisibles = new BooleanValue("Invisibles", true);
    private EnumValue<SortMode> sortingmode = new EnumValue("SortMode", SortMode.FOV);
    private TimerUtil itemTimer = new TimerUtil();
    private TimerUtil teleportTimer = new TimerUtil();
    private TimerUtil reteleportTimer = new TimerUtil();
    private boolean shouldTeleport, teleported, teleportedBack;
    private double oldX, oldY, oldZ;

    public AutoBackStab() {
        super("AutoBackStab", Category.COMBAT, 0xDE9B1F);
        setRenderlabel("Auto Back Stab");
        setDescription("Automatically replaces gold swords");
        addValues(delay, range, teleportshid, invisibles, sortingmode);
    }

    @Override
    public void onDisable() {
        shouldTeleport = teleported = teleportedBack = false;
        oldX = oldY = oldZ = 0;
        itemTimer.reset();
        teleportTimer.reset();
        reteleportTimer.reset();
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
            if (target != null) {
            double x = (target.lastTickPosX + (target.posX - target.lastTickPosX) * event.getPartialTicks());
            double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.getPartialTicks();
            double z = (target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.getPartialTicks());
			RenderUtil.drawEntityESP(x + (1.5 * Math.sin(Math.toRadians(target.rotationYaw))) - RenderManager.renderPosX, y - RenderManager.renderPosY + 0.55, z + (1.5 * -Math.cos(Math.toRadians(target.rotationYaw))) - RenderManager.renderPosZ, 0.4, 0.5, new Color(0xE33726),true);
            }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        target = getBestTarget(event.getYaw());
        if (event.isPre()) {
            if (teleportshid.isEnabled() && Client.INSTANCE.getModuleManager().getModuleClass(KillAura.class).isEnabled()) {
                if (target != null) {
                    shouldTeleport = mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() == Items.golden_sword;
                } else {
                    shouldTeleport = false;
                }
            } else {
                shouldTeleport = false;
            }
        } else {
            if (mc.thePlayer.getHeldItem() == null && itemTimer.reach(delay.getValue())) {
                for (int i = 9; i < 45; ++i) {
                    if (i == mc.thePlayer.inventory.currentItem + 36) {
                        continue;
                    }
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (is == null || is.getItem() != Items.golden_sword) {
                        continue;
                    }
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
                    itemTimer.reset();
                }
            }
        }
        if (shouldTeleport && !teleported && !teleportedBack) {
            double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks;
            double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks;
            double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks;
            oldX = mc.thePlayer.posX;
            oldY = mc.thePlayer.posY;
            oldZ = mc.thePlayer.posZ;
            mc.thePlayer.setPosition(x + (1.5 * Math.sin(Math.toRadians(target.rotationYaw))), y, z + (1.5 * -Math.cos(Math.toRadians(target.rotationYaw))));
            teleported = true;
            reteleportTimer.reset();
        }
        if (teleported && !teleportedBack) {
            if (reteleportTimer.reach(1000)) {
                mc.thePlayer.setPosition(oldX, oldY, oldZ);
                teleported = false;
                teleportedBack = false;
                shouldTeleport = false;
                reteleportTimer.reset();
            }
            if (mc.thePlayer.getHeldItem() == null) {
                mc.thePlayer.setPosition(oldX, oldY, oldZ);
                teleportedBack = true;
                teleported = false;
                teleportTimer.reset();
            }
        }
        if (teleportedBack && teleportTimer.reach(2000)) {
            teleportedBack = false;
            oldX = oldY = oldZ = 0;
            teleportTimer.reset();
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        final double d = range.getValue();
        return !AntiBot.getBots().contains(entity) && entity != null && mc.thePlayer != entity && entity instanceof EntityPlayer && entity.getDistanceSqToEntity(mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue()) && !Client.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

    private EntityLivingBase getBestTarget(float yaw) {
        targets.clear();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (isValid(ent)) {
                    targets.add(ent);
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        sortTargets(yaw);
        return targets.get(0);
    }

    private void sortTargets(float yaw) {
        switch (sortingmode.getValue()) {
            case DISTANCE:
                targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceSqToEntity));
                break;
            case HEALTH:
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case FOV:
                targets.sort(Comparator.comparingDouble(this::yawDist));
                break;
            case CYCLE:
                targets.sort(Comparator.comparingDouble(player -> yawDistCycle(player, yaw)));
                break;
            case ARMOR:
                targets.sort(Comparator.comparingDouble(this::getArmorVal));
                break;
        }
    }

    private double getArmorVal(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) ent;
            double armorstrength = 0;
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    armorstrength += getArmorStrength(stack);
                }
            }
            return armorstrength;
        }
        return 0;
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private double yawDistCycle(EntityLivingBase e, float yaw) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(yaw - Math.atan2(difference.zCoord, difference.xCoord)) % 90.0f;
        return d;
    }

    private enum SortMode {
        FOV, DISTANCE, HEALTH, CYCLE, ARMOR
    }

    private double yawDist(EntityLivingBase e) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f)) % 360.0f;
        return (d > 180.0f) ? (360.0f - d) : d;
    }

}