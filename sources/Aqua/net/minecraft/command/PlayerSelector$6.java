package net.minecraft.command;

import com.google.common.base.Predicate;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

static final class PlayerSelector.6
implements Predicate<Entity> {
    final /* synthetic */ Map val$map;

    PlayerSelector.6(Map map) {
        this.val$map = map;
    }

    public boolean apply(Entity p_apply_1_) {
        Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
        for (Map.Entry entry : this.val$map.entrySet()) {
            String s1;
            ScoreObjective scoreobjective;
            String s = (String)entry.getKey();
            boolean flag = false;
            if (s.endsWith("_min") && s.length() > 4) {
                flag = true;
                s = s.substring(0, s.length() - 4);
            }
            if ((scoreobjective = scoreboard.getObjective(s)) == null) {
                return false;
            }
            String string = s1 = p_apply_1_ instanceof EntityPlayerMP ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
            if (!scoreboard.entityHasObjective(s1, scoreobjective)) {
                return false;
            }
            Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
            int i = score.getScorePoints();
            if (i < (Integer)entry.getValue() && flag) {
                return false;
            }
            if (i <= (Integer)entry.getValue() || flag) continue;
            return false;
        }
        return true;
    }
}
