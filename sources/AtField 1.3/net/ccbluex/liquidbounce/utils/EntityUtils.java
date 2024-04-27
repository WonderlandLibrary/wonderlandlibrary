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

public final class EntityUtils
extends MinecraftInstance {
    public static boolean targetAnimals;
    public static boolean targetMobs;
    public static boolean targetDead;
    public static boolean targetInvisible;
    public static boolean targetPlayer;

    public static String getName(INetworkPlayerInfo iNetworkPlayerInfo) {
        if (iNetworkPlayerInfo.getDisplayName() != null) {
            return iNetworkPlayerInfo.getDisplayName().getFormattedText();
        }
        ITeam iTeam = iNetworkPlayerInfo.getPlayerTeam();
        String string = iNetworkPlayerInfo.getGameProfile().getName();
        return iTeam == null ? string : iTeam.formatString(string);
    }

    static {
        targetInvisible = false;
        targetPlayer = true;
        targetMobs = true;
        targetAnimals = false;
        targetDead = false;
    }

    public static int getPing(IEntityPlayer iEntityPlayer) {
        if (iEntityPlayer == null) {
            return 0;
        }
        INetworkPlayerInfo iNetworkPlayerInfo = mc.getNetHandler().getPlayerInfo(iEntityPlayer.getUniqueID());
        return iNetworkPlayerInfo == null ? 0 : iNetworkPlayerInfo.getResponseTime();
    }

    public static boolean isSelected(IEntity iEntity, boolean bl) {
        if (classProvider.isEntityLivingBase(iEntity) && (targetDead || iEntity.isEntityAlive()) && iEntity != null && !iEntity.equals(mc.getThePlayer()) && (targetInvisible || !iEntity.isInvisible())) {
            if (targetPlayer && classProvider.isEntityPlayer(iEntity)) {
                IEntityPlayer iEntityPlayer = iEntity.asEntityPlayer();
                if (bl) {
                    if (AntiBot.isBot(iEntityPlayer)) {
                        return false;
                    }
                    if (iEntityPlayer != false && !LiquidBounce.moduleManager.getModule(NoFriends.class).getState()) {
                        return false;
                    }
                    if (iEntityPlayer.isSpectator()) {
                        return false;
                    }
                    Teams teams = (Teams)LiquidBounce.moduleManager.getModule(Teams.class);
                    return !teams.getState() || !teams.isInYourTeam(iEntityPlayer);
                }
                return true;
            }
            return targetMobs && iEntity != false || targetAnimals && iEntity == false;
        }
        return false;
    }
}

