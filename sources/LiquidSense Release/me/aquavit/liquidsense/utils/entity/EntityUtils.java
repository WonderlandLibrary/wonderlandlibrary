package me.aquavit.liquidsense.utils.entity;

import me.aquavit.liquidsense.module.modules.client.Target;
import me.aquavit.liquidsense.module.modules.blatant.NoFriends;
import me.aquavit.liquidsense.module.modules.misc.Teams;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.misc.AntiBot;
import me.aquavit.liquidsense.utils.render.ColorUtils;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class EntityUtils extends MinecraftInstance {

    public static boolean isSelected(final Entity entity, final boolean canAttackCheck, final boolean showself) {
        if(entity instanceof EntityLivingBase && (Target.dead.get() || entity.isEntityAlive()) && (showself || entity != mc.thePlayer)) {
            if(Target.invisible.get() || !entity.isInvisible()) {
                if(Target.player.get() && entity instanceof EntityPlayer) {
                    final EntityPlayer entityPlayer = (EntityPlayer) entity;

                    if(canAttackCheck) {
                        if(AntiBot.isBot(entityPlayer))
                            return false;

                        if (isFriend(entityPlayer) && !LiquidSense.moduleManager.getModule(NoFriends.class).getState())
                            return false;

                        if(entityPlayer.isSpectator())
                            return false;

                        final Teams teams = (Teams) LiquidSense.moduleManager.getModule(Teams.class);
                        return !teams.getState() || !Teams.isInYourTeam(entityPlayer);
                    }

                    return true;
                }

                return Target.mob.get() && isMob(entity) || Target.animal.get() && isAnimal(entity);

            }
        }
        return false;
    }

    public static boolean isFriend(final Entity entity) {
        return entity instanceof EntityPlayer && entity.getName() != null &&
                LiquidSense.fileManager.friendsConfig.isFriend(ColorUtils.stripColor(entity.getName()));
    }

    public static boolean isAnimal(final Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem ||
                entity instanceof EntityBat;
    }

    public static boolean isMob(final Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime ||
                entity instanceof EntityGhast || entity instanceof EntityDragon;
    }

    public static String getName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() :
                ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    public static int getPing(final EntityPlayer entityPlayer) {
        if(entityPlayer == null)
            return 0;

        final NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID());

        return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
    }
}
