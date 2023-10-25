package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

/*
 * Exception performing whole class analysis ignored.
 */
public class CommandResultStats {
    private static final int NUM_RESULT_TYPES = Type.values().length;
    private static final String[] STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
    private String[] entitiesID = STRING_RESULT_TYPES;
    private String[] objectives = STRING_RESULT_TYPES;

    public void setCommandStatScore(ICommandSender sender, Type resultTypeIn, int scorePoint) {
        String s = this.entitiesID[resultTypeIn.getTypeID()];
        if (s != null) {
            Scoreboard scoreboard;
            ScoreObjective scoreobjective;
            String s1;
            1 icommandsender = new /* Unavailable Anonymous Inner Class!! */;
            try {
                s1 = CommandBase.getEntityName((ICommandSender)icommandsender, (String)s);
            }
            catch (EntityNotFoundException var11) {
                return;
            }
            String s2 = this.objectives[resultTypeIn.getTypeID()];
            if (s2 != null && (scoreobjective = (scoreboard = sender.getEntityWorld().getScoreboard()).getObjective(s2)) != null && scoreboard.entityHasObjective(s1, scoreobjective)) {
                Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                score.setScorePoints(scorePoint);
            }
        }
    }

    public void readStatsFromNBT(NBTTagCompound tagcompound) {
        if (tagcompound.hasKey("CommandStats", 10)) {
            NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");
            for (Type commandresultstats$type : Type.values()) {
                String s = commandresultstats$type.getTypeName() + "Name";
                String s1 = commandresultstats$type.getTypeName() + "Objective";
                if (!nbttagcompound.hasKey(s, 8) || !nbttagcompound.hasKey(s1, 8)) continue;
                String s2 = nbttagcompound.getString(s);
                String s3 = nbttagcompound.getString(s1);
                CommandResultStats.setScoreBoardStat(this, commandresultstats$type, s2, s3);
            }
        }
    }

    public void writeStatsToNBT(NBTTagCompound tagcompound) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (Type commandresultstats$type : Type.values()) {
            String s = this.entitiesID[commandresultstats$type.getTypeID()];
            String s1 = this.objectives[commandresultstats$type.getTypeID()];
            if (s == null || s1 == null) continue;
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s1);
        }
        if (!nbttagcompound.hasNoTags()) {
            tagcompound.setTag("CommandStats", (NBTBase)nbttagcompound);
        }
    }

    public static void setScoreBoardStat(CommandResultStats stats, Type resultType, String entityID, String objectiveName) {
        if (entityID != null && entityID.length() != 0 && objectiveName != null && objectiveName.length() != 0) {
            if (stats.entitiesID == STRING_RESULT_TYPES || stats.objectives == STRING_RESULT_TYPES) {
                stats.entitiesID = new String[NUM_RESULT_TYPES];
                stats.objectives = new String[NUM_RESULT_TYPES];
            }
            stats.entitiesID[resultType.getTypeID()] = entityID;
            stats.objectives[resultType.getTypeID()] = objectiveName;
        } else {
            CommandResultStats.removeScoreBoardStat(stats, resultType);
        }
    }

    private static void removeScoreBoardStat(CommandResultStats resultStatsIn, Type resultTypeIn) {
        if (resultStatsIn.entitiesID != STRING_RESULT_TYPES && resultStatsIn.objectives != STRING_RESULT_TYPES) {
            resultStatsIn.entitiesID[resultTypeIn.getTypeID()] = null;
            resultStatsIn.objectives[resultTypeIn.getTypeID()] = null;
            boolean flag = true;
            for (Type commandresultstats$type : Type.values()) {
                if (resultStatsIn.entitiesID[commandresultstats$type.getTypeID()] == null || resultStatsIn.objectives[commandresultstats$type.getTypeID()] == null) continue;
                flag = false;
                break;
            }
            if (flag) {
                resultStatsIn.entitiesID = STRING_RESULT_TYPES;
                resultStatsIn.objectives = STRING_RESULT_TYPES;
            }
        }
    }

    public void addAllStats(CommandResultStats resultStatsIn) {
        for (Type commandresultstats$type : Type.values()) {
            CommandResultStats.setScoreBoardStat(this, commandresultstats$type, resultStatsIn.entitiesID[commandresultstats$type.getTypeID()], resultStatsIn.objectives[commandresultstats$type.getTypeID()]);
        }
    }
}
