// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.server.MinecraftServer;

public class CommandResultStats
{
    private static final int NUM_RESULT_TYPES;
    private static final String[] STRING_RESULT_TYPES;
    private String[] entitiesID;
    private String[] objectives;
    
    public CommandResultStats() {
        this.entitiesID = CommandResultStats.STRING_RESULT_TYPES;
        this.objectives = CommandResultStats.STRING_RESULT_TYPES;
    }
    
    public void setCommandStatForSender(final MinecraftServer server, final ICommandSender sender, final Type typeIn, final int p_184932_4_) {
        final String s = this.entitiesID[typeIn.getTypeID()];
        if (s != null) {
            final ICommandSender icommandsender = new ICommandSender() {
                @Override
                public String getName() {
                    return sender.getName();
                }
                
                @Override
                public ITextComponent getDisplayName() {
                    return sender.getDisplayName();
                }
                
                @Override
                public void sendMessage(final ITextComponent component) {
                    sender.sendMessage(component);
                }
                
                @Override
                public boolean canUseCommand(final int permLevel, final String commandName) {
                    return true;
                }
                
                @Override
                public BlockPos getPosition() {
                    return sender.getPosition();
                }
                
                @Override
                public Vec3d getPositionVector() {
                    return sender.getPositionVector();
                }
                
                @Override
                public World getEntityWorld() {
                    return sender.getEntityWorld();
                }
                
                @Override
                public Entity getCommandSenderEntity() {
                    return sender.getCommandSenderEntity();
                }
                
                @Override
                public boolean sendCommandFeedback() {
                    return sender.sendCommandFeedback();
                }
                
                @Override
                public void setCommandStat(final Type type, final int amount) {
                    sender.setCommandStat(type, amount);
                }
                
                @Override
                public MinecraftServer getServer() {
                    return sender.getServer();
                }
            };
            String s2;
            try {
                s2 = CommandBase.getEntityName(server, icommandsender, s);
            }
            catch (CommandException var12) {
                return;
            }
            final String s3 = this.objectives[typeIn.getTypeID()];
            if (s3 != null) {
                final Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
                final ScoreObjective scoreobjective = scoreboard.getObjective(s3);
                if (scoreobjective != null && scoreboard.entityHasObjective(s2, scoreobjective)) {
                    final Score score = scoreboard.getOrCreateScore(s2, scoreobjective);
                    score.setScorePoints(p_184932_4_);
                }
            }
        }
    }
    
    public void readStatsFromNBT(final NBTTagCompound tagcompound) {
        if (tagcompound.hasKey("CommandStats", 10)) {
            final NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");
            for (final Type commandresultstats$type : Type.values()) {
                final String s = commandresultstats$type.getTypeName() + "Name";
                final String s2 = commandresultstats$type.getTypeName() + "Objective";
                if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s2, 8)) {
                    final String s3 = nbttagcompound.getString(s);
                    final String s4 = nbttagcompound.getString(s2);
                    setScoreBoardStat(this, commandresultstats$type, s3, s4);
                }
            }
        }
    }
    
    public void writeStatsToNBT(final NBTTagCompound tagcompound) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (final Type commandresultstats$type : Type.values()) {
            final String s = this.entitiesID[commandresultstats$type.getTypeID()];
            final String s2 = this.objectives[commandresultstats$type.getTypeID()];
            if (s != null && s2 != null) {
                nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
                nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s2);
            }
        }
        if (!nbttagcompound.isEmpty()) {
            tagcompound.setTag("CommandStats", nbttagcompound);
        }
    }
    
    public static void setScoreBoardStat(final CommandResultStats stats, final Type resultType, @Nullable final String entityID, @Nullable final String objectiveName) {
        if (entityID != null && !entityID.isEmpty() && objectiveName != null && !objectiveName.isEmpty()) {
            if (stats.entitiesID == CommandResultStats.STRING_RESULT_TYPES || stats.objectives == CommandResultStats.STRING_RESULT_TYPES) {
                stats.entitiesID = new String[CommandResultStats.NUM_RESULT_TYPES];
                stats.objectives = new String[CommandResultStats.NUM_RESULT_TYPES];
            }
            stats.entitiesID[resultType.getTypeID()] = entityID;
            stats.objectives[resultType.getTypeID()] = objectiveName;
        }
        else {
            removeScoreBoardStat(stats, resultType);
        }
    }
    
    private static void removeScoreBoardStat(final CommandResultStats resultStatsIn, final Type resultTypeIn) {
        if (resultStatsIn.entitiesID != CommandResultStats.STRING_RESULT_TYPES && resultStatsIn.objectives != CommandResultStats.STRING_RESULT_TYPES) {
            resultStatsIn.entitiesID[resultTypeIn.getTypeID()] = null;
            resultStatsIn.objectives[resultTypeIn.getTypeID()] = null;
            boolean flag = true;
            for (final Type commandresultstats$type : Type.values()) {
                if (resultStatsIn.entitiesID[commandresultstats$type.getTypeID()] != null && resultStatsIn.objectives[commandresultstats$type.getTypeID()] != null) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultStatsIn.entitiesID = CommandResultStats.STRING_RESULT_TYPES;
                resultStatsIn.objectives = CommandResultStats.STRING_RESULT_TYPES;
            }
        }
    }
    
    public void addAllStats(final CommandResultStats resultStatsIn) {
        for (final Type commandresultstats$type : Type.values()) {
            setScoreBoardStat(this, commandresultstats$type, resultStatsIn.entitiesID[commandresultstats$type.getTypeID()], resultStatsIn.objectives[commandresultstats$type.getTypeID()]);
        }
    }
    
    static {
        NUM_RESULT_TYPES = Type.values().length;
        STRING_RESULT_TYPES = new String[CommandResultStats.NUM_RESULT_TYPES];
    }
    
    public enum Type
    {
        SUCCESS_COUNT(0, "SuccessCount"), 
        AFFECTED_BLOCKS(1, "AffectedBlocks"), 
        AFFECTED_ENTITIES(2, "AffectedEntities"), 
        AFFECTED_ITEMS(3, "AffectedItems"), 
        QUERY_RESULT(4, "QueryResult");
        
        final int typeID;
        final String typeName;
        
        private Type(final int id, final String name) {
            this.typeID = id;
            this.typeName = name;
        }
        
        public int getTypeID() {
            return this.typeID;
        }
        
        public String getTypeName() {
            return this.typeName;
        }
        
        public static String[] getTypeNames() {
            final String[] astring = new String[values().length];
            int i = 0;
            for (final Type commandresultstats$type : values()) {
                astring[i++] = commandresultstats$type.getTypeName();
            }
            return astring;
        }
        
        @Nullable
        public static Type getTypeByName(final String name) {
            for (final Type commandresultstats$type : values()) {
                if (commandresultstats$type.getTypeName().equals(name)) {
                    return commandresultstats$type;
                }
            }
            return null;
        }
    }
}
