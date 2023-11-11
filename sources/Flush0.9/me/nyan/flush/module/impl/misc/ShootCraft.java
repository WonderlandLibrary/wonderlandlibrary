package me.nyan.flush.module.impl.misc;

import com.google.common.collect.Lists;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.combat.CombatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShootCraft extends Module {
    private EntityLivingBase target;
    private int ticksPassed;

    public ShootCraft() {
        super("ShootCraft", Category.MISC);
    }

    @Override
    public void onEnable() {
        target = null;
        ticksPassed = 0;
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        ticksPassed++;
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ArrayList<Score> scores = scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))
                .stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toCollection(Lists::newArrayList));
        int found = 0;
        String[] strings = new String[] {"Temps restant:", "Kills:", "Top Kills:"};
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
            for (String s1 : strings) {
                if (s.contains(s1)) {
                    found++;
                    break;
                }
            }
        }

        if (found != strings.length) {
            return;
        }

        if (target == null) {
            List<Entity> entities = mc.theWorld.playerEntities
                    .stream()
                    .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                    .collect(Collectors.toList());

            for (Entity entity : entities) {
                if (!(entity instanceof EntityPlayer) || entity == mc.thePlayer || !mc.thePlayer.canEntityBeSeen(entity) ||
                        ((EntityPlayer) entity).hurtTime > 0 || entity.isDead || target.hurtResistantTime > 0) {
                    continue;
                }
                target = (EntityLivingBase) entity;
                ticksPassed = 0;
            }
        } else {
            if (!mc.thePlayer.canEntityBeSeen(target) || target.hurtTime > 0 || target.isDead || target.hurtResistantTime > 0) {
                target = null;
                return;
            }

            float[] rotations = CombatUtils.getRotations(target.posX, target.posY, target.posZ);
            e.setYaw(rotations[0]);
            e.setPitch(rotations[1]);

            if (ticksPassed >= 3 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() == Items.blaze_rod) {
                mc.playerController.syncCurrentPlayItem();
                for (int i = 0; i < 1; i++) {
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                }
                target = null;
                ticksPassed = 0;
            }
        }
    }
}