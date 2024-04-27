/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;

public final class EntityUtils
extends MinecraftInstance {
    public static boolean targetInvisible = false;
    public static boolean targetPlayer = true;
    public static boolean targetMobs = true;
    public static boolean targetAnimals = false;
    public static boolean targetDead = false;

    public static boolean isSelected(IEntity entity, boolean canAttackCheck) {
        if (classProvider.isEntityLivingBase(entity) && (targetDead || entity.isEntityAlive()) && entity != null && !entity.equals(mc.getThePlayer()) && (targetInvisible || !entity.isInvisible())) {
            if (targetPlayer && classProvider.isEntityPlayer(entity)) {
                IEntityPlayer entityPlayer = entity.asEntityPlayer();
                if (canAttackCheck) {
                    if (AntiBot.isBot(entityPlayer)) {
                        return false;
                    }
                    if (EntityUtils.isFriend(entityPlayer) && !LiquidBounce.moduleManager.getModule(NoFriends.class).getState()) {
                        return false;
                    }
                    if (entityPlayer.isSpectator()) {
                        return false;
                    }
                    Teams teams = (Teams)LiquidBounce.moduleManager.getModule(Teams.class);
                    return !teams.getState() || !teams.isInYourTeam(entityPlayer);
                }
                return true;
            }
            return targetMobs && EntityUtils.isMob(entity) || targetAnimals && EntityUtils.isAnimal(entity);
        }
        return false;
    }

    public static boolean isFriend(IEntity entity) {
        return classProvider.isEntityPlayer(entity) && entity.getName() != null && LiquidBounce.fileManager.friendsConfig.isFriend(ColorUtils.stripColor(entity.getName()));
    }

    public static boolean isAnimal(IEntity entity) {
        return classProvider.isEntityAnimal(entity) || classProvider.isEntitySquid(entity) || classProvider.isEntityGolem(entity) || classProvider.isEntityBat(entity);
    }

    public static boolean isMob(IEntity entity) {
        return classProvider.isEntityMob(entity) || classProvider.isEntityVillager(entity) || classProvider.isEntitySlime(entity) || classProvider.isEntityGhast(entity) || classProvider.isEntityDragon(entity) || classProvider.isEntityShulker(entity);
    }

    public static String getName(INetworkPlayerInfo networkPlayerInfoIn) {
        if (networkPlayerInfoIn.getDisplayName() != null) {
            return networkPlayerInfoIn.getDisplayName().getFormattedText();
        }
        ITeam team = networkPlayerInfoIn.getPlayerTeam();
        String name = networkPlayerInfoIn.getGameProfile().getName();
        return team == null ? name : team.formatString(name);
    }

    public static int getPing(IEntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        INetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID());
        return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
    }
}

