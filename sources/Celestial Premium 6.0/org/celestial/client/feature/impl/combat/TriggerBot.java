/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.AntiBot;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class TriggerBot
extends Feature {
    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting team;
    public static BooleanSetting onlyCrit;
    public static BooleanSetting spaceOnly;
    public static BooleanSetting adaptiveCrits;
    public static NumberSetting adaptiveCritsHealth;
    public static NumberSetting critFallDistance;

    public TriggerBot() {
        super("TriggerBot", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u043d\u043e\u0441\u0438\u0442 \u0443\u0434\u0430\u0440 \u043f\u0440\u0438 \u043d\u0430\u0432\u043e\u0434\u043a\u0435 \u043d\u0430 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u044c", Type.Combat);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        team = new BooleanSetting("Team Check", false, () -> true);
        range = new NumberSetting("Trigger Range", 4.0f, 1.0f, 6.0f, 0.1f, () -> true);
        onlyCrit = new BooleanSetting("Only Crits", "\u0411\u044c\u0435\u0442 \u0432 \u043d\u0443\u0436\u043d\u044b\u0439 \u043c\u043e\u043c\u0435\u043d\u0442 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", false, () -> true);
        spaceOnly = new BooleanSetting("Space Only", "Only Crits \u0431\u0443\u0434\u0443\u0442 \u0440\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0435\u0441\u043b\u0438 \u0437\u0430\u0436\u0430\u0442 \u043f\u0440\u043e\u0431\u0435\u043b", false, () -> onlyCrit.getCurrentValue());
        adaptiveCrits = new BooleanSetting("Adaptive Crits", "\u0411\u044c\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0443\u0434\u0430\u0440\u043e\u043c \u0435\u0441\u043b\u0438 \u0443 \u043d\u0435\u0433\u043e \u043c\u0430\u043b\u043e \u0445\u043f \u0447\u0442\u043e \u0431\u044b \u0434\u043e\u0431\u0438\u0442\u044c", false, () -> onlyCrit.getCurrentValue());
        adaptiveCritsHealth = new NumberSetting("Adaptive Crits Health", "\u041a\u0430\u043a\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0445\u043f \u0431\u0443\u0434\u0435\u0442 \u0443 \u0438\u0433\u0440\u043e\u043a\u0430 \u0447\u0442\u043e \u0431\u044b \u043a\u0438\u043b\u043b\u0430\u0443\u0440\u0430 \u0431\u0438\u043b\u0430 \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0443\u0434\u0430\u0440\u043e\u043c (10 \u0445\u043f = 5 \u0441\u0435\u0440\u0434\u0435\u0446)", 5.0f, 0.5f, 10.0f, 0.5f, () -> onlyCrit.getCurrentValue() && adaptiveCrits.getCurrentValue());
        critFallDistance = new NumberSetting("Crits Fall Distance", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u0438 \u0434\u043e \u0437\u0435\u043c\u043b\u0438 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", 0.15f, 0.08f, 0.42f, 0.01f, () -> onlyCrit.getCurrentValue());
        this.addSettings(range, players, mobs, team, onlyCrit, spaceOnly, adaptiveCrits, adaptiveCritsHealth, critFallDistance);
    }

    public static boolean canTrigger(EntityLivingBase player) {
        for (Friend friend : Celestial.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) continue;
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && AntiBot.mode.currentMode.equals("Need Hit")) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player) && (AntiBot.mode.currentMode.equals("Matrix New") || AntiBot.mode.currentMode.equals("Matrix"))) {
            return false;
        }
        if (team.getCurrentValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getCurrentValue()) {
                return false;
            }
        }
        return player != TriggerBot.mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = TriggerBot.mc.objectMouseOver.entityHit;
        if (entity == null || TriggerBot.mc.player.getDistanceToEntity(entity) > range.getCurrentValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            return;
        }
        if (TriggerBot.canTrigger((EntityLivingBase)entity)) {
            boolean flag;
            TriggerBot.mc.player.jumpTicks = 0;
            BlockPos blockPos = new BlockPos(TriggerBot.mc.player.posX, TriggerBot.mc.player.posY - 0.1, TriggerBot.mc.player.posZ);
            Block block = TriggerBot.mc.world.getBlockState(blockPos).getBlock();
            float f2 = TriggerBot.mc.player.getCooledAttackStrength(0.5f);
            boolean bl = flag = f2 > 0.9f;
            if (!flag && onlyCrit.getCurrentValue()) {
                return;
            }
            if (!(adaptiveCrits.getCurrentValue() && ((EntityLivingBase)entity).getHealth() / 2.0f <= adaptiveCritsHealth.getCurrentValue() || Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState() && Flight.flyMode.currentMode.equals("Sunrise Disabler") || !TriggerBot.mc.gameSettings.keyBindJump.isKeyDown() && spaceOnly.getCurrentValue())) {
                if (MovementHelper.airBlockAboveHead()) {
                    if (!(TriggerBot.mc.player.fallDistance >= critFallDistance.getCurrentValue() || block instanceof BlockLiquid || !onlyCrit.getCurrentValue() || TriggerBot.mc.player.isRiding() || TriggerBot.mc.player.isOnLadder() || TriggerBot.mc.player.isInLiquid() || TriggerBot.mc.player.isInWeb)) {
                        TriggerBot.mc.player.connection.sendPacket(new CPacketEntityAction(TriggerBot.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                        return;
                    }
                } else if (!(!(TriggerBot.mc.player.fallDistance > 0.0f) || TriggerBot.mc.player.onGround || !onlyCrit.getCurrentValue() || TriggerBot.mc.player.isRiding() || TriggerBot.mc.player.isOnLadder() || TriggerBot.mc.player.isInLiquid() || TriggerBot.mc.player.isInWeb)) {
                    TriggerBot.mc.player.connection.sendPacket(new CPacketEntityAction(TriggerBot.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            }
            if (TriggerBot.mc.player.getCooledAttackStrength(0.0f) == 1.0f) {
                TriggerBot.mc.playerController.attackEntity(TriggerBot.mc.player, entity);
                TriggerBot.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

