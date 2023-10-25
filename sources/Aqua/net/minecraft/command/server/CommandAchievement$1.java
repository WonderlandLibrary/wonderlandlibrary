package net.minecraft.command.server;

import com.google.common.base.Predicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;

class CommandAchievement.1
implements Predicate<Achievement> {
    final /* synthetic */ EntityPlayerMP val$entityplayermp;
    final /* synthetic */ StatBase val$statbase;

    CommandAchievement.1(EntityPlayerMP entityPlayerMP, StatBase statBase) {
        this.val$entityplayermp = entityPlayerMP;
        this.val$statbase = statBase;
    }

    public boolean apply(Achievement p_apply_1_) {
        return this.val$entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_) && p_apply_1_ != this.val$statbase;
    }
}
