/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.EnumChatFormatting;

public class GoalColor
implements IScoreObjectiveCriteria {
    private final String goalName;

    public GoalColor(String string, EnumChatFormatting enumChatFormatting) {
        this.goalName = String.valueOf(string) + enumChatFormatting.getFriendlyName();
        IScoreObjectiveCriteria.INSTANCES.put(this.goalName, this);
    }

    @Override
    public int func_96635_a(List<EntityPlayer> list) {
        return 0;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
        return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
    }

    @Override
    public String getName() {
        return this.goalName;
    }
}

