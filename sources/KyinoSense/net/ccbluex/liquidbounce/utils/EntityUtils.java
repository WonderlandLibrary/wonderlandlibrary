/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.monster.EntityGolem
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.Teams;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class EntityUtils
extends MinecraftInstance {
    public static boolean targetInvisible = false;
    public static boolean targetPlayer = true;
    public static boolean targetMobs = true;
    public static boolean targetAnimals = false;
    public static boolean targetDead = false;

    public static boolean isSelected(Entity entity, boolean canAttackCheck) {
        if (entity instanceof EntityLivingBase && (targetDead || entity.func_70089_S()) && entity != EntityUtils.mc.field_71439_g && (targetInvisible || !entity.func_82150_aj())) {
            if (targetPlayer && entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                if (canAttackCheck) {
                    if (AntiBot.isBot((EntityLivingBase)entityPlayer)) {
                        return false;
                    }
                    if (EntityUtils.isFriend((Entity)entityPlayer) && !LiquidBounce.moduleManager.getModule(NoFriends.class).getState()) {
                        return false;
                    }
                    if (entityPlayer.func_175149_v()) {
                        return false;
                    }
                    Teams teams = (Teams)LiquidBounce.moduleManager.getModule(Teams.class);
                    return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entityPlayer);
                }
                return true;
            }
            return targetMobs && EntityUtils.isMob(entity) || targetAnimals && EntityUtils.isAnimal(entity);
        }
        return false;
    }

    public static boolean isFriend(Entity entity) {
        return entity instanceof EntityPlayer && entity.func_70005_c_() != null && LiquidBounce.fileManager.friendsConfig.isFriend(ColorUtils.stripColor(entity.func_70005_c_()));
    }

    public static boolean isEnemy(Entity entity) {
        return entity instanceof EntityPlayer && entity.func_70005_c_() != null && !LiquidBounce.fileManager.enemysConfig.isEnemy(ColorUtils.stripColor(entity.func_70005_c_()));
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityBat;
    }

    public static boolean isMob(Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }

    public static String getName(NetworkPlayerInfo networkPlayerInfoIn) {
        return networkPlayerInfoIn.func_178854_k() != null ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), (String)networkPlayerInfoIn.func_178845_a().getName());
    }

    public static int getPing(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        NetworkPlayerInfo networkPlayerInfo = mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au());
        return networkPlayerInfo == null ? 0 : networkPlayerInfo.func_178853_c();
    }
}

