package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.Team;

static final class PlayerSelector.5
implements Predicate<Entity> {
    final /* synthetic */ String val$s_f;
    final /* synthetic */ boolean val$flag;

    PlayerSelector.5(String string, boolean bl) {
        this.val$s_f = string;
        this.val$flag = bl;
    }

    public boolean apply(Entity p_apply_1_) {
        if (!(p_apply_1_ instanceof EntityLivingBase)) {
            return false;
        }
        EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
        Team team = entitylivingbase.getTeam();
        String s1 = team == null ? "" : team.getRegisteredName();
        return s1.equals((Object)this.val$s_f) != this.val$flag;
    }
}
