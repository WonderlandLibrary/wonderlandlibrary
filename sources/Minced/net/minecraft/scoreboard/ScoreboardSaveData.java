// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.WorldSavedData;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger LOGGER;
    private Scoreboard scoreboard;
    private NBTTagCompound delayedInitNbt;
    
    public ScoreboardSaveData() {
        this("scoreboard");
    }
    
    public ScoreboardSaveData(final String name) {
        super(name);
    }
    
    public void setScoreboard(final Scoreboard scoreboardIn) {
        this.scoreboard = scoreboardIn;
        if (this.delayedInitNbt != null) {
            this.readFromNBT(this.delayedInitNbt);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        if (this.scoreboard == null) {
            this.delayedInitNbt = nbt;
        }
        else {
            this.readObjectives(nbt.getTagList("Objectives", 10));
            this.readScores(nbt.getTagList("PlayerScores", 10));
            if (nbt.hasKey("DisplaySlots", 10)) {
                this.readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
            }
            if (nbt.hasKey("Teams", 9)) {
                this.readTeams(nbt.getTagList("Teams", 10));
            }
        }
    }
    
    protected void readTeams(final NBTTagList tagList) {
        for (int i = 0; i < tagList.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            String s = nbttagcompound.getString("Name");
            if (s.length() > 16) {
                s = s.substring(0, 16);
            }
            final ScorePlayerTeam scoreplayerteam = this.scoreboard.createTeam(s);
            String s2 = nbttagcompound.getString("DisplayName");
            if (s2.length() > 32) {
                s2 = s2.substring(0, 32);
            }
            scoreplayerteam.setDisplayName(s2);
            if (nbttagcompound.hasKey("TeamColor", 8)) {
                scoreplayerteam.setColor(TextFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
            }
            scoreplayerteam.setPrefix(nbttagcompound.getString("Prefix"));
            scoreplayerteam.setSuffix(nbttagcompound.getString("Suffix"));
            if (nbttagcompound.hasKey("AllowFriendlyFire", 99)) {
                scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
            }
            if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99)) {
                scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
            }
            if (nbttagcompound.hasKey("NameTagVisibility", 8)) {
                final Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(nbttagcompound.getString("NameTagVisibility"));
                if (team$enumvisible != null) {
                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
            }
            if (nbttagcompound.hasKey("DeathMessageVisibility", 8)) {
                final Team.EnumVisible team$enumvisible2 = Team.EnumVisible.getByName(nbttagcompound.getString("DeathMessageVisibility"));
                if (team$enumvisible2 != null) {
                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible2);
                }
            }
            if (nbttagcompound.hasKey("CollisionRule", 8)) {
                final Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(nbttagcompound.getString("CollisionRule"));
                if (team$collisionrule != null) {
                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }
            }
            this.loadTeamPlayers(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
        }
    }
    
    protected void loadTeamPlayers(final ScorePlayerTeam playerTeam, final NBTTagList tagList) {
        for (int i = 0; i < tagList.tagCount(); ++i) {
            this.scoreboard.addPlayerToTeam(tagList.getStringTagAt(i), playerTeam.getName());
        }
    }
    
    protected void readDisplayConfig(final NBTTagCompound compound) {
        for (int i = 0; i < 19; ++i) {
            if (compound.hasKey("slot_" + i, 8)) {
                final String s = compound.getString("slot_" + i);
                final ScoreObjective scoreobjective = this.scoreboard.getObjective(s);
                this.scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
            }
        }
    }
    
    protected void readObjectives(final NBTTagList nbt) {
        for (int i = 0; i < nbt.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            final IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));
            if (iscorecriteria != null) {
                String s = nbttagcompound.getString("Name");
                if (s.length() > 16) {
                    s = s.substring(0, 16);
                }
                final ScoreObjective scoreobjective = this.scoreboard.addScoreObjective(s, iscorecriteria);
                scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
                scoreobjective.setRenderType(IScoreCriteria.EnumRenderType.getByName(nbttagcompound.getString("RenderType")));
            }
        }
    }
    
    protected void readScores(final NBTTagList nbt) {
        for (int i = 0; i < nbt.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            final ScoreObjective scoreobjective = this.scoreboard.getObjective(nbttagcompound.getString("Objective"));
            String s = nbttagcompound.getString("Name");
            if (s.length() > 40) {
                s = s.substring(0, 40);
            }
            final Score score = this.scoreboard.getOrCreateScore(s, scoreobjective);
            score.setScorePoints(nbttagcompound.getInteger("Score"));
            if (nbttagcompound.hasKey("Locked")) {
                score.setLocked(nbttagcompound.getBoolean("Locked"));
            }
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        if (this.scoreboard == null) {
            ScoreboardSaveData.LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return compound;
        }
        compound.setTag("Objectives", this.objectivesToNbt());
        compound.setTag("PlayerScores", this.scoresToNbt());
        compound.setTag("Teams", this.teamsToNbt());
        this.fillInDisplaySlots(compound);
        return compound;
    }
    
    protected NBTTagList teamsToNbt() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final ScorePlayerTeam scoreplayerteam : this.scoreboard.getTeams()) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Name", scoreplayerteam.getName());
            nbttagcompound.setString("DisplayName", scoreplayerteam.getDisplayName());
            if (scoreplayerteam.getColor().getColorIndex() >= 0) {
                nbttagcompound.setString("TeamColor", scoreplayerteam.getColor().getFriendlyName());
            }
            nbttagcompound.setString("Prefix", scoreplayerteam.getPrefix());
            nbttagcompound.setString("Suffix", scoreplayerteam.getSuffix());
            nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
            nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
            nbttagcompound.setString("NameTagVisibility", scoreplayerteam.getNameTagVisibility().internalName);
            nbttagcompound.setString("DeathMessageVisibility", scoreplayerteam.getDeathMessageVisibility().internalName);
            nbttagcompound.setString("CollisionRule", scoreplayerteam.getCollisionRule().name);
            final NBTTagList nbttaglist2 = new NBTTagList();
            for (final String s : scoreplayerteam.getMembershipCollection()) {
                nbttaglist2.appendTag(new NBTTagString(s));
            }
            nbttagcompound.setTag("Players", nbttaglist2);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }
    
    protected void fillInDisplaySlots(final NBTTagCompound compound) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        for (int i = 0; i < 19; ++i) {
            final ScoreObjective scoreobjective = this.scoreboard.getObjectiveInDisplaySlot(i);
            if (scoreobjective != null) {
                nbttagcompound.setString("slot_" + i, scoreobjective.getName());
                flag = true;
            }
        }
        if (flag) {
            compound.setTag("DisplaySlots", nbttagcompound);
        }
    }
    
    protected NBTTagList objectivesToNbt() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final ScoreObjective scoreobjective : this.scoreboard.getScoreObjectives()) {
            if (scoreobjective.getCriteria() != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Name", scoreobjective.getName());
                nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
                nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
                nbttagcompound.setString("RenderType", scoreobjective.getRenderType().getRenderType());
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        return nbttaglist;
    }
    
    protected NBTTagList scoresToNbt() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final Score score : this.scoreboard.getScores()) {
            if (score.getObjective() != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Name", score.getPlayerName());
                nbttagcompound.setString("Objective", score.getObjective().getName());
                nbttagcompound.setInteger("Score", score.getScorePoints());
                nbttagcompound.setBoolean("Locked", score.isLocked());
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        return nbttaglist;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
